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
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.WaterInfoBean;
import com.smartfarm.db.bean.WaterInfo;
import com.smartfarm.db.util.WaterInfoDao;
import com.smartfarm.msg.MsgHelper;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.NetCheckWork;
import com.smartfarm.view.util.NetManager;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.WaterItem;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.ShareHelper;
import com.smartfarm.widget.togglebutton.ToggleButton;
import com.smartfarm.widget.togglebutton.ToggleButton.OnToggleChanged;

public class WaterFragment extends BaseFragment implements EventHandler {
	
	private WaterItem[] waterItems;
	private CheckBox[] waterChecks;
	private ToggleButton mSwitch;
	private ToggleButton rumpSwitch;
	private ToggleButton yaoSwitch;
	private ToggleButton onekeySwitch;
	private TextView yaoState;
	private TextView rumpState;
	private TextView onekeyState;
	private TextView controlWay;
	private TextView tip;
	private TextView showLinkInfo;
	private TextView currTime;
	private View checkLinear;
	private View checkLinear2;
	private View waterLinear;
	private View waterLinear2;

	private boolean first = true;
	private boolean currNetState = false;
	
	private WaterInfo currInfo;
	
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		if(view == null) {
			
			view = inflater.inflate(R.layout.fragment_water, container, false);

			initView(view);
		}
		
		initData();
		
		return view;
	}

	@Override
	public void initView(View view) {

		waterItems = new WaterItem[9];
		waterItems[Constants.WATER_ID_1] = new WaterItem(
				Constants.WATER_ID_1, view.findViewById(R.id.water_item1));
		waterItems[Constants.WATER_ID_2] = new WaterItem(
				Constants.WATER_ID_2, view.findViewById(R.id.water_item2));
		waterItems[Constants.WATER_ID_3] = new WaterItem(
				Constants.WATER_ID_3, view.findViewById(R.id.water_item3));
		waterItems[Constants.WATER_ID_4] = new WaterItem(
				Constants.WATER_ID_4, view.findViewById(R.id.water_item4));
		waterItems[Constants.WATER_ID_5] = new WaterItem(
				Constants.WATER_ID_5, view.findViewById(R.id.water_item5));
		waterItems[Constants.WATER_ID_6] = new WaterItem(
				Constants.WATER_ID_6, view.findViewById(R.id.water_item6));
		waterItems[Constants.WATER_ID_7] = new WaterItem(
				Constants.WATER_ID_7, view.findViewById(R.id.water_item7));
		waterItems[Constants.WATER_ID_8] = new WaterItem(
				Constants.WATER_ID_8, view.findViewById(R.id.water_item8));
		waterItems[Constants.WATER_ID_9] = new WaterItem(
				Constants.WATER_ID_9, view.findViewById(R.id.water_item9));
		
		waterChecks = new CheckBox[9];
		waterChecks[Constants.WATER_ID_1] = (CheckBox) view.findViewById(R.id.water_checkbox_w1);
		waterChecks[Constants.WATER_ID_2] = (CheckBox) view.findViewById(R.id.water_checkbox_w2);
		waterChecks[Constants.WATER_ID_3] = (CheckBox) view.findViewById(R.id.water_checkbox_w3);
		waterChecks[Constants.WATER_ID_4] = (CheckBox) view.findViewById(R.id.water_checkbox_w4);
		waterChecks[Constants.WATER_ID_5] = (CheckBox) view.findViewById(R.id.water_checkbox_w5);
		waterChecks[Constants.WATER_ID_6] = (CheckBox) view.findViewById(R.id.water_checkbox_w6);
		waterChecks[Constants.WATER_ID_7] = (CheckBox) view.findViewById(R.id.water_checkbox_w7);
		waterChecks[Constants.WATER_ID_8] = (CheckBox) view.findViewById(R.id.water_checkbox_w8);
		waterChecks[Constants.WATER_ID_9] = (CheckBox) view.findViewById(R.id.water_checkbox_w9);
		
		tip = (TextView) view.findViewById(R.id.water_tip);
		mSwitch = (ToggleButton) view.findViewById(R.id.water_btn_switch);
		yaoSwitch = (ToggleButton) view.findViewById(R.id.water_yao_switch);
		rumpSwitch = (ToggleButton) view.findViewById(R.id.water_rump_switch);
		onekeySwitch=(ToggleButton) view.findViewById(R.id.water_onekey_switch);
		controlWay = (TextView) view.findViewById(R.id.water_text_control_way);
		yaoState = (TextView) view.findViewById(R.id.water_yao_state);
		rumpState = (TextView) view.findViewById(R.id.water_rump_state);
		onekeyState=(TextView) view.findViewById(R.id.water_onekey_state);
		showLinkInfo = (TextView) view.findViewById(R.id.water_text_link);
		currTime = (TextView) view.findViewById(R.id.water_curr_time);
		checkLinear = view.findViewById(R.id.water_ll_2);
		waterLinear = view.findViewById(R.id.water_ll2);
		checkLinear2 = view.findViewById(R.id.water_ll_3);
		waterLinear2 = view.findViewById(R.id.water_ll3);

		view.findViewById(R.id.water_rump_rl).setOnClickListener(this);
		view.findViewById(R.id.water_yao_rl).setOnClickListener(this);
		view.findViewById(R.id.water_onekey_rl).setOnClickListener(this);
		view.findViewById(R.id.water_mode_rl).setOnClickListener(this);
		view.findViewById(R.id.water_btn_link).setOnClickListener(this);
		view.findViewById(R.id.water_btn_diagnosis).setOnClickListener(this);
		view.findViewById(R.id.water_btn_close).setOnClickListener(this);
		view.findViewById(R.id.water_btn_open).setOnClickListener(this);
		view.findViewById(R.id.water_btn_load).setOnClickListener(this);
		
		rumpSwitch.setClickChange(false);
		rumpSwitch.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(final boolean on) {
				CommonDialog dialog = new CommonDialog(getActivity());
				dialog.setTitle(R.string.sure);
				dialog.setMessage(!on ? "确认需要关闭水泵？" : "确认需要打开水泵？");

				dialog.setPositiveButton("确定",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								if(!on)
									ProtocolFactory.getWaterRumpCloseProtocol().send();
								else
									ProtocolFactory.getWaterRumpOpenProtocol().send();
								
								dialog.dismiss();
							}
						});

				dialog.setNegativeButton("取消", null);
				dialog.show();
			}
		});
		
		yaoSwitch.setClickChange(false);
		yaoSwitch.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(final boolean on) {
				CommonDialog dialog = new CommonDialog(getActivity());
				dialog.setTitle(R.string.sure);
				dialog.setMessage(!on ? "确认需要关闭药泵？" : "确认需要打开药泵？");

				dialog.setPositiveButton("确定",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								if(!on)
									ProtocolFactory.getWaterYaoCloseProtocol().send();
								else
									ProtocolFactory.getWaterYaoOpenProtocol().send();
								
								dialog.dismiss();
							}
						});

				dialog.setNegativeButton("取消", null);
				dialog.show();
			}
		});
		
		onekeySwitch.setClickChange(false);
		onekeySwitch.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(final boolean on) {
				CommonDialog dialog = new CommonDialog(getActivity());
				dialog.setTitle(R.string.sure);
				dialog.setMessage(!on ? "确认要取消一键灌水？" : "确认需要进行一键灌水？");

				dialog.setPositiveButton("确定",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								if(!on)
									ProtocolFactory.getWaterOnekeyCloseProtocol().send();
								else
									ProtocolFactory.getWaterOnekeyOpenProtocol().send();
								
								dialog.dismiss();
							}
						});

				dialog.setNegativeButton("取消", null);
				dialog.show();
			}
		});
		
		mSwitch.setClickChange(false);
		mSwitch.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(final boolean on) {
				CommonDialog dialog = new CommonDialog(getActivity());
				dialog.setTitle(on ? R.string.sure_to_auto : R.string.sure_to_manual);
				dialog.setMessage(on ? R.string.to_water_auto_prompt : R.string.to_water_manual_prompt);

				dialog.setPositiveButton("确定",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								ProtocolFactory.getSetWaterModeProtocol(on).send();
								dialog.dismiss();
							}
						});

				dialog.setNegativeButton("取消", null);
				dialog.show();
			}
		});
	}
	
	@Override
	public void initData() {

		refreshYaoState(false);
		refreshRumpState(false);
		refreshOnekeyState(false);
		
		ProtocolFactory.getWaterInfoProtocol(false).send();
		super.initData();
	}



	private String getChecked() {
		
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < waterChecks.length; i++)
			if (waterChecks[i].isChecked() && waterChecks[i].getVisibility() == View.VISIBLE) {

				result.append(i);
				result.append(";");
			}
		
		return result.toString();
	}
	
	public void refreshRumpState(boolean state) {
		
		if(state) {

			rumpSwitch.setToggleOn();
			rumpState.setText(R.string.rump_state_open);
		} else {
			
			rumpSwitch.setToggleOff();
			rumpState.setText(R.string.rump_state_close);
		}
	}
	
	public void refreshYaoState(boolean state) {
		
		if(state) {

			yaoSwitch.setToggleOn();
			yaoState.setText(R.string.yao_state_open);
		} else {
			
			yaoSwitch.setToggleOff();
			yaoState.setText(R.string.yao_state_close);
		}
	}
	private void refreshOnekeyState(boolean state) {
		
		if(state) {
			onekeySwitch.setToggleOn();
			onekeyState.setText(R.string.onekey_state_open);
		} else {
			
			onekeySwitch.setToggleOff();
			onekeyState.setText(R.string.onekey_state_close);
		}
	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId()) {

		case R.id.water_mode_rl:
			
			mSwitch.toggle();
			break;
		case R.id.water_rump_rl:
			
			rumpSwitch.toggle();
			break;
		case R.id.water_yao_rl:
			
			yaoSwitch.toggle();
			break;
		case R.id.water_onekey_rl:
			
			onekeySwitch.toggle();
			break;
		case R.id.water_btn_diagnosis:
			
			NetCheckWork.showCheckDialog(getActivity());
			break;
		case R.id.water_btn_open:
			
			String open = getChecked();
			
			if (!ShowUtil.isEmpty(open))
				ProtocolFactory.getWaterOpenProtocol(open).send();
			else
				ToastTool.showToast(R.string.please_selected_close_water);
			
			break;
		case R.id.water_btn_close:
			
			String close = getChecked();

			if (!ShowUtil.isEmpty(close))
				ProtocolFactory.getWaterCloseProtocol(close).send();
			else
				ToastTool.showToast(R.string.please_selected_close_water);
			break;
		case R.id.water_btn_link:
			
			if(AppContext.context().getAccountManager().isLogined())
				MsgHelper.get().relink(getActivity());
			break;
		case R.id.water_btn_load:
			
			ToastTool.showToast("正在读取湿度信息...");
			ProtocolFactory.getWaterInfoProtocol(true).send();
			break;
		}
	}
	
	private void modeChange() {
		
		currInfo = AppContext.context().getWaterInfo();
		boolean mode = currInfo.isWaterMode();
		
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
		
		currInfo = AppContext.context().getWaterInfo();
		boolean mode = currInfo.isWaterMode();
		
		if(AppContext.context().isNightMode()) 
			tip.setVisibility(View.VISIBLE);
		else
			tip.setVisibility(View.GONE);
		
		if (mode)
			mSwitch.setToggleOn();
		else
			mSwitch.setToggleOff();

		controlWay.setText(mode ? R.string.mode_auto : R.string.mode_manually);

		refreshNetState();

		int len = currInfo.getWaterCount();

		for (int i = 0; i < 9; i++) {
			if (i < len) {
				waterItems[i].setVisibility(View.VISIBLE);
				waterChecks[i].setVisibility(View.VISIBLE);
			} else {
				waterItems[i].setVisibility(View.INVISIBLE);
				waterChecks[i].setVisibility(View.INVISIBLE);
			}

			if (first)
				waterChecks[i].setChecked(true);
		}

		first = false;
		if (len <= 3) {
			waterLinear.setVisibility(View.GONE);
			checkLinear.setVisibility(View.GONE);
		} else {
			checkLinear.setVisibility(View.VISIBLE);
			waterLinear.setVisibility(View.VISIBLE);
		}

		if (len > 6) {
			waterLinear2.setVisibility(View.VISIBLE);
			checkLinear2.setVisibility(View.VISIBLE);
		} else {
			checkLinear2.setVisibility(View.GONE);
			waterLinear2.setVisibility(View.GONE);
		}
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
			UIHelper.showSimpleBack(getActivity(), BackPage.WATER_SETTING);
		
		return true;
	}

	@Override
	public void onEvent(LocalEvent event) {
		
		if (event.getEventType() == LocalEvent.EVENT_TYPE_NET_OK || 
				event.getEventType() == LocalEvent.EVENT_TYPE_NET_DOWN) {
			
			refreshNetState();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_WATER_MODE) {
			
			modeChange();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_WATER_READ) {
			
			WaterInfoBean bean = (WaterInfoBean) event.getEventData();

			refreshYaoState(bean.isYao());
			refreshRumpState(bean.isRump());
			refreshOnekeyState(bean.isOnekey());
			
			currInfo.setWaterMode(bean.isMode());
			WaterInfoDao.update(currInfo);
			
			modeChange();
			currTime.setVisibility(View.VISIBLE);
			currTime.setText("上次更新时间:" + CommonTool.getDateString(new Date(bean.getTime())));
			
			int[] values = bean.getValues();
			boolean[] states = bean.getStates();
			float[] temps = bean.getTemps();
			for(int i = 0; i < 9; i++) {
				waterItems[i].setValue(values[0]);
				waterItems[i].setState(states[i]);
				waterItems[i].setTemp(temps[0]);;
			}
		}
	}
}
