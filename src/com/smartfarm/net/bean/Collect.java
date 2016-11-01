package com.smartfarm.net.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("collect")
public class Collect extends Entity {

	private static final long serialVersionUID = -1725575623169131674L;

	@XStreamAlias("collectMsgs")
	private AppMessage collectMsgs;

	@XStreamAlias("collectNews")
	private News collectNews;

	@XStreamAlias("collectComments")
	private Comment collectComments;

	public AppMessage getCollectMsgs() {
		return collectMsgs;
	}

	public void setCollectMsgs(AppMessage collectMsgs) {
		this.collectMsgs = collectMsgs;
	}

	public News getCollectNews() {
		return collectNews;
	}

	public void setCollectNews(News collectNews) {
		this.collectNews = collectNews;
	}

	public Comment getCollectComments() {
		return collectComments;
	}

	public void setCollectComments(Comment collectComments) {
		this.collectComments = collectComments;
	}
}
