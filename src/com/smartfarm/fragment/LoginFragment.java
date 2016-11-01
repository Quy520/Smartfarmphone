package com.smartfarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.User;
import com.smartfarm.db.util.UserDao;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.ApiUser;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.SimpleTextWatcher;
import com.smartfarm.view.util.UIHelper;

public class LoginFragment extends BaseFragment {

	private EditText userName;
	private EditText userPwd;
	private View userNameClear;
	private View userPwdClear;
	
	
	private final TextWatcher mUserNameWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        	userNameClear.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
        }
    };
    
    private final TextWatcher mPassswordWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        	userPwdClear.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
        }
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initView(view);
        initData();
        
        return view;
    }

    @Override
    public void initView(View view) {
    	
    	userName = (EditText) view.findViewById(R.id.login_user_name);
    	userPwd = (EditText) view.findViewById(R.id.login_user_pwd);
    	userNameClear = view.findViewById(R.id.login_name_clear);
    	userPwdClear = view.findViewById(R.id.login_pwd_clear);
    	
    	userName.addTextChangedListener(mUserNameWatcher);
    	userPwd.addTextChangedListener(mPassswordWatcher);
    	userNameClear.setOnClickListener(this);
    	userPwdClear.setOnClickListener(this);
        view.findViewById(R.id.login_btn).setOnClickListener(this);
        view.findViewById(R.id.login_to_resign).setOnClickListener(this);
        view.findViewById(R.id.login_to_forget).setOnClickListener(this);
    }
    
	private void prepareOk(String salt) {

		final String mPwd = Md5Utils.encode(Md5Utils.encode(userPwd.getText()
				.toString()) + userName.getText().toString());

		SmartfarmNetHelper.appLogin(Md5Utils.encode(mPwd + salt), new BaseAsyncHttpResHandler() {

				@Override
				public void onFailure(int errorCode, String data) {
					hideLoadingDialog();
//					ToastTool.showToastLong("登录失败，错误代码:" + errorCode);
				}

				@Override
				public void onSuccess(ApiResponse res) {
					hideLoadingDialog();
					ToastTool.showToastLong("登录成功");
					ApiUser user = JSON.toJavaObject((JSON) res.getResponseData(), ApiUser.class);
					
					User localUser = user.getUser();
						
					localUser = UserDao.add(localUser);
					AppContext.context().getAccountManager().save(localUser, user.getPhone(),
							Md5Utils.encode(mPwd), user.getToken(), user.getRefreshToken());

					CommonTool.HideKb(getActivity(), getView());
					UIHelper.showMainView(getActivity());
//					getActivity().finish();
					
				}
				
				@Override
				public void onFinish() {
					
				}
			});

	}
    
    @Override
    public void onClick(View v) {
    	
        switch (v.getId()) {
        case R.id.login_btn:
        	
        	if(!prepareForLogin())
        		return;
        	
        	showLoadingDialog();
        	
        	SmartfarmNetHelper.appLoginPrepare(userName.getText().toString(), new BaseAsyncHttpResHandler() {

				@Override
				public void onFailure(int errorCode, String data) {
					hideLoadingDialog();
//					ToastTool.showToastLong("网络连接失败，错误代码：" + errorCode);
					
				}

				@Override
				public void onSuccess(ApiResponse res) {
					prepareOk(res.getResponseData().toString());
					return;
					
				}
        	});
        	
        	break;
        case R.id.login_to_resign:
        	
        	UIHelper.showSimpleBack(getActivity(), BackPage.RESIGN);
        	break;
        case R.id.login_name_clear:
        	
        	userName.setText("");
        	userName.requestFocus();
        	break;
        case R.id.login_pwd_clear:

        	userPwd.setText("");
        	userPwd.requestFocus();
        	break;
        	
        case R.id.login_to_forget:
        	
        	UIHelper.showSimpleBack(getActivity(), BackPage.FORGET_PWD);
        	break;
        default:
            break;
        }
    }
    
    private boolean prepareForLogin() {
    	
    	if(!CommonTool.isNetworkConnected()) {
    		ToastTool.showToast("网络异常，请检查网络！");
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
        
        return true;
    }

	@Override
	public boolean onBackPressed() {

		UIHelper.showMainView(getActivity());
    	
		return true;
	}
}
