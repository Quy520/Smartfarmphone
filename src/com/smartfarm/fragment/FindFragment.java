package com.smartfarm.fragment;

import android.view.View;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.view.R;

public class FindFragment extends BaseFragment {

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_find;
	}

	@Override
	public void initView(View view) {

		view.findViewById(R.id.find_btn_activity).setOnClickListener(this);
		view.findViewById(R.id.find_btn_circle).setOnClickListener(this);
		view.findViewById(R.id.find_btn_friend).setOnClickListener(this);
		view.findViewById(R.id.find_btn_mall).setOnClickListener(this);
		view.findViewById(R.id.find_btn_pesticide).setOnClickListener(this);
		view.findViewById(R.id.find_btn_scan).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		
		case R.id.find_btn_activity:
			
			break;
		case R.id.find_btn_circle:

//			UIHelper.showSimpleBack(getActivity(), BackPage.USER_CENTER, ApiContants.USER_CENTER_WATCH);
			break;
		case R.id.find_btn_friend:
			
			break;
		case R.id.find_btn_mall:
			
			break;
		case R.id.find_btn_pesticide:
			
			break;
		case R.id.find_btn_scan:
			
			break;
		}
	}
}
