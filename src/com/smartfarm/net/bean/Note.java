package com.smartfarm.net.bean;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("note")
public class Note extends Entity {

	private static final long serialVersionUID = 1283525201209239618L;
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("descr")
	private String descr;
	
	@XStreamAlias("pubDate")
	private Date pubDate;
	
	@XStreamAlias("commentCount")
	private int commentCount;
	
	@XStreamAlias("favorite")
	private boolean favorite;

	@XStreamAlias("location")
	private String location;
	
	@XStreamAlias("img")
	private String img;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	
}
