package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.TransferQrCodeInfoBean;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPopupWindowDialog;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPsdPop;
import com.zhaoxi.Open_source_Android.common.dialog.CommonTransfgerConfirmDialog;
import com.zhaoxi.Open_source_Android.common.view.EasyPickerView;
import com.zhaoxi.Open_source_Android.common.view.EasyPickerViewWithSubText;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CreateQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.Number0TextWatcher;
import com.zhaoxi.Open_source_Android.libs.tools.Number1TextWatcher;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ColdWalletUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.CreateTradeRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetHpbFeeRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetNonceRequest;
import com.zhaoxi.Open_source_Android.net.Request.StockDetailRequest;
import com.zhaoxi.Open_source_Android.net.Request.TotalHpbRequest;
import com.zhaoxi.Open_source_Android.net.Request.WalletTransferRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.net.bean.StockDetailBean;
import com.zhaoxi.Open_source_Android.net.bean.TokenTypeBean;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;
import com.zhaoxi.Open_source_Android.web3.tx.ChainId;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferActivity extends BaseTitleBarActivity {
    public static final String ADDRESS = "address";
    public static final String TRANSFER_TYPE = "transferType";
    public static final String TOKEN_SYMBOL = "tokenSymbol";
    public static final int REQUEST_CODE = 0x333;
    public static final int REQUEST_ADDRESS_CODE = 0x004;
    private static final int REQUEST_SELECT_TOKEN_ID_CODE = 0x005;


    private static final int REQUEST_COLD_WALLET_TRANSFER = 0x006;
    public static final String RESULT_COLD_WALLET_TRANSFER = "RESULT_COLD_WALLET_TRANSFER";

    @BindView(R.id.root_layout)
    LinearLayout mRootLayout;
    @BindView(R.id.transfer_activity_edit_to_address)
    EditText mEditToAddress;
    @BindView(R.id.transfer_activity_edit_money)
    EditText mEditMoney;
    @BindView(R.id.transfer_activity_btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.transfer_activity_img_delete)
    ImageView mImgDelete;
    @BindView(R.id.transfer_activity_fee_money)
    TextView mTxtFeeMoney;
    @BindView(R.id.transfer_activity_edit_form_address)
    TextView mTxtFormAddress;

    @BindView(R.id.transfer_activity_img_token_type_up_down)
    ImageView mTokenTypeUpdown;
    @BindView(R.id.transfer_activity_img_token_name_up_down)
    ImageView mTokenTypeNameUpdown;

    @BindView(R.id.transfer_activity_token_name_container)
    RelativeLayout mTokenNameSelectContainer;
    @BindView(R.id.transfer_activity_token_type_container)
    LinearLayout mTokenTypeSelectContainer;

    @BindView(R.id.transfer_activity_edit_token_type)
    EditText mEtTokenType;
    @BindView(R.id.transfer_activity_type_name)
    TextView mTokenTypeName;
    @BindView(R.id.transfer_activity_txt_token_id)
    TextView mTokenTokenId;
    @BindView(R.id.tv_select_token_id)
    TextView mTokenSelectTokenId;
    @BindView(R.id.transfer_one_txt)
    TextView mTransTokenName;
    @BindView(R.id.txt_transfer_01)
    TextView mTransferTitle;

    @BindView(R.id.transfer_amount_container)
    LinearLayout mTransferAmountContainer;
    @BindView(R.id.transfer_token_container)
    LinearLayout mTransferTokenContainer;


    private String toAddress;
    private BigDecimal mCurAllMoney = new BigDecimal(0);

    private String mDefaultAddress = "";
    private CommonTransfgerConfirmDialog mCommonTransfgerConfirmDialog;
    private BigInteger mGasPrice;
    private BigInteger mGasLimit;
    private int numStyle;
    private String digits01 = "0123456789.";
    private String digits02 = "0123456789,";
    private String currentTokenType;
    private String currentTokenTypeName;
    private String transferType;
    private String mTokenSymbol;
    private String mContractAddress;
    private boolean isLoading = false;
    private boolean isFirstLoad = true;

    ArrayList<String> tokenTypeDatas = new ArrayList<>();
    ArrayList<String> tokenNumDatas = new ArrayList<>();
    //存放合约地址map: key :代币简称 value:代币合约地址
    private Map<String, String> contractAddressMap = new HashMap<>();
    // 存放代币数量map:key:代币简称 value:代币数量
    private Map<String, String> tokenNumMap = new HashMap<>();
    private BigDecimal mCurrentHpbMoney;
    private String mCurrentTokenNum;
    private int mTotalNum;
    private String moneyOrToken;
    private String mTransFee = "0";

    /*用于判断是否为冷钱包时需要*/
    private CreateDbWallet mCreateDbWallet;
    private WalletBean mWalletBean;
    private BigInteger mNonce;
    private String mSignContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        // 实例化创建钱包对象
        mCreateDbWallet = new CreateDbWallet(this);
        numStyle = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        mDefaultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mWalletBean = mCreateDbWallet.queryWallet(this, mDefaultAddress);
        toAddress = getIntent().getStringExtra(ADDRESS);
        transferType = getIntent().getStringExtra(TRANSFER_TYPE);// 获取转账类型
        mTokenSymbol = getIntent().getStringExtra(TOKEN_SYMBOL);
        // 交易费用
        initFee();
        switch (transferType) {
            case DAppConstants.TYPE_HRC_20:
                mEtTokenType.setText(getResources().getString(R.string.hrc_20));
                currentTokenType = getResources().getString(R.string.hrc_20);
                mEtTokenType.setEnabled(false);
                mTokenTypeName.setText(mTokenSymbol);
                mTransTokenName.setText(mTokenSymbol);
                mTokenTypeNameUpdown.setVisibility(View.VISIBLE);
                initTokenTypeName(DAppConstants.TYPE_HRC_20);
                mTransferTitle.setText(getResources().getString(R.string.activity_transfer_num));
                break;
            case DAppConstants.TYPE_HRC_721:
                mEtTokenType.setText(getResources().getString(R.string.hrc_721));
                currentTokenType = getResources().getString(R.string.hrc_721);
                mEtTokenType.setEnabled(false);
                mTokenTypeName.setText(mTokenSymbol);
                mTokenTypeNameUpdown.setVisibility(View.VISIBLE);
                mTransferAmountContainer.setVisibility(View.GONE);
                mTransferTokenContainer.setVisibility(View.VISIBLE);
                initTokenTypeName(DAppConstants.TYPE_HRC_721);
                mTransferTitle.setText(getResources().getString(R.string.transfer_roll_to_token_id));
                break;
            default:
                // 默认显示主链币
                mEtTokenType.setText(getResources().getString(R.string.main_token));
                // 币种默认是HPB
                mTokenTypeName.setText(TransferActivity.this.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
                mTransTokenName.setText(TransferActivity.this.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
                currentTokenType = TransferActivity.this.getResources().getString(R.string.hpb);
                // 币种下拉框图标默认不显示
                mTokenTypeNameUpdown.setVisibility(View.GONE);
                mTransferTitle.setText(getResources().getString(R.string.total_activity_txt_new_03));
                initHpbData();
                break;
        }


        if (!StrUtil.isEmpty(toAddress)) {
            mEditToAddress.setText(toAddress);
            mEditToAddress.setSelection(toAddress.length());
        } else {
            mEditToAddress.requestFocus();
        }

        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.color_F5F6F8, true);
        setTitle(R.string.main_home_top_txt_03, true);
        mTxtFormAddress.setText(mDefaultAddress);

        if (numStyle == 0) {
            mEditMoney.setKeyListener(DigitsKeyListener.getInstance(digits01));
            mEditMoney.addTextChangedListener(new Number0TextWatcher(this, mBtnSubmit, mEditMoney));
        } else {
            mEditMoney.setKeyListener(DigitsKeyListener.getInstance(digits02));
            mEditMoney.addTextChangedListener(new Number1TextWatcher(this, mBtnSubmit, mEditMoney));
        }

        mCommonTransfgerConfirmDialog = new CommonTransfgerConfirmDialog(this, mTxtTitle);
        mCommonTransfgerConfirmDialog.setOnSubmitListener(new CommonTransfgerConfirmDialog.OnSubmitListener() {
            @Override
            public void setOnSubmitListener(String money) {
                //输入密码
                if (mWalletBean.getIsClodWallet() != 0) {
                    if (!TextUtils.isEmpty(mSignContent)) {
                        SystemLog.D("setOnSubmitListener", "mSignContent = " + mSignContent);
                        new CreateTradeRequest(mSignContent).doRequest(TransferActivity.this, new CommonResultListener(TransferActivity.this) {
                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                super.onSuccess(jsonArray);
                                dismissProgressDialog();
                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_main_map_txt_06));
                                finish();
                            }
                        });
                    }
                } else {
                    showDialog(money);
                }

            }
        });
    }


    /**
     * 初始化HPB数据
     */
    private void initHpbData() {
        new TotalHpbRequest(mDefaultAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    mCurrentHpbMoney = new BigDecimal("" + jsonArray.get(2));
                    mCurAllMoney = Convert.fromWei(mCurrentHpbMoney, Convert.Unit.ETHER);
                    String hpbMoney = SlinUtil.formatNumFromWeiT(TransferActivity.this, mCurrentHpbMoney, 18);
                    mEditMoney.setHint(getResources().getString(R.string.transfer_activity_txt_009) + hpbMoney);
                }
            }

            @Override
            public void onError(String error) {
                DappApplication.getInstance().showToast(error);
            }
        });
    }

    /**
     * 获取交易费用
     */
    private void initFee() {
        //获取gasLimit gasPrice
        new GetHpbFeeRequest(mDefaultAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                //最大交易次数
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                //交易费用
                String strFee = SlinUtil.FeeValueString(TransferActivity.this, mGasPrice, mGasLimit);
                // 去除尾部零
                mTransFee = SlinUtil.tailClearAll(TransferActivity.this, strFee);
                // 设置交易费用
                mTxtFeeMoney.setText(mTransFee + TransferActivity.this.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
            }
        });
    }


    /**
     * 初始化代币库存数量
     */
    private void initTokenNumByContractAddress(String contractAddress) {
        new StockDetailRequest(1
                , contractAddress
                , SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS)).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    StockDetailBean stockDetailBean = JSON.parseObject(jsonArray.get(2).toString(), StockDetailBean.class);
                    mTotalNum = stockDetailBean.getTotal();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick({R.id.transfer_activity_btn_submit,
            R.id.transfer_activity_txt_all, R.id.transfer_activity_fee_des,
            R.id.transfer_activity_img_scan_address, R.id.transfer_activity_img_select_address,
            R.id.transfer_activity_img_token_type_up_down, R.id.transfer_activity_img_token_name_up_down,
            R.id.transfer_activity_token_name_container, R.id.transfer_activity_token_type_container,
            R.id.tv_select_token_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.transfer_activity_btn_submit:
                submitTrade();
                break;
            case R.id.transfer_activity_txt_all:
                String currentTokenType = mEtTokenType.getText().toString();
                if (StrUtil.isEmpty(currentTokenType)) {// 清先选择币种
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_transfer_select_token_type));
                    return;
                }

                if (getResources().getString(R.string.main_token).equals(currentTokenType)) {
                    currentTokenType = DAppConstants.TYPE_HPB;
                }

                switch (currentTokenType) {
                    case DAppConstants.TYPE_HRC_20:
                        if (!TextUtils.isEmpty(mCurrentTokenNum)) {
                            mEditMoney.setText(mCurrentTokenNum);
                            mEditMoney.setSelection(mCurrentTokenNum.length());
                        } else {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_transfer_select_token_type));
                        }
                        break;
                    case DAppConstants.TYPE_HPB:
                        String fee = mTxtFeeMoney.getText().toString();
                        if (StrUtil.isEmpty(fee)) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_06));
                            return;
                        }
                        BigDecimal money1 = Convert.toWei(mCurAllMoney, Convert.Unit.ETHER).subtract(new BigDecimal(mGasLimit.multiply(mGasPrice)));
                        String money;
                        if (money1.compareTo(new BigDecimal(0)) < 0) {
                            money = "0";
                        } else {
                            money = SlinUtil.formatNumFromWeiS(this, money1);
                        }

                        mEditMoney.setText(money);
                        mEditMoney.setSelection(money.length());
                        break;
                }
                break;
            case R.id.transfer_activity_fee_des://费用说明
                Intent goto_webView = new Intent(this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.transfer_activity_txt_10));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + DAppConstants.backUrlHou(this, 9));
                startActivity(goto_webView);
                break;
            case R.id.transfer_activity_img_scan_address:
                Intent it_gotoScan = new Intent(TransferActivity.this, ScaningActivity.class);
                it_gotoScan.putExtra(ScaningActivity.RESURE_TYPE, 2);
                startActivityForResult(it_gotoScan, REQUEST_CODE);
                break;
            case R.id.transfer_activity_img_select_address://跳转地址簿 选择地址
                Intent it_address = new Intent(TransferActivity.this, AddressBookActivity.class);
                it_address.putExtra(AddressBookActivity.ADDRESS_BOOK_SOURCE, 1);
                startActivityForResult(it_address, REQUEST_ADDRESS_CODE);
                break;
            case R.id.transfer_activity_img_token_type_up_down://选择币种类型
            case R.id.transfer_activity_token_type_container:
                showTokenTypeSelectDialog();
                break;
            case R.id.transfer_activity_img_token_name_up_down:// 选择代币
            case R.id.transfer_activity_token_name_container:
                showTokenTypeNameSelectDialog();
                break;
            case R.id.tv_select_token_id:// 选择代币ID
                String typeName = mTokenTypeName.getText().toString();

                if (TextUtils.isEmpty(typeName)) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_transfer_select_token_type));
                    return;
                }

                if (mTotalNum == 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_transfer_select_token_is_null));
                    return;
                }

                mContractAddress = TextUtils.isEmpty(mContractAddress) ? contractAddressMap.get(typeName) : mContractAddress;
                Intent data = new Intent(this, SelectTokenIdActivity.class);
                data.putExtra(SelectTokenIdActivity.CONTRACT_ADDRESS, mContractAddress);
                startActivityForResult(data, REQUEST_SELECT_TOKEN_ID_CODE);
                break;
        }
    }


    private void getNonce() {
        new GetNonceRequest(mDefaultAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                    showTransferInfoQrCode(mapEthInfo.getNonce().toString());
                }
            }
        });

    }


    private BigDecimal getMoney(String money) {
        BigDecimal moneyDecimal = null;
        if (numStyle == 0) {
            moneyDecimal = new BigDecimal(money.replace(",", ""));
        } else {
            if (DAppConstants.TYPE_HRC_20.equals(currentTokenType)) {
                moneyDecimal = new BigDecimal(money.replace(".", "").replace(",", "."));
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
                moneyDecimal = new BigDecimal(money00.replace(",", ""));
            }
        }

        return moneyDecimal;
    }

    private void showTransferInfoQrCode(String nonce) {
        TransferQrCodeInfoBean transferQrCodeInfoBean = new TransferQrCodeInfoBean();
        String data = "";
        String toAddress = mEditToAddress.getText().toString();
        String fromAddress = mTxtFormAddress.getText().toString();

        switch (currentTokenType) {
            case DAppConstants.TYPE_HRC_721:
                String tokenId = mTokenTokenId.getText().toString().trim();
                SystemLog.D("onSuccess", "tokenId = " + tokenId);
                BigInteger tokenIdValue = new BigInteger(tokenId);
                data = TransferUtils.getToken721TransferData(fromAddress, toAddress, tokenIdValue);
                transferQrCodeInfoBean.setCoin(currentTokenTypeName);
                transferQrCodeInfoBean.setContractAddress(mContractAddress);
                break;
            case DAppConstants.TYPE_HRC_20:
                BigDecimal money = null;
                if (numStyle != 0) {
                    String m = getMoney(moneyOrToken).toPlainString().replace(".", "-").replace(",", ".").replace("-", ",");
                    money = new BigDecimal(m);
                } else {
                    money = getMoney(moneyOrToken);
                }
                SystemLog.D("showTransferInfoQrCode", "money = " + money.toPlainString());
                BigInteger valueData = Convert.toWei(money, Convert.Unit.ETHER).toBigInteger();
                data = TransferUtils.getToken20TransferData(toAddress, valueData);
                transferQrCodeInfoBean.setCoin(currentTokenTypeName);
                transferQrCodeInfoBean.setContractAddress(mContractAddress);
                break;
            case DAppConstants.TYPE_HPB:
                transferQrCodeInfoBean.setCoin(DAppConstants.TYPE_HPB);
                break;
        }

        transferQrCodeInfoBean.setData(data);
        transferQrCodeInfoBean.setCointype(currentTokenType);
        transferQrCodeInfoBean.setFrom(fromAddress);
        transferQrCodeInfoBean.setTo(toAddress);
        transferQrCodeInfoBean.setGaslimt(mGasLimit.toString());
        transferQrCodeInfoBean.setGasprice(mGasPrice.toString());
        transferQrCodeInfoBean.setMoney(moneyOrToken);
        transferQrCodeInfoBean.setNonce(nonce);
//        String transferInfo = "[" + "\"0\"" + "," + JSON.toJSONString(transferQrCodeInfoBean) + "]";
        String transferInfo = ColdWalletUtil.toJson(ColdWalletUtil.TYPE_TRANSFER, transferQrCodeInfoBean);
        SystemLog.D("showTransferInfoQrCode", "transferInfo = " + transferInfo);

        View view = LayoutInflater.from(this).inflate(R.layout.view_digital_sign_qrcode_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(mRootLayout, Gravity.CENTER, 0, 0);

        ImageView ivDigitalSignQrCode = view.findViewById(R.id.iv_qr_code_info);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_qrcode_logo, null);
        CreateQrAsyncTask asyncTask = new CreateQrAsyncTask(this, ivDigitalSignQrCode, transferInfo, logo);
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
                // 下一步
                popupWindow.dismiss();
                // 跳转到扫码页面
                Intent scanIntent = new Intent(TransferActivity.this, ScaningActivity.class);
                scanIntent.putExtra(ScaningActivity.RESURE_TYPE, ScaningActivity.TYPE_TRANSFER);
                startActivityForResult(scanIntent, REQUEST_COLD_WALLET_TRANSFER);
                //finish();
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


    private void showTokenTypeNameSelectDialog() {
        if (CollectionUtil.isCollectionEmpty(tokenTypeDatas)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_transaction_record_title_03));
            return;
        }
        String tokenTypeName = mTokenTypeName.getText().toString();
        View view = LayoutInflater.from(this).inflate(R.layout.view_token_type_name_select_layout, null, false);
        EasyPickerViewWithSubText easyPickerViewWithSubText = view.findViewById(R.id.epv_h);
        Button submit = view.findViewById(R.id.token_type_select_btn_submit);
        TextView tvCancel = view.findViewById(R.id.token_type_select_cancel);
        CommonPopupWindowDialog commonPopupWindowDialog = new CommonPopupWindowDialog(this, view);
        if (!CollectionUtil.isCollectionEmpty(tokenTypeDatas)) {
            easyPickerViewWithSubText.setDataList(tokenTypeDatas, tokenNumDatas);
            if (!TextUtils.isEmpty(tokenTypeName)) {
                if (tokenTypeDatas.contains(tokenTypeName)) {
                    int index = tokenTypeDatas.indexOf(tokenTypeName);
                    easyPickerViewWithSubText.moveBy(index);
                }
            }

            commonPopupWindowDialog.show(mRootLayout);
        }

        easyPickerViewWithSubText.setOnScrollChangedListener(new EasyPickerViewWithSubText.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {
                currentTokenTypeName = tokenTypeDatas.get(curIndex);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonPopupWindowDialog.dismiss();
                if (TextUtils.isEmpty(currentTokenTypeName)) {
                    if (!CollectionUtil.isCollectionEmpty(tokenTypeDatas)) {
                        currentTokenTypeName = tokenTypeDatas.get(0);
                    }
                }

                mTokenTypeName.setText(currentTokenTypeName);
                mTransTokenName.setText(currentTokenTypeName);

                mCurrentTokenNum = tokenNumMap.get(currentTokenTypeName);
                mContractAddress = contractAddressMap.get(currentTokenTypeName);
                SystemLog.D("submit", "mCurrentTokenNum = " + mCurrentTokenNum);
                SystemLog.D("submit", "contractAddress = " + mContractAddress);
                // 如果已经选择过代币ID，再重新选择币种后需要将代币ID清空
                mTokenTokenId.setText("");
                mEditMoney.setText("");
                mEditMoney.setHint(getResources().getString(R.string.transfer_activity_txt_009) + (TextUtils.isEmpty(mCurrentTokenNum) ? "0" : mCurrentTokenNum));

                initTokenNumByContractAddress(mContractAddress);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonPopupWindowDialog.dismiss();
            }
        });

    }

    private void showTokenTypeSelectDialog() {
        String tokenType = mEtTokenType.getText().toString();
        ArrayList<String> datas = new ArrayList<>();
        datas.add(getResources().getString(R.string.main_token));
        datas.add(getResources().getString(R.string.hrc_20));
        datas.add(getResources().getString(R.string.hrc_721));
        // 如果弹出代币类型选择框而不选择，默认选择主链币
        currentTokenType = datas.get(0);
        View view = LayoutInflater.from(this).inflate(R.layout.view_token_select_layout, null, false);
        EasyPickerView easyPickerView = view.findViewById(R.id.epv_h);
        Button submit = view.findViewById(R.id.token_type_select_btn_submit);
        TextView tvCancel = view.findViewById(R.id.token_type_select_cancel);
        CommonPopupWindowDialog commonPopupWindowDialog = new CommonPopupWindowDialog(this, view);
        commonPopupWindowDialog.show(mRootLayout);

        easyPickerView.setDataList(datas);
        if (!TextUtils.isEmpty(tokenType)) {
            if (datas.contains(tokenType)) {
                int index = datas.indexOf(tokenType);
                easyPickerView.moveBy(index);
            }
        }

        easyPickerView.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {
                currentTokenType = datas.get(curIndex);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确定
                commonPopupWindowDialog.dismiss();
                if (!TextUtils.isEmpty(currentTokenType)) {
                    mEtTokenType.setText(currentTokenType);
                    if (getResources().getString(R.string.hrc_20).equals(currentTokenType) || getResources().getString(R.string.hrc_721).equals(currentTokenType)) {
                        mTokenTypeNameUpdown.setVisibility(View.VISIBLE);
                        mTokenTypeName.setText("");
                        mTransTokenName.setText("");
                        mTokenTokenId.setText("");
                    } else {
                        mTokenTypeNameUpdown.setVisibility(View.GONE);
                    }

                    if (getResources().getString(R.string.hrc_721).equals(currentTokenType)) {
                        mTransferAmountContainer.setVisibility(View.GONE);
                        mTransferTokenContainer.setVisibility(View.VISIBLE);
                    } else {
                        mTransferAmountContainer.setVisibility(View.VISIBLE);
                        mTransferTokenContainer.setVisibility(View.GONE);
                    }

                    if (DAppConstants.TYPE_HRC_20.equals(currentTokenType)) {
                        mTransferTitle.setText(getResources().getString(R.string.activity_transfer_num));
                    }

                    SystemLog.D("submit", "currentTokeType = " + currentTokenType.trim());
                    if (getResources().getString(R.string.main_token).equals(currentTokenType.trim())) {
                        // 币种默认是HPB
                        mTokenTypeName.setText(TransferActivity.this.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
                        mEditMoney.setText("");
                        mTransferTitle.setText(getResources().getString(R.string.total_activity_txt_new_03));
                        if (!StrUtil.isNull(SlinUtil.formatNumFromWeiT(TransferActivity.this, mCurrentHpbMoney, 18))) {
                            mEditMoney.setHint(getResources().getString(R.string.transfer_activity_txt_009) + SlinUtil.formatNumFromWeiT(TransferActivity.this, mCurrentHpbMoney, 18));
                        }
                    } else {
                        initTokenTypeName(currentTokenType);
                    }

                    if (StrUtil.isEmpty(mTokenTypeName.getText().toString())) {
                        mEditMoney.setText("");
                        mEditMoney.setHint("");
                    }
                }

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                commonPopupWindowDialog.dismiss();
            }
        });


    }

    /**
     * 初始化代币币种
     *
     * @param tokenType
     */
    private void initTokenTypeName(String tokenType) {
        if (!isLoading) {
            isLoading = true;
            if (isFirstLoad) {
                isFirstLoad = false;
                showProgressDialog();
            }
        }
        tokenNumDatas.clear();
        tokenTypeDatas.clear();
        contractAddressMap.clear();
        new WalletTransferRequest(mDefaultAddress, tokenType).doRequest(TransferActivity.this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    TokenTypeBean tokenTypeBean = JSON.parseObject(jsonArray.get(2).toString(), TokenTypeBean.class);
                    List<TokenTypeBean.TokenTypeInfo> tokenTypeBeanList = tokenTypeBean.getList();
                    if (!CollectionUtil.isCollectionEmpty(tokenTypeBeanList)) {
                        for (TokenTypeBean.TokenTypeInfo tokenTypeInfo : tokenTypeBeanList) {
                            String symbol = tokenTypeInfo.getSymbol();
                            tokenTypeDatas.add(symbol);
                            BigDecimal money = new BigDecimal(tokenTypeInfo.getTokenNum());
                            int decimals = tokenTypeInfo.getDecimals();
                            String tokenNum = SlinUtil.formatNumFromWeiT(TransferActivity.this, money, decimals);
                            tokenNumDatas.add(tokenNum);

                            contractAddressMap.put(symbol, tokenTypeInfo.getContractAddress());
                            tokenNumMap.put(symbol, tokenNum);
                        }
                    }
                }


                if (DAppConstants.TYPE_HRC_721.equals(transferType) || DAppConstants.TYPE_HRC_20.equals(transferType)) {
                    mContractAddress = contractAddressMap.get(mTokenSymbol);
                    mCurrentTokenNum = tokenNumMap.get(mTokenSymbol);

                    SystemLog.D("WalletTransferRequest", "mTokenSymbol = " + mTokenSymbol);
                    SystemLog.D("WalletTransferRequest", "contractAddress = " + mContractAddress);
                    SystemLog.D("WalletTransferRequest", "mCurrentTokenNum = " + mCurrentTokenNum);

                    initTokenNumByContractAddress(mContractAddress);

                    if (DAppConstants.TYPE_HRC_20.equals(transferType)) {
                        mEditMoney.setText("");
                        mEditMoney.setHint(getResources().getString(R.string.transfer_activity_txt_009) + (TextUtils.isEmpty(mCurrentTokenNum) ? "0" : mCurrentTokenNum));
                    }
                }

                if (CollectionUtil.isCollectionEmpty(tokenTypeDatas)) {
                    // 如果币种类型为空，隐藏下拉按钮
                    mTokenTypeNameUpdown.setVisibility(View.GONE);
                }

                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                isLoading = false;
                DappApplication.getInstance().showToast(error);
                dismissProgressDialog();
            }
        });
    }

    /**
     * 计算转账金额是否小于剩余金额
     *
     * @return
     */
    private boolean MoneyHandle() {
        String count = mEditMoney.getText().toString();
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
        if (money1.compareTo(Convert.toWei(mCurAllMoney, Convert.Unit.ETHER)) > 0) {
            return false;
        }
        return true;
    }

    private void submitTrade() {
        //判断地址
        String address = mEditToAddress.getText().toString();
        if (mDefaultAddress.toLowerCase().equals(address.toLowerCase())) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_00));
            return;
        }

        if (StrUtil.isEmpty(address)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_01));
            return;
        }

        if (!address.startsWith("0x") || address.length() != 42) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_02));
            return;
        }

        String fee = mTxtFeeMoney.getText().toString();
        if (StrUtil.isEmpty(fee)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_06));
            return;
        }

        if (DAppConstants.TYPE_HRC_721.equals(currentTokenType)) {
            moneyOrToken = mTokenTokenId.getText().toString();
            if (StrUtil.isEmpty(moneyOrToken)) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_transfer_select_token));
                return;
            }

            mCommonTransfgerConfirmDialog.setTransferTitleVisibility();
        } else {
            mCommonTransfgerConfirmDialog.setTokenTypeNameVisibility();
            moneyOrToken = mEditMoney.getText().toString();
            if (StrUtil.isEmpty(moneyOrToken)) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_03));
                return;
            }

            if (moneyOrToken.equals("0")) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_04));
                return;
            }

            if (DAppConstants.TYPE_HRC_20.equals(currentTokenType)) {
                if (numStyle == 0) {// 正常样式
                    BigDecimal enableTransferNum = new BigDecimal(mCurrentTokenNum.replace(",", "").trim());
                    BigDecimal transferNum = new BigDecimal(moneyOrToken.replace(",", "").trim());
                    if (transferNum.compareTo(enableTransferNum) > 0) {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_07));
                        return;
                    }
                } else {// 其他样式
                    BigDecimal enableTransferNum = new BigDecimal(mCurrentTokenNum.replace(".", "").replace(",", ".").trim());
                    BigDecimal transferNum = new BigDecimal(moneyOrToken.replace(".", "").replace(",", ".").trim());
                    if (transferNum.compareTo(enableTransferNum) > 0) {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_07));
                        return;
                    }
                }

            } else {

                if (numStyle == 0) {
                    if (moneyOrToken.contains(".")) {
                        String mm = moneyOrToken.replace(",", "").trim();
                        if (new BigDecimal(mm).compareTo(new BigDecimal("0")) == 0) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_04));
                            return;
                        }
                    }
                } else {
                    if (moneyOrToken.contains(",")) {
                        String mm = moneyOrToken.replace(".", "").trim();
                        if (new BigDecimal(mm.replace(",", "").trim()).compareTo(new BigDecimal("0")) == 0) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_04));
                            return;
                        }
                    }
                }

                if (!MoneyHandle()) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_05));
                    return;
                }
            }


        }

        if (mWalletBean.getIsClodWallet() != 0) {
            // 冷钱包
            getNonce();
        } else {
            //显示详情
            mCommonTransfgerConfirmDialog.setTokenTypeName(mTransTokenName.getText().toString());
            mCommonTransfgerConfirmDialog.show(moneyOrToken, mTxtFeeMoney.getText().toString(), mEditToAddress.getText().toString(), numStyle);
        }

    }

    private void getNonce(String privateKey, String money) {
        showProgressDialog();
        new GetNonceRequest(mDefaultAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                String status = (String) jsonArray.get(0);
                BigInteger nonce = null;
                if ("000000".equals(status)) {//代表处理成功
                    MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                    nonce = mapEthInfo.getNonce();
                    String toAddress = mEditToAddress.getText().toString();
                    String fromAddress = mTxtFormAddress.getText().toString();
                    BigDecimal money01 = getMoney(money);
                    SystemLog.D("onSuccess", "mContractAddress = " + mContractAddress);
                    SystemLog.D("onSuccess", "money01 = " + money01.toPlainString());

                    switch (currentTokenType) {
                        case DAppConstants.TYPE_HRC_20://20
                            handleToken20Transfer(
                                    mContractAddress
                                    , toAddress
                                    , money01
                                    , privateKey
                                    , nonce);
                            break;
                        case DAppConstants.TYPE_HRC_721://721
                            String tokenId = mTokenTokenId.getText().toString().trim();
                            SystemLog.D("onSuccess", "tokenId = " + tokenId);
                            BigInteger tokenIdValue = new BigInteger(tokenId);
                            handleToken721Transfer(
                                    mContractAddress
                                    , fromAddress
                                    , toAddress
                                    , tokenIdValue
                                    , privateKey
                                    , nonce);
                            break;
                        default://HPB
                            handleSignTransaction(toAddress, money01, privateKey, nonce);
                            break;
                    }

                }

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
        BigInteger valueData = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
        String data = "";
        int chainId = ChainId.MAINNET;//测试网络
        //私钥
        String signedData = "";
        try {
            //
            signedData = TransferUtils.signTransaction(nonce, mGasPrice, mGasLimit, toAddress.toLowerCase(), valueData, data, chainId, privateKey);

            new CreateTradeRequest(signedData).doRequest(this, new CommonResultListener(this) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    super.onSuccess(jsonArray);
                    dismissProgressDialog();
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_main_map_txt_06));
                    finish();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * HRC-20转账
     *
     * @param toAddress
     * @param value
     * @param privateKey
     * @param nonce
     */
    private void handleToken20Transfer(String contractAddress, String toAddress, BigDecimal value, String privateKey, BigInteger nonce) {
        //交易金额
        BigInteger valueData = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
        //私钥
        String signedData = TransferUtils.token20Transfer(nonce, mGasPrice, mGasLimit, contractAddress.toLowerCase(), toAddress.toLowerCase(), valueData, privateKey);
        new CreateTradeRequest(signedData).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                dismissProgressDialog();
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_main_map_txt_06));
                finish();
            }
        });

    }


    /**
     * HRC-721 tokenId转移
     *
     * @param contractAddress
     * @param fromAddress
     * @param toAddress
     * @param tokenIdValue
     * @param privateKey
     * @param nonce
     */
    private void handleToken721Transfer(String contractAddress, String fromAddress, String toAddress, BigInteger tokenIdValue, String privateKey, BigInteger nonce) {
        //私钥
        String signedData = TransferUtils.token721Transfer(nonce, mGasPrice, mGasLimit, contractAddress.toLowerCase(), fromAddress.toLowerCase(), toAddress.toLowerCase(), tokenIdValue, privateKey);
        new CreateTradeRequest(signedData).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                dismissProgressDialog();
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_main_map_txt_06));
                finish();
            }
        });

    }

    /**
     * 弹出输入密码框
     */
    private void showDialog(String money) {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this, mRootLayout);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                //获取私钥
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(TransferActivity.this, mDefaultAddress, psd, 10);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == ScaningActivity.RESULT_CODE && data != null) {
            String resultContent = data.getStringExtra(ScaningActivity.RESULT_CONTENT);
            mEditToAddress.setText(resultContent);
            mEditToAddress.setSelection(resultContent.length());
        } else if (requestCode == REQUEST_ADDRESS_CODE && resultCode == RESULT_OK && data != null) {
            String resultContent = data.getStringExtra(AddressBookActivity.RESULT_CONTENT);
            mEditToAddress.setText(resultContent);
            mEditToAddress.setSelection(resultContent.length());
        } else if (requestCode == REQUEST_SELECT_TOKEN_ID_CODE && resultCode == RESULT_OK && data != null) {
            String tokenIds = data.getStringExtra(SelectTokenIdActivity.RESULT_TOKEN_IDS);
            mTokenTokenId.setText(tokenIds);
            mBtnSubmit.setEnabled(true);
            mBtnSubmit.setTextColor(getResources().getColor(R.color.white));
            mBtnSubmit.setBackgroundResource(R.drawable.bg_receive_btn_bg);
        } else if (requestCode == REQUEST_COLD_WALLET_TRANSFER && resultCode == RESULT_OK && data != null) {
            mSignContent = data.getStringExtra(RESULT_COLD_WALLET_TRANSFER);
            SystemLog.D("onActivityResult", "resultContent = " + mSignContent);
            mCommonTransfgerConfirmDialog.setTokenTypeName(mTransTokenName.getText().toString());
            mCommonTransfgerConfirmDialog.show(moneyOrToken, mTxtFeeMoney.getText().toString(), mEditToAddress.getText().toString(), numStyle);

        }
    }
}
