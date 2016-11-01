package com.smartfarm.msg;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.bean.User;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.util.NetManager;

public class MsgUtils {

	public static Protocol msgFilter(String msg) {
		
		Protocol protocol = ProtocolFactory.GetProtocol(msg);
		User user = AppContext.context().getUser();
		String phoneNum = user == null ? "" : user.getPhone();
		
		if (protocol == null)
			return null;

		if(protocol.getCmdType() == Constants.MOTOR_CONTROL_TYPE_SUPER_TEST) {
			
			protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
			protocol.setReceiver(protocol.getSender());
			protocol.setSender(phoneNum);
			protocol.send();
			return null;
		}
		
		if (protocol.getTime() < System.currentTimeMillis() - 30 * 60 * 1000)
			return null;

		if (phoneNum.equals("") || !protocol.getReceiver().contains(phoneNum))
			if (!protocol.getReceiver().equals(Constants.RECEIVER_ALL))
				return null;

		if(protocol.getSender().contains(phoneNum)) {
			
			NetManager.getInstence().receiveMsg(protocol);
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_NET_OK));
			
			return null;
		}
		
		PengInfo info = PengInfoDao.findByPhoneNum(protocol.getSender());

		if(info == null)
			return null;

		protocol.setInfo(info);
		protocol.setPadId(info.getId());
		
		return protocol;
	}
}
