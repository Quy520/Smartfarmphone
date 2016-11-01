package com.smartfarm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.smartfarm.msg.MsgHelper;
import com.smartfarm.tools.AccountManager;
import com.smartfarm.tools.Constants;
import com.smartfarm.view.util.ConfigManager;
/**
 * 初始化欢迎页面
 * @author QSD
 *
 */
public class WelcomeActivity extends Activity {

//	private BaseAsyncHttpResHandler httpHandler = new BaseAsyncHttpResHandler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SystemTool.gc(this); //针对性能好的手机使用，加快应用相应速度

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.welcome);
        
    	delayRedirect(ConfigManager.getWelcomeEnable(this) && AppContext.context().isShowWelcome());
    	
    	AppContext.context().setShowWelcome(false);
    }
    
    private void delayRedirect(final boolean delay) {
    	
    	new Thread(new Runnable() {
			public void run() {
				
				AccountManager aManager = AppContext.context().getAccountManager();
				
				if(aManager.isLogined()) {
					
					if (MsgHelper.get().isLogin()) {
						
						if (delay) {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {}
						}
					} else {
						
						MsgHelper.get().login(null);
					}

					redirectTo();
//					SmartfarmNetHelper.appDeviceOnline(httpHandler);
					
				} else {
					
					if (delay) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {}
					}

					redirectTo();
				}
				
			}
		}).start();
    }
    
    
    /**
     * 跳转到...
     * 跳转到知道页面或者主页面
     */
    private void redirectTo() {
    	
    	Intent intent = new Intent();
 
    	if(ConfigManager.getVersion(this).equals(Constants.CURR_VERSION)) 
    		intent.setClass(this, SmartFarm.class);
    	else
    		intent.setClass(this, GuildActivity.class);
    	
    	startActivity(intent);
        finish();
    }
}
