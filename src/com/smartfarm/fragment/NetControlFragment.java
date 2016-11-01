package com.smartfarm.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.TempBean;
import com.smartfarm.bean.TempTuple;
import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.InfoRecordDao;
import com.smartfarm.msg.MsgHelper;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.AssistThreadWork;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.HelperThread;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.NetCheckWork;
import com.smartfarm.view.util.NetManager;
import com.smartfarm.widget.InnerScrollView;
import com.smartfarm.widget.TempManager;
import com.smartfarm.widget.TempWindow;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.ShareHelper;
import com.smartfarm.widget.togglebutton.ToggleButton;
import com.smartfarm.widget.togglebutton.ToggleButton.OnToggleChanged;
/**
 * 网络控制页面
 * @author QSD
 *
 */
public class NetControlFragment extends BaseFragment implements EventHandler {

	private int pengId = 0;

	private ToggleButton mSwitch;
	private ToggleButton mSwitchAuto;
	// private ToggleButton mSwitchOpenAll;
	// private ToggleButton mSwitchCloseAll;
	private TextView controlWay;
	private CheckBox[] windowCheck;
	private TextView receiveMsg;
	private TextView showLinkInfo;
	private InnerScrollView inner;
	private View checkLinear;
	private View tempLinear;
	private View checkLinear2;
	private View tempLinear2;
	private TextView tip;
	private TextView modeTip;
	private TempWindow[] temp;
	private TextView lastTempTime;
	private boolean first = true;
	private TempManager tempManager;
	private boolean currNetState = false;

//	private TextView venstateMsg;
//	private TextView rainstateMsg;

	@Override
	protected int getLayoutId() {
		return R.layout.net_control_view;
	}

	@Override
	public void initView(View ncView) {

		tempManager = new TempManager();

		mSwitch = (ToggleButton) ncView.findViewById(R.id.nc_btn_switch);
		mSwitchAuto = (ToggleButton) ncView
				.findViewById(R.id.nc_btn_auto_enable);
		// mSwitchOpenAll = (ToggleButton)
		// ncView.findViewById(R.id.nc_text_ventilation);
		// mSwitchCloseAll = (ToggleButton)
		// ncView.findViewById(R.id.nc_descr_closeall);
		controlWay = (TextView) ncView.findViewById(R.id.nc_text_control_way);
		receiveMsg = (TextView) ncView.findViewById(R.id.nc_text_receive_msg);
		lastTempTime = (TextView) ncView
				.findViewById(R.id.nc_title_current_tem);
		showLinkInfo = (TextView) ncView.findViewById(R.id.nc_text_link);
		ScrollView outer = (ScrollView) ncView.findViewById(R.id.nc_outer);
		inner = (InnerScrollView) ncView.findViewById(R.id.nc_inner_scroll);
		checkLinear = ncView.findViewById(R.id.nc_ll_check);
		tempLinear = ncView.findViewById(R.id.temp_ll);
		checkLinear2 = ncView.findViewById(R.id.nc_ll_check2);
		tempLinear2 = ncView.findViewById(R.id.temp_ll3);
		tip = (TextView) ncView.findViewById(R.id.tip);
		modeTip = (TextView) ncView.findViewById(R.id.mode_tip);
//		venstateMsg = (TextView) ncView.findViewById(R.id.nc_btn_ventilation);
//		rainstateMsg = (TextView) ncView.findViewById(R.id.nc_btn_closeall);

		ncView.findViewById(R.id.nc_mode_rl).setOnClickListener(this);
		ncView.findViewById(R.id.nc_mode_rl1).setOnClickListener(this);
		ncView.findViewById(R.id.nc_mode_rl2).setOnClickListener(this);
		ncView.findViewById(R.id.nc_mode_auto_enable).setOnClickListener(this);

		temp = new TempWindow[9];
		windowCheck = new CheckBox[9];

		temp[Constants.WINDOW_ID_1] = (TempWindow) ncView
				.findViewById(R.id.temp_win1);
		temp[Constants.WINDOW_ID_1].setWindowId(Constants.WINDOW_ID_1);
		temp[Constants.WINDOW_ID_2] = (TempWindow) ncView
				.findViewById(R.id.temp_win2);
		temp[Constants.WINDOW_ID_2].setWindowId(Constants.WINDOW_ID_2);
		temp[Constants.WINDOW_ID_3] = (TempWindow) ncView
				.findViewById(R.id.temp_win3);
		temp[Constants.WINDOW_ID_3].setWindowId(Constants.WINDOW_ID_3);
		temp[Constants.WINDOW_ID_4] = (TempWindow) ncView
				.findViewById(R.id.temp_win4);
		temp[Constants.WINDOW_ID_4].setWindowId(Constants.WINDOW_ID_4);
		temp[Constants.WINDOW_ID_5] = (TempWindow) ncView
				.findViewById(R.id.temp_win5);
		temp[Constants.WINDOW_ID_5].setWindowId(Constants.WINDOW_ID_5);
		temp[Constants.WINDOW_ID_6] = (TempWindow) ncView
				.findViewById(R.id.temp_win6);
		temp[Constants.WINDOW_ID_6].setWindowId(Constants.WINDOW_ID_6);
		temp[Constants.WINDOW_ID_7] = (TempWindow) ncView
				.findViewById(R.id.temp_win7);
		temp[Constants.WINDOW_ID_7].setWindowId(Constants.WINDOW_ID_7);
		temp[Constants.WINDOW_ID_8] = (TempWindow) ncView
				.findViewById(R.id.temp_win8);
		temp[Constants.WINDOW_ID_8].setWindowId(Constants.WINDOW_ID_8);
		temp[Constants.WINDOW_ID_9] = (TempWindow) ncView
				.findViewById(R.id.temp_win9);
		temp[Constants.WINDOW_ID_9].setWindowId(Constants.WINDOW_ID_9);

		windowCheck[Constants.WINDOW_ID_1] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w1);
		windowCheck[Constants.WINDOW_ID_2] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w2);
		windowCheck[Constants.WINDOW_ID_3] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w3);
		windowCheck[Constants.WINDOW_ID_4] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w4);
		windowCheck[Constants.WINDOW_ID_5] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w5);
		windowCheck[Constants.WINDOW_ID_6] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w6);
		windowCheck[Constants.WINDOW_ID_7] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w7);
		windowCheck[Constants.WINDOW_ID_8] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w8);
		windowCheck[Constants.WINDOW_ID_9] = (CheckBox) ncView
				.findViewById(R.id.nc_checkbox_w9);

		inner.setParentScrollView(outer);

		mSwitch.setClickChange(false);
		mSwitchAuto.setClickChange(false);
		// mSwitchCloseAll.setClickChange(false);
		// mSwitchOpenAll.setClickChange(false);

		mSwitch.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(final boolean on) {
				CommonDialog dialog = new CommonDialog(getActivity());
				dialog.setTitle(on ? R.string.sure_to_auto
						: R.string.sure_to_manual);
				dialog.setMessage(on ? R.string.to_auto_prompt
						: R.string.to_manual_prompt);

				dialog.setPositiveButton("确定",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (on)
									ProtocolFactory.GetSetTheModeProtocol(true)
											.send();
								else
									ProtocolFactory
											.GetSetTheModeProtocol(false)
											.send();

								dialog.dismiss();
							}
						});

				dialog.setNegativeButton("取消", null);
				dialog.show();
			}
		});

		mSwitchAuto.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(final boolean on) {
				CommonDialog dialog = new CommonDialog(getActivity());
				dialog.setTitle(on ? R.string.sure_to_high_auto
						: R.string.sure_to_high_manual);
				dialog.setMessage(on ? R.string.to_auto_open_descr
						: R.string.to_auto_prompt);

				dialog.setPositiveButton("确定",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (on)
									ProtocolFactory
											.GetSetTheHighAutoModeProtocol(true)
											.send();
								else
									ProtocolFactory
											.GetSetTheHighAutoModeProtocol(
													false).send();

								dialog.dismiss();
							}
						});

				dialog.setNegativeButton("取消", null);
				dialog.show();
			}
		});

		/*
		 * mSwitchCloseAll.setOnToggleChanged(new OnToggleChanged() {
		 * 
		 * @Override public void onToggle(final boolean on) { CommonDialog
		 * dialog = new CommonDialog(getActivity()); dialog.setTitle(on ?
		 * R.string.sure_to_rain : R.string.sure_to_out_rain);
		 * dialog.setMessage(on ? R.string.sure_to_rain_info :
		 * R.string.sure_to_rain_out_info);
		 * 
		 * dialog.setPositiveButton("确定",new AlertDialog.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { if
		 * (on) ProtocolFactory.GetSetTheRainModeProtocol(true).send(); else
		 * ProtocolFactory.GetSetTheRainModeProtocol(false).send();
		 * 
		 * dialog.dismiss(); } });
		 * 
		 * dialog.setNegativeButton("取消", null); dialog.show(); } });
		 */

		/*
		 * mSwitchOpenAll.setOnToggleChanged(new OnToggleChanged() {
		 * 
		 * @Override public void onToggle(final boolean on) { CommonDialog
		 * dialog = new CommonDialog(getActivity()); dialog.setTitle(on ?
		 * R.string.open_ven_into : R.string.open_ven_out); dialog.setMessage(on
		 * ? R.string.sure_to_ven : R.string.sure_to_out_ven);
		 * 
		 * dialog.setPositiveButton("确定", new AlertDialog.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { if
		 * (on) ProtocolFactory.GetSetTheWindowOpen(true).send(); else
		 * ProtocolFactory.GetSetTheWindowOpen(false).send();
		 * 
		 * dialog.dismiss(); } });
		 * 
		 * dialog.setNegativeButton("取消", null); dialog.show(); } });
		 */

		ncView.findViewById(R.id.nc_btn_link).setOnClickListener(this);
		ncView.findViewById(R.id.nc_btn_diagnosis).setOnClickListener(this);
		ncView.findViewById(R.id.nc_btn_syn).setOnClickListener(this);
		ncView.findViewById(R.id.nc_btn_temp).setOnClickListener(this);
		ncView.findViewById(R.id.nc_btn_clear).setOnClickListener(this);
		ncView.findViewById(R.id.nc_btn_stop).setOnClickListener(this);
		ncView.findViewById(R.id.nc_btn_close_window).setOnClickListener(this);
		ncView.findViewById(R.id.nc_btn_open_window).setOnClickListener(this);
		ncView.findViewById(R.id.nc_descr_closeall).setOnClickListener(this);
		ncView.findViewById(R.id.nc_text_ventilation).setOnClickListener(this);
	}

	@Override
	public void initData() {

		PengInfo info = AppContext.context().getCurrPengInfo();

		pengId = info.getId();

		if (AppContext.context().isNightMode())
			tip.setVisibility(View.VISIBLE);
		else
			tip.setVisibility(View.GONE);

		/*
		 * modeChange(); modeAutoChange();
		 * 
		 * modeOpenAll();
		 */
		modeCloseAll();
		refreshNet();

		int len = info.getWindowCount();

		for (int i = 0; i < 9; i++) {
			if (i < len) {
				temp[i].setVisibility(View.VISIBLE);
				windowCheck[i].setVisibility(View.VISIBLE);
			} else {
				temp[i].setVisibility(View.INVISIBLE);
				windowCheck[i].setVisibility(View.INVISIBLE);
			}

			if (first)
				windowCheck[i].setChecked(true);
		}

		first = false;
		if (len <= 3) {
			tempLinear.setVisibility(View.GONE);
			checkLinear.setVisibility(View.GONE);
		} else {
			checkLinear.setVisibility(View.VISIBLE);
			tempLinear.setVisibility(View.VISIBLE);
		}

		if (len > 6) {
			tempLinear2.setVisibility(View.VISIBLE);
			checkLinear2.setVisibility(View.VISIBLE);
		} else {
			checkLinear2.setVisibility(View.GONE);
			tempLinear2.setVisibility(View.GONE);
		}

		refreshTempInfo();
	}

	public void setTemp(TempBean tempBean) {

		if (tempBean != null) {
			tempManager.save(tempBean);
		}

		refreshTempInfo();
	}

	private void refreshNet() {

		currNetState = NetManager.getInstence().isNetOk();
		showLinkInfo.setText(currNetState ? R.string.net_link_ok
				: R.string.net_link_error);
	}

	private void modeCloseAll() {
		PengInfo info = AppContext.context().getCurrPengInfo();
		/*
		 * if (info.isCloseAll()) { mSwitchCloseAll.setToggleOn();
		 * rainstateMsg.setText(R.string.mode_rain_open);
		 * modeTip.setText("当前处于下雨关风");
		 * modeTip.setTextColor(getResources().getColor(R.color.red)); return; }
		 * else { mSwitchCloseAll.setToggleOff();
		 * rainstateMsg.setText(R.string.mode_rain_close); }
		 */
		/*
		 * if (info.isOpenAll()) { mSwitchOpenAll.setToggleOn();
		 * venstateMsg.setText(R.string.mode_wind_open);
		 * modeTip.setText("当前处于放晨风");
		 * modeTip.setTextColor(getResources().getColor(R.color.sky_blue));
		 * return; } else { mSwitchOpenAll.setToggleOff();
		 * venstateMsg.setText(R.string.mode_wind_close); }
		 */
		if (info.isAutoMode()) {
			mSwitch.setToggleOn();
			modeTip.setText(R.string.mode_tip_auto);
			modeTip.setTextColor(getResources().getColor(R.color.sky_blue));
			controlWay.setText(R.string.mode_auto);
		} else {
			mSwitch.setToggleOff();
			modeTip.setText(R.string.mode_tip);
			modeTip.setTextColor(getResources().getColor(R.color.red));
			controlWay.setText(R.string.mode_manually);
		}
	}

	private void refreshTempInfo() {

		PengInfo info = AppContext.context().getCurrPengInfo();
		TempBean tempBean = tempManager.get(info.getId());

		if (tempBean.isVaild())
			lastTempTime.setText(CommonTool.getLastUpdateTime(new Date(tempBean
					.getTime())));
		else
			lastTempTime.setText("");

		Iterator<TempTuple> it = tempBean.iterator();

		for (int index = 0; it.hasNext(); index++) {
			this.temp[index].setTemp(it.next());
		}
	}

	public void setReveiveMsg(String msg) {

		boolean newNetState = NetManager.getInstence().isNetOk();
		if (currNetState ^ newNetState) {
			currNetState = newNetState;
			showLinkInfo.setText(newNetState ? R.string.net_link_ok
					: R.string.net_link_error);
		}

		receiveMsg.append(msg + "\n");
		inner.scrollToBottom(receiveMsg);
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {

		if (!NetManager.getInstence().isNetOk()
				&& v.getId() != R.id.nc_btn_link
				&& v.getId() != R.id.nc_btn_diagnosis) {
			netIsFaild();
		}

		if (!AppContext.context().getAccountManager().isLogined())
			ToastTool.showToast("请先登录帐号...");
		CommonDialog dialog = new CommonDialog(getActivity());
		switch (v.getId()) {
		case R.id.nc_mode_rl:

			mSwitch.toggle();
			break;
		case R.id.nc_mode_auto_enable:

			mSwitchAuto.toggle();
			break;

		case R.id.nc_descr_closeall:
			// 下雨关风
			dialog.setTitle(R.string.sure_to_rain);
			dialog.setMessage(R.string.sure_to_rain_info);
			dialog.setPositiveButton("确定", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					ProtocolFactory.GetSetTheRainModeProtocol(true).send();
					dialog.dismiss();
				}
			});

			dialog.setNegativeButton("取消", null);
			dialog.show();

			break;

		case R.id.nc_text_ventilation:
			// 放晨风
			dialog.setTitle( R.string.open_ven_into);
			dialog.setMessage(R.string.sure_to_ven);

			dialog.setPositiveButton("确定",new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ProtocolFactory.GetSetTheWindowOpen(true).send();
					dialog.dismiss();
				}
			});
			dialog.setNegativeButton("取消", null);
			dialog.show();
			break;

		/*
		 * case R.id.nc_mode_rl1:
		 * 
		 * mSwitchCloseAll.toggle(); break;
		 */

		/*
		 * case R.id.nc_mode_rl2:
		 * 
		 * mSwitchOpenAll.toggle(); break;
		 */
		case R.id.nc_btn_syn:

			dialog.setTitle("确认操作");
			dialog.setMessage(getActivity().getString(R.string.sure_to_setting));
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("同步", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					HelperThread.getInstance().setThreadWork(
							new SynConfigRunable());

					dialog.dismiss();
				}
			});
			dialog.show();
			break;
		case R.id.nc_btn_temp:

			ToastTool.showToast(R.string.requesting_temp);

			ProtocolFactory.GetReadTempsProtocol().send();

			break;
		case R.id.nc_btn_diagnosis:

			NetCheckWork.showCheckDialog(getActivity());
			break;
		case R.id.nc_btn_clear:

			receiveMsg.setText("");
			break;
		case R.id.nc_btn_link:

			if (AppContext.context().getAccountManager().isLogined())
				MsgHelper.get().relink(getActivity());

			break;
		case R.id.nc_btn_close_window:

			showTipDialog(Constants.MOTOR_CONTROL_TYPE_CLOSE,
					"目前时间较晚，确定需要关闭风口吗？");
			break;
		case R.id.nc_btn_open_window:

			showTipDialog(Constants.MOTOR_CONTROL_TYPE_OPEN,
					"目前时间较晚，确定需要打开风口吗？");
			break;
		case R.id.nc_btn_stop:

			showTipDialog(Constants.MOTOR_CONTROL_TYPE_STOP,
					"目前时间较晚，确定需要停止风口吗？");
			break;
		}
	}

	private String getCheckedWindow() {

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < windowCheck.length; i++)
			if (windowCheck[i].isChecked()
					&& windowCheck[i].getVisibility() == View.VISIBLE) {

				result.append(i);
				result.append(";");
			}

		return result.toString();
	}

	private void showTipDialog(final int type, String tip) {

		Date nowdate = new Date();

		Calendar before = new GregorianCalendar();
		before.setTime(nowdate);
		before.set(Calendar.HOUR_OF_DAY, 6);
		before.set(Calendar.MINUTE, 0);
		before.set(Calendar.SECOND, 0);

		Calendar after = new GregorianCalendar();
		after.setTime(nowdate);
		after.set(Calendar.HOUR_OF_DAY, 18);
		after.set(Calendar.MINUTE, 0);
		after.set(Calendar.SECOND, 0);

		if (nowdate.getTime() >= after.getTimeInMillis()
				|| nowdate.getTime() <= before.getTimeInMillis()) {

			CommonDialog dialog = new CommonDialog(getActivity());
			dialog.setTitle(R.string.sure);
			dialog.setMessage(tip);
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							sendControlProtocol(type);
							dialog.dismiss();
						}
					});
			dialog.show();
		} else {

			sendControlProtocol(type);
		}
	}

	private void sendControlProtocol(int type) {

		switch (type) {
		case Constants.MOTOR_CONTROL_TYPE_OPEN:

			String openWin = getCheckedWindow();

			if (openWin.length() > 0)
				ProtocolFactory.GetControlsProtocol(
						Constants.MOTOR_CONTROL_TYPE_OPEN, openWin).send();
			else
				ToastTool.showToast(R.string.please_selected_open_w);

			break;
		case Constants.MOTOR_CONTROL_TYPE_CLOSE:

			String closeWin = getCheckedWindow();

			if (closeWin.length() > 0) {

				ProtocolFactory.GetControlsProtocol(
						Constants.MOTOR_CONTROL_TYPE_CLOSE, closeWin).send();
			} else
				ToastTool.showToast(R.string.please_selected_close_w);

			break;
		case Constants.MOTOR_CONTROL_TYPE_STOP:

			String stopWin = getCheckedWindow();

			if (stopWin.length() > 0)
				ProtocolFactory.GetControlsProtocol(
						Constants.MOTOR_CONTROL_TYPE_STOP, stopWin).send();
			else
				ToastTool.showToast(R.string.please_selected_stop_w);
			break;
		}
	}

	private void netIsFaild() {
		ToastTool.showToast(R.string.net_is_faild);
	}

	@Override
	public void onResume() {

		super.onResume();

		InfoRecord temp = InfoRecordDao.findLast(InfoRecord.RECORD_TYPE_TEMP,
				AppContext.context().getCurrPengInfo().getId());

		if (temp != null)
			setTemp(CommonTool.resolveTemp(temp.getData(), temp.getPengId(),
					temp.getTime()));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.main_menu_share, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// ShareHelper.handleShare(getActivity());
		switch (item.getItemId()) {
		case R.id.share:
			ShareHelper.handleShare(getActivity());
		}

		return true;
	}

	@Override
	public void onEvent(LocalEvent event) {

		if (getHoldView() == null)
			return;

		if (getHoldView() == null)
			return;

		if (event.getEventType() == LocalEvent.EVENT_TYPE_PENG_CHANGE
				|| event.getEventType() == LocalEvent.EVENT_TYPE_PENG_SWITCHING) {

			initData();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_MODE_CHANGE) {

			modeCloseAll();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_NET_OK
				|| event.getEventType() == LocalEvent.EVENT_TYPE_NET_DOWN) {

			refreshNet();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_TEMP) {

			TempBean bean = (TempBean) event.getEventData();

			if (bean.getId() == pengId)
				setTemp(bean);
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_AUTO_OPEN_ENABLE) {
			modeCloseAll();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_IS_RAIN) {
			modeCloseAll();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_OPEN_WINDOWS) {
			modeCloseAll();
		}
	}

	static class SynConfigRunable implements AssistThreadWork {

		@Override
		public void working() {

			PengInfo info = AppContext.context().getCurrPengInfo();
			StringBuilder result = new StringBuilder();

			result.append("*setTempMin:");
			result.append(info.getTempMin());
			result.append("*setTempMax:");
			result.append(info.getTempMax());
			result.append("*setThresholdTempMax:");
			result.append(info.getTempDiffMax());
			result.append("*setThresholdTempMin:");
			result.append(info.getTempDiffMin());
			result.append("*isAlarmEnable:");
			result.append(info.isAlarmMsgEnable());
			result.append("*setOpenLen:");
			result.append(info.getLenMax());
			result.append("*MorningTimePeriod:");
			result.append(info.getVenTime());
			result.append("*MorningOpenTime:");
			result.append(info.getNightStart());
			result.append("*NightCloseTime:");
			result.append(info.getNightEnd());
			result.append("*alarmMax:");
			result.append(info.getAlarmTempMax());
			result.append("*alarmMin:");
			result.append(info.getAlarmTempMin());
			result.append("*pushTime:");
			result.append(info.getPushTime());
			result.append("*o1:");
			result.append(info.getLenFirst());
			result.append("*o2:");
			result.append(info.getOpenSecond());
			result.append("*o3:");
			result.append(info.getOpenThird());
			result.append("*o4:");
			result.append(info.getOpenFourth());
			result.append("*o5:");
			result.append(info.getOpenFifth());
			result.append("*ha:");
			result.append(info.isAlarmMaxEnable());
			result.append("*la:");
			result.append(info.isAlarmMinEnable());

			ProtocolFactory.getSynDataProtocol(result.toString()).send();

			ProtocolFactory.getSynTempProtocol().send();
		}

		@Override
		public void ok() {
		}

	}
}
