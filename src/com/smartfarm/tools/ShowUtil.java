package com.smartfarm.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;

import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.view.R;
import com.smartfarm.view.SimpleBackActivity;
import com.smartfarm.view.SmartFarm;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ConfigManager;

public class ShowUtil {
	
	/**
	 * @param root
	 *            最外层布局，需要调整的布局
	 * @param scrollToView
	 *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
	 */
	public static void controlKeyboardLayout(final Activity activity, final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						// 获取root在窗体的可视区域
						root.getWindowVisibleDisplayFrame(rect);
						// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
						int rootInvisibleHeight = root.getRootView()
								.getHeight() - rect.bottom;
						// 若不可视区域高度大于100，则键盘显示
						if (rootInvisibleHeight > 100) {
							
							int[] location = new int[2];
							// 获取scrollToView在窗体的坐标
							scrollToView.getLocationInWindow(location);
							// 计算root滚动高度，使scrollToView在可见区域
							int intsrollHeight = (location[1]
									+ scrollToView.getHeight() + ShowUtil
										.dip2px(activity, 10))
									- rect.bottom;
							
							if(intsrollHeight > 0) {

								root.scrollTo(0, intsrollHeight);
							}
						} else {

							// 键盘隐藏
							root.scrollTo(0, 0);
						}
					}
				});
	}

	public static int dip2px(Context context, float dipValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * m + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / m + 0.5f);
	}
	
	/**
     * 调用系统安装了的应用分享
     * 
     * @param context
     * @param title
     * @param url
     */
    public static void showSystemShareOption(Activity context,
	    final String title, final String url) {
    	
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
		intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		context.startActivity(Intent.createChooser(intent, "选择分享"));
    }
    
	public static void openUrl(Context context, String url) {
		if(url != null && !url.equals("")) {
			
			Uri uri = Uri.parse(url);  
	        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}
	}
	
	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		// 去掉标题栏
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

	public static void LightScreen(Context context) {
		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		@SuppressWarnings("deprecation")
		final WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
		wakeLock.acquire();
		wakeLock.release();
		
	}
	
	// 保存到sdcard
	public static void takeScreenShotAndSave(Activity activity,
			String strFileName) {

		Bitmap b = takeScreenShot(activity);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (b != null)
				b.recycle();
		}
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}
	
	public static void showNotice(Context context, String title, String content, String url) {
		showNotice(context, title, content, url, "您有新消息!", 10087);
	}
	
	public static void showNotice(Context context, String title, String content, String url, String ticker, int id) {
		NotificationCompat.BigTextStyle textStyle = new BigTextStyle();
		textStyle
				.setBigContentTitle(title)
				.setSummaryText("上海洲涛")
				.bigText(content);
		Builder builder = new NotificationCompat.Builder(context)
				.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.text_message_h))
				.setSmallIcon(R.drawable.text_message_s)
				.setTicker(ticker)
				.setContentTitle(title)
				.setContentText(content)
				.setStyle(textStyle)
				.setAutoCancel(true);
		
		boolean hasSound = ConfigManager.getBoolean(context, ConfigManager.KEY_ENABLE_SOUND);
		boolean hasVibrate = ConfigManager.getBoolean(context, ConfigManager.KEY_ENABLE_VIBRATE);
		
		if(hasSound && hasVibrate)
			builder.setDefaults(Notification.DEFAULT_ALL);
		else if(hasSound)
			builder.setDefaults(Notification.DEFAULT_SOUND);
		else if(hasVibrate)
			builder.setDefaults(Notification.DEFAULT_VIBRATE);
		else
			builder.setDefaults(Notification.DEFAULT_LIGHTS);
		
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		if(url != null && !url.equals("")) {
			Uri uri = Uri.parse(url);  
	        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
	        
	        PendingIntent restartIntent = PendingIntent.getActivity(context, -1,
	        		intent, Intent.FLAG_ACTIVITY_NEW_TASK);
	        builder.setContentIntent(restartIntent);
		} else {
			Intent resultIntent = new Intent(context, SmartFarm.class);
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
	
			PendingIntent restartIntent = PendingIntent.getActivity(context, -1,
					resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
	
			builder.setContentIntent(restartIntent);
		}
		
		nm.notify(23456 + id, builder.build());
	}
	
	public static boolean showAlarm(Context context, String topic, String msg, int id) {
		try {
			Uri alarmSound = Uri.parse("android.resource://"
					+ context.getPackageName() + "/" + R.raw.alarm);

			NotificationCompat.BigTextStyle textStyle = new BigTextStyle();
			textStyle
					.setBigContentTitle(topic)
					.bigText(msg);
			Builder builder = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.attn)
					.setTicker(msg)
					.setContentTitle(topic)
					.setContentText(msg)
					.setStyle(textStyle)
					.setAutoCancel(true)
					.setSound(alarmSound);

			Intent resultIntent = new Intent(context, SimpleBackActivity.class);
			resultIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, BackPage.TEMP_MANAGER.getValue());
			Bundle bundle = new Bundle();
	        bundle.putInt(SimpleBackActivity.BUNDLE_KEY_ARGS, InfoRecord.RECORD_TYPE_ALARM);
	        resultIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, bundle);
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 关键的一步，设置启动模式

			PendingIntent restartIntent = PendingIntent.getActivity(context, -1,
					resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			builder.setContentIntent(restartIntent);
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Log.d("mmsg", " show alarm msg notice!");
			LightScreen(context);
			nm.notify(30086 + id, builder.build());
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			// importance:
			// The relative importance level that the system places
			// on this process.
			// May be one of IMPORTANCE_FOREGROUND, IMPORTANCE_VISIBLE,
			// IMPORTANCE_SERVICE, IMPORTANCE_BACKGROUND, or IMPORTANCE_EMPTY.
			// These constants are numbered so that "more important" values are
			// always smaller than "less important" values.
			// processName:
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(context.getPackageName())
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public static <T> String join(T[] array, String cement) {
		StringBuilder builder = new StringBuilder();

		if (array == null || array.length == 0) {
			return null;
		}
		for (T t : array) {
			builder.append(t).append(cement);
		}

		builder.delete(builder.length() - cement.length(), builder.length());

		return builder.toString();
	}

	public static boolean isNetworkEnabled(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	public static String getImei(Context context, String imei) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		return imei;
	}
	
	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics;
	}
	
	public static float dpToPixel(Context context, float dp) {
		return dp * (getDisplayMetrics(context).densityDpi / 160F);
	}
	
	public static boolean isShouldAlarm(PengInfo info) {
		try {
			Date nowdate = new Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(nowdate);
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(sdf.parse(info.getNightStart()));
			
			calendar.set(Calendar.HOUR_OF_DAY, startCalendar.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, startCalendar.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, startCalendar.get(Calendar.SECOND));
			Date startDate = calendar.getTime();
			
			if(startDate.getTime() > nowdate.getTime()) {
				calendar.setTime(nowdate);
				calendar.add(Calendar.DATE, 1);
				nowdate = calendar.getTime();
			}
			
			if (startDate.getTime() <= nowdate.getTime()) {

				calendar.setTime(startDate);
				startCalendar.setTime(sdf.parse(info.getNightEnd()));
				
				calendar.set(Calendar.HOUR_OF_DAY, startCalendar.get(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, startCalendar.get(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, startCalendar.get(Calendar.SECOND));
				Date endDate = calendar.getTime();

				if (endDate.getTime() <= startDate.getTime()) {
					calendar.add(Calendar.DATE, 1);
					endDate = calendar.getTime(); 
				}

				if (endDate.getTime() >= nowdate.getTime()) {
					return false;
				}
			}

			return true;

		} catch (Exception e) {
			
			e.printStackTrace();
			return true;
		}
	}
}
