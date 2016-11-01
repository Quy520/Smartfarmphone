package com.smartfarm.widget;

import com.smartfarm.fragment.MsgControlFragment;
import com.smartfarm.fragment.NetControlFragment;
import com.smartfarm.fragment.SettingFragment;
import com.smartfarm.view.R;

public enum MainTab {

	NET_CONTROL(0, R.string.tab_net_control, R.drawable.net_control_icon,
			NetControlFragment.class),

	MSG_CONTROL(1, R.string.tab_msg_control, R.drawable.messages2,
			MsgControlFragment.class),

	SETTING(2, R.string.tab_setting, R.drawable.setting_tab_icon,
			SettingFragment.class);

	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
