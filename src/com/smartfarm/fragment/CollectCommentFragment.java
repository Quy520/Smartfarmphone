package com.smartfarm.fragment;

import java.io.Serializable;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.Comment;
import com.smartfarm.net.bean.CommentList;
import com.smartfarm.view.adapter.CommentAdapter;

public class CollectCommentFragment extends BaseAsyncListFragment<Comment> {

    protected static final String TAG = NewsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "collect_comment_list";

    @Override
    protected CommentAdapter getListAdapter() {
        return new CommentAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
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

//        SmartfarmNetHelper.getCollectComment(mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
    	
        Comment comment = mAdapter.getItem(position);
        if (comment != null) {
//            UIHelper.showNewsDetail(getActivity(), news.getId(), news.getCommentCount());
        }
    }
}
