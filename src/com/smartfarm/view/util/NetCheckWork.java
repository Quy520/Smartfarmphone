package com.smartfarm.view.util;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.smartfarm.bean.NetBean;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.msg.MsgHelper;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.AssistThreadWork;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.HelperThread;
import com.smartfarm.view.R;
import com.smartfarm.widget.dialog.CommonDialog;

public class NetCheckWork implements AssistThreadWork {
	
	public static final String REASON_NO_NET = "本地连接尚未打开，请尝试打开手机wifi或移动数据连接!\n";
	public static final String REASON_NET_ERROR = "本地连接不能连接到网络，可能因为信号不好等因素!\n";
	public static final String REASON_NO_LOGIN = "本地未能登录服务器!\n";
	public static final String REASON_NO_SERVER = "服务器未响应，可能服务器延迟等原因!\n";
	public static final String REASON_NO_RESPONSE = "大棚%1$s温控机未响应，可能是温控机未开启或未启动程序或未联网!\n";
	public static final String REASON_NROMAL = "网络正常，能正常连接温控机!";
	
	public static final String OK_NET_SWITCH = "本地连接已经打开;\n";
	public static final String OK_NET = "本地已连接上网络;\n";
	public static final String OK_LOGIN = "本地已登录;\n";
	public static final String OK_SERVER = "本地已连接上服务器;\n";
	public static final String OK_RESPONSE = "本地已连接%1$s的温控机;\n";
	public static final String OK_CHECK = "end";
	
	private static CommonDialog diagnosisDialog;
	
	private TextView addText;
	private CommonDialog dialog;
	private BlockingDeque<String> tipsQueue;
	
	public NetCheckWork(TextView tv, CommonDialog dialog) {
		addText = tv;
		this.dialog = dialog;
		tipsQueue = new LinkedBlockingDeque<String>();
	}
	
	@Override
	public void working() {
		
		CommonTool.delay(1000);
		if(!CommonTool.isNetworkConnected()) {
			tipsQueue.add(REASON_NO_NET);
			HelperThread.getInstance().publish(this);
			tipsQueue.add(OK_CHECK);
			return;
		} 
		tipsQueue.add(OK_NET_SWITCH);
		HelperThread.getInstance().publish(this);

		if(!CommonTool.isConnected()) {
			tipsQueue.add(REASON_NET_ERROR);
			HelperThread.getInstance().publish(this);
			tipsQueue.add(OK_CHECK);
			return;
		}
		tipsQueue.add(OK_NET);
		HelperThread.getInstance().publish(this);
		
		if(!MsgHelper.get().isLogin()) {
			tipsQueue.add(REASON_NO_LOGIN);
			HelperThread.getInstance().publish(this);
			tipsQueue.add(OK_CHECK);
			return;
		}
		tipsQueue.add(OK_LOGIN);
		HelperThread.getInstance().publish(this);	

		if(!EMClient.getInstance().isConnected()) {
			tipsQueue.add(REASON_NO_SERVER);
			HelperThread.getInstance().publish(this);
			tipsQueue.add(OK_CHECK);
			return;
		}
		tipsQueue.add(OK_SERVER);
		HelperThread.getInstance().publish(this);
		
		List<PengInfo> allPeng = PengInfoDao.findAll();
		NetManager.getInstence().clearNetInfo();
		
		for(PengInfo info : allPeng) {
			long curr = System.currentTimeMillis();
			int count = 6;

			while(count > 0) {
				ProtocolFactory.GetTestProtocol(info.getId()).send();
				
				CommonTool.delay(5000);
				
				NetBean bean = NetManager.getInstence().getGhNetInfo(info.getId());
				
				if(curr - bean.time < 60000)
					break;
				else 
					count--;
			}
			
			if(count <= 0) {
				tipsQueue.add(String.format(REASON_NO_RESPONSE, info.getName()));
				HelperThread.getInstance().publish(this);
				continue;
			}
			tipsQueue.add(String.format(OK_RESPONSE, info.getName()));
			HelperThread.getInstance().publish(this);
		}

		CommonTool.delay(1000);
		tipsQueue.add(OK_CHECK);
	}

	@Override
	public void ok() {

		try {
			
			String content = tipsQueue.take();
			
			if(content.equals(OK_CHECK)) {
				dialog.cancel();
				
				if(diagnosisDialog != null)
					diagnosisDialog = null;
				
				SpannableStringBuilder builder = new SpannableStringBuilder();
				builder.append(addText.getText().toString());
				
				CommonDialog resultDialog = new CommonDialog(dialog.getContext());
				resultDialog.setTitle("诊断结果");
				resultDialog.setMessage(builder);
				resultDialog.setPositiveButton("确定", null);
				resultDialog.show();
				
			} else {

				addText.append(content);
			}
		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@SuppressLint("InflateParams")
	public static void showCheckDialog(Context context) {
		
		View view = LayoutInflater.from(context).inflate(
				R.layout.net_diagnosis_dialog, null);

		TextView progressText = (TextView) view
				.findViewById(R.id.diagnosis_process_text);

		if(diagnosisDialog == null) {

			diagnosisDialog = new CommonDialog(context);
			diagnosisDialog.setCancelable(false);
			diagnosisDialog.setTitle(R.string.net_diagnosis);
			diagnosisDialog.setContent(view);

			diagnosisDialog.show();
			HelperThread.getInstance().setThreadWork(
					new NetCheckWork(progressText, diagnosisDialog));
		} else {
			
			diagnosisDialog.show();
		}
	}
}
