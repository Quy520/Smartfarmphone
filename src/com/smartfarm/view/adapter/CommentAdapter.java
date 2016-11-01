package com.smartfarm.view.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartfarm.emoji.InputHelper;
import com.smartfarm.net.bean.Comment;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.R;
import com.smartfarm.widget.AvatarView;

public class CommentAdapter extends ListBaseAdapter<Comment>{

	@SuppressLint("InflateParams")
	@Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
		
        ViewHolder vh = null;
        
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_comment, null);
            vh = new ViewHolder();
            convertView.setTag(vh);

            vh.time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.face = (AvatarView) convertView.findViewById(R.id.iv_avatar);
            vh.content = (TextView) convertView.findViewById(R.id.tv_content);
            vh.location = (TextView) convertView.findViewById(R.id.tv_location);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Comment comment = mDatas.get(position);
        
        Spanned span = Html.fromHtml(comment.getContent().trim());
        span = InputHelper.displayEmoji(convertView.getResources(), span);
        
        if(!ShowUtil.isEmpty(comment.getMentionName())) {
        	
        	SpannableString atName = new SpannableString("@" + comment.getMentionName() + " ");
        	atName.setSpan(new ForegroundColorSpan(convertView.getResources().getColor(R.color.sky_blue)), 
        			0, atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.content.setText(atName);
        } else
        	vh.content.setText("");
        
        vh.content.append(span);
        vh.time.setText(comment.getFriendlyTime());
        vh.name.setText(comment.getAuthorName());
        vh.face.setAvatarUrl(comment.getPortraitUrl().getImgThumbUrl());

        
        vh.location.setVisibility(View.GONE);
        
        return convertView;
    }
	
	class ViewHolder {

		AvatarView face;
        TextView name;
        TextView time;
        TextView content;
        TextView location;
    }
}
