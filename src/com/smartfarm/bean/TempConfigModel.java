package com.smartfarm.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.db.util.TempConfigDao;
import com.smartfarm.view.AppContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressLint("UseSparseArrays")
public class TempConfigModel implements Serializable {

	private static final long serialVersionUID = -4930912203344785753L;

	@XStreamAlias("mode")
	private boolean moreMode;
	private int pengId;

	@XStreamAlias("temp")
	private TempConfig tempConfig;

	@XStreamAlias("temps")
	private Map<Integer, TempConfig> tempConfigs;
	
	public TempConfigModel(PengInfo info) {
		
		pengId = info.getId();
		moreMode = info.isMoreMode();
		
		reset(info);
	}
	
	public TempConfig getTempConfig() {
		return tempConfig;
	}

	public Map<Integer, TempConfig> getTempConfigs() {
		return tempConfigs;
	}

	private void reset(PengInfo info) {
		
		if(moreMode) {
			
			tempConfigs = new HashMap<Integer, TempConfig>();
			List<TempConfig> data = TempConfigDao.findAll(info);
			
			for(TempConfig config : data) {
				tempConfigs.put(config.getWindowId(), config);
			}
		} else {
			
			tempConfig = new TempConfig();
			
			tempConfig.setMax(info.getTempMax());
			tempConfig.setMin(info.getTempMin());
			tempConfig.setNorMax(info.getTempDiffMax());
			tempConfig.setNorMin(info.getTempDiffMin());
			tempConfig.setPengId(info.getId());
		}
	}
	
	public TempConfig getTempConfig(int windowId) {
		
		if(moreMode) {

			return tempConfigs.get(windowId);
		} else {

			return tempConfig;
		}
	}
	
	public void update(TempConfig config) {
		
		if(moreMode) {
			
			TempConfigDao.update(config);
		} else {
			
			PengInfo info = AppContext.context().getCurrPengInfo();
			
			info.setTempMax(config.getMax());
			info.setTempMin(config.getMin());
			info.setTempDiffMax(config.getNorMax());
			info.setTempDiffMin(config.getNorMin());
			
			PengInfoDao.update(info);
		}
	}

	public boolean isMoreMode() {
		return moreMode;
	}

	public void setMoreMode(boolean moreMode) {
		this.moreMode = moreMode;
	}

	public int getPengId() {
		return pengId;
	}

	public void setPengId(int pengId) {
		this.pengId = pengId;
	}
}
