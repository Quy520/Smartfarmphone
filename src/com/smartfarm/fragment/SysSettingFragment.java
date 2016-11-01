package com.smartfarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ConfigManager;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.togglebutton.ToggleButton;
import com.smartfarm.widget.togglebutton.ToggleButton.OnToggleChanged;

public class SysSettingFragment extends BaseFragment {
	
	private ToggleButton sound;
	private ToggleButton vibrate;
	private ToggleButton welcome;
	private ToggleButton waterEnable;
	private ToggleButton lightEnable;
	private ToggleButton imageEnable;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);
		initView(view);
		initData();
		return view;
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	public void initView(View view) {
		
		sound = (ToggleButton) view.findViewById(R.id.sys_setting_btn_sound);
		vibrate = (ToggleButton) view.findViewById(R.id.sys_setting_btn_vibrate);
		welcome = (ToggleButton) view.findViewById(R.id.sys_setting_btn_welcome);
		waterEnable = (ToggleButton) view.findViewById(R.id.sys_setting_btn_water);
		lightEnable = (ToggleButton) view.findViewById(R.id.sys_setting_btn_light);
		imageEnable = (ToggleButton) view.findViewById(R.id.sys_setting_btn_net);
		
		sound.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				ConfigManager.getInstance().putBoolean(ConfigManager.KEY_ENABLE_SOUND, on);
			}
		});

		vibrate.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				ConfigManager.getInstance().putBoolean(ConfigManager.KEY_ENABLE_VIBRATE, on);
			}
		});

		imageEnable.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				ConfigManager.getInstance().putBoolean(ConfigManager.KEY_LOAD_IMG, on);
			}
		});

		welcome.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				ConfigManager.setWelcomeEnable(getActivity(), on);
			}
		});

		waterEnable.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {

				ConfigManager.getInstance().putBoolean(ConfigManager.KEY_ENABLE_WATER, on);
			}
		});

		lightEnable.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {

				ConfigManager.getInstance().putBoolean(ConfigManager.KEY_ENABLE_LIGHT, on);
			}
		});

		view.findViewById(R.id.sys_setting_about).setOnClickListener(this);
		view.findViewById(R.id.sys_setting_exit).setOnClickListener(this);
		view.findViewById(R.id.sys_setting_sound).setOnClickListener(this);
		view.findViewById(R.id.sys_setting_vibrate).setOnClickListener(this);
		view.findViewById(R.id.sys_setting_welcome).setOnClickListener(this);
		view.findViewById(R.id.sys_setting_water).setOnClickListener(this);
		view.findViewById(R.id.sys_setting_light).setOnClickListener(this);
		view.findViewById(R.id.sys_setting_net).setOnClickListener(this);
	}

	@Override
	public void initData() {

		if(ConfigManager.getInstance().hasSound())
			sound.setToggleOn();
		else
			sound.setToggleOff();

		if(ConfigManager.getInstance().hasVibrate())
			vibrate.setToggleOn();
		else
			vibrate.setToggleOff();

		if(ConfigManager.getWelcomeEnable(getActivity()))
			welcome.setToggleOn();
		else
			welcome.setToggleOff();
		
		if (ConfigManager.getInstance().getBoolean(
				ConfigManager.KEY_ENABLE_WATER))
			waterEnable.setToggleOn();
		else
			waterEnable.setToggleOff();

		if (ConfigManager.getInstance().getBoolean(
				ConfigManager.KEY_ENABLE_LIGHT))
			lightEnable.setToggleOn();
		else
			lightEnable.setToggleOff();

		if (ConfigManager.getInstance().getBoolean(
				ConfigManager.KEY_LOAD_IMG))
			imageEnable.setToggleOn();
		else
			imageEnable.setToggleOff();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
		case R.id.sys_setting_about:

			UIHelper.showSimpleBack(getActivity(), BackPage.ABOUT);
			break;
		case R.id.sys_setting_exit:

			CommonTool.toHome(getActivity());
			break;
		case R.id.sys_setting_sound:
			
			sound.toggle();
			break;
		case R.id.sys_setting_vibrate:
			
			vibrate.toggle();
			break;
		case R.id.sys_setting_welcome:
			
			welcome.toggle();
			break;
		case R.id.sys_setting_water:
			
			waterEnable.toggle();
			break;
		case R.id.sys_setting_light:
			
			lightEnable.toggle();
			break;
		case R.id.sys_setting_net:
			
			imageEnable.toggle();
			break;
		default:
			break;
		}

	}
}
