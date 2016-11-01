package com.smartfarm.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.TextView;

import com.qihoo.updatesdk.lib.UpdateHelper;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.fragment.NavigationDrawerFragment;
import com.smartfarm.msg.ImHelperInterface;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.util.NetCheckWork;
import com.smartfarm.view.util.SystemBarTintManager;
import com.smartfarm.widget.PagerSlidingTabStrip;
import com.smartfarm.widget.PagerSlidingTabStrip.OnClickTabListener;
import com.smartfarm.widget.dialog.CommonDialog;
/**
 * 主页面
 * @author QSD
 *
 */

@SuppressLint("InflateParams")
public class SmartFarm extends ActionBarActivity implements OnClickListener,
		NavigationDrawerFragment.NavigationDrawerCallbacks, EventHandler {
	
	private View noticeView;
	private TextView noticeText;
	private PagerSlidingTabStrip mTabStrip;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private Fragment stateFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(Constants.TAG, " Main activity onCreate");
		setTheme(AppContext.context().getAppTheme());

		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
//	           setTranslucentStatus(true);    
	           SystemBarTintManager tintManager = new SystemBarTintManager(this);    
	           tintManager.setStatusBarTintEnabled(true);    
	           tintManager.setStatusBarTintResource(AppContext.context().isNightMode() 
	        		   ? R.color.night_actionbar_background : R.color.day_actionbar_background);//通知栏所需颜色  
	    } 
		
		setContentView(R.layout.main);
		
		AppManager.getAppManager().addActivity(this);
		
		stateFragment = getSupportFragmentManager().findFragmentById(R.id.main_temp_fragment);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		Intent serviceIntent = new Intent();
		serviceIntent.setClass(this, MsgService.class);
		startService(serviceIntent);
		bindService(serviceIntent, serviceConn, Context.BIND_NOT_FOREGROUND);

		initWidget();
		UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));

		AppContext.context().setRuning(true);
		
		EventBus.getDefault().add(this);
		
		ProtocolFactory.GetTestSelfProtocol().send();
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

    	stateFragment.onCreateOptionsMenu(menu, getMenuInflater());
    	
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		stateFragment.onOptionsItemSelected(item);
		
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	protected void onDestroy() {

		Log.d(Constants.TAG, " Main activity onDestroy");

		AppContext.context().setRuning(false);

		unbindService(serviceConn);
		EventBus.getDefault().remove(this);

		super.onDestroy();
	}

	private void initWidget() {
		noticeView = findViewById(R.id.main_notice);

		noticeText = (TextView) findViewById(R.id.empty_notice_text);

		findViewById(R.id.empty_notice_close).setOnClickListener(this);
		noticeText.setOnClickListener(this);

		mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tabstrip);

		recreateTitleTab();
	}
	
	public void recreateTitleTab() {
		List<PengInfo> allPeng = PengInfoDao.findAll();

		if (allPeng.size() < 2)
			mTabStrip.setVisibility(View.GONE);
		else
			mTabStrip.setVisibility(View.VISIBLE);

		mTabStrip.removeAllTab();
		for (PengInfo info : allPeng) {
			View v = LayoutInflater.from(this).inflate(
					R.layout.base_viewpage_fragment_tab_item, null, false);
			TextView title = (TextView) v.findViewById(R.id.tab_title);
			title.setText(info.getName());
			v.setContentDescription("" + info.getId());

			mTabStrip.addTab(v);
		}	
		
		mTabStrip.setOnClickTabListener(new OnClickTabListener() {

			@Override
			public void onClickTab(View tab, int index) {

				int id = Integer.valueOf(tab.getContentDescription().toString());

				AppContext.context().setCurrPengInfo(id);
				
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_PENG_SWITCHING));
			}
		});

		mTabStrip.setSelectedById(AppContext.context().getCurrPengInfo().getId());
	}
	
	public void showNoticeView(String text) {
		if (noticeView.getVisibility() != View.VISIBLE) {
			noticeText.setText(text);
			noticeView.setVisibility(View.VISIBLE);
		}
	}

	public void hideNoticeView() {
		if (noticeView.getVisibility() == View.VISIBLE) {
			noticeView.setVisibility(View.GONE);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			CommonTool.toHome(this);
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {

		noticeView.setVisibility(View.GONE);

		if (v.getId() == R.id.empty_notice_text) {

			NetCheckWork.showCheckDialog(this);
		}
	}

	private ServiceConnection serviceConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

		}
	};

	@Override
	public void onNavigationDrawerItemSelected(int position) {

	}

	@Override
	public void onEvent(LocalEvent event) {
		
		if(event.getEventType() == LocalEvent.EVENT_TYPE_TEST_ERROR) {
			
			showNoticeView(event.getEventMsg());
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_NET_DOWN) {
			
			String errorText = "";
			
			if(event.getEventValue() == ImHelperInterface.CONN_RES_CONFLTCT)
				errorText = "帐号异常，帐号在其他地方登录！";
			else if(event.getEventValue() == ImHelperInterface.CONN_RES_EXIT)
				errorText = "帐号已离线！";
			else
				errorText = "网络异常，点击诊断网络故障！";
			
			showNoticeView(errorText);
			
			if(event.getEventValue() == ImHelperInterface.CONN_RES_CONFLTCT) {
				ShowUtil.showNotice(AppContext.context(), "帐号提示", "您的手机号码在异地登录,如需重连请点击连接状态下的重新连接按钮，"
						+ "请尽量不要让其他人使用自己的手机号码控制，避免发生其他的安全性问题。", "");
				
				try {
					
					CommonDialog dialog = new CommonDialog(this);
					dialog.setTitle("帐号提示");
					dialog.setMessage("您的手机号码在异地登录,如需重连请点击连接状态下的重新连接按钮，"
							+ "请尽量不要让其他人使用自己的手机号码控制，避免发生其他的安全性问题。");
					dialog.setPositiveButton("确定", null);
					dialog.show();
					
				} catch(BadTokenException e) {
					
					Log.e(Constants.TAG, " show account dialog error! ");
				}
					
				AppContext.context().getAccountManager().exit();
			}
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_NET_OK) {
			
			hideNoticeView();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_PENG_CHANGE) {
			
			recreateTitleTab();
		}
	}
}
