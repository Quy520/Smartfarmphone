package com.smartfarm.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.smartfarm.bean.TempBean;
import com.smartfarm.bean.TempTuple;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.msg.Protocol;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.StringUtils;

public class CommonTool {

    // 手机网络类型
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static void copyTextToBoard(String string) {
    	if (TextUtils.isEmpty(string))
    		return;
    	ClipboardManager clip = (ClipboardManager) AppContext.context()
    			.getSystemService(Context.CLIPBOARD_SERVICE);
    	clip.setPrimaryClip(ClipData.newPlainText("smartfarm", string));
    	ToastTool.showToast(R.string.copy_success);
    }
    
	public static String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);

			// 执行命令cmd，只取结果中含有filter的这一行
			while ((line = br.readLine()) != null
					&& line.contains(filter) == false) {
				// result += line;
				Log.i("test", "line: " + line);
			}

			result = line;
			Log.i("test", "result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void HideKb(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && v != null) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

    public static void showSoftKeyboard(View view) {
    	((InputMethodManager) AppContext.context().getSystemService(
    			Context.INPUT_METHOD_SERVICE)).showSoftInput(view,
    					InputMethodManager.SHOW_FORCED);
    }

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) AppContext
				.context().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	public static boolean isWifiOpen() {
		boolean isWifiConnect = false;
		ConnectivityManager cm = (ConnectivityManager) AppContext.context()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
		for (int i = 0; i < networkInfos.length; i++) {
			if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
				if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
					isWifiConnect = false;
				}
				if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
					isWifiConnect = true;
				}
			}
		}
		return isWifiConnect;
	}

	public static void openAppInMarket(Context context) {
		if (context != null) {
			String pckName = context.getPackageName();
			try {
				gotoMarket(context, pckName);
			} catch (Exception ex) {
				try {
					String otherMarketUri = "http://market.android.com/details?id="
							+ pckName;
					Intent intent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(otherMarketUri));
					context.startActivity(intent);
				} catch (Exception e) {

				}
			}
		}
	}

	public static void gotoMarket(Context context, String pck) {
		if (!isHaveMarket(context)) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + pck));
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(intent);
		}
	}

	public static boolean isHaveMarket(Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.APP_MARKET");
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
		return infos.size() > 0;
	}

	public static void toHome(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
	}

	public static void ShowExceptionStack(Context context, Exception e) {
		StringBuilder result = new StringBuilder();
		for (StackTraceElement elem : e.getStackTrace()) {
			result.append(elem);
			result.append("\n");
		}
		new AlertDialog.Builder(context).setTitle("error").setMessage(result)
				.setNegativeButton("ok", null).show();
	}

	public static boolean checkMobile(String mobile) {
		String telRegex = "[1]\\d{10}";
		if (TextUtils.isEmpty(mobile))
			return false;
		else
			return mobile.matches(telRegex);
	};

	public static boolean checkTemper(String temper) {

		String telRegex = "\\d{2}[.]\\d{1}[C]";
		if (TextUtils.isEmpty(temper))
			return false;
		else
			return temper.matches(telRegex);
	};

	public static boolean isConnected() {
		try {
			// ping baidu!
			Process p = Runtime.getRuntime().exec(
					"/system/bin/ping -c " + 1 + " 202.108.22.5");
			int status = p.waitFor();// 只有0时表示正常返回。
			return status == 0;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isNetworkConnected() {

		// 判断网络是否连接
		ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContext
				.context().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}

		return false;
	}

	/**
	 * 直接调用短信接口发短信，不含发送报告和接受报告
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static void SendSMS(String phoneNumber, String message) {
		try {
			// 获取短信管理器
			android.telephony.SmsManager smsManager = android.telephony.SmsManager
					.getDefault();
			// 拆分短信内容（手机短信长度限制）
			List<String> divideContents = smsManager.divideMessage(message);
			
			if(divideContents != null)
				for (String text : divideContents) {
					smsManager.sendTextMessage(phoneNumber, null, text, null, null);
				}
			else
				ToastTool.showToast("短信发送失败！");
		} catch (IllegalArgumentException e) {
			ToastTool.showToast("设置手机号码有误或未设置，无法发送信息。");
		}
	}

	public static void delay(int ms) {
		try {
			Thread.currentThread();
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String getCurrentDate() {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd",
				Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());

		return format.format(date);
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static String getCmdType(int cmdType) {

		switch (cmdType) {
		case Constants.MOTOR_CONTROL_TYPE_OPEN:

			return "打开";
		case Constants.MOTOR_CONTROL_TYPE_CLOSE:

			return "关闭";
		case Constants.MOTOR_CONTROL_TYPE_STOP:

			return "停止";
		default:
			return "";
		}
	}

	public static String resolveControlRes(int cmdType, int[] windows,
			String data) {

		StringBuilder result = new StringBuilder();

		if (!data.contains(";"))
			return "";

		String[] res = data.split(";");

		boolean isAll = windows == null;

		if (!isAll && windows.length != res.length)
			return "消息异常！";

		for (int i = 0; i < res.length; i++) {

			if (res[i].equals(""))
				continue;

			result.append("风口");
			result.append(isAll ? i + 1 : windows[i] + 1);
			result.append(getCmdType(cmdType));

			int controlRes = 0;

			try {
				controlRes = Integer.valueOf(res[i]);
			} catch (Exception e) {

				// res code error, ingore this code
				continue;
			}

			int scuessRes = controlRes % 1000;

			if (scuessRes == 1) {

				result.append("成功!\n");
			} else {

				result.append("失败!\n");

				if (scuessRes == 101)
					result.append("原因:驱动异常!\n");
				else if (scuessRes == 102)
					result.append("原因:电机正在工作!\n");
				else if (scuessRes == 103)
					result.append("原因:链路异常!\n");
			}

			if (controlRes / 1000 == 1)
				result.append("该窗口已开到最大！\n");
			if (controlRes / 1000 == 2)
				result.append("该窗口已关到最小！\n");
		}

		result.replace(result.lastIndexOf("\n"), result.length(), "");
		return result.toString();
	}

	public static String GetResponseString(Protocol protocol) {

		switch (protocol.getCmdType()) {
		case Constants.MOTOR_CONTROL_TYPE_CLOSE:

			if (protocol.getData().contains("error")) {
				if (protocol.getWindowId().contains("all"))
					return "正处于通风打开风口阶段，请等待电机停止后再进行操作!";
				else
					return "正处于通风时间，无法关闭风口!";
			} else {
				if (protocol.getWindowId().contains("all")) {
					if (protocol.getData().contains("end"))
						return "已结束通风模式，正在关闭风口!";
					else {
						return resolveControlRes(protocol.getCmdType(), null,
								protocol.getData());
					}
				} else {
					return resolveControlRes(protocol.getCmdType(),
							protocol.getWindowIds(), protocol.getData());
				}
			}

		case Constants.MOTOR_CONTROL_TYPE_OPEN:
			if (protocol.getData().contains("error"))
				return "正处于通风时间，无法打开窗口!";
			else if (protocol.getWindowId().contains("ven"))
				return "大棚开始通风!";
			else
				return resolveControlRes(protocol.getCmdType(),
						protocol.getWindowIds(), protocol.getData());

		case Constants.MOTOR_CONTROL_TYPE_STOP:
			if (protocol.getData().contains("error"))
				return "正处于通风时间，无法停止窗口!";
			else
				return resolveControlRes(protocol.getCmdType(),
						protocol.getWindowIds(), protocol.getData());

		case Constants.MOTOR_CONTROL_TYPE_READ:

			return "读取温度成功!";

		case Constants.MOTOR_CONTROL_TYPE_MODE:
			if (protocol.getData().contains("error"))
				return "正处于通风时间，无法变更模式!";
			else
				return "模式变更成功!";
			
		case Constants.MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE:
			if (protocol.getData().contains("error"))
				return "放晨风已启动失败";
			else
				return "放晨风已启动!";
			
		case Constants.MOTOR_CONTROL_TYPE_OPEN_WIND_END:
			if (protocol.getData().contains("error"))
				return "放晨风结束失败";
			else
				return "放晨风结束!";	
		case Constants.MOTOR_CONTROL_TYPE_RAIN_MODE:
			if (protocol.getData().contains("error"))
				return "下雨关风已启动失败";
			else
				return "下雨关风已启动!";	
			
		case Constants.MOTOR_CONTROL_TYPE_LIGHT_CONFIG:
		case Constants.MOTOR_CONTROL_TYPE_WATER_CONFIG:
		case Constants.MOTOR_CONTROL_TYPE_SYN:

			if(protocol.getWindowId().contains("get"))
				return "参数读取成功!";
			else
				return "参数同步成功!";

		case Constants.MOTOR_CONTROL_TYPE_ALARM:

			return "收到报警!";

		case Constants.MOTOR_CONTROL_TYPE_PWD:

			return "温控机密码错误!";

		case Constants.MOTOR_CONTROL_TYPE_NOTICE:

			if (protocol.getData().contains("close"))
				return "重启温控机成功!";
			else
				return "执行成功!";

		case Constants.MOTOR_CONTROL_TYPE_WATER_OPEN:
		case Constants.MOTOR_CONTROL_TYPE_LIGHT_OPEN:

			if(protocol.getData().equals("no"))
				return "请先打开电磁阀再打开水泵!";
			
			return "打开成功";

		case Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE:
		case Constants.MOTOR_CONTROL_TYPE_LIGHT_CLOSE:

			return "关闭成功";

		default:
			return "";
		}
	}

	public static String getLastUpdateTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(
				"上次更新时间:yyyy/MM/dd-HH:mm:ss", Locale.getDefault());

		return format.format(date);
	}

	public static String getDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss",
				Locale.getDefault());

		return format.format(date);
	}

	public static String getDateString2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());

		return format.format(date);
	}

	public static float[] ResolveTemp(String msg) {
		String[] data = msg.split("T");
		float[] temps = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int index = 0;

		for (int i = 1; i < data.length; i++) {
			try {
				temps[index] = Float.valueOf(data[i].substring(
						data[i].indexOf("=") + 1, data[i].indexOf(";")));
			} catch (NumberFormatException e) {
			}
			index++;
		}

		return temps;
	}

	public static String getTempString(String tempData) {
		StringBuilder result = new StringBuilder();
		String[] data = tempData.split("T");

		for (int i = 1; i < data.length; i++) {

			result.append("风口");
			result.append(i);
			result.append(":");
			result.append(data[i].substring(data[i].indexOf("=") + 1,
					data[i].indexOf(":")));
			result.append("℃,已打开:");
			result.append(data[i].substring(data[i].indexOf(":") + 1,
					data[i].indexOf(";")));
			result.append("厘米;    ");
		}

		return result.toString();
	}

    public static String getLightString(String lightData) {
    	
        StringBuilder result = new StringBuilder();
        String[] data = lightData.split(";");
        int turnOnNum = 0;
        int turnOffNum = 0;
        int lightNum = data.length - 2;
        int count = AppContext.context().getLightInfo().getCount();
        
        for (int i = 0; i < count; i++) {
            if (Boolean.parseBoolean(data[i].split("=")[1])) turnOnNum++;
            else turnOffNum++;
        }
        result.append(turnOnNum + "个补光灯打开," + turnOffNum + "个补光灯关闭");
        result.append("。当前照度为 " + data[lightNum].split("=")[1] + " lux,");
        result.append("处于" + (Boolean.parseBoolean(data[lightNum + 1].split("=")[1]) ? "自动模式" : "手动模式"));
        return result.toString();
    }

    public static String getWaterString(String waterData) {
    	
        StringBuilder result = new StringBuilder();
        String[] data = waterData.split(";");
        int turnOnNum = 0;
        int turnOffNum = 0;
        int waterNum = data.length - 4;
        int count = AppContext.context().getWaterInfo().getWaterCount();
        
        for (int i = 0; i < count; i++) {
            if (Boolean.parseBoolean(data[i].substring(data[i].indexOf(":") + 1, data[i].indexOf("&"))))
                turnOnNum++;
            else 
            	turnOffNum++;
        }
        
        result.append(turnOnNum + "个水泵打开," + turnOffNum + "个水泵关闭");
        result.append("。当前土壤温度为 " + (int)Float.parseFloat(data[0].substring(data[0].indexOf("&") + 1, data[0].length())) + "°C,");
        result.append("。土壤湿度为 " + (int)Float.parseFloat(data[0].substring(data[0].indexOf("=") + 1, data[0].indexOf(":"))) + "%,");
        result.append("当前处于" + (data[waterNum + 1].split("=")[1].equals("no")? "手动模式" : "自动模式"));
        
        return result.toString();
    }

	public static String getTempString(Context context, String padNum, String data) {
		StringBuilder result = new StringBuilder();

		PengInfo info = PengInfoDao.findByPhoneNum(padNum);
		int len = info != null ? info.getWindowCount() : 3;
		TempBean bean = resolveTemp(data, info.getId(),
				System.currentTimeMillis());
		Iterator<TempTuple> it = bean.iterator();

		for (int i = 0; it.hasNext(); i++) {
			TempTuple tuple = it.next();

			if (i >= len)
				continue;

			result.append("风口");
			result.append(i + 1);
			result.append(":");
			result.append(tuple.temp);
			result.append("℃,已打开:");
			result.append(tuple.state);
			result.append("厘米;\n");
		}

		return result.toString();
	}

	public static TempBean resolveTemp(String tempData, int id, long time) {

		int[] temp = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int[] state = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		if (tempData != null) {
			String[] data = tempData.split("T");
			int index = 0;

			for (int i = 1; i < data.length; i++) {
				try {
					temp[index] = Integer.valueOf(data[i].substring(
							data[i].indexOf("=") + 1, data[i].indexOf(":")));

					state[index] = Integer.valueOf(data[i].substring(
							data[i].indexOf(":") + 1, data[i].indexOf(";")));

				} catch (NumberFormatException e) {

					Log.w(Constants.TAG,
							"can't resolve temp value and ignore. ");
				} catch (StringIndexOutOfBoundsException e) {

					Log.w(Constants.TAG, "temp msg has error. ");
				}

				index++;
			}
		}

		return new TempBean(temp, state, id, time, tempData);
	}
}
