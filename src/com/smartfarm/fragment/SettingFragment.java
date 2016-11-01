package com.smartfarm.fragment;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.ShareHelper;
import com.smartfarm.widget.togglebutton.ToggleButton;
import com.smartfarm.widget.togglebutton.ToggleButton.OnToggleChanged;
import com.tencent.bugly.crashreport.CrashReport;

public class SettingFragment extends BaseFragment implements AlertDialog.OnClickListener, EventHandler {

	private View dialogView;
	private AlertDialog dialog;
	private TimePicker dialogTimePicker;

	private EditText morningTimePeroid; // 早晨放风时间长度（分钟）
	private TextView morningOpenTime; // 早晨放风时间
	private TextView nightCloseTime; // 晚上关窗时间
	private EditText padphonenumber; // Pad电话号码
	private EditText ghName; // 大棚名称
	private EditText padPwd;
	private Spinner windowCount;
	private EditText pushTime;
	private EditText alarmMax;
	private EditText alarmMin;
	private View focusView;

	private ToggleButton alarmMaxEnable;
	private ToggleButton alarmMinEnable;
	private ToggleButton alarmMsgEnable;

	@Override
	protected int getLayoutId() {
		return R.layout.setting_view;
	}

	@Override
	public void initView(View settingView) {
		
		focusView = settingView.findViewById(R.id.setting_focus);
		alarmMaxEnable = (ToggleButton) settingView
				.findViewById(R.id.setting_btn_max);
		alarmMinEnable = (ToggleButton) settingView
				.findViewById(R.id.setting_btn_min);
		alarmMsgEnable = (ToggleButton) settingView.findViewById(R.id.setting_btn_msg_enable);
		ghName = (EditText) settingView.findViewById(R.id.gh_name);
		morningTimePeroid = (EditText) settingView
				.findViewById(R.id.setting_vent_time);
		padphonenumber = (EditText) settingView
				.findViewById(R.id.setting_tem_machine_num);
		windowCount = (Spinner) settingView
				.findViewById(R.id.edit_window_count);
		morningOpenTime = (TextView) settingView
				.findViewById(R.id.setting_open_window_time);
		nightCloseTime = (TextView) settingView
				.findViewById(R.id.setting_close_window_time);
		padPwd = (EditText) settingView.findViewById(R.id.pad_pwd);
		alarmMax = (EditText) settingView.findViewById(R.id.setting_alarm_max);
		alarmMin = (EditText) settingView.findViewById(R.id.setting_alarm_min);
		pushTime = (EditText) settingView.findViewById(R.id.edit_push_time);

		morningOpenTime.setOnClickListener(this);
		nightCloseTime.setOnClickListener(this);

		GreenHouseControl ghControl = new GreenHouseControl();
		settingView.findViewById(R.id.setting_enable_max).setOnClickListener(ghControl);
		settingView.findViewById(R.id.setting_enable_min).setOnClickListener(ghControl);
		settingView.findViewById(R.id.setting_stalls).setOnClickListener(ghControl);
		settingView.findViewById(R.id.setting_temp).setOnClickListener(ghControl);

		settingView.findViewById(R.id.read_setting).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						CommonDialog dialog = new CommonDialog(getActivity());
						dialog.setTitle("确认操作");
						dialog.setMessage("确定要从温控机读取参数并且覆盖本地参数吗？");
						dialog.setNegativeButton("取消", null);
						dialog.setPositiveButton("确定",
								new AlertDialog.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										ProtocolFactory.GetSynDataProtocol().send();
										dialog.dismiss();
									}
								});
						dialog.show();
					}
				});

		alarmMaxEnable.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {

				alarmMax.setVisibility(on ? View.VISIBLE : View.GONE);
			}
		});

		alarmMinEnable.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {

				alarmMin.setVisibility(on ? View.VISIBLE : View.GONE);
			}
		});
	}

	@Override
	protected boolean refreshOptionsMenu() {
		return true;
	}

	@Override
	public void initData() {
		
		PengInfo info = AppContext.context().getCurrPengInfo();
		
		try {
			
			focusView.requestFocus();

			padPwd.setText("");
			ghName.setText("");
			pushTime.setText("");
			alarmMax.setText("");
			alarmMin.setText("");
			padphonenumber.setText("");
			morningTimePeroid.setText("");
			padPwd.setHint("点击输入新密码");
			ghName.setHint(info.getName());
			pushTime.setHint("" + info.getPushTime());
			alarmMax.setHint("" + info.getAlarmTempMax());
			alarmMin.setHint("" + info.getAlarmTempMin());
			morningTimePeroid.setHint("" + info.getVenTime());
			padphonenumber.setHint(info.getPhoneNum());
			setWindowCount(info.getWindowCount());
			
			if (info.isAlarmMaxEnable())
				alarmMaxEnable.setToggleOn();
			else
				alarmMaxEnable.setToggleOff();

			if (info.isAlarmMinEnable())
				alarmMinEnable.setToggleOn();
			else
				alarmMinEnable.setToggleOff();
			if(info.isAlarmMsgEnable())
				alarmMsgEnable.setToggleOn();
			else
				alarmMsgEnable.setToggleOff();
			
			morningOpenTime.setText(info.getNightStart());
			nightCloseTime.setText(info.getNightEnd());

			alarmMax.setVisibility(info.isAlarmMaxEnable() ? View.VISIBLE
					: View.GONE);
			alarmMin.setVisibility(info.isAlarmMinEnable() ? View.VISIBLE
					: View.GONE);
			
		} catch(NullPointerException e) {
			CrashReport.postCatchedException(e);
		}
	}
	
	public int getWindowCount() {
		return windowCount.getSelectedItemPosition() + 2;
	}

	private void setWindowCount(int count) {
		windowCount.setSelection(count - 2);
	}

	private boolean submit() {
		PengInfo info = AppContext.context().getCurrPengInfo();

		if (!padphonenumber.getText().toString().equals("")
				&& !CommonTool.checkMobile(padphonenumber.getText().toString())) {
			ToastTool.showToast("输入温控机号码有误，请重新输入!");
			return false;
		}

		if (!ShowUtil.isEmpty(alarmMax.getText().toString())) {

			int alarmMaxValue = Integer.valueOf(alarmMax.getText().toString());

			if (alarmMaxValue <= 0) {
				ToastTool.showToast("报警最高温度设置过低，请重新输入!");
				return false;
			} else if (alarmMaxValue > 55) {
				ToastTool.showToast("报警最高温度设置过高，请重新输入!");
				return false;
			}

			info.setAlarmTempMax(alarmMaxValue);
		}

		if (!ShowUtil.isEmpty(alarmMin.getText().toString())) {

			int alarmMinValue = Integer.valueOf(alarmMin.getText().toString());

			if (alarmMinValue <= -10) {
				ToastTool.showToast("报警最低温度设置过低，请重新输入!");
				return false;
			} else if (alarmMinValue > 55) {
				ToastTool.showToast("报警最低温度设置过高，请重新输入!");
				return false;
			}

			info.setAlarmTempMin(alarmMinValue);
		}

		if (!ShowUtil.isEmpty(pushTime.getText().toString())) {

			int pushTimeValue = Integer.valueOf(pushTime.getText().toString());

			if (pushTimeValue <= 0) {
				ToastTool.showToast("推送温度时间设置过小，请重新输入!");
				return false;
			} else if (pushTimeValue > 120) {
				ToastTool.showToast("推送温度时间设置过大，建议不超过两小时，请重新输入!");
				return false;
			}

			info.setPushTime(pushTimeValue);
		}

		if (!ShowUtil.isEmpty(padPwd.getText().toString())) {
			String pwd = Md5Utils.encode(padPwd.getText().toString());
			info.setPwd(pwd);
			Log.d(Constants.TAG, "pwd : " + padPwd.getText().toString()
					+ " , md5 : " + pwd);
		}

		info.setWindowCount(getWindowCount());
		info.setAlarmMaxEnable(alarmMaxEnable.currState());
		info.setAlarmMinEnable(alarmMinEnable.currState());
		info.setAlarmMsgEnable(alarmMsgEnable.currState());
		info.setNightEnd(nightCloseTime.getText().toString());
		info.setNightStart(morningOpenTime.getText().toString());

		if (!ghName.getText().toString().equals("")) {
			info.setName(ghName.getText().toString());
		}

		if (!padphonenumber.getText().toString().equals(""))
			info.setPhoneNum(padphonenumber.getText().toString());

		if (!morningTimePeroid.getText().toString().equals(""))
			info.setVenTime(Integer.valueOf(morningTimePeroid.getText()
					.toString()));

		PengInfoDao.update(info);
		ToastTool.showToast(R.string.save_success);
		
		if (!ghName.getText().toString().equals("")) {
			EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_PENG_CHANGE));
		}
		
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {

		if(dialog == null) {
			
			dialogView = LayoutInflater.from(getActivity()).inflate(
					R.layout.timepicker_dialog_view, null);
			dialog = new AlertDialog.Builder(getActivity()).setView(dialogView)
					.setTitle(R.string.set_time_title)
					.setNegativeButton(R.string.cancle, null)
					.setPositiveButton(R.string.confirm, this).create();

			dialogTimePicker = (TimePicker) dialogView
					.findViewById(R.id.tp_dialog_picker);
			dialogTimePicker.setIs24HourView(true);
		}
		
		Calendar calendar = Calendar.getInstance();
		dialogTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		dialogTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		dialogView.setContentDescription("" + v.getId());

		dialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		StringBuilder result = new StringBuilder();

		if (dialogTimePicker.getCurrentHour() < 10)
			result.append("0");
		result.append(dialogTimePicker.getCurrentHour());
		result.append(":");

		if (dialogTimePicker.getCurrentMinute() < 10)
			result.append("0");
		result.append(dialogTimePicker.getCurrentMinute());

		int id = Integer.valueOf(dialogView.getContentDescription().toString());
		if (id == R.id.setting_close_window_time)
			nightCloseTime.setText(result.toString());
		else
			morningOpenTime.setText(result.toString());
	}

	private class GreenHouseControl implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.setting_enable_max:

				alarmMaxEnable.toggle();
				break;
			case R.id.setting_enable_min:

				alarmMinEnable.toggle();
				break;
			case R.id.setting_stalls:

				UIHelper.showSimpleBack(getActivity(), BackPage.STAILLS);
				break;
			case R.id.setting_temp:

				UIHelper.showSimpleBack(getActivity(), BackPage.TEMP_CONFIG);
				break;
				
			case R.id.setting_msg_enable:
				
				alarmMsgEnable.toggle();
				break;
			}
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.submit_menu, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId()){
		 case R.id.share:
			 ShareHelper.handleShare(getActivity());
		}

		if(submit()) 
			CommonTool.HideKb(getActivity(), getView());
		return true;
	}
		
				
	

	@Override
	public void onEvent(LocalEvent event) {

		if(event.getEventType() == LocalEvent.EVENT_TYPE_CONFIG_CHANGE || 
				event.getEventType() == LocalEvent.EVENT_TYPE_PENG_CHANGE || 
				event.getEventType() == LocalEvent.EVENT_TYPE_PENG_SWITCHING) {

			if(getHoldView() != null)
				initData();
		}
	}
}
