package com.smartfarm.fragment;

import android.os.Bundle;

import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.base.BaseViewPagerFragment;
import com.smartfarm.net.ApiContants;
import com.smartfarm.view.adapter.ViewPageFragmentAdapter;
import com.smartfarm.widget.OnTabReselectListener;

public class BrowseFragment extends BaseViewPagerFragment implements OnTabReselectListener {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        adapter.addTab("推荐", "recommend", NoteFragment.class, getBundle(ApiContants.NOTE_TYPE_RECOMMEND));
        adapter.addTab("附近", "around", NoteFragment.class, getBundle(ApiContants.NOTE_TYPE_AROUND));
        adapter.addTab("求助", "help", NoteFragment.class, getBundle(ApiContants.NOTE_TYPE_HELP));
//        adapter.addTab("关注", "friend", NoteFragment.class, getBundle(ApiContants.NOTE_TYPE_FRIEND));
//        adapter.addTab("关心", "concern", NoteFragment.class, getBundle(ApiContants.NOTE_TYPE_CONCERN));
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
