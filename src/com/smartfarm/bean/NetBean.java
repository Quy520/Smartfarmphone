package com.smartfarm.bean;

/**
 * 网络对象：平板id，和时间
 * @author QSD
 *
 */
public class NetBean {
	public final int padId;
	public final long time;
	private boolean ok = true;
	
	public NetBean(int padId) {
		this.padId = padId;
		time = System.currentTimeMillis();
	}
	
	public NetBean() {
		ok = false;
		time = 0;
		padId = -1;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	@Override
	public int hashCode() {
		int res = 17;
		res = 37 * res + padId;
		
		return res;
	}

	@Override
	public boolean equals(Object o) {
		
		if(NetBean.class.isInstance(o)) {
			if(padId == ((NetBean)o).padId)
				return true;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "padId -> " + padId + ", time -> " + time;
	}
}
