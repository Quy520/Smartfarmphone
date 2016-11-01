package com.smartfarm.bean;

import com.smartfarm.msg.Protocol;

public class LightInfoBean {

	private boolean[] states;
	private int light;
	private boolean mode;
	
	public LightInfoBean(Protocol protocol) {
		
		states = new boolean[4];
		
		String[] data = protocol.getData().split("L");
		
		int index = 0;
		for(int i = 1; i < data.length; i++) {
			
			String value = null;
			
			try {
				value = data[i].substring(data[i].indexOf("=") + 1, data[i].length() - 1);
				
				states[index++] = Boolean.valueOf(value);

			} catch (Exception e) {
				
				if(data[i].contains("mode"))
					mode = value.contains("true");
				else if(data[i].contains("value"))
					light = Integer.valueOf(value);
			}
		}
	}
	
	public int getValue() {
		
		return light;
	}
	
	public boolean getMode() {
		
		return mode;
	}
	
	public boolean[] getStates() {
		
		return states;
	}
}
