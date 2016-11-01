package com.smartfarm.fragment;

import android.view.View;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.net.ApiContants;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.UIHelper;

public class MyNoteFragment extends BaseFragment {
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_my_note;
	}

	@Override
	public void initView(View view) {
		
		view.findViewById(R.id.mn_btn_comment).setOnClickListener(this);
		view.findViewById(R.id.mn_btn_read).setOnClickListener(this);
		view.findViewById(R.id.mn_btn_submit).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		
		case R.id.mn_btn_comment:

			UIHelper.showSimpleBack(getActivity(), BackPage.COMMENT_HISTORY);
			break;
		case R.id.mn_btn_read:
			
//			UIHelper.showSimpleBack(getActivity(), BackPage.READ_NOTE);
			break;
		case R.id.mn_btn_submit:

			UIHelper.showSimpleBack(getActivity(), BackPage.NOTE, ApiContants.NOTE_TYPE_SELF);
			break;
		}
	}
}
