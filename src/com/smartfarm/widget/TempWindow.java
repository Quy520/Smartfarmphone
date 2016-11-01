package com.smartfarm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartfarm.bean.TempTuple;
import com.smartfarm.view.R;

public class TempWindow extends LinearLayout {

	private int state = 0;
	private TempView mTemp;
	private TextView mState;
	
	public TempWindow(Context context) {
		this(context, null, 0);
	}
	
	public TempWindow(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TempWindow(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		this.setLayoutParams(lp);
		LayoutInflater.from(context).inflate(R.layout.temp_view, this);
		
		mTemp = (TempView) findViewById(R.id.temp_show);
		mState = (TextView) findViewById(R.id.temp_state);
		
		setWinState(state);
	}

	public void setWindowId(int id) {
		
		mTemp.setWindowId(id);
	}
	
	public void setTemp(TempTuple tuple) {
		
		mTemp.setTemp(tuple.temp);
		setWinState(tuple.state);
	}
	
	private void setWinState(int state) {
		
		mState.setText(String.format(getContext().getString(R.string.win_state), state));
	}
}
