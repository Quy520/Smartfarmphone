package com.smartfarm.net.bean;

import java.util.List;

import com.smartfarm.bean.ListEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("smartfarm")
public class ApiUserList extends Entity implements ListEntity<AppUser> {

	private static final long serialVersionUID = -8410128686845822811L;

	@XStreamAlias("users")
	private List<AppUser> users;

	public List<AppUser> getUsers() {
		return users;
	}

	public void setUsers(List<AppUser> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return users.toString();
	}

	@Override
	public List<AppUser> getList() {
		return users;
	}
}
