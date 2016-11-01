package com.smartfarm.base;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.bean.Entity;
import com.smartfarm.view.adapter.ListBaseAdapter;
import com.smartfarm.view.util.CacheManager;
import com.smartfarm.widget.EmptyLayout;

public abstract class BeseHaveHeaderListFragment<T1 extends Entity, T2 extends Serializable>
		extends BaseAsyncListFragment<T1> {

	protected T2 detailBean;//

	protected Activity aty;

	protected final AsyncHttpResponseHandler mDetailHandler = new BaseAsyncHttpResHandler() {

		@Override
		public void onFailure(int errorCode, String data) {
			readDetailCacheData(getDetailCacheKey());
		}

		@Override
		public void onSuccess(ApiResponse res) {

			try {
				if(res.getErrorCode() > 0) {
					T2 detail = getDetailBean(res.getResponseData());
					if (detail != null) {
						requstListData();
						executeOnLoadDetailSuccess(detail);
						new SaveCacheTask(getActivity(), detail,
								getDetailCacheKey()).execute();
					} else {
						onFailure(res.getErrorCode(), "load detail error");
					}
				} else {
					throw new RuntimeException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(res.getErrorCode(), "load detail error");
			}
		}
	};

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		mListView.addHeaderView(initHeaderView());
		aty = getActivity();
		super.initView(view);
		requestDetailData(isRefresh());
	}

	protected boolean isRefresh() {
		return false;
	}

	protected abstract void requestDetailData(boolean isRefresh);

	protected abstract View initHeaderView();

	protected abstract String getDetailCacheKey();

	protected abstract void executeOnLoadDetailSuccess(T2 detailBean);

	protected abstract T2 getDetailBean(Object obj);

	@Override
	protected boolean requestDataIfViewCreated() {
		return false;
	}

	private void requstListData() {
		mState = STATE_REFRESH;
		mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
		sendRequestData();
	}

	/***
	 * 带有header view的listfragment不需要显示是否数据为空
	 */
	@Override
	protected boolean needShowEmptyNoData() {
		return false;
	}

	protected void readDetailCacheData(String cacheKey) {
		new ReadCacheTask(getActivity()).execute(cacheKey);
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

	private class ReadCacheTask extends AsyncTask<String, Void, T2> {
		private final WeakReference<Context> mContext;

		private ReadCacheTask(Context context) {
			mContext = new WeakReference<Context>(context);
		}

		@Override
		protected T2 doInBackground(String... params) {
			if (mContext.get() != null) {
				Serializable seri = CacheManager.readObject(mContext.get(),
						params[0]);
				if (seri == null) {
					return null;
				} else {
					return (T2) seri;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(T2 t) {
			super.onPostExecute(t);
			if (t != null) {
				requstListData();
				executeOnLoadDetailSuccess(t);
			}
		}
	}

	@Override
	protected void executeOnLoadDataError(String error) {
		mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
		mAdapter.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findHeaderView(View headerView, int viewId) {
		return (T) headerView.findViewById(viewId);
	}
}
