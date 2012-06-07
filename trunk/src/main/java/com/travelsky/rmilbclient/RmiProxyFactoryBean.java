package com.travelsky.rmilbclient;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author zhongfeng
 * 
 */
public class RmiProxyFactoryBean implements FactoryBean {

	private Object serviceProxy;

	private RmiLbServiceConfig<?> config;
	
	/**
	 * 
	 */
	public RmiProxyFactoryBean() {
	}

	/**
	 * @param config
	 */
	public RmiProxyFactoryBean(RmiLbServiceConfig<?> config) {
		this.config = config;
	}

	public Object getObject() {
		RmiProxyFactory factory = RmiProxyFactory.getInstance();
		serviceProxy = factory.create(config);
		return serviceProxy;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return config.getServiceInterface();
	}

	public boolean isSingleton() {
		return true;
	}

	public RmiLbServiceConfig<?> getConfig() {
		return config;
	}

	public void setConfig(RmiLbServiceConfig<?> config) {
		this.config = config;
	}

}
