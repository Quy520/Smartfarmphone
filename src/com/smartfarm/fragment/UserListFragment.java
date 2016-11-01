package com.smartfarm.fragment;

import java.io.Serializable;

import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.net.ApiContants;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.ApiUserList;
import com.smartfarm.net.bean.AppUser;
import com.smartfarm.view.adapter.ListBaseAdapter;
import com.smartfarm.view.adapter.UserAdapter;

public class UserListFragment extends BaseAsyncListFragment<AppUser> {

    protected static final String TAG = NoteFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "user_list";

	@Override
	protected ListBaseAdapter<AppUser> getListAdapter() {
		return new UserAdapter();
	}

    @Override
	public String getTitle() {
		return mCatalog == ApiContants.USER_FAN ? "我的粉丝" : "我关注的人";
	}

	/**
     * 获取当前展示页面的缓存数据3
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected ApiUserList parseList(Object obj) throws Exception {

    	ApiUserList users = JSON.toJavaObject((JSON) obj, ApiUserList.class);
    	
        return users;
    }

    @Override
    protected ApiUserList readList(Serializable seri) {
        return ((ApiUserList) seri);
    }

    @Override
    protected void sendRequestData() {

        SmartfarmNetHelper.getUserRelationList(mCurrentPage, mCatalog, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
    	
    	AppUser note = mAdapter.getItem(position);
        if (note != null) {
//            UIHelper.showNewsDetail(getActivity(), news.getId(), news.getCommentCount());
        }
    }
}
