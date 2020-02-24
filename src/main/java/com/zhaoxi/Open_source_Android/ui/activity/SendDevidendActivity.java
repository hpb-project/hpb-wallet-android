package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.VoteSignBean;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPsdPop;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CreateQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.SendNumber0TextWatcher;
import com.zhaoxi.Open_source_Android.libs.tools.SendNumber1TextWatcher;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ColdWalletUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.CreateTradeRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetHpbFeeRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetNodeBalanceRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteTransationReceiptRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteVoteRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.net.bean.NodeDividenBean;
import com.zhaoxi.Open_source_Android.net.bean.VotePersonBean;
import com.zhaoxi.Open_source_Android.ui.dialog.NodeDividendBLPop;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendDevidendActivity extends BaseTitleBarActivity {
    public static final String CONTRACTADDR = "NodeDividendRecordsActivity.CONTRACTADDR";
    public static final String BILI = "NodeDividendRecordsActivity.BILI";
    public static final String IS_CAN_SET = "NodeDividendRecordsActivity.IS_CAN_sET";
    private static final int REQUEST_CODE_SCAN_DBL = 0x0003;
    private static final int REQUEST_CODE_SCAN_DC = 0x0004;
    public static final String SIGN_CONTENT = "signContent";

    @BindView(R.id.activity_send_devidend_ed_money)
    EditText mEdMoney;
    @BindView(R.id.activity_send_devidend_txt_allmoney)
    TextView mTxtAllmoney;
    @BindView(R.id.root_send_devidend)
    LinearLayout mRootView;
    @BindView(R.id.activity_send_devidend_ttx_choose_fw)
    TextView mTxtChooseFW;

    private BigDecimal mAllMoney = new BigDecimal("0");
    private int numStyle;
    private BigInteger mGasPrice = new BigInteger("0");
    private BigInteger mGasLimit = new BigInteger("0");
    private BigInteger mNonce;
    private String mDefultAddress;
    private String mBonusContractAddr, mFenHBl;//合约地址
    private CommonPsdPop mBlPsdDialog, mMoneyPsdDialog;
    private boolean mIsCanSet = false;
    private boolean isSetBlSuccese = false, isTimeEnd = false,
            isSetMoneySuccse = false, isMoneyTimeEnd = false;
    private String mTransBlHax = "", mTransMoneyHax = "";
    private CountDownTimer mTimer, mMoneyTimer;
    private String digits01 = "0123456789.";
    private String digits02 = "0123456789,";
    private CreateDbWallet mCreateDbWallet;
    private boolean mIsColdWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_devidend);
        ButterKnife.bind(this);
        mBonusContractAddr = getIntent().getStringExtra(CONTRACTADDR);
        mFenHBl = getIntent().getStringExtra(BILI);
        mIsCanSet = getIntent().getBooleanExtra(IS_CAN_SET, false);
        mCreateDbWallet = new CreateDbWallet(this);

        initViews();
        initData();
    }

    private void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false, 0.2f)
                .init();
        setTitleBgColor(R.color.base_new_theme_color, false);
        setTitle(getResources().getString(R.string.activity_cion_fh_txt_24), true);

        showRightTxtWithTextListener(getResources().getString(R.string.activity_cion_fh_txt_12), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分红记录
                startActivity(new Intent(SendDevidendActivity.this, NodeDividendRecordsActivity.class));
            }
        });
        if (mIsCanSet) {
            mTxtChooseFW.setVisibility(View.VISIBLE);
        }

        numStyle = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mIsColdWallet = mCreateDbWallet.isColdWallet(this, mDefultAddress);

        if (numStyle == 0) {
            mEdMoney.setKeyListener(DigitsKeyListener.getInstance(digits01));
            mEdMoney.addTextChangedListener(new SendNumber0TextWatcher(SendDevidendActivity.this, mEdMoney));
        } else {
            mEdMoney.setKeyListener(DigitsKeyListener.getInstance(digits02));
            mEdMoney.addTextChangedListener(new SendNumber1TextWatcher(SendDevidendActivity.this, mEdMoney));
        }
    }

    private void initData() {
        if (!StrUtil.isEmpty(mDefultAddress)) {//没有就获取最新的钱包
            new GetNodeBalanceRequest(mDefultAddress).doRequest(this, new CommonResultListener(this) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    String status = (String) jsonArray.get(0);
                    if ("000000".equals(status) && jsonArray.get(2) != null) {//代表处理成功
                        NodeDividenBean nodeDividenBean = JSON.parseObject(jsonArray.get(2).toString(), NodeDividenBean.class);

                        BigDecimal allMoney = new BigDecimal("" + nodeDividenBean.getBonusBalance());
                        String canNum = SlinUtil.tailClearAll(SendDevidendActivity.this, SlinUtil.formatNumFromWeiS(SendDevidendActivity.this, allMoney));
                        mTxtAllmoney.setText(getResources().getString(R.string.activity_cion_fh_txt_10_02, canNum));

                        mAllMoney = Convert.fromWei(new BigDecimal("" + nodeDividenBean.getCurBalance()), Convert.Unit.ETHER);
                    }
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                    mTxtAllmoney.setText(getResources().getString(R.string.activity_cion_fh_txt_10_02, "0"));
                }
            });
        }

        //获取gasLimit gasPrice
        new GetHpbFeeRequest(mDefultAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                //最大交易次数
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
            }
        });
    }

    @OnClick({R.id.activity_send_devidend_btn_confirm, R.id.activity_send_devidend_ttx_choose_fw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_send_devidend_btn_confirm:
                submitRedSet();
                break;
            case R.id.activity_send_devidend_ttx_choose_fw:
                changeRange();
                break;
        }
    }

    private void submitRedSet() {
        String redMoney = mEdMoney.getText().toString();
        if (StrUtil.isEmpty(redMoney)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_25_01));
            return;
        }
        if ("0".equals(redMoney)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_25_02));
            return;
        }

        if (numStyle == 0) {
            if (redMoney.contains(".")) {
                String mm = redMoney.replace(",", "");
                if (new BigDecimal(mm.replace(".", "")).compareTo(new BigDecimal(0)) == 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_25_02));
                    return;
                }
            }
        } else {
            if (redMoney.contains(",")) {
                String mm = redMoney.replace(".", "");
                if (new BigDecimal(mm.replace(",", "")).compareTo(new BigDecimal(0)) == 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_25_02));
                    return;
                }
            }
        }

        if (!MoneyHandle()) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_25_03));
            return;
        }

        if (mIsColdWallet) {
            handleColdWallet(redMoney, 2);
        } else showprivateKeyDialog(redMoney);
    }

    /**
     * 计算转账金额是否小于剩余金额
     *
     * @return
     */
    private boolean MoneyHandle() {
        String count = mEdMoney.getText().toString();
        BigDecimal money;
        if (numStyle == 1) {
            if (count.contains(",")) {
                String m1 = count.replace(".", ",");
                StringBuilder sb = new StringBuilder(m1);
                int index = m1.lastIndexOf(",");
                count = (sb.replace(index, index + 1, ".")).toString();
            }
        }
        money = new BigDecimal(count.replace(",", ""));
        BigDecimal money1 = Convert.toWei(money, Convert.Unit.ETHER).add(new BigDecimal(mGasLimit.multiply(mGasPrice)));
        if (money1.compareTo(Convert.toWei(mAllMoney, Convert.Unit.ETHER)) > 0) {
            return false;
        }
        return true;
    }

    private void changeRange() {
        NodeDividendBLPop blPop = new NodeDividendBLPop(this, mRootView);
        blPop.setHandleData(new NodeDividendBLPop.HandleData() {
            @Override
            public void getData(String bilie) {
                if (mIsColdWallet) {
                    handleColdWallet(bilie, 1);
                } else showPsd(bilie);//弹出密码框
            }
        });
        blPop.show(mFenHBl);
    }

    private void handleColdWallet(String value, int type) {
        new GetHpbFeeRequest(mDefultAddress).doRequest(SendDevidendActivity.this, new CommonResultListener(SendDevidendActivity.this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                //最大交易次数
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                mNonce = mapEthInfo.getNonce();

                VoteSignBean votePersonBean = new VoteSignBean();
                votePersonBean.setFrom(mDefultAddress);
                votePersonBean.setNonce(mNonce.toString());
                votePersonBean.setGaslimt(mGasLimit.toString());
                votePersonBean.setGasprice(mGasPrice.toString());

                votePersonBean.setContractAddress(mBonusContractAddr);
                String content = "";
                String data = "";
                if (type == 1) {//比列
                    data = TransferUtils.DividendBonusBiliData(mDefultAddress, new BigDecimal(value));
                    content = getResources().getString(R.string.activity_cold_sign_txt_04, value) + "%";
                } else {
                    content = getResources().getString(R.string.activity_cold_sign_txt_05, value);
                    data = TransferUtils.DividendMoneyData(mDefultAddress);
                    BigDecimal money01 = null;
                    if (numStyle == 0) {
                        money01 = new BigDecimal(value.replace(",", ""));
                    } else {
                        String money00 = "";
                        if (value.contains(",")) {
                            String m1 = value.replace(".", ",");
                            StringBuilder sb = new StringBuilder(m1);
                            int index = m1.lastIndexOf(",");
                            money00 = (sb.replace(index, index + 1, ".")).toString();
                        } else {
                            money00 = value;
                        }
                        money01 = new BigDecimal(money00.replace(",", ""));
                    }
                    votePersonBean.setMoney(money01.toString());
                }
                votePersonBean.setContent(content);
                votePersonBean.setData(data);

                String strContent = ColdWalletUtil.toJson(ColdWalletUtil.TYPE_AUTHOR, votePersonBean);
                SystemLog.D("showPopupWindow", "strContent = " + strContent);
                showQrCodeDialog(strContent, type);
            }
        });
    }

    private void showQrCodeDialog(String strContent, int showType) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_digital_sign_qrcode_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);

        ImageView ivDigitalSignQrCode = view.findViewById(R.id.iv_qr_code_info);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_qrcode_logo, null);
        CreateQrAsyncTask asyncTask = new CreateQrAsyncTask(this, ivDigitalSignQrCode, strContent, logo);
        asyncTask.setOnResultListener(new CreateQrAsyncTask.OnResultListener() {
            @Override
            public void setOnResultListener(Bitmap bitmap) {
                // 生成的bitmap
            }
        });
        asyncTask.execute();//开始执行

        Button btnNextTip = view.findViewById(R.id.btn_next_tip);
        btnNextTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // 下一步，跳转到扫码页面
                if (showType == 1) {
                    Intent intent = new Intent(SendDevidendActivity.this, ScaningActivity.class);
                    intent.putExtra(ScaningActivity.RESURE_TYPE, ScaningActivity.TYPE_NODE_DED);
                    startActivityForResult(intent, REQUEST_CODE_SCAN_DBL);
                } else{
                    Intent intent = new Intent(SendDevidendActivity.this, ScaningActivity.class);
                    intent.putExtra(ScaningActivity.RESURE_TYPE, ScaningActivity.TYPE_NODE_DED);
                    startActivityForResult(intent, REQUEST_CODE_SCAN_DC);
                }
            }
        });

        // 设置背景透明度
        final WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }

    private void showPsd(String bilie) {
        mBlPsdDialog = new CommonPsdPop(this, mRootView);
        mBlPsdDialog.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(SendDevidendActivity.this, mDefultAddress, psd, 4);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(result);
                        } else {//获取nonce
                            showProgressDialog();
                            getNonce(bilie, 1, result);
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        mBlPsdDialog.show(getResources().getString(R.string.activity_cion_fh_txt_27_01), null);
    }

    /**
     * 确认分红预设
     */
    private void showprivateKeyDialog(String money) {
        mMoneyPsdDialog = new CommonPsdPop(this, mRootView);
        mMoneyPsdDialog.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(SendDevidendActivity.this, mDefultAddress, psd, 3);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(result);
                        } else {//获取nonce
                            showProgressDialog();
                            BigDecimal money01 = null;
                            if (numStyle == 0) {
                                money01 = new BigDecimal(money.replace(",", ""));
                            } else {
                                String money00 = "";
                                if (money.contains(",")) {
                                    String m1 = money.replace(".", ",");
                                    StringBuilder sb = new StringBuilder(m1);
                                    int index = m1.lastIndexOf(",");
                                    money00 = (sb.replace(index, index + 1, ".")).toString();
                                } else {
                                    money00 = money;
                                }
                                money01 = new BigDecimal(money00.replace(",", ""));
                            }
                            getNonce(money01.toString(), 2, result);
                        }
                    }
                });
                asyncTask.execute();
            }
        });

        mMoneyPsdDialog.show(getResources().getString(R.string.activity_cion_fh_txt_26_01), getResources().getString(R.string.activity_cion_fh_txt_26_02, money));
    }

    private void getNonce(String value, int type, String privateKey) {//type 1设置比例 2确认金额
        //获取gasLimit gasPrice
        new GetHpbFeeRequest(mDefultAddress).doRequest(SendDevidendActivity.this, new CommonResultListener(SendDevidendActivity.this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                //最大交易次数
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                mNonce = mapEthInfo.getNonce();

                if (type == 1) {
                    String signedData = TransferUtils.DividendBonusPercentageMethod(mNonce, mGasPrice, mGasLimit, privateKey,
                            mBonusContractAddr, mDefultAddress, new BigDecimal(value));
                    new CreateTradeRequest(signedData).doRequest(SendDevidendActivity.this,
                            new CommonResultListener(SendDevidendActivity.this) {
                                @Override
                                public void onSuccess(JSONArray jsonArray) {
                                    super.onSuccess(jsonArray);
                                    mTransBlHax = jsonArray.get(2).toString();
                                    dismissProgressDialog();
                                    if (mBlPsdDialog != null) {
                                        mBlPsdDialog.dismiss();
                                    }
                                    showProgress();
                                }
                            });
                } else {
                    BigInteger valueData = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
                    String signedData = TransferUtils.DividendMoneySetMethod(mNonce, mGasPrice, mGasLimit, privateKey,
                            mBonusContractAddr, mDefultAddress, valueData);
                    new CreateTradeRequest(signedData).doRequest(SendDevidendActivity.this,
                            new CommonResultListener(SendDevidendActivity.this) {
                                @Override
                                public void onSuccess(JSONArray jsonArray) {
                                    super.onSuccess(jsonArray);
                                    mTransMoneyHax = jsonArray.get(2).toString();
                                    dismissProgressDialog();
                                    if (mMoneyPsdDialog != null) {
                                        mMoneyPsdDialog.dismiss();
                                    }
                                    showMoneyProgress();
                                }
                            });
                }
            }
        });
    }

    private void showMoneyProgress() {
        getTransactionMoneyReceipt();
        mMoneyTimer = new CountDownTimer(60 * 1000 + 500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isShowDialog()) {
                    showTextProgressDialog(getResources().getString(R.string.activity_vote_gl_txt_17));
                }
            }

            @Override
            public void onFinish() {
                dismissTextProgressDialog();
                isMoneyTimeEnd = true;
            }
        };
        mMoneyTimer.start();
    }

    private void showProgress() {
        getTransactionBlReceipt();
        mTimer = new CountDownTimer(60 * 1000 + 500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isShowDialog()) {
                    showTextProgressDialog(getResources().getString(R.string.activity_vote_gl_txt_17));
                }
            }

            @Override
            public void onFinish() {
                dismissTextProgressDialog();
                isTimeEnd = true;
            }
        };
        mTimer.start();
    }

    private void getTransactionMoneyReceipt() {
        if (!isMoneyTimeEnd && !isSetMoneySuccse) {
            new VoteTransationReceiptRequest(mTransMoneyHax).doRequest(this, new CommonResultListener(this) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    super.onSuccess(jsonArray);
                    //0x1代表成功，0x0代表失败，null就是还在pending
                    if (jsonArray.get(2) == null) {//继续转圈
                        getTransactionMoneyReceipt();
                        isSetMoneySuccse = false;
                    } else {
                        isSetMoneySuccse = true;
                        VotePersonBean VotePersonBean = JSON.parseObject(jsonArray.get(2).toString(), VotePersonBean.class);
                        String status = VotePersonBean.getStatus();
                        if ("0x1".equals(status)) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_28_01));
                            initData();
                        } else {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_28_02));
                        }
                        dismissTextProgressDialog();
                        mMoneyTimer.cancel();
                    }
                }

                @Override
                public void onError(String error) {
                    mMoneyTimer.cancel();
                    dismissTextProgressDialog();
                    DappApplication.getInstance().showToast(error);
                }
            });
        }
    }

    private void getTransactionBlReceipt() {
        if (!isTimeEnd && !isSetBlSuccese) {
            new VoteTransationReceiptRequest(mTransBlHax).doRequest(this, new CommonResultListener(this) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    super.onSuccess(jsonArray);
                    //0x1代表成功，0x0代表失败，null就是还在pending
                    if (jsonArray.get(2) == null) {//继续转圈
                        getTransactionBlReceipt();
                        isSetBlSuccese = false;
                    } else {
                        isSetBlSuccese = true;
                        VotePersonBean VotePersonBean = JSON.parseObject(jsonArray.get(2).toString(), VotePersonBean.class);
                        String status = VotePersonBean.getStatus();
                        if ("0x1".equals(status)) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_28_03));
                            mIsCanSet = false;
                            mTxtChooseFW.setVisibility(View.GONE);
                        } else {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_28_04));
                        }
                        dismissTextProgressDialog();
                        mTimer.cancel();
                    }
                }

                @Override
                public void onError(String error) {
                    mTimer.cancel();
                    dismissTextProgressDialog();
                    DappApplication.getInstance().showToast(error);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN_DBL && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra(SIGN_CONTENT);
            if (!TextUtils.isEmpty(result)) {
                new VoteVoteRequest(result).doRequest(SendDevidendActivity.this, new CommonResultListener(SendDevidendActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        super.onSuccess(jsonArray);
                        mTransBlHax = jsonArray.get(2).toString();
                        dismissProgressDialog();
                        showProgress();
                    }
                });
            }
        } else if (requestCode == REQUEST_CODE_SCAN_DC && resultCode == RESULT_OK && data != null) {
            String result1 = data.getStringExtra(SIGN_CONTENT);
            if (!TextUtils.isEmpty(result1)) {
                new VoteVoteRequest(result1).doRequest(SendDevidendActivity.this, new CommonResultListener(SendDevidendActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        super.onSuccess(jsonArray);
                        mTransMoneyHax = jsonArray.get(2).toString();
                        dismissProgressDialog();
                        showMoneyProgress();
                    }
                });

            }
        }
    }
}
