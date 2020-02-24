package com.zhaoxi.Open_source_Android.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.DappsLoadinfBean;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPsdPop;
import com.zhaoxi.Open_source_Android.common.dialog.CommonThreeLoadingDialog;
import com.zhaoxi.Open_source_Android.common.dialog.CommonTransferOtherConfirmDialog;
import com.zhaoxi.Open_source_Android.common.view.ProgressHoriWebView;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.NetUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.CreateTradeRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetHpbFeeRequest;
import com.zhaoxi.Open_source_Android.net.Request.MerchantCallBackRequest;
import com.zhaoxi.Open_source_Android.net.Request.TotalHpbRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.web3.tx.ChainId;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 展示Dapps Html页面
 *
 * @author zhutt on 2018-06-07
 */
public class DappsWebActivity extends BaseTitleBarActivity implements DownloadListener {
    public static final String ACTIVITY_TITLE_INFO = "DappsWebActivity.ACTIVITY_TITLE_INFO";
    public static final String WEBVIEW_LOAD_URL = "DappsWebActivity.WEBVIEW_LOAD_URL";
    public static final String WEBVIEW_INTENT_OUT = "DappsWebActivity.WEBVIEW_INTENT_OUT";

    @BindView(R.id.common_web_view_webview)
    ProgressHoriWebView mWebView;
    @BindView(R.id.common_web_view_load_error)
    LinearLayout mLayoutError;
    @BindView(R.id.root_dapps_web)
    LinearLayout mRootView;

    private String mTitle, mLoadUrl, mAddress, mStrFee, mLanguage, mDefultAddress = "", mMerchantParams;
    private ChangeLanguageUtil mChangeLanguageUtil;
    private BigDecimal mCurMoney = new BigDecimal("0");
    private BigInteger mGasPrice, mGasLimit, mNonce;
    private boolean isIntentOut, mIsColdWallet;
    private final int version = Build.VERSION.SDK_INT;
    private CommonThreeLoadingDialog mCommonThreeLoadingDialog;
    private CommonTransferOtherConfirmDialog mCommonTransferOtherConfirmDialog;
    private CreateDbWallet mCreateDbWallet;
    private boolean isLoginSuccse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        ButterKnife.bind(this);
        mChangeLanguageUtil = new ChangeLanguageUtil();
        initViews();
        initDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int type = mChangeLanguageUtil.languageType(this);
        if (type == 1) {
            mLanguage = "cn";
        } else {
            mLanguage = "en";
        }
    }

    private void initDatas() {
        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mCreateDbWallet = new CreateDbWallet(this);
        mIsColdWallet = mCreateDbWallet.isColdWallet(DappsWebActivity.this, mDefultAddress);
        loadUrl();
    }

    private void initViews() {
        mTitle = getIntent().getStringExtra(ACTIVITY_TITLE_INFO);
        mAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mLoadUrl = getIntent().getStringExtra(WEBVIEW_LOAD_URL);
        isIntentOut = getIntent().getBooleanExtra(WEBVIEW_INTENT_OUT, false);
        if (StrUtil.isEmpty(mAddress)) {
            finish();
        }
        setTitle(mTitle);
        setImgBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        if (isIntentOut) {
            showRightImgWithImgListener(R.mipmap.icon_go_out, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(mLoadUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }

        leftTxtWithTextListener(getResources().getString(R.string.web_dapps_title_close),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        WebSettings setting = mWebView.getSettings();
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setDownloadListener(this);
        setting.setJavaScriptEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);
        // 全屏显示
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        //兼容https Url 里面配置http资源
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        int sytemVersion = Build.VERSION.SDK_INT;
        if (sytemVersion > 11)
            mWebView.removeJavascriptInterface("searchBoxJavaBredge_");
        if (sytemVersion >= 17)
            mWebView.addJavascriptInterface(webAddJsObject, "android");
    }

    @Override
    protected void onPause() {
        super.onPause();
        int value = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
        mChangeLanguageUtil.init(this, value, false);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mWebView.setVisibility(View.VISIBLE);
            mLayoutError.setVisibility(View.GONE);
            if (mWebView.canGoBack()) {
                //显示关闭按钮
                hideOrShowLeftText(true);
            } else {
                //隐藏关闭按钮
                hideOrShowLeftText(false);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.cancel();
            mWebView.setVisibility(View.GONE);
            mLayoutError.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            SystemLog.I("DappsWebActivity", "url:" + url);
            if (isLoginSuccse && url.equals(mLoadUrl)) {
                finish();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    };
    private WebChromeClient mWebchromeclient = new WebChromeClient() {
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (url != null && url.contains("dlmzt8ygp7jdw.cloudfront.net")) {
                final LayoutInflater inflater = LayoutInflater.from(DappsWebActivity.this);
                final View myview = inflater.inflate(R.layout.view_common_web_input_dialog, null);
                ((EditText) myview.findViewById(R.id.common_web_edit_name)).setText(defaultValue);
                new AlertDialog.Builder(DappsWebActivity.this).setTitle(message).setView(myview)
                        .setPositiveButton(getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String value = ((EditText) myview.findViewById(R.id.common_web_edit_name)).getText().toString();
                                result.confirm(value);
                            }
                        }).setNegativeButton(getResources().getString(R.string.dailog_psd_btn_cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).show();
                return true;
            } else return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            DappApplication.getInstance().showToast(message);
            result.confirm();
            return true;
        }
    };

    private Handler mHandler = new Handler();
    public Object webAddJsObject = new Object() {
        /**
         * Dapps 获取当前账户地址
         */
        @JavascriptInterface
        public void getAccount() {
            mHandler.post(
                    new Runnable() {//返回
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        public void run() {
                            Map<String, Object> data = new HashMap<>();
                            data.put("account", mDefultAddress);
                            data.put("language", mLanguage);
                            data.put("type", "getAccount");
                            String json = JSON.toJSONString(data);
                            if (!StrUtil.isEmpty(json)) {
                                if (version < 18) {
                                    mWebView.loadUrl("javascript:window.hpbweb3.getCallback(" + json + ")");
                                } else {
                                    mWebView.evaluateJavascript("javascript:window.hpbweb3.getCallback(" + json + ")", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            SystemLog.I("ztt", "onReceiveValue:" + value);
                                        }
                                    });
                                }
                            } else {
                                DappApplication.getInstance().showToast(getResources().getString(R.string.main_find_dapps_txt_04));
                            }
                        }
                    });
        }

        /**
         * Dapps 请求登录授权
         * @param params
         */
        @JavascriptInterface
        public void signToLogin(String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mIsColdWallet) {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.cold_wallet_txt_12));
                        return;
                    }
                    if (isLoginSuccse) return;
                    if (!StrUtil.isEmpty(params)) {
                        DappsLoadinfBean loadinfBean;
                        try {
                            loadinfBean = JSON.parseObject(params, DappsLoadinfBean.class);
                            mCommonThreeLoadingDialog = new CommonThreeLoadingDialog(DappsWebActivity.this, mCommonToobar);
                            mCommonThreeLoadingDialog.setOnSubmitListener(new CommonThreeLoadingDialog.OnSubmitListener() {
                                @Override
                                public void setOnSubmitListener(String address) {
                                    showPsdDialog(1, address, loadinfBean);
                                }
                            });
                            mCommonThreeLoadingDialog.show(loadinfBean.getDappName(), loadinfBean.getDappIcon());
                        } catch (Exception e) {
                            DappApplication.getInstance().showToast(e.getMessage());
                        }

                    } else {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.main_find_dapps_txt_04));
                    }
                }
            });
        }

        /**
         * Dapps 请求支付
         * @param params
         */
        @JavascriptInterface
        public void startToPay(String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mIsColdWallet) {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.cold_wallet_txt_12));
                        return;
                    }
                    if (!StrUtil.isEmpty(params)) {
                        mMerchantParams = params;
                        DappsLoadinfBean loadinfBean;
                        try {
                            loadinfBean = JSON.parseObject(params, DappsLoadinfBean.class);
                            getAcouuntInfo(loadinfBean);
                        } catch (Exception e) {
                            DappApplication.getInstance().showToast(e.getMessage());
                        }

                    } else {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.main_find_dapps_txt_04));
                    }

                }
            });
        }

        @JavascriptInterface
        public void sendTransaction(String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mIsColdWallet) {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.cold_wallet_txt_12));
                        return;
                    }
                    if (!StrUtil.isEmpty(params)) {
                        showProgressDialog();
                        DappsLoadinfBean loadinfBean;
                        try {
                            loadinfBean = JSON.parseObject(params, DappsLoadinfBean.class);
                            new GetHpbFeeRequest(mDefultAddress).doRequest(DappsWebActivity.this, new CommonResultListener(DappsWebActivity.this) {
                                @Override
                                public void onSuccess(JSONArray jsonArray) {
                                    super.onSuccess(jsonArray);
                                    MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                                    mNonce = mapEthInfo.getNonce();
                                    showPsd2Dialog(mDefultAddress,loadinfBean);
                                    dismissProgressDialog();
                                }
                            });

                        } catch (Exception e) {
                            DappApplication.getInstance().showToast(e.getMessage());
                        }
                    } else {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.main_find_dapps_txt_04));
                    }

                }
            });
        }
    };

    /**
     * 获取账户基本信息
     *
     * @param loadinfBean
     */
    private void getAcouuntInfo(DappsLoadinfBean loadinfBean) {
        showProgressDialog();
        //step1:验证白名单
//        new VitertifyThreeRequest(loadinfBean.getTo()).doRequest(this, new CommonResultListener(this) {
//            @Override
//            public void onSuccess(JSONArray jsonArray) {
//                super.onSuccess(jsonArray);
//                String status = jsonArray.get(2).toString();
//                if ("1".equals(status)) {
        //step2:白名单验证成功 获取金额/gas Nonce
        new TotalHpbRequest(mDefultAddress).doRequest(DappsWebActivity.this, new CommonResultListener(DappsWebActivity.this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                mCurMoney = new BigDecimal("" + jsonArray.get(2));
                //获取gasLimit gasPrice nonce
                new GetHpbFeeRequest(mDefultAddress).doRequest(DappsWebActivity.this, new CommonResultListener(DappsWebActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        super.onSuccess(jsonArray);
                        MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                        mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                        mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                        mNonce = mapEthInfo.getNonce();
                        mStrFee = SlinUtil.FeeValueString(DappsWebActivity.this, mGasPrice, mGasLimit);
                        mCommonTransferOtherConfirmDialog = new CommonTransferOtherConfirmDialog(DappsWebActivity.this, mCommonToobar);
                        mCommonTransferOtherConfirmDialog.setOnSubmitListener(new CommonTransferOtherConfirmDialog.OnSubmitListener() {

                            @Override
                            public void setOnSubmitListener(String address) {
                                //step3:签名
                                showPsdDialog(2, address, loadinfBean);
                            }
                        });
                        mCommonTransferOtherConfirmDialog.show(mCurMoney, loadinfBean.getAmount(), mStrFee, loadinfBean.getTo(), loadinfBean.getDesc());
                        dismissProgressDialog();
                    }
                });
            }
        });
//                } else {
//                    dismissProgressDialog();
//                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_three_txt_06));
//                }
//            }
//        });
    }

    /**
     * 输入密码框
     */
    private void showPsd2Dialog(String adddress, DappsLoadinfBean loadinfBean) {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this,mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                //获取私钥
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(DappsWebActivity.this, adddress, psd, 10);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                        } else {
                            handleSignDataTransaction(loadinfBean, loadinfBean.getAmount(),
                                    new BigDecimal(loadinfBean.getAmount()), result, mNonce);
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        commonPsdPop.show(getResources().getString(R.string.transfer_three_txt_04));
    }

    /**
     * 输入密码框
     */
    private void showPsdDialog(int type, String adddress, DappsLoadinfBean loadinfBean) {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this,mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                //获取私钥
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(DappsWebActivity.this, adddress, psd, 10);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                        } else {
                            if (type == 1) {
                                String timestamp = DateUtilSL.getCurTimestamp();
                                String data = timestamp + adddress + loadinfBean.getUuID() + getResources().getString(R.string.app_name);//ref为钱包名，标示来源
                                String signData = TransferUtils.LoginSign(data, result);
                                if (!StrUtil.isEmpty(signData)) {
                                    submitLoading(adddress, signData, timestamp, loadinfBean);
                                } else {
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_three_txt_03));
                                }
                            } else {
                                handleSignTransaction(loadinfBean, loadinfBean.getAmount(),
                                        new BigDecimal(loadinfBean.getAmount()), result, mNonce,
                                        loadinfBean.getOrderId(), loadinfBean.getNotifyUrl(), loadinfBean.getDesc());
                            }
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        commonPsdPop.show(getResources().getString(R.string.transfer_three_txt_04));
    }

    /**
     * 签名交易 含data
     * @param loadinfBean
     * @param payMoney
     * @param value
     * @param privateKey
     * @param nonce
     */
    private void handleSignDataTransaction(DappsLoadinfBean loadinfBean, String payMoney, BigDecimal value,
                                           String privateKey, BigInteger nonce) {
        BigInteger valueData = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
        String signedData = "";
        int chainId = ChainId.MAINNET;//测试网络

        try {
            signedData = TransferUtils.signTransaction(nonce, new BigInteger(loadinfBean.getGasPrice()), new BigInteger(loadinfBean.getGas()),
                    loadinfBean.getTo(), valueData, loadinfBean.getData(), chainId, privateKey);
            new CreateTradeRequest(signedData).doRequest(this, new CommonResultListener(this) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    super.onSuccess(jsonArray);
                    String hash = jsonArray.get(2).toString();
                    if (!StrUtil.isEmpty(hash)) {
                        submit2Pay(hash);
                    } else {
                        dismissProgressDialog();
                        DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_three_txt_05));
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 签名交易
     *
     * @param loadinfBean Dapps参数
     * @param value       交易金额
     * @param privateKey  私钥
     * @param nonce
     */
    private void handleSignTransaction(DappsLoadinfBean loadinfBean, String payMoney, BigDecimal value,
                                       String privateKey, BigInteger nonce, String orderId,
                                       String notifyUrl, String des) {
        BigInteger valueData = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
        String signedData = "";
        String order = StrUtil.isEmpty(orderId) ? "" : orderId;
        String url = StrUtil.isEmpty(notifyUrl) ? "" : notifyUrl;
        String desc = StrUtil.isEmpty(des) ? "" : des;
        signedData = TransferUtils.ThreePayMethod(nonce, mGasPrice, mGasLimit, privateKey,
                loadinfBean.getTo().toLowerCase(), valueData, order, url, desc);
        if (true) {
            new CreateTradeRequest(signedData).doRequest(this, new CommonResultListener(this) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    super.onSuccess(jsonArray);
                    showProgressDialog();
                    String hash = jsonArray.get(2).toString();
                    if (!StrUtil.isEmpty(hash)) {
                        new MerchantCallBackRequest(hash, loadinfBean.getNotifyUrl(), loadinfBean.getOrderId())
                                .doRequest(DappsWebActivity.this, new CommonResultListener(DappsWebActivity.this) {
                                    @Override
                                    public void onError(String error) {
                                    }
                                });
                        submitPay(hash, payMoney, loadinfBean);
                    } else {
                        dismissProgressDialog();
                        DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_three_txt_05));
                    }
                }
            });
        } else {
            if (!StrUtil.isEmpty(signedData)) {
                submitPay(signedData, payMoney, loadinfBean);
            } else {
                dismissProgressDialog();
                DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_three_txt_03));
            }
        }
    }

    /**
     * Dapps 登录授权
     *
     * @param adddress    登录授权地址
     * @param signData    登录授权签名
     * @param timestamp   当前时间戳
     * @param loadinfBean Dapps参数
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void submitLoading(String adddress, String signData, String timestamp, DappsLoadinfBean loadinfBean) {
        showProgressDialog();
        Map<String, Object> data = new HashMap<>();
        data.put("protocol", loadinfBean.getProtocol());
        data.put("version", loadinfBean.getVersion());
        data.put("blockchain", loadinfBean.getBlockchain());
        data.put("timestamp", timestamp);
        data.put("sign", signData);
        data.put("action", "login");
        data.put("uuID", loadinfBean.getUuID());
        data.put("account", adddress);
        data.put("ref", getResources().getString(R.string.app_name));

        String json = JSON.toJSONString(data);
        if (!StrUtil.isEmpty(json)) {
            if (version < 18) {
                mWebView.loadUrl("javascript:window.hpbweb3.getCallback(" + json + ")");
            } else {
                mWebView.evaluateJavascript("javascript:window.hpbweb3.getCallback(" + json + ")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        isLoginSuccse = true;
                        SystemLog.I("ztt", "onReceiveValue:" + value);
                    }
                });
            }
        } else {
            DappApplication.getInstance().showToast(getResources().getString(R.string.main_find_dapps_txt_04));
        }
        dismissProgressDialog();
    }

    /**
     * Dapps 第三方支付
     *
     * @param loadinfBean Dapps参数
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void submitPay(String signData, String paymoney, DappsLoadinfBean loadinfBean) {
        Map<String, Object> data = new HashMap<>();
        data.put("result", "1");
        data.put("protocol", loadinfBean.getProtocol());
        data.put("version", loadinfBean.getVersion());
        data.put("blockchain", loadinfBean.getBlockchain());
        data.put("amount", paymoney);
        if (loadinfBean.isSend()) {
            data.put("txID", signData);
            data.put("signature", "");
        } else {
            data.put("txID", "");
            data.put("signature", signData);
        }
        data.put("action", "pay");

        String json = JSON.toJSONString(data);
        if (!StrUtil.isEmpty(json)) {
            if (version < 18) {
                mWebView.loadUrl("javascript:window.hpbweb3.getCallback(" + json + ")");
            } else {
                mWebView.evaluateJavascript("javascript:window.hpbweb3.getCallback(" + json + ")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        SystemLog.I("ztt", "onReceiveValue:" + value);
                    }
                });
            }
        } else {
            DappApplication.getInstance().showToast(getResources().getString(R.string.main_find_dapps_txt_04));
        }
        dismissProgressDialog();
    }



    /**
     * Dapps 第三方支付
     *
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void submit2Pay(String haxh) {
        Map<String, Object> data = new HashMap<>();
        data.put("action", "pay");
        data.put("txID", haxh);

        String json = JSON.toJSONString(data);
        if (!StrUtil.isEmpty(json)) {
            if (version < 18) {
                mWebView.loadUrl("javascript:window.hpbweb3.getCallback(" + json + ")");
            } else {
                mWebView.evaluateJavascript("javascript:window.hpbweb3.getCallback(" + json + ")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        SystemLog.I("ztt", "onReceiveValue:" + value);
                    }
                });
            }
        } else {
            DappApplication.getInstance().showToast(getResources().getString(R.string.main_find_dapps_txt_04));
        }
        dismissProgressDialog();
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @OnClick(R.id.common_web_view_load_again)
    public void onViewClicked() {
        loadUrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    private void loadUrl() {
        //判断网络是否连接
        if (!NetUtil.isNetworkAvalible(DappApplication.getInstance())) {
            mWebView.setVisibility(View.VISIBLE);
            mLayoutError.setVisibility(View.GONE);
            return;
        }
        SystemLog.I("DappsWebActivity", "fisrt url:" + mLoadUrl);
        mWebView.loadUrl(mLoadUrl);
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
