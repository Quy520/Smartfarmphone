package com.smartfarm.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.SimpleTextWatcher;
import com.smartfarm.view.util.UIHelper;

public class LoginPhoneFragment extends BaseFragment {
	
	private EditText userPhone;
	private View userPhoneClear;
	private String phone;
	
	private final TextWatcher mUserPhoneWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        	userPhoneClear.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
        }
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_login_phone, container, false);
        initView(view);
        initData();
        return view;
	}
	
	@Override
	public void initView(View view) {
		userPhone = (EditText) view.findViewById(R.id.login_user_name);
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		view.findViewById(R.id.login_to_resign).setOnClickListener(this);
		userPhone.addTextChangedListener(mUserPhoneWatcher);
		userPhoneClear = view.findViewById(R.id.login_name_clear);
		userPhoneClear.setOnClickListener(this);
	}
	
	@Override
	public void initData() {
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_name_clear:
			userPhone.setText("");
			userPhone.requestFocus();
			break;
			
		case R.id.login_btn:
			phone = userPhone.getText().toString();
			AppContext.context().setPhone(phone);
			if(phone.length()<10){
				ToastTool.showToast("手机号码输入错误，请重新输入");
				userPhone.requestFocus();
				return;
			}
			
			AppContext.context().setPhone(phone);
			showLoadingDialog();
			SmartfarmNetHelper.getSmsVerCode(phone,
					new BaseAsyncHttpResHandler() {
						@Override
						public void onSuccess(ApiResponse res) {
							 hideLoadingDialog();
							 ToastTool.showToast("短信已发送，请注意查收");
							 UIHelper.showSimpleBack(getActivity(), BackPage.LOING_NEW);
							 AppContext.context().setMsgTime(System.currentTimeMillis() - 60 * 1000);
						}

						@Override
						public void onFailure(int errorCode, String data) {
							hideLoadingDialog();
//							ToastTool.showToast("网络连接失败，错误代码:" + errorCode);
						}
					});
			
			break;
			
		case R.id.login_to_resign:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN);
			break;
		}
	}
}
