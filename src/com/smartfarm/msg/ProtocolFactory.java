package com.smartfarm.msg;

import com.smartfarm.bean.TempConfigModel;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.XmlUtils;
import com.smartfarm.view.AppContext;



public class ProtocolFactory {

	public static Protocol getLightInfoProtocol() {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_LIGHT_READ);
		
		return protocol;
	}
	
	public static Protocol getReadLightInfoProtocol() {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_LIGHT_READ);
		protocol.setWindowId("btn");
		
		return protocol;
	}
	
	public static Protocol getWaterStateProtocol() {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_STATE);
		protocol.setWindowId("get");
		
		return protocol;
	}
	
	public static Protocol getWaterInfoProtocol(boolean refresh) {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_READ);
		protocol.setWindowId(refresh ? "btn" : "");

		return protocol;
	}
	
	public static Protocol getReadOtherInfoProtocol(boolean refresh) {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_READ_OTHERS);
		protocol.setWindowId(refresh ? "btn" : "");

		return protocol;
	}
	
	public static Protocol getWaterOpenProtocol(String ids) {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_OPEN);
		protocol.setWindowId(ids);

		return protocol;
	}
	
	public static Protocol getLightOpenProtocol(String ids) {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_LIGHT_OPEN);
		protocol.setWindowId(ids);

		return protocol;
	}
	
	public static Protocol getLightCloseProtocol(String ids) {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_LIGHT_CLOSE);
		protocol.setWindowId(ids);

		return protocol;
	}
	
	public static Protocol getWaterRumpOpenProtocol() {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_OPEN);
		protocol.setWindowId("rump");
		
		return protocol;
	}
	
	public static Protocol getWaterYaoOpenProtocol() {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_OPEN);
		protocol.setWindowId("yao");
		
		return protocol;
	}
	
	public static Protocol getWaterOnekeyOpenProtocol(){
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_OPEN);
		protocol.setWindowId("onekey");
		
		return protocol;
	}
	
	public static Protocol getWaterCloseProtocol(String ids) {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE);
		protocol.setWindowId(ids);

		return protocol;
	}
	
	public static Protocol getWaterRumpCloseProtocol() {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE);
		protocol.setWindowId("rump");

		return protocol;
	}
	
	public static Protocol getWaterYaoCloseProtocol() {
		
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE);
		protocol.setWindowId("yao");

		return protocol;
	}
	
	public static Protocol getWaterOnekeyCloseProtocol(){
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE);
		protocol.setWindowId("onekey");
		
		return protocol;
	}

	
	public static Protocol getWaterModeProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_MODE);
		protocol.setWindowId("get");
		
		return protocol;
	}
	
	public static Protocol getSetWaterModeProtocol(boolean isToAuto) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_MODE);
		protocol.setData(isToAuto ? "auto" : "no");
		
		return protocol;
	}
	
	public static Protocol getSetLightModeProtocol(boolean isToAuto) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_LIGHT_MODE);
		protocol.setData(isToAuto ? "auto" : "no");
		
		return protocol;
	}
	
	public static Protocol getSynWaterDataProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_CONFIG);
		protocol.setWindowId("get");
		
		return protocol;
	}
	
	public static Protocol getSynLightDataProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_LIGHT_CONFIG);
		protocol.setWindowId("get");
		
		return protocol;
	}
	
	public static Protocol getSynLightDataProtocol(String data) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_LIGHT_CONFIG);
		protocol.setData(data);
		
		return protocol;
	}
	
	public static Protocol getSynWaterDataProtocol(String data) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_CONFIG);
		protocol.setData(data);
		
		return protocol;
	}
	
	public static Protocol GetReadTempsProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_READ);
		
		return protocol;
	}
	
	public static Protocol GetNoticeProtocol(String cmd) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_NOTICE);
		protocol.setData(cmd);
		
		return protocol;
	}
	
	public static Protocol GetModeProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_MODE);
		
		return protocol;
	}
	
	public static Protocol GetTestProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_TEST);
		
		return protocol;
	}
	
	public static Protocol GetTestSelfProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_TEST);
		protocol.setReceiver(AppContext.context().getUser() != null 
				? AppContext.context().getUser().getPhone() : "");
		
		return protocol;
	}
	
	public static Protocol GetTestProtocol(int padId) {
		Protocol protocol = new Protocol(padId);
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_TEST);
		
		return protocol;
	}
	
	public static Protocol GetVentilationProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_OPEN);
		protocol.setWindowId("ven");
		
		return protocol;
	}
	
	public static Protocol GetControlsProtocol(int controlType, int...destWindows) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		
		StringBuilder ids = new StringBuilder();
		for(int id : destWindows) {
			ids.append(id);
			ids.append(";");
		}
		ids.delete(ids.length() - 2, ids.length() - 1);
		
		protocol.setWindowId(ids.toString());
		protocol.setCmdType(controlType);
		
		return protocol;
	}
	
	public static Protocol GetControlsProtocol(int controlType, String destWindows) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setWindowId(destWindows);
		protocol.setCmdType(controlType);
		
		return protocol;
	}
	
	public static Protocol GetCloseAllWindowProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_CLOSE);
		protocol.setWindowId("all");
		
		return protocol;
	}
	
	public static Protocol GetSetTheModeProtocol(boolean isToAuto) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_MODE);
		protocol.setData(isToAuto ? "auto" : "no");
		
		return protocol;
	}
	/*
	 * 高温转自动
	 */
	public static Protocol GetSetTheHighAutoModeProtocol(boolean isToAuto){
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_HIGH_MODE);
		protocol.setData(isToAuto ? "auto" : "no");
		
		return protocol;
	}
	//下雨关风
	public static Protocol GetSetTheRainModeProtocol(boolean isRain){
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_RAIN_MODE);
		protocol.setData(isRain ? "rain" : "stop");
		
		return protocol;
	}
	
	//一键放风
	public static Protocol GetSetTheWindowOpen(boolean isRain){
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE);
		protocol.setData(isRain ? "wind" : "stop");
		
		return protocol;
	}
	
	public static Protocol GetSynDataProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_SYN);
		protocol.setWindowId("get");
		
		return protocol;
	}
	
	public static Protocol getSynDataProtocol(String data) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_SYN);
		protocol.setData(data);
		
		return protocol;
	}
	
	public static Protocol getSynTempProtocol() {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_SYN_TEMP);
		protocol.setData(XmlUtils.toXml(new TempConfigModel(AppContext.context().getCurrPengInfo())));
		
		return protocol;
	}

	public static Protocol GetProtocol(String protocol) {
		Protocol res = new Protocol();
		
		if(protocol != null && protocol.contains("-")) {
			String[] data = protocol.split("-", 8);

			if(data.length < 7)
				return null;
			
			try {
				
				res.setTime(Long.parseLong(data[0]));
				res.setSender(data[1]);
				res.setReceiver(data[2]);
				res.setProtocolType(Integer.parseInt(data[3]));
				res.setCmdType(Integer.parseInt(data[4]));
				res.setWindowId(data[5]);
				res.setData(data[6]);
				
				if(data.length == 8)
					res.setPwd(data[7]);
				
				return res;
				
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
}
