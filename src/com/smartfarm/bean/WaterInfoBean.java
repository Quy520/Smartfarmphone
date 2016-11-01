package com.smartfarm.bean;

import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.msg.Protocol;


public class WaterInfoBean {

	private int[] values;
	private boolean[] states;
	private float[] temps;
	private boolean mode;
	private boolean rump;
	private boolean yao;
	private boolean onekey;
	
	private int id;
	private long time;
	private String sourceData;
	
	public WaterInfoBean(int pengId, long time, String waterData) {
		
		id = pengId;
		this.time = time;
		sourceData = waterData;
		
		values = new int[9];
		states = new boolean[9];
		temps = new float[9];
		String[] data = waterData.split("W");
		
		int index = 0;
		for(int i = 1; i < data.length; i++) {
			
			String key = null; 
			String value = null;
			
			try {
				key = data[i].substring(data[i].indexOf("=") + 1, data[i].indexOf(":"));
				value = data[i].substring(data[i].indexOf(":") + 1, data[i].indexOf("&"));
				String temp = data[i].substring(data[i].indexOf("&") + 1, data[i].indexOf(";"));
				
				values[index] = Integer.valueOf(key);
				states[index] = Boolean.valueOf(value);
				temps[index++] = Float.valueOf(temp);

			} catch (Exception e) {

				if(key == null || value == null) {
					key = data[i];
					value = data[i].substring(data[i].indexOf("=") + 1, data[i].length() - 1);
				}
				
				if(key.contains("mode"))
					mode = !value.contains("no");
				else if(key.contains("rump"))
					rump = value.contains("true");
				else if(key.contains("yao"))
					yao = value.contains("true");
				else if(key.contains("onekey"))
					onekey = value.contains("true");
			}
		}
	}
	
	public String getSourceData() {
		return sourceData;
	}

	public void setSourceData(String sourceData) {
		this.sourceData = sourceData;
	}

	public WaterInfoBean(InfoRecord record) {
		
		this(record.getPengId(), record.getTime(), record.getData());
	}
	
	public WaterInfoBean(Protocol waterProtocol) {
		
		this(waterProtocol.getPadId(), waterProtocol.getTime(), waterProtocol.getData());
	}
	
	public int getId() {
		return id;
	}

	public long getTime() {
		return time;
	}

	public int[] getValues() {
		return values;
	}

	public float[] getTemps() {
		return temps;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	public boolean[] getStates() {
		return states;
	}

	public void setStates(boolean[] states) {
		this.states = states;
	}

	public boolean isRump() {
		return rump;
	}

	public void setRump(boolean rump) {
		this.rump = rump;
	}

	public boolean isMode() {
		return mode;
	}

	public void setMode(boolean mode) {
		this.mode = mode;
	}

	public boolean isYao() {
		return yao;
	}

	public void setYao(boolean yao) {
		this.yao = yao;
	}

	public boolean isOnekey() {
		return onekey;
	}

	public void setOnekey(boolean onekey) {
		this.onekey = onekey;
	}
	
}
