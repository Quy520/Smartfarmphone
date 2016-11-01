package com.smartfarm.net.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 数据操作结果实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@XStreamAlias("result")
public class Result implements Serializable {

	private static final long serialVersionUID = 6765417227078949450L;

	@XStreamAlias("errorCode")
	private int errorCode;

	@XStreamAlias("errorMessage")
	private String errorMessage;

	public boolean OK() {
		return errorCode == 1;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}