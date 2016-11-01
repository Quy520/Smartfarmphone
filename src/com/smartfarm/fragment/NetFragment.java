package com.smartfarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.view.R;

public class NetFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_net_control, container,
				false);

		initView(view);
		initData();
		return view;
	}
}
