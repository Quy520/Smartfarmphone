package com.smartfarm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ConfigManager;
import com.smartfarm.view.util.UIHelper;

public class GuildActivity extends Activity implements OnPageChangeListener, OnClickListener {

	private int guildCount = 7;
	private ImageView[] cursors;
	private ViewPager mViewPager;
	private int cursorIndex = 0;
	private LinearLayout cursorContainer;
	private Button okBtn;
	private CheckBox protocolBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setTheme(AppContext.context().getAppTheme());
		
		super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.guild_view);
        
        initView();
        initData();
	}

	public void initView() {
        
		okBtn = (Button) findViewById(R.id.guild_ok);
		protocolBtn = (CheckBox) findViewById(R.id.guild_protocol);
		mViewPager = (ViewPager) findViewById(R.id.guild_viewpager);
		cursorContainer = (LinearLayout) findViewById(R.id.guild_cursor_container);
		
		cursors = new ImageView[guildCount];
		
		for(int i = 0; i < cursors.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.MATRIX);
			imageView.setImageResource(R.drawable.guild_cursor_selector);
			imageView.setSelected(false);
			imageView.setPadding(10, 0, 10, 0);
			
			cursors[i] = imageView;
			cursorContainer.addView(imageView);
		}

		okBtn.setOnClickListener(this);
		protocolBtn.setOnClickListener(this);
		protocolBtn.setText(Html.fromHtml("<u>《上海洲涛智能大棚最终用户协议》</u>" ));
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setAdapter(new GuildViewPagerAdapter());
		
	}

	public void initData() {

		cursors[cursorIndex].setSelected(true);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int arg0) {
		
		cursorIndex = arg0;
		 
		for(int i = 0; i < cursors.length; i++) {
			
			if(i == cursorIndex)
				cursors[i].setSelected(true);
			else
				cursors[i].setSelected(false);
		}
		
		if(cursorIndex >= guildCount - 1) {
			
			cursorContainer.setVisibility(View.GONE);
			okBtn.setVisibility(View.VISIBLE);
			protocolBtn.setVisibility(View.VISIBLE);
		} else {

			cursorContainer.setVisibility(View.VISIBLE);
			okBtn.setVisibility(View.GONE);
			protocolBtn.setVisibility(View.GONE);
		}
	}
	
	class GuildViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return guildCount;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {}

		@Override
		public Object instantiateItem(View container, int position) {
			
			ImageView imageView = new ImageView(GuildActivity.this);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setSelected(false);
			
//			x.image().bind(imageView, "assets://step" + (position + 1) + ".png");
			ImageLoader.getInstance().displayImage("assets://step" + (position + 1) + ".png", imageView);
			
			((ViewPager)container).addView(imageView);

			return imageView;
		}
	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.guild_ok) {
			
			if(!protocolBtn.isChecked()) {
				
				ToastTool.showToast("请先同意用户协议");
				return;
			}
			
			Intent intent = new Intent(this, SmartFarm.class);
	        startActivity(intent);
	        ConfigManager.putVersion(this, Constants.CURR_VERSION);
	        finish();
		} else {
			
			if(protocolBtn.isChecked()) {

	        	UIHelper.showSimpleBack(GuildActivity.this, BackPage.USER_PROTOCPL);
				return;
			}
		}
	}
}
