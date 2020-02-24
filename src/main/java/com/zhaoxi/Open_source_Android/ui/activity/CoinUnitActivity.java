package com.zhaoxi.Open_source_Android.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoinUnitActivity extends BaseTitleBarActivity {
    @BindView(R.id.radio_btn_coin_unit_ren_txt)
    TextView mChainTxt;
    @BindView(R.id.radio_btn_coin_unit_ren_img)
    ImageView mChainImg;
    @BindView(R.id.radio_btn_coin_unit_mei_txt)
    TextView mEnglishTxt;
    @BindView(R.id.radio_btn_coin_unit_mei_img)
    ImageView mEnglishImg;

    private int mCurrentChoseUnit;
    private int mDefultChoseUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_unit);
        ButterKnife.bind(this);
        setTitle(R.string.activity_coin_unit_txt_01, true);
        showRightTxtWithTextListener(getResources().getString(R.string.activity_system_setting_txt_02), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDefultChoseUnit != mCurrentChoseUnit) {
                    SharedPreferencesUtil.setSharePreInt(CoinUnitActivity.this, SharedPreferencesUtil.CHANGE_COIN_UNIT, mCurrentChoseUnit);
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_coin_unit_txt_04));
                }
            }
        });

        initSelect();
    }

    private void initSelect() {
        mDefultChoseUnit = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_COIN_UNIT);
        if (mDefultChoseUnit == 0) {
            int languge = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
            if (languge == 0) {
                String sysLanguage = Locale.getDefault().getLanguage();
                if ("zh".equals(sysLanguage)) {
                    mDefultChoseUnit = 1;
                    onCheckchangedStyle(getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                            View.VISIBLE, View.GONE);
                } else {
                    mDefultChoseUnit = 2;
                    onCheckchangedStyle(getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                            View.GONE, View.VISIBLE);
                }
            } else {
                if (languge == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                    mDefultChoseUnit = 1;
                    onCheckchangedStyle(getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                            View.VISIBLE, View.GONE);
                } else{
                    mDefultChoseUnit = 2;
                    onCheckchangedStyle(getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                            View.GONE, View.VISIBLE);
                }
            }
//            SharedPreferencesUtil.setSharePreInt(CoinUnitActivity.this, SharedPreferencesUtil.CHANGE_COIN_UNIT, mDefultChoseUnit);
        }else{
            if(mDefultChoseUnit == 1){
                onCheckchangedStyle(getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                        View.VISIBLE, View.GONE);
            }else{
                onCheckchangedStyle(getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                        View.GONE, View.VISIBLE);
            }
        }

        mCurrentChoseUnit = mDefultChoseUnit;
    }

    @OnClick({R.id.radio_btn_coin_unit_ren, R.id.radio_btn_coin_unit_mei})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_btn_coin_unit_ren://ren
                mCurrentChoseUnit = 1;
                onCheckchangedStyle(getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                        View.VISIBLE, View.GONE);
                break;
            case R.id.radio_btn_coin_unit_mei://mei
                mCurrentChoseUnit = 2;
                onCheckchangedStyle(getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                        View.GONE, View.VISIBLE);
                break;
        }
    }

    private void onCheckchangedStyle(int colorLeft1, int colorLeft2,
                                     int visibility1, int visibility2) {
        mChainTxt.setTextColor(colorLeft1);
        mEnglishTxt.setTextColor(colorLeft2);
        mChainImg.setVisibility(visibility1);
        mEnglishImg.setVisibility(visibility2);
    }
}
