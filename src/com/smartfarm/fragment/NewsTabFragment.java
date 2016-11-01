package com.smartfarm.fragment;

import android.os.Bundle;

import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.base.BaseViewPagerFragment;
import com.smartfarm.net.ApiContants;
import com.smartfarm.view.adapter.ViewPageFragmentAdapter;
import com.smartfarm.widget.OnTabReselectListener;

public class NewsTabFragment extends BaseViewPagerFragment implements OnTabReselectListener {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        adapter.addTab("新闻", "news", NewsFragment.class, getBundle(ApiContants.NEWS_TYPE_NEWS));
        adapter.addTab("帮帮", "bangbang", NewsFragment.class, getBundle(ApiContants.NEWS_TYPE_BANGBANG));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseAsyncListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }
    
	@Override
	public void onTabReselect() {
		
	}
}
