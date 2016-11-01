package com.smartfarm.view.util;

import com.smartfarm.fragment.AboutFragment;
import com.smartfarm.fragment.CollectFragment;
import com.smartfarm.fragment.CommentListFragment;
import com.smartfarm.fragment.CommunityUserFragment;
import com.smartfarm.fragment.ForgetPasswordFragment;
import com.smartfarm.fragment.HelpDetailFragment;
import com.smartfarm.fragment.HelpFragment;
import com.smartfarm.fragment.HistoryFragment;
import com.smartfarm.fragment.LightFragment;
import com.smartfarm.fragment.LightSettingFragment;
import com.smartfarm.fragment.LoginFragment;
import com.smartfarm.fragment.LoginNewFragment;
import com.smartfarm.fragment.LoginPhoneFragment;
import com.smartfarm.fragment.ModifyPwdFragment;
import com.smartfarm.fragment.MyCodeFragment;
import com.smartfarm.fragment.MyCommentFragment;
import com.smartfarm.fragment.MyNoteFragment;
import com.smartfarm.fragment.NetFragment;
import com.smartfarm.fragment.NewsFragment;
import com.smartfarm.fragment.NoteFragment;
import com.smartfarm.fragment.NotePubFragment;
import com.smartfarm.fragment.PengManagerFragment;
import com.smartfarm.fragment.ResignFragment;
import com.smartfarm.fragment.StallsFragment;
import com.smartfarm.fragment.SuggestFragment;
import com.smartfarm.fragment.SysSettingFragment;
import com.smartfarm.fragment.TempConfigFragment;
import com.smartfarm.fragment.TempControlFragment;
import com.smartfarm.fragment.TempManagerFragment;
import com.smartfarm.fragment.UserCenterFragment;
import com.smartfarm.fragment.UserDetailFragment;
import com.smartfarm.fragment.UserInfoFragment;
import com.smartfarm.fragment.UserListFragment;
import com.smartfarm.fragment.UserProtocolFragment;
import com.smartfarm.fragment.WaterFragment;
import com.smartfarm.fragment.WaterSettingFragment;
import com.smartfarm.view.R;


public enum BackPage {
	
	SETTING(1, R.string.setting, SysSettingFragment.class),
	ABOUT(2, R.string.about, AboutFragment.class),
	NET(3, R.string.about, NetFragment.class),
	HELP(4, R.string.help, HelpFragment.class),
	HELP_DETAIL(5, R.string.help, HelpDetailFragment.class),
	LOGIN(6, R.string.login, LoginFragment.class),
	USER_PROTOCPL(7, R.string.user_protocol, UserProtocolFragment.class),
	PENG_MANAGER(8, R.string.peng_manager, PengManagerFragment.class),
	TEMP_MANAGER(9, R.string.history_temp, TempManagerFragment.class),
	STAILLS(10, R.string.setting_stalls, StallsFragment.class),
	SUGGEST(11, R.string.feedback, SuggestFragment.class),
	USER_INFO(12, R.string.user_info, UserInfoFragment.class),
	RESIGN(13, R.string.resign, ResignFragment.class),
	USER_DETAIL(14, R.string.user_detail_title, UserDetailFragment.class),
	MODIFY_PWD(15, R.string.modify_pwd, ModifyPwdFragment.class),
	WATER(16, R.string.item_water, WaterFragment.class),
	LIGHT(17, R.string.item_light, LightFragment.class),
	HISTORY(18, R.string.history, HistoryFragment.class),
	WATER_SETTING(19, R.string.water_setting, WaterSettingFragment.class),
	NEWS(20, R.string.item_news, NewsFragment.class),
	TEMP_CONFIG(21, R.string.temp_config, TempConfigFragment.class),
	LIGHT_SETTING(22, R.string.light_setting, LightSettingFragment.class),
	LIGHT_RECORD(23, R.string.history_light, LightSettingFragment.class),
	COMMUNITY_USER(24, R.string.user_title, CommunityUserFragment.class),
	MY_CODE(25, R.string.user_code_title, MyCodeFragment.class),
	COLLECT(26, R.string.me_collect, CollectFragment.class),
	NOTE(28, R.string.mn_submit, NoteFragment.class),
	COMMENT(29, R.string.comment, CommentListFragment.class),
	MY_NOTE(30, R.string.me_note, MyNoteFragment.class),
	USER_LIST(31, R.string.user_list, UserListFragment.class),
	USER_CENTER(32, R.string.user_list, UserCenterFragment.class),
	NOTE_PUB(33, R.string.note_pub_title, NotePubFragment.class),
	COMMENT_HISTORY(34, R.string.comment_history, MyCommentFragment.class),
	TEMP_CONTROL(35, R.string.temp_control, TempControlFragment.class),
	FORGET_PWD(36,R.string.forget_password,ForgetPasswordFragment.class),
	LOGIN_PHONE(37,R.string.user_login_phone,LoginPhoneFragment.class),
	LOING_NEW(38,R.string.user_login_new,LoginNewFragment.class);
	
    private int title;
    private Class<?> clz;
    private int value;

    private BackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }
    
    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static BackPage getPageByValue(int val) {
        for (BackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }
}
