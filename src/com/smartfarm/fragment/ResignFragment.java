package com.smartfarm.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import com.smartfarm.base.BaseFragment;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.control.MyCountTimer;

public class ResignFragment extends BaseFragment {

	private EditText nickName;
	private EditText userName;
	private EditText userPwd;
	private EditText userPwdAgain;
	private EditText verCode;
	private View userNameClear;
	private View userPwdClearAgain;
	private View userPwdClear;
	private Button sendCode;
	private Button sendVoiceCode;
	private MyCountTimer timeCount;
	private MyCountTimer voiceCount;
	private BroadcastReceiver mMsgReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_resign, container, false);

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

		nickName = (EditText) view.findViewById(R.id.login_user_nickname);
		userName = (EditText) view.findViewById(R.id.login_user_name);
		userPwd = (EditText) view.findViewById(R.id.login_user_pwd);
		userPwdAgain = (EditText) view.findViewById(R.id.login_user_pwd_again);
		verCode = (EditText) view.findViewById(R.id.login_user_code);
		userNameClear = view.findViewById(R.id.login_name_clear);
		userPwdClear = view.findViewById(R.id.login_pwd_clear);
		userPwdClearAgain = view.findViewById(R.id.login_pwd_clear_again);
		sendCode = (Button) view.findViewById(R.id.register_send_code);
		sendVoiceCode = (Button) view.findViewById(R.id.voice_code);

		userNameClear.setOnClickListener(this);
		userPwdClear.setOnClickListener(this);
		userPwdClearAgain.setOnClickListener(this);
		sendCode.setOnClickListener(this);
		sendVoiceCode.setOnClickListener(this);
		view.findViewById(R.id.login_btn).setOnClickListener(this);
	}

	@Override
	public void initData() {
		timeCount = new MyCountTimer(60000, 1000, sendCode, "获取短信验证码");
		voiceCount = new MyCountTimer(60000, 1000, sendVoiceCode,
				"短信验证码收不到嘛？可以试试语音验证码");
	}

	@Override
    public void onClick(View v) {
		String phone = userName.getText().toString();
        switch (v.getId()) {
        case R.id.login_btn:
        	
        	if(!prepareForLogin())
        		return;

        	showLoadingDialog();
        	
        	String account = userName.getText().toString();
        	String pwd = userPwd.getText().toString();
        	String code = verCode.getText().toString();
        	String name = nickName.getText().toString();
        	if(name.isEmpty()){
        		name = (int)(Math.random() * 100000) + "phone";
        	}
        	if(account.isEmpty()){
        		ToastTool.showToast("请输入手机号码");
        		return;
        	}
        	if(pwd.isEmpty()){
        		ToastTool.showToast("请输入输入密码");
        		return;
        	}
        	if(code.isEmpty()){
        		ToastTool.showToast("请输入验证码");
        		return;
        	}
        	SmartfarmNetHelper.appResign(name,account, 
        			Md5Utils.encode(Md5Utils.encode(pwd) + account),code, new BaseAsyncHttpResHandler() {
				@Override
				public void onSuccess(ApiResponse res) {
					hideLoadingDialog();
	        		ToastTool.showToastLong("注册成功!请登录...");
		        	getActivity().finish();
				}
				
				@Override
				public void onFailure(int errorCode, String data) {
					hideLoadingDialog();
//					ToastTool.showToastLong("网络连接失败，错误代码:" + errorCode);
				}

				@Override
				public void onFinish() {
					hideLoadingDialog();
				}
			});
        	
        	break;
        case R.id.login_name_clear:
        	
        	userName.setText("");
        	userName.requestFocus();
        	break;
        case R.id.login_pwd_clear:

        	userPwd.setText("");
        	userPwdAgain.setText("");
        	userPwd.requestFocus();
        	break;
        case R.id.login_pwd_clear_again:

        	userPwdAgain.setText("");
        	userPwdAgain.requestFocus();
        	break;
        	
        case R.id.register_send_code:
			if (phone.isEmpty()) {
				ToastTool.showToast("请输入手机号码！");
				userName.requestFocus();
				return;
			}
			AppContext.context().setPhone(phone);
			showLoadingDialog();
			sendVoiceCode.setVisibility(View.VISIBLE);
			SmartfarmNetHelper.getSmsVerCode(phone,new BaseAsyncHttpResHandler() {

				@Override
				public void onSuccess(ApiResponse res) {
					hideLoadingDialog();
					timeCount.start();
					ToastTool.showToast("短信已发送，请注意查收");
				}
				@Override
				public void onFailure(int errorCode, String data) {
					hideLoadingDialog();
//					ToastTool.showToast("网络连接失败，错误代码:" + errorCode);
				}
			});
    	break;
        	
        case R.id.voice_code:
        	
			if (phone.isEmpty()) {
				ToastTool.showToast("请输入手机号码！");
				userName.requestFocus();
				return;
			}
			AppContext.context().setPhone(phone);
			showLoadingDialog();
			
			Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setMessage("是否需要发送语音验证码？");
			dialog.setTitle("提示").setPositiveButton("确认",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SmartfarmNetHelper.getVoiceVerCode(userName.getText().toString(),
							new BaseAsyncHttpResHandler() {
								@Override
								public void onSuccess(ApiResponse res) {
									hideLoadingDialog();
									voiceCount.start();
									ToastTool.showToast("语音已发送，请注意查收");
								}

								@Override
								public void onFailure(int errorCode, String data) {
									hideLoadingDialog();
//									ToastTool.showToast("网络连接失败，错误代码:" + errorCode);
								}
							});
				}
			});
			dialog.setNegativeButton("取消", null);
			dialog.show();
        	break;
       
        default:
            break;
        }
    }

	private boolean prepareForLogin() {

		if (!CommonTool.isNetworkConnected()) {
			ToastTool.showToast("网络异常，请检查网络！");
			return false;
		}

		if (ShowUtil.isEmpty(nickName.getText().toString())) {
			ToastTool.showToast("请输入用户名！");
			nickName.requestFocus();
			return false;
		}

		String uName = userName.getText().toString();
		if (ShowUtil.isEmpty(uName)) {
			ToastTool.showToast("请输入手机号码！");
			userName.requestFocus();
			return false;
		}

		String pwd = userPwd.getText().toString();
		if (ShowUtil.isEmpty(pwd)) {
			ToastTool.showToast("请输入密码！");
			userPwd.requestFocus();
			return false;
		}

		String pwdAgain = userPwdAgain.getText().toString();
		if (ShowUtil.isEmpty(pwd)) {
			ToastTool.showToast("请确认输入密码！");
			userPwd.requestFocus();
			return false;
		} else if (!pwdAgain.equals(pwd)) {
			ToastTool.showToast("两次密码输入不一致！");
			userPwdAgain.requestFocus();
			return false;
		}
		String code = verCode.getText().toString();
		if (code.isEmpty()) {
			ToastTool.showToast("请输入验证码");
			verCode.requestFocus();
			return false;
		}

		return true;
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
					if (yzm.matches("【上海洲涛】.*")) {
						verCode.setText(getNumbers(msg.getDisplayMessageBody()));
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
