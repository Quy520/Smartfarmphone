package com.smartfarm.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;

import com.smartfarm.base.BaseActivity;
import com.smartfarm.base.BaseDetailFragment;
import com.smartfarm.base.BaseFragment;
import com.smartfarm.emoji.KJEmojiFragment;
import com.smartfarm.emoji.OnSendClickListener;
import com.smartfarm.emoji.ToolbarFragment;
import com.smartfarm.emoji.ToolbarFragment.OnActionClickListener;
import com.smartfarm.emoji.ToolbarFragment.ToolAction;
import com.smartfarm.fragment.MessageDetailFragment;
import com.smartfarm.fragment.NewsDetailFragment;

/**
 * 详情activity
 * 
 */
public class DetailActivity extends BaseActivity implements OnSendClickListener {

    public static final int DISPLAY_NEWS = 0;
    public static final int DISPLAY_NOTE = DISPLAY_NEWS + 1;
    
    public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";

    private OnSendClickListener currentFragment;
    public KJEmojiFragment emojiFragment = new KJEmojiFragment();
    public ToolbarFragment toolFragment = new ToolbarFragment();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.actionbar_title_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        int displayType = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE, DISPLAY_NEWS);
        
        BaseFragment fragment = null;
        int actionBarTitle = 0;
        switch (displayType) {
        case DISPLAY_NEWS:
            actionBarTitle = R.string.news_detail;
            fragment = new NewsDetailFragment();
            break;
        case DISPLAY_NOTE:
            actionBarTitle = R.string.msg_detail;
            fragment = new MessageDetailFragment();
            break;
        default:
            break;
        }
        setActionBarTitle(actionBarTitle);
        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
        trans.replace(R.id.container, fragment);
        trans.commitAllowingStateLoss();
        if (fragment instanceof OnSendClickListener) {
            currentFragment = (OnSendClickListener) fragment;
        } else {        	
            currentFragment = new OnSendClickListener() {
                @Override
                public void onClickSendButton(Editable str) {}
                
                @Override
                public void onClickFlagButton() {}
            };
        }
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView() {
        
    	if (currentFragment instanceof MessageDetailFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emoji_keyboard, emojiFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.emoji_keyboard, toolFragment).commit();
        }
       
        toolFragment.setOnActionClickListener(new OnActionClickListener() {
            @Override
            public void onActionClick(ToolAction action) {
                switch (action) {
                case ACTION_CHANGE:
                case ACTION_WRITE_COMMENT:
                	
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.footer_menu_slide_in,
                                    R.anim.footer_menu_slide_out)
                            .replace(R.id.emoji_keyboard, emojiFragment)
                            .commit();
                    break;
                case ACTION_FAVORITE:
                    ((BaseDetailFragment) currentFragment).handleFavoriteOrNot();
                    break;
                case ACTION_REPORT:
                    ((BaseDetailFragment) currentFragment).onReportMenuClick();
                    break;
                case ACTION_SHARE:
                    ((BaseDetailFragment) currentFragment).handleShare();
                    break;
                case ACTION_VIEW_COMMENT:
                    ((BaseDetailFragment) currentFragment).onclickWriteComment();
                    break;
                default:
                    break;
                }
            }
        });
    }

    @Override
    public void initData() {}

    @Override
    public void onClickSendButton(Editable str) {
        currentFragment.onClickSendButton(str);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (emojiFragment.isShowEmojiKeyBoard()) {
                    emojiFragment.hideAllKeyBoard();
                    return true;
                }
                if (emojiFragment.getEditText().getTag() != null) {
                    emojiFragment.getEditText().setTag(null);
                    emojiFragment.getEditText().setHint("说点什么吧");
                    return true;
                }
            } catch (NullPointerException e) {
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setCommentCount(int count) {
        try {
            toolFragment.setCommentCount(count);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClickFlagButton() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.footer_menu_slide_in,
                        R.anim.footer_menu_slide_out)
                .replace(R.id.emoji_keyboard, toolFragment).commit();
        try {
            toolFragment.setCommentCount(((BaseDetailFragment) currentFragment)
                    .getCommentCount());
        } catch (Exception e) {}
    }
}
