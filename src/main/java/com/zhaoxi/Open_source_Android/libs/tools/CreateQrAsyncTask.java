package com.zhaoxi.Open_source_Android.libs.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class CreateQrAsyncTask extends AsyncTask<Void, Void, Bitmap> {
    private BaseActivity mActivity;
    private ImageView mImgQr;
    private String address = "";
    private Bitmap mLogo;

    public CreateQrAsyncTask(BaseActivity activity, ImageView imageView,String address) {
        mActivity = activity;
        mImgQr = imageView;
        this.address = address;
    }

    public CreateQrAsyncTask(BaseActivity activity, ImageView imageView,String address,Bitmap logo) {
        mActivity = activity;
        mImgQr = imageView;
        this.address = address;
        this.mLogo = logo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mActivity.showProgressDialog();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        if(mLogo != null){
           return  QRCodeEncoder.syncEncodeQRCode(address,BGAQRCodeUtil.dp2px(mActivity, 280), Color.BLACK,mLogo);
        }
        return QRCodeEncoder.syncEncodeQRCode(address, BGAQRCodeUtil.dp2px(mActivity, 280));
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mActivity.dismissProgressDialog();
        if (bitmap != null) {
            mImgQr.setImageBitmap(bitmap);
            if(mOnResultListener != null){
                mOnResultListener.setOnResultListener(bitmap);
            }
        } else {
            DappApplication.getInstance().showToast(mActivity.getResources().getString(R.string.txt_qr_create_faile));
        }
    }

    public interface OnResultListener {
        void setOnResultListener(Bitmap bitmap);
    }

    private OnResultListener mOnResultListener;

    public void setOnResultListener(OnResultListener mOnResultListener) {
        this.mOnResultListener = mOnResultListener;
    }

}
