package com.smartfarm.net;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.smartfarm.bean.DeviceInfoBean;
import com.smartfarm.bean.ResponseModel;
import com.smartfarm.net.bean.ApiUpdateUser;
import com.smartfarm.net.bean.PubMsg;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.DeviceIdHelper;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;

public class SmartfarmNetHelper {

	/**
	 * 设备登录
	 * 
	 * @param DeviceId
	 * @param Version
	 * @param Location
	 * @param device_type
	 */
	public static void appDeviceOnline(BaseAsyncHttpResHandler handler) {
		RequestParams params = new RequestParams();
		ResponseModel model = new ResponseModel();
		params.put("DeviceId", DeviceIdHelper.DEVICE_ID);
		params.put("Location", AppContext.context().getCurrLocation());
		params.put("Version",CommonTool.getVersion(AppContext.context()));
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/device/online";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 设备基本信息同步
	 * 
	 * @param DeviceId
	 * @param Imei
	 * @param Mac
	 * @param Name
	 * @param PhoneNumber
	 */
	public static void appDeviceLogin(BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		ResponseModel model = new ResponseModel();
		params.put("Imei", AppContext.context().getImei());
		params.put("PhoneNumber", AppContext.context().getPhone());
		params.put("Mac", AppContext.context().getMac());
		params.put("Name", "pad");
		params.put("DeviceId", DeviceIdHelper.DEVICE_ID);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/device/base-info";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 设备参数同步
	 * 
	 * @param DeviceId
	 * @param Setting
	 * @param Replace
	 */
	public static void synSetting(BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		ResponseModel model = new ResponseModel();
		DeviceInfoBean bean = new DeviceInfoBean();
		params.put("Setting", JSON.toJSONString(bean));
		params.put("DeviceId",DeviceIdHelper.DEVICE_ID);
		String url = "v1.0/device/setting";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(url);
		handler.setOldModel(model);
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, url, params, handler);
	}

	/**
	 * 温控机温度上传
	 * 
	 * @param DeviceId
	 * @param Temp
	 */
	public static void synTemp(String tempInfo,BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		ResponseModel model = new ResponseModel();
		params.put("DeviceId", DeviceIdHelper.DEVICE_ID);
		params.put("Temp", tempInfo);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/device/temp";
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}

	/**
	 * 获取密码验证的salt
	 * 
	 * @param phone
	 * @param handler
	 */
	public static void appLoginPrepare(String phone,
			BaseAsyncHttpResHandler handler) {
		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.put("PhoneNumber", phone);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/salt";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 用户登录
	 * 
	 * @param pwd
	 * @param handler
	 */
	public static void appLogin(String pwd, BaseAsyncHttpResHandler handler) {
		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.put("Password", pwd);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/login";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}

	/**
	 * 用户注册
	 * 
	 * @param phone
	 * @param name
	 * @param pwd
	 * @param handler
	 */
	public static void appResign(String nickName, String userName, 
			String pwd,String code, BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		ResponseModel model = new ResponseModel();
		params.put("PhoneNumber", userName);
		params.put("Password", pwd);
		params.put("Name", nickName);
		params.put("VerCode", code);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/register";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}
	
	/**
	 * 2.用户注册登录
	 * 
	 * @param phone
	 * @param name
	 * @param pwd
	 * @param handler
	 */
	public static void appRegisterLogin(String phoneNumber, String verCode,
			BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		ResponseModel model = new ResponseModel();
		params.put("PhoneNumber", phoneNumber);
		params.put("VerCode", verCode);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/register-login";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}

	/**
	 * 获取短信验证码，注意获取间隔不要太短，一天一个手机号码上限获取八次验证码
	 * 
	 * @param phone
	 * @param handler
	 */
	public static void getSmsVerCode(String phone,
			BaseAsyncHttpResHandler handler) {
		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.put("PhoneNumber", phone);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/sms";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 获取语音验证码，次数与短信验证码共享
	 * 
	 * @param phone
	 * @param handler
	 */
	public static void getVoiceVerCode(String phone,
			BaseAsyncHttpResHandler handler) {
		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.put("PhoneNumber", phone);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/voice";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}
	
	/**
	 * 用户注销
	 * 
	 * @param Token
	 */
	public static void getLogOut(BaseAsyncHttpResHandler handler){
		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.put("Token", AppContext.context().getAccountManager().getToken());
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/voice";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 用户修改密码
	 * 
	 * @param pwd
	 * @param newPwd
	 * @param handler
	 */
	public static void appModify(String pwd, String newPwd,BaseAsyncHttpResHandler handler) {
		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.setContentEncoding(HTTP.UTF_8);
		params.put("NewPassword", newPwd);
		params.put("Password", pwd);
		String loginurl = "v1.0/auth/change-password";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.put(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 用户动态修改密码
	 * 
	 * @param pwd
	 * @param newPwd
	 * @param VerCode
	 * @param handler
	 */
	public static void appModify(String phone, String pwd, String verCode,
			BaseAsyncHttpResHandler handler) {
		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.setContentEncoding(HTTP.UTF_8);
		params.put("PhoneNumber", phone);
		params.put("Password", pwd);
		params.put("VerCode", verCode);
		String loginurl = "v1.0/auth/change-password/sms";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.put(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 用户更新个人信息
	 * 
	 * @param user
	 * @param handler
	 */
	public static void appUpdate(ApiUpdateUser a, BaseAsyncHttpResHandler handler) {

		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.setContentEncoding(HTTP.UTF_8);
		params.put("UserInfo", JSON.toJSONString(a));
		params.put("UserInfo", a);
		params.put("Token", AppContext.context().getAccountManager().getToken());
		String loginurl = "v1.0/auth/update";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_PUT);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.put(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 上传头像
	 * 
	 * @param portrait
	 * @param handler
	 * @return
	 */
	public static boolean uploadPortrait(File portrait,
			BaseAsyncHttpResHandler handler) {

		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.setContentEncoding(HTTP.UTF_8);
		try {
			params.put("Img", portrait);
		} catch (FileNotFoundException e) {
			return false;
		}
		params.put("Token", AppContext.context().getAccountManager().getToken());
		String loginurl = "v1.0/auth/update/portrait";
//		handler.set
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_PUT);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.put(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
		return true;
	}
	
	/**
	 * 发送建议
	 * 
	 * @param Token
	 * @param s_type
	 * @param Content
	 */
	
	public static void appSendSuggest(String content,BaseAsyncHttpResHandler handler){
		ResponseModel model = new ResponseModel();
		RequestParams params = new RequestParams();
		params.setContentEncoding(HTTP.UTF_8);
		params.put("Token", AppContext.context().getAccountManager().getToken());
		params.put("Content", content);
		params.put("s_type", 0);
		String loginurl = "/v1.0/feel/suggest/0";
		model.setHandle(handler);
		model.setParams(params);
		model.setType(ResponseModel.MSG_POST);
		model.setUrl(loginurl);
		handler.setOldModel(model);
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}
	
	/**
	 * 交换token
	 * 
	 * @param Token
	 */
	public static void switchToken(final ResponseModel oldRequest) {

		RequestParams params = new RequestParams();
		params.setContentEncoding(HTTP.UTF_8);
		params.put("Token", AppContext.context().getAccountManager().getRefreshToken());
		String loginurl = "v1.0/auth/switch-token";
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				new BaseAsyncHttpResHandler() {

					@Override
					public void onSuccess(ApiResponse res) {
						Log.d("cxy", "交换token->"+res.getErrorCode());
						if (res.getErrorCode() == 0) {
							String _token = res.getResponseData().toString();
							AppContext.context().getAccountManager().setToken(_token);
							oldRequest.getParams().put("Token", _token);
							switch (oldRequest.getType()) {
							case ResponseModel.MSG_GET:
								ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER,
										oldRequest.getUrl(), oldRequest.getParams(), oldRequest.getHandle());
								break;
							case ResponseModel.MSG_POST:
								ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER,
										oldRequest.getUrl(), oldRequest.getParams(), oldRequest.getHandle());
								break;
								
							case ResponseModel.MSG_PUT:
								ApiHttpClient.put(ApiHttpClient.PYTHON_SERVER,
										oldRequest.getUrl(), oldRequest.getParams(), oldRequest.getHandle());
								break;
							}
						}
					}

					@Override
					public void onFailure(int errorCode, String data) {
						ToastTool.showToast("token交换失败" + errorCode);
						Log.d("cxy", "交换token失败->"+errorCode);
					}
				});
	}
    
    /**
     * 获取相关用户列表
     * 
     * @param pageIndex
     * @param type
     * @param handler
     */
    public static void getUserRelationList(int pageIndex, int type, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        
        if(type == ApiContants.USER_FAN)
            params.put("type", 1);
        else 
            params.put("type", 2);
        	
        String url = "user/relation/" + pageIndex + "/" + Constants.PAGE_SIZE;
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取推荐的消息
     * 
     * @param page
     * @param handler
     */
    public static void getRecommendMsg(int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        	
        String url = "msg/get/" + page + "/" + Constants.PAGE_SIZE + "/1";
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取附近的消息
     * 
     * @param page
     * @param handler
     */
    public static void getNearbyMsg(int page, String cityCode, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("cityCode", cityCode);
        params.put("token", AppContext.context().getAccountManager().getToken());
        	
        String url = "msg/get/" + page + "/" + Constants.PAGE_SIZE + "/2";
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取关心的消息
     * 
     * @param page
     * @param handler
     */
    public static void getConcernMsg(int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        	
        String url = "msg/get/" + page + "/" + Constants.PAGE_SIZE + "/3";
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取关注的人的消息
     * 
     * @param page
     * @param handler
     */
    public static void getWatcherMsg(int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        	
        String url = "msg/get/" + page + "/" + Constants.PAGE_SIZE + "/4";
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取求助的消息
     * 
     * @param page
     * @param handler
     */
    public static void getHelpMsg(int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        	
        String url = "msg/get/" + page + "/" + Constants.PAGE_SIZE + "/5";
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取自己发布的消息
     * 
     * @param page
     * @param handler
     */
    public static void getSelfMsg(int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        	
        String url = "msg/self/" + page + "/" + Constants.PAGE_SIZE;
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 删除自己发布的消息
     * 
     * @param page
     * @param handler
     */
    public static void getRemoveMsg(int msgId, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        	
        String url = "msg/remove/" + msgId;
        ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }

    /**
     * 获取全种类新闻列表
     * 
     * @param page 页码
     * @param handler
     */
    public static void getNewsList(int page, AsyncHttpResponseHandler handler) {
    	
        RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        
        String url = "news/1/" + page + "/" + Constants.PAGE_SIZE;
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }

    /**
     * 获取帮助新闻列表
     * 
     * @param page 页码
     * @param handler
     */
    public static void getHelpNewsList(int page, AsyncHttpResponseHandler handler) {
    	
        RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        
        String url = "news/2/" + page + "/" + Constants.PAGE_SIZE;
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }

    /**
     * 获取新闻详情
     * 
     * @param id 新闻id
     * @param handler
     */
    public static void getNewsDetail(int id, AsyncHttpResponseHandler handler) {
    	
        RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, "news/detail/" + id, params, handler);
    }

    /**
     * 添加收藏
     * 
     * @param dstId
     * @param dstType
     * @param handler
     */
    public static void collect(int dstId, int dstType, AsyncHttpResponseHandler handler) {
    	
        RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());

        ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, "collect/" + dstType + "/" + dstId, params, handler);
    }

    /**
     * 移除某个收藏
     * 
     * @param dstId
     * @param dstType
     * @param handler
     */
    public static void removeCollect(int dstId, int dstType, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());

        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, "collect/" + dstType + "/" + dstId, params, handler);
    }

    /**
     * 获取收藏的消息
     * 
     * @param page
     * @param handler
     */
    public static void getCollectMsg(int page, AsyncHttpResponseHandler handler) {

    	getCollect(ApiContants.CONTROL_TYPE_MSG, page, handler);
    }

    /**
     * 获取收藏的新闻
     * 
     * @param page
     * @param handler
     */
    public static void getCollectNews(int page, AsyncHttpResponseHandler handler) {

    	getCollect(ApiContants.CONTROL_TYPE_NEWS, page, handler);
    }

    /**
     * 获取收藏的评论
     * 
     * @param page 
     * @param handler
     */
    public static void getCollectComment(int page, AsyncHttpResponseHandler handler) {
    	
    	getCollect(ApiContants.CONTROL_TYPE_COMMENT, page, handler);
    }

    private static void getCollect(int type, int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
        
        String url = "collect/get/" + type + "/" + page + "/" + Constants.PAGE_SIZE;
        ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
  
    /**
     * 发布消息
     * 
     * @param msg
     * @param handler
     */
    public static void pubMsg(PubMsg msg, AsyncHttpResponseHandler handler) {
    	pubMsg(msg, null, handler);
    }
    
    /**
     * 发布消息带图片
     * 
     * @param msg
     * @param img
     * @param handler
     */
    public static void pubMsg(PubMsg msg, File img, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        
        if(img != null) {
        	try {
    			params.put("img", img);
    		} catch (FileNotFoundException e) {}
        }
        
        params.put("token", AppContext.context().getAccountManager().getToken());
        params.put("msg", JSON.toJSONString(msg));
		
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, "msg/pub", params, handler);
    }
    
    /**
     * 转发消息
     * 
     * @param msgId
     * @param msg
     * @param handler
     */
    public static void forwardMsg(int msgId, PubMsg msg, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        
        params.put("token", AppContext.context().getAccountManager().getToken());
        params.put("msg", JSON.toJSONString(msg));
		
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, "msg/forward/" + msgId, params, handler);
    }
    
    /**
     * 点赞
     * 
     * @param dstType ApiContants下controlType
     * @param dstId 目标id
     * @param handler
     */
    public static void thumbMsg(int dstType, int dstId, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        
        params.put("token", AppContext.context().getAccountManager().getToken());
		
        String url = "thumb/" + dstType + "/" + dstId;
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }

    /**
     * 取消点赞
     * 
     * @param dstType ApiContants下controlType
     * @param dstId 目标id
     * @param handler
     */
    public static void unthumbMsg(int dstType, int dstId, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        
        params.put("token", AppContext.context().getAccountManager().getToken());
		
        String url = "thumb/" + dstType + "/" + dstId;
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取点赞用户列表
     * 
     * @param dstType 目标类型
     * @param dstId 目标id
     * @param page 页码
     * @param handler
     */
    public static void getThumbs(int dstType, int dstId, int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        
        params.put("page", page);
        params.put("size", Constants.PAGE_SIZE);
		
        String url = "thumb/get/" + dstType + "/" + dstId;
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 发表评论
     * 
     * @param dstType 目标类型
     * @param dstId 目标id
     * @param content 内容
     * @param handler
     */
    public static void pubComment(int dstType, int dstId, String content, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);

        params.put("content", content);
        params.put("token", AppContext.context().getAccountManager().getToken());
		
        String url = "comment/pub/" + dstType + "/" + dstId;
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 发表评论
     * 
     * @param dstType 目标类型
     * @param dstId 目标id
     * @param content 内容
     * @param mention 提到某用户的id
     * @param handler
     */
    public static void pubCommentWithMention(int dstType, 
    		int dstId, String content, int mention, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);

        params.put("content", content);
        params.put("token", AppContext.context().getAccountManager().getToken());
		
        String url = "comment/pub/" + dstType + "/" + dstId + "/" + mention;
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 删除评论，只能删除自己的评论
     * 
     * @param commentId 评论id
     * @param handler
     */
    public static void deleteComment(int commentId, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
		
        String url = "comment/delete/" + commentId;
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取目标的评论列表
     * 
     * @param dstType 目标类型
     * @param dstId 目标id
     * @param page 页码
     * @param handler
     */
    public static void getComment(int dstType, int dstId, int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
		
        String url = "comment/" + dstType + "/" + dstId + "/" + page + "/" + Constants.PAGE_SIZE;
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取用户的所有评论列表
     * 
     * @param dstType 目标类型
     * @param page 页码
     * @param handler
     */
    public static void getUserComment(int dstType, int page, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
		
        String url = "comment/" + dstType + "/" + page + "/" + Constants.PAGE_SIZE;
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
    /**
     * 获取msg详情
     * 
     * @param msgId
     * @param handler
     */
    public static void getMsgDetail(int msgId, AsyncHttpResponseHandler handler) {
    	
    	RequestParams params = new RequestParams();
        params.setContentEncoding(HTTP.UTF_8);
        params.put("token", AppContext.context().getAccountManager().getToken());
		
        String url = "msg/" + msgId;
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, url, params, handler);
    }
    
}
