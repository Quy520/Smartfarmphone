package com.smartfarm.fragment;

import java.io.Serializable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.net.ApiContants;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.AppMessage;
import com.smartfarm.net.bean.MsgList;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.adapter.ListBaseAdapter;
import com.smartfarm.view.adapter.MsgAdapter;
import com.smartfarm.view.util.UIHelper;

public class NoteFragment extends BaseAsyncListFragment<AppMessage> implements EventHandler {

    protected static final String TAG = NoteFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "note_list";

	@Override
	protected ListBaseAdapter<AppMessage> getListAdapter() {
		return new MsgAdapter();
	}

    /**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }
    
    @Override
    public String getTitle() {
    	
    	switch(mCatalog) {
    	case ApiContants.NOTE_TYPE_RECOMMEND:
    		return "精彩从这里开始";
    	case ApiContants.NOTE_TYPE_AROUND:
    		return "关注身边发生的事";
    	case ApiContants.NOTE_TYPE_HELP:
    		return "帮助每个需要帮助的人";
    	case ApiContants.NOTE_TYPE_FRIEND:
    		return "精彩动态不容错过";
    	case ApiContants.NOTE_TYPE_CONCERN:
    		return "所见即所得";
    	case ApiContants.NOTE_TYPE_SELF:
    		return "我发布的消息";
    	}
    	
    	return super.getTitle();
    }

    @Override
    protected MsgList parseList(Object obj) throws Exception {

    	MsgList msgs = JSON.toJavaObject((JSON) obj, MsgList.class);
    	
        return msgs;
    }

    @Override
    protected MsgList readList(Serializable seri) {
        return ((MsgList) seri);
    }

    @Override
    protected void sendRequestData() {
    	
    	switch(mCatalog) {
    	case ApiContants.NOTE_TYPE_RECOMMEND:
    		
    		SmartfarmNetHelper.getRecommendMsg(mCurrentPage, mHandler);
    		break;
    	case ApiContants.NOTE_TYPE_AROUND:
    		
    		SmartfarmNetHelper.getNearbyMsg(mCurrentPage, AppContext.context().getCityCode(), mHandler);
    		break;
    	case ApiContants.NOTE_TYPE_HELP:
    		
    		SmartfarmNetHelper.getHelpMsg(mCurrentPage, mHandler);
    		break;
    	case ApiContants.NOTE_TYPE_FRIEND:
    		
    		SmartfarmNetHelper.getWatcherMsg(mCurrentPage, mHandler);
    		break;
    	case ApiContants.NOTE_TYPE_CONCERN:

    		SmartfarmNetHelper.getConcernMsg(mCurrentPage, mHandler);
    		break;
    	case ApiContants.NOTE_TYPE_SELF:

    		SmartfarmNetHelper.getSelfMsg(mCurrentPage, mHandler);
    		break;
    	}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
    	
        AppMessage note = mAdapter.getItem(position);
        if (note != null) {
        	UIHelper.showMsgDetail(getActivity(), note.getMsgId(), note);
        }
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sendRequestData();
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onEvent(LocalEvent event) {
		if(event.getEventType() == LocalEvent.EVENT_TYPE_PUB_MSG && mCatalog < 4)
			sendRequestData();
	}
}
