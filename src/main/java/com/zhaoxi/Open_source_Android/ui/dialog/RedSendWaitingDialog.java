package com.zhaoxi.Open_source_Android.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.anim.AnimationsContainer;

/**
 * des:红包发送等待页面
 * Created by ztt on 2018/6/21.
 */

public class RedSendWaitingDialog extends LinearLayout {
    private BaseActivity mContext;
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;

    private ImageView mImgWaitClose, mImgOkClose;
    private RelativeLayout mLayoutWaiting, mLayoutWaitOk;
    private TextView mTxtWaitContent;
    private Button mBtnSplsh, mBtnShare;
    private ImageView mImgWaitAnim, mImgOkAnim;
    private AnimationDrawable mAnimOk;
    private AnimationsContainer.FramesSequenceAnimation mAnimWait;

    private String mRedId = "";

    public interface OnRedSendWaitListener {
        void setOnSplahListener(String redId);

        void setOnShareListener();

        void cancel();
    }


    public void setOnRedSendWaitListener(OnRedSendWaitListener redSendWaitListener) {
        this.mOnRedSendWaitListener = redSendWaitListener;
    }

    private OnRedSendWaitListener mOnRedSendWaitListener;

    public RedSendWaitingDialog(BaseActivity activity, View currentFocusView) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.view_red_send_waitting_dialog, this);
        mImgWaitClose = findViewById(R.id.red_send_wait_img_close);
        mImgOkClose = findViewById(R.id.red_send_wait_img_ok_close);
        mLayoutWaiting = findViewById(R.id.red_send_wait_layout_waiting);
        mTxtWaitContent = findViewById(R.id.red_send_wait_txt_content);
        mBtnSplsh = findViewById(R.id.red_send_wait_btn_falsh);
        mLayoutWaitOk = findViewById(R.id.red_send_wait_layout_waitok);
        mBtnShare = findViewById(R.id.red_send_wait_btn_share);
        mImgWaitAnim = findViewById(R.id.red_img_wait_animation);
        mImgOkAnim = findViewById(R.id.red_img_ok_animation);

        mAnimOk = (AnimationDrawable) getResources().getDrawable(R.drawable.red_close);
        mAnimOk.setOneShot(false);
        mImgOkAnim.setImageDrawable(mAnimOk);

        mImgWaitClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRedSendWaitListener.cancel();
                dismiss();
            }
        });

        mImgOkClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAnimOk.isRunning()) mOnRedSendWaitListener.cancel();
                dismiss();
            }
        });

        mBtnSplsh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRedSendWaitListener.setOnSplahListener(mRedId);
            }
        });

        mBtnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRedSendWaitListener.setOnShareListener();
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

    public void show(String redNum, String money, String redId) {
        mLayoutWaiting.setVisibility(View.VISIBLE);
        mLayoutWaitOk.setVisibility(View.GONE);
        mTxtWaitContent.setText(mContext.getResources().getString(R.string.activity_red_send_txt_20,
                redNum, money));
        mRedId = redId;
        if(mAnimWait == null)
            mAnimWait = AnimationsContainer.getInstance(R.array.red_wait_anim, 10).createProgressDialogAnim(mImgWaitAnim,false);
        mAnimWait.start();
        mContext.dismissProgressDialog();
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void showWaitOk() {
        mLayoutWaiting.setVisibility(View.GONE);
        mLayoutWaitOk.setVisibility(View.VISIBLE);
        mAnimWait.stop();
        mAnimOk.start();
    }

    public void dismiss() {
        if (mAnimWait != null) {
            mAnimWait.stop();
        }
        if (mAnimOk != null && mAnimOk.isRunning()) {
            mAnimOk.stop();
        }
        mPopupWindow.dismiss();
    }
}
