package com.smartfarm.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.TempConfigModel;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.widget.togglebutton.ToggleButton;
import com.smartfarm.widget.togglebutton.ToggleButton.OnToggleChanged;

public class TempConfigFragment extends BaseFragment implements TextWatcher, OnItemSelectedListener, OnToggleChanged{
	
	private View root;
	
	private EditText tempMax;
	private EditText tempMin;
	private EditText tempNorMax;
	private EditText tempNorMin;

	private TextView maxTempDescr;
	private TextView maxNorTempDescr;
	private TextView minNorTempDescr;
	private TextView minTempDescr;
	
	private Spinner windowIdSpinner;
	private TextView selectIdTitle;
	private ToggleButton moreModeSwitch;
	
	private String[] windowIds;
	
	private TempConfigModel mTempModel;
	private TempConfig mTempConfig;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_temp_config, container, false);

		initView(root);

		initData();
		
        return root;
	}
	
	@Override
	public void initView(View view) {

		tempMax = (EditText) view.findViewById(R.id.tc_max_temp);
		tempMin = (EditText) view.findViewById(R.id.tc_min_temp);
		tempNorMax = (EditText) view.findViewById(R.id.tc_max_nor_temp);
		tempNorMin = (EditText) view.findViewById(R.id.tc_min_nor_temp);

		maxTempDescr = (TextView) view.findViewById(R.id.tc_max_temp_descr);
		maxNorTempDescr = (TextView) view.findViewById(R.id.tc_nmax_temp_descr);
		minNorTempDescr = (TextView) view.findViewById(R.id.tc_nmin_temp_descr);
		minTempDescr = (TextView) view.findViewById(R.id.tc_min_temp_descr);

		moreModeSwitch = (ToggleButton) view.findViewById(R.id.more_mode_toggle);
		windowIdSpinner = (Spinner) view.findViewById(R.id.select_window_id);
		selectIdTitle = (TextView) view.findViewById(R.id.select_window_id_title);
		
		tempMax.addTextChangedListener(this);
		tempNorMax.addTextChangedListener(this);
		tempNorMin.addTextChangedListener(this);
		tempMin.addTextChangedListener(this);
		view.findViewById(R.id.more_mode_switch).setOnClickListener(this);
		
		moreModeSwitch.setOnToggleChanged(this);
	}
	
	@Override
	public void onClick(View v) {

		moreModeSwitch.toggle();
	}
	
	@Override
	public void initData() {

		PengInfo info = AppContext.context().getCurrPengInfo();
    	windowIds = new String[info.getWindowCount()];
    	
    	for(int i = 1; i <= info.getWindowCount(); i++)
    		windowIds[i - 1] = String.valueOf(i);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
                android.R.layout.simple_spinner_item, windowIds); 
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        windowIdSpinner.setAdapter(adapter); 
        windowIdSpinner.setOnItemSelectedListener(this);
        windowIdSpinner.setSelection(0);
        
        refreshData();
	}
	
	private void refreshData() {
		
		mTempModel = new TempConfigModel(AppContext.context().getCurrPengInfo());
		mTempConfig = mTempModel.getTempConfig(windowIdSpinner.getSelectedItemPosition());
		
		int show = mTempModel.isMoreMode() ? View.VISIBLE : View.GONE;
		
		selectIdTitle.setVisibility(show);
		windowIdSpinner.setVisibility(show);

		if(mTempModel.isMoreMode())
			moreModeSwitch.setToggleOn();
		else
			moreModeSwitch.setToggleOff();
		
		tempMax.setText("");
		tempMin.setText("");
		tempNorMax.setText("");
		tempNorMin.setText("");

		tempMax.setHint("" + mTempConfig.getMax());
		tempMin.setHint("" + mTempConfig.getMin());
		tempNorMax.setHint("" + mTempConfig.getNorMax());
		tempNorMin.setHint("" + mTempConfig.getNorMin());

		maxTempDescr.setText(String.format(getString(R.string.max_temp_descr), mTempConfig.getMax()));
		maxNorTempDescr.setText(String.format(getString(R.string.max_nor_temp_descr), mTempConfig.getNorMax(), mTempConfig.getMax()));
		minNorTempDescr.setText(String.format(getString(R.string.min_nor_temp_descr), mTempConfig.getNorMin(), mTempConfig.getNorMax()));
		minTempDescr.setText(String.format(getString(R.string.min_temp_descr), mTempConfig.getMin(), mTempConfig.getNorMin()));
	}

	private boolean submit() {
		
		int tempMaxValue = -1;
		if (!ShowUtil.isEmpty(tempMax.getText().toString()))
			tempMaxValue = Integer.valueOf(tempMax.getText().toString());
		else
			tempMaxValue = Integer.valueOf(tempMax.getHint().toString());

		int tempNorMaxValue = -1;
		if (!ShowUtil.isEmpty(tempNorMax.getText().toString()))
			tempNorMaxValue = Integer.valueOf(tempNorMax.getText().toString());
		else
			tempNorMaxValue = Integer.valueOf(tempNorMax.getHint().toString());

		if (tempNorMaxValue >= tempMaxValue) {

			tempNorMax.requestFocus();
			ToastTool.showToast("输入的适宜温度下限过大，请重新输入！");
			return false;
		}

		int tempNorMinValue = -1;
		if (!ShowUtil.isEmpty(tempNorMin.getText().toString()))
			tempNorMinValue = Integer.valueOf(tempNorMin.getText().toString());
		else
			tempNorMinValue = Integer.valueOf(tempNorMin.getHint().toString());

		if (tempNorMinValue >= tempNorMaxValue) {

			tempNorMin.requestFocus();
			ToastTool.showToast("输入的开风温度过大，请重新输入！");
			return false;
		}

		int tempMinValue = -1;
		if (!ShowUtil.isEmpty(tempMin.getText().toString()))
			tempMinValue = Integer.valueOf(tempMin.getText().toString());
		else
			tempMinValue = Integer.valueOf(tempMin.getHint().toString());

		if (tempMinValue >= tempNorMinValue) {

			tempMin.requestFocus();
			ToastTool.showToast("输入的关风温度过大，请重新输入！");
			return false;
		}

		mTempConfig.setMax(tempMaxValue);
		mTempConfig.setMin(tempMinValue);
		mTempConfig.setNorMax(tempNorMaxValue);
		mTempConfig.setNorMin(tempNorMinValue);

		mTempModel.update(mTempConfig);
		refreshData();
		
		CommonTool.HideKb(getActivity(), root);
		ToastTool.showToast("温度配置信息成功！");
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.main_activity_menu, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		submit();
		return true;
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (root == null || root.findFocus() == null)
			return;

		switch (root.findFocus().getId()) {
		case R.id.tc_max_temp:

			String newMax = ShowUtil.isEmpty(s.toString()) ? tempMax.getHint()
					.toString() : s.toString();
			String currMaxNor = ShowUtil.isEmpty(tempNorMax.getText().toString()) ? tempNorMax.getHint()
					.toString() : tempNorMax.getText().toString();

			maxTempDescr.setText(String.format(getString(R.string.max_temp_descr), newMax));
			maxNorTempDescr.setText(String.format(getString(
					R.string.max_nor_temp_descr), currMaxNor, newMax));
			break;
		case R.id.tc_max_nor_temp:

			String newMaxNor = ShowUtil.isEmpty(s.toString()) ? tempNorMax
					.getHint().toString() : s.toString();
			String currMax = ShowUtil.isEmpty(tempMax.getText().toString()) ? tempMax
					.getHint().toString() : tempMax.getText().toString();
			String currMinNor = ShowUtil.isEmpty(tempNorMin.getText().toString()) ? tempNorMin
					.getHint().toString() : tempNorMin.getText().toString();

			maxNorTempDescr.setText(String.format(getString(
					R.string.max_nor_temp_descr), newMaxNor, currMax));
			minNorTempDescr.setText(String.format(getString(
					R.string.min_nor_temp_descr), currMinNor, newMaxNor));
			break;
		case R.id.tc_min_nor_temp:

			String newMinNor = ShowUtil.isEmpty(s.toString()) ? tempNorMin
					.getHint().toString() : s.toString();
			String currMaxNor2 = ShowUtil.isEmpty(tempNorMax.getText() .toString()) 
					? tempNorMax.getHint().toString() : tempNorMax.getText().toString();
			String currMin = ShowUtil.isEmpty(tempMin.getText() .toString()) 
					? tempMin.getHint().toString() : tempMin.getText().toString();

			minNorTempDescr.setText(String.format(getString(
					R.string.min_nor_temp_descr), newMinNor, currMaxNor2));
			minTempDescr.setText(String.format(
					getString(R.string.min_temp_descr), currMin, newMinNor));
			break;
		case R.id.tc_min_temp:

			String newMin = ShowUtil.isEmpty(s.toString()) ? tempMin.getHint()
					.toString() : s.toString();
			String currMinNor2 = ShowUtil.isEmpty(tempNorMin.getText().toString()) 
					? tempNorMin.getHint().toString() : tempNorMin.getText().toString();

			minTempDescr.setText(String.format(
					getString(R.string.min_temp_descr), newMin, currMinNor2));
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		refreshData();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

	@Override
	public void onToggle(boolean on) {
		
		int show = on ? View.VISIBLE : View.GONE;
		
		selectIdTitle.setVisibility(show);
		windowIdSpinner.setVisibility(show);
		
		PengInfo info = AppContext.context().getCurrPengInfo();
		info.setMoreMode(on);
		PengInfoDao.update(info);
		
		refreshData();
	}
}
