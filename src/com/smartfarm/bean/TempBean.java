package com.smartfarm.bean;

import java.util.Iterator;

import com.smartfarm.view.AppContext;

public class TempBean implements Iterable<TempTuple> {
	private int[] temp;
	private int[] state;
	private long time;
	private int id;
	private boolean vaild = false;
	private String sourceData;

	@Override
	public Iterator<TempTuple> iterator() {
		return new MyIterator();
	}
	
	public TempBean(int[] temp, int[] state, int id, long time, String data) {
		this.id = id;
		this.temp = temp;
		this.time = time;
		this.state = state;
		sourceData = data;
		vaild = true;
		
		if(id < 0)
			vaild = false;
	}
	
	public String getSourceData() {
		return sourceData;
	}

	public int[] getTemp() {
		return temp;
	}

	public void setTemp(int[] temp) {
		this.temp = temp;
	}

	public long getTime() {
		
		if(vaild)
			return time;
		
		return 0;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isVaild() {
		return vaild;
	}

	public void setVaild(boolean vaild) {
		this.vaild = vaild;
	}
	
	public int getMaxTemp() {

		int max = 0;
		int len = AppContext.context().getCurrPengInfo().getWindowCount();
		for(int i = 0; i < len; i++)
			if(temp[i] > max)
				max = temp[i];
		
		return max;
	}
	
	public int getMinTemp() {
		
		int min = temp[0];
		int len = AppContext.context().getCurrPengInfo().getWindowCount();
		for(int i = 1; i < len; i++)
			if(temp[i] < min)
				min = temp[i];
		
		return min;
	}

	class MyIterator implements Iterator<TempTuple> {

		int index = 0;
		
		@Override
		public boolean hasNext() {
			return index < temp.length;
		}

		@Override
		public TempTuple next() {
			return new TempTuple(temp[index], state[index++]);
		}

		@Override
		public void remove() {}
	}
}
