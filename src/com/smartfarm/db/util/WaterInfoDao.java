package com.smartfarm.db.util;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.WaterInfo;
import com.smartfarm.tools.ExceptionUtils;

public class WaterInfoDao {

	public static WaterInfo findByPengId(int id) {

		try {

			Dao<WaterInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(WaterInfo.class);
			
			WaterInfo res = dao.queryBuilder().where().eq("peng_id", id).queryForFirst();
			
			if(res == null) {
				
				res = getDefault();
				res.setPengId(id);
				dao.create(res);
			}
			
			return res;

		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}

		return null;
	}

	public static void update(WaterInfo water) {

		try {

			Dao<WaterInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(WaterInfo.class);
			
			dao.update(water);

		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	private static WaterInfo getDefault() {
		
		WaterInfo defaultInfo = new WaterInfo();

		defaultInfo.setWaterCount(3);
		defaultInfo.setWaterTime(30);
		defaultInfo.setWaterMin(60);
		defaultInfo.setWaterAlarmMin(50);
		defaultInfo.setWaterAlarmMax(80);
		defaultInfo.setWaterMode(false);
		defaultInfo.setWaterMaxEnable(false);
		defaultInfo.setWaterMinEnable(false);
		
		return defaultInfo;
	}
}
