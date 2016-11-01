package com.smartfarm.db.bean;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "peng_info") 
public class PengInfo {

    @DatabaseField(generatedId = true) 
	private int id;
	
    @DatabaseField(columnName = "user_id") 
	private int userId;

    @DatabaseField(columnName = "name") 
	private String name;

    @DatabaseField(columnName = "phone_num") 
	private String phoneNum;

    @DatabaseField(columnName = "pwd") 
	private String pwd;

    @DatabaseField(columnName = "window_count") 
	private int windowCount;

    @DatabaseField(columnName = "temp_max") 
	private int tempMax;

    @DatabaseField(columnName = "temp_min") 
	private int tempMin;

    @DatabaseField(columnName = "temp_diff_max") 
	private int tempDiffMax;

    @DatabaseField(columnName = "temp_diff_min") 
	private int tempDiffMin;
	
    @DatabaseField(columnName = "len_max") 
	private int lenMax;
	
    @DatabaseField(columnName = "len_first") 
	private int lenFirst;
	
    @DatabaseField(columnName = "ven_time") 
	private int venTime;
	
    @DatabaseField(columnName = "night_start") 
	private String nightStart;
	
    @DatabaseField(columnName = "night_end") 
	private String nightEnd;

    @DatabaseField(columnName = "create_time") 
	private long createTime;

    @DatabaseField(columnName = "auto_mode") 
	private boolean autoMode = false;
	
    @DatabaseField(columnName = "remark") 
	private String remark;

    @DatabaseField(columnName = "alarm_temp_max") 
	private int alarmTempMax = 35;

    @DatabaseField(columnName = "alarm_temp_min") 
	private int alarmTempMin = 15;

    @DatabaseField(columnName = "push_time") 
	private int pushTime = 10;

    @DatabaseField(columnName = "alarm_max_enable") 
	private boolean alarmMaxEnable = true;

    @DatabaseField(columnName = "alarm_min_enable") 
	private boolean alarmMinEnable = false;

    @DatabaseField(columnName = "open_second") 
	private int openSecond = 5;

    @DatabaseField(columnName = "open_third") 
	private int openThird = 10;

    @DatabaseField(columnName = "open_fourth") 
	private int openFourth = 15;

    @DatabaseField(columnName = "open_fifth") 
	private int openFifth = 20;

    @DatabaseField(columnName = "more_mode") 
	private boolean moreMode;
    
    @DatabaseField(columnName = "high_auto") 
	private boolean highAuto = false;
    
    @DatabaseField(columnName = "open_all")
    private boolean openAll = false;
    
    @DatabaseField(columnName = "close_all")
    private boolean closeAll = false;
    
    @DatabaseField(columnName = "alarm_msg_enable")
    private boolean alarmMsgEnable = false;

	public boolean isAlarmMsgEnable() {
		return alarmMsgEnable;
	}

	public void setAlarmMsgEnable(boolean alarmMsgEnable) {
		this.alarmMsgEnable = alarmMsgEnable;
	}

	public boolean isOpenAll() {
		return openAll;
	}

	public void setOpenAll(boolean openAll) {
		this.openAll = openAll;
	}

	public boolean isCloseAll() {
		return closeAll;
	}

	public void setCloseAll(boolean closeAll) {
		this.closeAll = closeAll;
	}

	public boolean isHighAuto() {
		return highAuto;
	}

	public void setHighAuto(boolean highAuto) {
		this.highAuto = highAuto;
	}

	public boolean isMoreMode() {
		return moreMode;
	}

	public void setMoreMode(boolean moreMode) {
		this.moreMode = moreMode;
	}

	public boolean isAlarmMaxEnable() {
		return alarmMaxEnable;
	}

	public void setAlarmMaxEnable(boolean alarmMaxEnable) {
		this.alarmMaxEnable = alarmMaxEnable;
	}

	public boolean isAlarmMinEnable() {
		return alarmMinEnable;
	}

	public void setAlarmMinEnable(boolean alarmMinEnable) {
		this.alarmMinEnable = alarmMinEnable;
	}

	public int getOpenSecond() {
		return openSecond;
	}

	public void setOpenSecond(int openSecond) {
		this.openSecond = openSecond;
	}

	public int getOpenThird() {
		return openThird;
	}

	public void setOpenThird(int openThird) {
		this.openThird = openThird;
	}

	public int getOpenFourth() {
		return openFourth;
	}

	public void setOpenFourth(int openFourth) {
		this.openFourth = openFourth;
	}

	public int getOpenFifth() {
		return openFifth;
	}

	public void setOpenFifth(int openFifth) {
		this.openFifth = openFifth;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isAutoMode() {
		return autoMode;
	}

	public void setAutoMode(boolean autoMode) {
		this.autoMode = autoMode;
		Log.d("zqq", "set->" + autoMode);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getWindowCount() {
		return windowCount;
	}

	public void setWindowCount(int windowCount) {
		this.windowCount = windowCount;
	}

	public int getTempMax() {
		return tempMax;
	}

	public void setTempMax(int tempMax) {
		this.tempMax = tempMax;
	}

	public int getTempMin() {
		return tempMin;
	}

	public void setTempMin(int tempMin) {
		this.tempMin = tempMin;
	}

	public int getTempDiffMax() {
		return tempDiffMax;
	}

	public void setTempDiffMax(int tempDiffMax) {
		this.tempDiffMax = tempDiffMax;
	}

	public int getTempDiffMin() {
		return tempDiffMin;
	}

	public void setTempDiffMin(int tempDiffMin) {
		this.tempDiffMin = tempDiffMin;
	}

	public int getLenMax() {
		return lenMax;
	}

	public void setLenMax(int lenMax) {
		this.lenMax = lenMax;
	}

	public int getLenFirst() {
		return lenFirst;
	}

	public void setLenFirst(int lenFirst) {
		this.lenFirst = lenFirst;
	}

	public int getVenTime() {
		return venTime;
	}

	public void setVenTime(int venTime) {
		this.venTime = venTime;
	}

	public String getNightStart() {
		return nightStart;
	}

	public void setNightStart(String nightStart) {
		this.nightStart = nightStart;
	}

	public String getNightEnd() {
		return nightEnd;
	}

	public void setNightEnd(String nightEnd) {
		this.nightEnd = nightEnd;
	}

	public int getAlarmTempMax() {
		return alarmTempMax;
	}

	public void setAlarmTempMax(int alarmTempMax) {
		this.alarmTempMax = alarmTempMax;
	}

	public int getAlarmTempMin() {
		return alarmTempMin;
	}

	public void setAlarmTempMin(int alarmTempMin) {
		this.alarmTempMin = alarmTempMin;
	}

	public int getPushTime() {
		return pushTime;
	}

	public void setPushTime(int pushTime) {
		this.pushTime = pushTime;
	}
}
