package com.zhaoxi.Open_source_Android.common.dialog;

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
 * des:主页扫描结果选择
 * Created by ztt on 2019/2/12.
 */

public class CommonScanResultDialog extends LinearLayout {
    private BaseActivity mContext;
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;

    private View mViewLine;
    private TextView mTxtTransfer, mTxtBindAddress, mTxtLookInfo, mTxtCancle;

    public interface OnSubmitListener {
        void setOnSubmit(String address, int type);

        void setOnCancle();
    }

    public void setOnSubmitListener(OnSubmitListener mOnSubmitListener) {
        this.mOnSubmitListener = mOnSubmitListener;
    }

    private OnSubmitListener mOnSubmitListener;

    public CommonScanResultDialog(BaseActivity activity, View currentFocusView,int walletType,String address) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.view_home_scan_dialog, this);
        mTxtTransfer = findViewById(R.id.home_scan_txt_transfer);
        mViewLine = findViewById(R.id.home_scan_view_line);
        mTxtBindAddress = findViewById(R.id.home_scan_txt_bind);
        mTxtLookInfo = findViewById(R.id.home_scan_txt_look);
        mTxtCancle = findViewById(R.id.home_scan_txt_cancle);
        if(walletType == 0){//hold
            mTxtTransfer.setVisibility(View.VISIBLE);
            mViewLine.setVisibility(View.VISIBLE);
        }else{
            mTxtTransfer.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }

        mTxtCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSubmitListener != null) {
                    mOnSubmitListener.setOnCancle();
                }
                dismiss();
            }
        });
        mTxtTransfer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSubmitListener != null) {
                    mOnSubmitListener.setOnSubmit(address,0);
                }
                dismiss();
            }
        });
        mTxtBindAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSubmitListener != null) {
                    mOnSubmitListener.setOnSubmit(address,1);
                }
                dismiss();
            }
        });
        mTxtLookInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSubmitListener != null) {
                    mOnSubmitListener.setOnSubmit(address,2);
                }
                dismiss();
            }
        });

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
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
