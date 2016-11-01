package com.smartfarm.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.view.R;

public class UserProtocolFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return getMessageView(getUserProtocol(getActivity()));
	}
	
	private ScrollView getMessageView(Spanned spanned) {
		ScrollView scrollView = new ScrollView(getActivity());
		scrollView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));
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
	
	public Spannable getUserProtocol(Context context) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					context.getAssets().open("user_protocol.txt"), "GBK"));
			
			String line = "";
			while((line = in.readLine()) != null) {

				result.append(line);
				result.append("\r\n");
			}
			
			SpannableStringBuilder builder = new SpannableStringBuilder();
			builder.append(result);
			
			return builder;
		} catch(Exception e) {
			return null;
		} finally {
			try {
				if(in != null)
					in.close();
			} catch(Exception e) {}
		}
	}
}
