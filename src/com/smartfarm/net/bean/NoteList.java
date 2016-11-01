package com.smartfarm.net.bean;

import java.util.ArrayList;
import java.util.List;

import com.smartfarm.bean.ListEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("smartfarm")
public class NoteList extends Entity implements ListEntity<Note> {
	
	private static final long serialVersionUID = -5979836821646735661L;

	@XStreamAlias("pagesize")
	private int pagesize;
	
	@XStreamAlias("notes")
	private List<Note> noteList = new ArrayList<Note>();
	
	@XStreamAlias("newsCount")
	private int newsCount;

	@Override
	public List<Note> getList() {
		return noteList;
	}

	public int getPagesize() {
		return pagesize;
	}

	public List<Note> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<Note> noteList) {
		this.noteList = noteList;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getNewsCount() {
		return newsCount;
	}

	public void setNewsCount(int newsCount) {
		this.newsCount = newsCount;
	}
}
