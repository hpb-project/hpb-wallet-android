package com.zhaoxi.Open_source_Android.libs.tools;

import android.os.AsyncTask;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.NodeDividenBean;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:导出节点分红
 * Created by ztt on 2018/6/6.
 */

public class CreateTxtAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private BaseActivity mActivity;
    private List<NodeDividenBean.VoteDetailsBean> mData;
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
    public CreateTxtAsyncTask(BaseActivity activity, List<NodeDividenBean.VoteDetailsBean> data, String fileName) {
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
                .append("                                "+mActivity.getResources().getString(R.string.activity_cion_fh_txt_21_07)+"                                           ")
                .append("      "+mActivity.getResources().getString(R.string.activity_cion_fh_txt_21_08)+"\r\n");
        NodeDividenBean.VoteDetailsBean bean;
        for (int i = 0; i < mData.size(); i++) {
            bean = mData.get(i);
            str.append(""+(i+1)+"            ");
            str.append(bean.getAddress()+"        ");
            BigDecimal poll = Convert.fromWei(new BigDecimal(bean.getVoteNum()), Convert.Unit.ETHER);
            str.append(SlinUtil.NumberFormat0(mActivity, SlinUtil.ValueScale(poll,0))+"\n");
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
