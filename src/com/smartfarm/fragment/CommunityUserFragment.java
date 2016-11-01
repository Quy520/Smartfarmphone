package com.smartfarm.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.db.bean.User;
import com.smartfarm.db.util.UserDao;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.ApiUpdateUser;
import com.smartfarm.tools.FileUtil;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ImageUtils;
import com.smartfarm.view.util.StringUtils;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.AvatarView;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.CommonDialog.OnEditSubmitListener;

public class CommunityUserFragment extends BaseFragment {

    private final static int CROP = 200;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/Smartfarm/Portrait/";
    
    private Uri origUri;
    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    protected String theLarge;
    
	private User user;
	
	private AvatarView userFace;
	private TextView userName;
	private TextView userAccount;
	private TextView userSex;
	private TextView userAddr;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_community_user;
	}

	@Override
	public void initView(View view) {

		userSex = (TextView) view.findViewById(R.id.user_tv_sax);
		userName = (TextView) view.findViewById(R.id.user_tv_name);
		userAddr = (TextView) view.findViewById(R.id.user_tv_addr);
		userFace = (AvatarView) view.findViewById(R.id.user_iv_face);
		userAccount = (TextView) view.findViewById(R.id.user_tv_phone);
		
		view.findViewById(R.id.user_btn_sex).setOnClickListener(this);
		view.findViewById(R.id.user_btn_name).setOnClickListener(this);
		view.findViewById(R.id.user_btn_addr).setOnClickListener(this);
		view.findViewById(R.id.user_btn_code).setOnClickListener(this);
		view.findViewById(R.id.user_btn_face).setOnClickListener(this);
		view.findViewById(R.id.user_btn_index).setOnClickListener(this);
		
		userFace.setClickable(false);
	}

	@Override
	public void initData() {
		
		user = AppContext.context().getUser();
        String contentPattern = getActivity()
        		.getResources().getString(R.string.user_detail_content);

        userFace.setAvatarUrl(user.getFace());	
        setTextAndStyle(userName, contentPattern, user.getName());
        setTextAndStyle(userAccount, contentPattern, user.getPhone());
        setTextAndStyle(userAddr, contentPattern, user.getAddr());
        setTextAndStyle(userSex, contentPattern, user.isSex() ? "男" : "女");
	}

	private void setTextAndStyle(TextView tv, String pattern, String content) {
    	
    	if(!ShowUtil.isEmpty(content)) {

    		tv.setText(String.format(pattern, content));
    		tv.setTextColor(getActivity().getResources().getColor(R.color.sky_blue));
        } else {
        	
        	tv.setText(R.string.user_detail_null);
        	tv.setTextColor(getActivity().getResources().getColor(R.color.main_gray));
        }
    }

	@Override
	public void onClick(View v) {

		CommonDialog show = null;
		
		switch(v.getId()) {
		
		case R.id.user_btn_addr:
			
			show = new CommonDialog(getActivity());
			show.setTitle("请输入新的地址");
			show.setEdit(new OnEditSubmitListener() {
				
				@Override
				public void onSubmit(DialogInterface dialog, String edit) {
					
					if(!ShowUtil.isEmpty(edit)) {
						
						showLoadingDialog();
						
						ApiUpdateUser uploadUser = new ApiUpdateUser();
						uploadUser.setAddr(edit);
						uploadUser.setSex(user.isSex());
						user.setAddr(edit);
						
						SmartfarmNetHelper.appUpdate(uploadUser, new UpdateHandler());
					}
				}
			}, "修改", user.getAddr());
			show.setNegativeButton("取消", null);
			show.show();
			break;
		case R.id.user_btn_code:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.MY_CODE);
			break;
		case R.id.user_btn_face:
			
			showClickAvatar();
			break;
		case R.id.user_btn_index:

//			UIHelper.showSimpleBack(getActivity(), BackPage.USER_CENTER, ApiContants.USER_CENTER_ME);
			break;
		case R.id.user_btn_name:
			
			show = new CommonDialog(getActivity());
			show.setTitle("请输入新的昵称");
			show.setEdit(new OnEditSubmitListener() {
				
				@Override
				public void onSubmit(DialogInterface dialog, String edit) {
					
					if(!ShowUtil.isEmpty(edit)) {
						
						showLoadingDialog();
						
						ApiUpdateUser uploadUser = new ApiUpdateUser();
						uploadUser.setName(edit);
						uploadUser.setSex(user.isSex());
						user.setName(edit);
						
						SmartfarmNetHelper.appUpdate(uploadUser, new UpdateHandler());
					}
				}
			}, "修改", user.getName());
			show.setNegativeButton("取消", null);
			show.show();
			break;
		case R.id.user_btn_sex:
			
			show = new CommonDialog(getActivity());
			show.setMessage("请选择性别");
			show.setNegativeButton("女", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					showLoadingDialog();
					dialog.dismiss();

					ApiUpdateUser uploadUser = new ApiUpdateUser();
					uploadUser.setSex(false);
					user.setSex(false);
					
					SmartfarmNetHelper.appUpdate(uploadUser, new UpdateHandler());
				}
			});
			show.setPositiveButton("男", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					showLoadingDialog();
					dialog.dismiss();

					ApiUpdateUser uploadUser = new ApiUpdateUser();
					uploadUser.setSex(true);
					user.setSex(true);
					
					SmartfarmNetHelper.appUpdate(uploadUser, new UpdateHandler());
				}
			});
			show.show();
			break;
		}
	}
	class UpdateHandler extends BaseAsyncHttpResHandler {

		@Override
		public void onFailure(int errorCode, String data) {
			
			hideLoadingDialog();
			AppContext.context().setUser(UserDao.findById(user.getId()));
			ToastTool.showToastLong("网络连接异常，错误代码：" + errorCode);
			initData();
		}

		@Override
		public void onSuccess(ApiResponse res) {
			
			hideLoadingDialog();
			
			if(res.getErrorCode() < 0) {
				
				User user = AppContext.context().getUser();
				ToastTool.showToastLong(res.getMessage());
				AppContext.context().setUser(UserDao.findById(user.getId()));
				initData();
			} else {
				
				ToastTool.showToast("修改成功！");
				
				User user = AppContext.context().getUser();
				UserDao.update(user);
				AppContext.context().setUser(user);
				initData();
			}
		}
	}
	
	public void showClickAvatar() {
		
        if (user == null) {
        	
            ToastTool.showToast("获取用户信息失败，请稍后再试!");
            getActivity().finish();
            return;
        }
        
        handleSelectPicture();
    }
	
	private void handleSelectPicture() {
		
        final CommonDialog dialog = new CommonDialog(getActivity(), R.style.dialog_common);
        
		dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.choose_picture);
        dialog.setNegativeButton(R.string.cancle, null);
        dialog.setItemsWithoutChk(
                getResources().getStringArray(R.array.choose_picture),
                new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
                        dialog.dismiss();
                        goToSelectPicture(position);
                    }
                });
        dialog.show();
    }

    private void goToSelectPicture(int position) {
    	
        switch (position) {
        case 0:
        	
            startImagePick();
            break;
        case 1:
        	
            startTakePhoto();
            break;
        default:
            break;
        }
    }
    
    /**
     * 选择图片裁剪
     * 
     * @param output
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }
    
    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/oschina/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
            ToastTool.showToast("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(new Date());
        String fileName = "sf_face_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;

        theLarge = savePath + fileName;// 该照片的绝对路径

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
        	ToastTool.showToast("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(getActivity(), uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "sf_face_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    /**
     * 拍照后裁剪
     * 
     * @param data
     *            原始图片
     * @param output
     *            裁剪后图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
            final Intent imageReturnIntent) {
    	
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
            startActionCrop(origUri);// 拍照后裁剪
            break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
            startActionCrop(imageReturnIntent.getData());// 选图后裁剪
            break;
        case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
            uploadNewPhoto();
            break;
        }
    }
    /**
     * 上传新照片
     */
    private void uploadNewPhoto() {
    	
    	showLoadingDialog("正在上传头像...");

        // 获取头像缩略图
        if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 200, 200);
        } else {
            ToastTool.showToast("图像不存在，上传失败");
        }
            
        boolean res = SmartfarmNetHelper.uploadPortrait(protraitFile, new BaseAsyncHttpResHandler() {

					@Override
					public void onFailure(int errorCode, String data) {
						ToastTool.showToast("更换头像失败");
					}
					@Override
					public void onSuccess(ApiResponse res) {
						if(res.getErrorCode() > 0 && res.getResponseData() != null) {
                            ToastTool.showToast("更换成功");
                            userFace.setImageBitmap(protraitBitmap);
                            user.setFace(res.getResponseData().toString());
                            UserDao.update(user);
                        } else {
                            ToastTool.showToast("更换头像失败");
                        }
					}
                    @Override
                    public void onFinish() {
                        hideLoadingDialog();
                    }
                });
        if(!res) {
			ToastTool.showToast("图像不存在，上传失败!");
            hideLoadingDialog();
        }
    }
}
