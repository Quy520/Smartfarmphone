package com.smartfarm.base;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smartfarm.bean.ListEntity;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.bean.Entity;
import com.smartfarm.net.bean.Result;
import com.smartfarm.net.bean.ResultBean;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.XmlUtils;
import com.smartfarm.view.R;
import com.smartfarm.view.SimpleBackActivity;
import com.smartfarm.view.adapter.ListBaseAdapter;
import com.smartfarm.view.util.CacheManager;
import com.smartfarm.widget.EmptyLayout;

public abstract class BaseAsyncListFragment<T extends Entity> extends BaseFragment
		implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener,
		OnScrollListener {

	public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";
	private static final int PAGE_FIRST = 1;

	protected SwipeRefreshLayout mSwipeRefreshLayout;
	protected EmptyLayout mErrorLayout;
	protected ListView mListView;

	protected ListBaseAdapter<T> mAdapter;

	protected int mStoreEmptyState = -1;

	protected int mCurrentPage = PAGE_FIRST;

	protected int mCatalog = 1;
	// 错误信息
	protected Result mResult;

	private AsyncTask<String, Void, ListEntity<T>> mCacheTask;
	private ParserTask mParserTask;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_pull_refresh_listview;
	}

	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mCatalog = args.getInt(BUNDLE_KEY_CATALOG, -1);
			
			if(mCatalog < 0)
				mCatalog = args.getInt(SimpleBackActivity.BUNDLE_KEY_ARGS, -1);
			
			receiveArgs(args);
		}
	}
	
	protected void receiveArgs(Bundle args) {}

	@Override
	public void initView(View view) {

    	mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
    	mListView = (ListView) view.findViewById(R.id.listview);
    	mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
    	
    	mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(
				R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);

		mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentPage = PAGE_FIRST;
				mState = STATE_REFRESH;
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(true);
			}
		});

		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(this);

		if (mAdapter != null) {
			mListView.setAdapter(mAdapter);
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else {
			mAdapter = getListAdapter();
			mListView.setAdapter(mAdapter);

			if (requestDataIfViewCreated()) {
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				mState = STATE_NONE;
				requestData(false);
			} else {
				mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
			}

		}
		if (mStoreEmptyState != -1) {
			mErrorLayout.setErrorType(mStoreEmptyState);
		}
	}

	@Override
	public void onDestroyView() {
		
		mStoreEmptyState = mErrorLayout.getErrorState();
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		cancelReadCacheTask();
		cancelParserTask();
		super.onDestroy();
	}

	protected abstract ListBaseAdapter<T> getListAdapter();

	// 下拉刷新数据
	@Override
	public void onRefresh() {
		
		if (mState == STATE_REFRESH) {
			return;
		}
		// 设置顶部正在刷新
		mListView.setSelection(0);
		setSwipeRefreshLoadingState();
		mCurrentPage = PAGE_FIRST;
		mState = STATE_REFRESH;
		requestData(true);
	}

	protected boolean requestDataIfViewCreated() {
		return true;
	}

	protected String getCacheKeyPrefix() {
		return null;
	}

	protected ListEntity<T> parseList(Object obj) throws Exception {
		return null;
	}

	protected ListEntity<T> readList(Serializable seri) {
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	private String getCacheKey() {
		return new StringBuilder(getCacheKeyPrefix()).append("_")
				.append(mCurrentPage).toString();
	}

	// 是否需要自动刷新
	protected boolean needAutoRefresh() {
		return true;
	}

	/***
	 * 获取列表数据
	 * 
	 * 
	 * @author 火蚁 2015-2-9 下午3:16:12
	 * 
	 * @return void
	 * @param refresh
	 */
	protected void requestData(boolean refresh) {
		String key = getCacheKey();
		if (isReadCacheData(refresh)) {
			readCacheData(key);
		} else {
			// 取新的数据
			sendRequestData();
		}
	}

	/***
	 * 判断是否需要读取缓存的数据
	 * 
	 * @author 火蚁 2015-2-10 下午2:41:02
	 * 
	 * @return boolean
	 * @param refresh
	 * @return
	 */
	private boolean isReadCacheData(boolean refresh) {
		
		String key = getCacheKey();
		if (!CommonTool.isNetworkConnected()) {
			return true;
		}
		
		// 第一页若不是主动刷新，缓存存在，优先取缓存的
		if (CacheManager.isExistDataCache(getActivity(), key) && !refresh
				&& mCurrentPage == PAGE_FIRST) {
			return true;
		}
		
		// 其他页数的，缓存存在以及还没有失效，优先取缓存的
		if (CacheManager.isExistDataCache(getActivity(), key)
				&& !CacheManager.isCacheDataFailure(getActivity(), key)
				&& mCurrentPage != PAGE_FIRST) {
			return true;
		}

		return false;
	}

	protected void sendRequestData() {}

	private void readCacheData(String cacheKey) {
		cancelReadCacheTask();
		mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
	}

	private void cancelReadCacheTask() {
		if (mCacheTask != null) {
			mCacheTask.cancel(true);
			mCacheTask = null;
		}
	}

	private class CacheTask extends AsyncTask<String, Void, ListEntity<T>> {
		private final WeakReference<Context> mContext;

		private CacheTask(Context context) {
			mContext = new WeakReference<Context>(context);
		}

		@Override
		protected ListEntity<T> doInBackground(String... params) {
			Serializable seri = CacheManager.readObject(mContext.get(),
					params[0]);
			if (seri == null) {
				return null;
			} else {
				return readList(seri);
			}
		}

		@Override
		protected void onPostExecute(ListEntity<T> list) {
			super.onPostExecute(list);
			if (list != null) {
				executeOnLoadDataSuccess(list.getList());
			} else {
				executeOnLoadDataError(null);
			}
			executeOnLoadFinish();
		}
	}

	private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
		private final WeakReference<Context> mContext;
		private final Serializable seri;
		private final String key;

		private SaveCacheTask(Context context, Serializable seri, String key) {
			mContext = new WeakReference<Context>(context);
			this.seri = seri;
			this.key = key;
		}

		@Override
		protected Void doInBackground(Void... params) {
			CacheManager.saveObject(mContext.get(), seri, key);
			return null;
		}
	}

	protected AsyncHttpResponseHandler mHandler = new BaseAsyncHttpResHandler() {
		
		@Override
		public void onSuccess(ApiResponse res) {

			if (isAdded()) {
				if (mState == STATE_REFRESH) {
					onRefreshNetworkSuccess();
				}
				executeParserTask(res);
			}
		}
		
		@Override
		public void onFailure(int errorCode, String data) {
			
			requestError();
		}
	};
	
	protected void requestError() {
		if (isAdded()) {
			readCacheData(getCacheKey());
		}
	}

	protected void executeOnLoadDataSuccess(List<T> data) {
		if (data == null) {
			data = new ArrayList<T>();
		}

		if (mResult != null && !mResult.OK()) {

			ToastTool.showToast(mResult.getErrorMessage());
		}

		mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		if (mCurrentPage == PAGE_FIRST) {
			mAdapter.clear();
		}

		for (int i = 0; i < data.size(); i++) {
			if (compareTo(mAdapter.getData(), data.get(i))) {
				data.remove(i);
				i--;
			}
		}
		int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
		if ((mAdapter.getCount() + data.size()) == 0) {
			adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
		} else if (data.size() == 0
				|| (data.size() < getPageSize() && mCurrentPage == PAGE_FIRST)) {
			adapterState = ListBaseAdapter.STATE_NO_MORE;
			mAdapter.notifyDataSetChanged();
		} else {
			adapterState = ListBaseAdapter.STATE_LOAD_MORE;
		}
		mAdapter.setState(adapterState);
		mAdapter.addData(data);
		// 判断等于是因为最后有一项是listview的状态
		if (mAdapter.getCount() == 1) {

			if (needShowEmptyNoData()) {
				mErrorLayout.setErrorType(EmptyLayout.NODATA);
			} else {
				mAdapter.setState(ListBaseAdapter.STATE_EMPTY_ITEM);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 是否需要隐藏listview，显示无数据状态
	 * 
	 * @author 火蚁 2015-1-27 下午6:18:59
	 * 
	 */
	protected boolean needShowEmptyNoData() {
		return true;
	}

	protected boolean compareTo(List<? extends Entity> data, Entity enity) {
		int s = data.size();
		if (enity != null) {
			for (int i = 0; i < s; i++) {
				if (enity.getId() == data.get(i).getId()) {
					return true;
				}
			}
		}
		return false;
	}

	protected int getPageSize() {
		return Constants.PAGE_SIZE;
	}

	protected void onRefreshNetworkSuccess() {
	}

	protected void executeOnLoadDataError(String error) {
		if (mCurrentPage == PAGE_FIRST
				&& !CacheManager.isExistDataCache(getActivity(), getCacheKey())) {
			mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
			mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
			mAdapter.notifyDataSetChanged();
		}
	}

	// 完成刷新
	protected void executeOnLoadFinish() {
		setSwipeRefreshLoadedState();
		mState = STATE_NONE;
	}

	/** 设置顶部正在加载的状态 */
	private void setSwipeRefreshLoadingState() {
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setRefreshing(true);
			// 防止多次重复刷新
			mSwipeRefreshLayout.setEnabled(false);
		}
	}

	/** 设置顶部加载完毕的状态 */
	private void setSwipeRefreshLoadedState() {
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setRefreshing(false);
			mSwipeRefreshLayout.setEnabled(true);
		}
	}

	private void executeParserTask(ApiResponse res) {
		cancelParserTask();
		mParserTask = new ParserTask(res);
		mParserTask.execute();
	}

	private void cancelParserTask() {
		if (mParserTask != null) {
			mParserTask.cancel(true);
			mParserTask = null;
		}
	}

	class ParserTask extends AsyncTask<Void, Void, String> {

		private ApiResponse response;
		private boolean parserError;
		private List<T> list;

		public ParserTask(ApiResponse response) {
			this.response = response;
		}

		@Override
		protected String doInBackground(Void... params) {
			
			try {
				ListEntity<T> data = parseList(response.getResponseData());
				new SaveCacheTask(getActivity(), data, getCacheKey()).execute();
				list = data.getList();
				if (list == null) {
					ResultBean resultBean = XmlUtils.toBean(ResultBean.class, response.getResponseData().toString().getBytes());
					if (resultBean != null) {
						mResult = resultBean.getResult();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

				parserError = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (parserError) {
				readCacheData(getCacheKey());
			} else {
				executeOnLoadDataSuccess(list);
				executeOnLoadFinish();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mAdapter == null || mAdapter.getCount() == 0) {
			return;
		}
		// 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
		if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
			return;
		}
		// 判断是否滚动到底部
		boolean scrollEnd = false;
		try {
			if (view.getPositionForView(mAdapter.getFooterView()) == view
					.getLastVisiblePosition())
				scrollEnd = true;
		} catch (Exception e) {
			scrollEnd = false;
		}

		if (mState == STATE_NONE && scrollEnd) {
			if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE
					|| mAdapter.getState() == ListBaseAdapter.STATE_NETWORK_ERROR) {
				mCurrentPage++;
				mState = STATE_LOADMORE;
				requestData(false);
				mAdapter.setFooterViewLoading();
			}
		}
	}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {

    }
}
