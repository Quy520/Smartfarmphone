package com.smartfarm.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.smartfarm.base.BaseActivity;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.CommunityTab;
import com.smartfarm.widget.MyFragmentTabHost;
import com.smartfarm.widget.OnTabReselectListener;
import com.smartfarm.widget.dialog.ShareHelper;

@SuppressLint("InflateParams")
public class CommunityActivity extends BaseActivity implements OnTabChangeListener, OnClickListener, OnTouchListener {
    
    private View mAddBt;
    private MyFragmentTabHost mTabHost;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_conmunity;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	public void initView() {
		
		mTabHost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
		mAddBt = findViewById(R.id.quick_option_iv);
		
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		if (android.os.Build.VERSION.SDK_INT > 10) {
			mTabHost.getTabWidget().setShowDividers(0);
		}

		initTabs();

		// 中间按键图片触发
		mAddBt.setOnClickListener(this);

		mTabHost.setCurrentTab(0);
		mTabHost.setOnTabChangedListener(this);

		handleIntent(getIntent());
	}

	@Override
	public void initData() {
		
		getActionBar().setTitle(R.string.community_title);
	}
	
	@SuppressLint("InflateParams")
	private void initTabs() {
		
		CommunityTab[] tabs = CommunityTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
        	CommunityTab mainTab = tabs[i];
            TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_conmunity, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            if (i == 2) {
                indicator.setVisibility(View.INVISIBLE);
                mTabHost.setNoTabChangedTag(getString(mainTab.getResName()));
            }
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(CommunityActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);

            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
    
    /**
     * 处理传进来的Intent
     * 
     * @param intent
     */
    private void handleIntent(Intent intent) {
    	
    	if (intent == null)
            return;
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

    	getMenuInflater().inflate(R.menu.community_menu, menu);
    	
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {
		
		case R.id.cm_scan:

			ToastTool.showToast("click scan btn!");
			break;
		case R.id.cm_refresh:

			ToastTool.showToast("click refresh btn!");
			break;
		case R.id.cm_share:

			ShareHelper.handleShare(this);
			break;
		case R.id.cm_search:

			ToastTool.showToast("click search btn!");
			break;
		}
		
		return true;
	}

	@Override
	public void onClick(View v) {

        int id = v.getId();
        switch (id) {
        // 点击了快速操作按钮
        case R.id.quick_option_iv:

        	UIHelper.showSimpleBack(this, BackPage.NOTE_PUB);
            break;

        default:
            break;
        }
	}

	@Override
	public void onTabChanged(String tabId) {
		
		final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
//        if (tabId.equals(getString(MainTab.ME.getResName()))) {
//            mBvNotice.setText("");
//            mBvNotice.hide();
//        }
        supportInvalidateOptionsMenu();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		super.onTouchEvent(event);
        boolean consumed = false;
        // use getTabHost().getCurrentTabView to decide if the current tab is
        // touched again
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(mTabHost.getCurrentTabView())) {
            // use getTabHost().getCurrentView() to get a handle to the view
            // which is displayed in the tab - and to get this views context
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && OnTabReselectListener.class.isInstance(currentFragment)) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
	}

    @SuppressLint("InflateParams")
	private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }

    /**
     * 监听返回
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            
        	//click back btn
        }
        return super.onKeyDown(keyCode, event);
    }
}
