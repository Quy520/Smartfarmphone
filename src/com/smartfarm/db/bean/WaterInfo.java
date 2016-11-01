package com.smartfarm.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "water_info") 
public class WaterInfo {

    @DatabaseField(generatedId = true) 
	private int id;

    @DatabaseField(columnName = "peng_id") 
	private int pengId;

    @DatabaseField(columnName = "water_count") 
	private int waterCount;

    @DatabaseField(columnName = "water_time") 
	private int waterTime;

    @DatabaseField(columnName = "water_min") 
	private int waterMin;

    @DatabaseField(columnName = "water_alarm_max") 
	private int waterAlarmMax;

    @DatabaseField(columnName = "water_alarm_min") 
	private int waterAlarmMin;

    @DatabaseField(columnName = "water_mode") 
	private boolean waterMode;

    @DatabaseField(columnName = "water_max_enable") 
	private boolean waterMaxEnable;

    @DatabaseField(columnName = "water_min_enable") 
	private boolean waterMinEnable;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPengId() {
		return pengId;
	}

	public void setPengId(int pengId) {
		this.pengId = pengId;
	}

	public int getWaterCount() {
		return waterCount;
	}

	public void setWaterCount(int waterCount) {
		this.waterCount = waterCount;
	}

	public int getWaterTime() {
		return waterTime;
	}

	public void setWaterTime(int waterTime) {
		this.waterTime = waterTime;
	}

	public int getWaterMin() {
		return waterMin;
	}

	public void setWaterMin(int waterMin) {
		this.waterMin = waterMin;
	}

	public int getWaterAlarmMax() {
		return waterAlarmMax;
	}

	public void setWaterAlarmMax(int waterAlarmMax) {
		this.waterAlarmMax = waterAlarmMax;
	}

	public int getWaterAlarmMin() {
		return waterAlarmMin;
	}

	public void setWaterAlarmMin(int waterAlarmMin) {
		this.waterAlarmMin = waterAlarmMin;
	}

	public boolean isWaterMode() {
		return waterMode;
	}

	public void setWaterMode(boolean waterMode) {
		this.waterMode = waterMode;
	}

	public boolean isWaterMaxEnable() {
		return waterMaxEnable;
	}

	public void setWaterMaxEnable(boolean waterMaxEnable) {
		this.waterMaxEnable = waterMaxEnable;
	}

	public boolean isWaterMinEnable() {
		return waterMinEnable;
	}

	public void setWaterMinEnable(boolean waterMinEnable) {
		this.waterMinEnable = waterMinEnable;
	}
	
	
}
