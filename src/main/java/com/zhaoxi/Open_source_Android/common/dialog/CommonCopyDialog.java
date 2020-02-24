package com.zhaoxi.Open_source_Android.common.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.widget.Button;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * des:
 * Created by ztt on 2019/1/10.
 */

public class CommonCopyDialog extends Dialog {

    public CommonCopyDialog(Context context) {
        super(context);
    }

    public CommonCopyDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public CommonCopyDialog.Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public CommonCopyDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommonCopyDialog dialog = new CommonCopyDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_common_copy_dialog,
                    null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            Button positive = (Button) layout
                    .findViewById(R.id.common_copy_btn_i_know);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positiveButtonClickListener.onClick(dialog,
                            DialogInterface.BUTTON_POSITIVE);
                    dialog.dismiss();
                }
            });

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
