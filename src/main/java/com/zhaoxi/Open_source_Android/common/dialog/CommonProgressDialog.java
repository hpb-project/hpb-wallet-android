package com.zhaoxi.Open_source_Android.common.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * des:共同样式的进度条
 * Created by ztt on 2018/6/5.
 */

public class CommonProgressDialog extends ProgressDialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_common_progress_dialog);
    }

    public CommonProgressDialog(Context context,
                                boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, R.style.Dialog);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }
}
