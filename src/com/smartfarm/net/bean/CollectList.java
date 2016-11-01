package com.smartfarm.net.bean;

import java.util.List;

import com.smartfarm.bean.ListEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class CollectList extends Entity implements ListEntity<Collect> {

	private static final long serialVersionUID = -2194966646592496314L;

    @XStreamAlias("collects")
    private List<Collect> collects;
    
	@Override
	public List<Collect> getList() {
		return collects;
	}

	public List<Collect> getCollects() {
		return collects;
	}

	public void setCollects(List<Collect> collects) {
		this.collects = collects;
	}

}
