package com.zhaoxi.Open_source_Android.common.dialog;

import android.graphics.Color;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.view.RoundAngleImageView;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

/**
 * des:第三方授权登录
 * Created by ztt on 2018/6/21.
 */

public class CommonThreeLoadingDialog extends LinearLayout {
    private BaseActivity mContext;
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;

    private ImageView mImgClose;
    private RoundAngleImageView mImgHeader;
    private TextView mTxtName,mTxtFormAddress;
    private Button mBtnSubmit;

    public interface OnSubmitListener{
        void setOnSubmitListener(String address);
    }


    public void setOnSubmitListener(OnSubmitListener mOnSubmitListener) {
        this.mOnSubmitListener = mOnSubmitListener;
    }

    private OnSubmitListener mOnSubmitListener;

    public CommonThreeLoadingDialog(BaseActivity activity, View currentFocusView) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.view_three_loding_dialog, this);
        mImgClose = findViewById(R.id.threee_loading_dialog_img_close);
        mImgHeader = findViewById(R.id.threee_loading_dialog_img_header);
        mTxtName = findViewById(R.id.threee_loading_dialog_txt_name);
        mTxtFormAddress = findViewById(R.id.threee_loading_dialog_txt_form_address);
        mBtnSubmit = findViewById(R.id.three_loding_dialog_btn_submit);

        mImgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromaddress = SharedPreferencesUtil.getSharePreString(mContext, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                mOnSubmitListener.setOnSubmitListener(fromaddress);
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

    public void show(String name,String imgUrl) {
        String fromaddress = SharedPreferencesUtil.getSharePreString(mContext, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mTxtFormAddress.setText(fromaddress);
        mTxtName.setText(name);
        Glide.with(mContext)
                .load(imgUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.icon_img_defult))
                .into(mImgHeader);
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
