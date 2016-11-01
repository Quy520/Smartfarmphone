package com.smartfarm.net.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("user_info")
public class UserPublicInfo extends Entity {

	private static final long serialVersionUID = 961275187026564643L;

	@XStreamAlias("name")
	private String name;

	@XStreamAlias("portrait")
	private String portrait;

	@XStreamAlias("sex")
	private boolean sex;

	@XStreamAlias("signature")
	private String signature;

	@XStreamAlias("integral")
	private int integral;

	@XStreamAlias("commentCount")
	private int commentCount;

	@XStreamAlias("pubCount")
	private int pubCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getPubCount() {
		return pubCount;
	}

	public void setPubCount(int pubCount) {
		this.pubCount = pubCount;
	}

}
