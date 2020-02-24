package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.zhaoxi.Open_source_Android.common.dialog.CommonPsdPop;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.SendRed0TextWatcher;
import com.zhaoxi.Open_source_Android.libs.tools.SendRed1TextWatcher;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetHpbFeeRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetNonceRequest;
import com.zhaoxi.Open_source_Android.net.Request.RawReadyRequest;
import com.zhaoxi.Open_source_Android.net.Request.RedFalshRequest;
import com.zhaoxi.Open_source_Android.net.Request.SendRedRequest;
import com.zhaoxi.Open_source_Android.net.Request.TotalHpbRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.net.bean.RedBaseBean;
import com.zhaoxi.Open_source_Android.ui.dialog.RedSendWaitingDialog;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendRedPacketsActivity extends BaseTitleBarActivity {

    @BindView(R.id.send_red_packets_txt_all_money)
    TextView mTxtAllMoney;
    @BindView(R.id.send_red_packets_edit_input_money)
    EditText mEditMoney;
    @BindView(R.id.send_red_packets_txt_change_red_type)
    TextView mTxtChangeRedType;
    @BindView(R.id.send_red_packets_edit_num_red)
    EditText mEditNumRed;
    @BindView(R.id.send_red_packets_txt_fee)
    TextView mTxtFeeMoney;
    @BindView(R.id.send_red_packets_edit_reddes)
    EditText mEditRedDes;
    @BindView(R.id.send_red_packets_txt_des_num)
    TextView mTxtDesNum;
    @BindView(R.id.send_red_packets_txt_all_money_show)
    TextView mTxtAllMoneyShow;
    @BindView(R.id.send_red_packets_btn_submit_red)
    Button mBtnSubmitRed;
    @BindView(R.id.send_red_packets_all)
    TextView mTxtRedType;
    @BindView(R.id.send_red_packets_redpackets)
    ImageView mImgRedType;
    @BindView(R.id.send_red_packets_txt_change_red_type_des)
    TextView mTxtRedTypeDes;
    @BindView(R.id.root_send_redpackets)
    LinearLayout mRootView;

    private int mRedType = 2;//拼2；普1
    private BigDecimal mCurAllMoney = new BigDecimal(0);
    private String mDefultAddress = "";
    private BigInteger mGasPrice = new BigInteger("0");
    private BigInteger mGasLimit = new BigInteger("0");
    private int numStyle;
    private String digits01 = "0123456789.";
    private String digits02 = "0123456789,";

    private SendRed0TextWatcher mSendRed0TextWatcher;
    private SendRed1TextWatcher mSendRed1TextWatcher;
    private RedSendWaitingDialog mRedSendWaitingDialog;
    private int mLanguageType;
    private String mRedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_red_packets);
        ButterKnife.bind(this);
        setTitle(R.string.main_send_redpackets_txt, true);
        showRightTxtWithTextListener(getResources().getString(R.string.main_send_redpackets_txt_right), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendRedPacketsActivity.this, RedRecordsActivity.class));
            }
        });
        mLanguageType = ChangeLanguageUtil.languageType(this);
        numStyle = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        initData();
        mEditMoney.requestFocus();
        if (numStyle == 0) {
            mEditMoney.setKeyListener(DigitsKeyListener.getInstance(digits01));
            mSendRed0TextWatcher = new SendRed0TextWatcher(this, mRedType, mBtnSubmitRed, mEditMoney,
                    mEditNumRed, mTxtAllMoneyShow, numStyle);
            mEditMoney.addTextChangedListener(mSendRed0TextWatcher);
        } else {
            mEditMoney.setKeyListener(DigitsKeyListener.getInstance(digits02));
            mSendRed1TextWatcher = new SendRed1TextWatcher(this, mRedType, mBtnSubmitRed, mEditMoney,
                    mEditNumRed, mTxtAllMoneyShow, numStyle);
            mEditMoney.addTextChangedListener(mSendRed1TextWatcher);
        }
        mEditNumRed.addTextChangedListener(new RedNumTextWatcher());
        mEditRedDes.addTextChangedListener(new ReDesTextWatcher());

        if (mRedType == 2) {
            if (mLanguageType == 1) {
                mImgRedType.setImageResource(R.mipmap.icon_red_style_ping);
            } else {
                mImgRedType.setImageResource(R.mipmap.icon_red_style_ping_en);
            }
        } else {
            if (mLanguageType == 1) {
                mImgRedType.setImageResource(R.mipmap.icon_red_style_pu);
            } else {
                mImgRedType.setImageResource(R.mipmap.icon_red_style_pu_en);
            }
        }
    }

    private void initData() {
        showProgressDialog();
        new TotalHpbRequest(mDefultAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {
                    BigDecimal money = new BigDecimal("" + jsonArray.get(2));
                    mCurAllMoney = Convert.fromWei(money, Convert.Unit.ETHER);
                    mTxtAllMoney.setText(getResources().getString(R.string.activity_red_send_txt_01) + SlinUtil.formatNumFromWeiS(SendRedPacketsActivity.this, money) + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_01));
                }
            }

            @Override
            public void onError(String error) {
                DappApplication.getInstance().showToast(error);
            }
        });

        //获取gasLimit gasPrice
        new GetHpbFeeRequest(mDefultAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                //最大交易次数
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                //交易费用
                String strFee = SlinUtil.FeeValueString(SendRedPacketsActivity.this, mGasPrice, mGasLimit);
                mTxtFeeMoney.setText(SlinUtil.tailClearAll(SendRedPacketsActivity.this, strFee) + " " + SendRedPacketsActivity.this.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
                dismissProgressDialog();
            }
        });
    }

    private class RedNumTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String money = mEditMoney.getText().toString();
            if (s.length() > 0 && money.length() > 0) {
                mBtnSubmitRed.setEnabled(true);
                mBtnSubmitRed.setTextColor(getResources().getColor(R.color.white));
                mBtnSubmitRed.setBackgroundResource(R.drawable.draw_btn_defult_bg_03);
            } else {
                mBtnSubmitRed.setEnabled(false);
                mBtnSubmitRed.setTextColor(getResources().getColor(R.color.color_2E2F47));
                mBtnSubmitRed.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
            }
            String text = s.toString();

            if (text.equals("0")) {
                mEditNumRed.setText("");
                return;
            }

            if (mRedType == 1) {
                String curMoney = money;
                if (numStyle == 1) {//,
                    if (curMoney.contains(",")) {
                        curMoney = money.replace(",", ".");
                    }
                }
                String txtMoney = "0";
                if (!StrUtil.isEmpty(money) && !StrUtil.isEmpty(text)) {
                    BigDecimal showMoney = new BigDecimal(text).multiply(new BigDecimal(curMoney));
                    txtMoney = String.valueOf(showMoney);
                    if (numStyle == 1) {
                        if (txtMoney.contains(".")) {
                            txtMoney = txtMoney.replace(".", ",");
                        }
                    }
                }
                String total = txtMoney;
                if (numStyle == 1 && txtMoney.contains(",")) {
                    total = txtMoney.replace(",", ".");
                }
                String ssTol = SlinUtil.tailClearAll(SendRedPacketsActivity.this, total);
                if (numStyle == 1 && ssTol.contains(".")) {
                    ssTol = ssTol.replace(".", ",");
                }
//                mTxtAllMoneyShow.setText(ssTol);

                //------
                String zMoney = ssTol;
                if (numStyle == 1 && zMoney.contains(",")) {
                    zMoney = zMoney.replace(",", ".");
                }
                zMoney = SlinUtil.tailClearAll(SendRedPacketsActivity.this, SlinUtil.NumberFormat2(SendRedPacketsActivity.this, new BigDecimal(zMoney)));
                mTxtAllMoneyShow.setText(zMoney);
            } else {
                if (StrUtil.isEmpty(money)) {
                    money = "0";
                }
                String total = money;
                if (numStyle == 1 && money.contains(",")) {
                    total = money.replace(",", ".");
                }
                String ssTol = SlinUtil.tailClearAll(SendRedPacketsActivity.this, total);
                if (numStyle == 1 && ssTol.contains(".")) {
                    ssTol = ssTol.replace(".", ",");
                }
//                mTxtAllMoneyShow.setText(ssTol);

                //------
                String zMoney = ssTol;
                if (numStyle == 1 && zMoney.contains(",")) {
                    zMoney = zMoney.replace(",", ".");
                }
                zMoney = SlinUtil.tailClearAll(SendRedPacketsActivity.this, SlinUtil.NumberFormat2(SendRedPacketsActivity.this, new BigDecimal(zMoney)));
                mTxtAllMoneyShow.setText(zMoney);
            }
            if (!"".equals(text) && Integer.valueOf(text) > 100) {
                mEditNumRed.setText("100");
                mEditNumRed.setSelection(3);
                BigInteger gasLimit = new BigInteger("0");
                gasLimit = mGasLimit.multiply(new BigInteger("100"));
                String strFee = SlinUtil.FeeValueString(SendRedPacketsActivity.this, mGasPrice, gasLimit);
                mTxtFeeMoney.setText(SlinUtil.tailClearAll(SendRedPacketsActivity.this, strFee) + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_01));
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_05));
            } else {
                BigInteger gasLimit = new BigInteger("0");
                if (StrUtil.isEmpty(text)) {
                    gasLimit = mGasLimit;
                } else {
                    gasLimit = mGasLimit.multiply(new BigInteger(text));
                }

                String strFee = SlinUtil.FeeValueString(SendRedPacketsActivity.this, mGasPrice, gasLimit);
                mTxtFeeMoney.setText(SlinUtil.tailClearAll(SendRedPacketsActivity.this, strFee) + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_01));
            }
        }
    }

    private class ReDesTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            mTxtDesNum.setText("" + length + "/25");

            if (length > 0) {
                mTxtDesNum.setTextColor(getResources().getColor(R.color.color_2E2F47));
            } else {
                mTxtDesNum.setTextColor(getResources().getColor(R.color.color_D1D2DC));
            }
        }
    }

    @OnClick({R.id.send_red_packets_txt_change_red_type, R.id.send_red_packets_txt_what_fee, R.id.send_red_packets_btn_submit_red})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_red_packets_txt_change_red_type:
                if (mRedType == 2) {
                    mRedType = 1;
                    mTxtRedType.setText(getResources().getString(R.string.activity_red_send_txt_02_1));
                    mTxtRedTypeDes.setText(getResources().getString(R.string.activity_red_send_txt_04_1));
                    mTxtChangeRedType.setText(getResources().getString(R.string.activity_red_send_txt_05_1));
                    if (mLanguageType == 1) {
                        mImgRedType.setImageResource(R.mipmap.icon_red_style_pu);
                    } else {
                        mImgRedType.setImageResource(R.mipmap.icon_red_style_pu_en);
                    }
                } else {
                    mRedType = 2;
                    mTxtRedType.setText(getResources().getString(R.string.activity_red_send_txt_02));
                    mTxtRedTypeDes.setText(getResources().getString(R.string.activity_red_send_txt_04));
                    mTxtChangeRedType.setText(getResources().getString(R.string.activity_red_send_txt_05));
                    if (mLanguageType == 1) {
                        mImgRedType.setImageResource(R.mipmap.icon_red_style_ping);
                    } else {
                        mImgRedType.setImageResource(R.mipmap.icon_red_style_ping_en);
                    }
                }
                if (numStyle == 0) {
                    mSendRed0TextWatcher.setRedType(mRedType);
                } else mSendRed1TextWatcher.setRedType(mRedType);
                mEditMoney.setText("");
                mEditMoney.requestFocus();
                mEditNumRed.setText("");
                mEditRedDes.setText("");
                mTxtAllMoneyShow.setText("0");
                break;
            case R.id.send_red_packets_txt_what_fee:
                Intent goto_webView = new Intent(this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.transfer_activity_txt_10));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + DAppConstants.backUrlHou(this, 9));
                startActivity(goto_webView);
                break;
            case R.id.send_red_packets_btn_submit_red:
                submitRed();
                break;
        }
    }

    private void submitRed() {
        String money = mEditMoney.getText().toString();
        String num = mEditNumRed.getText().toString();

        if (StrUtil.isEmpty(money)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_03));
            return;
        }

        if (money.equals("0")) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_01));
            return;
        }

        if (numStyle == 0) {
            if (money.contains(".")) {
                String mm = money.replace(",", "");
                if (new BigDecimal(mm.replace(".", "")).compareTo(new BigDecimal(0)) == 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_01));
                    return;
                }
            }
        } else {
            if (money.contains(",")) {
                String mm = money.replace(".", "");
                if (new BigDecimal(mm.replace(",", "")).compareTo(new BigDecimal(0)) == 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_01));
                    return;
                }
            }
        }

        if (mRedType == 1) {//普通
            if (money.contains(",")) {
                money = money.replace(",", ".");
            }
            if ((Float.valueOf(money) >= 100 && money.contains(".")) || (Float.valueOf(money) > 100 && !money.contains("."))) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_06));
            }
        } else {
            if (money.contains(",")) {
                money = money.replace(",", ".");
            }
            if ((Float.valueOf(money) >= 10000 && money.contains(".")) || (Float.valueOf(money) > 10000 && !money.contains("."))) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_03));
            }
        }

        if (numStyle == 1 && money.contains(",")) {
            BigDecimal m = new BigDecimal(money.replace(",", ".")).multiply(new BigDecimal(100));
            if (mRedType == 2) {
                if (new BigDecimal(num).compareTo(m) > 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_02));
                    return;
                }
            }

            if (m.compareTo(new BigDecimal(1)) == -1) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_02));
                return;
            }

            if (new BigDecimal(money.replace(",", ".")).compareTo(new BigDecimal(10000)) > 0) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_03));
                return;
            }

        } else {
            if (mRedType == 2) {
                if (new BigDecimal(num).compareTo(new BigDecimal(money).multiply(new BigDecimal(100))) > 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_02));
                    return;
                }
            }

            if (new BigDecimal(money).multiply(new BigDecimal(100)).compareTo(new BigDecimal(1)) == -1) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_02));
                return;
            }

            if (new BigDecimal(money).compareTo(new BigDecimal(10000)) > 0) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_03));
                return;
            }
        }

        if (num.equals(0)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_04));
            return;
        }

        if (Integer.valueOf(num) > 100) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_send_toast_05));
            return;
        }

        if (mRedType == 2) {//直接剩余金额对比输入金额
            if (!MoneyHandle(mRedType, new BigDecimal(num))) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_get_txt_13));
                return;
            }
        } else {//money*num  对比剩余金额
            if (!MoneyHandle(mRedType, new BigDecimal(num))) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_get_txt_13));
                return;
            }
        }

        //打开输入密码框
        BigDecimal moneyAll = accountMoney(mRedType, new BigDecimal(num));
        showDialog(String.valueOf(moneyAll));
    }

    /**
     * 弹出输入密码框
     */
    private void showDialog(String money) {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this, mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                //获取私钥
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(SendRedPacketsActivity.this, mDefultAddress, psd, 10);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                        } else {
                            //准备发红包
                            rawSendRed(result, money);
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        String allMoney = mTxtAllMoneyShow.getText().toString();
        commonPsdPop.show(getResources().getString(R.string.activity_red_send_txt_12),
                getResources().getString(R.string.activity_red_send_txt_13, allMoney));
    }

    private void rawSendRed(String privateKey, String money) {
        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        String redNum = mEditNumRed.getText().toString();
        String redDes = mEditRedDes.getText().toString();
        if (StrUtil.isEmpty(redDes)) {
            redDes = getResources().getString(R.string.activity_red_send_txt_11);
        }

        String count = mTxtAllMoneyShow.getText().toString();
        if (numStyle == 1) {
            if (count.contains(".") && !count.contains(",")) {
                count = count.replace(".", ",");
            } else if (count.contains(",")) {
                String m1 = count.replace(".", ",");
                StringBuilder sb = new StringBuilder(m1);
                int index = m1.lastIndexOf(",");
                count = (sb.replace(index, index + 1, ".")).toString();
            }
        }

        BigDecimal mm = Convert.toWei(new BigDecimal(count.replace(",", "")), Convert.Unit.ETHER);

        new RawReadyRequest(mDefultAddress, String.valueOf(SlinUtil.ValueScale(mm, 0)), redNum, "1", String.valueOf(mRedType), redDes).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                RedBaseBean mRedBaseBean = JSON.parseObject(jsonArray.get(2).toString(), RedBaseBean.class);
                //获取nonce
                getNonce(mRedBaseBean, privateKey, money);
            }
        });
    }

    private void getNonce(RedBaseBean redBaseBean, String privateKey, String money) {
        showProgressDialog();
        new GetNonceRequest(mDefultAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                String status = (String) jsonArray.get(0);
                BigInteger nonce = null;
                if ("000000".equals(status)) {//代表处理成功
                    MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                    nonce = mapEthInfo.getNonce();
                    BigDecimal money01 = new BigDecimal(money);
                    handleSignTransaction(redBaseBean, money01, privateKey, nonce);
                }
            }
        });
    }

    private void handleSignTransaction(RedBaseBean redBaseBean, BigDecimal value, String privateKey, BigInteger nonce) {
        //交易金额
        String num = mEditNumRed.getText().toString();
        String signedData = TransferUtils.redSignDataMethod(mDefultAddress, redBaseBean, "mintDedaultBatch"
                , nonce, mGasPrice, mGasLimit.multiply(new BigInteger(num)), privateKey, value, redBaseBean.getContractAddr());
        mRedId = redBaseBean.getPacketNo();
        //发送
        new SendRedRequest(mRedId, signedData).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                //交易状态：0-失败，1-成功，2-确认中
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    //发送成功之后，弹出对话框
                    mRedSendWaitingDialog = new RedSendWaitingDialog(SendRedPacketsActivity.this, mCommonToobar);
                    mRedSendWaitingDialog.setOnRedSendWaitListener(new RedSendWaitingDialog.OnRedSendWaitListener() {
                        @Override
                        public void setOnSplahListener(String redId) {
                            //刷新交易状态
                            flashRed(mRedId);
                        }

                        @Override
                        public void setOnShareListener() {
                            //todo 分享
                            mRedSendWaitingDialog.dismiss();
                        }

                        @Override
                        public void cancel() {
                            mTimer.cancel();
                        }
                    });
                    String num = mEditNumRed.getText().toString();
                    String allMoney = mTxtAllMoneyShow.getText().toString();
                    mRedSendWaitingDialog.show(num, allMoney, mRedId);

                    //开启倒计时
                    SystemLog.I("ztt", "倒计时开始");
                    frashData();
                }
            }
        });
    }

    //没7秒刷新一次红包就绪中
    private CountDownTimer mTimer;

    private void frashData() {
        mTimer = new CountDownTimer(600 * 1000 + 500, 7000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SystemLog.I("ztt", "倒计时开始--调用");
                flashRed(mRedId);
            }

            @Override
            public void onFinish() {
                //不做处理
            }
        };
        mTimer.start();
    }

    private void flashRed(String redId) {
        showProgressDialog();
        new RedFalshRequest(redId).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    //交易状态：0-失败，1-成功，2-确认中
                    String redStatus = jsonArray.get(2).toString();
                    if ("0".equals(redStatus)) {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_get_txt_05));
                        mRedSendWaitingDialog.dismiss();
                        mTimer.cancel();
                    } else if ("1".equals(redStatus)) {
                        //发送请求交易状态
                        mTimer.cancel();
                        mRedSendWaitingDialog.showWaitOk();
                    }
                    dismissProgressDialog();
                }
            }
        });
    }

    /**
     * 计算转账金额是否小于剩余金额
     *
     * @return
     */
    private boolean MoneyHandle(int type, BigDecimal num) {
        String count = mEditMoney.getText().toString();
        BigDecimal money;
        if (numStyle == 1 && count.contains(",")) {
            String m1 = count.replace(".", ",");
            StringBuilder sb = new StringBuilder(m1);
            int index = m1.lastIndexOf(",");
            count = (sb.replace(index, index + 1, ".")).toString();
        }

        money = new BigDecimal(count.replace(",", ""));
        BigDecimal money1;
        BigInteger gasLimit = mGasLimit.multiply(num.toBigInteger());
        if (type == 2) {
            money1 = Convert.toWei(money, Convert.Unit.ETHER).add(new BigDecimal(gasLimit.multiply(mGasPrice)));
        } else {
            money1 = Convert.toWei(num.multiply(money), Convert.Unit.ETHER).add(new BigDecimal(gasLimit.multiply(mGasPrice)));
        }
        if (money1.compareTo(Convert.toWei(mCurAllMoney, Convert.Unit.ETHER)) > 0) {
            return false;
        }
        return true;
    }

    /**
     * 计算总金额
     *
     * @param type
     * @param num
     * @return
     */
    private BigDecimal accountMoney(int type, BigDecimal num) {
        //金额 = fee + 发的费用*个数 +红包金额
        String count = mEditMoney.getText().toString();
        if (numStyle == 1 && count.contains(",")) {
            String m1 = count.replace(".", ",");
            StringBuilder sb = new StringBuilder(m1);
            int index = m1.lastIndexOf(",");
            count = (sb.replace(index, index + 1, ".")).toString();
        }

        BigDecimal money = new BigDecimal(count.replace(",", ""));
        BigDecimal money1;
        BigInteger gasLimit = mGasLimit.multiply(num.toBigInteger());
        if (type == 2) {
            money1 = Convert.toWei(money, Convert.Unit.ETHER).add(new BigDecimal(gasLimit.multiply(mGasPrice)));
        } else {
            money1 = Convert.toWei(num.multiply(money), Convert.Unit.ETHER).add(new BigDecimal(gasLimit.multiply(mGasPrice)));
        }

        money1 = SlinUtil.ValueScale(money1, 0);
        return money1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
