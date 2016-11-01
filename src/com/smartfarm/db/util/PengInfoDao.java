package com.smartfarm.db.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.tools.ExceptionUtils;
import com.smartfarm.tools.Md5Utils;

public class PengInfoDao {

	public static List<PengInfo> findAll() {

		List<PengInfo> res;
		try {
			
			Dao<PengInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(PengInfo.class);
			
			res = dao.queryForAll();
			
			if (res == null || res.isEmpty()) {

				res = new ArrayList<PengInfo>();
				res.add(add("大棚1"));
			}
			return res;
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
			return null;
		}
	}
	
	public static PengInfo add(String name) {

		PengInfo defaultInfo = getDefaultPengInfo(name);
		try {

			Dao<PengInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(PengInfo.class);


			if (!name.equals("")) {
				dao.create(defaultInfo);
			} else {
				dao.create(defaultInfo);
				defaultInfo.setName("大棚" + defaultInfo.getId());
				dao.update(defaultInfo);
			}

		} catch(SQLException e) {
			ExceptionUtils.report(e);
		}
		
		return defaultInfo;
	}
	
	public static PengInfo findById(int id) {
		
		try {

			Dao<PengInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(PengInfo.class);
			
			return dao.queryForId(id);
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return null;
		}
	}
	
	public static PengInfo findByPhoneNum(String phone) {
		
		try {

			Dao<PengInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(PengInfo.class);
			
			return dao.queryBuilder().where().eq("phone_num", phone).queryForFirst();
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return null;
		}
	}
	
	public static boolean update(PengInfo info) {
		
		try {

			Log.d("mmsg", "info.getPhone -> " + info.getPhoneNum());
			Dao<PengInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(PengInfo.class);
			
			return dao.update(info) > 0;
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return false;
		}
	}
	
	public static boolean delete(PengInfo info) {
		
		try {

			Dao<PengInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(PengInfo.class);
			
			return dao.delete(info) > 0;
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return false;
		}
	}
	
	public static boolean delete(int id) {
		
		try {

			Dao<PengInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(PengInfo.class);
			
			return dao.deleteById(id) > 0;
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return false;
		}
	}

	public static PengInfo getDefaultPengInfo(String name) {

		PengInfo defaultInfo = new PengInfo();
		defaultInfo.setUserId(1);
		defaultInfo.setLenFirst(30);
		defaultInfo.setLenMax(120);
		defaultInfo.setName(name);
		defaultInfo.setNightEnd("06:00");
		defaultInfo.setNightStart("18:00");
		defaultInfo.setPwd(Md5Utils.encode("123456"));
		defaultInfo.setTempDiffMax(30);
		defaultInfo.setTempDiffMin(26);
		defaultInfo.setTempMax(34);
		defaultInfo.setTempMin(22);
		defaultInfo.setVenTime(30);
		defaultInfo.setTempMax(34);
		defaultInfo.setTempDiffMax(30);
		defaultInfo.setTempDiffMin(26);
		defaultInfo.setTempMin(22);
		defaultInfo.setAlarmMaxEnable(true);
		defaultInfo.setAlarmMinEnable(false);
		defaultInfo.setOpenSecond(5);
		defaultInfo.setOpenThird(10);
		defaultInfo.setOpenFourth(15);
		defaultInfo.setOpenFifth(20);
		defaultInfo.setWindowCount(3);
		defaultInfo.setCreateTime(System.currentTimeMillis());

		return defaultInfo;
	}
}
