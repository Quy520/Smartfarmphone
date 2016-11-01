package com.smartfarm.fragment;

import java.io.Serializable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseDetailFragment;
import com.smartfarm.emoji.OnSendClickListener;
import com.smartfarm.net.ApiContants;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.Entity;
import com.smartfarm.net.bean.News;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.DetailActivity;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.StringUtils;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.EmptyLayout;

public class NewsDetailFragment extends BaseDetailFragment implements OnSendClickListener {

    private static final String BLOG_CACHE_KEY = "news_detail_";
    
	private int id;
	private News mNews;
	
	private TextView mTvTime;
	private TextView mTvTitle;
	private TextView mTvSource;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container,
                false);

        mCommentCount = getActivity().getIntent().getIntExtra("comment_count", 0);
        id = getActivity().getIntent().getIntExtra("id", 0);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
    	
        ((DetailActivity) getActivity()).toolFragment.setCommentCount(mCommentCount);
        
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mWebView = (WebView) view.findViewById(R.id.webview);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvSource = (TextView) view.findViewById(R.id.tv_source);
        mEmptyLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        
        UIHelper.initWebView(mWebView);
    }

    @Override
    protected boolean hasReportMenu() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailActivity) getActivity()).toolFragment.showReportButton();
    }

    @Override
    protected void onFavoriteChanged(boolean flag) {
        super.onFavoriteChanged(flag);
        mNews.setCollect(flag);
        saveCache(mNews);
    }

    @Override
    protected String getCacheKey() {
        return new StringBuilder(BLOG_CACHE_KEY).append(id).toString();
    }

    @Override
    protected void sendRequestData() {
        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        SmartfarmNetHelper.getNewsDetail(id, mHandler);
    }

    @Override
    protected Entity parseData(Object obj) throws Exception {
    	
        return JSON.toJavaObject((JSON) obj, News.class);
    }

    @Override
    protected Entity readData(Serializable seri) {
        return (News) seri;
    }

    @Override
    public void onclickWriteComment() {
        
        if(mNews != null)
        	UIHelper.showSimpleBack(getActivity(), BackPage.COMMENT, mNews.getId());
    }

    @Override
    protected void executeOnLoadDataSuccess(Entity entity) {
        mNews = (News) entity;
        fillUI();
        fillWebViewBody();
        ((DetailActivity) getActivity()).toolFragment.setCommentCount(mCommentCount);
    }

    private void fillUI() {
        mTvTitle.setText(mNews.getTitle());
        mTvSource.setText(mNews.getFromDescr());
        mTvTime.setText(mNews.getFriendlyTime());
        notifyFavorite(mNews.isCollect());
    }

    @Override
    public int getCommentCount() {
        return mNews.getCommentCount();
    }

    private void fillWebViewBody() {
        StringBuffer body = new StringBuffer();
        body.append(UIHelper.setHtmlCotentSupportImagePreview(mNews.getContent()));
        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
        mWebView.loadDataWithBaseURL(null, body.toString(), "text/html",
                "utf-8", null);
    }

    @Override
    protected int getFavoriteTargetId() {
        return id;
    }
    
    @Override
    protected int getFavoriteTargetType() {
    	return ApiContants.CONTROL_TYPE_NEWS;
    }

    @Override
    protected String getShareUrl() {
        return mNews != null ? mNews.getFromUrl() : "";
    }

    @Override
    protected String getRepotrUrl() {
        return mNews != null ? mNews.getFromUrl() : "";
    }

    @Override
    protected String getShareTitle() {
        return mNews != null ? mNews.getTitle()
                : Constants.SHARE_TITLE;
    }

    @Override
    protected String getShareContent() {
        return mNews != null ? StringUtils.getSubString(0, 55,
                getFilterHtmlBody(mNews.getContent())) : "";
    }

    @Override
    protected int getRepotrId() {
        return mNews != null ? id : 0;
    }
    
	@Override
	public void onClickSendButton(Editable str) {
		
		if (!CommonTool.isConnected()) {
			ToastTool.showToast(R.string.tip_network_error);
			return;
		}
		
		if (!AppContext.context().isLogin()) {
			UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
			return;
		}
		
		if (TextUtils.isEmpty(str)) {
			ToastTool.showToast(R.string.tip_comment_content_empty);
			return;
		}
		
		 showLoadingDialog();
		 SmartfarmNetHelper.pubComment(ApiContants.CONTROL_TYPE_NEWS, id, str.toString(), mCommentHandler);
	}

	@Override
	public void onClickFlagButton() {}

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
