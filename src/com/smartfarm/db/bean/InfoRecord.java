package com.smartfarm.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "info_record") 
public class InfoRecord {
	
	public static final int RECORD_TYPE_WATER = 1;
	public static final int RECORD_TYPE_LIGHT = RECORD_TYPE_WATER + 1;
	public static final int RECORD_TYPE_TEMP = RECORD_TYPE_LIGHT + 1;
	public static final int RECORD_TYPE_ALARM = RECORD_TYPE_TEMP + 1;
	public static final int RECORD_TYPE_OTHER = RECORD_TYPE_ALARM + 1;

    @DatabaseField(generatedId = true) 
	private int id;

    @DatabaseField(columnName = "type")  
	private int type;

    @DatabaseField(columnName = "data")  
	private String data;
	
    @DatabaseField(columnName = "peng_name")  
	private String pengName;

    @DatabaseField(columnName = "peng_phone_num")  
	private String pengPhoneNum;
	
    @DatabaseField(columnName = "peng_id")  
	private int pengId;
	
    @DatabaseField(columnName = "remark")  
	private String remark;
	
    @DatabaseField(columnName = "time")  
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getPengName() {
		return pengName;
	}

	public void setPengName(String pengName) {
		this.pengName = pengName;
	}

	public String getPengPhoneNum() {
		return pengPhoneNum;
	}

	public void setPengPhoneNum(String pengPhoneNum) {
		this.pengPhoneNum = pengPhoneNum;
	}

	public int getPengId() {
		return pengId;
	}

	public void setPengId(int pengId) {
		this.pengId = pengId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
