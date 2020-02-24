package com.zhaoxi.Open_source_Android.ui.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;


public class SensorStartDialog extends LinearLayout {
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;
    private ImageView mImgHand;
    private ObjectAnimator mAnim;

    public SensorStartDialog(BaseActivity activity, View currentFocusView) {
        super(activity);
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.view_sensor_start_dialog, this);

        mImgHand = findViewById(R.id.view_sensor_start_hand);

        initDialogPopupWindow();
    }

    private void initDialogPopupWindow() {
        mPopupWindow = new PopupWindow(this,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(dw);
    }

    public void show() {
        if (mAnim == null) {
//            mAnim = ObjectAnimator.ofFloat(mImgHand, "rotation", 0f, 45f, -30f, 0f);
            // 过渡动画参数粒度设置的平滑些
            mAnim = ObjectAnimator.ofFloat(mImgHand, "rotation", 0f, 10f,20f,30f,40f,45f,40f,30f,20f,10f,0f,-10f,-20f,-30f,-20f,-10f, 0f);
            // 过渡动画时间设置为800毫秒
            mAnim.setDuration(800);
            mAnim.setRepeatCount(ValueAnimator.INFINITE);
        }
        mAnim.start();
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        if (mAnim != null) {
            mAnim.cancel();
            mAnim = null;
        }
        mPopupWindow.dismiss();
    }

}
