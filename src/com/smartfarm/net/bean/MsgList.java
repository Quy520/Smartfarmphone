package com.smartfarm.net.bean;

import java.util.List;

import com.smartfarm.bean.ListEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("smartfarm")
public class MsgList extends Entity implements ListEntity<AppMessage> {

	private static final long serialVersionUID = -8410128686845822811L;

	@XStreamAlias("messages")
	private List<AppMessage> msgs;

	public List<AppMessage> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<AppMessage> msgs) {
		this.msgs = msgs;
	}

	@Override
	public String toString() {
		return msgs.toString();
	}

	@Override
	public List<AppMessage> getList() {
		return msgs;
	}
}
