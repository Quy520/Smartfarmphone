package com.smartfarm.bean;

/**
 * 本地消息事件
 * 
 * @author jeff
 *
 */
public class LocalEvent {

	/**
	 * 收到温度消息
	 */
	public static final int EVENT_TYPE_TEMP = 101;

	/**
	 * 接收到网络消息
	 */
	public static final int EVENT_TYPE_RECEIVE_MSG = EVENT_TYPE_TEMP + 1;

	/**
	 * 测试消息未响应
	 */
	public static final int EVENT_TYPE_TEST_ERROR = EVENT_TYPE_RECEIVE_MSG + 1;

	/**
	 * 失去网络连接
	 */
	public static final int EVENT_TYPE_NET_DOWN = EVENT_TYPE_TEST_ERROR + 1;

	/**
	 * 连接上网络
	 */
	public static final int EVENT_TYPE_NET_OK = EVENT_TYPE_NET_DOWN + 1;

	/**
	 * 温度模式变更
	 */
	public static final int EVENT_TYPE_MODE_CHANGE = EVENT_TYPE_NET_OK + 1;

	/**
	 * 配置信息变更
	 */
	public static final int EVENT_TYPE_CONFIG_CHANGE = EVENT_TYPE_MODE_CHANGE + 1;

	/**
	 * 大棚信息变更
	 */
	public static final int EVENT_TYPE_PENG_CHANGE = EVENT_TYPE_CONFIG_CHANGE + 1;

	/**
	 * 大棚切换
	 */
	public static final int EVENT_TYPE_PENG_SWITCHING = EVENT_TYPE_PENG_CHANGE + 1;

	/**
	 * 水自一体状态变化
	 */
	public static final int EVENT_TYPE_WATER_STATE = EVENT_TYPE_PENG_SWITCHING + 1;

	/**
	 * 水自一体模式变化
	 */
	public static final int EVENT_TYPE_WATER_MODE = EVENT_TYPE_WATER_STATE + 1;

	/**
	 * 水自一体参数变化
	 */
	public static final int EVENT_TYPE_WATER_CONFIG = EVENT_TYPE_WATER_MODE + 1;

	/**
	 * 光照度模式变化
	 */
	public static final int EVENT_TYPE_LIGHT_MODE = EVENT_TYPE_WATER_CONFIG + 1;

	/**
	 * 光照度参数变化
	 */
	public static final int EVENT_TYPE_LIGHT_CONFIG = EVENT_TYPE_LIGHT_MODE + 1;

	/**
	 * 收到湿度信息
	 */
	public static final int EVENT_TYPE_WATER_READ = EVENT_TYPE_LIGHT_CONFIG + 1;

	/**
	 * 收到照度信息
	 */
	public static final int EVENT_TYPE_LIGHT_READ = EVENT_TYPE_WATER_READ + 1;

	/**
	 * 发布消息成功
	 */
	public static final int EVENT_TYPE_PUB_MSG = EVENT_TYPE_LIGHT_READ + 1;

	/**
	 * 收到照度信息
	 */
	public static final int EVENT_TYPE_OTHER_READ = EVENT_TYPE_PUB_MSG + 1;

	/**
	 * 高温切自动
	 */
	public static final int EVENT_TYPE_AUTO_OPEN_ENABLE = EVENT_TYPE_OTHER_READ + 1;
	//下雨关风
	public static final int EVENT_TYPE_IS_RAIN = EVENT_TYPE_AUTO_OPEN_ENABLE + 1;
	//一键放风
	public static final int EVENT_TYPE_OPEN_WINDOWS = EVENT_TYPE_IS_RAIN + 1;
	
	public static final int EVENT_TYPE_OPEN_WINDS_END = EVENT_TYPE_OPEN_WINDOWS + 1;

	private int eventType;
	private Object eventData;
	private String eventMsg;
	private int eventValue;

	public static LocalEvent getEvent(int eventType) {

		return getEvent(eventType, -1, "", null);
	}

	public static LocalEvent getEvent(int eventType, String eventMsg) {

		return getEvent(eventType, -1, eventMsg, null);
	}

	public static LocalEvent getEvent(int eventType, int eventValue) {

		return getEvent(eventType, eventValue, "", null);
	}

	public static LocalEvent getEvent(int eventType, int eventValue,
			String eventMsg) {

		return getEvent(eventType, eventValue, eventMsg, null);
	}

	public static LocalEvent getEvent(int eventType, int eventValue,
			String eventMsg, Object eventData) {

		LocalEvent event = new LocalEvent();

		event.eventMsg = eventMsg;
		event.eventType = eventType;
		event.eventData = eventData;
		event.eventValue = eventValue;

		return event;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public Object getEventData() {
		return eventData;
	}

	public void setEventData(Object eventData) {
		this.eventData = eventData;
	}

	public String getEventMsg() {
		return eventMsg;
	}

	public void setEventMsg(String eventMsg) {
		this.eventMsg = eventMsg;
	}

	public int getEventValue() {
		return eventValue;
	}

	public void setEventValue(int eventValue) {
		this.eventValue = eventValue;
	}

}
