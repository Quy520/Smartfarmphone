package com.smartfarm.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smartfarm.view.MsgService;

public class BootBroadcastReciver extends BroadcastReceiver {
	
 	@Override
 	public void onReceive(Context context, Intent intent) {

		Intent serviceIntent = new Intent();
		serviceIntent.setClass(context, MsgService.class);
		context.startService(serviceIntent);
 	}
}
