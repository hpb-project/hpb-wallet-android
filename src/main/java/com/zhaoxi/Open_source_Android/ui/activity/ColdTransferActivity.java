package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.tools.CreateQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.GetHpbFeeRequest;
import com.zhaoxi.Open_source_Android.net.Request.TotalHpbRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ColdTransferActivity extends BaseTitleBarActivity {

    @BindView(R.id.cold_wallet_info_img_qr)
    ImageView mImgQr;
    @BindView(R.id.cold_info_img_status)
    ImageView mImgStatus;
    @BindView(R.id.cold_info_txt_formaddress)
    TextView mTxtFormaddress;
    @BindView(R.id.cold_info_txt_nonce)
    TextView mTxtNonce;
    @BindView(R.id.cold_info_txt_gaslimit)
    TextView mTxtGaslimit;
    @BindView(R.id.cold_info_txt_gasprice)
    TextView mTxtGasprice;
    @BindView(R.id.cold_info_layout_base)
    LinearLayout mLayoutColdBase;
    @BindView(R.id.cold_info_txt_money)
    TextView mTxtMoney;

    private String mCAddress;
    private BigInteger mCNonce;
    private String mCGasLimit, mCGasPrice;
    private String mCMoney;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cold_transfer);
        ButterKnife.bind(this);
        mCAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        setTitle(R.string.cold_wallet_txt_1,true);
        mImgStatus.setBackgroundResource(R.mipmap.icon_cold_close);
        initDatas();
    }

    @OnClick({R.id.cold_info_layout_status, R.id.cold_transfer_activity_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cold_info_layout_status:
                if (!isOpen) {//展开
                    mImgStatus.setBackgroundResource(R.mipmap.icon_cold_open);
                    mLayoutColdBase.setVisibility(View.VISIBLE);
                    isOpen = true;
                } else {//收起
                    mImgStatus.setBackgroundResource(R.mipmap.icon_cold_close);
                    mLayoutColdBase.setVisibility(View.GONE);
                    isOpen = false;
                }
                break;
            case R.id.cold_transfer_activity_btn_next:
                Intent it_gotoScan = new Intent(this, ScaningActivity.class);
                it_gotoScan.putExtra(ScaningActivity.RESURE_TYPE, 3);
                startActivity(it_gotoScan);
                break;
        }
    }

    private void initDatas() {
        new TotalHpbRequest(mCAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                BigDecimal money = new BigDecimal("" + jsonArray.get(2));
                mCMoney = String.valueOf(money);
                mTxtMoney.setText(SlinUtil.formatNumFromWeiS(ColdTransferActivity.this, money)+" "+getResources().getString(R.string.wallet_manager_txt_money_unit_01));
                //获取gasLimit gasPrice
                new GetHpbFeeRequest(mCAddress).doRequest(ColdTransferActivity.this, new CommonResultListener(ColdTransferActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        super.onSuccess(jsonArray);
                        MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                        mCNonce = mapEthInfo.getNonce();
                        mCGasPrice = mapEthInfo.getGasPrice();
                        mCGasLimit = mapEthInfo.getGasLimit();
                        mTxtNonce.setText("" + mCNonce);
                        mTxtGaslimit.setText(mCGasLimit);
                        mTxtGasprice.setText(mCGasPrice);
                        mTxtFormaddress.setText(mCAddress);

                        if(StrUtil.isEmpty(mCMoney)){
                            mCMoney = "0";
                        }

                        String qrBaseResult = mCAddress + "#HPB#" + mCMoney + "#HPB#" + mCNonce + "#HPB#" + mCGasLimit + "#HPB#" + mCGasPrice;
                        if (StrUtil.isEmpty(mCGasLimit) || StrUtil.isEmpty(mCGasPrice)) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.cold_wallet_txt_10));
                            return;
                        }
                        CreateQrAsyncTask asyncTask = new CreateQrAsyncTask(ColdTransferActivity.this, mImgQr, qrBaseResult);
                        asyncTask.execute();//开始执行
                    }
                });
            }
        });
    }
}
