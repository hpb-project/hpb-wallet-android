package com.zhaoxi.Open_source_Android.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;

/**
 * des:
 * Created by ztt on 2018/11/7.
 */

public class CopyMnenoicWarnDialog extends LinearLayout {
    private BaseActivity mContext;
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;
    private TextView mTxtIKnow;

    public CopyMnenoicWarnDialog(BaseActivity activity, View currentFocusView) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.item_copy_mnenic_warn, this);
        mTxtIKnow = findViewById(R.id.view_copy_mneonic_i_know);
        mTxtIKnow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        initDialogPopupWindow();
    }

    private void initDialogPopupWindow() {
        mPopupWindow = new PopupWindow(this,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
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
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }
    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
