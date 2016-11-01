package com.smartfarm.view.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.smartfarm.view.AppContext;

public class ConfigManager {
	
	public static final String KEY_VERSION = "curr_version";
	public static final String KEY_SHOW_WELCOME = "show_welcome";
	public static final String KEY_ENABLE_SOUND = "msg_sound";
	public static final String KEY_ENABLE_VIBRATE = "msg_vibrate";
	public static final String KEY_ENABLE_WATER = "water_enable";
	public static final String KEY_ENABLE_LIGHT = "light_enable";
	public static final String KEY_SEND_WAY = "default_send_way";
	public static final String KEY_INIT_FLAG = "initialFlag";
	public static final String KEY_LAST_UPLOAD = "lastUpload";
	public static final String KEY_LOAD_IMG = "load_img";
	
	private static final String CONFIG_NAME = "config_file";
	
	public static final int SEND_BY_HUANXIN = 102;
	public static final int SEND_BY_RONGYUN = 102;
	
	private static ConfigManager cfgManager;
	private SharedPreferences configurationInfo;
	private Editor save;
	
	public static ConfigManager getInstance() {
		
		if(cfgManager == null) {
			cfgManager = new ConfigManager(AppContext.context());
		}
		
		return cfgManager;
	}
	
	public static String getVersion(Context context) {
		
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		
		return sp.getString(KEY_VERSION, "null");
	}
	
	public static void putVersion(Context context, String version) {
		
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		
		Editor editor = sp.edit();
		editor.putString(KEY_VERSION, version);
		editor.commit();
	}
	
	public static void putString(Context context, String key, String value) {
		
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		
		return sp.getBoolean(key, true);
	}

	public static String getString(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		
		return sp.getString(key, "");
	}

	public static boolean getBoolean(Context context, String key, boolean defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		
		return sp.getBoolean(key, defaultValue);
	}
	
	public static void setWelcomeEnable(Context context, boolean enable) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		
		Editor editor = sp.edit();
		editor.putBoolean(KEY_SHOW_WELCOME, enable);
		editor.commit();
	}
	
	public static boolean getWelcomeEnable(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		
		return sp.getBoolean(KEY_SHOW_WELCOME, true);
	}
	
	public boolean hasSound() {
		return configurationInfo.getBoolean(KEY_ENABLE_SOUND, true);
	}
	
	public boolean hasVibrate() {
		return configurationInfo.getBoolean(KEY_ENABLE_VIBRATE, true);
	}
	
	private ConfigManager(Context context) {
		configurationInfo = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		save = configurationInfo.edit();

		int initialFlag = configurationInfo.getInt(KEY_INIT_FLAG, -1);
		
		if (initialFlag == -1) {
			initAppCfg();
		}
	}
	
	public int getSendWay() {
		return configurationInfo.getInt(KEY_SEND_WAY, SEND_BY_HUANXIN);
	}
	
	public static void setSendWay(Context context, int way) {

		if(way != SEND_BY_HUANXIN && way != SEND_BY_RONGYUN)
			return;
		
		SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(KEY_SEND_WAY, way);
		edit.commit();
	}
	
	public void initAppCfg() {
		save.putInt(KEY_INIT_FLAG, 1);
							
		save.putInt("id_index", 1);	
		save.putInt(KEY_SEND_WAY, SEND_BY_HUANXIN);										
		save.putBoolean(KEY_ENABLE_SOUND, true);											
		save.putBoolean(KEY_ENABLE_VIBRATE, true);										
		save.putBoolean(KEY_SHOW_WELCOME, true);										
		save.putBoolean(KEY_ENABLE_WATER, false);										
		save.putBoolean(KEY_ENABLE_LIGHT, false);	

		save.commit();	
	}
	
	/**
	 * Description 从配置文件中写入String型值
	 * @param key 键
	 * @param value 值
	 */
	public void putString(String key, String value) {
		save.putString(key, value);
		save.commit();
	}
	
	/**
	 * Description 从配置文件中写入int型值
	 * @param key 键
	 * @param value 值
	 */
	public void putInt(String key, int value) {
		save.putInt(key, value);
		save.commit();
	}
	
	/**
	 * Description 从配置文件中写入long型值
	 * @param key 键
	 * @param value 值
	 */
	public void putLong(String key, long value) {
		save.putLong(key, value);
		save.commit();
	}

	/**
	 * Description 从配置文件中写入boolean型值
	 * @param key 键
	 * @param value 值
	 */
	public void putBoolean(String key,boolean value){
		save.putBoolean(key, value);
		save.commit();
	}
	
	/**
	 * Description 从配置文件中获取int型值
	 * @param key 键
	 * @return 返回键对应的值，没有对应值则返回-1
	 */
	public int getInt(String key) {
		return configurationInfo.getInt(key, -1);
	}
	
	/**
	 * Description 从配置文件中获取int型值
	 * @param key 键
	 * @return 返回键对应的值，没有对应值则返回-1
	 */
	public int getInt(String key, int defaultValue) {
		return configurationInfo.getInt(key, defaultValue);
	}
	
	/**
	 * Description 从配置文件中获取long型值
	 * @param key 键
	 * @return 返回键对应的值，没有对应值则返回-1
	 */
	public long getLong(String key) {
		return configurationInfo.getLong(key, -1);
	}
	
	/**
	 * Description 从配置文件中获取String型值
	 * @param key 键
	 * @return 返回键对应的值，没有对应值则返回空字符串
	 */
	public String getString(String key) {
		return configurationInfo.getString(key, "");
	}
	
	public Boolean getBoolean(String key){
		return configurationInfo.getBoolean(key, false);
	}
	
	public Boolean getBoolean(String key, boolean defaultValue){
		return configurationInfo.getBoolean(key, defaultValue);
	}
	
	/**
	 * Description 从配置文件中移除某个键值对
	 * @param key 键名
	 */
	public void Remove(String key){
		save.remove(key);
		save.commit();
	}
}
