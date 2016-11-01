package com.smartfarm.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.User;
import com.smartfarm.db.util.UserDao;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.ApiUser;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.control.MyCountTimer;
import com.smartfarm.view.util.UIHelper;

public class LoginNewFragment extends BaseFragment {

	private EditText userCode;
	private Button sendMsgCode;
	private Button sendVoiceCode;
	private MyCountTimer timeCountMsg;
	private MyCountTimer timeCountVoice;
	private MyCountTimer _timeCountMsg;
	private MyCountTimer _timeCountVoice;
	private String phone = "";
	private BroadcastReceiver mMsgReceiver;
	private long msgTime;
	private TextView userPhone;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_login_new, container,
				false);

		initView(view);
		initData();

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMsgReceiver = new SmsReceiver();
		getActivity().registerReceiver(mMsgReceiver,
				new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	}

	@Override
	public void onDestroy() {
		if (mMsgReceiver != null)
			getActivity().unregisterReceiver(mMsgReceiver);
		super.onDestroy();
	}

	@Override
	public void initView(View view) {
		userCode = (EditText) view.findViewById(R.id.login_user_code);
		sendMsgCode = (Button) view.findViewById(R.id.register_send_code);
		sendVoiceCode = (Button) view.findViewById(R.id.voice_code);
		userPhone = (TextView) view.findViewById(R.id.user_number);
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		sendMsgCode.setOnClickListener(this);
		sendVoiceCode.setOnClickListener(this);
	}

	@Override
	public void initData() {
		phone = AppContext.context().getPhone();
		userPhone.setText(phone);
		msgTime = System.currentTimeMillis(); 
		timeCountMsg = new MyCountTimer(60 * 1000,1000,sendMsgCode,"获取短信验证码");
		timeCountVoice = new MyCountTimer(60 * 1000,1000,sendVoiceCode,"短信验证码收不到？可以试试语音验证码");
		long differ = msgTime - AppContext.context().getMsgTime();
//		long differ_voice = msgTime - AppContext.context().getVoiceTime();
//		if(differ < 60 * 1000){
		_timeCountMsg = new MyCountTimer(differ,1000,sendMsgCode,"获取短信验证码");
		_timeCountMsg.start();
//			return;
//		}
//		if(differ_voice < 60 * 1000){
//			_timeCountVoice = new MyCountTimer(differ_voice,1000,sendVoiceCode,"获取语音验证码");
//			_timeCountVoice.start();
//		}
	}

	private void sendMsg() {
		showLoadingDialog();
		SmartfarmNetHelper.getSmsVerCode(phone,new BaseAsyncHttpResHandler() { 
			
			@Override
			public void onSuccess(ApiResponse res) {
				hideLoadingDialog();
				ToastTool.showToast("短信已发送，请注意查收");
				timeCountMsg.start();
				AppContext.context().setMsgTime(System.currentTimeMillis());
			}
			
			@Override
			public void onFailure(int errorCode, String data) {
				hideLoadingDialog();
			}
		});
		
	}
	
	private void login(){
		String code = userCode.getText().toString();
		
		SmartfarmNetHelper.appRegisterLogin(phone,code,new BaseAsyncHttpResHandler() {
			
			@Override
			public void onSuccess(ApiResponse res) {
				hideLoadingDialog();
				ToastTool.showToast("登录成功");
				ApiUser user = JSON.toJavaObject((JSON)res.getResponseData(), ApiUser.class);
				User localUser = user.getUser();
				
				localUser = UserDao.add(localUser);
				AppContext.context().getAccountManager()
					.save(localUser, user.getPhone(), user.getHxPwd(), user.getToken(),user.getRefreshToken());

				CommonTool.HideKb(getActivity(), getView());
//				getActivity().finish();
				UIHelper.showMainView(getActivity());
			}
			
			@Override
			public void onFailure(int errorCode, String data) {
				hideLoadingDialog();
//				ToastTool.showToast("登陆失败data->"+data+";errorCode->"+errorCode);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_send_code:
			sendMsg();
			break;

		case R.id.voice_code:
			if (phone.isEmpty()) {
				ToastTool.showToast("参数错误");
				getActivity().finish();
				return;
			}
			showLoadingDialog();
			SmartfarmNetHelper.getVoiceVerCode(phone, new BaseAsyncHttpResHandler() {
				@Override
				public void onSuccess(ApiResponse res) {
					hideLoadingDialog();
					ToastTool.showToast("语音已发送，请注意查收");
					AppContext.context().setVoiceTime(System.currentTimeMillis());
					timeCountVoice.start();
				}

				@Override
				public void onFailure(int errorCode, String data) {
					hideLoadingDialog();
//					ToastTool.showToast("网络连接失败，错误代码:" + errorCode);
				}
			});
			break;

		case R.id.login_btn:
			login();
			break;

		default:
			break;
		}
	}

	class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			SmsMessage msg = null;
			if (bundle != null) {
				Object[] smsObj = (Object[]) bundle.get("pdus");
				for (Object object : smsObj) {
					msg = SmsMessage.createFromPdu((byte[]) object);
					String yzm = msg.getDisplayMessageBody();
					if(yzm.matches("【上海洲涛】.*")){
						userCode.setText(getNumbers(msg.getDisplayMessageBody()));
						login();
					}
				}
			}
		}
	}
	
	// 截取数字
	public String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	} 

}
