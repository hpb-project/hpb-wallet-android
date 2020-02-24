package com.zhaoxi.Open_source_Android.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NumberStyleActivity extends BaseTitleBarActivity {

    @BindView(R.id.radio_btn_numberstyle_eg)
    TextView mTxtNumberstyleEg;
    @BindView(R.id.txt_numberstyle_left_01)
    TextView mTxtLeft01;
    @BindView(R.id.txt_numberstyle_content_01)
    TextView mTxtContent01;
    @BindView(R.id.img_numberstyle_select_01)
    ImageView mImgSelect01;
    @BindView(R.id.txt_numberstyle_left_02)
    TextView mTxtLeft02;
    @BindView(R.id.txt_numberstyle_content_02)
    TextView mTxtContent02;
    @BindView(R.id.img_numberstyle_select_02)
    ImageView mImgSelect02;

    private int mCurrentChoseStyle;
    private int mDefultChoseStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_style);
        ButterKnife.bind(this);

        setTitle(R.string.activity_system_setting_txt_05,true);
        showRightTxtWithTextListener(getResources().getString(R.string.activity_system_setting_txt_02), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDefultChoseStyle != mCurrentChoseStyle) {
                    SharedPreferencesUtil.setSharePreInt(NumberStyleActivity.this, SharedPreferencesUtil.CHANGE_NUMBER_STYLE, mCurrentChoseStyle);
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_system_setting_txt_09));
                }
            }
        });

        initSelect();
    }

    @OnClick({R.id.layout_btn_numberstyle_01, R.id.layout_btn_numberstyle_02})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_btn_numberstyle_01:
                if (mCurrentChoseStyle != 0) {
                    mCurrentChoseStyle = 0;
                    onCheckchangedStyle(R.drawable.bg_select_number_style_02, R.drawable.bg_select_number_style_01,
                            getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                            getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                            View.VISIBLE, View.GONE, getResources().getString(R.string.activity_system_setting_txt_08_01));
                }
                break;
            case R.id.layout_btn_numberstyle_02:
                if (mCurrentChoseStyle != 1) {
                    mCurrentChoseStyle = 1;
                    onCheckchangedStyle(R.drawable.bg_select_number_style_01, R.drawable.bg_select_number_style_02,
                            getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                            getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                            View.GONE, View.VISIBLE, getResources().getString(R.string.activity_system_setting_txt_08_02));
                }
                break;
        }
    }

    private void initSelect() {
        mDefultChoseStyle = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        if (mDefultChoseStyle == 0) {
            onCheckchangedStyle(R.drawable.bg_select_number_style_02, R.drawable.bg_select_number_style_01,
                    getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                    getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                    View.VISIBLE, View.GONE, getResources().getString(R.string.activity_system_setting_txt_08_01));

        } else {
            onCheckchangedStyle(R.drawable.bg_select_number_style_01, R.drawable.bg_select_number_style_02,
                    getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                    getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                    View.GONE, View.VISIBLE, getResources().getString(R.string.activity_system_setting_txt_08_02));
        }
        mCurrentChoseStyle = mDefultChoseStyle;
    }

    private void onCheckchangedStyle(int resid1, int resid2, int colorLeft1, int colorLeft2, int color1
            , int color2, int visibility1, int visibility2, String str) {
        mTxtLeft01.setBackgroundResource(resid1);
        mTxtLeft02.setBackgroundResource(resid2);
        mTxtLeft01.setTextColor(colorLeft1);
        mTxtLeft02.setTextColor(colorLeft2);
        mTxtContent01.setTextColor(color1);
        mTxtContent02.setTextColor(color2);
        mImgSelect01.setVisibility(visibility1);
        mImgSelect02.setVisibility(visibility2);
        mTxtNumberstyleEg.setText(str + getResources().getString(R.string.wallet_manager_txt_money_unit_01));
    }
}
