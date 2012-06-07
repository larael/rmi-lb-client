package com.travelsky.rmilbclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhongfeng
 * 
 */
public class RmiProxyFactory {

	protected static Logger logger = LoggerFactory
			.getLogger(RmiProxyFactory.class.getName());

	/**
	 * single instance
	 */
	private final static RmiProxyFactory m_instance = new RmiProxyFactory();

	/**
	 * Creates the new proxy factory.
	 */
	private RmiProxyFactory() {
	}

	/**
	 * Creates a new proxy with the specified URL. The returned object is a
	 * proxy with the interface specified by api.
	 * 
	 * <pre>
	 * List&lt;String&gt; serviceUrls = new ArrayList&lt;String&gt;();
	 * 
	 * serviceUrls.add(rmi://localhost:8098/CalculatorRMIService);
	 * serviceUrls.add(rmi://localhost:8099/CalculatorRMIService);
	 * 
	 * RmiLbServiceConfig<CalculatorService> config = new RmiLbServiceConfig(serviceUrls,
	 * 		CalculatorService.class);
	 * 
	 * RmiProxyFactory factory = RmiProxyFactory.getInstance();
	 * 
	 * CalculatorService as = factory.create(config);
	 * 
	 * </pre>
	 * 
	 * 
	 * @return a proxy to the object with the specified interface.
	 */
	public <T> T create(RmiLbServiceConfig<T> config, ClassLoader loader) {

		StubManager stubMgr = StubManagerBuilder
				.buildStubManager(config);
		// start monitor
		StubMonitorService monitorService = new StubMonitorService(config
				.getMonitorPeriod(), stubMgr);
		monitorService.startMonitor();
		//create proxy
		InvocationHandler handler = new RmiProxy(stubMgr);
		Class<T> api = config.getServiceInterface();
		Object proxyObj = Proxy.newProxyInstance(loader, new Class[] {api}, handler);

		return api.cast(proxyObj);
	}

	public <T> T create(RmiLbServiceConfig<T> config) {
		return create(config, Thread.currentThread().getContextClassLoader());
	}

	public static RmiProxyFactory getInstance() {
		return m_instance;
	}

}
