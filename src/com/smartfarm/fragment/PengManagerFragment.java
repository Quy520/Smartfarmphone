package com.smartfarm.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.tools.AssistThreadWork;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.HelperThread;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.NetManager;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.CommonDialog.OnEditSubmitListener;


public class PengManagerFragment extends BaseFragment {

	private TextView totalText;
	private List<PengInfo> allPeng = new ArrayList<PengInfo>();
	private PengListAdapter mAdapter;
	private View view;
	
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	if(view == null) {
    		
    		view = inflater.inflate(R.layout.fragment_peng_manager, container, false);

            initView(view);
    	}
    	
        initData();
        
        return view;
    }

    private void removePeng(final int id) {
    	
    	CommonDialog dialog = new CommonDialog(getActivity());
		dialog.setTitle("确认操作");
		dialog.setMessage("删除此大棚后所有相关数据都将被删除，确定要删除该大棚吗？");
		dialog.setNegativeButton("取消", null);
		dialog.setPositiveButton("删除",
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						dialog.dismiss();
						showLoadingDialog();
						HelperThread.getInstance().setThreadWork(new AssistThreadWork() {
							
							@Override
							public void working() {
								
								PengInfoDao.delete(id);

								if(AppContext.context().getCurrPengInfo().getId() == id)
									AppContext.context().setCurrPengInfo(PengInfoDao.findAll().get(0));
								
								NetManager.getInstence().resetNetInfo();
							}
							
							@Override
							public void ok() {
								
								EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_PENG_CHANGE));
								hideLoadingDialog();
								ToastTool.showToast("删除成功!");
								refresh();
							}
						});
					}
				});
		dialog.show();
    }
    
	@Override
	public void initView(View view) {

		mAdapter = new PengListAdapter();
		totalText = (TextView) view.findViewById(R.id.peng_total_info);
		ListView list = (ListView) view.findViewById(R.id.peng_list);
		
		list.setAdapter(mAdapter);
	}

	@Override
	public void onResume() {
		
		refresh();
		super.onResume();
	}

	private void refresh() {
		allPeng = PengInfoDao.findAll();
		mAdapter.notifyDataSetChanged();
		
		totalText.setText(String.format(getString(R.string.peng_total_info), allPeng.size()));
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.add, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		CommonDialog dialog = new CommonDialog(getActivity());
		dialog.setTitle("请输入需要添加的大棚的名字：");
		dialog.setEdit(new OnEditSubmitListener() {

			@Override
			public void onSubmit(DialogInterface dialog, final String edit) {

				showLoadingDialog();
				HelperThread.getInstance().setThreadWork(new AssistThreadWork() {
					
					@Override
					public void working() {
						PengInfo info = PengInfoDao.add(edit);
						
						if(info != null) 
							AppContext.context().setCurrPengInfo(info);

						NetManager.getInstence().resetNetInfo();
					}
					
					@Override
					public void ok() {
						EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_PENG_CHANGE));
						hideLoadingDialog();
						ToastTool.showToast("添加成功!");
						refresh();
					}
				});
			}
		}, "添加");
		dialog.show();
		
		return true;
	}
	
	class PengListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return allPeng.size();
		}

		@Override
		public Object getItem(int position) {
			return allPeng.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if(convertView == null) {
				
				holder = new ViewHolder();
				convertView = inflateView(R.layout.peng_list_item);
				
				holder.name = (TextView) convertView.findViewById(R.id.peng_item_name);
				holder.num = (TextView) convertView.findViewById(R.id.peng_item_num);
				holder.time = (TextView) convertView.findViewById(R.id.peng_item_time);
				
				registerForContextMenu(convertView);
				convertView.setTag(holder);
			} else {
				
				holder = (ViewHolder) convertView.getTag();
			}
			
			PengInfo info = allPeng.get(position);
			
			holder.name.setText(info.getName());
			holder.num.setText(ShowUtil.isEmpty(info.getPhoneNum()) ? "无" : info.getPhoneNum());
			holder.time.setText(CommonTool.getDateString(new Date(info.getCreateTime())));

			convertView.setContentDescription(String.valueOf(position));
			
			return convertView;
		}
		
		class ViewHolder {
			
			TextView name;
			TextView num;
			TextView time;
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		int position = Integer.valueOf(v.getContentDescription().toString());

		menu.add(0, position, 0, "移除");
		menu.add(1, position, 0, "重命名");
	}
	
	@Override
	public boolean onContextItemSelected(final MenuItem item) {

		if(item.getGroupId() == 0) {
			
			removePeng(allPeng.get(item.getItemId()).getId());
		} else {
			
			CommonDialog dialog = new CommonDialog(getActivity());
			dialog.setTitle("请输入新的大棚的名字：");
			dialog.setEdit(new OnEditSubmitListener() {

				@Override
				public void onSubmit(DialogInterface dialog, final String edit) {

					if(edit.equals(""))
						return;
					
					showLoadingDialog();
					HelperThread.getInstance().setThreadWork(new AssistThreadWork() {
						
						@Override
						public void working() {
							PengInfo info = allPeng.get(item.getItemId());
							
							info.setName(edit);
							PengInfoDao.update(info);
							AppContext.context().setCurrPengInfo(info);
						}
						
						@Override
						public void ok() {
							EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_PENG_CHANGE));
							hideLoadingDialog();
							ToastTool.showToast("修改成功!");
							refresh();
						}
					});
				}
			}, "修改");
			dialog.show();
		}
		
		return true;
	}
}
