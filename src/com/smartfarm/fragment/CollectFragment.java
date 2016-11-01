package com.smartfarm.fragment;

import android.os.Bundle;

import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.base.BaseViewPagerFragment;
import com.smartfarm.net.ApiContants;
import com.smartfarm.view.adapter.ViewPageFragmentAdapter;

public class CollectFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        adapter.addTab("收藏的帖子", "c_note", CollectNoteFragment.class, getBundle(ApiContants.COLLECT_TYPE_NOTE));
        adapter.addTab("收藏的新闻", "c_news", CollectNewsFragment.class, getBundle(ApiContants.COLLECT_TYPE_NEWS));
        adapter.addTab("收藏的评论", "c_comments", CollectCommentFragment.class, getBundle(ApiContants.COLLECT_TYPE_COMMENTS));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseAsyncListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }
}
