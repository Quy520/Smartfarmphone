package com.smartfarm.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smartfarm.tools.ShowUtil;
import com.smartfarm.widget.DialogTitleView;

public class PushNoticeActivity extends Activity {
	
	BroadcastReceiver offScreenReceiver = new BroadcastReceiver() {  
        @Override  
        public void onReceive(final Context context, final Intent intent) {  
             
//        	PushNoticeActivity.this.finish();
        	
        }  
    };  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		 win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );
		
		setContentView(R.layout.push_msg_view);

        final IntentFilter filter = new IntentFilter();  
        filter.addAction(Intent.ACTION_SCREEN_OFF);  
        registerReceiver(offScreenReceiver, filter);  
        
		Bundle bundle = this.getIntent().getExtras();
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        final String url = bundle.getString("url");
        
        View outer = findViewById(R.id.outer);
        DialogTitleView headerVw = (DialogTitleView) findViewById(R.id.dialog_header);
        FrameLayout container = (FrameLayout) findViewById(R.id.content_container);
		View barDivider = findViewById(R.id.button_bar_divider);
		View buttonDivider = findViewById(R.id.button_divder);
		Button positiveBt = (Button) findViewById(R.id.positive_bt);
		Button negativeBt = (Button) findViewById(R.id.negative_bt);
        
		barDivider.setVisibility(View.VISIBLE);
		headerVw.setVisibility(View.VISIBLE);
		headerVw.titleTv.setText(title);
		outer.setBackgroundResource(R.drawable.dialog_background);
        
        ScrollView scrollView = new ScrollView(this);
		scrollView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		TextView tvMessage = new TextView(this, null,
				R.style.dialog_pinterest_text);
		tvMessage.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		tvMessage.setPadding(15, 15, 15, 15);
		tvMessage.setLineSpacing(0.0F, 1.3F);
		tvMessage.setMaxLines(12);
		tvMessage.setText(Html.fromHtml(content));
		tvMessage.setTextColor(this.getResources().getColor(
				R.color.black));

		ScrollView.LayoutParams lp = new ScrollView.LayoutParams(
				ScrollView.LayoutParams.MATCH_PARENT,
				ScrollView.LayoutParams.WRAP_CONTENT);
		scrollView.addView(tvMessage, lp);

		container.removeAllViews();
		container.setPadding(15, 15, 15, 15);
		container.addView(scrollView, lp);
		
		negativeBt.setVisibility(View.VISIBLE);
		negativeBt.setText("关闭");
		positiveBt.setText("前往");
		
        if(ShowUtil.isEmpty(url)) {
        	positiveBt.setVisibility(View.GONE);
        	buttonDivider.setVisibility(View.GONE);
        } else {
        	positiveBt.setVisibility(View.VISIBLE);
        	buttonDivider.setVisibility(View.VISIBLE);
        	positiveBt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it = new Intent();
					it.setClass(PushNoticeActivity.this, TemporaryActivity.class);
					it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					it.putExtras(bundle);
					PushNoticeActivity.this.startActivity(it);
					PushNoticeActivity.this.finish();
				}
			});
        }
		
        negativeBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				PushNoticeActivity.this.finish();
				
			}
		});
	}
	
	@Override
	protected void onDestroy() {

		unregisterReceiver(offScreenReceiver);
		
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
