package com.travelsky.rmilbclient.schedule;

/**
 * @author zhongfeng
 *
 * @param <E>
 */
public interface LbSchedule <E> {

	public E nextHost();
}
