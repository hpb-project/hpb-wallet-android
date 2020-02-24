package com.zhaoxi.Open_source_Android.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;

public class CommonTipDialog extends Dialog {

    public CommonTipDialog(Context context) {
        super(context);
    }

    public CommonTipDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private String message2;
        private SpannableStringBuilder message3;
        private String positiveButtonText;
        private String negativeButtonText;

        private OnClickListener positiveButtonClickListener,
                negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(SpannableStringBuilder message) {
            this.message3 = message;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public String getMessage2() {
            return message2;
        }

        public void setMessage2(String message2) {
            this.message2 = message2;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CommonTipDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommonTipDialog dialog = new CommonTipDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_common_tip_dialog,
                    null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (title != null)
                ((TextView) layout.findViewById(R.id.common_tip_dialog_title))
                        .setText(title);
            else
                ((TextView) layout.findViewById(R.id.common_tip_dialog_title))
                        .setText(context.getString(R.string.dialog_common_title_text));
            // set the confirm button
            TextView positive = (TextView) layout
                    .findViewById(R.id.common_tip_dialog_positive_button);
            TextView mMessge1 =  ((TextView) layout.findViewById(R.id.common_tip_dialog_message_1));
            TextView mMessge2 =  ((TextView) layout.findViewById(R.id.common_tip_dialog_message_2));
            if (positiveButtonText != null) {
                positive.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    positive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }else {
                    positive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                positive.setVisibility(View.GONE);
            }
            // set the cancel button
            TextView cancel = (TextView) layout
                    .findViewById(R.id.common_tip_dialog_cancle_button);
            View line = layout.findViewById(R.id.common_tip_dialog_line);
            if (negativeButtonText != null) {
                cancel.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    cancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                } else {
                    cancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                cancel.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                positive.setTextColor(context.getResources().getColor(R.color.color_007AFF));
            }
            // set the content message
            if (message != null) {
                mMessge1.setText(message);
            } else if (message3 != null) {
                ((TextView) layout
                        .findViewById(R.id.common_tip_dialog_message_1))
                        .setText(message3);
            } else {
                mMessge1.setVisibility(View.GONE);
            }

            if (message2 != null) {
                if(message != null){
                    setMargin(mMessge1,20,10);
                    setMargin(mMessge2,5,25);
                }else{
                    setMargin(mMessge2,20,25);
                }
                mMessge2.setText(message2);
                mMessge2.setVisibility(View.VISIBLE);

            } else {
                mMessge2.setVisibility(View.GONE);
            }

            dialog.setContentView(layout);
            return dialog;
        }
    }

    private static void setMargin(TextView view, int top,int bottom){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, top, 0, bottom);

        view.setLayoutParams(lp);
    }
}
