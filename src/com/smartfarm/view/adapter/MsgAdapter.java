package com.smartfarm.view.adapter;

import java.util.Iterator;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.bitmap.BitmapHelper;
import org.kymjs.kjframe.utils.DensityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smartfarm.emoji.InputHelper;
import com.smartfarm.net.ApiContants;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.AppMessage;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.ImagePreviewActivity;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ImageUtils;
import com.smartfarm.view.util.KJAnimations;
import com.smartfarm.view.util.StringUtils;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.AvatarView;
import com.smartfarm.widget.MyLinkMovementMethod;
import com.smartfarm.widget.MyURLSpan;
import com.smartfarm.widget.TweetTextView;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.DialogHelper;

public class MsgAdapter extends ListBaseAdapter<AppMessage> {

    private Bitmap recordBitmap;
    private Context context;
    private final KJBitmap kjb = new KJBitmap();

    final private AsyncHttpResponseHandler handler = new BaseAsyncHttpResHandler() {
		
		@Override
		public void onSuccess(ApiResponse res) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFailure(int errorCode, String data) {
			// TODO Auto-generated method stub
			
		}
	};
    
    @SuppressLint("InflateParams")
	@Override
    protected View getRealView(final int position, View convertView,
            final ViewGroup parent) {
        context = parent.getContext();
        final ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_msg, null);
            vh = new ViewHolder();

            vh.author = (TextView) convertView.findViewById(R.id.tv_tweet_name);
            vh.time = (TextView) convertView.findViewById(R.id.tv_tweet_time);
            vh.content = (TweetTextView) convertView.findViewById(R.id.tweet_item);
            vh.commentcount = (TextView) convertView.findViewById(R.id.tv_tweet_comment_count);
            vh.platform = (TextView) convertView.findViewById(R.id.tv_tweet_platform);
            vh.face = (AvatarView) convertView.findViewById(R.id.iv_tweet_face);
            vh.image = (ImageView) convertView.findViewById(R.id.iv_tweet_image);
            vh.del = (TextView) convertView.findViewById(R.id.tv_del);
            vh.likeState = (ImageView) convertView.findViewById(R.id.iv_like_state);
            vh.likeUsers = (TextView) convertView.findViewById(R.id.tv_likeusers);
            vh.location = (TextView) convertView.findViewById(R.id.tv_location);
            vh.likeOption = convertView.findViewById(R.id.ll_like);
            
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final AppMessage msg = mDatas.get(position);

        if (AppContext.context().getUser().equalsUser(msg.getAuthor())) {
            vh.del.setVisibility(View.VISIBLE);
            vh.del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    optionDel(parent.getContext(), msg, position);
                }
            });
            vh.likeOption.setVisibility(View.GONE);
        } else {
            vh.del.setVisibility(View.GONE);
            vh.likeOption.setVisibility(View.VISIBLE);
        }

        vh.face.setUserInfo(msg.getAuthor(), msg.getUserName());
        vh.face.setAvatarUrl(msg.getPortraitUrl().getImgThumbUrl());
        vh.author.setText(msg.getUserName());
        vh.time.setText(StringUtils.friendlyTime(msg.getPubTime()));
        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);

        Spanned span = Html.fromHtml(msg.getContent().trim());

//        if (!StringUtils.isEmpty(msg.getImg())) {
//            if (recordBitmap == null) {
//                initRecordImg(parent.getContext());
//            }
//            ImageSpan recordImg = new ImageSpan(parent.getContext(),
//                    recordBitmap);
//            SpannableString str = new SpannableString("c");
//            str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            vh.content.setText(str);
//            span = InputHelper.displayEmoji(context.getResources(), span);
//            vh.content.append(span);
//        } else {
            span = InputHelper.displayEmoji(context.getResources(), span);
            vh.content.setText(span);
//        }
        MyURLSpan.parseLinkText(vh.content, span);

        vh.commentcount.setText(String.valueOf(msg.getCommentCount()));

        showTweetImage(vh, msg.getImgUrl().getImgThumbUrl(), msg.getImgUrl().getImgUrl(),
                parent.getContext());
        msg.setLikeUsers(context, vh.likeUsers, true);
        final ViewHolder vh1 = vh;
        OnClickListener likeClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.context().isLogin()) {
                    if (AppContext.context().getUser().equalsUser(msg.getAuthor())) {
                        ToastTool.showToast("不能给自己点赞~");
                    } else {
                        updateLikeState(vh1, msg);
                    }

                } else {
                	ToastTool.showToast("先登陆再赞~");
                    UIHelper.showSimpleBack(context, BackPage.LOGIN_PHONE);
                }
            }
        };

        vh.likeOption.setOnClickListener(likeClick);
        if (msg.isLike()) {
            vh.likeState.setBackgroundResource(R.drawable.ic_likeed);
        } else {
            vh.likeState.setBackgroundResource(R.drawable.ic_unlike);
        }
        
        if(ShowUtil.isEmpty(msg.getLocation())) {
        	vh.location.setVisibility(View.GONE);
        } else {
        	vh.location.setVisibility(View.VISIBLE);
        	vh.location.setText(msg.getLocation());
        }
        
        vh.platform.setVisibility(View.VISIBLE);
//        switch (tweet.getAppclient()) {
//        case Tweet.CLIENT_MOBILE:
//            vh.platform.setText(R.string.from_mobile);
//            break;
//        case Tweet.CLIENT_ANDROID:
            vh.platform.setText(R.string.from_android);
//            break;
//        case Tweet.CLIENT_IPHONE:
//            vh.platform.setText(R.string.from_iphone);
//            break;
//        case Tweet.CLIENT_WINDOWS_PHONE:
//            vh.platform.setText(R.string.from_windows_phone);
//            break;
//        case Tweet.CLIENT_WECHAT:
//            vh.platform.setText(R.string.from_wechat);
//            break;
//        default:
//            vh.platform.setText("");
//            vh.platform.setVisibility(View.GONE);
//            break;
//        }
        return convertView;
    }

    private void updateLikeState(ViewHolder vh, AppMessage msg) {
        if (msg.isLike()) {
        	msg.setLike(false);
        	msg.setThumbCount(msg.getThumbCount() - 1);
            if (msg.getLikeUser() != null && !msg.getLikeUser().isEmpty()) {
            	msg.getLikeUser().remove(0);
            }
            //点赞
//            SmartfarmNetHelper.unthumbMsg(ApiContants.CONTROL_TYPE_MSG, msg.getMsgId(), handler);
            vh.likeState.setBackgroundResource(R.drawable.ic_unlike);
        } else {
        	msg.setLike(true);
            vh.likeState.setAnimation(KJAnimations.getScaleAnimation(1.5f, 300));
            
            if(msg.getLikeUser() != null)
            	msg.getLikeUser().add(0, AppContext.context().getUser().getAppUser());
          //点赞
//            SmartfarmNetHelper.thumbMsg(ApiContants.CONTROL_TYPE_MSG, msg.getMsgId(), handler);
            vh.likeState.setBackgroundResource(R.drawable.ic_likeed);
            msg.setThumbCount(msg.getThumbCount() + 1);
        }
        msg.setLikeUsers(context, vh.likeUsers, true);
    }

    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.audio3);
        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(cxt, 20f), DensityUtils.dip2px(cxt, 20f));
    }
    
    /**
     * 动态设置动弹列表图片显示规则
     * 
     * @author kymjs
     */
    private void showTweetImage(final ViewHolder vh, String imgSmall,
            final String imgBig, final Context context) {
        if (imgSmall != null && !TextUtils.isEmpty(imgSmall)) {
            kjb.display(vh.image, imgSmall, new BitmapCallBack() {
                @Override
                public void onPreLoad() {
                    super.onPreLoad();
                    vh.image.setImageResource(R.drawable.pic_bg);
                }

                @Override
                public void onSuccess(Bitmap bitmap) {
                    super.onSuccess(bitmap);
                    if (bitmap != null) {
                        bitmap = BitmapHelper.scaleWithXY(bitmap,
                                300 / bitmap.getHeight());
                        vh.image.setImageBitmap(bitmap);
                    }
                }
            });
            vh.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePreviewActivity.showImagePrivew(context, 0,
                            new String[] { imgBig });
                }
            });
            vh.image.setVisibility(AvatarView.VISIBLE);
        } else {
            vh.image.setVisibility(AvatarView.GONE);
        }
    }
    
    private void optionDel(Context context, final AppMessage msg,
            final int position) {

        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(context);
        dialog.setTitle("提示");
        dialog.setMessage("确定删除吗？");
        dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //删除信息
//                SmartfarmNetHelper.getRemoveMsg(msg.getMsgId(), new BaseAsyncHttpResHandler() {
//                	
//                	@Override
//                	public void onSuccess(ApiResponse res) {
//
//                		if(res.getErrorCode() > 0) {
//                			ToastTool.showToast("删除成功!");
//                			
//                			Iterator<AppMessage> it = mDatas.iterator();
//                			while(it.hasNext())
//                				if(it.next().getMsgId() == msg.getMsgId()) {
//                					it.remove();
//                					break;
//                				}
//                			
//                			notifyDataSetChanged();
//                		}
//                	}
//
//					@Override
//					public void onFailure(int errorCode, String data) {
//						
//					}
//                });
            }
        });
        dialog.setPositiveButton("取消", null);

        dialog.show();
    }

	static class ViewHolder {
        TextView author;
        TextView time;
        TweetTextView content;
        TextView commentcount;
        TextView platform;
        AvatarView face;
        ImageView image;
        ImageView likeState;
        TextView del;
        TextView likeUsers;
        TextView location;
        View likeOption;
    }
}
