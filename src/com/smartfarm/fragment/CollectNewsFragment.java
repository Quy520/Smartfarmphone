package com.smartfarm.fragment;

import java.io.Serializable;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.News;
import com.smartfarm.net.bean.NewsList;
import com.smartfarm.view.adapter.NewsListAdapter;
import com.smartfarm.view.util.UIHelper;

public class CollectNewsFragment extends BaseAsyncListFragment<News> {

    protected static final String TAG = NewsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "collect_news_list";

    @Override
    protected NewsListAdapter getListAdapter() {
        return new NewsListAdapter();
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
    protected NewsList parseList(Object obj) throws Exception {
        return JSON.toJavaObject((JSON) obj, NewsList.class);
    }

    @Override
    protected NewsList readList(Serializable seri) {
        return ((NewsList) seri);
    }

    @Override
    protected void sendRequestData() {

//        SmartfarmNetHelper.getCollectNews(mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
    	
        News news = mAdapter.getItem(position);
        if (news != null) {
            UIHelper.showNewsDetail(getActivity(), news.getId(), news.getCommentCount());
        }
    }
}
