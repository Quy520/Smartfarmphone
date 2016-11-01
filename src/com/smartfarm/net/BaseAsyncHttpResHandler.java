package com.smartfarm.net;

import org.apache.http.Header;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smartfarm.bean.ResponseModel;
import com.smartfarm.tools.ToastTool;

public abstract class BaseAsyncHttpResHandler extends AsyncHttpResponseHandler {

	private ResponseModel oldModel;

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

		if (arg2 == null)
			onFailure(arg0, "");
		else
			onFailure(arg0, new String(arg2));
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

		if (arg2 == null)
			onFailure(arg0, "Server not response!");
		else {
			ApiResponse res = ApiResponse.getApiResponse(new String(arg2));

			if (res == null) {
				onFailure(arg0, "Server response can not be resolved!");
				return;
			} else if(res.getErrorCode() == 0){
				onSuccess(res);
				return;
			}
			else if (res.getErrorCode() == ApiResponse.ERROR_RUNTIME_TOKEN || res.getErrorCode() == ApiResponse.ERROR_CONTROL_TOKEN) {
				if (oldModel != null){
					SmartfarmNetHelper.switchToken(oldModel);
					Log.d("cxy1", oldModel.getUrl());
				}
				else{
					Log.d("cxy1", "oldModel为空");
				}
				return;
			}else if(res.getErrorCode() == ApiResponse.ERROR_MISS_PHONE){
				ToastTool.showToast("手机号码缺失");
			} else if(res.getErrorCode() == ApiResponse.ERROR_PARAM_PHONE){
				ToastTool.showToast("手机号码不合法");
			}else if(res.getErrorCode() == ApiResponse.ERROR_CONTROL_ACCOUNT_NOT){
				ToastTool.showToast("没有此帐号");
			}else if(res.getErrorCode()==ApiResponse.ERROR_MISS_PWD ){
				ToastTool.showToast("密码缺失");
			}else if(res.getErrorCode()== ApiResponse.ERROR_MISS_SALT){
				ToastTool.showToast("未调用获取salt值API");
			}else if(res.getErrorCode()== ApiResponse.ERROR_CONTROL_USER_REMOVE){
				ToastTool.showToast("帐号被移除");
			}else if(res.getErrorCode()== ApiResponse.ERROR_CONTROL_PASSWORD){
				ToastTool.showToast("密码错误");
			}else if(res.getErrorCode()==ApiResponse.ERROR_MISS_VER){
				ToastTool.showToast("校验码缺失");
			}else if(res.getErrorCode()==ApiResponse.ERROR_PARAM_VER){
				ToastTool.showToast("验证码不合法");
			}else if(res.getErrorCode()==ApiResponse.ERROR_CONTROL_VER){
				ToastTool.showToast("验证码错误");
			}else if(res.getErrorCode()==ApiResponse.ERROR_CONTROL_HUANXIN_RESIGN){
				ToastTool.showToast("同步环信注册帐号失败");
			}else if(res.getErrorCode() == ApiResponse.ERROR_MISS_PHONE){
				ToastTool.showToast("今日验证码发送已超过八条，如需重置次数，请联系代理商");
			}else if(res.getErrorCode() == ApiResponse.ERROR_LIMIT_VER){
				ToastTool.showToast("超过验证码发送上限");
			}else if(res.getErrorCode() == ApiResponse.ERROR_CONTROL_TAOBAO_SMS){
				ToastTool.showToast("同步发送验证短信操作失败");
			}else if(res.getErrorCode() == ApiResponse.ERROR_CONTROL_ACCOUNT_EXIST){
				ToastTool.showToast("帐号已存在");
			}else if(res.getErrorCode() == ApiResponse.ERROR_PARAMETER){
				ToastTool.showToast("请先获取验证码");
			}else if(res.getErrorCode() == ApiResponse.ERROR_MISS_NAME){
				ToastTool.showToast("用户名缺失");
			}else if(res.getErrorCode() == ApiResponse.ERROR_MISS_SUGGEST_CONTENT){
				ToastTool.showToast("建议内容缺失");
			}else if(res.getErrorCode() == ApiResponse.ERROR_RUNTIME_LOGIN){
				ToastTool.showToast("登录过期");
			}
			else{
				ToastTool.showToast(res.getMessage());
			}
			
			onFailure(arg0, "ErrorCoed -> " + res.getErrorCode());
				
		}
	}

	public void setOldModel(ResponseModel oldModel) {
		this.oldModel = oldModel;
	}

	public abstract void onFailure(int errorCode, String data);

	public abstract void onSuccess(ApiResponse res);

}
