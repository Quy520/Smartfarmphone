package com.smartfarm.db.bean;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartfarm.net.bean.AppUser;
import com.smartfarm.tools.ShowUtil;

@DatabaseTable(tableName = "user") 
public class User {

    @DatabaseField(generatedId = true) 
	private int id;

    @DatabaseField(columnName = "name") 
	private String name;
	
    @DatabaseField(columnName = "sex") 
	private boolean sex;
	
    @DatabaseField(columnName = "phone_num") 
	private String phone;
	
    @DatabaseField(columnName = "addr") 
	private String addr;
	
    @DatabaseField(columnName = "remark") 
	private String remark;

    @DatabaseField(columnName = "time") 
	private Date time;

    @DatabaseField(columnName = "face") 
	private String face;

    @DatabaseField(columnName = "qq") 
    private String qq;

    @DatabaseField(columnName = "email") 
    private String email;

    @DatabaseField(columnName = "birthday") 
    private String birthday;

    @DatabaseField(columnName = "signature") 
	private String signature;

	public User() {}
	
	public User(String name, String phone) {
		
		this.name = name;
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}
	
	public boolean equalsUser(int uId) {
		
		if(ShowUtil.isEmpty(remark))
			return false;
		
		if(remark.equals(String.valueOf(uId)))
			return true;
		
		return false;
	}
	
	public AppUser getAppUser() {
		return new AppUser(getUid(), name, face);
	}
	
	public int getUid() {
		
		try {
			return Integer.valueOf(remark);
		} catch(Exception e) {
			return 0;
		}
	}
}
