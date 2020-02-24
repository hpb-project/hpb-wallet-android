package com.zhaoxi.Open_source_Android.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

/**
 * 带文字进度条
 * Created by 51973 on 2018/5/25.
 */

public class CommonDialogProgress extends Dialog {

    public CommonDialogProgress(Context context) {
        super(context);
    }

    public CommonDialogProgress(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String massage;

        public Builder(Context context) {
            this.context = context;
        }

        public void setMassage(String msg) {
            this.massage = msg;
        }

        public CommonDialogProgress create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommonDialogProgress dialog = new CommonDialogProgress(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_common_dialog_progress, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView mTxtContent = layout.findViewById(R.id.txt_common_dialog_tips);
            if (!StrUtil.isEmpty(massage)) {
                mTxtContent.setText(massage);
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
