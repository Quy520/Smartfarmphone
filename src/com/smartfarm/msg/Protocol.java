package com.smartfarm.msg;

import java.util.Date;

import android.util.Log;

import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.util.NetManager;

public class Protocol {
	private long time = 0;
	private String sender = "";
	private String receiver = "";
	private int protocolType = 0;
	private int cmdType = 0;
	private String windowId = "null";
	private String data = "null";
	private String pwd = "";
	private int padId;
	private PengInfo info;

	public PengInfo getInfo() {
		return info;
	}

	public void setInfo(PengInfo info) {
		this.info = info;
	}

	public Protocol() {

		PengInfo info = AppContext.context().getCurrPengInfo();
		sender = AppContext.context().getAccountManager().getPhoneNum();
		receiver = info.getPhoneNum();
		padId = info.getId();
		pwd = info.getPwd();
	}

	public Protocol(int padId) {

		PengInfo info = PengInfoDao.findById(padId);

		if (info == null)
			return;

		sender = AppContext.context().getAccountManager().getPhoneNum();
		receiver = info.getPhoneNum();
		pwd = info.getPwd();
		this.padId = padId;

	}

	public void send() {

		if (receiver == null || receiver.equals("")) {

			EventBus.getDefault().runningOnUiThread(new Runnable() {

				@Override
				public void run() {

					PengInfo noNumPad = PengInfoDao.findById(padId);

					if (noNumPad != null)
						ToastTool.showToast("请设置(" + noNumPad.getName()
								+ ")的温控机号码!");
					else
						ToastTool.showToast("请设置温控机号码!");
				}
			});

			return;
		}

		MsgHelper.get().sendMsg(this, new MsgCallback() {

			@Override
			public void onSuccess() {

				Log.d(Constants.TAG, "send ok");

				if (getProtocolType() != Constants.PROTOCOL_TYPE_RESPONSE)
					NetManager.getInstence().addCheckMsg(Protocol.this);
			}

			@Override
			public void onFaild(int errorCode, String errorMsg) {

				String show = null;
				switch (getCmdType()) {
				case Constants.MOTOR_CONTROL_TYPE_CLOSE:
					show = "读取温度失败!";
					break;
				case Constants.MOTOR_CONTROL_TYPE_OPEN:
					show = "打开窗口失败!";
					break;
				case Constants.MOTOR_CONTROL_TYPE_STOP:
					show = "停止窗口失败!";
					break;
				case Constants.MOTOR_CONTROL_TYPE_READ:
					show = "读取温度失败!";
					break;
				}

				if (show != null)
					EventBus.getDefault().noticeMsg(show);
			}
		});
	}

	public String getProtocolString() {
		StringBuilder res = new StringBuilder();
		res.append(time);
		res.append("-");
		res.append(sender);
		res.append("-");
		res.append(receiver);
		res.append("-");
		res.append(protocolType);
		res.append("-");
		res.append(cmdType);
		res.append("-");
		res.append(windowId);
		res.append("-");
		res.append(data);
		res.append("-");
		res.append(pwd);

		return res.toString();
	}

	public int getPadId() {
		return padId;
	}

	public void setPadId(int padId) {
		this.padId = padId;
	}

	@Override
	public boolean equals(Object o) {

		if (Protocol.class.isInstance(o)) {
			Protocol other = (Protocol) o;

			if (other.getTime() == time)
				return true;
			else
				return false;

		} else
			return false;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	public int getCmdType() {
		return cmdType;
	}

	public void setCmdType(int cmdType) {
		this.cmdType = cmdType;
	}

	public String getWindowId() {
		return windowId;
	}

	public int[] getWindowIds() {
		String[] ids = windowId.split(";");

		int[] res = new int[ids.length];

		try {
			for (int i = 0; i < ids.length; i++) {
				res[i] = Integer.parseInt(ids[i]);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return res;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("温控机");

		if (cmdType == Constants.MOTOR_CONTROL_TYPE_ALARM)
			result.append(receiver);
		else
			result.append(sender);

		switch (protocolType) {
		case Constants.PROTOCOL_TYPE_REQUEST:
			switch (cmdType) {
			case Constants.MOTOR_CONTROL_TYPE_READ:
				result.append("推送温度:");
				result.append(data);
				break;
			case Constants.MOTOR_CONTROL_TYPE_TEST:
				result.append("测试连接");
				break;
			}

			break;
		case Constants.PROTOCOL_TYPE_RESPONSE:
			switch (cmdType) {
			case Constants.MOTOR_CONTROL_TYPE_READ:
				result.append("响应温度请求:");
				result.append(data);
				break;
			case Constants.MOTOR_CONTROL_TYPE_TEST:
				result.append("响应测试连接");
				break;
			case Constants.MOTOR_CONTROL_TYPE_MODE:

				if (data.contains("error")) {
					result.append("响应模式更改请求:");
					result.append("请求失败");
				} else if (data.contains("auto")) {
					result.append("响应模式状态请求:");
					result.append("当前为自动模式");
				} else if (data.contains("no")) {
					result.append("响应模式状态请求:");
					result.append("当前为手动模式");
				} else {
					result.append("响应模式更改请求:");
					result.append("操作成功");
				}
				break;
			case Constants.MOTOR_CONTROL_TYPE_ALARM:

				result.append("报警:");
				result.append(data);

				break;
			case Constants.MOTOR_CONTROL_TYPE_OPEN:

				result.append("响应开窗请求:");

				if (data.contains("error")) {
					result.append("请求失败");
				} else {
					result.append("操作成功");
				}
				break;
			case Constants.MOTOR_CONTROL_TYPE_CLOSE:

				result.append("响应关窗请求:");

				if (data.contains("error")) {
					result.append("请求失败");
				} else {
					result.append("操作成功");
				}
				break;
			case Constants.MOTOR_CONTROL_TYPE_STOP:

				result.append("响应停止请求:");

				if (data.contains("error")) {
					result.append("请求失败");
				} else {
					result.append("操作成功");
				}
				break;
			case Constants.MOTOR_CONTROL_TYPE_SYN:

				result.append("响应同步参数请求:");

				if (data.contains("error")) {
					result.append("请求失败");
				} else {
					result.append("操作成功");
				}
				break;
			}

			break;
		}

		result.append(" --- ");
		result.append(CommonTool.getDateString(new Date(time)));

		return result.toString();
	}
}
