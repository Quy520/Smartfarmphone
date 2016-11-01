package com.smartfarm.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.view.R;
import com.smartfarm.widget.MainTab;
import com.smartfarm.widget.MyFragmentTabHost;

public class TempControlFragment extends BaseFragment implements OnTabChangeListener {

	private static final int TIME_ANIMATION = 200;
	
	private MyFragmentTabHost mTabHost;
	private String[] mHeadTitles;

	private CharSequence mTitle = "";
	private boolean isToolsHide = false;
	private float lastY = 0;
	private boolean animationBeing = false;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_temp_control;
	}

	@Override
	public void initView(View view) {
		
		mTabHost = (MyFragmentTabHost) view.findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
		if (android.os.Build.VERSION.SDK_INT > 10) {
			mTabHost.getTabWidget().setShowDividers(0);
		}

		mTabHost.setCurrentTab(0);
		mTabHost.setOnTabChangedListener(this);
		
		initTabs();
	}

	@Override
	public void initData() {
		super.initData();
	}
	
	public void restoreActionBar() {
		
		if(!ActionBarActivity.class.isInstance(getActivity())) 
			return;
		
		ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		Fragment fragment = getCurrentFragment();
		
		if(fragment != null)
			fragment.onCreateOptionsMenu(menu, inflater);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Fragment fragment = getCurrentFragment();
		
		if(fragment != null)
			fragment.onOptionsItemSelected(item);
			
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			
			lastY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:

			float disY = ev.getY() - lastY;

			if (Math.abs(disY) > 25) {

				if (disY < 0) {
					if (!isToolsHide)
						hideTools();
				} else {
					if (isToolsHide)
						showTools();
				}
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@SuppressLint("InflateParams") 
	private void initTabs() {
		MainTab[] tabs = MainTab.values();
		final int size = tabs.length;

		mHeadTitles = new String[size];
		for (int i = 0; i < size; i++) {
			MainTab mainTab = tabs[i];
			TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
			View indicator = LayoutInflater.from(getActivity())
					.inflate(R.layout.tab_indicator, null);
			TextView title = (TextView) indicator.findViewById(R.id.tab_title);
			Drawable drawable = this.getResources().getDrawable(mainTab.getResIcon());
			title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

			mHeadTitles[i] = getString(mainTab.getResName());
			title.setText(getString(mainTab.getResName()));
			tab.setIndicator(indicator);
			tab.setContent(new TabContentFactory() {

				@Override
				public View createTabContent(String tag) {
					return new View(getActivity());
				}
			});
			mTabHost.addTab(tab, mainTab.getClz(), null);
		}
	}

	private void showTools() {

		if (!isToolsHide || animationBeing)
			return;

		animationBeing = true;
		
		ObjectAnimator anim = ObjectAnimator.ofFloat(mTabHost, "y",
				mTabHost.getY(), mTabHost.getY() - mTabHost.getHeight());
		anim.setDuration(TIME_ANIMATION);
		anim.addListener(animatorLinstener);
		anim.start();

		isToolsHide = false;
	}

	private void hideTools() {

		if (isToolsHide || animationBeing)
			return;

		animationBeing = true;

		ObjectAnimator anim = ObjectAnimator.ofFloat(mTabHost, "y",
				mTabHost.getY(), mTabHost.getY() + mTabHost.getHeight());
		anim.setDuration(TIME_ANIMATION);
		anim.addListener(animatorLinstener);
		anim.start();
		isToolsHide = true;
	}

	public Fragment getCurrentFragment() {
		return getChildFragmentManager().findFragmentByTag(
				mTabHost.getCurrentTabTag());
	}
	
	private AnimatorListenerAdapter animatorLinstener = new AnimatorListenerAdapter() {

		@Override
		public void onAnimationEnd(Animator animation) {

			animationBeing = false;
			
			super.onAnimationEnd(animation);
		}
	};
	   
	@Override
	public void onTabChanged(String tabId) {

		final int size = mTabHost.getTabWidget().getTabCount();
		int viewIndex = 0;

		for (int i = 0; i < size; i++) {
			View v = mTabHost.getTabWidget().getChildAt(i);
			if (i == mTabHost.getCurrentTab()) {
				v.setSelected(true);
				viewIndex = i;
			} else {
				v.setSelected(false);
			}
		}

		mTitle = mHeadTitles[viewIndex];
		CommonTool.HideKb(getActivity(), getActivity().getCurrentFocus());
		getActivity().supportInvalidateOptionsMenu();
	}
	
}
