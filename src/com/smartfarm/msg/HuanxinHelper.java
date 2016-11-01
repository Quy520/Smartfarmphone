package com.smartfarm.msg;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.tools.AccountManager;
import com.smartfarm.tools.AssistThreadWork;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.HelperThread;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.util.NetManager;

/**
 * 环信辅助工具
 * 
 * @author jeff
 *
 */
class HuanxinHelper implements ImHelperInterface {
	
	private boolean relinking = false;
    
    private void dealwithMsg(String msg) {
    	
    	Log.d(Constants.TAG, "receive huanxin msg -> " + msg);
        
        Protocol protocol = MsgUtils.msgFilter(msg);
        
        if(protocol != null)
        	EventBus.getDefault().postInOtherThread(
        			LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RECEIVE_MSG, 0, "", protocol));
    }
    
    /**
     * 全局事件监听
     * 
     * 
     */
    private void registerEventListener() {
    	
    	EMMessageListener messageListener = new EMMessageListener() {
			
			@Override
			public void onMessageReceived(List<EMMessage> messages) {}
			
			@Override
			public void onCmdMessageReceived(List<EMMessage> messages) {
				
			    for (EMMessage message : messages) {

                    //获取消息body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    dealwithMsg(cmdMsgBody.action());
                }
			}

			@Override
			public void onMessageReadAckReceived(List<EMMessage> messages) {}
			
			@Override
			public void onMessageDeliveryAckReceived(List<EMMessage> message) {}
			
			@Override
			public void onMessageChanged(EMMessage message, Object change) {}
		};
		
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

	@Override
	public void init() {
		
		Log.d(Constants.TAG, " init huanxin sdk !");
		EMOptions options = new EMOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		//初始化
		EMClient.getInstance().init(AppContext.context(), options);

		EMClient.getInstance().addConnectionListener(new GlobeConnectionListener());
        registerEventListener();
	}

	@Override
	public void relink(Activity activity) {
		
		if(relinking)
    		return;
    	
    	relinking = true;
    	HelperThread.getInstance().setThreadWork(new RelinkRunable(activity));
	}

	@Override
	public void sendMsg(Protocol msg) {

		sendMsg(msg, null);
	}

	@Override
	public void sendMsg(Protocol msg, final MsgCallback callback) {
		
		Log.d(Constants.TAG, "send msg by huanxin!");
		
		EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

		cmdMsg.setReceipt(msg.getReceiver());
		cmdMsg.addBody(new EMCmdMessageBody(msg.getProtocolString()));
		cmdMsg.setMessageStatusCallback(new EMCallBack() {
			
			@Override
			public void onSuccess() {
				
				if(callback != null)
					callback.onSuccess();
			}
			
			@Override
			public void onProgress(int arg0, String arg1) {}
			
			@Override
			public void onError(int arg0, String arg1) {
				
				if(arg0 == 201 && AppContext.context().getAccountManager().isLogined()) {

					Log.d(Constants.TAG, " to relogin user!");
					login(null);
				}
				
				if(callback != null)
					callback.onFaild(arg0, arg1);
			}
		});
		EMClient.getInstance().chatManager().sendMessage(cmdMsg);
	}

	@Override
	public boolean isLogin() {
		return EMClient.getInstance().isLoggedInBefore();
	}

	@Override
	public boolean login(final MsgCallback callback) {
		
		AccountManager aManager = AppContext.context().getAccountManager();
		
		if(ShowUtil.isEmpty(aManager.getPhoneNum()) || ShowUtil.isEmpty(aManager.getHuanxinPwd()))
			return false;
		
		if(!CommonTool.isNetworkConnected()) {
			
			EventBus.getDefault().noticeMsg("请检查手机网络设置!");
			return false;
		}

		Log.d("mmsg", "account -> " + aManager.getPhoneNum());
		Log.d("cxy", "pwd -> " + aManager.getHuanxinPwd());
		EMClient.getInstance().login(aManager.getPhoneNum(), aManager.getHuanxinPwd(), new EMCallBack() {

			@Override
			public void onSuccess() {

				EventBus.getDefault().runningOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager().loadAllConversations();
						
						if(callback != null)
							callback.onSuccess();
					}
				});
			}
		 
			@Override
			public void onProgress(int progress, String status) {}
		 
			@Override
			public void onError(int code, String message) {
				
				Log.d("main", "登陆聊天服务器失败！");
				if(callback != null)
					callback.onFaild(code, message);
			}
		});
		
		return true;
	}

	@Override
	public void logout(final MsgCallback callback) {
		if(!isLogin()) {
			if (callback != null) {
				callback.onSuccess();
			}
			return;
		}
		
		EMClient.getInstance().logout(true, new EMCallBack() {

			@Override
			public void onSuccess() {
				if (callback != null) 
					callback.onSuccess();
			}

			@Override
			public void onProgress(int progress, String status) {}

			@Override
			public void onError(int code, String error) {
				if (callback != null) 
					callback.onFaild(code, error);
			}
		});
	}

	@Override
	public void exit() {

    	logout(null);
	}
	
	/**
     * 环信连接状态监听器
     * 
     * @author jeff
     *
     */
    class GlobeConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {

			Log.d(Constants.TAG, " connected huanxin server !");
			
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_NET_OK));
			
			NetManager.getInstence().setNetOk(true);
		}

		@Override
		public void onDisconnected(int error) {
			
			Log.d(Constants.TAG, " disconnected huanxin server !");

			int errorType = 0;
			
			if (error == EMError.USER_REMOVED) {
				
				errorType = CONN_RES_REMOVEED;
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            	
            	errorType = CONN_RES_CONFLTCT;
            } else {
            	
            	if(CommonTool.isNetworkConnected()) 
    				errorType = CONN_RES_SERVER_ERROR;
            	else
    				errorType = CONN_RES_NO_NET;
            }

			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_NET_DOWN, errorType));
			
			NetManager.getInstence().setNetOk(false);
		}
    }
    
    class RelinkRunable implements AssistThreadWork {

    	BlockingDeque<Integer> tipsQueue;
    	ProgressDialog progressDialog = null;
    	Activity activity;
    	
    	public RelinkRunable(Activity activity) {
    		
    		this.activity = activity;
    		tipsQueue = new LinkedBlockingDeque<Integer>();
    		progressDialog = new ProgressDialog(activity);
        	progressDialog.setCancelable(false);
    		progressDialog.setMessage("正在退出当前账号...");
    		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		progressDialog.show();
    	}
    	
		@Override
		public void working() {
			
			CommonTool.delay(1000);
			
			if(!isLogin())
				exit();
			
			tipsQueue.add(1);
			HelperThread.getInstance().publish(RelinkRunable.this);
			
			if(!login(new MsgCallback() {
				
				@Override
				public void onSuccess() {
					
					if(progressDialog != null) {
						
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								progressDialog.dismiss();
								ToastTool.showToast("已重新连接服务器");
								relinking = false;
							}
						});
					}
				}
				
				@Override
				public void onFaild(final int errorCode, final String errorMsg) {
					
					if(progressDialog != null) {
						
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								progressDialog.dismiss();
								ToastTool.showToast("重新连接失败，错误代码：" + errorCode);
								relinking = false;
								Log.d(Constants.TAG, "relink error , code -> " + errorCode + ", error descr -> " + errorMsg);
							}
						});
					}
				}
			}))
				tipsQueue.add(3);
			else
				tipsQueue.add(2);
		}

		@Override
		public void ok() {
			
			if(progressDialog == null)
				return;
			
			try {
				
				int cmd = tipsQueue.take();
				
				if(cmd == 1) 
					progressDialog.setMessage("退出成功，正在尝试重新登录...");
				else if(cmd == 3) {
					progressDialog.dismiss();
					relinking = false;
				}
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }

	@Override
	public void checkLink() {
		
		if(!CommonTool.isNetworkConnected())
			return;
		
		if(!isLogin())
			login(null);
		
		ProtocolFactory.GetTestSelfProtocol().send();
	}
}
