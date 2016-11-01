package com.smartfarm.view.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.NetBean;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.msg.Protocol;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;

public class NetManager implements EventHandler {
	
	public static final int CHECK_MSG = 100;
	public static final int CHECK_NET = CHECK_MSG + 1;
	
	private static Handler netMsgHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			
			switch(msg.what) {
			case CHECK_MSG:
				
				NetManager.getInstence().checkDelayMsg((Protocol)msg.obj);
				break;
			case CHECK_NET:
				
				NetManager.getInstence().check();
				sendEmptyMessageDelayed(CHECK_NET, Constants.CHECK_NET_PERIOD);
				break;
			}
			
			super.dispatchMessage(msg);
		}
		
	};
	
	private static NetManager netManager = new NetManager();

	public static NetManager getInstence() {
		return netManager;
	}
	
	private Vector<NetBean> netInfo;
	private boolean netOk = false;
	private List<Protocol> uncheckedMsgs;
	private Map<Integer, Integer> okFlag;
	private OnNetErrorListener listener;
	
	{
		EventBus.getDefault().add(this);
	}
	
	public void setOnNetErrorLintener(OnNetErrorListener listener) {
		this.listener = listener;
	}
	
	@SuppressLint("UseSparseArrays")
	private NetManager() {
		
		netInfo = new Vector<NetBean>();
		uncheckedMsgs = new ArrayList<Protocol>();
		okFlag = new HashMap<Integer, Integer>();
		
		resetNetInfo();
		
		netMsgHandler.removeCallbacksAndMessages(null);
		netMsgHandler.sendEmptyMessageDelayed(CHECK_NET, Constants.CHECK_NET_PERIOD);
	}
	
	public void resetNetInfo() {
		
		netInfo.clear();
		
		List<PengInfo> allPeng = PengInfoDao.findAll();
		
		if(allPeng != null)
			for(PengInfo info : allPeng) 
				netInfo.add(new NetBean(info.getId()));
	}
	
	public void clearNetInfo() {

		netInfo.clear();
		
		for(int i = 0; i < netInfo.size(); i++) 
			netInfo.set(i, new NetBean());
	}
	
	public void receiveMsg(Protocol protocol) {
		
		netOk = true;
		
		if(uncheckedMsgs.contains(protocol)) {
			uncheckedMsgs.remove(protocol);
			okFlag.put(protocol.getPadId(), 10000);
		}

		boolean exist = false;
		for(int i = 0; i < netInfo.size(); i++) {
			
			NetBean bean = netInfo.get(i);
			
			if(bean.padId != protocol.getPadId()) 
				continue;
			
			netInfo.set(i, new NetBean(bean.padId));
			
			exist = true;
		}
		
		if(!exist) {

			netInfo.add(new NetBean(protocol.getPadId()));
		}
	}
	
	public boolean isNetOk() {
		return netOk;
	}
	
	public void setNetOk(boolean isNetOk) {
		netOk = isNetOk;
	}
	
	public NetBean getGhNetInfo(int ghId) {
		
		Iterator<NetBean> it = netInfo.iterator();
		
		while(it.hasNext()) {
			NetBean bean = it.next();
			if(bean.padId == ghId)
				return bean;
		}
		
		return new NetBean();
	}
	
	public void addCheckMsg(Protocol protocol) {
		
		uncheckedMsgs.add(protocol);
		
		Message msg = new Message();
		msg.what = CHECK_MSG;
		msg.obj = protocol;
		netMsgHandler.sendMessageDelayed(msg, 120000);
	}

	public void check() {
		long curr = System.currentTimeMillis();

		for(NetBean bean : netInfo) {
			long diff = curr - bean.time;

			if(diff > 480000) 
				ProtocolFactory.GetTestProtocol(bean.padId).send();
		}
	}
	
	public void checkDelayMsg(Protocol protocol) {
		if(uncheckedMsgs.remove(protocol)) {
			
			int id = protocol.getPadId();
			
			if(!okFlag.containsKey(id)) 
				okFlag.put(id, 10000);

			int before = okFlag.get(id) / 10000;
			
			okFlag.put(id, okFlag.get(id) + 1);
			
			if((okFlag.get(id) % 10000) >= (8 * before + 4) && listener != null) {
				listener.onNetError(id);
				
				okFlag.put(id, (before + 1) * 10000);
			}
		}
	}
	
	public interface OnNetErrorListener {
		
		public void onNetError(int dest);
	}

	@Override
	public void onEvent(LocalEvent event) {
		
		if(event.getEventType() == LocalEvent.EVENT_TYPE_RECEIVE_MSG) {
			receiveMsg((Protocol)event.getEventData());
		}
	}
}
