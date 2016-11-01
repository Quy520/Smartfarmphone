package com.smartfarm.db.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.tools.ExceptionUtils;

public class TempConfigDao {

	public static List<TempConfig> findAll(PengInfo info) {

		List<TempConfig> res;
		try {
			
			Dao<TempConfig, Integer> dao = DatabaseHelper.getDbHelper().getDao(TempConfig.class);
			
			res = dao.queryForEq("peng_id", info.getId());
			
			if(res == null || res.isEmpty()) {
				
				res = getDefaultConfig(info);
				
				for (TempConfig liveItemBean : res) {  
                	dao.create(liveItemBean);
                }  
			}
			return res;
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
			return null;
		}
	}
	
	public static void update(Map<Integer, TempConfig> configs, int pengId) {
		
		try {
			
			Dao<TempConfig, Integer> dao = DatabaseHelper.getDbHelper().getDao(TempConfig.class);
			
			for(TempConfig config : configs.values()) {
				
				dao.updateRaw("UPDATE temp_config SET max = " + config.getMax() 
						+ ", nor_max = " + config.getNorMax() + ", nor_min = " + config.getNorMin()
						+ ", min = " + config.getMin() + " WHERE window_id = " + config.getWindowId()
						+ " AND peng_id = " + pengId);
			}
			
		} catch (SQLException e) {
			ExceptionUtils.report(e);
		}
	}
	
	public static boolean update(TempConfig config) {
		
		try {

			Dao<TempConfig, Integer> dao = DatabaseHelper.getDbHelper().getDao(TempConfig.class);
			
			return dao.update(config) > 0;
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return false;
		}
	}
	
	public static List<TempConfig> getDefaultConfig(PengInfo info) {
		
		List<TempConfig> res = new ArrayList<TempConfig>();
		
		for(int i = 1; i <= 9; i++) {
			
			TempConfig config = new TempConfig();
		
			config.setMax(info.getTempMax());
			config.setMin(info.getTempMin());
			config.setNorMax(info.getTempDiffMax());
			config.setNorMin(info.getTempDiffMin());
			config.setPengId(info.getId());
			config.setWindowId(i - 1);
			res.add(config);
		}
		
		return res;
	}
}
