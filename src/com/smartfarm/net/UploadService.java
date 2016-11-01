package com.smartfarm.net;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.AppContext;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

public class UploadService extends Service {
	
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    	LocationClientOption option = new LocationClientOption();
    	final LocationClient mLocationClient = new LocationClient(getApplicationContext());
		
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.SetIgnoreCacheException(false);       //设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
        option.setProdName("SmartfarmPhone"); 
        mLocationClient.setLocOption(option);

        mLocationClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {

				Log.d(Constants.TAG, "current location -> " + location.getAddrStr() 
						+ ", city code -> " + location.getCityCode());
				AppContext.context().setCurrLocation(location);
				mLocationClient.stop();

				loginPrepare();
			}
		});
        
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);    
		String mac = wifi.getConnectionInfo().getMacAddress();
        
		if(ShowUtil.isEmpty(mac)) {
			
			wifi.setWifiEnabled(true); 
			CommonTool.delay(8000);
			mac = wifi.getConnectionInfo().getMacAddress();
			wifi.setWifiEnabled(false); 
		}

		AppContext.context().setMac(mac);
	    mLocationClient.start();
    	

        return super.onStartCommand(intent, flags, startId);
    }
    
    private void loginPrepare() {
    	
    	SmartfarmNetHelper.appDeviceOnline(new BaseAsyncHttpResHandler() {

			@Override
			public void onFailure(int errorCode, String data) {

				UploadService.this.stopSelf();
			}

			@Override
			public void onSuccess(ApiResponse res) {

				if(res.getErrorCode() > 0)
					toLogin();
				else
					onFailure(-1, "");
			}
    	});
    }
     
    private void toLogin() {
    	
    	SmartfarmNetHelper.appDeviceLogin(new BaseAsyncHttpResHandler() {

			@Override
			public void onFailure(int errorCode, String data) {

				UploadService.this.stopSelf();
			}

			@Override
			public void onSuccess(ApiResponse res) {

				UploadService.this.stopSelf();
			}
    	});
    }
}
