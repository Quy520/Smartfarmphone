package com.smartfarm.fragment;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.LightInfo;
import com.smartfarm.db.util.LightInfoDao;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.widget.dialog.CommonDialog;

public class LightSettingFragment extends BaseFragment implements EventHandler {
	
	private View settingView;
	
	private Spinner lightCount;
	private EditText lightMin;
	private EditText lightMax;
	private EditText lightNeed;
	private EditText lightDiy;
	private TextView lightStart;
	
	private LightInfo info;
	private AlertDialog timeDialog;
	private TimePicker startTimePicker;
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (settingView == null) {
			settingView = inflater.inflate(R.layout.fragment_light_setting, null, false);
		}
		
		initView(settingView);
		
		initData();
		
		return settingView;
	}

	@Override
	public void initView(View view) {

		lightCount = (Spinner) view.findViewById(R.id.light_setting_count);
		lightMin = (EditText) view.findViewById(R.id.light_setting_min);
		lightMax = (EditText) view.findViewById(R.id.light_setting_max);
		lightNeed = (EditText) view.findViewById(R.id.light_setting_need);
		lightDiy = (EditText) view.findViewById(R.id.light_setting_diy);
		lightStart = (TextView) view.findViewById(R.id.light_setting_start);
		
		lightStart.setOnClickListener(this);
		view.findViewById(R.id.light_btn_read).setOnClickListener(this);
		view.findViewById(R.id.light_btn_send).setOnClickListener(this);
	}
	
	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.light_btn_read) {
			
			ProtocolFactory.getSynLightDataProtocol().send();
			ToastTool.showToast("正在读取主控端参数...");
		} else if(v.getId() == R.id.light_setting_start) {
			
			if(timeDialog == null) {
				
				View dialogView = LayoutInflater.from(getActivity()).inflate(
						R.layout.timepicker_dialog_view, null);
				timeDialog = new AlertDialog.Builder(getActivity())
						.setView(dialogView)
						.setTitle(R.string.set_time_title)
						.setNegativeButton(R.string.cancle, null)
						.setPositiveButton(R.string.confirm, new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								StringBuilder result = new StringBuilder();

								if (startTimePicker.getCurrentHour() < 10)
									result.append("0");
								result.append(startTimePicker.getCurrentHour());
								result.append(":");

								if (startTimePicker.getCurrentMinute() < 10)
									result.append("0");
								result.append(startTimePicker.getCurrentMinute());

								lightStart.setText(result.toString());
							}
						})
						.create();
				
				startTimePicker = (TimePicker) dialogView.findViewById(R.id.tp_dialog_picker);
				startTimePicker.setIs24HourView(true);
			}

			Calendar calendar = Calendar.getInstance();
			startTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			startTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

			timeDialog.show();
			
		} else {
			
			CommonDialog dialog = new CommonDialog(getActivity());
			dialog.setTitle("确认操作");
			dialog.setMessage(getActivity().getString(R.string.sure_to_setting));
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("同步", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					LightInfo info = AppContext.context().getLightInfo();
					StringBuilder result = new StringBuilder();

					result.append("*setMax:");
					result.append(info.getMax());
					result.append("*setMin:");
					result.append(info.getMin());
					result.append("*setNeed:");
					result.append(info.getNeed());
					result.append("*setDiy:");
					result.append(info.getDiy());
					result.append("*setStart:");
					result.append(info.getStart());
					
					ProtocolFactory.getSynLightDataProtocol(result.toString()).send();;

					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}
	
	@Override
	public void initData() {
		
		info = AppContext.context().getLightInfo();

		lightMin.setText("");
		lightMax.setText("");
		lightNeed.setText("");
		lightDiy.setText("");
		lightStart.setText(info.getStart());
		
		lightMin.setHint(String.valueOf(info.getMin()));
		lightMax.setHint(String.valueOf(info.getMax()));
		lightNeed.setHint(String.valueOf(info.getNeed()));
		lightDiy.setHint(String.valueOf(info.getDiy()));
		
		setLightCount(info.getCount());
	}

	private int getLightCount() {
		return lightCount.getSelectedItemPosition() + 2;
	}

	private void setLightCount(int count) {
		lightCount.setSelection(count - 2);
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
	
	public boolean submit() {
		
		int minValue = 0;
		if(!ShowUtil.isEmpty(lightMin.getText().toString())) {
			
			minValue = Integer.valueOf(lightMin.getText().toString());
			if(minValue > 54612 || minValue <= 0) {

				lightMin.requestFocus();
				ToastTool.showToast("最小有效照度设置不合理，请重新输入！");
				return false;
			}
		} else {
			minValue = info.getMin();
		}
		
		int maxValue = 0;
		if(!ShowUtil.isEmpty(lightMax.getText().toString())) {
			
			maxValue = Integer.valueOf(lightMax.getText().toString());
			if(maxValue > 54612 || maxValue <= 0) {

				lightMax.requestFocus();
				ToastTool.showToast("最大照度设置不合理，请重新输入！");
				return false;
			}
		}  else {
			maxValue = info.getMax();
		}
		
		int needValue = 0;
		if(!ShowUtil.isEmpty(lightNeed.getText().toString())) {
			
			needValue = Integer.valueOf(lightNeed.getText().toString());
			if(needValue > 1440 || needValue <= 0) {

				lightNeed.requestFocus();
				ToastTool.showToast("需要的有效光照时间设置不合理，请重新输入！");
				return false;
			}
		} else {
			needValue = info.getNeed();
		}
		
		int diyValue = 0;
		if(!ShowUtil.isEmpty(lightDiy.getText().toString())) {
			
			diyValue = Integer.valueOf(lightDiy.getText().toString());
			if(diyValue > 1440 || diyValue <= 0) {
				
				lightDiy.requestFocus();
				ToastTool.showToast("自定义补光时间长度设置不合理，请重新输入！");
				return false;
			}
		}  else {
			diyValue = info.getDiy();
		}
		
		info.setDiy(diyValue);
		info.setMax(maxValue);
		info.setMin(minValue);
		info.setNeed(needValue);
		info.setCount(getLightCount());
		info.setStart(lightStart.getText().toString());
		
		LightInfoDao.update(info);
		ToastTool.showToast("设置成功！");
		
		return true;
	}

	@Override
	public void onEvent(LocalEvent event) {
		
		if(event.getEventType() == LocalEvent.EVENT_TYPE_LIGHT_CONFIG) {
			initData();
		}
	}
}
