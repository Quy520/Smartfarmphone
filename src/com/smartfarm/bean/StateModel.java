package com.smartfarm.bean;

import java.util.Date;

import com.smartfarm.tools.CommonTool;
import com.smartfarm.view.R;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StateModel {
	
	public static final int TYPE_TEMP = 0;
	public static final int TYPE_CO2 = TYPE_TEMP + 1;
	public static final int TYPE_WATER = TYPE_CO2 + 1;
	public static final int TYPE_LIGHT = TYPE_WATER + 1;
	public static final int TYPE_MAX = TYPE_LIGHT + 1;

	private int type;
	
	private TextView titleView;
	private TextView valueView;
	private TextView tipView;
	private TextView modeView;
	private TextView lastTime;
	private CardView bgView;

	private View loadView;
	private TextView loadMsg;
	private ProgressBar progress;
	
	public StateModel(View view, int type) {
		
		titleView = (TextView) view.findViewById(R.id.tv_left_subtitle);
		valueView = (TextView) view.findViewById(R.id.tv_left_value);
		modeView = (TextView) view.findViewById(R.id.tv_control_mode);
		tipView = (TextView) view.findViewById(R.id.tv_right_tip);
		progress = (ProgressBar) view.findViewById(R.id.pb_load);
		lastTime = (TextView) view.findViewById(R.id.ms_part_last);
		loadMsg = (TextView) view.findViewById(R.id.tv_load_error_msg);
		loadView = view.findViewById(R.id.fl_bg);
		bgView = (CardView) view;
		
		this.type = type;
		
		switch(type) {
		
		case TYPE_TEMP:

			lastTime.setText("");
			titleView.setText("最低温度(℃)/最高温度(℃)");
			tipView.setText("this is tip");
			valueView.setText("20°/30°");
			modeView.setText("自动控制");
			tipView.setSelected(false);
			loadView.setVisibility(View.GONE);
			
			break;
		case TYPE_LIGHT:

			lastTime.setText("");
			modeView.setText("手动控制");
			titleView.setText("当前照度(Lux)");
			loadView.setVisibility(View.GONE);
			tipView.setSelected(false);
			break;
		case TYPE_WATER:

			lastTime.setText("");
			titleView.setText("土壤温度(℃)/土壤湿度(RH)");
			loadView.setVisibility(View.GONE);
			tipView.setSelected(true);
			break;
		case TYPE_CO2:

			lastTime.setText("");
			titleView.setText("空气湿度(RH)/二氧化碳浓度(PPM)");
			loadView.setVisibility(View.GONE);
			tipView.setSelected(true);
			break;
		}
	}
	
	public void setLastTime(long time) {
		
		lastTime.setText(CommonTool.getLastUpdateTime(new Date(time)));
		
		if(time < System.currentTimeMillis() - 8 * 60 * 1000)
			lastTime.setTextColor(lastTime.getResources().getColor(R.color.red));
		else
			lastTime.setTextColor(lastTime.getResources().getColor(R.color.white));
	}
	
	public void setVisibility(int visibility) {
		
		bgView.setVisibility(visibility);
	}
	
	public void setValue(String value) {
		
		valueView.setText(value);
	}
	
	public void setMode(boolean mode) {
		
		if(mode) {
			modeView.setText("自动模式");
			modeView.setTextColor(modeView.getResources().getColor(R.color.white));
		} else {
			modeView.setText("手动模式");
			modeView.setTextColor(modeView.getResources().getColor(R.color.state_red));
		}
	}
	
	public void setTip(String tip, boolean nor) {
		
		tipView.setText(tip);
		tipView.setSelected(!nor);
	}
	
	public void showProgress() {

		progress.setVisibility(View.VISIBLE);
		loadMsg.setVisibility(View.GONE);
		loadView.setVisibility(View.VISIBLE);
	}
	
	public void loadSuccess() {
		
		loadView.setVisibility(View.GONE);
	}
	
	public void loadFaild(String msg) {

		loadMsg.setText(msg);
		progress.setVisibility(View.GONE);
		loadMsg.setVisibility(View.VISIBLE);
	}
	
	public int getType() {
		
		return type;
	}
}
