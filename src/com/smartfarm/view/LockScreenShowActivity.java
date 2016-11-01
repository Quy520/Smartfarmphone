package com.smartfarm.view;

import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.smartfarm.bean.TempBean;
import com.smartfarm.bean.TempTuple;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.widget.DialogTitleView;
import com.smartfarm.widget.TempWindow;

@SuppressLint("InflateParams")
public class LockScreenShowActivity extends Activity {
	
	private int oldTimeOut = -1;
	
	BroadcastReceiver offScreenReceiver = new BroadcastReceiver() {  
        @Override  
        public void onReceive(final Context context, final Intent intent) {  
             
        	LockScreenShowActivity.this.finish();
        }  
    };  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		setContentView(R.layout.push_msg_view);
		
		try {
			oldTimeOut = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e1) {
			e1.printStackTrace();
		}
		Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 15000);
		
		try {
			initShowContent();
		} catch(Exception e) {
			e.printStackTrace();
		}
        
        final IntentFilter filter = new IntentFilter();  
        filter.addAction(Intent.ACTION_SCREEN_OFF);  
        registerReceiver(offScreenReceiver, filter);  
	}
	
	private void initShowContent() {
		View outer = findViewById(R.id.outer);
        DialogTitleView headerVw = (DialogTitleView) findViewById(R.id.dialog_header);
        FrameLayout container = (FrameLayout) findViewById(R.id.content_container);
		View barDivider = findViewById(R.id.button_bar_divider);
        
		barDivider.setVisibility(View.VISIBLE);
		headerVw.setVisibility(View.VISIBLE);
		outer.setBackgroundResource(R.drawable.dialog_background);
		
		View tempview = LayoutInflater.from(this).inflate(R.layout.lock_screen_show_view, null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ScrollView.LayoutParams.MATCH_PARENT,
				ScrollView.LayoutParams.WRAP_CONTENT);

		container.removeAllViews();
		container.setPadding(15, 15, 15, 15);
		container.addView(tempview, lp);
		
		TempWindow[] temp = new TempWindow[9];
		
		temp[Constants.WINDOW_ID_1] = (TempWindow)tempview.findViewById(R.id.temp_win1);
		temp[Constants.WINDOW_ID_2] = (TempWindow)tempview.findViewById(R.id.temp_win2);
		temp[Constants.WINDOW_ID_3] = (TempWindow)tempview.findViewById(R.id.temp_win3);
		temp[Constants.WINDOW_ID_4] = (TempWindow)tempview.findViewById(R.id.temp_win4);
		temp[Constants.WINDOW_ID_5] = (TempWindow)tempview.findViewById(R.id.temp_win5);
		temp[Constants.WINDOW_ID_6] = (TempWindow)tempview.findViewById(R.id.temp_win6);
		temp[Constants.WINDOW_ID_7] = (TempWindow)tempview.findViewById(R.id.temp_win7);
		temp[Constants.WINDOW_ID_8] = (TempWindow)tempview.findViewById(R.id.temp_win8);
		temp[Constants.WINDOW_ID_9] = (TempWindow)tempview.findViewById(R.id.temp_win9);
		temp[Constants.WINDOW_ID_1].setWindowId(Constants.WINDOW_ID_1);
		temp[Constants.WINDOW_ID_2].setWindowId(Constants.WINDOW_ID_2);
		temp[Constants.WINDOW_ID_3].setWindowId(Constants.WINDOW_ID_3);
		temp[Constants.WINDOW_ID_4].setWindowId(Constants.WINDOW_ID_4);
		temp[Constants.WINDOW_ID_5].setWindowId(Constants.WINDOW_ID_5);
		temp[Constants.WINDOW_ID_6].setWindowId(Constants.WINDOW_ID_6);
		temp[Constants.WINDOW_ID_7].setWindowId(Constants.WINDOW_ID_7);
		temp[Constants.WINDOW_ID_8].setWindowId(Constants.WINDOW_ID_8);
		temp[Constants.WINDOW_ID_9].setWindowId(Constants.WINDOW_ID_9);
		
        Bundle bundle = this.getIntent().getExtras();
        String tempString = bundle.getString("msg");
        String padNum = bundle.getString("pad");
        String name = bundle.getString("name");
        
        PengInfo info = PengInfoDao.findByPhoneNum(padNum);

		int windowCount = info != null ? info.getWindowCount() : 3;
		TempBean bean = CommonTool.resolveTemp(tempString, info.getId(), System.currentTimeMillis());

		headerVw.titleTv.setText("收到新的来自" + name + "的温度信息");
		
		Iterator<TempTuple> it = bean.iterator();
		
		for(int i = 0; it.hasNext(); i++) {
			if(i < windowCount)
				temp[i].setVisibility(View.VISIBLE);
			else {
				if(windowCount <= 3)
					temp[i].setVisibility(i < 3 ? View.INVISIBLE : View.GONE);
				else if(windowCount <= 6) 
					temp[i].setVisibility(i < 6 ? View.INVISIBLE : View.GONE);
				else
					temp[i].setVisibility(View.INVISIBLE);
			}
			
			temp[i].setTemp(it.next());
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
		initShowContent();
	}

	@Override
	protected void onDestroy() {

		unregisterReceiver(offScreenReceiver);
		
		if(oldTimeOut >= 0)
			Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, oldTimeOut);
		
		super.onDestroy();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();

			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}
}
