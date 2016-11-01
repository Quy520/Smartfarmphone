package com.smartfarm.db.util;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.LightInfo;
import com.smartfarm.tools.ExceptionUtils;

public class LightInfoDao {
	
	public static final int DEFAULT_LIGHT_NEED = 480;
	public static final int DEFAULT_LIGHT_MIN = 100;
	public static final int DEFAULT_LIGHT_MAX = 10000;
	public static final int DEFAULT_LIGHT_DIY = 120;
	public static final int DEFAULT_LIGHT_COUNT = 2;
	public static final String DEFAULT_LIGHT_START = "20:00";

	public static LightInfo findByPengId(int id) {

		try {

			Dao<LightInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(LightInfo.class);
			
			LightInfo res = dao.queryBuilder().where().eq("peng_id", id).queryForFirst();
			
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

	public static void update(LightInfo light) {

		try {

			Dao<LightInfo, Integer> dao = DatabaseHelper.getDbHelper().getDao(LightInfo.class);
			
			dao.update(light);

		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	private static LightInfo getDefault() {
		
		LightInfo res = new LightInfo();
		
		res.setCount(DEFAULT_LIGHT_COUNT);
		res.setDiy(DEFAULT_LIGHT_DIY);
		res.setMax(DEFAULT_LIGHT_MAX);
		res.setMin(DEFAULT_LIGHT_MIN);
		res.setNeed(DEFAULT_LIGHT_NEED);
		res.setStart(DEFAULT_LIGHT_START);
		res.setMode(false);
		
		return res;
	}
}
