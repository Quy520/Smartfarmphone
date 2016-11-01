package com.smartfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.UIHelper;

public class HelpFragment extends BaseFragment {

	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(view == null) {

			view = inflater.inflate(R.layout.fragment_help, container, false);

			view.findViewById(R.id.help_title_msg_cmd).setOnClickListener(this);
			view.findViewById(R.id.help_title_msg_control).setOnClickListener(this);
			view.findViewById(R.id.help_title_msg_temp).setOnClickListener(this);
			view.findViewById(R.id.help_title_net_alarm).setOnClickListener(this);
			view.findViewById(R.id.help_title_net_diagnosis).setOnClickListener(this);
			view.findViewById(R.id.help_title_net_mode).setOnClickListener(this);
			view.findViewById(R.id.help_title_net_relink).setOnClickListener(this);
			view.findViewById(R.id.help_title_net_syn).setOnClickListener(this);
			view.findViewById(R.id.help_title_net_temp).setOnClickListener(this);
			view.findViewById(R.id.help_title_net_ven).setOnClickListener(this);
			view.findViewById(R.id.help_title_net_win).setOnClickListener(this);
			view.findViewById(R.id.help_title_precautions).setOnClickListener(this);
			view.findViewById(R.id.help_title_set_function).setOnClickListener(this);
			view.findViewById(R.id.help_title_set_house).setOnClickListener(this);
			view.findViewById(R.id.help_title_set_win).setOnClickListener(this);
			view.findViewById(R.id.help_title_setting).setOnClickListener(this);
			view.findViewById(R.id.help_title_share).setOnClickListener(this);
		}

        return view;
	}

	@Override
	public void onClick(View v) {
		UIHelper.showSimpleBack(getActivity(), BackPage.HELP_DETAIL, v.getId());
	}
}
