package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * des:
 * Created by ztt on 2018/6/5.
 */

public class SharedPreferencesUtil {
    private static final String SHARE_NAME = "hpbWallet";
    public static final String CHANGE_LANGUAGE_NAME = "change_language_name";
    public static final String CHANGE_NUMBER_STYLE = "change_number_style";
    public static final String CHANGE_HAND_STATUS = "change_hand_status";
    public static final String CHANGE_COIN_UNIT = "change_coin_unit";//1为人民币 2为美元
    public static final String CHANGE_OPENCLOUD_STATUS = "change_opencloud_status";
    public static final String APP_OLD_VERSION = "app_old_version";
    public static final String MESSAGE_READ_LASTID_TH = "message_read_lastid_th";
    public static final String MESSAGE_READ_LASTID_EN = "message_read_lastid_en";
    public static final String CHOOSE_WALLET_ADDRESS = "choose_wallet_unit";
    public static final String VOTE_TIME = "vote_time";
    public static final String AD_SHOW_TIME = "ad_show_time";
    public static final String CHANGE_XUANFUBTN_STYLE = "change_xuanfubt_style";
    public static final String DIGITAL_SIGN_STR_CONTENT = "digitalSignStrContent";

    public static void setSharePreString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setSharePreInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getSharePreString(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        String value = sp.getString(key, "");
        return value;
    }

    public static int getSharePreInt(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        int value = sp.getInt(key, 0);
        return value;
    }
}
