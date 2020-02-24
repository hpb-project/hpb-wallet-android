package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 多语言
 *
 * @author zhutt on 2018-06-07
 */
public class LanguageManegerActivity extends BaseTitleBarActivity {
    @BindView(R.id.radio_btn_language_chain_txt)
    TextView mChainTxt;
    @BindView(R.id.radio_btn_language_chain_img)
    ImageView mChainImg;
    @BindView(R.id.radio_btn_language_english_txt)
    TextView mEnglishTxt;
    @BindView(R.id.radio_btn_language_english_img)
    ImageView mEnglishImg;

    private int mCurrentChoseLanguge;
    private int mDefultChoseLanguge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_maneger);
        ButterKnife.bind(this);
        setTitle(R.string.activity_system_setting_txt_01, true);
        showRightTxtWithTextListener(getResources().getString(R.string.activity_system_setting_txt_02), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDefultChoseLanguge != mCurrentChoseLanguge) {
                    ChangeLanguageUtil.init(LanguageManegerActivity.this, mCurrentChoseLanguge, true);
                } else {
                    finish();
                }
            }
        });

        initSelect();
    }

    @OnClick({R.id.radio_btn_language_chain, R.id.radio_btn_language_english})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_btn_language_chain://中文
                mCurrentChoseLanguge = ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA;
                onCheckchangedStyle(getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                        View.VISIBLE, View.GONE);
                break;
            case R.id.radio_btn_language_english://English
                mCurrentChoseLanguge = ChangeLanguageUtil.CHANGE_LANGUAGE_ENGLISH;
                onCheckchangedStyle(getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                        View.GONE, View.VISIBLE);
                break;
        }
    }

    private void initSelect() {
        mDefultChoseLanguge = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);

        if (mDefultChoseLanguge != 0) {
            if (mDefultChoseLanguge == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                onCheckchangedStyle(getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                        View.VISIBLE, View.GONE);
            } else
                onCheckchangedStyle(getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                        View.GONE, View.VISIBLE);
        } else {
            String sysLanguage = Locale.getDefault().getLanguage();
            if ("zh".equals(sysLanguage)) {
                mDefultChoseLanguge = ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA;
                onCheckchangedStyle(getResources().getColor(R.color.color_2E2F47), getResources().getColor(R.color.color_9C9EB9),
                        View.VISIBLE, View.GONE);
            } else {
                mDefultChoseLanguge = ChangeLanguageUtil.CHANGE_LANGUAGE_ENGLISH;
                onCheckchangedStyle(getResources().getColor(R.color.color_9C9EB9), getResources().getColor(R.color.color_2E2F47),
                        View.GONE, View.VISIBLE);
            }

        }

        mCurrentChoseLanguge = mDefultChoseLanguge;
    }

    private void onCheckchangedStyle(int colorLeft1, int colorLeft2,
                                     int visibility1, int visibility2) {
        mChainTxt.setTextColor(colorLeft1);
        mEnglishTxt.setTextColor(colorLeft2);
        mChainImg.setVisibility(visibility1);
        mEnglishImg.setVisibility(visibility2);
    }
}
