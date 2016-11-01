package com.smartfarm.fragment;

import android.view.View;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.User;
import com.smartfarm.net.ApiContants;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.AvatarView;
/**
 * 个人信息页面
 * @author QSD
 *
 */
public class MeFragment extends BaseFragment {

	private TextView userName;
	private TextView userPhone;
	private AvatarView userFace;
	
	private boolean firstCreate = true;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_me;
	}

	@Override
	public void initView(View view) {

		userFace = (AvatarView) view.findViewById(R.id.me_iv_user);
		userName = (TextView) view.findViewById(R.id.me_tv_name);
		userPhone = (TextView) view.findViewById(R.id.me_tv_account);
		
		view.findViewById(R.id.me_btn_msg).setOnClickListener(this);
		view.findViewById(R.id.me_btn_fans).setOnClickListener(this);
		view.findViewById(R.id.me_btn_note).setOnClickListener(this);
		view.findViewById(R.id.me_btn_user).setOnClickListener(this);
		view.findViewById(R.id.me_btn_watch).setOnClickListener(this);
		view.findViewById(R.id.me_btn_collect).setOnClickListener(this);
		
		userFace.setClickable(false);
	}

	@Override
	public void initData() {
		
		User user = AppContext.context().getUser();
		
		if(user != null) {

	        userFace.setAvatarUrl(user.getFace());	
			userName.setText(user.getName());
			userPhone.setText(user.getPhone());
		} else {
			
			userName.setText("");
			userPhone.setText("");
		}	
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		
		case R.id.me_btn_msg:
			
			break;
		case R.id.me_btn_note:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.MY_NOTE);
			break;
		case R.id.me_btn_user:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.COMMUNITY_USER);
			break;
		case R.id.me_btn_watch:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.USER_LIST, ApiContants.USER_WATCH);
			break;
		case R.id.me_btn_fans:

			UIHelper.showSimpleBack(getActivity(), BackPage.USER_LIST, ApiContants.USER_FAN);
			break;
		case R.id.me_btn_collect:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.COLLECT);
			break;
		}
	}

	@Override
	public void onResume() {
		
		if(firstCreate) 
			firstCreate = false;
		else 
			initData();
		
		super.onResume();
	}
}
