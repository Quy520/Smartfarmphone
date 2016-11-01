package com.smartfarm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.smartfarm.view.R;

public class MySwitch extends ImageView implements OnClickListener {
	private boolean on = true;
	private OnSwitchChangeListener listener;
	private boolean dontChange = false;
	
	public MySwitch(Context context) {
		this(context, null, 0);
	}
	
	public MySwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MySwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setImageResource(R.drawable.bn_yes_bg);
		setOnClickListener(this);
	}
	
	public boolean isOn() {
		return on;
	}
	
	public void setDontChange(boolean dontChange) {
		this.dontChange = dontChange;
	}
	
	public void setSwitch(boolean isOn) {
		on = isOn;
		
		if(on)
			setImageResource(R.drawable.bn_yes_bg);
		else 
			setImageResource(R.drawable.bn_no_bg);
	}

	@Override
	public void onClick(View v) {
		
		if(listener != null) {
			if(!listener.onSwitchChange(!on))
				return;
		}
		
		if(!dontChange) {
			
			on = !on;
			
			if(on)
				setImageResource(R.drawable.bn_yes_bg);
			else 
				setImageResource(R.drawable.bn_no_bg);
		}
	}
	
	public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
		listener = onSwitchChangeListener;
	}
	
	public interface OnSwitchChangeListener {
		/**
		 * @param nextState 将要改变的状态
		 * @return 返回一个boolean值，为true表示接受改变，为false则表示拒绝改变
		 */
		boolean onSwitchChange(boolean nextState);
	}
}
