package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.anim.AnimationsContainer;
import com.zhaoxi.Open_source_Android.libs.biometriclib.BiometricPromptManager;
import com.zhaoxi.Open_source_Android.libs.pushMsg.ServiceHelper;
import com.zhaoxi.Open_source_Android.libs.pushMsg.UmenPushManager;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.utils.AntiEmulatorUtil;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.ImageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.ShareUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SystemInfoUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetAdvertiseRequest;
import com.zhaoxi.Open_source_Android.net.bean.AdvertiseBean;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SplashActivity extends BaseActivity {
    private static final int GO_GUIDE = 1002;
    private static final int SHOW_AD = 1003;
    private static final int NO_SHOW_AD = 1004;
    private static final int SHOW_LOAD_AD = 1005;

    private static final long SPLASH_DELAY_MILLIS = 2500;
    public static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0x001;
    @BindView(R.id.splash_img_advertising)
    ImageView mImgAdvertising;
    @BindView(R.id.splash_layout_bar)
    LinearLayout mLayoutBar;
    @BindView(R.id.splash_txt_bar_time)
    TextView mTxtTime;
    @BindView(R.id.splash_img_animation)
    ImageView mImgAnimation;

    private int mInputPsdStyleCode;
    private ChangeLanguageUtil mChangeLanguageUtil;
    private BiometricPromptManager mManager;

    private String mAdFileName, mFileUrl;
    private String mAdFileType = "png";
    private int recLen = 3;
    private String mSkipType, mSkipUrl, mNextPage, mAdName;
    private boolean isAdCheack = false;
    private AnimationsContainer.FramesSequenceAnimation mAnimSplah;
    private int mCheckType = 1;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_GUIDE:
                    goGuide();
                    break;
                case SHOW_AD:
                    byte[] bytes = (byte[]) msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    try {
                        ImageUtil.saveAdFilePath(bitmap, mAdFileName, mAdFileType);
                        mImgAdvertising.setImageBitmap(bitmap);
                        showAd();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case NO_SHOW_AD:
                    goMain();
                    break;
                case SHOW_LOAD_AD:
                    String path = (String) msg.obj;
                    Bitmap bp = ShareUtil.getLoacalBitmap(SplashActivity.this, path);
                    mImgAdvertising.setImageBitmap(bp);
                    showAd();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }

        ButterKnife.bind(this);
        if (mAnimSplah == null)
            mAnimSplah = AnimationsContainer.getInstance(R.array.splash_anim, 30).createProgressDialogAnim(mImgAnimation, true);
        mAnimSplah.start();
        mShowCopy = false;
        mChangeLanguageUtil = new ChangeLanguageUtil();

        mManager = BiometricPromptManager.from(this);
        //判断运行环境
        isCheckedArt();
    }

    @OnClick(R.id.splash_img_advertising)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_img_advertising:
                isAdCheack = true;
                //1.检查是否开启安全登录
                int status = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_HAND_STATUS);
                if (status == 1) {//开启
                    //先判断是否已设置指纹 没有去提示开启
                    if (mManager.isBiometricPromptEnable()) {
                        mCheckType = 2;
                        OpenPasd();
                    } else goMainA();
                } else {
                    goMainA();
                }
                break;
        }
    }

    private void isCheckedArt() {
        //1.判断手机是否root
        if (new File("/system/bin/su").exists() || new File("/system/xbin/su").exists()) {
//            //弹出框提示 已root
            showWarnDialog(getResources().getString(R.string.activity_splash_warn_root));
        } else {//2.0判断是否为虚拟机
            if (AntiEmulatorUtil.isFeatures()) {
                showWarnDialog(getResources().getString(R.string.activity_splash_warn_vm));
            } else {
                String version = SystemInfoUtil.getAppVersionName(this);
                String verOldSion = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.APP_OLD_VERSION);
                if (StrUtil.isEmpty(verOldSion)) {
                    SharedPreferencesUtil.setSharePreString(this, SharedPreferencesUtil.APP_OLD_VERSION, version);
                    mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
                } else {
                    //判断当前版本是否与本地保存的版本一致
                    if (Integer.valueOf(verOldSion.replace(".", "")) < Integer.valueOf(version.replace(".", ""))) {
                        SharedPreferencesUtil.setSharePreString(this, SharedPreferencesUtil.APP_OLD_VERSION, version);
                        mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
                    } else {
//                        //判断是否是24之后
                        boolean isShowAd = DateUtilSL.isShowAdNow(this);
                        if (isShowAd) {
                            //向后台请求是否有广告
                            getAdvertise();
                        } else {
                            mHandler.sendEmptyMessageDelayed(NO_SHOW_AD, SPLASH_DELAY_MILLIS);
                        }
                    }
                }
            }
        }
    }

    private void getAdvertise() {
        new GetAdvertiseRequest(this).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    AdvertiseBean advertiseBean = JSON.parseObject(jsonArray.get(2).toString(), AdvertiseBean.class);
                    if (advertiseBean.getState().equals("2")) {
                        mFileUrl = advertiseBean.getPicUrl();
                        mSkipType = advertiseBean.getSkipType();
                        mSkipUrl = advertiseBean.getSkipUrl();
                        mNextPage = advertiseBean.getNextPage();
                        mAdName = advertiseBean.getName();
                        try {
                            downloadImage(mFileUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mHandler.sendEmptyMessageDelayed(NO_SHOW_AD, SPLASH_DELAY_MILLIS);
                    }
                } else mHandler.sendEmptyMessageDelayed(NO_SHOW_AD, SPLASH_DELAY_MILLIS);
            }

            @Override
            public void onError(String error) {
                mHandler.sendEmptyMessageDelayed(NO_SHOW_AD, SPLASH_DELAY_MILLIS);
            }
        });
    }

    private void showAd() {
        String nowAdShowData = DateUtilSL.getCurTimestamp();
        SharedPreferencesUtil.setSharePreString(SplashActivity.this, SharedPreferencesUtil.AD_SHOW_TIME, nowAdShowData);
        mImgAdvertising.setVisibility(View.VISIBLE);
        mLayoutBar.setVisibility(View.VISIBLE);
        mLayoutBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdCheack = true;
                goMain();
            }
        });
        setDuration();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isAdCheack) {
                if (recLen < 0) {
                    goMain();
                } else {
                    mTxtTime.setText("" + recLen);
                    recLen--;
                    handler.postDelayed(this, 1000);
                }
            }
        }
    };


    public void setDuration() {
        handler.postDelayed(runnable, 1000);
    }

    /**
     * 下载图片
     *
     * @param url
     * @return byte[]
     */
    public void downloadImage(String url) throws IOException {
        //判断文件是否存在
        String u[] = url.split("/");
        String n = u[u.length - 1];
        String a[] = n.split("\\.");
        mAdFileName = a[0];
        mAdFileType = a[1];
//        mAdFileName = u[u.length - 1];
//        String path = FileManager.createPic(DAppConstants.PATH_PIC_FILE) + mAdFileName;
        String path = FileManager.createPic(DAppConstants.PATH_PIC_FILE) + mAdFileName + "." + mAdFileType;
        if (FileManager.checkFileExists(path)) {
            Message message = mHandler.obtainMessage();
            message.what = SHOW_LOAD_AD;
            message.obj = path;
            mHandler.sendMessageDelayed(message, SPLASH_DELAY_MILLIS);
        } else {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = mHandler.obtainMessage();
                    message.what = NO_SHOW_AD;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] bytes = response.body().bytes();
                    Message message = mHandler.obtainMessage();
                    message.what = SHOW_AD;
                    message.obj = bytes;
                    mHandler.sendMessage(message);
                }
            });
        }
    }

    private void goMain() {
        //1.检查是否开启安全登录
        int status = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_HAND_STATUS);
        if (status == 1) {//开启
            //先判断是否已设置指纹 没有去提示开启
            if (mManager.isBiometricPromptEnable()) {
                OpenPasd();
            } else goMainC();
        } else {
            goMainC();
        }
    }


    private void goMainA() {
        Intent adIntent = new Intent(SplashActivity.this, MainActivity.class);
        adIntent.putExtra(UmenPushManager.SPLAH_AD_NAME, 1);//为了与推送区分
        if (mSkipType.equals("2")) {//跳转类型：1-内部，2-H5
            adIntent.putExtra(UmenPushManager.CALL_TO_ACTIVITY, CommonWebActivity.class);
            adIntent.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, mAdName);
            adIntent.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, mSkipUrl);
        } else if (mSkipType.equals("1")) {
            switch (mNextPage) {//跳转页面：1-红包，2-我的投票，3-转账
                case "1":
                    adIntent.putExtra(UmenPushManager.CALL_TO_ACTIVITY, SendRedPacketsActivity.class);
                    break;
                case "2":
                    adIntent.putExtra(UmenPushManager.CALL_TO_ACTIVITY, MyVoteRecordActivity.class);
                    break;
                case "3":
                    adIntent.putExtra(UmenPushManager.CALL_TO_ACTIVITY, TransferActivity.class);
                    break;
            }
        }
        startActivity(adIntent);
        finish();
    }

    private void goMainC() {
        int value = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
        mChangeLanguageUtil.init(this, value, false);
        //如果是点击通知打开的则设置通知参数
        Intent toMain = new Intent(this, MainActivity.class);
        ServiceHelper.startAppMainActivitySetNoticeIntent(this, toMain);
    }

    private void goGuide() {
        int value = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
        mChangeLanguageUtil.init(this, value, false);
        startActivity(new Intent(this, GuideActivity.class));
        finish();
    }

    /**
     * 温馨提示
     */
    private void showWarnDialog(String msg) {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(null, msg, null,
                null, null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String version = SystemInfoUtil.getAppVersionName(SplashActivity.this);
                        String verOldSion = SharedPreferencesUtil.getSharePreString(SplashActivity.this, SharedPreferencesUtil.APP_OLD_VERSION);
                        if (StrUtil.isEmpty(verOldSion)) {
                            SharedPreferencesUtil.setSharePreString(SplashActivity.this, SharedPreferencesUtil.APP_OLD_VERSION, version);
                            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
                        } else {
                            //判断当前版本是否与本地保存的版本一致
                            if (Integer.valueOf(verOldSion.replace(".", "")) < Integer.valueOf(version.replace(".", ""))) {
                                SharedPreferencesUtil.setSharePreString(SplashActivity.this, SharedPreferencesUtil.APP_OLD_VERSION, version);
                                mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
                            } else {
                                getAdvertise();
                            }
                        }

                    }
                });
    }

    private void OpenPasd() {
        if (mManager.isBiometricPromptEnable()) {
            mManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                @Override
                public void onUsePassword() {
                    mManager.showAuthenticationScreen(SplashActivity.this, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
                }

                @Override
                public void onSucceeded() {
                    if (mCheckType == 1) {
                        goMainC();
                    } else {
                        goMainA();
                    }
                }

                @Override
                public void onFailed() {
                }

                @Override
                public void onError(int code, String reason) {
                    if (code != 5) {
                        mInputPsdStyleCode = 1;
                        mManager.showAuthenticationScreen(SplashActivity.this, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
                    }
                }

                @Override
                public void onCancel() {
                    finish();
                }
            });
        }
    }

    /**
     * 接收系统解锁是否成功
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                goMainC();
            } else {
                if (mInputPsdStyleCode != 1) {
                    OpenPasd();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnimSplah != null) {
            mAnimSplah.stop();
        }
    }
}
