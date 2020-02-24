package com.zhaoxi.Open_source_Android.libs.tools;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.ui.activity.ImportWalletActivity;
import com.zhaoxi.Open_source_Android.ui.activity.ImportWalletTwoActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TransferActivity;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;

import static com.zhaoxi.Open_source_Android.ui.activity.ScaningActivity.RESULT_CODE;
import static com.zhaoxi.Open_source_Android.ui.activity.ScaningActivity.RESULT_CONTENT;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class ScanQrAsyncTask extends AsyncTask<Void, Void, String> {
    private BaseActivity mActivity;
    private String picturePath = "";
    private int mType;
    private QRCodeView mQRCodeView;

    public ScanQrAsyncTask(BaseActivity activity, String picturePath, int type, QRCodeView qrCodeView) {
        mActivity = activity;
        this.picturePath = picturePath;
        this.mType =type;
        this.mQRCodeView = qrCodeView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mActivity.showProgressDialog();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return QRCodeDecoder.syncDecodeQRCode(picturePath);
    }

    @Override
    protected void onPostExecute(String result) {
        mActivity.dismissProgressDialog();
        if (TextUtils.isEmpty(result)) {
            DappApplication.getInstance().showToast(mActivity.getResources().getString(R.string.scan_activity_txt_no_find_qr));
            mQRCodeView.startSpot();
        } else {
            //判断地址是否正确
            gotoTransfer(result);
        }
    }

    /**
     * 跳转到转账页面
     * @param result
     */
    private void gotoTransfer(String result){
        if(mType == 1){
            Intent goto_import = new Intent(mActivity,ImportWalletActivity.class);
            goto_import.putExtra(RESULT_CONTENT,result);
            mActivity.setResult(RESULT_CODE,goto_import);
            mActivity.finish();
        }else if(mType == 10){
            Intent goto_import = new Intent(mActivity,ImportWalletTwoActivity.class);
            goto_import.putExtra(RESULT_CONTENT,result);
            mActivity.setResult(RESULT_CODE,goto_import);
            mActivity.finish();
        }else if(mType == 0){
            Intent goto_transfer = new Intent(mActivity,TransferActivity.class);
            goto_transfer.putExtra(TransferActivity.ADDRESS,result);
            mActivity.startActivity(goto_transfer);
            mActivity.finish();
        }else{
            Intent goto_transfer = new Intent(mActivity,TransferActivity.class);
            goto_transfer.putExtra(RESULT_CONTENT,result);
            mActivity.setResult(RESULT_CODE,goto_transfer);
            mActivity.finish();
        }
    }
}
