package com.smartfarm.fragment;

import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smartfarm.base.BeseHaveHeaderListFragment;
import com.smartfarm.db.bean.User;
import com.smartfarm.emoji.InputHelper;
import com.smartfarm.emoji.OnSendClickListener;
import com.smartfarm.net.ApiContants;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.AppMessage;
import com.smartfarm.net.bean.Comment;
import com.smartfarm.net.bean.CommentList;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.DetailActivity;
import com.smartfarm.view.R;
import com.smartfarm.view.adapter.CommentAdapter;
import com.smartfarm.view.adapter.ListBaseAdapter;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.CacheManager;
import com.smartfarm.view.util.HTMLUtil;
import com.smartfarm.view.util.KJAnimations;
import com.smartfarm.view.util.StringUtils;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.AvatarView;
import com.smartfarm.widget.EmptyLayout;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.DialogHelper;

public class MessageDetailFragment extends
		BeseHaveHeaderListFragment<Comment, AppMessage> implements
		OnItemClickListener, OnItemLongClickListener, OnSendClickListener {

	private static final String CACHE_KEY_PREFIX = "msg_";
	private static final String CACHE_KEY_TWEET_COMMENT = "msg_comment_";
	private AvatarView mIvAvatar;
	private TextView mTvName, mTvFrom, mTvTime, mTvCommentCount, mLocation;
	private WebView mContent;
	private int mMsgId;
	private AppMessage mMessage;

	private View mLLLikeOPtion;
	private TextView mLikeUser;
	private ImageView mLikeState;

	private DetailActivity outAty;

	@Override
	protected CommentAdapter getListAdapter() {
		return new CommentAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		outAty = (DetailActivity) getActivity();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_TWEET_COMMENT + mMsgId + "_" + mCurrentPage;
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
		SmartfarmNetHelper.getComment(ApiContants.CONTROL_TYPE_MSG, mMsgId,
				mCurrentPage, mHandler);
	}

	@Override
	protected boolean requestDataIfViewCreated() {
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		outAty.emojiFragment.hideFlagButton();
	}

	private void fillUI() {
		mIvAvatar.setAvatarUrl(mMessage.getPortraitUrl().getImgThumbUrl());
		mIvAvatar.setUserInfo(mMessage.getAuthor(), mMessage.getUserName());
		mTvName.setText(mMessage.getUserName());
		mTvTime.setText(StringUtils.friendlyTime(mMessage.getPubTime()));
		
		if(ShowUtil.isEmpty(mMessage.getLocation())) {
			mLocation.setVisibility(View.GONE);
		} else {
			mLocation.setVisibility(View.VISIBLE);
			mLocation.setText(mMessage.getLocation());
		}
			
		mLocation.setText(mMessage.getLocation());
		switch (mMessage.getDevice()) {
		default:
			mTvFrom.setVisibility(View.GONE);
			break;
		case AppMessage.CLIENT_MOBILE:
			mTvFrom.setText(R.string.from_mobile);
			break;
		case AppMessage.CLIENT_ANDROID:
			mTvFrom.setText(R.string.from_android);
			break;
		case AppMessage.CLIENT_IPHONE:
			mTvFrom.setText(R.string.from_iphone);
			break;
		case AppMessage.CLIENT_WINDOWS_PHONE:
			mTvFrom.setText(R.string.from_windows_phone);
			break;
		case AppMessage.CLIENT_WECHAT:
			mTvFrom.setText(R.string.from_wechat);
			break;
		}

		mTvCommentCount.setText(mMessage.getCommentCount() + "");

		fillWebViewBody();
		setLikeUser();
		setLikeState();
	}

	private void setLikeState() {
		if (mMessage != null) {
			if (mMessage.isLike()) {
				mLikeState.setBackgroundResource(R.drawable.ic_likeed);
			} else {
				mLikeState.setBackgroundResource(R.drawable.ic_unlike);
			}
		}
	}

	private void setLikeUser() {
		if (mMessage == null || mMessage.getLikeUser() == null
				|| mMessage.getLikeUser().isEmpty()) {
			mLikeUser.setVisibility(View.GONE);
		} else {
			mLikeUser.setVisibility(View.VISIBLE);
			mMessage.setLikeUsers(getActivity(), mLikeUser, false);
		}
	}

	/**
	 * 填充webview内容
	 */
	private void fillWebViewBody() {
		StringBuffer body = new StringBuffer();
		body.append(UIHelper.WEB_STYLE + UIHelper.WEB_LOAD_IMAGES);

		StringBuilder tweetbody = new StringBuilder(InputHelper.getHtmlEmoji(
				getActivity().getResources(), mMessage.getContent()));

		String tweetBody = TextUtils.isEmpty(mMessage.getImg()) ? tweetbody
				.toString() : tweetbody.toString() + "<br/><img src=\""
				+ mMessage.getImgUrl().getImgThumbUrl() + "\">";
		body.append(setHtmlCotentSupportImagePreview(tweetBody));
		
		UIHelper.addWebImageShow(getActivity(), mContent);
		mContent.loadDataWithBaseURL(null, body.toString(), "text/html",
				"utf-8", null);
	}

	/**
	 * 添加图片放大支持
	 * 
	 * @param body
	 * @return
	 */
	private String setHtmlCotentSupportImagePreview(String body) {
		// 过滤掉 img标签的width,height属性
		body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
		return body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
				"$1$2\" onClick=\"javascript:mWebViewImageListener.showImagePreview('"
						+ mMessage.getImgUrl().getImgUrl() + "')\"");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Comment comment = mAdapter.getItem(position - 1);
		if (comment == null)
			return;
		outAty.emojiFragment.getEditText().setHint(
				"回复:" + comment.getAuthorName());
		outAty.emojiFragment.getEditText().setTag(comment);
		outAty.emojiFragment.showSoftKeyboard();
	}

	private final AsyncHttpResponseHandler mCommentHandler = new BaseAsyncHttpResHandler() {

		@Override
		public void onFailure(int errorCode, String data) {

			ToastTool.showToast(R.string.comment_publish_faile);
		}

		@Override
		public void onFinish() {
			hideLoadingDialog();
		}

		@Override
		public void onSuccess(ApiResponse res) {
			try {
				if (res.getErrorCode() > 0) {

					User user = AppContext.context().getUser();
					Comment comment = JSON.toJavaObject(
							(JSON) res.getResponseData(), Comment.class);
					comment.setAuthorName(user.getName());
					comment.setAuthorPortrait(user.getFace());
					comment.setAuthorId(Integer.valueOf(user.getRemark()));
					
					if(getTag() != null) {
						String name = getTag().toString();
						Log.d("mmsg", "@" + name);
						setTag(null);
					}

					ToastTool.showToast(R.string.comment_publish_success);
					mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
					mAdapter.addItem(0, comment);
					setTweetCommentCount();
				} else {
					ToastTool.showToast(R.string.comment_publish_faile);
				}
				outAty.emojiFragment.clean();
			} catch (Exception e) {
				e.printStackTrace();
				onFailure(res.getErrorCode(), res.getMessage());
			}
		}
	};

	// class DeleteOperationResponseHandler extends OperationResponseHandler {
	//
	// DeleteOperationResponseHandler(Object... args) {
	// super(args);
	// }
	//
	// @Override
	// public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
	// try {
	// Result res = XmlUtils.toBean(ResultBean.class, is).getResult();
	// if (res.OK()) {
	// ToastTool.showToastShort(R.string.delete_success);
	// mAdapter.removeItem(args[0]);
	// setTweetCommentCount();
	// } else {
	// ToastTool.showToastShort(res.getErrorMessage());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// onFailure(code, e.getMessage(), args);
	// }
	// }
	//
	// @Override
	// public void onFailure(int code, String errorMessage, Object[] args) {
	// ToastTool.showToastShort(R.string.delete_faile);
	// }
	// }

	private void handleDeleteComment(Comment comment) {
		if (!AppContext.context().isLogin()) {
			UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN);
			return;
		}
		ToastTool.showToast(R.string.deleting);

		// OSChinaApi.deleteComment(mTweetId, CommentList.CATALOG_TWEET,
		// comment.getId(), comment.getAuthorId(),
		// new DeleteOperationResponseHandler(comment));
	}

	private void setTweetCommentCount() {
		mAdapter.notifyDataSetChanged();
		if (mMessage != null) {
			mMessage.setCommentCount(mAdapter.getDataSize());
			mTvCommentCount.setText(mMessage.getCommentCount() + "");
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (position - 1 == -1) {
			return false;
		}
		final Comment item = mAdapter.getItem(position - 1);
		if (item == null)
			return false;
		int itemsLen = AppContext.context().getUser()
				.equalsUser(item.getAuthorId()) ? 2 : 1;
		String[] items = new String[itemsLen];
		items[0] = getResources().getString(R.string.copy);
		if (itemsLen == 2) {
			items[1] = getResources().getString(R.string.delete);
		}
		final CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(getActivity());
		dialog.setNegativeButton(R.string.cancle, null);
		dialog.setItemsWithoutChk(items, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialog.dismiss();
				if (position == 0) {
					CommonTool.copyTextToBoard(HTMLUtil.delHTMLTag(item
							.getContent()));
				} else if (position == 1) {
					handleDeleteComment(item);
				}
			}
		});
		dialog.show();
		return true;
	}

	@Override
	protected void requestDetailData(boolean isRefresh) {
		String key = getDetailCacheKey();
		mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
		if (CommonTool.isNetworkConnected()
				&& (!CacheManager.isExistDataCache(getActivity(), key) || isRefresh)) {
			SmartfarmNetHelper.getMsgDetail(mMsgId, mDetailHandler);
		} else {
			readDetailCacheData(key);
		}
	}

	@Override
	protected boolean isRefresh() {
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	protected View initHeaderView() {
		Intent args = getActivity().getIntent();
		mMsgId = args.getIntExtra("msg_id", 0);
		mMessage = (AppMessage) args.getParcelableExtra("msg");

		mListView.setOnItemLongClickListener(this);
		View header = LayoutInflater.from(getActivity()).inflate(
				R.layout.list_header_tweet_detail, null);
		mIvAvatar = (AvatarView) header.findViewById(R.id.iv_avatar);

		mTvName = (TextView) header.findViewById(R.id.tv_name);
		mTvFrom = (TextView) header.findViewById(R.id.tv_from);
		mTvTime = (TextView) header.findViewById(R.id.tv_time);
		mTvCommentCount = (TextView) header.findViewById(R.id.tv_comment_count);
		mContent = (WebView) header.findViewById(R.id.webview);
		mLocation = (TextView) header.findViewById(R.id.tv_location);
		UIHelper.initWebView(mContent);
		mLLLikeOPtion = header.findViewById(R.id.ll_like);
		mLLLikeOPtion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				likeOption();
			}
		});
		mLikeUser = (TextView) header.findViewById(R.id.tv_likeusers);
		mLikeState = (ImageView) header.findViewById(R.id.iv_like_state);
		return header;
	}

	private void likeOption() {
		if (mMessage == null)
			return;
		if (AppContext.context().getUser().equalsUser(mMessage.getAuthor())) {
			ToastTool.showToast("不能为自己点赞~");
			return;
		}

		if (AppContext.context().isLogin()) {
			if (mMessage.isLike()) {
				mMessage.setLike(false);

//				SmartfarmNetHelper.unthumbMsg(ApiContants.CONTROL_TYPE_MSG,
//						mMsgId, new BaseAsyncHttpResHandler());

				if (mMessage.getLikeUser() != null) {
					mMessage.getLikeUser().remove(0);
					mMessage.setThumbCount(mMessage.getThumbCount() - 1);
				}
			} else {
				mLikeState.setAnimation(KJAnimations.getScaleAnimation(1.5f,
						300));
				mMessage.setLike(true);
				mMessage.getLikeUser().add(0,
						AppContext.context().getUser().getAppUser());
				mMessage.setThumbCount(mMessage.getThumbCount() + 1);
//				SmartfarmNetHelper.thumbMsg(ApiContants.CONTROL_TYPE_MSG,
//						mMsgId, new BaseAsyncHttpResHandler());
			}
			setLikeState();
			mMessage.setLikeUsers(getActivity(), mLikeUser, false);
		} else {
			ToastTool.showToast("先登陆再点赞~");
		}
	}

	@Override
	protected String getDetailCacheKey() {
		return CACHE_KEY_PREFIX + mMsgId;
	}

	@Override
	protected void executeOnLoadDetailSuccess(AppMessage detailBean) {
		mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		this.mMessage = detailBean;
		fillUI();
		mAdapter.setNoDataText(R.string.comment_empty);
	}

	@Override
	protected AppMessage getDetailBean(Object obj) {
		return JSON.toJavaObject((JSON) obj, AppMessage.class);
	}

	@Override
	protected void executeOnLoadDataSuccess(List<Comment> data) {
		super.executeOnLoadDataSuccess(data);
		int commentCount = StringUtils.toInt(mMessage == null ? 0
				: this.mMessage.getCommentCount());
		if (commentCount < (mAdapter.getCount() - 1)) {
			commentCount = mAdapter.getCount() - 1;
		}
		mTvCommentCount.setText(commentCount + "");
	}

	@Override
	public void onClickSendButton(Editable str) {
		
		if (!AppContext.context().isLogin()) {
			UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN);
			return;
		}
		
		if (!CommonTool.isNetworkConnected()) {
			ToastTool.showToast(R.string.tip_network_error);
			return;
		}
		
		if (TextUtils.isEmpty(str)) {
			ToastTool.showToast(R.string.tip_comment_content_empty);
			return;
		}
		
		showLoadingDialog(R.string.progress_submit);
		
		try {
			 if (outAty.emojiFragment.getEditText().getTag() != null) {
				 Comment comment = (Comment) outAty.emojiFragment.getEditText().getTag();
				 
				 mCommentHandler.setTag(comment.getAuthorName());
				 SmartfarmNetHelper.pubCommentWithMention(ApiContants.CONTROL_TYPE_MSG, mMsgId, 
						 str.toString(), comment.getAuthorId(), mCommentHandler);
			 } else {
				 SmartfarmNetHelper.pubComment(ApiContants.CONTROL_TYPE_MSG, mMsgId, str.toString(), mCommentHandler);
			 }
		} catch (Exception e) {}
	}

	@Override
	public void onClickFlagButton() {
	}

}
