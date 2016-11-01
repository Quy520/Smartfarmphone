package com.smartfarm.tools;

import com.smartfarm.view.AppContext;
import com.smartfarm.view.util.ConfigManager;

public class DeviceIdHelper {

	public static String DEVICE_ID = "";
	
	private static final String DEVICE_ID_KEY = "device_id";
	
	public static void init() {
		
		DEVICE_ID = ConfigManager.getString(AppContext.context(), DEVICE_ID_KEY);
		
		if(ShowUtil.isEmpty(DEVICE_ID)) {
			
			DEVICE_ID = "a" + System.currentTimeMillis() + ":" + android.os.Build.MODEL;
			ConfigManager.putString(AppContext.context(), DEVICE_ID_KEY, DEVICE_ID);
		}
	}
}
