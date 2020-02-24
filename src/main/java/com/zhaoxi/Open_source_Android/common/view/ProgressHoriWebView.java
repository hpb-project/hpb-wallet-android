package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;

/**
 * des:
 * Created by ztt on 2019/1/7.
 */

public class ProgressHoriWebView extends WebView {
//    private ScrollWebView.OnScrollChangeListener mOnScrollChangeListener;
    private ProgressBar progressBar;

    public ProgressHoriWebView(Context context) {
        this(context, null);
    }

    public ProgressHoriWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressHoriWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        /*设置进度条属性*/
        progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.webview_progress_hori));
        /*设置ProgressBar的布局参数*/
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 8, 0);
        /*绑定参数*/
        progressBar.setLayoutParams(layoutParams);
       /* 将ProgressBar添加到WebView上 默认头部*/
        addView(progressBar);

        /*设置WebView辅助类WebChromeClient,获取实时加载进度*/
        setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (progress == 100)
                    progressBar.setVisibility(View.GONE);
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                DappApplication.getInstance().showToast(message);
                result.confirm();
                return true;
            }
        });
    }

//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//        // webview的高度
//        float webcontent = getContentHeight() * getScale();
//        // 当前webview的高度
//        float webnow = getHeight() + getScrollY();
//        if (Math.abs(webcontent - webnow) < 1) {
//            //处于底端
//            mOnScrollChangeListener.onPageEnd(l, t, oldl, oldt);
//        } else if (getScrollY() == 0) {
//            //处于顶端
//            mOnScrollChangeListener.onPageTop(l, t, oldl, oldt);
//        } else {
//            mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
//        }
//    }
//
//    public void setOnScrollChangeListener(ScrollWebView.OnScrollChangeListener listener) {
//        this.mOnScrollChangeListener = listener;
//    }
//
//    public interface OnScrollChangeListener {
//
//        public void onPageEnd(int l, int t, int oldl, int oldt);
//
//        public void onPageTop(int l, int t, int oldl, int oldt);
//
//        public void onScrollChanged(int l, int t, int oldl, int oldt);
//
//    }
}
