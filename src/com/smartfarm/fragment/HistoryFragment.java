package com.smartfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ConfigManager;
import com.smartfarm.view.util.UIHelper;
/**
 * 历史消息界面
 * @author QSD
 *
 */
public class HistoryFragment extends BaseFragment {
	
	private View view;
	
	private TextView water;
	private TextView light;
	private View waterDividing;
	private View lightDividing;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(view == null) {

			view = inflater.inflate(R.layout.fragment_history, container, false);
			
			water = (TextView) view.findViewById(R.id.history_water);
			light = (TextView) view.findViewById(R.id.history_light);
			waterDividing = view.findViewById(R.id.history_water_dividing);
			lightDividing = view.findViewById(R.id.history_light_dividing);

			water.setOnClickListener(this);
			light.setOnClickListener(this);
			view.findViewById(R.id.history_temp).setOnClickListener(this);
			view.findViewById(R.id.history_alarm).setOnClickListener(this);
		}

        return view;
	}

	@Override
	public void onResume() {

		int showWater = ConfigManager.getInstance().getBoolean(
				ConfigManager.KEY_ENABLE_WATER) ? View.VISIBLE : View.GONE;
		
		water.setVisibility(showWater);
		waterDividing.setVisibility(showWater);

		int showLight = ConfigManager.getInstance().getBoolean(
				ConfigManager.KEY_ENABLE_LIGHT) ? View.VISIBLE : View.GONE;
		
		light.setVisibility(showLight);
		lightDividing.setVisibility(showLight);
		
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.history_temp:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.TEMP_MANAGER, InfoRecord.RECORD_TYPE_TEMP);
			break;
		case R.id.history_alarm:

			UIHelper.showSimpleBack(getActivity(), BackPage.TEMP_MANAGER, InfoRecord.RECORD_TYPE_ALARM);
			break;
		case R.id.history_water:

			UIHelper.showSimpleBack(getActivity(), BackPage.TEMP_MANAGER, InfoRecord.RECORD_TYPE_WATER);
			break;
		case R.id.history_light:

			UIHelper.showSimpleBack(getActivity(), BackPage.TEMP_MANAGER, InfoRecord.RECORD_TYPE_LIGHT);
			break;
		}
	}
}
