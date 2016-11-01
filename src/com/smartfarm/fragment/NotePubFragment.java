package com.smartfarm.fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfarm.base.BaseFragment;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.emoji.EmojiKeyboardFragment;
import com.smartfarm.emoji.Emojicon;
import com.smartfarm.emoji.InputHelper;
import com.smartfarm.emoji.OnEmojiClickListener;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.bean.PubMsg;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.FileUtil;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;
import com.smartfarm.view.util.BackPage;
import com.smartfarm.view.util.ImageUtils;
import com.smartfarm.view.util.SimpleTextWatcher;
import com.smartfarm.view.util.StringUtils;
import com.smartfarm.view.util.UIHelper;
import com.smartfarm.widget.dialog.CommonDialog;
import com.smartfarm.widget.dialog.DialogHelper;

public class NotePubFragment extends BaseFragment implements OnEmojiClickListener {

    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    public static final int ACTION_TYPE_RECORD = 2; 
    public static final int ACTION_TYPE_TOPIC = 3; 
    
    private static final String TEXT_ATME = "@请输入用户名 ";

	private View mLyImage;
	private EditText mEtInput;
	private TextView mTvClear;
	private ImageView mIvImage;
	private ImageButton mIbEmoji;
	private ImageButton mIbPicture;
	private ImageButton mIbMention;
	private ImageButton mIbTrendSoftware;

    private String theLarge, theThumbnail;
    private boolean mIsKeyboardVisible;
    
    private File imgFile;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
                mIvImage.setImageBitmap((Bitmap) msg.obj);
                mLyImage.setVisibility(View.VISIBLE);
            }
        }
    };

    private final EmojiKeyboardFragment keyboardFragment = new EmojiKeyboardFragment();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getActivity().getWindow().setSoftInputMode(mode);	
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.pub, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	handleSubmit();
        return true;
    }

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_note_pub;
	}

	@Override
	public void initView(View view) {

		mLyImage = view.findViewById(R.id.rl_img);
		mTvClear = (TextView) view.findViewById(R.id.tv_clear);
		mIvImage = (ImageView) view.findViewById(R.id.iv_img);
		mEtInput = (EditText) view.findViewById(R.id.et_content);
		mIbPicture = (ImageButton) view.findViewById(R.id.ib_picture);
		mIbMention = (ImageButton) view.findViewById(R.id.ib_mention);
		mIbEmoji = (ImageButton) view.findViewById(R.id.ib_emoji_keyboard);
		mIbTrendSoftware = (ImageButton) view.findViewById(R.id.ib_trend_software);
	
		mIbEmoji.setOnClickListener(this);
        mTvClear.setOnClickListener(this);
        mIbPicture.setOnClickListener(this);
        mIbMention.setOnClickListener(this);
        mIbTrendSoftware.setOnClickListener(this);
        mTvClear.setText(String.valueOf(Constants.MAX_TEXT_LENGTH));
        view.findViewById(R.id.iv_clear_img).setOnClickListener(this);
        
        getFragmentManager().beginTransaction()
        	.replace(R.id.emoji_keyboard_fragment, keyboardFragment)
        	.commit();
		keyboardFragment.setOnEmojiClickListener(new OnEmojiClickListener() {
		    @Override
		    public void onEmojiClick(Emojicon v) {
		        InputHelper.input2OSC(mEtInput, v);
		    }
		
		    @Override
		    public void onDeleteButtonClick(View v) {
		        InputHelper.backspace(mEtInput);
		    }
		});

        mEtInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                mTvClear.setText((Constants.MAX_TEXT_LENGTH - s.length()) + "");
            }
        });
	}

	@Override
	public void initData() {
		
		String noteDraft = AppContext.context().getNoteDraft();
		if(!ShowUtil.isEmpty(noteDraft)) {
			Spanned span = Html.fromHtml(noteDraft.trim());
	        span = InputHelper.displayEmoji(getActivity().getResources(), span);
			mEtInput.setText(span);
		    mEtInput.setSelection(mEtInput.getText().toString().length());
		}
		
	}
    
    @Override
    public boolean onBackPressed() {
    	
        final String note = mEtInput.getText().toString();
        
        if (!TextUtils.isEmpty(note)) {
            CommonDialog dialog = DialogHelper
                    .getPinterestDialogCancelable(getActivity());
            dialog.setMessage(R.string.draft_tweet_message);
            dialog.setNegativeButton(R.string.cancle, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                	
                    AppContext.context().setNoteDraft("");
                    getActivity().finish();
                }
            });
            dialog.setPositiveButton(R.string.ok, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                	
                    dialog.dismiss();
                    AppContext.context().setNoteDraft(note);
                    getActivity().finish();
                }
            });
            dialog.show();
            return true;
        }
        return super.onBackPressed();
    }

    private void handleSelectPicture() {
    	
        final CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        
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

    private void handleClearWords() {
    	
        if (TextUtils.isEmpty(mEtInput.getText().toString()))
            return;
        
        final CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        
        dialog.setMessage(R.string.clearwords);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mEtInput.getText().clear();
                        if (mIsKeyboardVisible) {
                            CommonTool.showSoftKeyboard(mEtInput);
                        }
                    }
                });
        dialog.setNegativeButton(R.string.cancle, null);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
    	
        final int id = v.getId();
        
        if (id == R.id.ib_picture) {
        	
            handleSelectPicture();
        } else if (id == R.id.ib_mention) {
        	
//            insertMention();
        } else if (id == R.id.ib_trend_software) {
            
        } else if (id == R.id.tv_clear) {
        	
            handleClearWords();
        } else if (id == R.id.iv_clear_img) {
        	
            mIvImage.setImageBitmap(null);
            mLyImage.setVisibility(View.GONE);
            imgFile = null;
        } else if (id == R.id.ib_emoji_keyboard) {
        	
            if (!keyboardFragment.isShow()) {// emoji隐藏中
                keyboardFragment.showEmojiKeyBoard();
                keyboardFragment.hideSoftKeyboard();
            } else {
                keyboardFragment.hideEmojiKeyBoard();
                keyboardFragment.showSoftKeyboard(mEtInput);
            }
        }
    }
    
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
            final Intent imageReturnIntent) {
    	
        if (resultCode != Activity.RESULT_OK)
            return;
        
        new Thread() {
        	
            private String selectedImagePath;

            @Override
            public void run() {
                Bitmap bitmap = null;

                if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) {
                    if (imageReturnIntent == null)
                        return;
                    Uri selectedImageUri = imageReturnIntent.getData();
                    if (selectedImageUri != null) {
                        selectedImagePath = ImageUtils.getImagePath(
                                selectedImageUri, getActivity());
                    }

                    if (selectedImagePath != null) {
                        theLarge = selectedImagePath;
                    } else {
                        bitmap = ImageUtils.loadPicasaImageFromGalley(
                                selectedImageUri, getActivity());
                    }

                    if (AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.ECLAIR_MR1)) {
                        String imaName = FileUtil.getFileName(theLarge);
                        if (imaName != null)
                            bitmap = ImageUtils.loadImgThumbnail(getActivity(),
                                    imaName,
                                    MediaStore.Images.Thumbnails.MICRO_KIND);
                    }
                    if (bitmap == null && !StringUtils.isEmpty(theLarge))
                        bitmap = ImageUtils
                                .loadImgThumbnail(theLarge, 100, 100);
                } else if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
                    // 拍摄图片
                    if (bitmap == null && !StringUtils.isEmpty(theLarge)) {
                        bitmap = ImageUtils
                                .loadImgThumbnail(theLarge, 100, 100);
                    }
                }

                if (bitmap != null) {// 存放照片的文件夹
                    String savePath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/smartfarm/Camera/";
                    File savedir = new File(savePath);
                    if (!savedir.exists()) {
                        savedir.mkdirs();
                    }

                    String largeFileName = FileUtil.getFileName(theLarge);
                    String largeFilePath = savePath + largeFileName;
                    // 判断是否已存在缩略图
                    if (largeFileName.startsWith("thumb_")
                            && new File(largeFilePath).exists()) {
                        theThumbnail = largeFilePath;
                        imgFile = new File(theThumbnail);
                    } else {
                        // 生成上传的800宽度图片
                        String thumbFileName = "thumb_" + largeFileName;
                        theThumbnail = savePath + thumbFileName;
                        if (new File(theThumbnail).exists()) {
                            imgFile = new File(theThumbnail);
                        } else {
                            try {
                                // 压缩上传的图片
                                ImageUtils.createImageThumbnail(getActivity(),
                                        theLarge, theThumbnail, 800, 80);
                                imgFile = new File(theThumbnail);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // 保存动弹临时图片
                    // ((AppContext) getApplication()).setProperty(
                    // tempTweetImageKey, theThumbnail);

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            };
        }.start();
    }
    
    private void goToSelectPicture(int position) {
        switch (position) {
        case ACTION_TYPE_ALBUM:
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"),
                        ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
            } else {
                intent = new Intent(Intent.ACTION_PICK,
                        Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"),
                        ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
            }
            break;
        case ACTION_TYPE_PHOTO:
            // 判断是否挂载了SD卡
            String savePath = "";
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                savePath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/smartfarm/Camera/";
                File savedir = new File(savePath);
                if (!savedir.exists()) {
                    savedir.mkdirs();
                }
            }

            // 没有挂载SD卡，无法保存文件
            if (ShowUtil.isEmpty(savePath)) {
                ToastTool.showToast("无法保存照片，请检查SD卡是否挂载");
                return;
            }

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                    .format(new Date());
            String fileName = "zt_" + timeStamp + ".jpg";// 照片命名
            File out = new File(savePath, fileName);
            Uri uri = Uri.fromFile(out);

            theLarge = savePath + fileName;// 该照片的绝对路径

            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
            break;
        case ACTION_TYPE_TOPIC:
//            Bundle bundle = getArguments();
//            if (bundle != null) {
//                String topic = bundle.getString("tweet_topic");
//                setContentText(topic);
//                if (mEtInput != null) {
//                    mEtInput.setSelection(topic.length());
//                }
//            }
            break;
        default:
            break;
        }
    }
    
    private void insertMention() {
        CommonTool.showSoftKeyboard(mEtInput);
        // 在光标所在处插入“@用户名”
        int curTextLength = mEtInput.getText().length();
        if (curTextLength >= Constants.MAX_TEXT_LENGTH)
            return;
        String atme = TEXT_ATME;
        int start, end;
        if ((Constants.MAX_TEXT_LENGTH - curTextLength) >= atme.length()) {
            start = mEtInput.getSelectionStart() + 1;
            end = start + atme.length() - 2;
        } else {
            int num = Constants.MAX_TEXT_LENGTH - curTextLength;
            if (num < atme.length()) {
                atme = atme.substring(0, num);
            }
            start = mEtInput.getSelectionStart() + 1;
            end = start + atme.length() - 1;
        }
        if (start > Constants.MAX_TEXT_LENGTH || end > Constants.MAX_TEXT_LENGTH) {
            start = Constants.MAX_TEXT_LENGTH;
            end = Constants.MAX_TEXT_LENGTH;
        }
        mEtInput.getText().insert(mEtInput.getSelectionStart(), atme);
        mEtInput.setSelection(start, end);// 设置选中文字
    }

    @Override
    public void onDeleteButtonClick(View v) {}

    @Override
    public void onEmojiClick(Emojicon v) {
        InputHelper.input2OSC(mEtInput, v);
    }
    
    private void handleSubmit() {
    	
    	 if (!CommonTool.isConnected()) {
    		 ToastTool.showToast(R.string.tip_network_error);
             return;
         }
         if (!AppContext.context().isLogin()) {
        	 UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
             return;
         }
         String content = mEtInput.getText().toString().trim();
         if (TextUtils.isEmpty(content)) {
             mEtInput.requestFocus();
             ToastTool.showToast(R.string.tip_content_empty);
             return;
         }
         if (content.length() > Constants.MAX_TEXT_LENGTH) {
        	 ToastTool.showToast(R.string.tip_content_too_long);
             return;
         }
         
         PubMsg pubMsg = new PubMsg();
         pubMsg.setContent(content);
         pubMsg.setLocation(AppContext.context().getCurrLocation());
         pubMsg.setCityCode(ShowUtil.isEmpty(AppContext.context().getCityCode()) ? 0 : 
        	 Integer.valueOf(AppContext.context().getCityCode()));
         
         if(content.contains("?") || content.contains("？")) 
        	 pubMsg.setTopic("make_help");
         
         BaseAsyncHttpResHandler mHandler = new BaseAsyncHttpResHandler() {

			@Override
			public void onFailure(int errorCode, String data) {
				ToastTool.showToast("请求超时，发送失败！");
			}

			@Override
			public void onSuccess(ApiResponse res) {
				
				if(res.getErrorCode() > 0) {
					ToastTool.showToast("发送成功！");
					EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_PUB_MSG));
			        getActivity().finish();
				} else
					ToastTool.showToast("发送失败！");
			}

			@Override
			public void onFinish() {
				hideLoadingDialog();
			}
         };
         if (imgFile != null && imgFile.exists()) 
             SmartfarmNetHelper.pubMsg(pubMsg, imgFile, mHandler);
         else
        	 SmartfarmNetHelper.pubMsg(pubMsg, mHandler);
         
         if (mIsKeyboardVisible) {
             CommonTool.HideKb(getActivity(), getActivity().getCurrentFocus());
         }
         showLoadingDialog();
    }
}
