package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.libs.biometriclib.BiometricPromptManager;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SelfLoadActivity extends BaseTitleBarActivity {
    public static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0x006;

    @BindView(R.id.self_load_img_hand_change)
    ImageView mImgHandChange;

    private int mHandStuats;
    private BiometricPromptManager mManager;
    private int mInputPsdStyleCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_load);
        ButterKnife.bind(this);
        setTitle(R.string.activity_system_setting_txt_10,true);
        mManager = BiometricPromptManager.from(this);
        initHandStatus();
    }

    private void initHandStatus(){
        int style = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_HAND_STATUS);
        if (style == 0) {
            mHandStuats = 0;
            mImgHandChange.setImageResource(R.mipmap.icon_hand_toggle_close);
        }else{
            mHandStuats = 1;
            mImgHandChange.setImageResource(R.mipmap.icon_hand_toggle_open);
        }
    }

    @OnClick(R.id.self_load_img_hand_change)
    public void onViewClicked() {
        if(mHandStuats == 0){
            mHandStuats = 1;
        }else{
            mHandStuats = 0;
        }
        OpenPasd(mHandStuats);
    }
    private void OpenPasd(int handStatus){
        if (mManager.isBiometricPromptEnable()) {
            mManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                @Override
                public void onUsePassword() {
                    mManager.showAuthenticationScreen(SelfLoadActivity.this,REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
                }

                @Override
                public void onSucceeded() {
                    if(mHandStuats == 1){
                        mImgHandChange.setImageResource(R.mipmap.icon_hand_toggle_open);
                    }else{
                        mImgHandChange.setImageResource(R.mipmap.icon_hand_toggle_close);
                    }
                    SharedPreferencesUtil.setSharePreInt(SelfLoadActivity.this, SharedPreferencesUtil.CHANGE_HAND_STATUS,mHandStuats);
                }

                @Override
                public void onFailed() {
                }

                @Override
                public void onError(int code, String reason) {
                    if(code != 5){
                        mInputPsdStyleCode = 1;
                        mManager.showAuthenticationScreen(SelfLoadActivity.this,REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
                    }
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    /**
     *接收系统解锁是否成功
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                if(mHandStuats == 1){
                    mImgHandChange.setImageResource(R.mipmap.icon_hand_toggle_open);
                }else{
                    mImgHandChange.setImageResource(R.mipmap.icon_hand_toggle_close);
                }
                SharedPreferencesUtil.setSharePreInt(SelfLoadActivity.this, SharedPreferencesUtil.CHANGE_HAND_STATUS,mHandStuats);
            }else{
                if(mInputPsdStyleCode != 1){
                    OpenPasd(mHandStuats);
                }
            }
        }
    }
}
