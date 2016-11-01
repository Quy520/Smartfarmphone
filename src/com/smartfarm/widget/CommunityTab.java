package com.smartfarm.widget;

import com.smartfarm.fragment.BrowseFragment;
import com.smartfarm.fragment.FindFragment;
import com.smartfarm.fragment.MeFragment;
import com.smartfarm.fragment.NewsTabFragment;
import com.smartfarm.view.R;


public enum CommunityTab {

	BROWSE(0, R.string.tab_browse, R.drawable.tab_browse_selector, BrowseFragment.class),
	NEWS(1, R.string.tab_news, R.drawable.tab_news_selector, NewsTabFragment.class),
	QUICK(2, R.string.tab_quick, R.drawable.tab_browse_selector, null),
	FIND(3, R.string.tab_find, R.drawable.tab_find_selector, FindFragment.class),
	ME(4, R.string.tab_me, R.drawable.tab_me_selector, MeFragment.class);


	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private CommunityTab(int idx, int resName, int resIcon, Class<?> clz) {
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
