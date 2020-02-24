package com.zhaoxi.Open_source_Android.common.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * des:
 * Created by ztt on 2019/1/10.
 */

public class CommonWarnAddressDialog extends Dialog {

    public CommonWarnAddressDialog(Context context) {
        super(context);
    }

    public CommonWarnAddressDialog(Context context, int theme) {
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

        public CommonWarnAddressDialog.Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public CommonWarnAddressDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommonWarnAddressDialog dialog = new CommonWarnAddressDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.activity_receive_warn,
                    null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            Button positive = (Button) layout
                    .findViewById(R.id.receivables_warn_btn_know);
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
