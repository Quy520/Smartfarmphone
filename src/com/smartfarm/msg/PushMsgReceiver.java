package com.smartfarm.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.smartfarm.bean.PublicPushMsg;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.PushNoticeActivity;
import com.smartfarm.view.util.ConfigManager;

public class PushMsgReceiver extends BroadcastReceiver {
	
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    String taskid = bundle.getString("taskid");
                    String messageid = bundle.getString("messageid");
                    
                    Log.d(Constants.TAG, "receive msg by getui -> " + data);
                    PublicPushMsg resolveMsg = resolvePublicMsg(data);
    				
                    boolean res = false;
    				if(resolveMsg == null) {
    					
    					if(data.contains("#huanxin")) {
    						
    						ConfigManager.setSendWay(context, ConfigManager.SEND_BY_HUANXIN);
    						res = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90002);
    					} else if(data.contains("#rongyun")) {
    						
    						ConfigManager.setSendWay(context, ConfigManager.SEND_BY_HUANXIN);
    						res = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90002);
    					}

    					System.out.println("第三方回执接口调用" + (res ? "成功" : "失败"));
    					return;
    				}

					res = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90003);
					System.out.println("第三方回执接口调用" + (res ? "成功" : "失败"));
					
					PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);  
    				if(!pm.isScreenOn()) {
    					
    					Intent it = new Intent();
    					it.setClass(context, PushNoticeActivity.class);
    					it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    					Bundle b = new Bundle();
    					b.putString("title", resolveMsg.getTitle());
    					b.putString("content", resolveMsg.getContent());
    					b.putString("url", resolveMsg.getUrl());
    					it.putExtras(b);
    					context.startActivity(it);
    				}
    				
    				ShowUtil.showNotice(context, resolveMsg.getTitle(), resolveMsg.getTips(), resolveMsg.getUrl());
                }
                break;
            default:
                break;
        }
    }
    
    private PublicPushMsg resolvePublicMsg(String msg) {
		PublicPushMsg result = new PublicPushMsg();
		
		if(ShowUtil.isEmpty(msg) || !msg.contains(":&:"))
			return null;
		
		String[] splitMsg = msg.split(":&:");	
		
		for(int i = 0; i < splitMsg.length; i++) {
			
			if(i == 0) {
				result.setTitle(splitMsg[i]);
			} else if(i == 1) {
				result.setTips(splitMsg[i]);
			} else if(i == 2) {
				result.setContent(splitMsg[i]);
			} else if(i == 3) {
				result.setUrl(splitMsg[i]);
			}
		}
		
		return result;
	}
}
