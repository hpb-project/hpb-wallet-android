package com.zhaoxi.Open_source_Android.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.biometriclib.BiometricPromptManager;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SystemInfoUtil;

import java.io.File;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 引导页
 * @author zhutt on 2018-07-03
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class GuideActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = GuideActivity.class.getSimpleName();
    private static String PATHS[] = new String[]{DAppConstants.PATH_DOWNLOAD_FILE, DAppConstants.PATH_PIC_FILE,
            DAppConstants.PATH_PIC_FILE_SMALL, DAppConstants.PATH_PIC_HEADER_FILE, DAppConstants.PATH_EXPROT_FILE};
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private BGABanner mForegroundBanner;
    public static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0x002;
    private int mInputPsdStyleCode;
    private BiometricPromptManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestCodeQRCodePermissions();
        initView();
        setListener();
        processLogic();
    }

    private void initView() {
        setContentView(R.layout.activity_guide);
        mManager = BiometricPromptManager.from(this);
        mForegroundBanner = findViewById(R.id.banner_guide_foreground);
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                String version = SystemInfoUtil.getAppVersionName(GuideActivity.this);
                SharedPreferencesUtil.setSharePreString(GuideActivity.this,SharedPreferencesUtil.APP_OLD_VERSION,version);
                goMain();
            }
        });
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        int mLangusge = ChangeLanguageUtil.languageType(this);
        if(mLangusge == 1){
            mForegroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                    R.drawable.hpb_guide_background_1,
                    R.drawable.hpb_guide_background_2,
                    R.drawable.hpb_guide_background_3);
        }else{
            mForegroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                    R.drawable.hpb_guide_background_en_1,
                    R.drawable.hpb_guide_background_en_2,
                    R.drawable.hpb_guide_background_en_3);
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
                goMainC();
            }else{
                if(mInputPsdStyleCode != 1){
                    OpenPasd();
                }
            }
        }
    }

    private void goMain() {
        //1.检查是否开启安全登录
        int status = SharedPreferencesUtil.getSharePreInt(this,SharedPreferencesUtil.CHANGE_HAND_STATUS);
        if(status == 1){//开启
            if (mManager.isBiometricPromptEnable()) {
                OpenPasd();
            }else goMainC();
        }else{
            goMainC();
        }
    }

    private void goMainC(){
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
    }

    private void OpenPasd(){
        if (mManager.isBiometricPromptEnable()) {
            mManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                @Override
                public void onUsePassword() {
                    mManager.showAuthenticationScreen(GuideActivity.this,REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
                }

                @Override
                public void onSucceeded() {
                    goMainC();
                }

                @Override
                public void onFailed() {
                }

                @Override
                public void onError(int code, String reason) {
                    if(code != 5){
                        mInputPsdStyleCode = 1;
                        mManager.showAuthenticationScreen(GuideActivity.this,REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
                    }
                }

                @Override
                public void onCancel() {
                    finish();
                }
            });
        }
    }
    /* 设置权限开始   */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] mPermissionList = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, mPermissionList)) {
            ActivityCompat.requestPermissions(this, mPermissionList, REQUEST_CODE_QRCODE_PERMISSIONS);
        } else {
            for (int i = 0; i < PATHS.length; i++) {
                String path;
                if (FileManager.hasSDCard()) {
                    path = DAppConstants.SD_CARD_PATH + PATHS[i];
                } else {
                    path = DAppConstants.DATA_DIRECTORY_PATH + PATHS[i];
                }
                File exportDirPath = new File(path);
                if (!exportDirPath.exists()) {
                    exportDirPath.mkdirs();
                }
            }
        }
    }
    /* 设置权限接受   */
}
