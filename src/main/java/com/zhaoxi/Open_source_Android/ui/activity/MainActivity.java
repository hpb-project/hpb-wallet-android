package com.zhaoxi.Open_source_Android.ui.activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonTipDialog;
import com.zhaoxi.Open_source_Android.common.view.NoScrollViewPager;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.pushMsg.ServiceHelper;
import com.zhaoxi.Open_source_Android.libs.tools.CreateQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.MainChangePagerHomeListener;
import com.zhaoxi.Open_source_Android.libs.tools.MainChangePagerMeListener;
import com.zhaoxi.Open_source_Android.libs.tools.MainChangePagerNewsListener;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DialogDownload;
import com.zhaoxi.Open_source_Android.libs.utils.DownLoadUtil;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SystemInfoUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.CheckAPPVersionRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetMsgLastIdRequest;
import com.zhaoxi.Open_source_Android.net.Request.PacketShakeRedRequest;
import com.zhaoxi.Open_source_Android.net.Request.PushAppDeviceRequest;
import com.zhaoxi.Open_source_Android.net.bean.RedBaseBean;
import com.zhaoxi.Open_source_Android.net.bean.VersionBean;
import com.zhaoxi.Open_source_Android.ui.adapter.HomePagerAdapter;
import com.zhaoxi.Open_source_Android.ui.dialog.SensorEndDialog;
import com.zhaoxi.Open_source_Android.ui.dialog.SensorStartDialog;
import com.zhaoxi.Open_source_Android.ui.fragment.MainHomeFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.MainMeFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.MainNewsFragment;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseTitleBarActivity implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks {
    public static final String SET_CURRENT_CHECKED = "MainActivity.SET_CURRENT_CHECKED";
    public static final String HANDLE_TYPE = "MainActivity.HANDLE_TYPE";//1.切换中英文

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int INSTALL_PACKAGES_REQUESTCODE = 2;

    @BindView(R.id.main_activity_viewpager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.main_activity_tab_home)
    LinearLayout mLayoutHome;
    @BindView(R.id.main_activity_tab_find)
    LinearLayout mLayoutFind;
    @BindView(R.id.main_activity_tab_me)
    LinearLayout mLayoutMe;

    @BindView(R.id.main_activity_tab_home_img)
    ImageView mImgHome;
    @BindView(R.id.main_activity_tab_home_txt)
    TextView mTxtHome;
    @BindView(R.id.main_activity_tab_find_img)
    ImageView mImgFind;
    @BindView(R.id.main_activity_tab_find_txt)
    TextView mTxtFind;
    @BindView(R.id.main_activity_tab_me_img)
    ImageView mImgMe;
    @BindView(R.id.main_activity_tab_me_txt)
    TextView mTxtMe;

    @BindView(R.id.root_view_main)
    RelativeLayout mRootView;

    private static TextView mTxtSpot;
    private static LinearLayout mLayouyNavigetor;

    private long mExitTime = 0L;

    private CreateDbWallet mCreateDbWallet;
    private ChangeLanguageUtil mChangeLanguageUtil;

    private static String PATHS[] = new String[]{DAppConstants.PATH_DOWNLOAD_FILE, DAppConstants.PATH_PIC_FILE,
            DAppConstants.PATH_PIC_FILE_SMALL, DAppConstants.PATH_PIC_HEADER_FILE, DAppConstants.PATH_EXPROT_FILE};
    private VersionBean mVersionBean;
    private File mApkFile;

    public static int width;
    public static int height;
    private HomePagerAdapter homePagerAdapter;
    private Intent intent;//onNewIntent 带的intent

    /**
     * 传感器
     */
    private static SensorManager sensorManager;
    private static ShakeSensorListener shakeListener;
    private SensorStartDialog mSendorDialog;
    /**
     * 判断一次摇一摇动作
     */
    private boolean isShake = false;
    private static boolean isHome = true;
    private static int mXuanfuBtn;
    private static String mDefultAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCreateDbWallet = new CreateDbWallet(this);
        intent = getIntent();
        requestCodeQRCodePermissions();
        initViews();
        initDatas();
        getDisplayMetrics();
        postAppDevice();
        getMsgLastId();

        ServiceHelper.isAppWithNoticeOpen(this);
    }

    private void initViews() {
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).navigationBarColor(R.color.base_theme_color).init();
        mTxtSpot = findViewById(R.id.main_activity_tab_msg_spot);
        mLayouyNavigetor = findViewById(R.id.main_activity_navigetor);
        isHome = true;
        mImgHome.setSelected(true);
        mTxtHome.setSelected(true);

        mLayoutHome.setOnClickListener(this);
        mLayoutFind.setOnClickListener(this);
        mLayoutMe.setOnClickListener(this);

        initFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mXuanfuBtn = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_XUANFUBTN_STYLE);
        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (sensorManager == null) {
            sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
            shakeListener = new ShakeSensorListener();
        }
        if (mXuanfuBtn == 0 && isHome) {
            setRegisterSensor();
        }
    }

    /**
     * 设置默认钱包
     */
    private void initDatas() {
        //先判断是否有默认钱包
        String defultWallet = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (StrUtil.isEmpty(defultWallet)) {//没有就获取最新的钱包
            //将最新的钱包设置为默认钱包
            WalletBean walletBean = mCreateDbWallet.queryNewWallet();
            if (walletBean != null) {
                SharedPreferencesUtil.setSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS, walletBean.getAddress());
            }
        }
        //检测版本
        checkeVersion();
    }

    /**
     * 初始化 Fragment
     */
    private void initFragments() {
        mViewPager.setOffscreenPageLimit(2);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MainHomeFragment());
        fragmentList.add(new MainNewsFragment());
        fragmentList.add(new MainMeFragment());
        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(homePagerAdapter);
        mViewPager.setCurrentItem(0, false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_tab_home:
                checkedCurrent(0);
                break;
            case R.id.main_activity_tab_find:
                checkedCurrent(1);
                break;
            case R.id.main_activity_tab_me:
                checkedCurrent(2);
                break;
        }
    }

    private void checkedCurrent(int cur) {
        switch (cur) {
            case 0:
                isHome = true;
                tabSelected(mImgHome, mTxtHome);
                mChangeHomeViewPager.changeHomePager();
                if (mXuanfuBtn == 0) {
                    setRegisterSensor();
                }
                mViewPager.setCurrentItem(0, false);
                break;
            case 1:
                isHome = false;
                tabSelected(mImgFind, mTxtFind);
                mChangeNewsViewPager.changeNewsPager();
                if (mXuanfuBtn == 0) {
                    setUnRegisterSensor();
                }
                mViewPager.setCurrentItem(1, false);
                break;
            case 2:
                isHome = false;
                tabSelected(mImgMe, mTxtMe);
                mChangeViewPager.changePager();
                if (mXuanfuBtn == 0) {
                    setUnRegisterSensor();
                }
                mViewPager.setCurrentItem(2, false);
                break;
        }
    }

    private void tabSelected(ImageView img, TextView txt) {
        mImgHome.setSelected(false);
        mImgFind.setSelected(false);
        mImgMe.setSelected(false);
        mTxtHome.setSelected(false);
        mTxtFind.setSelected(false);
        mTxtMe.setSelected(false);
        img.setSelected(true);
        txt.setSelected(true);
    }

    private static void setRegisterSensor() {
        //判断是否钱包
        if(!StrUtil.isEmpty(mDefultAddress)){
            sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    private static void setUnRegisterSensor() {
        sensorManager.unregisterListener(shakeListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.intent = intent;
    }

    private MainChangePagerMeListener mChangeViewPager;
    private MainChangePagerHomeListener mChangeHomeViewPager;
    private MainChangePagerNewsListener mChangeNewsViewPager;


    public void setChangeViewPager(MainChangePagerMeListener changeViewPager) {
        this.mChangeViewPager = changeViewPager;
    }

    public void setChangeHomeViewPager(MainChangePagerHomeListener changeHomeViewPager) {
        this.mChangeHomeViewPager = changeHomeViewPager;
    }

    public void setChangeNewsViewPager(MainChangePagerNewsListener changeNewsViewPager) {
        this.mChangeNewsViewPager = changeNewsViewPager;
    }


    /* 设置权限开始   */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == INSTALL_PACKAGES_REQUESTCODE) {
            //有注册权限且用户允许安装
            installApk(mApkFile);
        } else {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
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
    /* 设置权限结束   */

    /**
     * 检测新版本
     */
    private void checkeVersion() {
        new CheckAPPVersionRequest(this).doRequest(this, new NetResultCallBack() {

            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    mVersionBean = JSON.parseObject(jsonArray.get(2).toString(), VersionBean.class);
                    int version = Integer.valueOf(SystemInfoUtil.getAppVersionName(MainActivity.this).replace(".", ""));
                    //判断当前版本是否与本地保存的版本一致
                    if (version < Integer.valueOf(mVersionBean.getVerNo().replace(".", ""))) {//更新
                        showAppUpdate(mVersionBean.getIsForceFlag().equals("0") ? false : true, mVersionBean.getDownloadUrl());
                    }
                }
            }

            @Override
            public void onError(String error) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.version_cheack_error));
            }
        });
    }

    private void showAppUpdate(final boolean isNeedUpdate, final String apkUrl) {
        CommonTipDialog.Builder myAlterDialog = new CommonTipDialog.Builder(MainActivity.this);
        myAlterDialog.setTitle(getResources().getString(R.string.activity_about_me_version_title_left)
                + mVersionBean.getVerNo());
        if (isNeedUpdate) {
            // 强制更新
            myAlterDialog.setMessage(getString(R.string.dialog_content_update_must));
        }
        myAlterDialog.setMessage2(getResources().getString(R.string.activity_about_me_version_content) + mVersionBean.getVerContent());
        myAlterDialog.setPositiveButton(getString(R.string.UMUpdateNow),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        String path = "";
                        if (FileManager.hasSDCard()) {
                            path = DAppConstants.SD_CARD_PATH + DAppConstants.PATH_DOWNLOAD_FILE;
                        } else {
                            path = DAppConstants.SD_CARD_PATH + DAppConstants.PATH_DOWNLOAD_FILE;
                        }
                        File exportDirPath = new File(path);
                        if (!exportDirPath.exists()) {
                            exportDirPath.mkdirs();
                        }
                        DownLoadUtil.downLoad(MainActivity.this, apkUrl, new DialogDownload.OnDownLoadCompletedListener() {
                            @Override
                            public void downloadSuccess(File apkFile) {
                                mApkFile = apkFile;
                                checkIsAndroidO(apkFile);
                            }

                            @Override
                            public void downloadFailed(String error) {
                                downlaodFailed(error, isNeedUpdate, apkUrl);
                            }

                            @Override
                            public void downloadCancle(String str) {
                                downlaodFailed(str, isNeedUpdate, apkUrl);
                            }
                        }, mLayoutHome);
                    }
                });
        if (isNeedUpdate) {
            myAlterDialog.setNegativeButton(getString(R.string.UMOutApp), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                    finish();
                }
            });
        } else {
            myAlterDialog.setNegativeButton(getString(R.string.UMNotNow), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
        }
        CommonTipDialog dialog = myAlterDialog.create();
        if (isNeedUpdate) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        if (!MainActivity.this.isFinishing()) {
            dialog.show();
        }
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.8);
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 下载失败 或 取消失败，提示语，再次显示下载框
     *
     * @param str
     * @param isNeedUpdate
     * @param apkUrl
     */
    private void downlaodFailed(String str, boolean isNeedUpdate, String apkUrl) {
        DappApplication.getInstance().showToast(str);
        showAppUpdate(isNeedUpdate, apkUrl);
    }

    /**
     * 判断是否是8.0系统,是的话需要获取此权限，判断开没开，没开的话处理未知应用来源权限问题,否则直接安装
     */
    private void checkIsAndroidO(File apkFile) {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk(apkFile);
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            installApk(apkFile);
        }
    }

    /**
     * 安装apk
     */
    private void installApk(File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.zhaoxi.Open_source_Android.fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            startActivity(intent);
            finish();
        }
    }

    private void postAppDevice() {
        List<String> listAddress = mCreateDbWallet.queryAllAddress();
        mChangeLanguageUtil = new ChangeLanguageUtil();
        int languageType = mChangeLanguageUtil.languageType(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceDetail", SystemInfoUtil.getDeviceId(this));//设备的唯一标识
        params.put("platform", 0);//设备系统 0-安卓 1-IOS 2-其它
        params.put("langType", (languageType - 1));//语言类型：0-中文；1-英文
        params.put("appVersion", SystemInfoUtil.getAppVersionName(this));//app版本
        params.put("addressList", listAddress);//设备的钱包地址列表，数组形式

        String appDevice = JSON.toJSONString(params);
        SystemLog.I("ztt", appDevice);

        new PushAppDeviceRequest(appDevice).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getMsgLastId() {
        String deviceId = SystemInfoUtil.getDeviceId(this);
        int type = ChangeLanguageUtil.languageType(this);

        new GetMsgLastIdRequest(this, deviceId, type).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                //获取本地保存的
                String oldLastId;
                if (type == 1) {
                    oldLastId = SharedPreferencesUtil.getSharePreString(MainActivity.this, SharedPreferencesUtil.MESSAGE_READ_LASTID_TH);
                } else {
                    oldLastId = SharedPreferencesUtil.getSharePreString(MainActivity.this, SharedPreferencesUtil.MESSAGE_READ_LASTID_EN);
                }
                String newLastId = jsonArray.get(2).toString();
                if (StrUtil.isEmpty(oldLastId)) {
                    if (!newLastId.equals("0")) {
                        mTxtSpot.setVisibility(View.VISIBLE);
                    } else {
                        mTxtSpot.setVisibility(View.GONE);
                    }
                    return;
                }
                if (Integer.valueOf(newLastId) > Integer.valueOf(oldLastId)) {
                    mTxtSpot.setVisibility(View.VISIBLE);
                } else {
                    mTxtSpot.setVisibility(View.GONE);
                }
            }
        });
    }

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mTxtSpot.setVisibility(View.GONE);
                    break;
                case 2:
                    if (mXuanfuBtn == 0) {
                        setUnRegisterSensor();
                    }
                    mLayouyNavigetor.setVisibility(View.GONE);
                    break;
                case 3:
                    if (mXuanfuBtn == 0) {
                        setRegisterSensor();
                    }
                    mLayouyNavigetor.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    private class ShakeSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            //避免一直摇
            if (isShake) {
                return;
            }
            float[] values = event.values;
            /*
             * x : x轴方向的重力加速度，向右为正
             * y : y轴方向的重力加速度，向前为正
             * z : z轴方向的重力加速度，向上为正
             */
            float x = Math.abs(values[0]);
            float y = Math.abs(values[1]);
            float z = Math.abs(values[2]);
            //加速度超过19，摇一摇成功
            if ((x > 40 || y > 40 || z > 40) && !isShake) {
                showHandDialog();
                isShake = true;
                //播放正在摇一摇时声音
//                playSound(MainActivity.this);
                playSound(MainActivity.this,R.raw.shake_sound);
                //震动，注意权限
                vibrate(500);
                //仿网络延迟操作，这里可以去请求服务器...
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //
                        //获取数据同时把摇一摇动画取消
                        new PacketShakeRedRequest().doRequest(MainActivity.this, new CommonResultListener(MainActivity.this) {
                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                // 播放摇一摇结束时的声音
                                playSound(MainActivity.this,R.raw.shake_sound_end);
                                RedBaseBean redBaseBean = JSON.parseObject(jsonArray.get(2).toString(), RedBaseBean.class);
                                mSendorDialog.dismiss();
                                if("2".equals(redBaseBean.getIsOver())){
                                    isShake = false;
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_gl_txt_22));
                                }else{
                                    showSensorEndDialog(redBaseBean);
                                }

                            }

                            @Override
                            public void onError(String error) {
                                super.onError(error);
                                isShake = false;
                                mSendorDialog.dismiss();
                            }
                        });
                    }
                }, 1000);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private void showSensorEndDialog(RedBaseBean redBaseBean) {
        SensorEndDialog.Builder sensorBuider = new SensorEndDialog.Builder(this);
        sensorBuider.setSensorEndListener(new SensorEndDialog.Builder.SensorEndListener() {
            @Override
            public void onHandleSensor() {
                isShake = false;
                Intent goto_details = new Intent(MainActivity.this, GetRedActivity.class);
                goto_details.putExtra(GetRedActivity.RED_RECORD_ID, redBaseBean.getRedPacketNo());
                goto_details.putExtra(GetRedActivity.RED_RECORD_KEY, redBaseBean.getKey());
                startActivity(goto_details);
            }

            @Override
            public void onNoHandleSensor() {
                isShake = false;
            }

        });
        sensorBuider.setType(redBaseBean.getKey());
        SensorEndDialog sensorEndDialog = sensorBuider.create();
        sensorEndDialog.setCanceledOnTouchOutside(false);
        // true 红包弹窗允许通过按下返回按钮关闭
        sensorEndDialog.setCancelable(true);
        sensorEndDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // 关闭红包弹出后允许继续摇一摇
                isShake = false;
            }
        });
        sensorEndDialog.show();

        WindowManager.LayoutParams params = sensorEndDialog.getWindow().getAttributes();
        params.height = (int) (this.getWindowManager().getDefaultDisplay().getHeight() * 0.8);
        params.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 1);
        sensorEndDialog.getWindow().setAttributes(params);
    }


    /**
     * 同步支持二维码Dialog
     * @param strContent
     */
    public void showSyncAssetsQrCodeDialog(String strContent) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_digital_sign_qrcode_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);

        ImageView ivDigitalSignQrCode = view.findViewById(R.id.iv_qr_code_info);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_qrcode_logo, null);
        CreateQrAsyncTask asyncTask = new CreateQrAsyncTask(this, ivDigitalSignQrCode, strContent, logo);
        asyncTask.setOnResultListener(new CreateQrAsyncTask.OnResultListener() {
            @Override
            public void setOnResultListener(Bitmap bitmap) {
                // 生成的bitmap
            }
        });
        asyncTask.execute();//开始执行

        Button btnNextTip = view.findViewById(R.id.btn_next_tip);
        btnNextTip.setText(getResources().getString(R.string.web_dapps_title_close));
        btnNextTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 下一步
                popupWindow.dismiss();
            }
        });

        // 设置背景透明度
        final WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isHome) {
            sensorManager.unregisterListener(shakeListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null)
            sensorManager = null;
    }

    private void playSound(Context context) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.shake_sound);
        player.start();
    }

    /**
     * 播放音频声音
     * @param context 上下问
     * @param resId 音频资源
     */
    private void playSound(Context context,int resId){
        MediaPlayer player = MediaPlayer.create(context, resId);
        player.start();
    }

    private void vibrate(long milliseconds) {
        Vibrator vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    private void showHandDialog() {
        if (mSendorDialog == null) {
            mSendorDialog = new SensorStartDialog(this, this.findViewById(R.id.root_view_main));
        }
        mSendorDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.toast_quit_again));
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }

            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getDisplayMetrics() {
        //已减去软键盘高度
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;  //以要素为单位
        height = metrics.heightPixels;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}