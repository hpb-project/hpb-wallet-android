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
 * des:
 * Created by ztt on 2018/6/11.
 */

public class CommonWarnDialog extends Dialog {
    public CommonWarnDialog(Context context) {
        super(context);
    }

    public CommonWarnDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String massage;

        public Builder(Context context) {
            this.context = context;
        }

        public void setMassage(String msg){
            this.massage = msg;
        }

        public CommonWarnDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommonWarnDialog dialog = new CommonWarnDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_common_warn_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView mTxtContent = layout.findViewById(R.id.common_warn_dialog_txt_content);
            if(!StrUtil.isEmpty(massage)){
                mTxtContent.setText(massage);
            }
            TextView mKwon = layout.findViewById(R.id.common_warn_dialog_txt_kwon);

            mKwon.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
