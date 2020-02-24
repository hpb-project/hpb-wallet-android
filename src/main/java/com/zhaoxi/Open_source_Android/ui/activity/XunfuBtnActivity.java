package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XunfuBtnActivity extends BaseTitleBarActivity {

    @BindView(R.id.self_xuanfu_img_btn_change)
    ImageView mImgChange;

    private int mXuanfuBtnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunfu_btn);
        ButterKnife.bind(this);

        setTitle(R.string.activity_system_setting_title,true);
        mXuanfuBtnStatus = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_XUANFUBTN_STYLE);
        if(mXuanfuBtnStatus == 0){//默认是启动的
            mImgChange.setImageResource(R.mipmap.icon_hand_toggle_open);
        }else{
            mImgChange.setImageResource(R.mipmap.icon_hand_toggle_close);
        }
    }

    @OnClick(R.id.self_xuanfu_img_btn_change)
    public void onViewClicked() {
        if(mXuanfuBtnStatus == 0){
            mXuanfuBtnStatus = 1;
            mImgChange.setImageResource(R.mipmap.icon_hand_toggle_close);
        }else{
            mXuanfuBtnStatus = 0;
            mImgChange.setImageResource(R.mipmap.icon_hand_toggle_open);
        }

        SharedPreferencesUtil.setSharePreInt(XunfuBtnActivity.this, SharedPreferencesUtil.CHANGE_XUANFUBTN_STYLE,mXuanfuBtnStatus);
    }
}
