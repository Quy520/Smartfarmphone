package com.smartfarm.widget;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.smartfarm.bean.TempBean;
import com.smartfarm.tools.Constants;

@SuppressLint("UseSparseArrays")
public class TempManager {
	private Map<Integer, TempBean> temps;
	
	public TempManager() {
		
		temps = new HashMap<Integer, TempBean>();
	}
	
	public void save(TempBean temp) {
		
		temps.put(temp.getId(), temp);
	}
	
	public TempBean get(int id) {
		
		if(temps.containsKey(id) && System.currentTimeMillis() - 
				temps.get(id).getTime() < Constants.CLEAR_TEMP_PERIOD)
			return temps.get(id);
		
		int[] noneArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		return new TempBean(noneArray, noneArray, -1, 0, "");
	}
}
