package com.smartfarm.fragment;

import java.io.Serializable;

import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.AppMessage;
import com.smartfarm.net.bean.MsgList;
import com.smartfarm.view.adapter.ListBaseAdapter;
import com.smartfarm.view.adapter.MsgAdapter;
import com.smartfarm.view.util.UIHelper;

public class CollectNoteFragment extends BaseAsyncListFragment<AppMessage> {

    protected static final String TAG = CollectFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "collect_msg_list";
    
	@Override
	protected ListBaseAdapter<AppMessage> getListAdapter() {
		return new MsgAdapter();
	}

    /**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected MsgList parseList(Object obj) throws Exception {
        return JSON.toJavaObject((JSON) obj, MsgList.class);
    }

    
    @Override
    protected MsgList readList(Serializable seri) {
        return ((MsgList) seri);
    }

    @Override
    protected void sendRequestData() {

//        SmartfarmNetHelper.getCollectMsg(mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
    	
    	AppMessage note = mAdapter.getItem(position);
        if (note != null) {
        	UIHelper.showMsgDetail(getActivity(), note.getMsgId(), note);
        }
    }

}
