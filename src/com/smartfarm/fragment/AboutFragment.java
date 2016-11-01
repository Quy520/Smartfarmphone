package com.smartfarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qihoo.updatesdk.lib.UpdateHelper;
import com.smartfarm.base.BaseFragment;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.GuildActivity;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.dialog.CommonDialog;

public class AboutFragment extends BaseFragment {

	private TextView mVersionName;
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
    	
    	mVersionName = (TextView) view.findViewById(R.id.about_version_name);
    	
        view.findViewById(R.id.about_feedback).setOnClickListener(this);
        view.findViewById(R.id.about_check_update).setOnClickListener(this);
        view.findViewById(R.id.about_knowmore).setOnClickListener(this);
        view.findViewById(R.id.about_oscsite).setOnClickListener(this);
        view.findViewById(R.id.about_new_version).setOnClickListener(this);
        view.findViewById(R.id.about_user_protocol).setOnClickListener(this);
        view.findViewById(R.id.about_guild).setOnClickListener(this);
    }

    @Override
    public void initData() {
        mVersionName.setText(String.format(getString(R.string.query_version),
				CommonTool.getVersion(getActivity())));
    }
    
    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
        case R.id.about_feedback:

        	UIHelper.showSimpleBack(getActivity(), BackPage.SUGGEST);
            break;
        case R.id.about_guild:

        	Intent it = new Intent(getActivity(), GuildActivity.class);
        	startActivity(it);
        	getActivity().finish();
        	
            break;
        case R.id.about_user_protocol:
        	
        	UIHelper.showSimpleBack(getActivity(), BackPage.USER_PROTOCPL);
            break;
        case R.id.about_check_update:

        	/*UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int arg0,
						UpdateResponse arg1) {

					if (arg0 == UpdateStatus.No) {
						ToastTool.showToast("当前已经是最新版本!");
					}
				}
			});
        	UmengUpdateAgent.forceUpdate(getActivity());*/
        	UpdateHelper.getInstance().manualUpdate(getActivity().getPackageName());
            break;
        case R.id.about_knowmore:
        	
        	ShowUtil.openUrl(getActivity(), Constants.MORE_SITE);
            break;
        case R.id.about_oscsite:
            
        	ShowUtil.openUrl(getActivity(), Constants.ZT_SITE);
            break;
        case R.id.about_new_version:
        	
        	SpannableStringBuilder builder = new SpannableStringBuilder();
			builder.append(getActivity().getString(R.string.update_log));
			
			CommonDialog newVersion = new CommonDialog(getActivity());
        	newVersion.setTitle(R.string.new_version_descr);
        	newVersion.setPositiveButton("确定", null);
        	newVersion.setMessage(builder);
			
        	newVersion.show();
            break;
        default:
            break;
        }
    }
}
