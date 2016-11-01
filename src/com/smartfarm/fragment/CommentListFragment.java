package com.smartfarm.fragment;

import java.io.Serializable;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.net.ApiContants;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.Comment;
import com.smartfarm.net.bean.CommentList;
import com.smartfarm.view.R;
import com.smartfarm.view.adapter.CommentAdapter;
import com.smartfarm.view.adapter.ListBaseAdapter;

public class CommentListFragment extends BaseAsyncListFragment<Comment> {

    protected static final String TAG = NoteFragment.class.getSimpleName();
    protected static final String CACHE_KEY_PREFIX = "comment_list";

	@Override
	protected ListBaseAdapter<Comment> getListAdapter() {
		return new CommentAdapter();
	}

	/**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected CommentList parseList(Object obj) throws Exception {
        return JSON.toJavaObject((JSON) obj, CommentList.class);
    }

    @Override
    protected CommentList readList(Serializable seri) {
        return ((CommentList) seri);
    }
    
    
    @Override
    protected void sendRequestData() {

//    	SmartfarmNetHelper.getComment(ApiContants.CONTROL_TYPE_NEWS, mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sendRequestData();
        return super.onOptionsItemSelected(item);
    }
}
