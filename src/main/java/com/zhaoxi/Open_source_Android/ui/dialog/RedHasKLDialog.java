package com.zhaoxi.Open_source_Android.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * des:
 * Created by ztt on 2019/1/10.
 */

public class RedHasKLDialog extends Dialog {

    public RedHasKLDialog(Context context) {
        super(context);
    }

    public RedHasKLDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private ImageView mImaAnim;
        private AnimationDrawable mAnimOk;
        private OnClickListener positiveButtonClickListener,negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public RedHasKLDialog.Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public RedHasKLDialog.Builder setNegativeButton(
                OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public RedHasKLDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RedHasKLDialog dialog = new RedHasKLDialog(context,
                    R.style.RedDialog);
            View layout = inflater.inflate(R.layout.view_red_has_dialog,
                    null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            mImaAnim = layout.findViewById(R.id.red_send_has_img_animation);

            mAnimOk = (AnimationDrawable) context.getResources().getDrawable(R.drawable.red_close);
            mAnimOk.setOneShot(false);
            mImaAnim.setImageDrawable(mAnimOk);
            mAnimOk.start();

            Button positive = (Button) layout
                    .findViewById(R.id.red_send_has_btn_get);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positiveButtonClickListener.onClick(dialog,
                            DialogInterface.BUTTON_POSITIVE);
                    if(mAnimOk !=null && mAnimOk.isRunning()){
                        mAnimOk.stop();
                    }
                    dialog.dismiss();
                }
            });

            ImageView imgclose = layout.findViewById(R.id.red_send_has_img_close);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    negativeButtonClickListener.onClick(dialog,
                            DialogInterface.BUTTON_NEGATIVE);
                    if(mAnimOk !=null && mAnimOk.isRunning()){
                        mAnimOk.stop();
                    }
                    dialog.dismiss();
                }
            });

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
