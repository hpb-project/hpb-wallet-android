package com.zhaoxi.Open_source_Android.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonScanResultDialog;
import com.zhaoxi.Open_source_Android.common.dialog.CommonTransfgerConfirmDialog;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.tools.ScanQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.CreateTradeRequest;
import com.zhaoxi.Open_source_Android.ui.AddaddressActivity;
import com.zhaoxi.Open_source_Android.web3.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ScaningActivity extends BaseTitleBarActivity implements QRCodeView.Delegate, EasyPermissions.PermissionCallbacks {
    private static final String TAG = ScaningActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    public static final int RESULT_CODE = 0x667;
    public static final String RESURE_TYPE = "ScaningActivity.RESURE_TYPE";
    public static final String RESURE_WALLET_TYPE = "ScaningActivity.RESURE_WALLET_TYPE";
    public static final String RESULT_CONTENT = "ScaningActivity.RESULT_CONTENT";

    // 数字签名
    public static final int TYPE_DIGITAL_SIGN = 0x06;
    // 同步资产
    public static final int TYPE_ASYNC_ASSETS = 0x07;
    // 转账
    public static final int TYPE_TRANSFER = 0x08;
    // 投票治理
    public static final int TYPE_VOTE_ZL = 0x09;
    // 竞选投票授权
    public static final int TYPE_VOTE = 0x10;
    // 竞选投票
    public static final int TYPE_VOTE_V = 0x11;
    // 节点分红
    public static final int TYPE_NODE_DED = 0x12;

    @BindView(R.id.zbarview)
    QRCodeView mQRCodeView;

    private int mType;//0：转账二维码  1:映射导入keystore 10:导入keystore 11：导入冷钱包 2.转账页面  3.冷钱包  4.添加地址 5.主页进入
    private int walletType;
    private CommonTransfgerConfirmDialog mCommonTransfgerConfirmDialog;
    private CommonScanResultDialog mCommonScanResultDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaning);
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra(RESURE_TYPE, 0);
        walletType = getIntent().getIntExtra(RESURE_WALLET_TYPE, 0);
//        if (mType == 3 || mType == 1) {
        setTitle(R.string.scan_activity_txt_title, true);
//        } else {
//            mTxtTitle.DefultLeftAndRightListener(this, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    gotoPhotos();
//                }
//            });
//        }
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    /**
     * 启动震动配置
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        mQRCodeView.stopSpot();
        gotoTransfer(result);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        DappApplication.getInstance().showToast(getResources().getString(R.string.scan_activity_txt_phone_error));
    }

    /**
     * 选择照片
     */
    private void gotoPhotos() {
        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(null)
                .maxChooseCount(1)
                .selectedPhotos(null)
                .pauseOnScroll(false)
                .build();
        startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
            ScanQrAsyncTask asyncTask = new ScanQrAsyncTask(this, picturePath, mType, mQRCodeView);
            asyncTask.execute();
        }
    }

    /**
     * 跳转到转账页面
     *
     * @param result
     */
    private void gotoTransfer(String result) {
        if (mType == 1) {
            Intent goto_import = new Intent(this, ImportWalletActivity.class);
            goto_import.putExtra(RESULT_CONTENT, result);
            setResult(RESULT_CODE, goto_import);
            finish();
        } else if (mType == 10) {
            Intent goto_import = new Intent(this, ImportWalletTwoActivity.class);
            goto_import.putExtra(RESULT_CONTENT, result);
            setResult(RESULT_CODE, goto_import);
            finish();
        } else if (mType == 11) {
            //判断是否是正确的二维码
            if (StrUtil.isEmpty(result) || !result.startsWith("0x") || result.length() != 42) {
                showCreateWalletDilaog();
                return;
            }

            if (!Numeric.isValidAddress(result)) {
                showCreateWalletDilaog();
                return;
            }

            Intent goto_import = new Intent(this, ImportWalletTwoActivity.class);
            goto_import.putExtra(RESULT_CONTENT, result);
            setResult(RESULT_CODE, goto_import);
            finish();
        } else if (mType == 3) {//跳转到冷钱包转账页面
            //判断是否是正确的二维码
            if (StrUtil.isEmpty(result) || !result.contains("#HPB#")) {
                showCreateWalletDilaog();
                return;
            }
            //当前页面弹出框，进行确认支付
            initfenxiDatds(result);
        } else if (mType == TYPE_DIGITAL_SIGN) {// 冷钱包数字签名
            Intent interDigitalSignSucceed = new Intent(this,DigitalSignSucceedActivity.class);
            interDigitalSignSucceed.putExtra(DigitalSignSucceedActivity.SIGN_CONTENT,result);
            startActivity(interDigitalSignSucceed);
            finish();
        }else if (mType == TYPE_TRANSFER) {// 冷钱包转账
            Intent interDigitalSignSucceed = new Intent(this,TransferActivity.class);
            interDigitalSignSucceed.putExtra(TransferActivity.RESULT_COLD_WALLET_TRANSFER,result);
            setResult(RESULT_OK,interDigitalSignSucceed);
            finish();
        }else if (mType == TYPE_VOTE_ZL){// 冷钱包投票治理
            Intent vateDetailIntent = new Intent(this,VoteAlDetailsActivity.class);
            vateDetailIntent.putExtra(VoteAlDetailsActivity.SIGN_CONTENT,result);
            setResult(RESULT_OK,vateDetailIntent);
            finish();
        }else if(mType == TYPE_VOTE){
            Intent vateDetailIntent = new Intent(this,VoteDetailsActivity.class);
            vateDetailIntent.putExtra(VoteDetailsActivity.SIGN_CONTENT,result);
            setResult(RESULT_OK,vateDetailIntent);
            finish();
        }else if(mType == TYPE_VOTE_V){
            Intent vateDetailIntent = new Intent(this,VoteDetailsActivity.class);
            vateDetailIntent.putExtra(VoteDetailsActivity.SIGN_CONTENT,result);
            setResult(RESULT_OK,vateDetailIntent);
            finish();
        }else if(mType == TYPE_NODE_DED){
            Intent vateDetailIntent = new Intent(this,SendDevidendActivity.class);
            vateDetailIntent.putExtra(SendDevidendActivity.SIGN_CONTENT,result);
            setResult(RESULT_OK,vateDetailIntent);
            finish();
        } else {
            //判断是否是正确的二维码
            if (StrUtil.isEmpty(result) || !result.startsWith("0x") || result.length() != 42) {
                showCreateWalletDilaog();
                return;
            }

            if (!Numeric.isValidAddress(result)) {
                showCreateWalletDilaog();
                return;
            }

            if (mType == 0) {
                Intent goto_transfer = new Intent(this, TransferActivity.class);
                goto_transfer.putExtra(TransferActivity.ADDRESS, result);
                startActivity(goto_transfer);
                finish();
            } else if (mType == 4) {
                Intent goto_transfer = new Intent(this, AddaddressActivity.class);
                goto_transfer.putExtra(RESULT_CONTENT, result);
                setResult(RESULT_CODE, goto_transfer);
                finish();
            } else if (mType == 5) {
                initHomeResult(result);
            } else {
                Intent goto_transfer = new Intent(this, TransferActivity.class);
                goto_transfer.putExtra(RESULT_CONTENT, result);
                setResult(RESULT_CODE, goto_transfer);
                finish();
            }
        }
    }

    private void initHomeResult(String result) {
//        View view = getLayoutInflater().inflate(R.layout.view_home_scan_dialog, null);
        mCommonScanResultDialog = new CommonScanResultDialog(this, mTxtTitle, walletType, result);
        mCommonScanResultDialog.setOnSubmitListener(new CommonScanResultDialog.OnSubmitListener() {
            @Override
            public void setOnSubmit(String address, int type) {
                switch (type) {
                    case 0://转账
                        Intent goto_transfer = new Intent(ScaningActivity.this, TransferActivity.class);
                        goto_transfer.putExtra(TransferActivity.ADDRESS, result);
                        goto_transfer.putExtra(TransferActivity.TRANSFER_TYPE, "");
                        startActivity(goto_transfer);
                        break;
                    case 1://新建联系人
                        Intent goto_bind = new Intent(ScaningActivity.this, AddaddressActivity.class);
                        goto_bind.putExtra(AddaddressActivity.ADD_ADDRESS, result);
                        startActivity(goto_bind);
                        break;
                    case 2://查询账户信息
                        Intent goto_webView = new Intent(ScaningActivity.this, CommonWebActivity.class);
                        goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.activity_scan_txt_01));
                        goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, DAppConstants.HPB_INFO_URL + result);
                        startActivity(goto_webView);
                        break;
                }
                finish();
            }

            @Override
            public void setOnCancle() {
                mQRCodeView.startSpot();
            }
        });
        mCommonScanResultDialog.show();
    }

    private void initfenxiDatds(String signResult) {
        String mStrSignHax, strFee;
        BigInteger gasPrice = new BigInteger("0");
        BigInteger gasLimit = new BigInteger("0");
        String money, fromAddress, toAddress;

        String[] arr = signResult.split("#HPB#"); // 用,分割
        gasPrice = new BigInteger(arr[0]);
        gasLimit = new BigInteger(arr[1]);
        money = "" + arr[2];
        fromAddress = arr[3];
        toAddress = arr[4];
        mStrSignHax = arr[5];
        strFee = SlinUtil.FeeValueString(this, gasPrice, gasLimit);
//        View view = getLayoutInflater().inflate(R.layout.view_transfer_comfirm_dialog, null);
        mCommonTransfgerConfirmDialog = new CommonTransfgerConfirmDialog(this, mTxtTitle);
        mCommonTransfgerConfirmDialog.setOnSubmitListener(new CommonTransfgerConfirmDialog.OnSubmitListener() {
            @Override
            public void setOnSubmitListener(String money) {
                showProgressDialog();
                //发送
                new CreateTradeRequest(mStrSignHax).doRequest(ScaningActivity.this, new CommonResultListener(ScaningActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        super.onSuccess(jsonArray);
                        dismissProgressDialog();
                        DappApplication.getInstance().showToast(getResources().getString(R.string.activity_main_map_txt_06));
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        mQRCodeView.startSpot();
                    }
                });
            }
        });
        mCommonTransfgerConfirmDialog.setOnDismissListener(new CommonTransfgerConfirmDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                mQRCodeView.startSpot();
            }
        });
        mCommonTransfgerConfirmDialog.show(money, strFee, toAddress, fromAddress, 0);
    }

    /**
     * 非法弹出框
     */
    private void showCreateWalletDilaog() {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(null, getResources().getString(R.string.dialog_common_qr_error_msg), null,
                null, null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mQRCodeView.startSpot();
                        dialogTool.dismiss();
                    }
                });
    }

    /**
     * 非法弹出框
     */
    private void showColdWalletDilaog() {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(null, getResources().getString(R.string.dialog_common_qr_error_msg_01), null,
                null, null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mQRCodeView.startSpot();
                        dialogTool.dismiss();
                    }
                });
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
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.permissions_txt_qrcode), REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        } else {
            mQRCodeView.startCamera();
            mQRCodeView.showScanRect();
        }
    }
    /* 设置权限结束   */
}
