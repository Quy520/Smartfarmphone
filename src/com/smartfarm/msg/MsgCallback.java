package com.smartfarm.msg;

public interface MsgCallback {

	public void onSuccess();
	
	public void onFaild(int errorCode, String errorMsg);
}
