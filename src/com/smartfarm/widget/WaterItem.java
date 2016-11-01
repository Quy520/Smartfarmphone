package com.smartfarm.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfarm.view.R;

public class WaterItem {
	
	private int id;
	int value = 0;
	private TextView descr;
	private TextView valueText;
	private TextView tempText;
	private ImageView state;
	private View root;
	
	private boolean isOpen = false;
	
	public WaterItem(int id, View root) {
		
		this.id = id;
		this.root = root;
		
		descr = (TextView) root.findViewById(R.id.water_id);
		tempText = (TextView) root.findViewById(R.id.water_temp);
		valueText = (TextView) root.findViewById(R.id.water_value);
		state = (ImageView) root.findViewById(R.id.water_state);
		
		descr.setText("电磁阀" + (id + 1) + "号");
		state.setSelected(false);
		setValue(100);
	}
	
	public void setVisibility(int visibility) {
		root.setVisibility(visibility);
	}
	
	public void setValue(int value) {
		this.value = value;
		valueText.setText(value + "%RH");
	}
	
	public void setTemp(float temp) {
		
		tempText.setText(temp + "℃");
	}
	
	public int getValue() {
		return value;
	}
	
	public void setState(boolean open) {
		isOpen = open;
		state.setSelected(open);
	}
	
	public boolean getState() {
		return isOpen;
	}
	
	public int getId() {
		return id;
	}
}
