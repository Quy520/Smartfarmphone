package com.smartfarm.view.adapter;

import org.kymjs.kjframe.KJBitmap;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfarm.net.bean.AppMessage;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.R;
import com.smartfarm.view.util.StringUtils;

public class NoteAdapter extends ListBaseAdapter<AppMessage>{

	private static KJBitmap kjb = new KJBitmap();
	
	@SuppressLint("InflateParams")
	@Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
		
        ViewHolder vh = null;
        
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_note, null);
            vh = new ViewHolder();
            convertView.setTag(vh);

            vh.img = (ImageView) convertView.findViewById(R.id.item_iv_img);
            vh.time = (TextView) convertView.findViewById(R.id.item_tv_time);
            vh.title = (TextView) convertView.findViewById(R.id.item_tv_title);
            vh.location = (TextView) convertView.findViewById(R.id.item_tv_location);
            vh.comment_count = (TextView) convertView.findViewById(R.id.item_tv_comment_count);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        AppMessage note = mDatas.get(position);
        
        if (StringUtils.isEmpty(note.getContent())) 
        	vh.title.setVisibility(View.GONE);
        else {
            vh.title.setVisibility(View.VISIBLE);
            vh.title.setText(note.getContent());
        }

        if(ShowUtil.isEmpty(note.getLocation()))
        	vh.location.setVisibility(View.GONE);
        else {
        	vh.location.setVisibility(View.VISIBLE);
        	vh.location.setText(note.getLocation());
        }
        
        vh.time.setText(StringUtils.friendlyTime(note.getPubTime()));
        vh.comment_count.setText(String.valueOf(note.getCommentCount()));
        
        if(ShowUtil.isEmpty(note.getImg()))
        	vh.img.setVisibility(View.GONE);
        else {
        	vh.img.setVisibility(View.VISIBLE);
//        	kjb.display(vh.img, note.getImgThumbUrl());
        }
        
        return convertView;
    }
	
	class ViewHolder {

        TextView title;
        TextView location;
        TextView time;
        TextView comment_count;
        ImageView img;
    }
}
