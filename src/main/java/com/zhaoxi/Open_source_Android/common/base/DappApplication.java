package com.zhaoxi.Open_source_Android.common.base;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.db.greendao.DaoMaster;
import com.zhaoxi.Open_source_Android.db.greendao.DaoSession;
import com.zhaoxi.Open_source_Android.db.greendao.MySQLiteOpenHelper;
import com.zhaoxi.Open_source_Android.libs.pushMsg.HanderMsg;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by 51973 on 2018/5/9.
 */
public class DappApplication extends BaseApplication {
    private static DappApplication mInstance;

    public static DappApplication getInstance() {
        return mInstance;
    }

    private ChangeLanguageUtil mChangeLanguageUtil;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        initDataBase();
        mInstance = this;
        mChangeLanguageUtil = new ChangeLanguageUtil();
        registerActivityLifecycleCallbacks(callbacks);

        UMConfigure.init(this, ""
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");

        PlatformConfig.setWeixin("", "");
        PlatformConfig.setQQZone("", "");
        PlatformConfig.setSinaWeibo("", "", "");
        getTestDeviceInfo();
    }

    public void getTestDeviceInfo() {
        //获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
        //设置通知栏显示数量 0~10 0标识不限制
        mPushAgent.setDisplayNotificationNumber(0);
        //通知栏打开操作
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void openActivity(Context context, UMessage msg) {
                HanderMsg.handerMsgCallToActivity(context, msg);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
        @Override

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //强制修改应用语言
            if (!ChangeLanguageUtil.isSameWithSetting(activity)) {
                int curType = SharedPreferencesUtil.getSharePreInt(getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
                mChangeLanguageUtil.init(activity, curType, false);
            }

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        //Activity 其它生命周期的回调

    };


    private void initDataBase() {
        setDebug();//默认开启
//        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "token_manager.db");
        MySQLiteOpenHelper devOpenHelper = new MySQLiteOpenHelper(this, "token_manager.db");
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
        mDaoSession.clear();// 清空所有数据表的缓存
    }

    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            initDataBase();
        }
        return mDaoSession;
    }

    private void setDebug() {
        // 查看数据库日志
        MigrationHelper.DEBUG = !Config.isPrduction;
        QueryBuilder.LOG_SQL = !Config.isPrduction;
        QueryBuilder.LOG_VALUES = !Config.isPrduction;
    }


    private static boolean enableDoubleClick = true;// 默认可重复点击
    // 设置三秒中之内不可重复点击
    private static DoubleClickCountDown doubleClickCountDown = new DoubleClickCountDown(3_000, 1_000);

    // 是否可重复点击
    private static boolean isEnableDoubleClick() {
        return enableDoubleClick;
    }

    /**
     * 禁止重复点击
     *
     * @param onPerformClickListener 在点击时执行点击事件
     * @param view                   当前view
     */
    public static void disableDoubleClick(OnPerformClickListener onPerformClickListener, View... view) {
        if (!isEnableDoubleClick()) {
            return;
        }
        if (onPerformClickListener != null) {
            if (view != null && view.length > 0)
                onPerformClickListener.onClick(view[0]);
            else
                onPerformClickListener.onClick();
        }

        doubleClickCountDown.start();
    }

    /**
     * 重复点击倒计时计时器，默认三秒钟
     */
    private static class DoubleClickCountDown extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public DoubleClickCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            enableDoubleClick = false;
        }

        @Override
        public void onFinish() {
            enableDoubleClick = true;
        }
    }


    public interface OnPerformClickListener {
        void onClick(View... view);
    }
}
