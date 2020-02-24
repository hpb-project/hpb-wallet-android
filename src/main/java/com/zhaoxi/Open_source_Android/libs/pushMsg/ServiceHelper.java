package com.zhaoxi.Open_source_Android.libs.pushMsg;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import java.util.List;

/**
 * @author ztt
 * @des
 * @date 2019/5/29.
 */

public class ServiceHelper {

    /**
     * 返回app运行状态
     *
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return int 1:前台 2:后台 0:不存在
     */
    public static int isAppAlive(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> listInfos = activityManager.getRunningTasks(20);
        // 判断程序是否在栈顶
        if (listInfos.get(0).topActivity.getPackageName().equals(packageName)) {
            return 1;
        } else {
            // 判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : listInfos) {
                if (info.topActivity.getPackageName().equals(packageName)) {
                    return 2;
                }
            }
            return 0;// 栈里找不到，返回3
        }
    }

    /**
     * 自动判断appUI进程是否已在运行，设置跳转信息
     *
     * @param context
     * @param intent
     */
    public static void startActivityWithAppIsRuning(Context context, Intent intent) {
        int isAppRuning = isAppAlive(context, UmenPushManager.APP_PACKAGE);
        if (isAppRuning != 0) {
            Intent newIntent = new Intent(context, (Class<?>) intent
                    .getExtras().getSerializable(
                            UmenPushManager.CALL_TO_ACTIVITY));
            newIntent.putExtras(intent.getExtras());
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
            return;
        }
        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(UmenPushManager.APP_PACKAGE);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launchIntent.putExtra(UmenPushManager.FORM_NOTICE_OPEN, true);
        launchIntent.putExtra(UmenPushManager.FORM_NOTICE_OPEN_DATA, intent);
        context.startActivity(launchIntent);
    }

    /**
     * 启动App时，为跳转到主页MainActivity的Intent写入打开通知的Intent，如果有通知的情况下
     *
     * @param appStartActivity app启动的第一个activity，在配置文件中设置的mainactivity
     * @param startMainActivityIntent
     */
    public static void startAppMainActivitySetNoticeIntent(Activity appStartActivity, Intent startMainActivityIntent) {
        /**
         * 如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的 将参数取出，传递到MainActivity中
         */
        try {
            if (appStartActivity.getIntent().getExtras() != null) {
                if (appStartActivity.getIntent().getExtras().getBoolean(UmenPushManager.FORM_NOTICE_OPEN) == true) {
                    Intent intent = appStartActivity.getIntent().getExtras().getParcelable(UmenPushManager.FORM_NOTICE_OPEN_DATA);
                    startMainActivityIntent.putExtra(UmenPushManager.FORM_NOTICE_OPEN_DATA, intent);
                }
            }
        } catch (Exception e) {

        }finally {
            appStartActivity.startActivity(startMainActivityIntent);
            appStartActivity.finish();
        }
    }

    /**
     * 判断是否是点击消息通知栏跳转过来的
     *
     * @param mainActivity 主页
     */
    public static void isAppWithNoticeOpen(Activity mainActivity) {
        try {
            if (mainActivity.getIntent().getExtras() != null) {
                if(mainActivity.getIntent().getIntExtra(UmenPushManager.SPLAH_AD_NAME,0)==1){
                    Intent adIt = mainActivity.getIntent();
                    Intent newIntent = new Intent(mainActivity, (Class<?>) adIt
                            .getExtras().getSerializable(
                                    UmenPushManager.CALL_TO_ACTIVITY));
                    newIntent.putExtras(adIt.getExtras());
                    mainActivity.startActivity(newIntent);
                }else{
                    Intent intent = mainActivity.getIntent().getExtras()
                            .getParcelable(UmenPushManager.FORM_NOTICE_OPEN_DATA);
                    Intent newIntent = new Intent(mainActivity, (Class<?>) intent
                            .getExtras().getSerializable(
                                    UmenPushManager.CALL_TO_ACTIVITY));
                    newIntent.putExtras(intent.getExtras());
                    mainActivity.startActivity(newIntent);
                }
            }
        } catch (Exception e) {

        }
    }
}
