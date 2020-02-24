package com.zhaoxi.Open_source_Android.libs.tools;

import android.os.AsyncTask;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;

import java.io.IOException;

/**
 * des:导出钱包
 * @author zhutt on 2018-06-13.
 */

public class ExportWalletAsyncTask extends AsyncTask<Void, Void, String> {
    private BaseActivity mActivity;
    private String mAddress;
    private String mPsd;
    private int mType;// 1、导出助记词  2、导出keystore 3.导出私钥
    private CreateDbWallet mCreateDbWallet;

    public interface OnResultExportListener{
        void setOnResultListener(String result);
    }

    private OnResultExportListener mOnResultListener;

    public void setOnResultListener(OnResultExportListener mOnResultListener) {
        this.mOnResultListener = mOnResultListener;
    }

    /**
     * @param activity
     * @param address
     * @param psd
     * @param type
     */
    public ExportWalletAsyncTask(BaseActivity activity, String address, String psd,int type) {
        mActivity = activity;
        mAddress = address;
        mPsd = psd;
        mType = type;
        mCreateDbWallet = new CreateDbWallet(mActivity);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = "";
        if (mType == 1) {
            message = mActivity.getResources().getString(R.string.dialog_progress_08);
        }else if(mType == 2){
            message = mActivity.getResources().getString(R.string.dialog_progress_07);
        }else if(mType == 3){
            message = mActivity.getResources().getString(R.string.dialog_progress_06);
        }else{
            message = mActivity.getResources().getString(R.string.dialog_progress_10);
        }
        mActivity.showTextProgressDialog(message);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = "";
        switch (mType){
            case 1:
                result =  mCreateDbWallet.exportWalletMnemonic(mActivity, mAddress, mPsd);
                break;
            case 2:
                try {
                    result = mCreateDbWallet.exportWalletKeyStore(mActivity,mAddress, mPsd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                result = mCreateDbWallet.exportWalletPrivateKey(mActivity,mAddress, mPsd);
                break;
            default:
                result = mCreateDbWallet.exportWalletPrivateKey(mActivity,mAddress, mPsd);
                break;
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        mActivity.dismissTextProgressDialog();
        mOnResultListener.setOnResultListener(result);
    }
}
