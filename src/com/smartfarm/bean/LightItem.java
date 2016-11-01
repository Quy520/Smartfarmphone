package com.smartfarm.bean;

import android.widget.TextView;

import com.smartfarm.view.R;

public class LightItem {
	
	private int id;
	private int value = 0;
	private TextView light;
	private String pattern;
	
	private boolean isOpen = false;
	
	public LightItem(int id, TextView light) {
		
		this.id = id;
		this.light = light;
		
		pattern = light.getResources().getString(R.string.light_value);
		light.setSelected(false);

		setLight(0);
	}

	public void setLight(int lightValue) {
		
		value = lightValue;
		light.setText(String.format(pattern, lightValue, id + 1));
	}
	
	public int getLishtValue() {
		
		return value;
	}
	
	public void setLightState(boolean isOn) {
		
		isOpen = isOn;
		light.setSelected(isOpen);
	}
	
	public boolean getLightState() {
		
		return isOpen;
	}
	
	public void setVisibility(int visibility) {
		
		light.setVisibility(visibility);
	}
}
