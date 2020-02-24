package com.zhaoxi.Open_source_Android.libs.tools;

import android.os.AsyncTask;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;
import com.zhaoxi.Open_source_Android.web3.crypto.Bip39WalletUtil;
import com.zhaoxi.Open_source_Android.web3.utils.AESUtil;

/**
 * des:修改密码 删除钱包
 * Created by ztt on 2018/6/6.
 */

public class ChangeWalletAsyncTask extends AsyncTask<Void, Void, Integer> {
    private BaseActivity mActivity;
    private String mAddress;
    private String mOldPsd;
    private String mNewPsd;
    private String mPsd;
    private int mImgId;
    private int mType;// 1、修改密码  2、删除钱包 3、删除冷钱包
    private CreateDbWallet mCreateDbWallet;

    public interface OnResultChangeListener {
        void setOnResultListener(int result);
    }

    private OnResultChangeListener mOnResultListener;

    public void setOnResultListener(OnResultChangeListener mOnResultListener) {
        this.mOnResultListener = mOnResultListener;
    }

    /**
     * @param activity
     * @param address
     * @param oldPsd
     * @param newPsd
     * @param type
     */
    public ChangeWalletAsyncTask(BaseActivity activity, String address, String oldPsd, String newPsd, int type) {
        mActivity = activity;
        mAddress = address;

        mOldPsd = oldPsd;
        mNewPsd = newPsd;
        mType = type;
        mCreateDbWallet = new CreateDbWallet(mActivity);
    }

    public ChangeWalletAsyncTask(BaseActivity activity, String address,int imgId, String psd, int type) {
        mActivity = activity;
        mAddress = address;
        mImgId = imgId;
        mPsd = psd;
        mType = type;
        mCreateDbWallet = new CreateDbWallet(mActivity);
    }

    public ChangeWalletAsyncTask(BaseActivity activity, String address,int imgId, int type){
        mActivity = activity;
        mAddress = address;
        mImgId = imgId;
        mType = type;
        mCreateDbWallet = new CreateDbWallet(mActivity);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = "";
        if (mType == 1) {
            message = mActivity.getResources().getString(R.string.dialog_progress_05);
        } else {
            message = mActivity.getResources().getString(R.string.dialog_progress_09);
        }
        mActivity.showTextProgressDialog(message);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int result = 0;
        if (mType == 1) {
            // 判断是否有助记词
            WalletBean  mWalletBean = mCreateDbWallet.queryWallet(mActivity,mAddress);
            if (!StrUtil.isEmpty(mWalletBean.getMnemonic())) {//存在助记词
                String mnemonic = AESUtil.decrypt(mOldPsd,mWalletBean.getMnemonic());
                //判断是否是正确的助记词
                Bip39WalletUtil bip39WalletUtil = new Bip39WalletUtil(mActivity);
                if(bip39WalletUtil.isMnemonic(mnemonic)){
                    result = mCreateDbWallet.updatePassword(mActivity, mAddress, mOldPsd, mNewPsd);
                }else{
                    result = 4;
                }
            }else{
                result = mCreateDbWallet.updatePassword(mActivity, mAddress, mOldPsd, mNewPsd);
            }
        } else if(mType == 2){
            try {
                result = mCreateDbWallet.deleteWallet(mActivity, mAddress,mImgId ,mPsd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                result = mCreateDbWallet.deleteColdWallet(mAddress,mImgId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        mActivity.dismissTextProgressDialog();
        mOnResultListener.setOnResultListener(result);
    }
}
