package com.smartfarm.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "light_info") 
public class LightInfo {

    @DatabaseField(generatedId = true) 
	private int id;

    @DatabaseField(columnName = "peng_id") 
	private int pengId;
	
    @DatabaseField(columnName = "min") 
	private int min;
	
    @DatabaseField(columnName = "max") 
	private int max;
	
    @DatabaseField(columnName = "need") 
	private int need;
	
    @DatabaseField(columnName = "diy") 
	private int diy;
	
    @DatabaseField(columnName = "count") 
	private int count;
	
    @DatabaseField(columnName = "start") 
	private String start;
	
    @DatabaseField(columnName = "mode") 
	private boolean mode;

	public boolean isMode() {
		return mode;
	}

	public void setMode(boolean mode) {
		this.mode = mode;
	}

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

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getNeed() {
		return need;
	}

	public void setNeed(int need) {
		this.need = need;
	}

	public int getDiy() {
		return diy;
	}

	public void setDiy(int diy) {
		this.diy = diy;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
	
	
}
