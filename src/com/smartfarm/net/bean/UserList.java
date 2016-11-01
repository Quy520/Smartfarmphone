package com.smartfarm.net.bean;

import java.util.ArrayList;
import java.util.List;

import com.smartfarm.bean.ListEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class UserList extends Entity implements ListEntity<AppUser> {

	private static final long serialVersionUID = 8346005418138745553L;

	@XStreamAlias("pagesize")
    private int pageSize;
    
    @XStreamAlias("allCount")
    private int allCount;
    
    @XStreamAlias("users")
    private final List<AppUser> commentlist = new ArrayList<AppUser>();

	@Override
	public List<AppUser> getList() {
		return commentlist;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public List<AppUser> getCommentlist() {
		return commentlist;
	}
}
