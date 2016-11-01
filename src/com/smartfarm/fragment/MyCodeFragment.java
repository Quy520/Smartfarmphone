package com.smartfarm.fragment;

import android.view.View;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.User;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.widget.AvatarView;
//我的微信二维码
public class MyCodeFragment extends BaseFragment {

	private TextView userName;
	private TextView userPhone;
	private AvatarView userFace;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_my_code;
	}

	@Override
	public void initView(View view) {

		userFace = (AvatarView) view.findViewById(R.id.me_iv_user);
		userName = (TextView) view.findViewById(R.id.me_tv_name);
		userPhone = (TextView) view.findViewById(R.id.me_tv_account);
		
		userFace.setClickable(false);
	}

	@Override
	public void initData() {

		User user = AppContext.context().getUser();
		
		if(user != null) {
			
			userName.setText(user.getName());
			userPhone.setText(user.getPhone());
			userFace.setAvatarUrl(user.getFace());
		} else {
			
			userName.setText("");
			userPhone.setText("");
		}	
	}

	
}
