package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonMapConfirmDialog;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPsdPop;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.Number0TextWatcher;
import com.zhaoxi.Open_source_Android.libs.tools.Number1TextWatcher;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.CreateEthTradeRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetEthNonceRequest;
import com.zhaoxi.Open_source_Android.net.Request.TotalEthAccountRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetEthFeeRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 映射交易
 *
 * @author zhutt
 */
public class MapTransferActivity extends BaseTitleBarActivity {
    public static final String RESOUCE_ADDRESS = "TransationRecordActivity.RESOUCE_ADDRESS";

    @BindView(R.id.map_transfer_activity_to_address)
    TextView mTxtToAddress;
    @BindView(R.id.map_transfer_activity_edit_money)
    EditText mEditMoney;
    @BindView(R.id.map_transfer_activity_shenyu_money)
    TextView mTxtShenyuMoney;
    @BindView(R.id.map_transfer_activity_btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.map_transfer_activity_img_delete)
    ImageView mImgDelete;
    @BindView(R.id.map_transfer_activity_fee_money)
    TextView mTxtFeeMoney;
    @BindView(R.id.map_transfer_activity_from_address)
    TextView mTxtFromAddress;
    @BindView(R.id.root_map_transfer)
    LinearLayout mRootView;

    private String fromAddress;
    private String toAddress = DAppConstants.MAIN_MAP_TO_ADDRESS;
    private BigDecimal mCurAllMoney = new BigDecimal(0), mCurEthMoney = new BigDecimal(0);

    private CommonMapConfirmDialog mCommonTransfgerConfirmDialog;
    private BigInteger mGasPrice;
    private BigInteger mGasLimit;
    private BigDecimal mFeePrice;
    private int numStyle;
    private String digits01 = "0123456789.";
    private String digits02 = "0123456789,";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_transfer);
        ButterKnife.bind(this);
        numStyle = SharedPreferencesUtil.getSharePreInt(this,SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        fromAddress = getIntent().getStringExtra(RESOUCE_ADDRESS);
        if (!StrUtil.isEmpty(fromAddress)) {
            mTxtFromAddress.setText(fromAddress);
        }

        setTitle(R.string.activity_main_map_txt_05,true);
        mTxtToAddress.setText(toAddress);
        initData();
        if(numStyle == 0){
            mEditMoney.setKeyListener(DigitsKeyListener.getInstance(digits01));
            mEditMoney.addTextChangedListener(new Number0TextWatcher(this,mBtnSubmit,mEditMoney));
        }else{
            mEditMoney.setKeyListener(DigitsKeyListener.getInstance(digits02));
            mEditMoney.addTextChangedListener(new Number1TextWatcher(this,mBtnSubmit,mEditMoney));
        }

        mEditMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mImgDelete.setVisibility(View.VISIBLE);
                } else {
                    mImgDelete.setVisibility(View.INVISIBLE);
                }
            }
        });

//        View view = getLayoutInflater().inflate(R.layout.view_choose_wallet_dialog, null);
        mCommonTransfgerConfirmDialog = new CommonMapConfirmDialog(this, mTxtTitle);
        mCommonTransfgerConfirmDialog.setOnSubmitListener(new CommonMapConfirmDialog.OnSubmitListener() {
            @Override
            public void setOnSubmitListener(String money) {
                showDialog(money);
            }
        });
    }
    private void initData() {
        //获取金额
        new TotalEthAccountRequest(fromAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    MapEthBean bannerInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                    BigDecimal hpbMoney = new BigDecimal("" + bannerInfo.getHpbToken());
                    mCurEthMoney = bannerInfo.getEthBalance();
                    mCurAllMoney = Convert.fromWei(hpbMoney, Convert.Unit.ETHER);
                    mTxtShenyuMoney.setText("" + SlinUtil.formatNumFromWeiS(MapTransferActivity.this, hpbMoney));
                }
            }

            @Override
            public void onError(String error) {
                DappApplication.getInstance().showToast(error);
            }
        });

        //获取gasLimit gasPrice
        new GetEthFeeRequest(fromAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                //最大交易次数
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                //交易费用
                BigDecimal fee = Convert.fromWei(new BigDecimal(mGasLimit.multiply(mGasPrice)), Convert.Unit.ETHER);
                mFeePrice = fee;

                String strFee = SlinUtil.FeeValueString(MapTransferActivity.this, mGasPrice, mGasLimit);
                mTxtFeeMoney.setText(SlinUtil.tailClearAll(MapTransferActivity.this,strFee) + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_03));
            }
        });

    }

    @OnClick({R.id.map_transfer_activity_img_delete, R.id.map_transfer_activity_btn_submit,
            R.id.map_transfer_activity_txt_all, R.id.map_transfer_activity_fee_des})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_transfer_activity_img_delete:
                mEditMoney.setText("");
                break;
            case R.id.map_transfer_activity_btn_submit:
                submitTrade();
                break;
            case R.id.map_transfer_activity_txt_all:
                BigDecimal money1 = Convert.toWei(mCurAllMoney, Convert.Unit.ETHER);
                String money = SlinUtil.formatNumFromWeiS(this, money1);
                mEditMoney.setText(money);
                mEditMoney.setSelection(money.length());
                break;
            case R.id.map_transfer_activity_fee_des://费用说明
                Intent goto_webView = new Intent(this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.transfer_activity_txt_10));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + DAppConstants.backUrlHou(this, 9));
                startActivity(goto_webView);
                break;
        }
    }

    /**
     * 计算转账金额是否小于剩余金额
     *
     * @return
     */
    private boolean MoneyHandle() {
        String count = mEditMoney.getText().toString();
        BigDecimal money;
        if(numStyle == 1){
            if(count.contains(",")){
                String m1 = count.replace(".", ",");
                StringBuilder sb = new StringBuilder(m1);
                int index = m1.lastIndexOf(",");
                count = (sb.replace(index, index+1, ".")).toString();
            }
        }
        money = new BigDecimal(count.replace(",", ""));
        BigDecimal money1 = Convert.toWei(money, Convert.Unit.ETHER);
        if (money1.compareTo(Convert.toWei(mCurAllMoney, Convert.Unit.ETHER)) > 0) {
            return false;
        }
        return true;
    }

    private void submitTrade() {
        String fee = mTxtFeeMoney.getText().toString();
        if (StrUtil.isEmpty(fee)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_06));
            return;
        }

        //判断金额
        String money = mEditMoney.getText().toString();
        if (StrUtil.isEmpty(money)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_03));
            return;
        }

        if (money.equals("0")) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_04));
            return;
        }

        if(numStyle == 0){
            if (money.contains(".")) {
                String mm = money.replace(",", "");
                if (new BigDecimal(mm.replace(".", "")).compareTo(new BigDecimal(0)) == 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_04));
                    return;
                }
            }
        }else{
            if (money.contains(",")) {
                String mm = money.replace(".", "");
                if (new BigDecimal(mm.replace(",", "")).compareTo(new BigDecimal(0)) == 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_04));
                    return;
                }
            }
        }

        if (!MoneyHandle()) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_05));
            return;
        }

        if (mCurEthMoney.subtract(mFeePrice).compareTo(new BigDecimal("0")) < 0) {
            DappApplication.getInstance().showToast("交易费用不足，无法交易");
            return;
        }

        //显示详情
        mCommonTransfgerConfirmDialog.show(money, toAddress, fromAddress, mTxtFeeMoney.getText().toString(),numStyle);
    }

    /**
     * 获取nonce
     *
     * @param privateKey
     * @param money
     */
    private void getNonce(String privateKey, String money) {
        showProgressDialog();
        new GetEthNonceRequest(fromAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                String status = (String) jsonArray.get(0);
                BigInteger nonce = null;
                if ("000000".equals(status)) {//代表处理成功
                    nonce = new BigInteger("" + jsonArray.get(2));
                }
                BigDecimal money01 = null;
                if(numStyle == 0){
                    money01 = new BigDecimal(money.replace(",", ""));
                }else{
                    String money00 = "";
                    if(money.contains(",")){
                        String m1 = money.replace(".", ",");
                        StringBuilder sb = new StringBuilder(m1);
                        int index = m1.lastIndexOf(",");
                        money00 = (sb.replace(index, index+1, ".")).toString();
                    }else{
                        money00 = money;
                    }
                    money01 = new BigDecimal(money00.replace(",", ""));
                }
                handleSignTransaction(toAddress, money01, privateKey, nonce);
            }
        });
    }

    /**
     * 签名交易
     *
     * @param toAddress  交易-to地址
     * @param value      交易金额
     * @param privateKey 私钥
     * @param nonce
     */
    private void handleSignTransaction(String toAddress, BigDecimal value, String privateKey, BigInteger nonce) {
        //交易金额
        BigDecimal valueData = Convert.toWei(value, Convert.Unit.ETHER);
        //私钥
        String signedData = "";
        try {
            signedData = TransferUtils.tokenTransaction(nonce, mGasPrice, mGasLimit, privateKey,
                    DAppConstants.MAIN_MAP_TOKEN_ADDRESS, toAddress.toLowerCase(), valueData);
            new CreateEthTradeRequest(signedData).doRequest(this, new CommonResultListener(this) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    super.onSuccess(jsonArray);
                    dismissProgressDialog();
                    DappApplication.getInstance().showToast(getResources().getString(R.string.main_map_txt_transfer_send_succese));
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出密码输入框
     *
     * @param money
     */
    private void showDialog(String money) {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this,mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                //获取私钥
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(MapTransferActivity.this, fromAddress, psd, 10);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                        } else {
                            //获取nonce
                            getNonce(result, money);
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        commonPsdPop.show(getResources().getString(R.string.transfer_activity_dialog_txt_07));
    }
}
