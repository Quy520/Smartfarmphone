package com.smartfarm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;

public class StallsFragment extends BaseFragment {
	
	private View view;
	private EditText len;
	private EditText stallsFirst;
	private EditText stallsSecond;
	private EditText stallsThird;
	private EditText stallsFourth;
	private EditText stallsFifth;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(view == null) {

			view = inflater.inflate(R.layout.fragment_temp_edit, container, false);

			len = (EditText) view.findViewById(R.id.stalls_len_edit);
			stallsFirst = (EditText) view.findViewById(R.id.stalls_first_edit);
			stallsSecond = (EditText) view.findViewById(R.id.stalls_second_edit);
			stallsThird = (EditText) view.findViewById(R.id.stalls_third_edit);
			stallsFourth = (EditText) view.findViewById(R.id.stalls_fourth_edit);
			stallsFifth = (EditText) view.findViewById(R.id.stalls_fifth_edit);

		}

        return view;
	}
	
	public void refeshData() {
		
		PengInfo info = AppContext.context().getCurrPengInfo();
		
		len.setText("");
		stallsFirst.setText("");
		stallsSecond.setText("");
		stallsThird.setText("");
		stallsFourth.setText("");
		stallsFifth.setText("");
		len.setHint(String.valueOf(info.getLenMax()));
		stallsFirst.setHint(String.valueOf(info.getLenFirst()));
		stallsSecond.setHint(String.valueOf(info.getOpenSecond()));
		stallsThird.setHint(String.valueOf(info.getOpenThird()));
		stallsFourth.setHint(String.valueOf(info.getOpenFourth()));
		stallsFifth.setHint(String.valueOf(info.getOpenFifth()));
	}
	
	@Override
	public void onResume() {
		super.onResume();

		refeshData();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.main_activity_menu, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		PengInfo info = AppContext.context().getCurrPengInfo();
		
		if(!ShowUtil.isEmpty(len.getText().toString())) {
			
			int openLenValue = Integer.valueOf(len.getText().toString());
			
			if(openLenValue < 100) {
				
				ToastTool.showToast("链条总长度输入有误，请重新设置!");
				return true; 
			}
			
			info.setLenMax(openLenValue);
		}
		
		if(!ShowUtil.isEmpty(stallsFirst.getText().toString())) {
			
			int openLenValue = Integer.valueOf(stallsFirst.getText().toString());
			
			if(openLenValue > 100 || openLenValue <= 0) {
				
				ToastTool.showToast("第一档长度输入有误，请重新设置!");
				return true;
			}
			
			info.setLenFirst(openLenValue);
		}
		
		if(!ShowUtil.isEmpty(stallsSecond.getText().toString())) {
			
			int openLenValue = Integer.valueOf(stallsSecond.getText().toString());
			
			if(openLenValue > 50 || openLenValue <= 0) {
				
				ToastTool.showToast("第二档长度输入有误，请重新设置!");
				return true;
			}
			
			info.setOpenSecond(openLenValue);
		}
		
		if(!ShowUtil.isEmpty(stallsThird.getText().toString())) {
			
			int openLenValue = Integer.valueOf(stallsThird.getText().toString());
			
			if(openLenValue > 50 || openLenValue <= 0) {
				
				ToastTool.showToast("第三档长度输入有误，请重新设置!");
				return true;
			}
			
			info.setOpenThird(openLenValue);
		}
		
		if(!ShowUtil.isEmpty(stallsFourth.getText().toString())) {
			
			int openLenValue = Integer.valueOf(stallsFourth.getText().toString());
			
			if(openLenValue > 50 || openLenValue <= 0) {
				
				ToastTool.showToast("第四档长度输入有误，请重新设置!");
				return true;
			}
			
			info.setOpenFourth(openLenValue);
		}
		
		if(!ShowUtil.isEmpty(stallsFifth.getText().toString())) {
			
			int openLenValue = Integer.valueOf(stallsFifth.getText().toString());
			
			if(openLenValue > 50 || openLenValue <= 0) {
				
				ToastTool.showToast("剩余档位长度输入有误，请重新设置!");
				return true;
			}
			
			info.setOpenFifth(openLenValue);
		}

		PengInfoDao.update(info);
		ToastTool.showToast(R.string.save_success);
		AppContext.context().refreshPengInfo();
		refeshData();
		
		return true;
	}

	@Override
	public void onClick(View v) {

	}
}
