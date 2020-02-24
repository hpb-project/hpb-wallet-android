package com.zhaoxi.Open_source_Android.ui.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.ProgressHoriWebView;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.NetUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 展示Html页面
 *
 * @author zhutt on 2018-06-07
 */
public class CommonWebActivity extends BaseTitleBarActivity {
    public static final String ACTIVITY_TITLE_INFO = "CommonWebActivity.ACTIVITY_TITLE_INFO";
    public static final String WEBVIEW_LOAD_URL = "CommonWebActivity.WEBVIEW_LOAD_URL";

    @BindView(R.id.common_web_view_webview)
    ProgressHoriWebView mWebView;
    @BindView(R.id.common_web_view_load_error)
    LinearLayout mLayoutError;

    private String mTitle;
    private String mLoadUrl;
    private ChangeLanguageUtil mChangeLanguageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        ButterKnife.bind(this);
        mChangeLanguageUtil = new ChangeLanguageUtil();

        mTitle = getIntent().getStringExtra(ACTIVITY_TITLE_INFO);
        mLoadUrl = getIntent().getStringExtra(WEBVIEW_LOAD_URL);
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

        WebSettings setting = mWebView.getSettings();
        mWebView.setWebViewClient(mWebViewClient);
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

        loadUrl();
    }

    @Override
    protected void onPause() {
        super.onPause();
        int value = SharedPreferencesUtil.getSharePreInt(this,SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
        mChangeLanguageUtil.init(this,value,false);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mWebView.setVisibility(View.VISIBLE);
            mLayoutError.setVisibility(View.GONE);
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
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    private Handler mHandler = new Handler();
    public Object webAddJsObject = new Object() {
        @JavascriptInterface
        public void goback() {
            mHandler.post(
                    new Runnable() {//返回
                        public void run() {
                            finish();
                        }
                    });
        }
    };

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

    private void loadUrl(){
        //判断网络是否连接
        if(!NetUtil.isNetworkAvalible(DappApplication.getInstance())){
            mWebView.setVisibility(View.VISIBLE);
            mLayoutError.setVisibility(View.GONE);
            return;
        }
        mWebView.loadUrl(mLoadUrl);
    }
}
