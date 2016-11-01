package com.smartfarm.view.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfarm.net.bean.News;
import com.smartfarm.view.R;

public class NewsListAdapter extends ListBaseAdapter<News> {
	
	@SuppressLint("InflateParams")
	@Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_news, null);
            vh = new ViewHolder();
            convertView.setTag(vh);
            
            vh.tip = (ImageView) convertView.findViewById(R.id.iv_tip);
            vh.time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.source = (TextView) convertView.findViewById(R.id.tv_source);
            vh.description = (TextView) convertView.findViewById(R.id.tv_description);
            vh.comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        News news = mDatas.get(position);

        vh.tip.setVisibility(View.GONE);
//        if (news.getHotType() == News.HOT_TYPE_NORMAL || news.getHotType() == 0) {
//
//        	if(StringUtils.isToday(news.getPubDate()))
//        		vh.tip.setImageResource(R.drawable.widget_today_icon);
//        	else
//        		vh.tip.setVisibility(View.GONE);
//        } else if(news.getHotType() == News.HOT_TYPE_HOT){
//            vh.tip.setImageResource(R.drawable.widget_hot_icon);
//        } else if(news.getHotType() == News.HOT_TYPE_UP){
//            vh.tip.setImageResource(R.drawable.widget_up_icon);
//        }

        vh.title.setText(news.getTitle());
        vh.description.setText(news.getContent());

        vh.source.setText(news.getFromDescr());
        vh.time.setText(news.getFriendlyTime());
        vh.comment_count.setText(String.valueOf(news.getCommentCount()));
        
        return convertView;
    }
	
	class ViewHolder {

        TextView title;
        TextView description;
        TextView source;
        TextView time;
        TextView comment_count;
        ImageView tip;
    }
}
