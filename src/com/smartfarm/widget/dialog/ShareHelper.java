package com.smartfarm.widget.dialog;

import android.app.Activity;

import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.R;
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

public class ShareHelper {
	public static void handleShare(final Activity activity) {
		
		final ShareDialog dialog = new ShareDialog(activity);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("分享到");
		dialog.setOnPlatformClickListener(new OnSharePlatformClick() {
			@Override
			public void onPlatformClick(int id) {
				switch (id) {
				case R.id.ly_share_weichat_circle:

					shareToWeiChatCircle(activity);

					break;
				case R.id.ly_share_weichat:

					shareToWeiChatFriend(activity);

					break;
				case R.id.ly_share_qq:

					shareToQQ(activity);

					break;
				case R.id.ly_share_qzone:

					shareToQzone(activity);

					break;
				case R.id.ly_share_other:

					ShowUtil.showSystemShareOption(activity, Constants.SHARE_TITLE, Constants.LINKED_URL);

					break;
				default:
					break;
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private static void shareToQzone(Activity activity) {
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
		qZoneSsoHandler.addToSocialSDK();

		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(Constants.SHARE_CONTENT);
		qzone.setTargetUrl(Constants.LINKED_URL);
		qzone.setTitle(Constants.SHARE_TITLE);
		qzone.setShareImage(new UMImage(activity, ShowUtil.takeScreenShot(activity)));
		
		UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.setShareMedia(qzone);
		mController.postShare(activity, SHARE_MEDIA.QZONE, null);
	}

	private static void shareToQQ(Activity activity) {
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
				Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
		qqSsoHandler.setTargetUrl(Constants.LINKED_URL);
		qqSsoHandler.setTitle(Constants.SHARE_TITLE);
		qqSsoHandler.addToSocialSDK();
		
		UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.setShareContent(Constants.SHARE_CONTENT);
		mController.setShareImage(new UMImage(activity, ShowUtil
				.takeScreenShot(activity)));
		mController.postShare(activity, SHARE_MEDIA.QQ, null);
	}

	private static void shareToWeiChatFriend(Activity activity) {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, Constants.WE_CHAT_APP_ID,
				Constants.WE_CHAT_APP_SECRET);
		wxHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setTitle(Constants.SHARE_TITLE);
		weixinContent.setShareContent(Constants.SHARE_CONTENT);
		weixinContent.setTargetUrl(Constants.LINKED_URL);
		weixinContent.setShareImage(new UMImage(activity, ShowUtil
				.takeScreenShot(activity)));

		UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.setShareMedia(weixinContent);
		mController.postShare(activity, SHARE_MEDIA.WEIXIN, null);
	}

	private static void shareToWeiChatCircle(Activity activity) {
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity,
				Constants.WE_CHAT_APP_ID, Constants.WE_CHAT_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setTitle(Constants.SHARE_TITLE);
		circleMedia.setShareContent(Constants.SHARE_CONTENT);
		circleMedia.setTargetUrl(Constants.LINKED_URL);
		circleMedia.setShareImage(new UMImage(activity, ShowUtil
				.takeScreenShot(activity)));

		UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.setShareMedia(circleMedia);
		mController.postShare(activity, SHARE_MEDIA.WEIXIN_CIRCLE, null);
	}
}
