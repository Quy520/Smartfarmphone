package com.smartfarm.view.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartfarm.net.bean.AppUser;
import com.smartfarm.view.R;
import com.smartfarm.view.util.StringUtils;
import com.smartfarm.widget.AvatarView;

public class UserAdapter extends ListBaseAdapter<AppUser>{

	@SuppressLint("InflateParams")
	@Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
		
        ViewHolder vh = null;
        
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_user, null);
            vh = new ViewHolder();
            convertView.setTag(vh);
            
            vh.name = (TextView) convertView.findViewById(R.id.user_item_name);
            vh.face = (AvatarView) convertView.findViewById(R.id.user_item_face);
            vh.signature = (TextView) convertView.findViewById(R.id.user_item_other);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        AppUser user = mDatas.get(position);

        vh.name.setText(user.getUserName());
        vh.face.setAvatarUrl(user.getImageUrl().getImgThumbUrl());
        
        if (!StringUtils.isEmpty(user.getSignature())) 
            vh.signature.setText(user.getSignature());
        else
        	vh.signature.setText("那家伙很懒，什么都没有留下来...");
        
        return convertView;
    }
	
	class ViewHolder {

        TextView name;
		AvatarView face;
        TextView signature;
    }
}
