package com.smartfarm.net.bean;

import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.smartfarm.db.bean.User;
import com.smartfarm.view.AppContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("message")
public class AppMessage extends Entity implements Parcelable {

	public static final int CLIENT_MOBILE = 1;
	public static final int CLIENT_ANDROID = 2;
	public static final int CLIENT_IPHONE = 3;
	public static final int CLIENT_WINDOWS_PHONE = 4;
	public static final int CLIENT_WECHAT = 5;

	private static final long serialVersionUID = 4957889560505714553L;

	@XStreamAlias("msgId")
	private int msgId;
	
	@XStreamAlias("author")
	private int author;
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("content")
	private String content;
	
	@XStreamAlias("commentCount")
	private int commentCount;
	
	@XStreamAlias("img")
	private String img;
	
	@XStreamAlias("location")
	private String location;
	
	@XStreamAlias("original")
	private int original;
	
	@XStreamAlias("pubTime")
	private String pubTime;
	
	@XStreamAlias("thumbCount")
	private int thumbCount;
	
	@XStreamAlias("topic")
	private String topic;
	
	@XStreamAlias("transferCount")
	private int transferCount;
	
	@XStreamAlias("userName")
	private String userName;
	
	@XStreamAlias("userPortrait")
	private String userPortrait;
	
	@XStreamAlias("oriContent")
	private String oriContent;
	
	@XStreamAlias("oriImg")
	private String oriImg;
	
	@XStreamAlias("likeUser")
	private List<AppUser> likeUser;

    @XStreamAlias("like")
    private boolean like;

    @XStreamAlias("device")
    private int device = 0;
	
	private ImageUrl imgUrl;
	private ImageUrl oriImgUrl; 
	private ImageUrl portraitUrl; 
	
	public AppMessage() {}
	
	public AppMessage(Parcel source) {
		
		msgId = source.readInt();
		author = source.readInt();
		title = source.readString();
		content = source.readString();
		commentCount = source.readInt();
		img = source.readString();
		location = source.readString();
		original = source.readInt();
		pubTime = source.readString();
		thumbCount = source.readInt();
		topic = source.readString();
		transferCount = source.readInt();
		userName = source.readString();
		userPortrait = source.readString();
		oriContent = source.readString();
		oriImg = source.readString();
		device = source.readInt();
	}

    public void setLikeUsers(Context contet, TextView likeUser, boolean limit) {
        // 构造多个超链接的html, 通过选中的位置来获取用户名
        if (getThumbCount() > 0 && getLikeUser() != null
                && !getLikeUser().isEmpty()) {
            likeUser.setVisibility(View.VISIBLE);
            likeUser.setMovementMethod(LinkMovementMethod.getInstance());
            likeUser.setFocusable(false);
            likeUser.setLongClickable(false);
            likeUser.setText(addClickablePart(contet, limit),
                    BufferType.SPANNABLE);
        } else {
            likeUser.setVisibility(View.GONE);
            likeUser.setText("");
        }
    }

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public List<AppUser> getLikeUser() {
		return likeUser;
	}

	public void setLikeUser(List<AppUser> likeUser) {
		this.likeUser = likeUser;
	}

	public ImageUrl getPortraitUrl() {
		
		if(portraitUrl == null)
			portraitUrl = new ImageUrl(userPortrait);
		
		return portraitUrl;
	}

	public ImageUrl getImgUrl() {
		
		if(imgUrl == null)
			imgUrl = new ImageUrl(img);
		
		return imgUrl;
	}

	public ImageUrl getOriImgUrl() {
		
		if(oriImgUrl == null)
			oriImgUrl = new ImageUrl(oriImg);
		
		return oriImgUrl;
	}

	public String getOriContent() {
		return oriContent;
	}

	public void setOriContent(String oriContent) {
		this.oriContent = oriContent;
	}

	public String getOriImg() {
		return oriImg;
	}

	public void setOriImg(String oriImg) {
		this.oriImg = oriImg;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getAuthor() {
		return author;
	}

	public void setAuthor(int author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getOriginal() {
		return original;
	}

	public void setOriginal(int original) {
		this.original = original;
	}

	public String getPubTime() {
		return pubTime;
	}

	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}

	public int getThumbCount() {
		return thumbCount;
	}

	public void setThumbCount(int thumbCount) {
		this.thumbCount = thumbCount;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getTransferCount() {
		return transferCount;
	}

	public void setTransferCount(int transferCount) {
		this.transferCount = transferCount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPortrait() {
		return userPortrait;
	}

	public void setUserPortrait(String userPortrait) {
		this.userPortrait = userPortrait;
	}
	
	private SpannableStringBuilder addClickablePart(final Context context,
            boolean limit) {

        StringBuilder sbBuilder = new StringBuilder();
        int showCunt = getLikeUser().size();
        if (limit && showCunt > 4) {
            showCunt = 4;
        }

        // 如果已经点赞，始终让该用户在首位
        if (isLike()) {

        	User me = AppContext.context().getUser();
            for (int i = 0; i < getLikeUser().size(); i++) {
                if (me.equalsUser(getLikeUser().get(i).getId())) {
                    getLikeUser().remove(i);
                }
            }
            getLikeUser().add(0, new AppUser(me.getUid(), me.getName(), me.getFace()));
        }

        for (int i = 0; i < showCunt; i++) {
            sbBuilder.append(getLikeUser().get(i).getUserName() + "、");
        }

        String likeUsersStr = sbBuilder
                .substring(0, sbBuilder.lastIndexOf("、")).toString();

        // 第一个赞图标
        // ImageSpan span = new ImageSpan(AppContext.getInstance(),
        // R.drawable.ic_unlike_small);
        SpannableString spanStr = new SpannableString("");
        // spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(likeUsersStr);

        String[] likeUsers = likeUsersStr.split("、");

        if (likeUsers.length > 0) {
            // 最后一个
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = likeUsersStr.indexOf(name) + spanStr.length();
                final int index = i;
                ssb.setSpan(new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
//                        AppUser user = getLikeUser().get(index);
//                        UIHelper.showUserCenter(context, user.getId(), user.getUserName());
                        //show user center
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        // ds.setColor(R.color.link_color); // 设置文本颜色
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }

                }, start, start + name.length(), 0);
            }
        }
        if (likeUsers.length < getThumbCount()) {
            ssb.append("等");
            int start = ssb.length();
            String more = getThumbCount() + "人";
            ssb.append(more);
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, getId());
//                    UIHelper.showSimpleBack(context, SimpleBackPage.TWEET_LIKE_USER_LIST, bundle);
                	// to thumb user list
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    // ds.setColor(R.color.link_color); // 设置文本颜色
                    // 去掉下划线
                    ds.setUnderlineText(false);
                }

            }, start, start + more.length(), 0);
            return ssb.append("觉得很赞");
        } else {
            return ssb.append("觉得很赞");
        }
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(msgId);
        dest.writeInt(author);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeInt(commentCount);
        dest.writeString(img);
        dest.writeString(location);
        dest.writeInt(original);
        dest.writeString(pubTime);
        dest.writeInt(thumbCount);
        dest.writeString(topic);
        dest.writeInt(transferCount);
        dest.writeString(userName);
        dest.writeString(userPortrait);
        dest.writeString(oriContent);
        dest.writeString(oriImg);
        dest.writeInt(device);
	}

    public static final Parcelable.Creator<AppMessage> CREATOR = new Creator<AppMessage>() {

        @Override
        public AppMessage[] newArray(int size) {
            return new AppMessage[size];
        }

        @Override
        public AppMessage createFromParcel(Parcel source) {
            return new AppMessage(source);
        }
    };
}
