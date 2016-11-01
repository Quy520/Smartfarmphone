package com.smartfarm.net.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public abstract class Entity extends Base {

	private static final long serialVersionUID = 4316109187225746635L;

	@XStreamAlias("id")
	protected int id;

	protected String cacheKey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
}