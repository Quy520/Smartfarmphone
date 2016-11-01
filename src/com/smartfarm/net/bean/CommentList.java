package com.smartfarm.net.bean;

import java.util.List;

import com.smartfarm.bean.ListEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("smartfarm")
public class CommentList extends Entity implements ListEntity<Comment> {

	private static final long serialVersionUID = -6013305235207530153L;
    
    @XStreamAlias("comments")
    private List<Comment> comments;
	
	@Override
	public List<Comment> getList() {
		return comments;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
