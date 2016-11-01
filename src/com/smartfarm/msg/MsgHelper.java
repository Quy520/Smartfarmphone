package com.smartfarm.msg;

import com.smartfarm.view.AppContext;
import com.smartfarm.view.util.ConfigManager;

public class MsgHelper {

	private static MsgHelper mHelper;
	
	private HuanxinHelper huanxin;
	private int currType;
	
	public static void init() {
		
		mHelper = new MsgHelper();
	}
	
	public static ImHelperInterface get() {
		
		return mHelper.huanxin;
	}
	
	public static MsgHelper getMsgHelper() {
		
		return mHelper;
	}
	
	private MsgHelper() {
		
		huanxin = new HuanxinHelper();
		huanxin.init();
		
		currType = ConfigManager.getInstance().getSendWay();
	}
	
	public void setSendWay(int type) {
		
		currType = type;
		ConfigManager.setSendWay(AppContext.context(), type);
	}
	
	public int getSendWay() {
		
		return currType;
	}
}
