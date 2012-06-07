package com.travelsky.rmilbclient;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author zhongfeng
 * 
 */
public class RmiProxyFactoryBean implements FactoryBean {

	private Object serviceProxy;

	private RmiLbServiceConfig<?> config;

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

}
