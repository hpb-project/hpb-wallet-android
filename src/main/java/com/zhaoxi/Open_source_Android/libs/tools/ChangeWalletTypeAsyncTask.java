package com.zhaoxi.Open_source_Android.libs.tools;

import android.content.Intent;
import android.os.AsyncTask;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.ui.activity.MainmapTwoActivity;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class ChangeWalletTypeAsyncTask extends AsyncTask<Void, Void, Integer> {
    private BaseActivity mActivity;
    private String mContent;
    private String mPsd;
    private int mType;// 1 keystore 2私钥
    private CreateDbWallet mCreateDbWallet;
    private boolean mUseFullScrypt = false;

    public ChangeWalletTypeAsyncTask(BaseActivity activity,String content,String psd,int type) {
        mActivity = activity;
        mContent = content;
        mPsd = psd;
        this.mType = type;
        mCreateDbWallet = new CreateDbWallet(mActivity.getApplicationContext());
    }

    public ChangeWalletTypeAsyncTask(BaseActivity activity,String content,String psd,int type,boolean useFullScrypt) {
        mActivity = activity;
        mContent = content;
        mPsd = psd;
        this.mType = type;
        mUseFullScrypt = useFullScrypt;
        mCreateDbWallet = new CreateDbWallet(mActivity.getApplicationContext());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = mActivity.getResources().getString(R.string.txt_dialog_txt_01);
        mActivity.showTextProgressDialog(message);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int result;
        String address;
        if(mType == 1){
            //修改walletType值 修改为1
            address = mCreateDbWallet.getKeystroeAddress(mActivity,mContent,mPsd,mUseFullScrypt);
        }else{
            //修改walletType值 修改为1
            address = mCreateDbWallet.getprivateKeyAddress(mActivity,mContent,mPsd);
        }
        if(address.contains("失败") || address.contains("failed")){
            result =0;
        }else{
            result = mCreateDbWallet.updateWalletType(address,"1");
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        mActivity.dismissTextProgressDialog();
        if(result != 0){
            DappApplication.getInstance().showToast(mActivity.getResources().getString(R.string.main_map_toast_change_type_error));
        }else{
            Intent it_map = new Intent(mActivity, MainmapTwoActivity.class);
            mActivity.startActivity(it_map);
            mActivity.finish();
        }
    }
}
