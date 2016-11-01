package com.smartfarm.tools;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.smartfarm.view.AppContext;
import com.smartfarm.widget.DiyToast;

@SuppressLint("ShowToast")
public class ToastTool {
	private static DiyToast mToast;

	private ToastTool() {
	}

	public static void showToastLong(String text) {
		if (mToast == null) 
			mToast = DiyToast.makeText(AppContext.context(), "", 5000);
			
		mToast.setText(text);
		mToast.setDuration(5000);
		mToast.show();
	}

	public static void showToast(String text) {
		if (mToast == null) 
			mToast = DiyToast.makeText(AppContext.context(), "", 2000);
			
		mToast.setText(text);
		mToast.setDuration(2000);
		mToast.show();
	}

	public static void showToast(int text) {
		if (mToast == null) 
			mToast = DiyToast.makeText(AppContext.context(), "", 2000);
			
		mToast.setText(text);
		mToast.setDuration(2000);
		mToast.show();
	}

	public static void showToastSystem(String msg) {
		
		Toast.makeText(AppContext.context(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public void cancelToast() {
		if (mToast != null) {
			mToast.hide();
			mToast = null;
		}
	}
}
