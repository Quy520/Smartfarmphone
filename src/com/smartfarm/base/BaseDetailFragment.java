package com.smartfarm.base;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import org.w3c.dom.Comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.internal.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.Entity;
import com.smartfarm.net.bean.Report;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.DetailActivity;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.CacheManager;
import com.smartfarm.view.util.HTMLUtil;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.EmptyLayout;
import com.smartfarm.widget.dialog.ReportDialog;
import com.smartfarm.widget.dialog.ShareDialog;
import com.smartfarm.widget.dialog.ShareDialog.OnSharePlatformClick;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public abstract class BaseDetailFragment extends BaseFragment implements
		OnItemClickListener {

	public static final String INTENT_ACTION_COMMENT_CHANGED = "INTENT_ACTION_COMMENT_CHAGED";

	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	private ListPopupWindow mMenuWindow;
	private MenuAdapter mMenuAdapter;

	protected EmptyLayout mEmptyLayout;

	public int mCommentCount = 0;

	protected WebView mWebView;

	protected AsyncHttpResponseHandler mCommentHandler = new BaseAsyncHttpResHandler() {

		@Override
		public void onSuccess(ApiResponse res) {

			try {
				if (res.getErrorCode() == 1) {
					hideLoadingDialog();
					ToastTool.showToast(R.string.comment_publish_success);

//					commentPubSuccess(rsb.getComment());
				} else {
					hideLoadingDialog();
					ToastTool.showToast(res.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(0, "");
			}
			((DetailActivity) getActivity()).emojiFragment.clean();
		}

		@Override
		public void onFailure(int errorCode, String data) {

			hideLoadingDialog();
			ToastTool.showToast(R.string.comment_publish_faile);
		}

		@Override
		public void onFinish() {
			((DetailActivity) getActivity()).emojiFragment.hideAllKeyBoard();
		};
	};

	protected void recycleWebView() {
		if (mWebView != null) {
			mWebView.setVisibility(View.GONE);
			mWebView.removeAllViews();
			mWebView.destroy();
			mWebView = null;
		}
	}

	protected void onCommentChanged(int opt, int id, int catalog,
			boolean isBlog, Comment comment) {
	}

	private AsyncTask<String, Void, Entity> mCacheTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMenuAdapter = new MenuAdapter();
		setHasOptionsMenu(true);

		mController.getConfig().closeToast();
	}

	protected boolean hasReportMenu() {
		return false;
	}

	@Override
	public void onDestroyView() {
		recycleWebView();
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		cancelReadCache();
		recycleWebView();
		super.onDestroy();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		requestData(false);
	}

	protected String getCacheKey() {
		return null;
	}

	protected Entity parseData(Object obj) throws Exception {
		return null;
	}

	protected Entity readData(Serializable seri) {
		return null;
	}

	protected void sendRequestData() {
	}

	protected void requestData(boolean refresh) {
		String key = getCacheKey();
		if (CommonTool.isNetworkConnected()
				&& (!CacheManager.isExistDataCache(getActivity(), key) || refresh)) {
			sendRequestData();
		} else {
			readCacheData(key);
		}
	}

	// 刷新数据
	protected void sendRefresh() {
		sendRequestData();
	}

	private void readCacheData(String cacheKey) {
		cancelReadCache();
		mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
	}

	private void cancelReadCache() {
		if (mCacheTask != null) {
			mCacheTask.cancel(true);
			mCacheTask = null;
		}
	}

	private class CacheTask extends AsyncTask<String, Void, Entity> {
		private final WeakReference<Context> mContext;

		private CacheTask(Context context) {
			mContext = new WeakReference<Context>(context);
		}

		@Override
		protected Entity doInBackground(String... params) {
			if (mContext.get() != null) {
				Serializable seri = CacheManager.readObject(mContext.get(),
						params[0]);
				if (seri == null) {
					return null;
				} else {
					return readData(seri);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Entity entity) {
			super.onPostExecute(entity);
			if (entity != null) {
				executeOnLoadDataSuccess(entity);
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

			try {
				Entity entity = parseData(res.getResponseData());
				if (entity != null) {
					mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
					executeOnLoadDataSuccess(entity);
					saveCache(entity);
				} else {
					throw new RuntimeException("load detail error");
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(0, e.getMessage());
			}
		}
		
		@Override
		public void onFailure(int errorCode, String data) {

			readCacheData(getCacheKey());
		}
	};

	private boolean mIsFavorited;

	protected void saveCache(Entity entity) {
		new SaveCacheTask(getActivity(), entity, getCacheKey()).execute();
	}

	protected void executeOnLoadDataSuccess(Entity entity) {
	}

	protected void executeOnLoadDataError(String object) {
		mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
		mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mState = STATE_REFRESH;
				mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(true);
			}
		});
	}

	protected void executeOnLoadFinish() {
	}

	protected void onFavoriteChanged(boolean flag) {
		((DetailActivity) getActivity()).toolFragment.setFavorite(flag);
	}

	protected int getFavoriteTargetId() {
		return -1;
	}

	protected int getFavoriteTargetType() {
		return -1;
	}

	protected String getShareUrl() {
		return Constants.LINKED_URL;
	}

	protected String getShareTitle() {
		return getString(R.string.share_title);
	}

	protected String getShareContent() {
		return "";
	}

	/***
	 * 获取去除html标签的body
	 * 
	 * @param body
	 * @return
	 */
	protected String getFilterHtmlBody(String body) {
		if (body == null)
			return "";
		return HTMLUtil.delHTMLTag(body.trim());
	}

	protected UMImage getShareImg() {
		UMImage img = new UMImage(getActivity(), R.drawable.newlogo);
		return img;
	}

	protected void commentPubSuccess(Comment comment) {}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			handleFavoriteOrNot();
			handleShare();
		} else if (position == 1) {
			onReportMenuClick();
		} else if (position == 2) {

		}
		if (mMenuWindow != null) {
			mMenuWindow.dismiss();
			mMenuWindow = null;
		}
	}

	private final AsyncHttpResponseHandler mReportHandler = new BaseAsyncHttpResHandler() {
		
		@Override
		public void onSuccess(ApiResponse res) {
			
			if (res.getErrorCode() == 1) {
				ToastTool.showToast(R.string.tip_report_success);
			} else {
				ToastTool.showToast(new String(res.getMessage()));
			}
		}
		
		@Override
		public void onFailure(int errorCode, String data) {
			ToastTool.showToast(R.string.tip_report_faile);
		}
		
		@Override
		public void onFinish() {
			hideLoadingDialog();
		}
	};

	public void onReportMenuClick() {
		if (getRepotrId() == 0) {
			ToastTool.showToast("正在加载，请稍等...");
		}
		if (!AppContext.context().isLogin()) {
			UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
			return;
		}
		int reportId = getRepotrId();

		final ReportDialog dialog = new ReportDialog(getActivity(),
				getRepotrUrl(), reportId);
		dialog.setCancelable(true);
		dialog.setTitle(R.string.report);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setNegativeButton(R.string.cancle, null);
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface d, int which) {
						Report report = null;
						if ((report = dialog.getReport()) != null) {
//							showWaitDialog(R.string.progress_submit);
//							OSChinaApi.report(report, mReportHandler);
							
							//send report
						}
						d.dismiss();
					}
				});
		dialog.show();
	}

	protected String getRepotrUrl() {
		return "";
	}

	protected int getRepotrId() {
		return 0;
	}

	/**
	 * 收藏
	 */
	public void handleFavoriteOrNot() {
		
		if (!CommonTool.isNetworkConnected()) {
			ToastTool.showToast(R.string.tip_no_internet);
			return;
		}
		
		if (!AppContext.context().isLogin()) {
			UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
			return;
		}
		
		if (getFavoriteTargetId() == -1 || getFavoriteTargetType() == -1) {
			return;
		}
		
//		if (!mIsFavorited) 
//			SmartfarmNetHelper.collect(getFavoriteTargetId(), getFavoriteTargetType(), mAddFavoriteHandler);
//		else 
//			SmartfarmNetHelper.removeCollect(getFavoriteTargetId(), getFavoriteTargetType(), mDelFavoriteHandler);
	}

	/**
	 * 分享
	 */
	public void handleShare() {
		if (TextUtils.isEmpty(getShareContent())
				|| TextUtils.isEmpty(getShareUrl())) {
			ToastTool.showToast("内容加载失败...");
			return;
		}
		final ShareDialog dialog = new ShareDialog(getActivity());
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle(R.string.share_to);
		dialog.setOnPlatformClickListener(new OnSharePlatformClick() {
			@Override
			public void onPlatformClick(int id) {
				switch (id) {
				case R.id.ly_share_weichat_circle:
					shareToWeiChatCircle();
					break;
				case R.id.ly_share_weichat:
					shareToWeiChat();
					break;
				case R.id.ly_share_qq:
					shareToQQ(SHARE_MEDIA.QQ);
					break;
				case R.id.ly_share_qzone:
					shareToQzone();
					break;
				case R.id.ly_share_other:
					ShowUtil.showSystemShareOption(getActivity(), getShareTitle(), getShareUrl());
					break;
				default:
					break;
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void shareToWeiChatCircle() {
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),
				Constants.WE_CHAT_APP_ID, Constants.WE_CHAT_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(getShareContent());
		// 设置朋友圈title
		circleMedia.setTitle(getShareTitle());
		circleMedia.setShareImage(getShareImg());
		circleMedia.setTargetUrl(getShareUrl());
		mController.setShareMedia(circleMedia);
		mController.postShare(getActivity(), SHARE_MEDIA.WEIXIN_CIRCLE, null);
	}

	private void shareToWeiChat() {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(),
				Constants.WE_CHAT_APP_ID, Constants.WE_CHAT_APP_SECRET);
		wxHandler.addToSocialSDK();
		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(getShareContent());
		// 设置title
		weixinContent.setTitle(getShareTitle());
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(getShareUrl());
		// 设置分享图片
		weixinContent.setShareImage(getShareImg());
		mController.setShareMedia(weixinContent);
		mController.postShare(getActivity(), SHARE_MEDIA.WEIXIN, null);
	}

	protected void shareToQQ(SHARE_MEDIA media) {
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
				Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
		qqSsoHandler.setTargetUrl(getShareUrl());
		qqSsoHandler.setTitle(getShareTitle());
		qqSsoHandler.addToSocialSDK();
		mController.setShareContent(getShareContent());
		mController.setShareImage(getShareImg());
		mController.postShare(getActivity(), media, null);
	}

	protected void shareToQzone() {
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
		qZoneSsoHandler.addToSocialSDK();

		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(getShareContent());
		qzone.setTargetUrl(getShareUrl());
		qzone.setTitle(getShareTitle());
		qzone.setShareImage(getShareImg());
		
		mController.setShareMedia(qzone);
		mController.postShare(getActivity(), SHARE_MEDIA.QZONE, null);
	}

	protected void notifyFavorite(boolean favorite) {
		mIsFavorited = favorite;
		FragmentActivity aty = getActivity();
		if (aty != null) {
			aty.supportInvalidateOptionsMenu();
		}
		if (mMenuAdapter != null) {
			mMenuAdapter.setFavorite(favorite);
		}
		onFavoriteChanged(favorite);
	}

	public boolean isFavorited() {
		return mIsFavorited;
	}

	@SuppressLint("ViewHolder")
	private static class MenuAdapter extends BaseAdapter {

		public void setFavorite(boolean favorite) {
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.list_cell_popup_menu, null);
			TextView name = (TextView) convertView.findViewById(R.id.tv_name);

			int iconResId = 0;
			// if (position == 0) {
			// name.setText(isFavorite ? R.string.detail_menu_unfavorite
			// : R.string.detail_menu_favorite);
			// iconResId = isFavorite ?
			// R.drawable.actionbar_menu_icn_unfavoirite
			// : R.drawable.actionbar_menu_icn_favoirite;
			// } else
			if (position == 0) {
				name.setText(parent.getResources().getString(
						R.string.detail_menu_for_share));
				iconResId = R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark;
			} else if (position == 1) {
				name.setText(parent.getResources().getString(
						R.string.detail_menu_for_report));
				iconResId = R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark;
			}
			Drawable drawable = AppContext.resources().getDrawable(iconResId);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			name.setCompoundDrawables(drawable, null, null, null);
			return convertView;
		}
	}

	private final AsyncHttpResponseHandler mAddFavoriteHandler = new BaseAsyncHttpResHandler() {
		
		@Override
		public void onSuccess(ApiResponse res) {
			try {

				if (res.getErrorCode() == 1 || res.getMessage().contains("已收藏")) {
					ToastTool.showToast(R.string.add_favorite_success);
					mMenuAdapter.setFavorite(true);
					mMenuAdapter.notifyDataSetChanged();
					mIsFavorited = true;
					getActivity().supportInvalidateOptionsMenu();
					onFavoriteChanged(true);
					ImageView view = (ImageView) getActivity().findViewById(
							R.id.action_favor);
					view.setImageResource(R.drawable.ic_action_favor_on_normal);
				} else {
					ToastTool.showToast(res.getMessage());
				}

			} catch (Exception e) {
				e.printStackTrace();
				onFailure(0, "");
			}
		}
		
		@Override
		public void onFailure(int errorCode, String data) {
			ToastTool.showToast(R.string.add_favorite_faile);
		}
	};

	private final AsyncHttpResponseHandler mDelFavoriteHandler = new BaseAsyncHttpResHandler() {
		
		@Override
		public void onSuccess(ApiResponse res) {
			
			try {

				if (res.getErrorCode() == 1 || res.getMessage().contains("该收藏不存在")) {
					ToastTool.showToast(R.string.del_favorite_success);
					mMenuAdapter.setFavorite(false);
					mMenuAdapter.notifyDataSetChanged();
					mIsFavorited = false;
					getActivity().supportInvalidateOptionsMenu();
					onFavoriteChanged(false);
					ImageView view = (ImageView) getActivity().findViewById(
							R.id.action_favor);
					view.setImageResource(R.drawable.ic_action_favor);
				} else {
					ToastTool.showToast(res.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(0, "");
			}
		}
		
		@Override
		public void onFailure(int errorCode, String data) {
			ToastTool.showToast(R.string.del_favorite_faile);
		}
	};

	public abstract int getCommentCount();

	@Override
	public void onClick(View v) {
	}

	@Override
	public void initView(View view) {
	}

	public void onclickWriteComment() {
	}

	@Override
	public void initData() {
	}
}
