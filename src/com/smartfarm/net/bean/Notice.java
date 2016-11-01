package com.smartfarm.net.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 通知信息实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@XStreamAlias("notice")
public class Notice implements Serializable {

	private static final long serialVersionUID = -3901701114252249610L;
	
	public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "smartfarm";

    public final static int TYPE_COMMENT = 1;
    public final static int TYPE_REVERTS = 2;

    @XStreamAlias("atmeCount")
    private int commentCount;

    @XStreamAlias("msgCount")
    private int revertsCount;

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getRevertsCount() {
		return revertsCount;
	}

	public void setRevertsCount(int revertsCount) {
		this.revertsCount = revertsCount;
	}

}
