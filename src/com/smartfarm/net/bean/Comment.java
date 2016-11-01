package com.smartfarm.net.bean;

import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.util.StringUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("comment")
public class Comment extends Entity {

	private static final long serialVersionUID = 410585562494835585L;

	@XStreamAlias("authorName")
	private String authorName;
	
	@XStreamAlias("authorPortrait")
	private String authorPortrait;
	
	@XStreamAlias("authorId")
	private int authorId;
	
	@XStreamAlias("content")
	private String content;
	
	@XStreamAlias("badCount")
	private int badCount;
	
	@XStreamAlias("thumbCount")
	private int thumbCount;
	
	@XStreamAlias("mentionName")
	private String mentionName;

	@XStreamAlias("commentTime")
	private String commentTime;

	private String friendlyTime;
	
	private ImageUrl portraitUrl;
	
	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public ImageUrl getPortraitUrl() {
		
		if(portraitUrl == null)
			portraitUrl = new ImageUrl(authorPortrait);
		
		return portraitUrl;
	}

	public String getFriendlyTime() {
		
		if(ShowUtil.isEmpty(friendlyTime))
			friendlyTime = StringUtils.friendlyTime(commentTime);
		
		return friendlyTime;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorPortrait() {
		return authorPortrait;
	}

	public void setAuthorPortrait(String authorPortrait) {
		this.authorPortrait = authorPortrait;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getBadCount() {
		return badCount;
	}

	public void setBadCount(int badCount) {
		this.badCount = badCount;
	}

	public int getThumbCount() {
		return thumbCount;
	}

	public void setThumbCount(int thumbCount) {
		this.thumbCount = thumbCount;
	}

	public String getMentionName() {
		return mentionName;
	}

	public void setMentionName(String mentionName) {
		this.mentionName = mentionName;
	}
}
