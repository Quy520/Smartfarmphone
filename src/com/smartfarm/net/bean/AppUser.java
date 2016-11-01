package com.smartfarm.net.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("user")
public class AppUser extends Entity {

	private static final long serialVersionUID = -1385889658657045219L;

	@XStreamAlias("name")
	private String userName;
	
	@XStreamAlias("face")
	private String userPortrait;

	@XStreamAlias("signature")
	private String signature;
    
    private ImageUrl imageUrl;
    
    public AppUser() {}
    
    public AppUser(int id, String name, String userPortrait) {
    	
    	this.id = id;
    	this.userName = name;
    	this.userPortrait = userPortrait;
    }
    
    public ImageUrl getImageUrl() {
    	
    	if(imageUrl == null)
    		imageUrl = new ImageUrl(userPortrait);
    	
    	return imageUrl;
    }

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPortrait() {
		return userPortrait;
	}

	public void setUserPortrait(String userPortrait) {
		this.userPortrait = userPortrait;
	}

	@Override
	public String toString() {

		return "user name : " + userName + ", user id : " + id;
	}
	
	
}
