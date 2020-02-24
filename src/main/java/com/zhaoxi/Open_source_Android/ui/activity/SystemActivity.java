package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.biometriclib.BiometricPromptManager;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SystemActivity extends BaseTitleBarActivity {
    @BindView(R.id.system_layout_xuanfu_btn_content)
    TextView mTxtXuanfuBtnStatus;
    @BindView(R.id.system_txt_numberstyle_content)
    TextView mTxtContent;
    @BindView(R.id.system_txt_language_content)
    TextView mTxtLanguage;
    @BindView(R.id.system_txt_openhand_status)
    TextView mTxtHandStatus;
    @BindView(R.id.system_layout_openhand)
    RelativeLayout mLayoutOpenHand;
    @BindView(R.id.system_line_openhand)
    View mViewline;
    @BindView(R.id.system_txt_coin_content)
    TextView mTxtCoinUnit;

    private int mDefultChoseLanguge;
    private BiometricPromptManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        ButterKnife.bind(this);

        setTitle(R.string.activity_system_setting_title, true);

        mManager = BiometricPromptManager.from(this);
        initLanguage();
        initNumStyle();
        initHandStatus();
        initCoinUnit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initXunfuBtnStatus();
    }

    @OnClick({R.id.system_layout_language, R.id.system_layout_numberstyle,
            R.id.system_layout_openhand, R.id.system_layout_xuanfu_btn,
            R.id.system_layout_coin_unit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.system_layout_language:
                startActivityForResult(new Intent(this, LanguageManegerActivity.class), 0x367);
                break;
            case R.id.system_layout_numberstyle:
                startActivityForResult(new Intent(this, NumberStyleActivity.class), 0x368);
                break;
            case R.id.system_layout_openhand:
                //先判断是否已设置指纹 没有去提示开启
                if (mManager.isBiometricPromptEnable()) {
                    startActivityForResult(new Intent(this, SelfLoadActivity.class), 0x369);
                } else {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.biometric_dialog_open_warn));
                }
                break;
            case R.id.system_layout_xuanfu_btn:
                startActivityForResult(new Intent(this, XunfuBtnActivity.class), 0x370);
                break;
            case R.id.system_layout_coin_unit:
                startActivityForResult(new Intent(this, CoinUnitActivity.class), 0x371);
                break;
        }
    }

    private void initLanguage() {
        mDefultChoseLanguge = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
        if (mDefultChoseLanguge != 0) {
            if (mDefultChoseLanguge == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                mTxtLanguage.setText(getResources().getString(R.string.activity_system_setting_txt_03));
            } else
                mTxtLanguage.setText(getResources().getString(R.string.activity_system_setting_txt_04));
        } else {
            String sysLanguage = Locale.getDefault().getLanguage();
            if ("zh".equals(sysLanguage)) {
                mTxtLanguage.setText(getResources().getString(R.string.activity_system_setting_txt_03));
            } else {
                mTxtLanguage.setText(getResources().getString(R.string.activity_system_setting_txt_04));
            }
        }
    }

    private void initNumStyle() {
        int style = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        if (style == 0) {
            mTxtContent.setText(getResources().getString(R.string.activity_system_setting_txt_08_01));
        } else {
            mTxtContent.setText(getResources().getString(R.string.activity_system_setting_txt_08_02));
        }
    }

    public void initCoinUnit(){
        int coinUnit = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_COIN_UNIT);
        if(coinUnit == 0){
            mDefultChoseLanguge = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
            if(mDefultChoseLanguge == 0){
                String sysLanguage = Locale.getDefault().getLanguage();
                if ("zh".equals(sysLanguage)) {
                    mTxtCoinUnit.setText(getResources().getString(R.string.activity_coin_unit_txt_05));
                } else {
                    mTxtCoinUnit.setText(getResources().getString(R.string.activity_coin_unit_txt_06));
                }
            }else{
                if (mDefultChoseLanguge == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                    mTxtCoinUnit.setText(getResources().getString(R.string.activity_coin_unit_txt_05));
                } else
                    mTxtCoinUnit.setText(getResources().getString(R.string.activity_coin_unit_txt_06));
            }
        }else{
            if(coinUnit == 1){
                mTxtCoinUnit.setText(getResources().getString(R.string.activity_coin_unit_txt_05));
            }else{
                mTxtCoinUnit.setText(getResources().getString(R.string.activity_coin_unit_txt_06));
            }
        }
    }

    private void initHandStatus() {
        //先判断是否有指纹
        if (mManager.isAboveApi23() && mManager.isHardwareDetected()) {
            mLayoutOpenHand.setVisibility(View.VISIBLE);
            mViewline.setVisibility(View.VISIBLE);
            int style = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_HAND_STATUS);
            if (style == 0) {
                mTxtHandStatus.setText(getResources().getString(R.string.activity_system_setting_txt_11_02));
            } else {
                mTxtHandStatus.setText(getResources().getString(R.string.activity_system_setting_txt_11_01));
            }
        } else {
            mLayoutOpenHand.setVisibility(View.GONE);
            mViewline.setVisibility(View.GONE);
        }
    }

    private void initXunfuBtnStatus() {
        int style = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_XUANFUBTN_STYLE);
        if (style == 0) {
            mTxtXuanfuBtnStatus.setText(getResources().getString(R.string.activity_system_setting_txt_11_01));
        } else {
            mTxtXuanfuBtnStatus.setText(getResources().getString(R.string.activity_system_setting_txt_11_02));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x367 && resultCode == RESULT_OK) {
            setResult(0x267);
            finish();
        } else if (requestCode == 0x368 && resultCode == RESULT_OK) {
            initNumStyle();
        } else if (requestCode == 0x369) {
            initHandStatus();
        }else if(requestCode == 0x371 && resultCode == RESULT_OK){
            initCoinUnit();
        }
    }
}
