package com.smartfarm.fragment;

import android.os.Bundle;

import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.base.BaseViewPagerFragment;
import com.smartfarm.net.ApiContants;
import com.smartfarm.view.adapter.ViewPageFragmentAdapter;
import com.smartfarm.widget.OnTabReselectListener;

public class MyCommentFragment extends BaseViewPagerFragment implements OnTabReselectListener {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        adapter.addTab("消息评论", "comment_msg", UserCommentFragment.class, getBundle(ApiContants.CONTROL_TYPE_MSG));
        adapter.addTab("新闻评论", "comment_news", UserCommentFragment.class, getBundle(ApiContants.CONTROL_TYPE_NEWS));
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
