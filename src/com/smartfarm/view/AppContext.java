package com.smartfarm.view;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.igexin.sdk.PushManager;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.LightInfo;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.bean.User;
import com.smartfarm.db.bean.WaterInfo;
import com.smartfarm.db.util.LightInfoDao;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.db.util.UserDao;
import com.smartfarm.db.util.WaterInfoDao;
import com.smartfarm.msg.MsgHelper;
import com.smartfarm.msg.Protocol;
import com.smartfarm.msg.ResolveMsg;
import com.smartfarm.net.ApiHttpClient;
import com.smartfarm.net.UploadService;
import com.smartfarm.tools.AccountManager;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.DeviceIdHelper;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ExceptionUtils;
import com.smartfarm.tools.HelperThread;
import com.smartfarm.tools.LocalDataCollection;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.util.ConfigManager;
import com.smartfarm.view.util.NetManager;
import com.tencent.bugly.crashreport.CrashReport;

public class AppContext extends Application implements EventHandler {

	private static AppContext instance;

	protected boolean isNeedCaughtExeption = false;// 是否捕获未知异常
	protected boolean needRestart = false;
	private PendingIntent restartIntent;
	private MyUncaughtExceptionHandler uncaughtExceptionHandler;
	private boolean showWelcome = true;
	private PengInfo currPengInfo;
	private boolean nightMode = false;
	private boolean runing = false;
	private String imei = "";
	private String mac = "";
	private String phone = "";
	private BDLocation currLocation;
	private AccountManager accountManager;
	private LightInfo lightInfo;
	private WaterInfo waterInfo;
	private long msgTime;
	private long voiceTime;

	private User currUser;

	public String getNoteDraft() {
		return ConfigManager.getString(instance,
				"note_draft_" + currUser.getId());
	}

	public void setNoteDraft(String draft) {
		ConfigManager.putString(instance, "note_draft_" + currUser.getId(),
				draft);
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public long getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(long msgTime) {
		this.msgTime = msgTime;
	}

	public long getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(long voiceTime) {
		this.voiceTime = voiceTime;
	}

	public String getCurrLocation() {

		if (currLocation == null)
			return "";

		return currLocation.getAddrStr();
	}

	public String getCityCode() {

		if (currLocation == null)
			return "";

		return currLocation.getCityCode();
	}

	public LightInfo getLightInfo() {

		if (lightInfo == null || lightInfo.getPengId() != currPengInfo.getId())
			lightInfo = LightInfoDao.findByPengId(currPengInfo.getId());

		return lightInfo;
	}

	public void setLightInfo(LightInfo lightInfo) {
		this.lightInfo = lightInfo;
	}

	public WaterInfo getWaterInfo() {

		if (waterInfo == null || waterInfo.getPengId() != currPengInfo.getId())
			waterInfo = WaterInfoDao.findByPengId(currPengInfo.getId());

		return waterInfo;
	}

	public void setWaterInfo(WaterInfo waterInfo) {
		this.waterInfo = waterInfo;
	}

	public void setCurrLocation(BDLocation currLocation) {
		this.currLocation = currLocation;
	}

	public static AppContext context() {
		return instance;
	}

	public static Resources resources() {
		return instance.getResources();
	}

	public void setUser(User user) {
		currUser = user;
	}

	public User getUser() {
		return currUser;
	}

	public boolean isLogin() {
		return currUser != null;
	}

	public String getImei() {
		return imei;
	}

	public void setNightMode(boolean isNight) {

		nightMode = isNight;
	}

	public boolean isNightMode() {

		return nightMode;
	}

	public int getAppTheme() {

		return nightMode ? R.style.AppTheme_night : R.style.AppTheme_day;
	}

	public AccountManager getAccountManager() {

		return accountManager;
	}

	public void setAccountManager(AccountManager accountManager) {

		this.accountManager = accountManager;
	}

	public boolean isRuning() {
		return runing;
	}

	public void setRuning(boolean runing) {
		this.runing = runing;
	}

	public PengInfo getCurrPengInfo() {

		if (currPengInfo == null)
			refreshPengInfo();

		return currPengInfo;
	}

	public void setCurrPengInfo(PengInfo currPengInfo) {

		if (currPengInfo != null) {

			this.currPengInfo = currPengInfo;
			setNewMode(!ShowUtil.isShouldAlarm(currPengInfo));
		}
	}

	public void setCurrPengInfo(int pengId) {

		PengInfo newInfo = PengInfoDao.findById(pengId);
		setCurrPengInfo(newInfo);
	}

	public void refreshPengInfo() {

		PengInfo newInfo = PengInfoDao.findById(currPengInfo.getId());
		setCurrPengInfo(newInfo);
	}

	public void checkMode() {

		setNewMode(!ShowUtil.isShouldAlarm(currPengInfo));
	}

	public void setNewMode(boolean mode) {

		if (mode != nightMode) {

			nightMode = mode;
			Activity main = AppManager.getActivity(SmartFarm.class);

			if (main != null)
				main.recreate();
		}
	}

	public boolean isShowWelcome() {
		return showWelcome;
	}

	public void setShowWelcome(boolean show) {
		showWelcome = show;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();

		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);

		Intent intent = new Intent(getApplicationContext(), SmartFarm.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		restartIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回
		if (processAppName == null
				|| !processAppName.equalsIgnoreCase("com.smartfarm.view")) {
			Log.e(Constants.TAG, "enter the service process!");
			return;
		}

		DeviceIdHelper.init();

		CrashReport.initCrashReport(this, "900012724", false);
		CrashReport.setUserId(currUser != null ? currUser.getPhone()
				: "default");
		EventBus.getDefault().add(this);
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));

		try {

			PengInfoDao.findAll();
		} catch (SQLiteException e) {

			ExceptionUtils.report(e);
			ToastTool.showToast("数据库初始化失败，尝试重启程序，若多次启动失败建议卸载后重新安装软件！");

			int count = ConfigManager.getInstance().getInt("restart_count", 0);

			if (count < 3) {

				ConfigManager.getInstance().putInt("restart_count", ++count);
				AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
						restartIntent);
			}

			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}

		ConfigManager.getInstance().putInt("restart_count", 0);
		accountManager = new AccountManager();
		if (accountManager.isLogined()) {

			User user = UserDao.findById(accountManager.getUserId());
			setUser(user);
		}

		HelperThread.getInstance();
		NetManager.getInstence();
		MsgHelper.init();

		PushManager.getInstance().initialize(this);
		ApiHttpClient.setHttpClient(new AsyncHttpClient());
		if (currPengInfo == null) {
			currPengInfo = PengInfoDao.findAll().get(0);
		}

		if (isNeedCaughtExeption) {
			cauchException();
		}

		nightMode = !ShowUtil.isShouldAlarm(currPengInfo);

		Intent upload = new Intent(AppContext.context(), UploadService.class);
		startService(upload);
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		Iterator<?> i = am.getRunningAppProcesses().iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					pm.getApplicationLabel(pm.getApplicationInfo(
							info.processName, PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	private void getExceptionString(Throwable ex, StringBuilder error) {
		if (ex.getCause() != null)
			getExceptionString(ex, error);
		else {
			for (StackTraceElement elem : ex.getStackTrace()) {
				error.append(elem.toString());
				error.append("\r\n");
			}
		}
	}

	// 创建服务用于捕获崩溃异常
	private class MyUncaughtExceptionHandler implements
			UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {

			StringBuilder error = new StringBuilder();
			getExceptionString(ex, error);

			LocalDataCollection.getInstance().addErrorLog(error.toString());

			if (needRestart) {
				AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
						restartIntent);
			}

			AppManager.getAppManager().AppExit(getApplicationContext());
		}
	};

	public void reStartApp() {
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
				restartIntent);

		AppManager.getAppManager().AppExit(getApplicationContext());
	}

	// -------------------异常捕获-----捕获异常后重启系统-----------------//

	private void cauchException() {

		// 程序崩溃时触发线程
		uncaughtExceptionHandler = new MyUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
	}

	@Override
	public void onEvent(LocalEvent event) {

		if (event.getEventType() == LocalEvent.EVENT_TYPE_MODE_CHANGE) {

			PengInfo info = AppContext.context().getCurrPengInfo();
			info.setAutoMode(event.getEventMsg().contains("auto"));
			info.setOpenAll(false);
			info.setCloseAll(false);
			PengInfoDao.update(info);
		}else if (event.getEventType() == LocalEvent.EVENT_TYPE_RECEIVE_MSG) {

			ResolveMsg.resolve((Protocol) event.getEventData());
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_IS_RAIN) {
			boolean isOpen = event.getEventMsg().contains("rain");
			PengInfo info = AppContext.context().getCurrPengInfo();
			info.setCloseAll(isOpen);
			info.setOpenAll(false);
			info.setAutoMode(!isOpen);
			info.setHighAuto(!isOpen);
			PengInfoDao.update(info);
			
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_OPEN_WINDOWS) {
			PengInfo info = AppContext.context().getCurrPengInfo();
			if(event.getEventMsg().contains("wind") || event.getEventMsg().contains("stop")){
				boolean isOpen = event.getEventMsg().contains("wind");
				info.setOpenAll(isOpen);
				info.setCloseAll(false);
				if(isOpen)
					info.setAutoMode(false);
				info.setHighAuto(!isOpen);
				PengInfoDao.update(info);
			}
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_OPEN_WINDS_END){
			PengInfo info = AppContext.context().getCurrPengInfo();
			if(event.getEventMsg().contains("stop")){
				info.setOpenAll(false);
			}
		}
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}
}
