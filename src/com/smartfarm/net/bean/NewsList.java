package com.smartfarm.net.bean;

import java.util.ArrayList;
import java.util.List;

import com.smartfarm.bean.ListEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("smartfarm")
public class NewsList extends Entity implements ListEntity<News> {

	private static final long serialVersionUID = -5323626589671648033L;

	public final static String PREF_READED_BLOG_LIST = "readed_blog_list.pref";
	
	@XStreamAlias("news")
	private List<News> news = new ArrayList<News>();

	@Override
	public List<News> getList() {
		return news;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}
}
