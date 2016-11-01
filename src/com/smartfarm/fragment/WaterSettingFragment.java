package com.smartfarm.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.WaterInfo;
import com.smartfarm.db.util.WaterInfoDao;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.togglebutton.ToggleButton;
import com.smartfarm.widget.togglebutton.ToggleButton.OnToggleChanged;

public class WaterSettingFragment extends BaseFragment implements EventHandler {
	
	private View settingView;
	
	private EditText waterTime;
	private EditText waterMin;
	private Spinner waterCount;
	private EditText waterAlarmMax;
	private EditText waterAlarmMin;
	
	private ToggleButton alarmMaxEnable;
	private ToggleButton alarmMinEnable;
	
	private WaterInfo currInfo;
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (settingView == null) {
			settingView = inflater.inflate(R.layout.fragment_water_setting, null, false);
		}
		
		initView(settingView);
		
		initData();
		
		return settingView;
	}

	@Override
	public void initView(View view) {
		
		waterMin = (EditText) view.findViewById(R.id.water_setting_min);
		waterTime = (EditText) view.findViewById(R.id.water_setting_time);
		waterCount = (Spinner) view.findViewById(R.id.water_setting_count);
		waterAlarmMax = (EditText) view.findViewById(R.id.water_setting_alarm_max);
		waterAlarmMin = (EditText) view.findViewById(R.id.water_setting_alarm_min);
		alarmMaxEnable = (ToggleButton) view.findViewById(R.id.water_setting_btn_max);
		alarmMinEnable = (ToggleButton) view.findViewById(R.id.water_setting_btn_min);
		
		view.findViewById(R.id.water_btn_read).setOnClickListener(this);
		view.findViewById(R.id.water_btn_send).setOnClickListener(this);
		
		alarmMaxEnable.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {

				waterAlarmMax.setVisibility(on ? View.VISIBLE : View.GONE);
			}
		});

		alarmMinEnable.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {

				waterAlarmMin.setVisibility(on ? View.VISIBLE : View.GONE);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.water_btn_read) {
			
			ProtocolFactory.getSynWaterDataProtocol().send();
			ToastTool.showToast("正在读取主控端参数...");
		} else {
			
			CommonDialog dialog = new CommonDialog(getActivity());
			dialog.setTitle("确认操作");
			dialog.setMessage(getActivity().getString(R.string.sure_to_setting));
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("同步", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					StringBuilder result = new StringBuilder();

					result.append("*setMin:");
					result.append(currInfo.getWaterMin());
					result.append("*setAlarmMax:");
					result.append(currInfo.getWaterAlarmMax());
					result.append("*setAlarmMin:");
					result.append(currInfo.getWaterAlarmMin());
					result.append("*setTime:");
					result.append(currInfo.getWaterTime());
					result.append("*setAlarmMaxEnable:");
					result.append(currInfo.isWaterMaxEnable());
					result.append("*setAlarmMinEnable:");
					result.append(currInfo.isWaterMinEnable());
					
					ProtocolFactory.getSynWaterDataProtocol(result.toString()).send();

					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}
	
	@Override
	public void initData() {
		
		currInfo = AppContext.context().getWaterInfo();

		waterMin.setText("");
		waterTime.setText("");
		waterAlarmMax.setText("");
		waterAlarmMin.setText("");
		
		waterMin.setHint(String.valueOf(currInfo.getWaterMin()));
		waterTime.setHint(String.valueOf(currInfo.getWaterTime()));
		waterAlarmMax.setHint(String.valueOf(currInfo.getWaterAlarmMax()));
		waterAlarmMin.setHint(String.valueOf(currInfo.getWaterAlarmMin()));
		
		setWaterCount(currInfo.getWaterCount());
		
		if (currInfo.isWaterMaxEnable())
			alarmMaxEnable.setToggleOn();
		else
			alarmMaxEnable.setToggleOff();

		if (currInfo.isWaterMinEnable())
			alarmMinEnable.setToggleOn();
		else
			alarmMinEnable.setToggleOff();
		
		waterAlarmMin.setVisibility(currInfo.isWaterMinEnable() ? View.VISIBLE : View.GONE);
		waterAlarmMax.setVisibility(currInfo.isWaterMaxEnable() ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.submit_menu, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(submit()) {
			
			CommonTool.HideKb(getActivity(), settingView);
		}
			
		return true;
	}
	
	private boolean submit() {
		
		int time = -1;
		if (!ShowUtil.isEmpty(waterTime.getText().toString()))
			time = Integer.valueOf(waterTime.getText().toString());
		else
			time = Integer.valueOf(waterTime.getHint().toString());
		
		int min = -1;
		if (!ShowUtil.isEmpty(waterMin.getText().toString())) {

			min = Integer.valueOf(waterMin.getText().toString());
			
			if(min == 0 || min > 100) {
				ToastTool.showToast("湿度阈值设置不合理，请重试！");
				waterMin.requestFocus();
				return false;
			}
		} else
			min = Integer.valueOf(waterMin.getHint().toString());
		
		int alarmMax = -1;
		if (alarmMaxEnable.currState() && !ShowUtil.isEmpty(waterAlarmMax.getText().toString())) {

			alarmMax = Integer.valueOf(waterAlarmMax.getText().toString());
			
			if(alarmMax == 0 || alarmMax > 100) {
				ToastTool.showToast("湿度报警最大值设置不合理，请重试！");
				waterAlarmMax.requestFocus();
				return false;
			}
		} else
			alarmMax = Integer.valueOf(waterAlarmMax.getHint().toString());
		
		int alarmMin = -1;
		if (alarmMinEnable.currState() && !ShowUtil.isEmpty(waterAlarmMin.getText().toString())) {

			alarmMin = Integer.valueOf(waterAlarmMin.getText().toString());
			
			if(alarmMin == 0 || alarmMin > 100) {
				ToastTool.showToast("湿度报警最大值设置不合理，请重试！");
				waterAlarmMin.requestFocus();
				return false;
			}
		} else
			alarmMin = Integer.valueOf(waterAlarmMin.getHint().toString());

		currInfo.setWaterMin(min);
		currInfo.setWaterTime(time);
		currInfo.setWaterAlarmMax(alarmMax);
		currInfo.setWaterAlarmMin(alarmMin);
		currInfo.setWaterCount(getWaterCount());
		currInfo.setWaterMaxEnable(alarmMaxEnable.currState());
		currInfo.setWaterMinEnable(alarmMinEnable.currState());
		
		WaterInfoDao.update(currInfo);
		ToastTool.showToast(R.string.save_success);
		
		return true;
	}

	private int getWaterCount() {
		return waterCount.getSelectedItemPosition() + 1;
	}

	private void setWaterCount(int count) {
		waterCount.setSelection(count - 1);
	}

	@Override
	public void onEvent(LocalEvent event) {
		
		if(event.getEventType() == LocalEvent.EVENT_TYPE_WATER_CONFIG) 
			initData();
	}
}
