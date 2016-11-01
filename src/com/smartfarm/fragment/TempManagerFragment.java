package com.smartfarm.fragment;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfarm.base.BaseListFragment;
import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.db.util.InfoRecordDao;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.R;
import com.smartfarm.view.SimpleBackActivity;
import com.smartfarm.view.adapter.ListBaseAdapter;
import com.smartfarm.view.util.StringUtils;

public class TempManagerFragment extends BaseListFragment<InfoRecord> {

	private int type = 0;
	
	@Override
	public void initView(View view) {
		super.initView(view);
		
		type = getArguments().getInt(SimpleBackActivity.BUNDLE_KEY_ARGS);
	}

	@Override
	protected ListBaseAdapter<InfoRecord> getListAdapter() {
		return new InfoAdapter();
	}

	@Override
	protected List<InfoRecord> requestData(int page) {
		return InfoRecordDao.findByPage(page, type);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}
	
	class InfoAdapter extends ListBaseAdapter<InfoRecord> {
		
		@SuppressLint("InflateParams")
	    @Override
	    protected View getRealView(int position, View convertView, ViewGroup parent) {
	        ViewHolder vh = null;
	        if (convertView == null || convertView.getTag() == null) {
	            convertView = getLayoutInflater(parent.getContext()).inflate(
	                    R.layout.list_cell_temp, null);
	            vh = new ViewHolder();
	            
	            vh.title = (TextView) convertView.findViewById(R.id.tv_title);
	            vh.description = (TextView) convertView.findViewById(R.id.tv_description);
	            vh.source = (TextView) convertView.findViewById(R.id.tv_source);
	            vh.time = (TextView) convertView.findViewById(R.id.tv_time);
	            vh.tip = (ImageView) convertView.findViewById(R.id.iv_tip);
	            
	            convertView.setTag(vh);
	        } else {
	            vh = (ViewHolder) convertView.getTag();
	        }

	        InfoRecord record = mDatas.get(position);
	        vh.title.setText(record.getPengName());
	        vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(R.color.main_black));

	        String description = record.getData();
	        
	        vh.description.setVisibility(View.GONE);
	        if (description != null && !ShowUtil.isEmpty(description)) {
	            vh.description.setVisibility(View.VISIBLE);
	            
	            if(type == InfoRecord.RECORD_TYPE_TEMP)
	            	vh.description.setText(CommonTool.getTempString(description));
	            else
	            	vh.description.setText(description);
	        }

	        Date data = new Date(record.getTime());
	        vh.source.setText(record.getPengPhoneNum());
	        vh.time.setText(CommonTool.getDateString(data));
	        
	        if (StringUtils.isToday(data)) {
	            vh.tip.setVisibility(View.VISIBLE);
	        } else {
	            vh.tip.setVisibility(View.GONE);
	        }

	        return convertView;
	    }
	}
	
	static class ViewHolder {
        TextView title;
        TextView description;
        TextView source;
        TextView time;
        ImageView tip;
    }
}
