package com.smartfarm.net.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 操作结果实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月14日 下午2:59:27
 * 
 */
@XStreamAlias("resultbean")
public class ResultBean extends Base {

	private static final long serialVersionUID = 4251606674419381509L;

	@XStreamAlias("result")
	private Result result;

	@XStreamAlias("notice")
	private Notice notice;

	@XStreamAlias("relation")
	private int relation;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}
}
