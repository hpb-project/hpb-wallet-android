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

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.ShareUtil;

import java.io.File;

/**
 * des:映射确认页面
 * Created by ztt on 2018/6/21.
 */

public class CommonShareDialog extends LinearLayout implements View.OnClickListener{
    private BaseActivity mContext;
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;

    private String mShareImgPath,mSmallShareImgPath;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_share_sina:
                shareImageLocal(mShareImgPath,mSmallShareImgPath,SHARE_MEDIA.SINA);
                break;
            case R.id.layout_share_wechat:
                shareImageLocal(mShareImgPath,mSmallShareImgPath,SHARE_MEDIA.WEIXIN);
                break;
            case R.id.layout_share_qq:
                shareImageLocal(mShareImgPath,mSmallShareImgPath,SHARE_MEDIA.QQ);
                break;
            case R.id.layout_share_wechat_circle:
                shareImageLocal(mShareImgPath,mSmallShareImgPath,SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.layout_share_more:
                ShareUtil.shareMore(mContext,mShareImgPath);
                break;
            case R.id.layout_share_cancle:
            case R.id.layout_share_close:
                dismiss();
                break;
        }
    }

    public CommonShareDialog(BaseActivity activity, View currentFocusView) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.activity_share, this);
        findViewById(R.id.layout_share_close).setOnClickListener(this);
        findViewById(R.id.layout_share_cancle).setOnClickListener(this);
        findViewById(R.id.layout_share_sina).setOnClickListener(this);
        findViewById(R.id.layout_share_wechat).setOnClickListener(this);
        findViewById(R.id.layout_share_qq).setOnClickListener(this);
        findViewById(R.id.layout_share_more).setOnClickListener(this);
        findViewById(R.id.layout_share_wechat_circle).setOnClickListener(this);

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

    public void show(String shareImgPath, String smallShareImgPath) {
        mShareImgPath = shareImgPath;
        mSmallShareImgPath = smallShareImgPath;
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    private void shareImageLocal(String imgPath1, String imgPath2, SHARE_MEDIA share_media) {//纯图片分享
        if(ShareUtil.shareImageLocal(mContext,share_media)){
            File file01 = new File(imgPath1);
            File file02 = new File(imgPath2);
            UMImage imagelocal = new UMImage(mContext, file01);
            imagelocal.setThumb(new UMImage(mContext, file02));
            new ShareAction(mContext).withMedia(imagelocal)
                    .setPlatform(share_media)
                    .setCallback(shareListener).share();
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.share_kx_txt_03));
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.share_kx_txt_04) + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.share_kx_txt_05));
        }
    };
}
