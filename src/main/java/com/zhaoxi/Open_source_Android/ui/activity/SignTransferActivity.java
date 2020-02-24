package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonTransfgerConfirmDialog;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.CreateTradeRequest;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignTransferActivity extends BaseTitleBarActivity {
    public static final String SIGNHAX = "SignTransferActivity.hax";

    @BindView(R.id.transfer_activity_edit_form_address)
    TextView mTxtFormAddress;
    @BindView(R.id.transfer_activity_edit_to_address)
    TextView mTxtToAddress;
    @BindView(R.id.transfer_activity_edit_money)
    TextView mTxtMoney;
    @BindView(R.id.transfer_activity_fee_des)
    TextView mTxtFeeDes;
    @BindView(R.id.transfer_activity_fee_money)
    TextView mTxtFeeMoney;

    private CommonTransfgerConfirmDialog mCommonTransfgerConfirmDialog;
    private String mSignResult, mStrSignHax;
    private BigInteger mGasPrice = new BigInteger("0");
    private BigInteger mGasLimit = new BigInteger("0");
    private String mMoney, mFromAddress, mToAddress;
    private int numStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_transfer);
        ButterKnife.bind(this);
        numStyle = SharedPreferencesUtil.getSharePreInt(this,SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        mSignResult = getIntent().getStringExtra(SIGNHAX);
        initViews();
        initDatas();
    }

    private void initViews() {
        setTitle(R.string.main_home_top_txt_03,true);

        //解析结果数据
        String[] arr = mSignResult.split("#HPB#"); // 用,分割
        mGasPrice = new BigInteger(arr[0]);
        mGasLimit = new BigInteger(arr[1]);
        mMoney = "" + arr[2];
        mFromAddress = arr[3];
        mToAddress = arr[4];
        mStrSignHax = arr[5];
//        View view = getLayoutInflater().inflate(R.layout.view_choose_wallet_dialog, null);
        mCommonTransfgerConfirmDialog = new CommonTransfgerConfirmDialog(this, mTxtTitle);
        mCommonTransfgerConfirmDialog.setOnSubmitListener(new CommonTransfgerConfirmDialog.OnSubmitListener() {
            @Override
            public void setOnSubmitListener(String money) {
                showProgressDialog();
                //发送
                new CreateTradeRequest(mStrSignHax).doRequest(SignTransferActivity.this, new CommonResultListener(SignTransferActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        super.onSuccess(jsonArray);
                        dismissProgressDialog();
                        DappApplication.getInstance().showToast(getResources().getString(R.string.activity_main_map_txt_06));
                        finish();
                    }
                });
            }
        });
    }

    private void initDatas() {
        mTxtMoney.setText(SlinUtil.NumberFormat8(this,new BigDecimal(mMoney)));
        //交易费用
        String strFee = SlinUtil.FeeValueString(this, mGasPrice, mGasLimit);
        mTxtFeeMoney.setText(SlinUtil.tailClearAll(this,strFee) + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_01));
        
        mTxtFormAddress.setText(mFromAddress);
        mTxtToAddress.setText(mToAddress);
    }

    @OnClick({R.id.transfer_activity_fee_des, R.id.transfer_activity_btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.transfer_activity_fee_des:
                Intent goto_webView = new Intent(this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.transfer_activity_txt_10));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL+ DAppConstants.backUrlHou(this, 9));
                startActivity(goto_webView);
                break;
            case R.id.transfer_activity_btn_submit:
                //显示详情
                mCommonTransfgerConfirmDialog.show(mMoney, mToAddress,mFromAddress,numStyle);
                break;
        }
    }
}
