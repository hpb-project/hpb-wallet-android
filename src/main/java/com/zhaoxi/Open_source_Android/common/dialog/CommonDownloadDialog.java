package com.zhaoxi.Open_source_Android.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * des:
 * Created by ztt on 2018/7/30.
 */

public class CommonDownloadDialog extends LinearLayout {
    private Context mContext;
    private ProgressBar mProgressBar;
    private Button mButtonCancle;

    private PopupWindow mPopupWindow;
    private View mCurrentFocusView;
    private OnCancleListener mListener;

    public interface OnCancleListener {
        void cancle();
    }

    public CommonDownloadDialog(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_dialog_download, this);
        mProgressBar = (ProgressBar) findViewById(R.id.update_progress);
        mButtonCancle = (Button) findViewById(R.id.custom_dialog_download_cancle_button);

        mButtonCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.cancle();
                dismiss();
            }
        });
        initDialogPopupWindow();
    }

    public void setProgressBarInfo(int progress) {
        mProgressBar.setProgress(progress);
    }

    public void setButtonCancleListener(OnCancleListener listener) {
        mListener = listener;
    }

    public void setCurrentView(View view) {
        mCurrentFocusView = view;
    }

    public void show() {
        if (mCurrentFocusView != null) {
            mPopupWindow
                    .showAtLocation(mCurrentFocusView, Gravity.CENTER, 0, 0);
        } else {
            mPopupWindow.showAtLocation(
                    ((Activity) mContext).getCurrentFocus(), Gravity.CENTER,
                    0, 0);
        }
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    private void initDialogPopupWindow() {
        mPopupWindow = new PopupWindow(this,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
    }
}
