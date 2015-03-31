package com.filetrans.net;

/**
 * Connetor发生变化监听器
 * @author Administrator
 *
 */
public interface IConnetorChangeListener {
	
	/**
	 * 断开连接事件
	 */
	public final static int EVENT_ID_OnDisconnet = 1;
	/**
	 * 建立连接成功事件
	 */
	public final static int EVENT_ID_OnConneted = 2;
	
	/**
	 * 监听状态变化
	 * @param eventId
	 * @param obj
	 */
	public void OnChange(int eventId,Object obj);
	

}
