package com.smartfarm.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.R;

public class SuggestFragment extends BaseFragment {
	
	private EditText feedback;
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.suggest_fragment, null);
		
		feedback = (EditText) view.findViewById(R.id.et_feedback);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.submit_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		getActivity().finish();
		ToastTool.showToast(R.string.str_feedback_succeed);
		
		if(!ShowUtil.isEmpty(feedback.getText().toString())) {
			
			SmartfarmNetHelper.appSendSuggest(feedback.getText().toString(),new BaseAsyncHttpResHandler() {
				
				@Override
				public void onSuccess(ApiResponse res) {
//					ToastTool.showToast("谢谢您的反馈，我们会及时处理");
				}
				
				@Override
				public void onFailure(int errorCode, String data) {
//					ToastTool.showToast("谢谢您真诚的反馈");
				}
			});
		
		}
		
		return true;
	}
}
