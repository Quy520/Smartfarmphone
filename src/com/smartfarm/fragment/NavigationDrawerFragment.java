package com.smartfarm.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.User;
import com.smartfarm.msg.ProtocolFactory;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.CommunityActivity;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ConfigManager;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.ActionBarDrawerToggle;
import com.smartfarm.widget.AvatarView;
import com.smartfarm.widget.DrawerArrowDrawable;
import com.smartfarm.widget.dialog.CommonDialog;
import com.zxing.activity.CaptureActivity;

/**
 * 侧滑菜单界面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 下午6:00:05
 * 
 */
public class NavigationDrawerFragment extends BaseFragment implements
		OnClickListener {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	private static final int RESULT_OK = 0;

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerArrowDrawable drawerArrow;

	private DrawerLayout mDrawerLayout;
	private View mDrawerListView;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	
	private TextView userName;
	private AvatarView userFace;
	
	private TextView itemWater;
	private View itemWaterDividing;
	private TextView itemLight;
	private View itemLightDividing;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
		}

		selectItem(mCurrentSelectedPosition);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(mDrawerListView == null) {
			mDrawerListView = inflater
					.inflate(R.layout.fragment_navigation_drawer, container, false);
			mDrawerListView.setOnClickListener(this);

			userName = (TextView) mDrawerListView.findViewById(R.id.user_name);
			userFace = (AvatarView) mDrawerListView.findViewById(R.id.user_face);
			itemWater = (TextView) mDrawerListView.findViewById(R.id.menu_item_water);
			itemWaterDividing = mDrawerListView.findViewById(R.id.menu_item_water_dividing);
			itemLight = (TextView) mDrawerListView.findViewById(R.id.menu_item_light);
			itemLightDividing = mDrawerListView.findViewById(R.id.menu_item_light_dividing);
			
			mDrawerListView.findViewById(R.id.menu_item_user).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_setting).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_exit).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_about).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_help).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_peng).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_history).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_news).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_water).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_light).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.add).setOnClickListener(this);

			userFace.setClickable(false);
			
			initView(mDrawerListView);
			initData();
		}
		
		return mDrawerListView;
	}
	
	@Override
	public void onResume() {

		User user = AppContext.context().getUser();
			
		if(user == null) {
			userName.setText("未登录");
		} else {
			userName.setText(user.getName());
	        userFace.setAvatarUrl(user.getFace());	
		}
		
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.menu_item_user:
			
			if(AppContext.context().isLogin()) 
				UIHelper.showSimpleBack(getActivity(), BackPage.USER_INFO);
			else 
				UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
			
			break;
		case R.id.menu_item_peng:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.PENG_MANAGER);
			break;
		case R.id.menu_item_water:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.WATER);
			break;
		case R.id.menu_item_light:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.LIGHT);
			break;
		case R.id.menu_item_news:
			
			if(AppContext.context().isLogin()) {
				
				Intent intent = new Intent(getActivity(), CommunityActivity.class);
		        getActivity().startActivity(intent);
			} else 
				UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
			break;
		case R.id.menu_item_history:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.HISTORY);
			break;
		case R.id.menu_item_help:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.HELP);
			break;
		case R.id.menu_item_setting:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.SETTING);
			break;
			//添加扫描按钮
		case R.id.add:
			ToastTool.showToast("shaomiao");
			Intent intent=new Intent(getActivity(),CaptureActivity.class);
			startActivityForResult(intent,0);
			Log.i("123", "132");
			
			break;
		case R.id.menu_item_exit:

			final CommonDialog sureDialog = new CommonDialog(getActivity());
			sureDialog.setCancelable(false);
			sureDialog.setMessage("确定要重启温控机吗？");
			sureDialog.setTitle("操作确认");
			sureDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					ProtocolFactory.GetNoticeProtocol("close").send();
					sureDialog.dismiss();
				}
			});
			sureDialog.setNegativeButton("取消", null);
			sureDialog.show();
			break;
		case R.id.menu_item_about:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.ABOUT);
			break;
		default:
			break;
		}
		mDrawerLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				mDrawerLayout.closeDrawers();
			}
		}, 400);
	}
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null&requestCode==RESULT_OK){
		 String result = data.getExtras().getString("result"); 
		 Log.i("123", result);
			ToastTool.showToast(result);
		}
	}

	@Override
	public void initView(View view) {

	}

	@Override
	public void initData() {
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	@SuppressLint("NewApi")
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		
		if(android.os.Build.VERSION.SDK_INT >= 14) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}

		drawerArrow = new DrawerArrowDrawable(getActivity()) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};

		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
				drawerArrow, R.string.navigation_drawer_open,
				R.string.navigation_drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActivity().invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActivity().invalidateOptionsMenu();
				
				int showWater = ConfigManager.getInstance().getBoolean(
						ConfigManager.KEY_ENABLE_WATER) ? View.VISIBLE : View.GONE;
				
				itemWater.setVisibility(showWater);
				itemWaterDividing.setVisibility(showWater);

				int showLight = ConfigManager.getInstance().getBoolean(
						ConfigManager.KEY_ENABLE_LIGHT) ? View.VISIBLE : View.GONE;
				
				itemLight.setVisibility(showLight);
				itemLightDividing.setVisibility(showLight);
			}
		};

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public void openDrawerMenu() {
		mDrawerLayout.openDrawer(mFragmentContainerView);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	public static interface NavigationDrawerCallbacks {
		void onNavigationDrawerItemSelected(int position);
	}
}
