package com.travelsky.rmilbclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelsky.rmilbclient.exceptions.RmiExceptionUtils;


/**
 * @author zhongfeng
 * 
 */
public class RmiProxy implements InvocationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(RmiProxy.class);

	private StubManager stubMgr;

	/**
	 * @param stubMgr
	 */
	public RmiProxy(StubManager stubMgr) {
		this.stubMgr = stubMgr;
	}

	public Object invoke(Object proxyObj, Method method, Object[] args)
			throws Throwable {
		try {
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

	public StubManager getStubMgr() {
		return stubMgr;
	}

	public void setStubMgr(StubManager stubMgr) {
		this.stubMgr = stubMgr;
	}

}
