package com.zhaoxi.Open_source_Android.libs.tools;

import android.content.Intent;
import android.os.AsyncTask;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.ui.activity.CreateWalletSucceseActivity;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;
import com.zhaoxi.Open_source_Android.web3.utils.AESUtil;

/**
 * des:创建钱包
 * Created by ztt on 2018/6/6.
 */

public class CreateWalletAsyncTask extends AsyncTask<Void, Void, Integer> {
    private BaseActivity mActivity;
    private String mWalletName;
    private String mPsd;
    private String mPrompt;

    private CreateDbWallet mCreateDbWallet;

    public CreateWalletAsyncTask(BaseActivity activity, String walletName, String psd, String prompt) {
        mActivity = activity;
        mWalletName = walletName;
        mPrompt = prompt;
        mPsd = psd;
        mCreateDbWallet = new CreateDbWallet(mActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mActivity.showTextProgressDialog(mActivity.getResources().getString(R.string.dialog_progress_01));
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        boolean isOver = mCreateDbWallet.isOverWalletNum();
        if (isOver) {
            return 3;
        } else {
            int result = mCreateDbWallet.exportBip39Wallet(mActivity, mWalletName, mPsd, mPrompt);
            return result;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {

        switch (result) {
            case 0://创建成功
                WalletBean walletBean = mCreateDbWallet.queryNewWallet();
                SharedPreferencesUtil.setSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS, walletBean.getAddress());

                DappApplication.getInstance().showToast(mActivity.getResources().getString(R.string.create_wallet_submit_succese));
                //跳转成功页面
                Intent it_gosuccese = new Intent(mActivity, CreateWalletSucceseActivity.class);
                it_gosuccese.putExtra(CreateWalletSucceseActivity.WALLET_VALUE, AESUtil.decrypt(mPsd, walletBean.getMnemonic()));
                it_gosuccese.putExtra(CreateWalletSucceseActivity.WALLET_ADDRESS, walletBean.getAddress());
                mActivity.startActivity(it_gosuccese);
                mActivity.finish();

                break;
            case 1:
                mActivity.dismissTextProgressDialog();
                //数据库已存在
                DappApplication.getInstance().showToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_faile_2));
                break;
            case 2:
                mActivity.dismissTextProgressDialog();
                //创建钱包失败
                DappApplication.getInstance().showToast(mActivity.getResources().getString(R.string.create_wallet_submit_faile));
                break;
            case 3:
                mActivity.dismissTextProgressDialog();
                //创建钱包失败
                DappApplication.getInstance().showToast(mActivity.getResources().getString(R.string.create_wallet_submit_faile_03));
                break;
        }
    }
}
