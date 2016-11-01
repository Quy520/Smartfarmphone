package com.smartfarm.fragment;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.OtherInfo;
import com.smartfarm.bean.StateModel;
import com.smartfarm.bean.TempBean;
import com.smartfarm.bean.WaterInfoBean;
import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.db.bean.LightInfo;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.bean.WaterInfo;
import com.smartfarm.db.util.InfoRecordDao;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.CommunityActivity;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ConfigManager;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.IconTextView;

@SuppressLint("UseSparseArrays") 
public class MainStateFragment extends BaseFragment 
		implements EventHandler, SwipeRefreshLayout.OnRefreshListener {

	private static final String TEMP_VALUE_PATTERN = "%d°/%d°";
	private static final String WATER_VALUE_PATTERN = "%d°/%d";
	private static final String LIGHT_VALUE_PATTERN = "%d/%d";
	
	private PengInfo info;
	
	private StateModel[] stateBeans;
	private Map<Integer, Long> pengLastTime = new HashMap<Integer, Long>();

	private long lastSend = 0;
	private CacheTask mCacheTask;
	private SendCheckRunable sendCheck;

	private TextView msgTip;
	private LinearLayout msgContiner;
	private SwipeRefreshLayout refreshLayout;
	private List<MsgBean> receiveMsgs = new LinkedList<MainStateFragment.MsgBean>();
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_main_state;
	}

	@Override
	public void initView(View view) {

		msgTip = (TextView) view.findViewById(R.id.ms_part_tip);
		msgContiner = (LinearLayout) view.findViewById(R.id.ms_part_continer);
		
		stateBeans = new StateModel[4];
		stateBeans[StateModel.TYPE_TEMP] = new StateModel(view.findViewById(R.id.ms_part_temp), StateModel.TYPE_TEMP);
		stateBeans[StateModel.TYPE_CO2] = new StateModel(view.findViewById(R.id.ms_part_co2), StateModel.TYPE_CO2);
		stateBeans[StateModel.TYPE_WATER] = new StateModel(view.findViewById(R.id.ms_part_water), StateModel.TYPE_WATER);
		stateBeans[StateModel.TYPE_LIGHT] = new StateModel(view.findViewById(R.id.ms_part_light), StateModel.TYPE_LIGHT);
		
		view.findViewById(R.id.ms_part_temp).setOnClickListener(this);
		view.findViewById(R.id.ms_part_co2).setOnClickListener(this);
		view.findViewById(R.id.ms_part_water).setOnClickListener(this);
		view.findViewById(R.id.ms_part_light).setOnClickListener(this);
		view.findViewById(R.id.ms_part_enter).setOnClickListener(this);
		
		if(msgContiner.getChildCount() == 0)
			msgTip.setVisibility(View.VISIBLE);
		
		refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
		refreshLayout.setOnRefreshListener(this);
		refreshLayout.setColorSchemeResources(
				R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getItemId() == R.id.refresh) {
    		requestData(true);
    		ToastTool.showToast("正在刷新数据，请稍候！");
    		setSwipeRefreshLoadedState();
    		return true;
    	}
    	
        return super.onOptionsItemSelected(item);
    }

	/** 设置顶部正在加载的状态 */
	protected void setSwipeRefreshLoadingState() {
		if (refreshLayout != null) {
			refreshLayout.setRefreshing(true);
			// 防止多次重复刷新
			refreshLayout.setEnabled(false);
		}
	}

	/** 设置顶部加载完毕的状态 */
	protected void setSwipeRefreshLoadedState() {
		if (refreshLayout != null) {
			refreshLayout.setRefreshing(false);
			refreshLayout.setEnabled(true);
		}
	}
	
	public void requestData(boolean refresh) {
		
		if(isReadDb() && !refresh) {
			readDbAll();
			return;
		}
		
		readAllByPad(refresh);
	}
	
	private void executeLoadDbSuccess(TempBean temp, OtherInfo other, WaterInfoBean water) {
		
		setTemp(temp);
		setLight(other);
		setWater(water);

		stateBeans[StateModel.TYPE_TEMP].loadSuccess();
		stateBeans[StateModel.TYPE_CO2].loadSuccess();
		stateBeans[StateModel.TYPE_WATER].loadSuccess();
		stateBeans[StateModel.TYPE_LIGHT].loadSuccess();
	}
	
	private void readAllByPad(boolean refresh) {
		
		if(lastSend > System.currentTimeMillis() - 2 * 60 * 1000 && !refresh)
			return;

		lastSend = System.currentTimeMillis();
		stateBeans[StateModel.TYPE_TEMP].showProgress();
		stateBeans[StateModel.TYPE_CO2].showProgress();
		stateBeans[StateModel.TYPE_WATER].showProgress();
		stateBeans[StateModel.TYPE_LIGHT].showProgress();
		
		sendCheck = new SendCheckRunable();
		sendCheck.execute();
	}
	
	private void readDbAll() {
		
		if (mCacheTask != null) {
			mCacheTask.cancel(true);
			mCacheTask = null;
		}
		
		stateBeans[StateModel.TYPE_TEMP].showProgress();
		stateBeans[StateModel.TYPE_CO2].showProgress();
		stateBeans[StateModel.TYPE_WATER].showProgress();
		stateBeans[StateModel.TYPE_LIGHT].showProgress();
		
		mCacheTask = new CacheTask();
		mCacheTask.execute(info.getId());
	}
	
	private boolean isReadDb() {
		
		if(!pengLastTime.containsKey(info.getId()))
			return false;
		
		if(pengLastTime.get(info.getId()) < System.currentTimeMillis() - 8 * 60 * 1000)
			return false;
		
		return true;
	} 

	private class CacheTask extends AsyncTask<Integer, Void, Void> {
		
		private OtherInfo light = null;
		private WaterInfoBean water = null;
		private TempBean temp = null;

		@Override
		protected Void doInBackground(Integer... params) {
			
			int pengId = params[0];
			
			InfoRecord tempRecord = InfoRecordDao.findLast(InfoRecord.RECORD_TYPE_TEMP, pengId);
			if(tempRecord != null)
				temp = CommonTool.resolveTemp(tempRecord.getData(), pengId, tempRecord.getTime());
			
			InfoRecord lightRecord = InfoRecordDao.findLast(InfoRecord.RECORD_TYPE_OTHER, pengId);
			if(lightRecord != null)
				light = new OtherInfo(lightRecord);
			
			InfoRecord waterRecord = InfoRecordDao.findLast(InfoRecord.RECORD_TYPE_WATER, pengId);
			if(waterRecord != null)
				water = new WaterInfoBean(waterRecord);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			super.onPostExecute(param);
			
			executeLoadDbSuccess(temp, light, water);
		}
	}
	
	private void setTemp(TempBean bean) {

		stateBeans[StateModel.TYPE_TEMP].setMode(info.isAutoMode());
		
		if(bean == null) {
			
			stateBeans[StateModel.TYPE_TEMP].setValue("0°/0°");
			stateBeans[StateModel.TYPE_TEMP].setTip("温度过低", false);
			stateBeans[StateModel.TYPE_TEMP].setLastTime(System.currentTimeMillis());
		} else if(bean.getId() == info.getId()) {

			stateBeans[StateModel.TYPE_TEMP].setValue(String.format(
					TEMP_VALUE_PATTERN, bean.getMinTemp(), bean.getMaxTemp()));
			
			if(bean.getMaxTemp() > info.getAlarmTempMax())
				stateBeans[StateModel.TYPE_TEMP].setTip("温度过高,注意开风透透气哦！", false);
			else if(bean.getMinTemp() < info.getAlarmTempMin())
				stateBeans[StateModel.TYPE_TEMP].setTip("温度过低,注意保护蔬菜哦！", false);
			else
				stateBeans[StateModel.TYPE_TEMP].setTip("温度刚刚好，注意保持哦！", true);

			stateBeans[StateModel.TYPE_TEMP].setLastTime(bean.getTime());
		}
	}
	
	public void setLight(OtherInfo bean) {
		
		LightInfo lightInfo = AppContext.context().getLightInfo();
		stateBeans[StateModel.TYPE_CO2].setMode(lightInfo.isMode());
		stateBeans[StateModel.TYPE_LIGHT].setMode(lightInfo.isMode());
		
		if(bean == null) {

			stateBeans[StateModel.TYPE_CO2].setValue("0/0");
			stateBeans[StateModel.TYPE_CO2].setTip("空气相当的干燥啊！", false);
			stateBeans[StateModel.TYPE_CO2].setLastTime(System.currentTimeMillis());

			stateBeans[StateModel.TYPE_LIGHT].setValue("0");
			stateBeans[StateModel.TYPE_LIGHT].setTip("可见度较低", false);
			stateBeans[StateModel.TYPE_LIGHT].setLastTime(System.currentTimeMillis());
		} else {
			
			stateBeans[StateModel.TYPE_CO2].setValue(
					String.format(LIGHT_VALUE_PATTERN, Math.round(bean.getHumidity()), bean.getCO2()));
			stateBeans[StateModel.TYPE_CO2].setLastTime(bean.getTime());

			stateBeans[StateModel.TYPE_LIGHT].setValue(String.valueOf(bean.getLight()));
			stateBeans[StateModel.TYPE_LIGHT].setLastTime(bean.getTime());
			
			if(bean.getHumidity() < 40)
				stateBeans[StateModel.TYPE_CO2].setTip("空气相当的干燥啊！", false);
			else 
				stateBeans[StateModel.TYPE_CO2].setTip("空气十分舒适，继续保持哦！", true);
			
			if(bean.getLight() < 100)
				stateBeans[StateModel.TYPE_LIGHT].setTip("天色渐暗,记得查看下大棚的状况哦！", true);
			else 
				stateBeans[StateModel.TYPE_LIGHT].setTip("大棚内还挺亮的！", true);
		}
	}
	
	public void setWater(WaterInfoBean bean) {

		WaterInfo waterInfo = AppContext.context().getWaterInfo();
		stateBeans[StateModel.TYPE_WATER].setMode(waterInfo.isWaterMode());
		
		if(bean == null) {

			stateBeans[StateModel.TYPE_WATER].setValue("0°/0");
			stateBeans[StateModel.TYPE_WATER].setTip("土壤温度过低,小心冻坏植物哦！", false);
			stateBeans[StateModel.TYPE_WATER].setLastTime(System.currentTimeMillis());
		} else {
			
			float temp = bean.getTemps()[0];
			int humidity = bean.getValues()[0];
			stateBeans[StateModel.TYPE_WATER].setValue(String.format(
					WATER_VALUE_PATTERN, Math.round(temp), humidity));
			
			if(temp > 30)
				stateBeans[StateModel.TYPE_WATER].setTip("土壤温度过热,小心热坏植物哦！", false);
			else if(temp < 5)
				stateBeans[StateModel.TYPE_WATER].setTip("土壤温度过低,小心冻坏植物哦！", false);
			else if(humidity < 40) 
				stateBeans[StateModel.TYPE_WATER].setTip("土壤干巴巴的,来浇点水怎么样？", false);
			else
				stateBeans[StateModel.TYPE_WATER].setTip("土壤状况非常良好，继续保持哦！", true);
			
			stateBeans[StateModel.TYPE_WATER].setLastTime(bean.getTime());
		}
	}

	@Override
	public void onResume() {
		
		super.onResume();
		refreshData();
	}

	public void refreshData() {

//		boolean load = first || lastRefresh < System.currentTimeMillis() - 5 * 60 * 1000;
//		
//		first = false;
//		lastRefresh = System.currentTimeMillis();
		
		info = AppContext.context().getCurrPengInfo();
		
		getActivity().getActionBar().setTitle(info.getName());
		
		setTemp(null);

		if(ConfigManager.getInstance().getBoolean(ConfigManager.KEY_ENABLE_LIGHT)) {
			
			stateBeans[StateModel.TYPE_CO2].setVisibility(View.VISIBLE);
			stateBeans[StateModel.TYPE_LIGHT].setVisibility(View.VISIBLE);		
		} else {
			
			stateBeans[StateModel.TYPE_CO2].setVisibility(View.GONE);
			stateBeans[StateModel.TYPE_LIGHT].setVisibility(View.GONE);
		}
		
		if(ConfigManager.getInstance().getBoolean(ConfigManager.KEY_ENABLE_WATER)) {

			stateBeans[StateModel.TYPE_WATER].setVisibility(View.VISIBLE);
			setWater(null);
		} else {
			stateBeans[StateModel.TYPE_WATER].setVisibility(View.GONE);
		}
		
		requestData(false);
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		
		case R.id.ms_part_temp:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.TEMP_CONTROL);
			break;
		case R.id.ms_part_co2:

			UIHelper.showSimpleBack(getActivity(), BackPage.LIGHT);
			break;
		case R.id.ms_part_water:

			UIHelper.showSimpleBack(getActivity(), BackPage.WATER);
			break;
		case R.id.ms_part_light:

			UIHelper.showSimpleBack(getActivity(), BackPage.LIGHT);
			break;
		case R.id.ms_part_enter:

			if(AppContext.context().isLogin()) {
				Intent intent = new Intent(getActivity(), CommunityActivity.class);
		        getActivity().startActivity(intent);
			} else 
				UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
			break;
		}
	}

	@SuppressLint("InflateParams") 
	private void receiveMsg(String content, long time) {
		
		MsgBean msg = new MsgBean(content, time);
		msgTip.setVisibility(View.GONE);
		receiveMsgs.add(msg);
		
		ViewHolder holder = null;

		if(msgContiner.getChildCount() < 5) {
			
			View item = mInflater.inflate(R.layout.list_cell_message, null);
			
			holder = new ViewHolder();
			holder.time = (TextView) item.findViewById(R.id.msg_tv_time);
			holder.content = (TextView) item.findViewById(R.id.msg_tv_content);
			holder.icon = (IconTextView) item.findViewById(R.id.itv_icon); 
			
			item.setTag(holder);
			msgContiner.addView(item, 0);
		} else {
			
			View item = msgContiner.getChildAt(4);
			msgContiner.removeView(item);
			
			holder = (ViewHolder) item.getTag();
			msgContiner.addView(item, 0);
		}
		
		holder.time.setText("接收时间" + CommonTool.getDateString(new Date(time)));
//		holder.icon.setText(iconRes);
		holder.content.setText(content);
	}
	
	class ViewHolder {
		
		TextView content;
		TextView time;
		IconTextView icon;
	}
	
	@Override
	public void onEvent(LocalEvent event) {

		WaterInfo waterInfo = AppContext.context().getWaterInfo();
		
		switch(event.getEventType()) {
		
		case LocalEvent.EVENT_TYPE_WATER_READ:

			WaterInfoBean bean = (WaterInfoBean) event.getEventData();
			
			if(bean.getId() != info.getId())
				return;
			
			setWater(bean);
			
			if(sendCheck != null) {
				sendCheck.receiveWater();
				stateBeans[StateModel.TYPE_WATER].loadSuccess();
			}
			
			receiveMsg(CommonTool.getWaterString(bean.getSourceData()), bean.getTime());
			break;
		case LocalEvent.EVENT_TYPE_TEMP:
			
			TempBean tempBean = (TempBean) event.getEventData();
			if(info.getId() == tempBean.getId()) {
				setTemp(tempBean);
				pengLastTime.put(info.getId(), tempBean.getTime());
				
				if(sendCheck != null) {
					sendCheck.receiveTemp();
					stateBeans[StateModel.TYPE_TEMP].loadSuccess();
				}
				receiveMsg(CommonTool.getTempString(tempBean.getSourceData()), tempBean.getTime());
			}
			break;
		case LocalEvent.EVENT_TYPE_PENG_CHANGE: 
		case LocalEvent.EVENT_TYPE_PENG_SWITCHING:

			refreshData();
			break;
		case LocalEvent.EVENT_TYPE_MODE_CHANGE: 

			stateBeans[StateModel.TYPE_TEMP].setMode(info.isAutoMode());
			break;
		case LocalEvent.EVENT_TYPE_LIGHT_MODE: 

			LightInfo lightInfo = AppContext.context().getLightInfo();
			stateBeans[StateModel.TYPE_CO2].setMode(lightInfo.isMode());
			stateBeans[StateModel.TYPE_LIGHT].setMode(lightInfo.isMode());
			break;
		case LocalEvent.EVENT_TYPE_WATER_MODE: 

			stateBeans[StateModel.TYPE_WATER].setMode(waterInfo.isWaterMode());
			break;
		case LocalEvent.EVENT_TYPE_OTHER_READ: 
			
			OtherInfo lightBean = (OtherInfo) event.getEventData();
			if(info.getId() == lightBean.getId()) {

				setLight(lightBean);
				if(sendCheck != null) {
					sendCheck.receiveLight();
					stateBeans[StateModel.TYPE_LIGHT].loadSuccess();
					stateBeans[StateModel.TYPE_CO2].loadSuccess();
				}
				receiveMsg(getLightString(lightBean), lightBean.getTime());
			}
			
			break;
		}
	}
	
	private String getLightString(OtherInfo lightBean) {
		
		StringBuilder res = new StringBuilder();
		
		res.append("当前照度为:");
		res.append(lightBean.getLight());
		res.append("Lux，当前二氧化碳含量为:");
		res.append(lightBean.getCO2());
		res.append("PPM，当前空气湿度为:");
		res.append(lightBean.getHumidity());
		res.append("RH。");
		
		return res.toString();
	}
	
	static class MsgBean {
		final String content;
		final long time;
		
		public MsgBean(String content, long time) {
			this.content = content;
			this.time = time;
		}
	}
	
	class SendCheckRunable implements Runnable {
		
		private boolean temp = false, light = false, water = false;
		
		public void execute() {
			ProtocolFactory.GetReadTempsProtocol().send();
			
			if(ConfigManager.getInstance().getBoolean(ConfigManager.KEY_ENABLE_WATER))
				ProtocolFactory.getWaterInfoProtocol(false).send();
			else
				water = true;

			if(ConfigManager.getInstance().getBoolean(ConfigManager.KEY_ENABLE_WATER))
				ProtocolFactory.getReadOtherInfoProtocol(false).send();
			else
				light = true;
			
			new Handler().postDelayed(this, 30 * 1000);
		}
		
		@Override
		public void run() {
			
			if(!temp) 
				stateBeans[StateModel.TYPE_TEMP].loadFaild("温控机未能及时响应温度请求");
			
			if(!water) 
				stateBeans[StateModel.TYPE_WATER].loadFaild("温控机未能及时响应湿度请求");
			
			if(!light) {
				stateBeans[StateModel.TYPE_LIGHT].loadFaild("温控机未能及时响应照度请求");
				stateBeans[StateModel.TYPE_CO2].loadFaild("温控机未能及时响应湿度及二氧化碳含量请求");
			}
			
			sendCheck = null;
		}
		
		public void receiveTemp() {
			temp = true;
		}
		
		public void receiveLight() {
			light = true;
		}
		
		public void receiveWater() {
			water = true;
		}
	}

	@Override
	public void onRefresh() {
		
		requestData(true);
		ToastTool.showToast("正在刷新数据，请稍候！");
		setSwipeRefreshLoadedState();
	}
}
