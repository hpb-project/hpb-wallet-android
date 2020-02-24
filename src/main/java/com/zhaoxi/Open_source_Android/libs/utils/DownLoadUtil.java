package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;
import android.view.View;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;

/**
 * des:
 * Created by ztt on 2018/7/30.
 */

public class DownLoadUtil {
    public static void downLoad(Context mContext, String mDownLoadUrl,
                                DialogDownload.OnDownLoadCompletedListener listener) {
        if (!NetUtil.isNetWorkAvailable(mContext)) {
            DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.umeng_common_info_interrupt));
            return;
        }
        DialogDownload updataUtl = new DialogDownload(mContext, listener);
        updataUtl.showChoiceDownLoadDialog(mDownLoadUrl);
    }

    public static void downLoad(Context mContext, String mDownLoadUrl,
                                DialogDownload.OnDownLoadCompletedListener listener, View currentFocus) {
        if (!NetUtil.isNetWorkAvailable(mContext)) {
            DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.umeng_common_info_interrupt));
            return;
        }
        DialogDownload updataUtl = new DialogDownload(mContext, listener, currentFocus);
        updataUtl.showChoiceDownLoadDialog(mDownLoadUrl);
    }
}
