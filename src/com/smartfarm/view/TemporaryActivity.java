package com.smartfarm.view;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

public class TemporaryActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON 
				 | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		
		Bundle bundle = this.getIntent().getExtras();
        final String url = bundle.getString("url");
        
		Uri uri = Uri.parse(url);  
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				TemporaryActivity.this.startActivity(intent);
				TemporaryActivity.this.finish();
			}
		}, 200);
	}
}
