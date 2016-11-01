package com.smartfarm.fragment;

import com.smartfarm.net.SmartfarmNetHelper;

public class UserCommentFragment extends CommentListFragment {

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + "_mine_" + mCatalog;
    }

    @Override
    protected void sendRequestData() {
    	SmartfarmNetHelper.getUserComment(mCatalog, mCurrentPage, mHandler);
    }
}
