package com.zhaoxi.Open_source_Android.common.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;

/**
 * create by fangz
 * create date:2019/9/7
 * create time:11:28
 */
public class CommonPopupWindowDialog extends PopupWindow {

    private Activity mActivity;

    public CommonPopupWindowDialog(Activity activity, View contentView) {
        super(contentView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, false);
        this.mActivity = activity;
        setAnimationStyle(R.style.VoteDialogAnim);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.7f;
        mActivity.getWindow().setAttributes(lp);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
                if (onDialogStatusChangeListener != null) {
                    onDialogStatusChangeListener.onDismiss();
                }
            }
        });

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SystemLog.D("onTouch", event.getAction() + "");
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    dismiss();
                }
                if (onDialogStatusChangeListener != null) {
                    onDialogStatusChangeListener.onTouch(v, event);
                }
                return false;
            }
        });

    }

    public void show(View parentView) {
        showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    private OnDialogStatusChangeListener onDialogStatusChangeListener;

    public void setOnDialogStatusChangeListener(OnDialogStatusChangeListener onDialogStatusChangeListener) {
        this.onDialogStatusChangeListener = onDialogStatusChangeListener;
    }

    public interface OnDialogStatusChangeListener {
        void onDismiss();

        boolean onTouch(View v, MotionEvent event);
    }
}
