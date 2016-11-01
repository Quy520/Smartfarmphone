package com.smartfarm.db.util;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.tools.ExceptionUtils;

public class InfoRecordDao {

	public static List<InfoRecord> findAll() {

		List<InfoRecord> res = null;
		try {
			
			Dao<InfoRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(InfoRecord.class);
			
			res = dao.queryForAll();
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
		
		return res;
	}

	public static List<InfoRecord> findByPage(int page, int type) {

		List<InfoRecord> res = null;
		try {
			
			Dao<InfoRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(InfoRecord.class);
			
			res = dao.queryBuilder().orderBy("time", false).limit(20l).offset(20l * page).where().eq("type", type).query();
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
		
		return res;
	}

	public static InfoRecord findLast(int type, int pendId) {

		InfoRecord res = null;
		try {
			
			Dao<InfoRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(InfoRecord.class);
			
			res = dao.queryBuilder().orderBy("time", false).where()
					.eq("type", type).and().eq("peng_id", pendId).queryForFirst();
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
		
		return res;
	}
	
	public static void add(PengInfo pengInfo, String info, int type, long time) {
		
		try {
			
			Dao<InfoRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(InfoRecord.class);
			InfoRecord record = new InfoRecord();
			
			record.setData(info);
			record.setTime(time);
			record.setType(type);
			record.setPengId(pengInfo.getId());
			record.setPengName(pengInfo.getName());
			record.setPengPhoneNum(pengInfo.getPhoneNum());
			
			dao.create(record);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	public static void add(InfoRecord record) {
		
		try {
			
			Dao<InfoRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(InfoRecord.class);
			dao.create(record);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	public static void delete(InfoRecord record) {
		
		try {
			
			Dao<InfoRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(InfoRecord.class);
			dao.delete(record);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	public static void deleteById(int id) {
		
		try {
			
			Dao<InfoRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(InfoRecord.class);
			dao.deleteById(id);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
}
