package com.smartfarm.fragment;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.base.BaseFragmentInterface;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.TempBean;
import com.smartfarm.bean.TempTuple;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.HostInfo;
import com.smartfarm.tools.MessageBox;
import com.smartfarm.tools.MessageBox.MessageBoxButtons;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.widget.InnerScrollView;
import com.smartfarm.widget.TempManager;
import com.smartfarm.widget.TempWindow;
import com.smartfarm.widget.dialog.ShareHelper;
/**
 * 短信页面控制
 * @author QSD
 *
 */
public class MsgControlFragment extends BaseFragment implements OnCheckedChangeListener, 
			BaseFragmentInterface, EventHandler {
	protected static final int TEMPMESSAGE = 0x102;
	private boolean showSetting = false;
	private MineHandler myHandler;

	private CheckBox checkTem;
	private CheckBox checkMotor;
	private CheckBox window1;
	private CheckBox window2;
	private CheckBox window3;
	private CheckBox phonenumber;
	private CheckBox phonenumber2;
	private CheckBox phonenumber3;
	private CheckBox openInterval;
	private CheckBox tempMin;
	private CheckBox tempMinThreshold;
	private CheckBox tempMax;
	private CheckBox tempMaxThreshold;
	private CheckBox morningTimePeroid;
	private CheckBox morningOpenTime;
	private CheckBox nightCloseTime;
	private CheckBox closeInterval;
	private CheckBox checkMatch;
	private CheckBox selectedAll;;
	private RadioGroup typeGroup;
	private RadioButton btnOpenWindow;
	private RadioButton btnCloseWindow;
	private RadioButton btnStop;
	private RadioButton btnModeAuto;
	private TextView receiveMsg;
	private TextView btnGetTemp;
	private TextView btnMatch;
	private TextView btnSendMode;
	private TextView btnSendSetting;
	private TextView btnSendControl;
	private TextView btnShowSetting;
	private LinearLayout settingContext;
	private TempWindow[] temp;
	private View tempLinear;
	private View tempLinear2;
	private BroadcastReceiver mTempSMSReceiver;
	private TextView lastTempTime;
	private TempManager tempManager;

	@Override
	protected int getLayoutId() {
		return R.layout.msg_control_view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myHandler = new MineHandler();
		mTempSMSReceiver = new SmsReceiver();
		getActivity().registerReceiver(mTempSMSReceiver,
				new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	}

	@Override
	public void onDestroy() {
		
		if (mTempSMSReceiver != null)
			getActivity().unregisterReceiver(mTempSMSReceiver);
		
		super.onDestroy();
	}
	
	@Override
	protected boolean refreshOptionsMenu() {
		return true;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.main_menu_share, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		 case R.id.share:
			 ShareHelper.handleShare(getActivity());
		}

		return true;
	
	}

	public void setTemp(TempBean tempBean) {

		if (tempBean != null) 
			tempManager.save(tempBean);

		initData();
	}

	// 验证手机格式
	public static boolean checkMobile(String mobile) {

		String telRegex = "[1]\\d{10}";
		if (TextUtils.isEmpty(mobile))
			return false;
		else
			return mobile.matches(telRegex);
	}

	public void updateWidgetEnable() {
		btnOpenWindow.setEnabled(checkMotor.isChecked());
		btnCloseWindow.setEnabled(checkMotor.isChecked());
		btnStop.setEnabled(checkMotor.isChecked());
		window1.setEnabled(checkMotor.isChecked());
		window2.setEnabled(checkMotor.isChecked());
		window3.setEnabled(checkMotor.isChecked());

		btnSendControl.setEnabled(checkMotor.isChecked()
				|| checkTem.isChecked());

		btnMatch.setEnabled(checkMatch.isChecked());
	}
	
	@Override
	public void initView(View mcView) {
		
		checkTem = (CheckBox) mcView.findViewById(R.id.mc_check_tem);
		checkMotor = (CheckBox) mcView
				.findViewById(R.id.mc_check_control_motor);
		lastTempTime = (TextView) mcView
				.findViewById(R.id.nc_title_current_tem);
		window1 = (CheckBox) mcView.findViewById(R.id.mc_checkbox_w1);
		window2 = (CheckBox) mcView.findViewById(R.id.mc_checkbox_w2);
		window3 = (CheckBox) mcView.findViewById(R.id.mc_checkbox_w3);
		phonenumber = (CheckBox) mcView.findViewById(R.id.mc_check_phonenum);
		phonenumber2 = (CheckBox) mcView.findViewById(R.id.mc_check_phonenum2);
		phonenumber3 = (CheckBox) mcView.findViewById(R.id.mc_check_phonenum3);
		openInterval = (CheckBox) mcView.findViewById(R.id.mc_check_open_len);
		tempMin = (CheckBox) mcView.findViewById(R.id.mc_check_min_tem);
		tempMinThreshold = (CheckBox) mcView
				.findViewById(R.id.mc_check_min_threshold);
		tempMax = (CheckBox) mcView.findViewById(R.id.mc_check_max_tem);
		tempMaxThreshold = (CheckBox) mcView
				.findViewById(R.id.mc_check_max_threshold);
		morningTimePeroid = (CheckBox) mcView
				.findViewById(R.id.mc_check_vent_time);
		morningOpenTime = (CheckBox) mcView
				.findViewById(R.id.mc_check_open_time);
		nightCloseTime = (CheckBox) mcView
				.findViewById(R.id.mc_check_close_time);
		closeInterval = (CheckBox) mcView.findViewById(R.id.mc_check_close_len);
		checkMatch = (CheckBox) mcView.findViewById(R.id.mc_text_match);
		selectedAll = (CheckBox) mcView
				.findViewById(R.id.mc_check_selected_all);
		typeGroup = (RadioGroup) mcView.findViewById(R.id.mc_radio_mode);
		btnOpenWindow = (RadioButton) mcView
				.findViewById(R.id.mc_radio_btn_open);
		btnCloseWindow = (RadioButton) mcView
				.findViewById(R.id.mc_radio_btn_close);
		btnStop = (RadioButton) mcView.findViewById(R.id.mc_radio_btn_stop);
		btnModeAuto = (RadioButton) mcView.findViewById(R.id.mc_radio_btn_auto);
		receiveMsg = (TextView) mcView.findViewById(R.id.mc_text_receive_msg);
		btnMatch = (TextView) mcView.findViewById(R.id.mc_btn_match);
		btnSendSetting = (TextView) mcView
				.findViewById(R.id.mc_btn_send_setting);
		btnSendMode = (TextView) mcView.findViewById(R.id.mc_btn_send_mode);

		btnSendControl = (TextView) mcView.findViewById(R.id.mc_btn_send);
		btnShowSetting = (TextView) mcView.findViewById(R.id.mc_text_ms);
		settingContext = (LinearLayout) mcView.findViewById(R.id.mc_setting_l1);
		ScrollView outer = (ScrollView) mcView.findViewById(R.id.outer);
		btnGetTemp = (TextView) mcView.findViewById(R.id.nc_btn_temp);
		InnerScrollView inner = (InnerScrollView) mcView
				.findViewById(R.id.mc_inner_scroll);
		tempLinear = mcView.findViewById(R.id.temp_ll);
		tempLinear2 = mcView.findViewById(R.id.temp_ll3);

		inner.setParentScrollView(outer);
		
		temp = new TempWindow[9];
		tempManager = new TempManager();

		temp[Constants.WINDOW_ID_1] = (TempWindow) mcView
				.findViewById(R.id.temp_win1);
		temp[Constants.WINDOW_ID_1].setWindowId(Constants.WINDOW_ID_1);
		temp[Constants.WINDOW_ID_2] = (TempWindow) mcView
				.findViewById(R.id.temp_win2);
		temp[Constants.WINDOW_ID_2].setWindowId(Constants.WINDOW_ID_2);
		temp[Constants.WINDOW_ID_3] = (TempWindow) mcView
				.findViewById(R.id.temp_win3);
		temp[Constants.WINDOW_ID_3].setWindowId(Constants.WINDOW_ID_3);
		temp[Constants.WINDOW_ID_4] = (TempWindow) mcView
				.findViewById(R.id.temp_win4);
		temp[Constants.WINDOW_ID_4].setWindowId(Constants.WINDOW_ID_4);
		temp[Constants.WINDOW_ID_5] = (TempWindow) mcView
				.findViewById(R.id.temp_win5);
		temp[Constants.WINDOW_ID_5].setWindowId(Constants.WINDOW_ID_5);
		temp[Constants.WINDOW_ID_6] = (TempWindow) mcView
				.findViewById(R.id.temp_win6);
		temp[Constants.WINDOW_ID_6].setWindowId(Constants.WINDOW_ID_6);
		temp[Constants.WINDOW_ID_7] = (TempWindow) mcView
				.findViewById(R.id.temp_win7);
		temp[Constants.WINDOW_ID_7].setWindowId(Constants.WINDOW_ID_7);
		temp[Constants.WINDOW_ID_8] = (TempWindow) mcView
				.findViewById(R.id.temp_win8);
		temp[Constants.WINDOW_ID_8].setWindowId(Constants.WINDOW_ID_8);
		temp[Constants.WINDOW_ID_9] = (TempWindow) mcView
				.findViewById(R.id.temp_win9);
		temp[Constants.WINDOW_ID_9].setWindowId(Constants.WINDOW_ID_9);

		checkTem.setOnCheckedChangeListener(this);
		window1.setOnCheckedChangeListener(this);
		window2.setOnCheckedChangeListener(this);
		window3.setOnCheckedChangeListener(this);
		checkMatch.setOnCheckedChangeListener(this);
		checkMotor.setOnCheckedChangeListener(this);
		btnShowSetting.setOnClickListener(this);
		btnSendControl.setOnClickListener(this);
		btnSendSetting.setOnClickListener(this);
		btnSendMode.setOnClickListener(this);
		btnMatch.setOnClickListener(this);
		btnGetTemp.setOnClickListener(this);
		mcView.findViewById(R.id.mc_btn_closeall).setOnClickListener(this);
		mcView.findViewById(R.id.mc_btn_closeall_rain).setOnClickListener(this);
		mcView.findViewById(R.id.mc_btn_openall).setOnClickListener(this);
		mcView.findViewById(R.id.mc_btn_stopall).setOnClickListener(this);
		selectedAll.setOnCheckedChangeListener(new SelectedAllCheckChangeLintener());
	}

	@Override
	public void initData() {
		
		PengInfo info = AppContext.context().getCurrPengInfo();
		int len = info.getWindowCount();

		for (int i = 0; i < temp.length; i++) {
			if (i < len) {
				temp[i].setVisibility(View.VISIBLE);
			} else {
				temp[i].setVisibility(View.INVISIBLE);
			}
		}

		if (len <= 3) {
			tempLinear.setVisibility(View.GONE);
		} else {
			tempLinear.setVisibility(View.VISIBLE);
		}

		if (len > 6) {
			tempLinear2.setVisibility(View.VISIBLE);
		} else {
			tempLinear2.setVisibility(View.GONE);
		}
		
		TempBean tempBean = tempManager.get(info.getId());
		
		if(tempBean.isVaild())
			lastTempTime.setText(CommonTool.getLastUpdateTime(new Date(tempBean
					.getTime())));
		else
			lastTempTime.setText("");
		
		Iterator<TempTuple> it = tempBean.iterator();
		
		for(int index = 0; it.hasNext(); index++) {
			this.temp[index].setTemp(it.next());
		}
		
		updateWidgetEnable();
		showSettingEnable();

	}

	@Override
	public void onEvent(LocalEvent event) {

		if(event.getEventType() == LocalEvent.EVENT_TYPE_PENG_CHANGE || 
				event.getEventType() == LocalEvent.EVENT_TYPE_PENG_SWITCHING) {

			if(getHoldView() != null)
				initData();
		}
	}

	@Override
	public void onClick(View v) {

		PackageManager pm = getActivity().getPackageManager();
		boolean receivePermission = (PackageManager.PERMISSION_GRANTED == pm
				.checkPermission("android.permission.RECEIVE_SMS",
						"com.smartfarm.view"));
		boolean sendPermission = (PackageManager.PERMISSION_GRANTED == pm
				.checkPermission("android.permission.SEND_SMS",
						"com.smartfarm.view"));

		if (!sendPermission) {
			MessageBox.Show(getActivity(), "没有发送短信的权限！", "权限问题",
					MessageBoxButtons.OK);
			return;
		}

		if (!receivePermission)
			if (MessageBox.DialogResult.Cancel == MessageBox.Show(
					getActivity(), "没有接收短信的权限,软件可能无法识别温控机的回馈信息，确认要发送短信控制吗？",
					"权限问题", MessageBoxButtons.OKCancel))
				return;

		PengInfo info = AppContext.context().getCurrPengInfo();
		String phoneNumberPad = info.getPhoneNum();

		switch (v.getId()) {
		case R.id.mc_btn_closeall:

			ToastTool.showToast("已发送关闭短信!");
			CommonTool.SendSMS(phoneNumberPad, "关闭全部");

			break;
		case R.id.mc_btn_send_mode:
			// 发送模式设置

			ToastTool.showToast("已发送设置模式短信!");
			if (btnModeAuto.isChecked()) {
				CommonTool.SendSMS(phoneNumberPad, "温度自动");
			} else {
				CommonTool.SendSMS(phoneNumberPad, "温度手动");
			}

			break;
		case R.id.mc_btn_closeall_rain:

			ToastTool.showToast("已发送下雨关窗短信!");
			CommonTool.SendSMS(phoneNumberPad, "下雨");

			break;
		case R.id.mc_btn_openall:

			ToastTool.showToast("已发送开窗短信!");
			CommonTool.SendSMS(phoneNumberPad, "打开全部");

			break;
		case R.id.mc_btn_stopall:

			ToastTool.showToast("已发送停止短信!");
			CommonTool.SendSMS(phoneNumberPad, "停止全部");

			break;
		case R.id.nc_btn_temp:
			// 请求温度

			ToastTool.showToast(R.string.requesting_temp);
			CommonTool.SendSMS(phoneNumberPad, "查看");

			break;
		case R.id.mc_text_ms:
			showSetting = !showSetting;
			showSettingEnable();
			break;
		case R.id.mc_btn_send_setting:
			// 发送设置短信

			break;
		case R.id.mc_btn_send:
			// 发送控制短信
			StringBuilder controlMsg = new StringBuilder();

			if (checkTem.isChecked())
				controlMsg.append("查看");

			if (checkMotor.isChecked()) {

				if (checkTem.isChecked())
					controlMsg.append("*");

				switch (typeGroup.getCheckedRadioButtonId()) {
				case R.id.mc_radio_btn_open:
					controlMsg.append("开窗");
					break;
				case R.id.mc_radio_btn_close:
					controlMsg.append("关窗");
					break;
				case R.id.mc_radio_btn_stop:
					controlMsg.append("停机");
					break;
				}

				if (window1.isChecked() || window2.isChecked()
						|| window3.isChecked()) {

					if (window1.isChecked())
						controlMsg.append("1");
					if (window2.isChecked())
						controlMsg.append("2");
					if (window3.isChecked())
						controlMsg.append("3");

				} else {
					ToastTool.showToast(R.string.please_selected_window);
					return;
				}
			}

			CommonTool.SendSMS(phoneNumberPad, controlMsg.toString());

			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		updateWidgetEnable();

		if (buttonView.getId() == R.id.mc_text_match && isChecked) {
			if (!HostInfo.isNetEnabled(getActivity()))
				ToastTool.showToast(R.string.data_link_faild);
		}
	}

	public void showSettingEnable() {
		settingContext.setVisibility(showSetting ? View.VISIBLE : View.GONE);
		selectedAll.setVisibility(showSetting ? View.VISIBLE : View.GONE);
		btnShowSetting.setText(showSetting ? R.string.msg_descr_modify_setting_show
						: R.string.msg_descr_modify_setting_hide);
	}

	class SelectedAllCheckChangeLintener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			phonenumber.setChecked(isChecked);
			phonenumber2.setChecked(isChecked);
			phonenumber3.setChecked(isChecked);
			openInterval.setChecked(isChecked);
			tempMin.setChecked(isChecked);
			tempMinThreshold.setChecked(isChecked);
			tempMax.setChecked(isChecked);
			tempMaxThreshold.setChecked(isChecked);
			morningTimePeroid.setChecked(isChecked);
			morningOpenTime.setChecked(isChecked);
			nightCloseTime.setChecked(isChecked);
			closeInterval.setChecked(isChecked);
		}

	}

	@SuppressLint("HandlerLeak")
	class MineHandler extends Handler {

		public void handleMessage(Message msg2) {

			switch (msg2.what) {
			case TEMPMESSAGE:
				String messagePad = msg2.getData().getString("messagePad");
				
				if(receiveMsg != null)
					receiveMsg.append(messagePad + "\n");

				if (messagePad.contains("T")) {

					setTemp(CommonTool.resolveTemp(messagePad, msg2.arg1, System.currentTimeMillis()));
				}
				break;
			}
			super.handleMessage(msg2);
		}
	};

	class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			SmsMessage msg = null;

			if (bundle != null) {

				Object[] smsObj = (Object[]) bundle.get("pdus");
				for (Object object : smsObj) {
					msg = SmsMessage.createFromPdu((byte[]) object);

					List<PengInfo> allPeng = PengInfoDao.findAll();
					
					for(PengInfo info : allPeng) {
						if (msg.getOriginatingAddress() != null && 
								msg.getOriginatingAddress().contains(info.getPhoneNum())) {

							Message message = new Message();
							Bundle data = new Bundle();

							message.what = TEMPMESSAGE;
							data.putString("messagePad",msg.getDisplayMessageBody());
							message.setData(data);
							message.arg1 = info.getId();

							myHandler.sendMessage(message);

							return;
						}
					}
				}
			}
		}
	}

	
}
