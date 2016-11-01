package com.smartfarm.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.smartfarm.base.BaseAsyncListFragment;
import com.smartfarm.net.ApiContants;
import com.smartfarm.net.bean.Comment;
import com.smartfarm.net.bean.CommentList;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.adapter.CommentAdapter;
import com.smartfarm.view.adapter.ListBaseAdapter;
import com.smartfarm.widget.AvatarView;

public class UserCenterFragment extends BaseAsyncListFragment<Comment> {

    protected static final String TAG = NoteFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "user_note_list";
    
    private AvatarView face;
    private TextView name;
    
	@Override
	public void initView(View view) {
		super.initView(view);
		
		View centerHead = inflateView(R.layout.user_center_head);

		name = (TextView) centerHead.findViewById(R.id.uc_name);
		face = (AvatarView) centerHead.findViewById(R.id.uc_face);
		
		mListView.addHeaderView(centerHead, null, false);
		mListView.setDivider(null);
	}
	
	@Override
	public void initData() {
		
		name.setText(AppContext.context().getUser().getName());
		
		if(mCatalog == ApiContants.USER_CENTER_ME)
			face.setAvatarUrl(AppContext.context().getUser().getFace());
		else
			face.setAvatarUrl("http://101.200.239.85:8080/ImageCache/b/test.jpg");
			
	}
	
	@Override
	protected ListBaseAdapter<Comment> getListAdapter() {
		return new CommentAdapter();
	}

    /**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected CommentList parseList(Object obj) throws Exception {
//    	CommentList list = XmlUtils.toBean(CommentList.class, is);
        return null;
    }

    @Override
    protected CommentList readList(Serializable seri) {
        return ((CommentList) seri);
    }

    @Override
    protected void sendRequestData() {

//        SmartfarmNetHelper.getNewsList(mCurrentPage, mCatalog, mHandler);
    	
    	ArrayList<Comment> res = new ArrayList<Comment>();
    	
    	for(int i = 0; i < 25; i++) {
    		
    		Comment note = new Comment();
    		
    		note.setContent("这是评论评论评论评论评论评论评论评论评论评论评论评论评论评论评论！");
//    		note.setLocation("上海市闵行区浦江镇竹园路88号");
//    		note.setPubDate(new Date(System.currentTimeMillis() - i * 1000 * 3600));
//    		note.setAuthor(mCatalog == ApiContants.USER_CENTER_ME ? "我" : ("用户"  + i));
    		res.add(note);
    	}
    	executeOnLoadDataSuccess(res);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
    	
    	Comment note = mAdapter.getItem(position);
        if (note != null) {
//            UIHelper.showNewsDetail(getActivity(), news.getId(), news.getCommentCount());
        }
    }
}
