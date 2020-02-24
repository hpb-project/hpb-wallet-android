package com.zhaoxi.Open_source_Android.libs.pushMsg;

import android.content.Context;
import android.content.Intent;

import com.umeng.message.entity.UMessage;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.ui.activity.NewsWebActivity;

import java.util.Map;

/**
 * @author ztt
 * @des 处理通知消息点击事件跳转到指定界面
 * @date 2019/5/29.
 */

public class HanderMsg {
    /**
     * 处理通知消息点击事件跳转到指定界面
     *
     * @param msg
     */
    public static void handerMsgCallToActivity(Context context, UMessage msg) {
        Intent intent = new Intent();
        /**
         * 兼容友盟后台直接推送的
         */
        // 获取动作参数
        String title = msg.title;
        String des = msg.ticker;
        String id = "";
        String activity = msg.activity;
        for (Map.Entry entry : msg.extra.entrySet()) {
            id = (String) entry.getValue();
        }
        if(activity.contains("NewsWebActivity")){
            String url = Config.COMMON_WEB_URL + DAppConstants.NEWS_DETAILS + id;
            intent.putExtra(UmenPushManager.CALL_TO_ACTIVITY, NewsWebActivity.class);
            intent.putExtra(NewsWebActivity.ACTIVITY_TITLE_INFO, title);
            intent.putExtra(NewsWebActivity.WEBVIEW_LOAD_URL, url);
            intent.putExtra(NewsWebActivity.ACTIVITY_DES_INFO,des);
        }
        ServiceHelper.startActivityWithAppIsRuning(context, intent);
    }
}
