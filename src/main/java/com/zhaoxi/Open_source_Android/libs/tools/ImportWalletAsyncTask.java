package com.zhaoxi.Open_source_Android.libs.tools;

import android.os.AsyncTask;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

/**
 * des:导入钱包
 * Created by ztt on 2018/6/6.
 */

public class ImportWalletAsyncTask extends AsyncTask<Void, Void, Integer> {
    private BaseActivity mActivity;
    private String mWalletName;
    private String mPsd;
    private String mPrompt;
    private String mContent;
    private int mType;// 1、导入助记词  2、导入keystore 3.导入私钥 4.导入冷钱包
    private String mWalletType = "0";// 0、普通钱包 1、映射钱包
    private boolean mUseFullScrypt = false;//判断钱包类型
    private CreateDbWallet mCreateDbWallet;
    private String mAddress;

    public interface OnResultListener {
        void setOnResultListener(int result);
    }

    private OnResultListener mOnResultListener;

    public void setOnResultListener(OnResultListener mOnResultListener) {
        this.mOnResultListener = mOnResultListener;
    }

    /**
     * @param activity
     * @param walletName
     * @param psd
     * @param content
     * @param prompt
     * @param type
     */
    public ImportWalletAsyncTask(BaseActivity activity, String walletName, String psd, String content,
                                 String prompt, int type,int walletType) {
        mActivity = activity;
        mWalletName = walletName;
        mPrompt = prompt;
        mPsd = psd;
        mContent = content;
        mType = type;
        mCreateDbWallet = new CreateDbWallet(mActivity.getApplicationContext());
        mWalletType = String.valueOf(walletType);
    }

    public ImportWalletAsyncTask(BaseActivity activity, String walletName, String psd, String content,
                                 String prompt, int type,int walletType,boolean useFullScrypt) {
        mActivity = activity;
        mWalletName = walletName;
        mPrompt = prompt;
        mPsd = psd;
        mContent = content;
        mType = type;
        mWalletType = String.valueOf(walletType);
        mUseFullScrypt = useFullScrypt;
        mCreateDbWallet = new CreateDbWallet(mActivity.getApplicationContext());
    }

    public ImportWalletAsyncTask(BaseActivity activity, String walletName,String address,int type){
        mActivity = activity;
        mWalletName = walletName;
        mAddress = address;
        mType = type;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = "";
        switch (mType){
            case 1:
                message = mActivity.getResources().getString(R.string.dialog_progress_04);
                break;
            case 2:
                message = mActivity.getResources().getString(R.string.dialog_progress_03);
                break;
            case 3:
                message = mActivity.getResources().getString(R.string.dialog_progress_02);
                break;
            case 4:
                message = mActivity.getResources().getString(R.string.dialog_progress_11);
                break;
        }
        mActivity.showTextProgressDialog(message);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int result = 0;
        boolean isOver = mCreateDbWallet.isOverWalletNum();
        if (isOver) {
            return 4;
        }
        switch (mType){
            case 1:
                result = mCreateDbWallet.importWalletMnemonic(mActivity, mWalletName, mPsd, mContent, mPrompt);
                break;
            case 2:
                result = mCreateDbWallet.importWalletKeyStore(mActivity, mWalletName, mContent, mPsd,mWalletType,mUseFullScrypt);
                break;
            case 3:
                result = mCreateDbWallet.importWalletPrivateKey(mActivity, mWalletName, mContent, mPsd, mPrompt,mWalletType);
                break;
            case 4:
                result = mCreateDbWallet.importWalletCold(mWalletName,mAddress);
                break;
        }

        if (result == 0) {
            WalletBean walletBean = mCreateDbWallet.queryNewWallet();
            SharedPreferencesUtil.setSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS, walletBean.getAddress());
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        mActivity.dismissTextProgressDialog();
        mOnResultListener.setOnResultListener(result);
    }
}
