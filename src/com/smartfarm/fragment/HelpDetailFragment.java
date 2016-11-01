package com.smartfarm.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.ShowContentBean;
import com.smartfarm.view.R;
import com.smartfarm.view.SimpleBackActivity;


public class HelpDetailFragment extends BaseFragment {

	private View view;
	private TextView title;
	private FrameLayout container;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(view == null) {
			
			view = inflater.inflate(R.layout.fragment_help_detail, container, false);
			
			initView(view);
		}
		
		initData();
		
		return view;
	}

	@Override
	public void initData() {
		
		Bundle bundle = getArguments();
		SpannableStringBuilder builder = new SpannableStringBuilder();
		ShowContentBean bean = getShowContent(bundle.getInt(SimpleBackActivity.BUNDLE_KEY_ARGS));

		title.setText(bean.title);
		container.removeAllViews();
		builder.append(bean.content);
		container.addView(getMessageView(builder));
	}

	private ShowContentBean getShowContent(int currHelpType) {
		
		Activity activity = getActivity();
		
		switch(currHelpType) {
		case R.id.help_title_msg_cmd:
			
			return new ShowContentBean(activity.getString(R.string.help_title_msg_cmd), 
					activity.getString(R.string.help_content_msg_cmd));
		case R.id.help_title_msg_control:
			
			return new ShowContentBean(activity.getString(R.string.help_title_msg_control), 
					activity.getString(R.string.help_content_msg_control));
		case R.id.help_title_msg_temp:
			
			return new ShowContentBean(activity.getString(R.string.help_title_msg_temp), 
					activity.getString(R.string.help_content_msg_temp));
		case R.id.help_title_net_alarm:
			
			return new ShowContentBean(activity.getString(R.string.help_title_net_alarm), 
					activity.getString(R.string.help_content_net_alarm));
		case R.id.help_title_net_diagnosis:
			
			return new ShowContentBean(activity.getString(R.string.help_title_net_diagnosis), 
					activity.getString(R.string.help_content_net_diagnosis));
		case R.id.help_title_net_mode:
			
			return new ShowContentBean(activity.getString(R.string.help_title_net_mode), 
					activity.getString(R.string.help_content_net_mode));
		case R.id.help_title_net_relink:
			
			return new ShowContentBean(activity.getString(R.string.help_title_net_relink), 
					activity.getString(R.string.help_content_net_relink));
		case R.id.help_title_net_syn:
			
			return new ShowContentBean(activity.getString(R.string.help_title_net_syn), 
					activity.getString(R.string.help_content_net_syn));
		case R.id.help_title_net_temp:
			
			return new ShowContentBean(activity.getString(R.string.help_title_net_temp), 
					activity.getString(R.string.help_content_net_temp));
		case R.id.help_title_net_ven:
			
			return new ShowContentBean(activity.getString(R.string.help_title_net_ven), 
					activity.getString(R.string.help_content_net_ven));
		case R.id.help_title_net_win:
			
			return new ShowContentBean(activity.getString(R.string.help_title_net_win), 
					activity.getString(R.string.help_content_net_win));
		case R.id.help_title_precautions:
			
			return new ShowContentBean(activity.getString(R.string.help_title_precautions), 
					activity.getString(R.string.help_content_precautions));
		case R.id.help_title_set_function:
			
			return new ShowContentBean(activity.getString(R.string.help_title_set_function), 
					activity.getString(R.string.help_content_set_function));
		case R.id.help_title_set_house:
			
			return new ShowContentBean(activity.getString(R.string.help_title_set_house), 
					activity.getString(R.string.help_content_set_house));
		case R.id.help_title_set_win:
			
			return new ShowContentBean(activity.getString(R.string.help_title_set_win), 
					activity.getString(R.string.help_content_set_win));
		case R.id.help_title_setting:
			
			return new ShowContentBean(activity.getString(R.string.help_title_setting), 
					activity.getString(R.string.help_content_setting));
		case R.id.help_title_share:
			
			return new ShowContentBean(activity.getString(R.string.help_title_share), 
					activity.getString(R.string.help_content_share));
		default:
			
			return new ShowContentBean("Error", "None");
		}
	}
	
	private ScrollView getMessageView(Spanned spanned) {
		ScrollView scrollView = new ScrollView(getActivity());
		scrollView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		TextView tvMessage = new TextView(getActivity(), null,
				R.style.dialog_pinterest_text);
		tvMessage.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		tvMessage.setPadding(10, 10, 10, 10);
		tvMessage.setLineSpacing(0.0F, 1.3F);
		tvMessage.setText(spanned != null ? spanned : "");
		tvMessage.setTextColor(getActivity().getResources().getColor(
				R.color.black));

		ScrollView.LayoutParams lp = new ScrollView.LayoutParams(
				ScrollView.LayoutParams.MATCH_PARENT,
				ScrollView.LayoutParams.WRAP_CONTENT);
		scrollView.addView(tvMessage, lp);
		
		return scrollView;
	}
	
	@Override
	public void initView(View view) {
		
		title = (TextView) view.findViewById(R.id.help_title);
		container = (FrameLayout) view.findViewById(R.id.help_container);
	}

	
	
}
