package com.zhaoxi.Open_source_Android.libs.tools;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class ExportKeysotreQrAsyncTask extends AsyncTask<Void, Void, Bitmap> {
    private BaseActivity mActivity;
    private ImageView mImgQr;
    private LinearLayout mLayout;
    private String address = "";

    public ExportKeysotreQrAsyncTask(BaseActivity activity, ImageView imageView, LinearLayout layout,String address) {
        mActivity = activity;
        mImgQr = imageView;
        mLayout = layout;
        this.address = address;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mActivity.showProgressDialog();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        return QRCodeEncoder.syncEncodeQRCode(address, BGAQRCodeUtil.dp2px(mActivity, 280));
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mActivity.dismissProgressDialog();
        if (bitmap != null) {
            mImgQr.setImageBitmap(bitmap);
            mLayout.setVisibility(View.GONE);
            mImgQr.setVisibility(View.VISIBLE);
        } else {
            DappApplication.getInstance().showToast(mActivity.getResources().getString(R.string.txt_qr_create_faile));
        }
    }
}
