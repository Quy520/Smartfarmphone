package com.smartfarm.tools;

import java.io.File;

import android.os.Environment;

public class Constants {
	public static final String TAG = "smartfarm_phone";

	public static final int MAX_TEXT_LENGTH = 512;

	public static final String CURR_VERSION = "3.2.0";
	public static final String SUPER_USER_PWD = "smartfarm";
	public static final int PAGE_SIZE = 20;

	public static final String ACTION_RECEIVE_TEMP = "com.smartfarm.view.service.RECEIVE_TEMP";
	public static final String ACTION_PROTOCOL_SEND_ERROR = "com.smartfarm.view.service.PROTOCOL_SEND_ERROR";
	public static final String ACTION_NET_CHECK_ERROR = "com.smartfarm.view.service.NET_CHECK_ERROR";

	public final static String DEFAULT_APP_DIR_NAME = "SmartFarm";
	
	public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ DEFAULT_APP_DIR_NAME
			+ File.separator
			+ "SaveImg"
			+ File.separator;

	public static final int SEND_RUNTIME = 40 * 1000;

	public static final int CLEAR_TEMP_PERIOD = 12 * 60 * 1000;
	public static final int CLEAR_TEMP_CHECK_PERIOD = 30 * 1000;

	public static final int CHECK_NET_PERIOD = 30 * 1000;
	public static final int CHECK_NET_ALARM_PERIOD = 8 * 60 * 1000;

	public static final String MSG_PREFIX_DATA = "data_";
	public static final String RECEIVER_ALL = "receiver_all";

	public static final String LOCAL_DATA_ROOT_PATH = "/SmartFarm/";

	public static final String ZT_SITE = "http://www.shztzn.com/";
	public static final String MORE_SITE = "http://www.shztzn.com/index.php?case=archive&act=list&catid=1";

	public static final String LINKED_URL = "http://zhushou.360.cn/detail/index/soft_id/3000720";
	public static final String SHARE_CONTENT = "我刚装了上海洲涛智能放风机,真特么的好用,你要不要也装一套？";
	public static final String SHARE_TITLE = "上海洲涛智能放风机,真特么好用";

	public static final String WE_CHAT_APP_ID = "wx075db958a8b28074";
	public static final String WE_CHAT_APP_SECRET = "b71a40f14087a15ea17422e069236711";

	public static final String QQ_APP_ID = "1104606731";
	public static final String QQ_APP_KEY = "KJlF1POoqnDNMmve";

	public static final int NET_ERROR_MAX_COUNT = 5;

	public static final int WINDOW_ID_1 = 0;
	public static final int WINDOW_ID_2 = WINDOW_ID_1 + 1;
	public static final int WINDOW_ID_3 = WINDOW_ID_2 + 1;
	public static final int WINDOW_ID_4 = WINDOW_ID_3 + 1;
	public static final int WINDOW_ID_5 = WINDOW_ID_4 + 1;
	public static final int WINDOW_ID_6 = WINDOW_ID_5 + 1;
	public static final int WINDOW_ID_7 = WINDOW_ID_6 + 1;
	public static final int WINDOW_ID_8 = WINDOW_ID_7 + 1;
	public static final int WINDOW_ID_9 = WINDOW_ID_8 + 1;

	public static final int WATER_ID_1 = 0;
	public static final int WATER_ID_2 = WATER_ID_1 + 1;
	public static final int WATER_ID_3 = WATER_ID_2 + 1;
	public static final int WATER_ID_4 = WATER_ID_3 + 1;
	public static final int WATER_ID_5 = WATER_ID_4 + 1;
	public static final int WATER_ID_6 = WATER_ID_5 + 1;
	public static final int WATER_ID_7 = WATER_ID_6 + 1;
	public static final int WATER_ID_8 = WATER_ID_7 + 1;
	public static final int WATER_ID_9 = WATER_ID_8 + 1;

	public static final int MOTOR_CONTROL_TYPE_OPEN = 10;
	public static final int MOTOR_CONTROL_TYPE_CLOSE = MOTOR_CONTROL_TYPE_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_STOP = MOTOR_CONTROL_TYPE_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_READ = MOTOR_CONTROL_TYPE_STOP + 10;
	public static final int MOTOR_CONTROL_TYPE_MODE = MOTOR_CONTROL_TYPE_READ + 10;
	public static final int MOTOR_CONTROL_TYPE_SYN = MOTOR_CONTROL_TYPE_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_ALARM = MOTOR_CONTROL_TYPE_SYN + 10;
	public static final int MOTOR_CONTROL_TYPE_TEST = MOTOR_CONTROL_TYPE_ALARM + 10;
	public static final int MOTOR_CONTROL_TYPE_PWD = MOTOR_CONTROL_TYPE_TEST + 10;
	public static final int MOTOR_CONTROL_TYPE_NOTICE = MOTOR_CONTROL_TYPE_PWD + 10;
	public static final int MOTOR_CONTROL_TYPE_SUPER_TEST = MOTOR_CONTROL_TYPE_NOTICE + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_READ = MOTOR_CONTROL_TYPE_SUPER_TEST + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_OPEN = MOTOR_CONTROL_TYPE_LIGHT_READ + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_CLOSE = MOTOR_CONTROL_TYPE_LIGHT_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_CONFIG = MOTOR_CONTROL_TYPE_LIGHT_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_READ = MOTOR_CONTROL_TYPE_WATER_CONFIG + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_OPEN = MOTOR_CONTROL_TYPE_WATER_READ + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_CLOSE = MOTOR_CONTROL_TYPE_WATER_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_MODE = MOTOR_CONTROL_TYPE_WATER_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_STATE = MOTOR_CONTROL_TYPE_WATER_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_SYN_TEMP = MOTOR_CONTROL_TYPE_WATER_STATE + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_CONFIG = MOTOR_CONTROL_TYPE_SYN_TEMP + 10;
	public static final int MOTOR_CONTROL_TYPE_BENG_OPEN = MOTOR_CONTROL_TYPE_LIGHT_CONFIG + 10;
	public static final int MOTOR_CONTROL_TYPE_BENG_CLOSE = MOTOR_CONTROL_TYPE_BENG_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_MODE = MOTOR_CONTROL_TYPE_BENG_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_YAO_OPEN = MOTOR_CONTROL_TYPE_LIGHT_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_YAO_CLOSE = MOTOR_CONTROL_TYPE_YAO_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_ONEKEY_OPEN = MOTOR_CONTROL_TYPE_YAO_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_ONEKEY_CLOSE = MOTOR_CONTROL_TYPE_ONEKEY_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_READ_OTHERS = MOTOR_CONTROL_TYPE_ONEKEY_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_HIGH_MODE = MOTOR_CONTROL_TYPE_READ_OTHERS + 10;
	public static final int MOTOR_CONTROL_TYPE_RAIN_MODE = MOTOR_CONTROL_TYPE_HIGH_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE = MOTOR_CONTROL_TYPE_RAIN_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_OPEN_WIND_END = MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE + 10;

	public static final int PROTOCOL_TYPE_REQUEST = 100;
	public static final int PROTOCOL_TYPE_RESPONSE = PROTOCOL_TYPE_REQUEST + 1;

	public static final int HELP_MSG_CMD = 1;
	public static final int HELP_MSG_CONTROL = HELP_MSG_CMD + 1;
	public static final int HELP_MSG_TEMP = HELP_MSG_CONTROL + 1;
	public static final int HELP_NET_ALARM = HELP_MSG_TEMP + 1;
	public static final int HELP_NET_DIAGNOSIS = HELP_NET_ALARM + 1;
	public static final int HELP_NET_MODE = HELP_NET_DIAGNOSIS + 1;
	public static final int HELP_NET_RELINK = HELP_NET_MODE + 1;
	public static final int HELP_NET_SYN = HELP_NET_RELINK + 1;
	public static final int HELP_NET_TEMP = HELP_NET_SYN + 1;
	public static final int HELP_NET_VEN = HELP_NET_TEMP + 1;
	public static final int HELP_NET_WIN = HELP_NET_VEN + 1;
	public static final int HELP_PRECAUTIONS = HELP_NET_WIN + 1;
	public static final int HELP_SET_FUNCTION = HELP_PRECAUTIONS + 1;
	public static final int HELP_SET_HOUSE = HELP_SET_FUNCTION + 1;
	public static final int HELP_SET_WIN = HELP_SET_HOUSE + 1;
	public static final int HELP_SETTING = HELP_SET_WIN + 1;
	public static final int HELP_SHARE = HELP_SETTING + 1;

}
