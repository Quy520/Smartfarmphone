package com.smartfarm.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.User;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.AvatarView;
import com.smartfarm.widget.dialog.CommonDialog;

public class UserInfoFragment extends BaseFragment {
	
	private AvatarView userFace;
	private TextView userName;
	private TextView userAccount;
    private View view;
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	
        if(view == null) {
        	
        	view = inflater.inflate(R.layout.fragment_user_info, container, false);

            userFace = (AvatarView) view.findViewById(R.id.user_face);
            userName = (TextView) view.findViewById(R.id.user_name);
            userAccount = (TextView) view.findViewById(R.id.user_account);
            
            userFace.setOnClickListener(this);
            view.findViewById(R.id.user_feedback).setOnClickListener(this);
            view.findViewById(R.id.user_info).setOnClickListener(this);
            view.findViewById(R.id.user_setting).setOnClickListener(this);
            view.findViewById(R.id.user_exit).setOnClickListener(this);
            view.findViewById(R.id.user_modify).setOnClickListener(this);
            view.findViewById(R.id.user_collect).setOnClickListener(this);
        }
        
        return view;
    }

	@Override
	public void onResume() {
		
		User user = AppContext.context().getUser();
		
		if(user != null) {

	        userFace.setAvatarUrl(user.getFace());
			userName.setText(user.getName());
	        userAccount.setText(user.getPhone());
		}
		
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.user_collect:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.COLLECT);
			break;
		case R.id.user_face:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.USER_DETAIL);
			break;
		case R.id.user_info:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.USER_DETAIL);
			break;
		case R.id.user_modify:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.MODIFY_PWD);
			getActivity().finish();
			break;
		case R.id.user_feedback:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.SUGGEST);
			break;
		case R.id.user_setting:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.SETTING);
			break;
		case R.id.user_exit:
			
			CommonDialog sureDialog = new CommonDialog(getActivity());
			sureDialog.setMessage("确认要退出帐号嘛？");
			sureDialog.setTitle("确认");
			sureDialog.setNegativeButton("取消", null);
			sureDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					AppContext.context().getAccountManager().exit();
					UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
					dialog.dismiss();
					getActivity().finish();
				}
			});
			sureDialog.show();
			break;
		}
	}
    
    
}
