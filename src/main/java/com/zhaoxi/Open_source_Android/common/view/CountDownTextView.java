package com.zhaoxi.Open_source_Android.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;

/**
 * 倒计时TextView Created by zhangtao
 */
@SuppressLint("AppCompatCustomView")
public class CountDownTextView extends TextView implements Runnable {
    private static final String TAG = "CountDownTextView";
    private Context mContext;
    private long lastTimeSecond;
    private boolean isAttachedTimes = false;

    public CountDownTextView(Context context) {
        this(context, null);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * 设置时间
     *
     * @param lastTimeSecond 最后时间
     */
    public void setTimes(long lastTimeSecond) {
        if (!isAttachedTimes) {
            isAttachedTimes = true;
            this.lastTimeSecond = lastTimeSecond;
            postDelayed(this, 0);
        }
    }

    /**
     * 开始刷新时间
     */
    public void startRefreshTime() {
        long[] times = DateUtilSL.calculateCountDown(lastTimeSecond);
        String strTime = "<font color='#FFFFFF'>" + times[0] + "</font><font color='#C0C0C0' >" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_03)
                + "</font><font color='#FFFFFF'>" + times[1] + "</font><font color='#C0C0C0'>" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_04)
                + "</font><font color='#FFFFFF'>" + times[2] + "</font><font color='#C0C0C0' >" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_05)
                + "</font>";
        setText(Html.fromHtml(strTime));
    }

    @Override
    public void run() {
        startRefreshTime();
        postDelayed(this, 60 * 1_000);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        SystemLog.D(TAG, "onAttachedToWindow()");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SystemLog.D(TAG, "onDetachedFromWindow()");
        isAttachedTimes = false;
        removeCallbacks(this);
    }

}
