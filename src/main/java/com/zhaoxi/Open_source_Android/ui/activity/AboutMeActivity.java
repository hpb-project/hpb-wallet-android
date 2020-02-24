package com.zhaoxi.Open_source_Android.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonTipDialog;
import com.zhaoxi.Open_source_Android.libs.utils.DialogDownload;
import com.zhaoxi.Open_source_Android.libs.utils.DownLoadUtil;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.SystemInfoUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.CheckAPPVersionRequest;
import com.zhaoxi.Open_source_Android.net.bean.VersionBean;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutMeActivity extends BaseTitleBarActivity {
    private static final int INSTALL_PACKAGES_REQUESTCODE = 2;

    @BindView(R.id.activity_about_me_txt_version)
    TextView mTxtVersion;
    @BindView(R.id.activity_about_me_img_version)
    TextView mTxtHasNewVersion;
    @BindView(R.id.activity_about_me_layout_03)
    RelativeLayout mLayoutVersion;

    private VersionBean mVersionBean;
    private File mApkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);
        initViews();
        initDatas();
    }

    private void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleGray();
        setTitle(R.string.activity_about_us_txt_title,true);
        String version = SystemInfoUtil.getAppVersionName(this);
        mTxtVersion.setText(getResources().getString(R.string.activity_about_us_txt_05) + version);
    }

    private void initDatas() {
        new CheckAPPVersionRequest(this).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                if (jsonArray.get(2) != null) {
                    mVersionBean = JSON.parseObject(jsonArray.get(2).toString(), VersionBean.class);
                    int version = Integer.valueOf(SystemInfoUtil.getAppVersionName(AboutMeActivity.this).replace(".",""));
                    //判断当前版本是否与本地保存的版本一致
                    if (version<Integer.valueOf(mVersionBean.getVerNo().replace(".",""))) {//更新
                        mTxtHasNewVersion.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @OnClick({R.id.activity_about_me_layout_01, R.id.activity_about_me_layout_02,
            R.id.activity_about_me_layout_03})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_about_me_layout_01:
                gotoWeb(getResources().getString(R.string.activity_about_us_txt_01), Config.COMMON_WEB_URL + DAppConstants.backUrlHou(this, 8));
                break;
            case R.id.activity_about_me_layout_02:
                gotoWeb(getResources().getString(R.string.activity_about_us_txt_02), Config.COMMON_WEB_URL + DAppConstants.backUrlHou(this, 4));
                break;
            case R.id.activity_about_me_layout_03:
                int version = Integer.valueOf(SystemInfoUtil.getAppVersionName(AboutMeActivity.this).replace(".",""));
                if (mVersionBean != null) {
                    if (version<Integer.valueOf(mVersionBean.getVerNo().replace(".",""))) {//更新
                        showAppUpdate(mVersionBean.getDownloadUrl());
                    }else{
                        DappApplication.getInstance().showToast(getResources().getString(R.string.about_activity_toast));
                    }
                }
                break;
        }
    }

    private void gotoWeb(String title, String url) {
        Intent goto_webView = new Intent(this, CommonWebActivity.class);
        goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, title);
        goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, url);
        startActivity(goto_webView);
    }

    private void showAppUpdate(final String apkUrl) {
        CommonTipDialog.Builder myAlterDialog = new CommonTipDialog.Builder(AboutMeActivity.this);
        myAlterDialog.setTitle(getResources().getString(R.string.activity_about_me_version_title_left)
                + mVersionBean.getVerNo());
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
                        DownLoadUtil.downLoad(AboutMeActivity.this, apkUrl, new DialogDownload.OnDownLoadCompletedListener() {
                            @Override
                            public void downloadSuccess(File apkFile) {
                                mApkFile = apkFile;
                                checkIsAndroidO(apkFile);
                            }

                            @Override
                            public void downloadFailed(String error) {
                                downlaodFailed(error, apkUrl);
                            }

                            @Override
                            public void downloadCancle(String str) {
                                downlaodFailed(str, apkUrl);
                            }
                        }, mTxtTitle);
                    }
                });

        myAlterDialog.setNegativeButton(getString(R.string.UMNotNow), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        CommonTipDialog dialog = myAlterDialog.create();

        if (!AboutMeActivity.this.isFinishing()) {
            dialog.show();
        }
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.8);
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 下载失败 或 取消失败，提示语，再次显示下载框
     *
     * @param str
     * @param apkUrl
     */
    private void downlaodFailed(String str, String apkUrl) {
        DappApplication.getInstance().showToast(str);
        showAppUpdate(apkUrl);
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

    /* 设置权限开始   */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == INSTALL_PACKAGES_REQUESTCODE){
            //有注册权限且用户允许安装
            installApk(mApkFile);
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
            Uri contentUri = FileProvider.getUriForFile(this, "com.zhaoxi.Open_source_Android.dapp.fileprovider", apkFile);
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
}
