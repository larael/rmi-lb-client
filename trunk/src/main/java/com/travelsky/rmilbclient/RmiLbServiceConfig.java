package com.travelsky.rmilbclient;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhongfeng
 * 
 */
public class RmiLbServiceConfig<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 31328307955440178L;

	/**
	 * RMI Service Url 列表，对等服务
	 */
	private List<String> serviceUrls;

	/**
	 * service interface
	 */
	private Class<E> serviceInterface;

	/**
	 * 是否启动时初始化stub
	 */
	private boolean lookupStubOnStartup = false;

	/**
	 * monitor 检测 间隔
	 */
	private long monitorPeriod = StubMonitorService.DEFAULF_PERIOD;

	/**
	 * 
	 */
	public RmiLbServiceConfig() {
	}

	/**
	 * @param serviceUrls
	 * @param serviceInterface
	 * @param lookupStubOnStartup
	 * @param monitorPeriod
	 */
	public RmiLbServiceConfig(List<String> serviceUrls,
			Class<E> serviceInterface) {
		this(serviceUrls, serviceInterface, false,
				StubMonitorService.DEFAULF_PERIOD);
	}

	/**
	 * @param serviceUrls
	 * @param serviceInterface
	 * @param lookupStubOnStartup
	 * @param monitorPeriod
	 */
	public RmiLbServiceConfig(List<String> serviceUrls,
			Class<E> serviceInterface, boolean lookupStubOnStartup,
			long monitorPeriod) {
		setServiceUrls(serviceUrls);
		setServiceInterface(serviceInterface);
		this.lookupStubOnStartup = lookupStubOnStartup;
		this.monitorPeriod = monitorPeriod;
	}

	public List<String> getServiceUrls() {
		return serviceUrls;
	}

	public void setServiceUrls(List<String> serviceUrls) {
		if (serviceUrls == null || serviceUrls.isEmpty())
			throw new IllegalArgumentException(
					" 'serviceUrls' must not be null or empty");
		this.serviceUrls = serviceUrls;
	}

	public Class<E> getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class<E> serviceInterface) {
		if (serviceInterface != null && !serviceInterface.isInterface()) {
			throw new IllegalArgumentException(
					"'serviceInterface' must be an interface");
		}
		this.serviceInterface = serviceInterface;
	}

	public boolean isLookupStubOnStartup() {
		return lookupStubOnStartup;
	}

	public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
		this.lookupStubOnStartup = lookupStubOnStartup;
	}

	public long getMonitorPeriod() {
		return monitorPeriod;
	}

	public void setMonitorPeriod(long monitorPeriod) {
		this.monitorPeriod = monitorPeriod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serviceUrls == null) ? 0 : serviceUrls.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RmiLbServiceConfig other = (RmiLbServiceConfig) obj;
		if (serviceUrls == null) {
			if (other.serviceUrls != null)
				return false;
		} else if (!serviceUrls.equals(other.serviceUrls))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RmiLbServiceConfig [lookupStubOnStartup=" + lookupStubOnStartup
				+ ", monitorPeriod=" + monitorPeriod + ", serviceInterface="
				+ serviceInterface + ", serviceUrls=" + serviceUrls + "]";
	}

}
