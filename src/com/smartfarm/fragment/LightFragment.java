package com.smartfarm.fragment;

import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LightInfoBean;
import com.smartfarm.bean.LightItem;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.LightInfo;
import com.smartfarm.db.util.LightInfoDao;
import com.smartfarm.msg.MsgHelper;
import com.smartfarm.msg.Protocol;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.NetCheckWork;
import com.smartfarm.view.util.NetManager;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.ShareHelper;
import com.smartfarm.widget.togglebutton.ToggleButton;
import com.smartfarm.widget.togglebutton.ToggleButton.OnToggleChanged;

public class LightFragment extends BaseFragment implements EventHandler {
	
	private LightItem[] lightItems;
	private CheckBox[] lightChecks;
	private ToggleButton mSwitch;
	private TextView controlWay;
	private TextView currTime;
	private TextView tip;
	private TextView showLinkInfo;
	private View checkLinear;
	private View checkLinear2;
	private View lightLinear;
	private View lightLinear2;

	private boolean first = true;
	private boolean currNetState = false;
	
	private View view;
	
	private LightInfo info;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		if(view == null) {
			
			view = inflater.inflate(R.layout.fragment_light, container, false);

			initView(view);
		}
		
		initData();
		
		return view;
	}
	
	@Override
	public void initView(View view) {

		lightItems = new LightItem[9];
		lightItems[Constants.WATER_ID_1] = new LightItem(
				Constants.WATER_ID_1, (TextView) view.findViewById(R.id.light_item1));
		lightItems[Constants.WATER_ID_2] = new LightItem(
				Constants.WATER_ID_2, (TextView) view.findViewById(R.id.light_item2));
		lightItems[Constants.WATER_ID_3] = new LightItem(
				Constants.WATER_ID_3, (TextView) view.findViewById(R.id.light_item3));
		lightItems[Constants.WATER_ID_4] = new LightItem(
				Constants.WATER_ID_4, (TextView) view.findViewById(R.id.light_item4));
		lightItems[Constants.WATER_ID_5] = new LightItem(
				Constants.WATER_ID_5, (TextView) view.findViewById(R.id.light_item5));
		lightItems[Constants.WATER_ID_6] = new LightItem(
				Constants.WATER_ID_6, (TextView) view.findViewById(R.id.light_item6));
		lightItems[Constants.WATER_ID_7] = new LightItem(
				Constants.WATER_ID_7, (TextView) view.findViewById(R.id.light_item7));
		lightItems[Constants.WATER_ID_8] = new LightItem(
				Constants.WATER_ID_8, (TextView) view.findViewById(R.id.light_item8));
		lightItems[Constants.WATER_ID_9] = new LightItem(
				Constants.WATER_ID_9, (TextView) view.findViewById(R.id.light_item9));
		
		lightChecks = new CheckBox[9];
		lightChecks[Constants.WATER_ID_1] = (CheckBox) view.findViewById(R.id.light_checkbox_w1);
		lightChecks[Constants.WATER_ID_2] = (CheckBox) view.findViewById(R.id.light_checkbox_w2);
		lightChecks[Constants.WATER_ID_3] = (CheckBox) view.findViewById(R.id.light_checkbox_w3);
		lightChecks[Constants.WATER_ID_4] = (CheckBox) view.findViewById(R.id.light_checkbox_w4);
		lightChecks[Constants.WATER_ID_5] = (CheckBox) view.findViewById(R.id.light_checkbox_w5);
		lightChecks[Constants.WATER_ID_6] = (CheckBox) view.findViewById(R.id.light_checkbox_w6);
		lightChecks[Constants.WATER_ID_7] = (CheckBox) view.findViewById(R.id.light_checkbox_w7);
		lightChecks[Constants.WATER_ID_8] = (CheckBox) view.findViewById(R.id.light_checkbox_w8);
		lightChecks[Constants.WATER_ID_9] = (CheckBox) view.findViewById(R.id.light_checkbox_w9);
		
		tip = (TextView) view.findViewById(R.id.light_tip);
		mSwitch = (ToggleButton) view.findViewById(R.id.light_btn_switch);
		controlWay = (TextView) view.findViewById(R.id.light_text_control_way);
		showLinkInfo = (TextView) view.findViewById(R.id.light_text_link);
		currTime = (TextView) view.findViewById(R.id.light_curr_time);
		checkLinear = view.findViewById(R.id.light_ll_2);
		lightLinear = view.findViewById(R.id.light_ll2);
		checkLinear2 = view.findViewById(R.id.light_ll_3);
		lightLinear2 = view.findViewById(R.id.light_ll3);

		view.findViewById(R.id.light_mode_rl).setOnClickListener(this);
		view.findViewById(R.id.light_btn_link).setOnClickListener(this);
		view.findViewById(R.id.light_btn_diagnosis).setOnClickListener(this);
		view.findViewById(R.id.light_btn_close).setOnClickListener(this);
		view.findViewById(R.id.light_btn_open).setOnClickListener(this);
		view.findViewById(R.id.light_btn_load).setOnClickListener(this);
		
		mSwitch.setClickChange(false);
		mSwitch.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(final boolean on) {
				CommonDialog dialog = new CommonDialog(getActivity());
				dialog.setTitle(on ? R.string.sure_to_auto : R.string.sure_to_manual);
				dialog.setMessage(on ? R.string.to_light_auto_prompt : R.string.to_light_manual_prompt);

				dialog.setPositiveButton("确定",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								ProtocolFactory.getSetLightModeProtocol(on).send();
								dialog.dismiss();
							}
						});

				dialog.setNegativeButton("取消", null);
				dialog.show();
			}
		});
	}

	private String getChecked() {
		
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < lightChecks.length; i++)
			if (lightChecks[i].isChecked() && lightChecks[i].getVisibility() == View.VISIBLE) {

				result.append(i);
				result.append(";");
			}
		
		return result.toString();
	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId()) {

		case R.id.light_mode_rl:
			
			mSwitch.toggle();
			break;
		case R.id.light_btn_diagnosis:
			
			NetCheckWork.showCheckDialog(getActivity());
			break;
		case R.id.light_btn_open:
			
			String open = getChecked();

			if (!ShowUtil.isEmpty(open))
				ProtocolFactory.getLightOpenProtocol(open).send();
			else
				ToastTool.showToast(R.string.please_selected_open_light);
			break;
		case R.id.light_btn_close:
			
			String close = getChecked();

			if (!ShowUtil.isEmpty(close))
				ProtocolFactory.getLightCloseProtocol(close).send();
			else
				ToastTool.showToast(R.string.please_selected_close_light);
			break;
		case R.id.light_btn_link:
			
			if(AppContext.context().getAccountManager().isLogined())
				MsgHelper.get().relink(getActivity());
			break;
		case R.id.light_btn_load:
			
			ToastTool.showToast("正在读取照度信息...");
			ProtocolFactory.getReadLightInfoProtocol().send();
			break;
		}
	}
	
	private void modeChange() {
		
		info = AppContext.context().getLightInfo();
		boolean mode = info.isMode();
		
		if (mode)
			mSwitch.setToggleOn();
		else
			mSwitch.setToggleOff();

		controlWay.setText(mode ? R.string.mode_auto : R.string.mode_manually);
	}
	
	private void refreshNetState() {
		
		if(view == null)
			return;
		
		currNetState = NetManager.getInstence().isNetOk();
		showLinkInfo.setText(currNetState ? R.string.net_link_ok
						: R.string.net_link_error);
	}
	
	@Override
	public void onResume() {

		refreshView();
		
		super.onResume();
	}
	
	private void refreshView() {
		
		info = AppContext.context().getLightInfo();
		
		if(AppContext.context().isNightMode()) 
			tip.setVisibility(View.VISIBLE);
		else
			tip.setVisibility(View.GONE);
		
		modeChange();

		refreshNetState();

		int len = AppContext.context().getLightInfo().getCount();

		for (int i = 0; i < 9; i++) {
			if (i < len) {
				lightItems[i].setVisibility(View.VISIBLE);
				lightChecks[i].setVisibility(View.VISIBLE);
			} else {
				lightItems[i].setVisibility(View.INVISIBLE);
				lightChecks[i].setVisibility(View.INVISIBLE);
			}

			if (first)
				lightChecks[i].setChecked(true);
		}

		first = false;
		if (len <= 3) {
			lightLinear.setVisibility(View.GONE);
			checkLinear.setVisibility(View.GONE);
		} else {
			checkLinear.setVisibility(View.VISIBLE);
			lightLinear.setVisibility(View.VISIBLE);
		}

		if (len > 6) {
			lightLinear2.setVisibility(View.VISIBLE);
			checkLinear2.setVisibility(View.VISIBLE);
		} else {
			checkLinear2.setVisibility(View.GONE);
			lightLinear2.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void initData() {

		ProtocolFactory.getLightInfoProtocol().send();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.setting_menu, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.share)
			ShareHelper.handleShare(getActivity());
		else
			UIHelper.showSimpleBack(getActivity(), BackPage.LIGHT_SETTING);
		
		return true;
	}

	
	@Override
	public void onEvent(LocalEvent event) {
		
		if (event.getEventType() == LocalEvent.EVENT_TYPE_NET_OK || 
				event.getEventType() == LocalEvent.EVENT_TYPE_NET_DOWN) {
			
			refreshNetState();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_LIGHT_MODE) {
			
			modeChange();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_LIGHT_READ) {

			Protocol protocol = (Protocol) event.getEventData();
			LightInfoBean bean = new LightInfoBean(protocol);
			
			info.setMode(bean.getMode());
			LightInfoDao.update(info);
			
			modeChange();
			currTime.setVisibility(View.VISIBLE);
			currTime.setText("上次更新时间:" + CommonTool.getDateString(new Date(protocol.getTime())));
			
			boolean[] states = bean.getStates();
			for(int i = 0; i < 4; i++) {

				lightItems[i].setLight(bean.getValue());
				lightItems[i].setLightState(states[i]);
			}
		}
	}
}
