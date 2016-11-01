package com.smartfarm.net.bean;

import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.util.StringUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("new")
public class News extends Entity {

	private static final long serialVersionUID = -1874480422599868680L;

	public static final int HOT_TYPE_NORMAL = 1;
	public static final int HOT_TYPE_HOT = 2;
	public static final int HOT_TYPE_UP = 3;

	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("content")
	private String content;
	
	@XStreamAlias("fromUrl")
	private String fromUrl;
	
	@XStreamAlias("createTime")
	private String createTime;
	
	@XStreamAlias("fromDescr")
	private String fromDescr;
	
	@XStreamAlias("thumbCount")
	private int thumbCount;
	
	@XStreamAlias("commentCount")
	private int commentCount;
	
	@XStreamAlias("collect")
	private boolean collect;

	private String friendlyTime;
	
	public boolean isCollect() {
		return collect;
	}

	public void setCollect(boolean collect) {
		this.collect = collect;
	}

	public String getFriendlyTime() {
		
		if(ShowUtil.isEmpty(friendlyTime))
			friendlyTime = StringUtils.friendlyTime(createTime);
		
		return friendlyTime;
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

	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFromDescr() {
		return fromDescr;
	}

	public void setFromDescr(String fromDescr) {
		this.fromDescr = fromDescr;
	}

	public int getThumbCount() {
		return thumbCount;
	}

	public void setThumbCount(int thumbCount) {
		this.thumbCount = thumbCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
}
