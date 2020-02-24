package com.zhaoxi.Open_source_Android.libs.tools;

import android.os.AsyncTask;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.DividendRecordsBean;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:导出节点分红
 * Created by ztt on 2018/6/6.
 */

public class CreateTxtRecordDetailsAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private BaseActivity mActivity;
    private List<DividendRecordsBean.RecordsBean> mData;
    private String mFileName;

    public interface OnResultExportListener{
        void setOnResultListener(boolean result);
    }

    private OnResultExportListener mOnResultListener;

    public void setOnResultListener(OnResultExportListener mOnResultListener) {
        this.mOnResultListener = mOnResultListener;
    }

    /**
     * @param activity
     */
    public CreateTxtRecordDetailsAsyncTask(BaseActivity activity, List<DividendRecordsBean.RecordsBean> data, String fileName) {
        mActivity = activity;
        mData = data;
        mFileName = fileName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = mActivity.getResources().getString(R.string.activity_cion_fh_txt_22_01);
        mActivity.showTextProgressDialog(message);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        StringBuffer str = new StringBuffer();
        str.append(mActivity.getResources().getString(R.string.activity_cion_fh_txt_21_06)+"  ")
                .append("                                "+mActivity.getResources().getString(R.string.activity_cion_fh_txt_21_07)+"                                ")
                .append("          "+mActivity.getResources().getString(R.string.activity_cion_fh_txt_19_01)+"      ")
                .append("    "+mActivity.getResources().getString(R.string.activity_cion_fh_txt_22_02)+"\r\n");
        DividendRecordsBean.RecordsBean bean;
        for (int i = 0; i < mData.size(); i++) {
            bean = mData.get(i);
            str.append((i+1)+"      ");
            str.append("  "+bean.getVoterAddr()+"  ");
            BigDecimal poll = Convert.fromWei(new BigDecimal(bean.getScore()), Convert.Unit.ETHER);
            str.append("      "+ SlinUtil.tailClearAll(mActivity,SlinUtil.NumberFormat8(mActivity, poll))+"            ");
            BigDecimal money = Convert.fromWei(new BigDecimal(bean.getBonus()), Convert.Unit.ETHER);
            str.append("  "+SlinUtil.tailClearAll(mActivity,SlinUtil.NumberFormat8(mActivity, money))+"\n");
        }
        String path = FileManager.createPic(DAppConstants.PATH_EXPROT_FILE);
        boolean isSuccese = FileManager.writeTxtToFile(str.toString(), path, mFileName);
        return isSuccese;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mActivity.dismissTextProgressDialog();
        mOnResultListener.setOnResultListener(result);
    }
}
