package com.smartfarm.msg;

import android.app.Activity;

public interface ImHelperInterface {
    
	public static final int CONN_RES_SCUESS = 666;
	public static final int CONN_RES_REMOVEED = CONN_RES_SCUESS + 1;
	public static final int CONN_RES_CONFLTCT = CONN_RES_REMOVEED + 1;
	public static final int CONN_RES_NO_NET = CONN_RES_CONFLTCT + 1;
	public static final int CONN_RES_SERVER_ERROR = CONN_RES_NO_NET + 1;
	public static final int CONN_RES_EXIT = CONN_RES_SERVER_ERROR + 1;
	
	public void init();
	
	public void exit();
	
	public void relink(Activity activity);
	
	public void sendMsg(Protocol msg);
	
	public void sendMsg(Protocol msg, MsgCallback callback);
	
	public void checkLink();
	
	public boolean isLogin();
	
	public boolean login(MsgCallback callback);
	
	public void logout(MsgCallback callback);
	
	
}
