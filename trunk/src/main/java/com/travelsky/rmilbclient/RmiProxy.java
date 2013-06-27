package com.travelsky.rmilbclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelsky.rmilbclient.exceptions.RmiExceptionUtils;
import com.travelsky.rmilbclient.exceptions.RmiInvokeAsynExecuteException;
import com.travelsky.rmilbclient.exceptions.RmiInvokeTimeoutException;

/**
 * @author zhongfeng
 * 
 */
public class RmiProxy implements InvocationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(RmiProxy.class);

	private StubManager stubMgr;

	private long timeout = -1;

	private final static ExecutorService EXEC_ASYN_SERVICE = Executors
			.newCachedThreadPool();

	/**
	 * @param stubMgr
	 */
	public RmiProxy(StubManager stubMgr) {
		this(stubMgr, -1);
	}

	/**
	 * @param stubMgr
	 * @param timeout
	 */
	public RmiProxy(StubManager stubMgr, long timeout) {
		this.stubMgr = stubMgr;
		this.timeout = timeout;
	}

	public Object invoke(Object proxyObj, Method method, Object[] args)
			throws Throwable {
		try {
			if (getTimeout() > 0) {
				return invokeAsyn(method,args);
			}
			return doInvoke(method, args);
		} catch (Throwable ex) {
			if (RmiExceptionUtils.isConnectFailure(ex)) {
				return handleRemoteConnectFailure(method, args, ex);
			} else {
				throw ex;
			}
		}
	}

	private Object handleRemoteConnectFailure(Method m, Object[] args,
			Throwable ex) throws Throwable {
		if (logger.isDebugEnabled())
			logger.debug("Could not connect to RMI service - retrying", ex);
		getStubMgr().updateStubsStatus();
		return doInvoke(m, args);
	}

	private Object doInvoke(Method method, Object[] args) throws Throwable {
		Object ret = null;
		Object stub = getStubMgr().getStub();
		if (stub != null)
			try {
				ret = method.invoke(stub, args);
			} catch (Throwable e) {
				throw e;
			}
		return ret;
	}

	private Object invokeAsyn(final Method method, final Object[] args) {
		Future<Object> futureTask = EXEC_ASYN_SERVICE
				.submit(new Callable<Object>() {
					public Object call() throws Exception {
						try {
							return doInvoke(method, args);
						} catch (Throwable e) {
							throw new Exception(e);
						}
					}
				});
		try {
			return futureTask.get(getTimeout(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RmiInvokeAsynExecuteException(e);
		} catch (ExecutionException e) {
			throw new RmiInvokeAsynExecuteException(e);
		} catch (TimeoutException e) {
			throw new RmiInvokeTimeoutException(e);
		} finally {
			futureTask.cancel(true);
		}
	}

	public StubManager getStubMgr() {
		return stubMgr;
	}

	public void setStubMgr(StubManager stubMgr) {
		this.stubMgr = stubMgr;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
