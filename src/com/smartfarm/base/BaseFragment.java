package com.smartfarm.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.tools.EventBus;

public class BaseFragment extends Fragment implements
		android.view.View.OnClickListener, BaseFragmentInterface {
	public static final int STATE_NONE = 0;
	public static final int STATE_REFRESH = 1;
	public static final int STATE_LOADMORE = 2;
	public static final int STATE_NOMORE = 3;
	public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
	public static int mState = STATE_NONE;

	protected LayoutInflater mInflater;
	private SweetAlertDialog pDialog;
	private View holdView;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		if(EventHandler.class.isInstance(this))
			EventBus.getDefault().add((EventHandler)this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {

		if(EventHandler.class.isInstance(this))
			EventBus.getDefault().remove((EventHandler)this);
		
		hideLoadingDialog();
		super.onDestroy();
	}

	protected boolean refreshOptionsMenu() {
		return false;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.mInflater = inflater;
		
		if(getLayoutId() != 0) {
			
			if(holdView()) {
				
				if(holdView == null) {
					
					holdView = mInflater.inflate(getLayoutId(), null);
					
					initView(holdView);
					
					if(refreshOptionsMenu())
						getActivity().supportInvalidateOptionsMenu();
				} else {
					
					ViewGroup parent = (ViewGroup) holdView.getParent();
					if (parent != null) {
						parent.removeView(holdView);
					}
				}
				
				initData();
			}
			
		}
		
		return holdView == null ? super.onCreateView(
				inflater, container, savedInstanceState) : holdView;
	}
	
	protected View getHoldView() {
		return holdView;
	}

	public String getTitle() {
		return "";
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return false;
	}
	
	protected boolean holdView() {
		return true;
	}

	protected int getLayoutId() {
		return 0;
	}

	protected View inflateView(int resId) {
		return this.mInflater.inflate(resId, null);
	}

	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void initView(View view) {

	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {

	}

	protected void showLoadingDialog() {

		showLoadingDialog("Loading");
	}

	protected void showLoadingDialog(int tipRes) {
		
		showLoadingDialog(getResources().getString(tipRes));
	}

	protected void showLoadingDialog(String tip) {

		Log.d("mmsg", "showLoadingDialog, tip -> " + tip);
		if(pDialog == null) {
			pDialog = new SweetAlertDialog(getActivity(),
					SweetAlertDialog.PROGRESS_TYPE).setTitleText(tip);

			pDialog.setCancelable(false);
			pDialog.show();
		}
	}
	
	protected void hideLoadingDialog() {
		
		if(pDialog == null) 
			return;
		
		pDialog.cancel();
		pDialog = null;
	}
}
