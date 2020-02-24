package com.zhaoxi.Open_source_Android.libs.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.zhaoxi.Open_source_Android.ui.activity.MainActivity;

import java.util.Locale;

/**
 * des:
 * Created by ztt on 2018/6/4.
 */

public class ChangeLanguageUtil {

    private static Activity mActivity = null;
    private static String mSysLanguage = null;

    public static final int CHANGE_LANGUAGE_CHINA = 1;
    public static final int CHANGE_LANGUAGE_ENGLISH = 2;
    public static final int CHANGE_LANGUAGE_DEFAULT = 0;

    private static Resources mResources;
    private static Locale mDefaultLocale;

    public static void init(Activity activity, int appLanguage, boolean isLauncher) {
        mResources = activity.getResources();
        mSysLanguage = activity.getResources().getConfiguration().locale.getLanguage();

        mActivity = activity;
        changeLanguage(appLanguage, isLauncher);
    }

    public static void changeLanguage(int language, boolean isLauncher) {
        Configuration config = mResources.getConfiguration();     // 获得设置对象
        DisplayMetrics dm = mResources.getDisplayMetrics();
        switch (language) {
            case CHANGE_LANGUAGE_CHINA:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    config.setLocale(Locale.CHINESE);
                else config.locale = Locale.CHINESE;     // 中文

                config.setLayoutDirection(Locale.CHINESE);
                SharedPreferencesUtil.setSharePreInt(mActivity, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME, CHANGE_LANGUAGE_CHINA);
                break;
            case CHANGE_LANGUAGE_ENGLISH:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    config.setLocale(Locale.ENGLISH);
                else config.locale = Locale.ENGLISH;// 英文

                config.setLayoutDirection(Locale.ENGLISH);
                SharedPreferencesUtil.setSharePreInt(mActivity, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME, CHANGE_LANGUAGE_ENGLISH);
                break;
            case CHANGE_LANGUAGE_DEFAULT:
                mSysLanguage = Locale.getDefault().getLanguage();

                if ("zh".equals(mSysLanguage)) {
                    mDefaultLocale = Locale.CHINESE;
                } else {
                    mDefaultLocale = Locale.ENGLISH;
                }

                config.locale = mDefaultLocale;         // 系统默认语言
                config.setLayoutDirection(mDefaultLocale);
                SharedPreferencesUtil.setSharePreInt(mActivity, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME, CHANGE_LANGUAGE_DEFAULT);
                break;
        }
        mResources.updateConfiguration(config, dm);
        mActivity.createConfigurationContext(config);
        if (!isLauncher) {
            return;
        }
        //重启启动应用
        Intent intent = new Intent();
        intent.setClass(mActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
        mActivity.setResult(Activity.RESULT_OK);
        mActivity.finish();
    }

    /**
     * 获取当前语言
     *
     * @param context
     * @return
     */
    public static int languageType(Context context) {
        int curType = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
        if (curType == 0) {
            mSysLanguage = Locale.getDefault().getLanguage();

            if ("zh".equals(mSysLanguage)) {
                curType = 1;
            } else {
                curType = 2;
            }
        }

        return curType;
    }

    /**
     * 判断是否与设定的语言相同.
     *
     * @param context
     * @return
     */
    public static boolean isSameWithSetting(Context context) {
        String language = Locale.getDefault().getLanguage();
        int curType = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
        if(curType == 1){//中文
            if(language.equals("zh")){
                return true;
            }else{
                return false;
            }
        }else{//英文
            if(language.equals("en")){
                return true;
            }else{
                return false;
            }
        }
    }
}
