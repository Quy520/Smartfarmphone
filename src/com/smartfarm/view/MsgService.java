package com.smartfarm.view;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.InfoRecordDao;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.msg.MsgHelper;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.util.NetManager;
import com.smartfarm.view.util.NetManager.OnNetErrorListener;

public class MsgService extends Service implements Runnable {

	private final MyBinder mBinder = new MyBinder();
	private int checkCount = 1;
	private Handler mHandler;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		NetManager.getInstence().setOnNetErrorLintener(new NetErrorListener());
		
		mHandler = new Handler();
		mHandler.postDelayed(this, 60000);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {

		mHandler.removeCallbacksAndMessages(null);
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, MsgService.class);
		this.startService(localIntent);

		super.onDestroy();
	}

	@Override
	public void run() {
		
		AppContext.context().refreshPengInfo();
		mHandler.postDelayed(this, 60000);
		
		if(checkCount % 11 == 0) {
			
			MsgHelper.get().checkLink();
			checkCount = 1;
		}
	}

	public class MyBinder extends Binder {  
        public MsgService getService(){  
            return MsgService.this;  
        }  
    } 
	
	private class NetErrorListener implements OnNetErrorListener {

		@Override
		public void onNetError(int dest) {

			PengInfo info = PengInfoDao.findById(dest);
			
			if(info == null)
				return;
			
			boolean shouldAlarm = ShowUtil.isShouldAlarm(info);
			Log.d(Constants.TAG, " should alarm -> " + shouldAlarm);
			
			
			String content = "请注意大棚(" + info.getName() + ")连接温控机异常！";

			if(shouldAlarm) {
				ShowUtil.showAlarm(MsgService.this, "连接异常", content, dest);
			}
			
			EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_TEST_ERROR, content));
			
			InfoRecordDao.add(info, content, InfoRecord.RECORD_TYPE_ALARM, System.currentTimeMillis());
		}
	}
}
