package com.smartfarm.net.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.smartfarm.db.bean.User;

public class ApiUser {

	private int userId;
	private String name;
	private String birthday;
	private boolean sex;
	private String addr;
	private String signature;
	private String portrait;
	private String email;
	private String qq;
	private String token;
	private String phone;
	private String createTime;
	private String hxPwd;
	private String refreshToken;

	public User getUser() {
		
		User user = new User();
		
		user.setId(userId);
		user.setAddr(addr);
		user.setFace(portrait);
		user.setName(name);
		user.setPhone(phone);
		user.setSex(sex);
		user.setBirthday(birthday);
		user.setEmail(email);
		user.setQq(qq);
		user.setSignature(signature);
		user.setRemark(String.valueOf(userId));
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
			user.setTime(sdf.parse(createTime));
		} catch (ParseException e) {

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
				user.setTime(sdf.parse(createTime));
			} catch(ParseException pe) {
				user.setTime(new Date());
			}
		}
		
		return user;
	}

	public String getHxPwd() {
		return hxPwd;
	}


	public void setHxPwd(String hxPwd) {
		this.hxPwd = hxPwd;
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {

		return "user name : " + name + ", user phone : " + phone;
	}
}
