package com.zhaoxi.Open_source_Android.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 倒计时TextView Created by zhangtao
 */
@SuppressLint("AppCompatCustomView")
public class CountDownView extends LinearLayout implements Runnable {
    private static final String TAG = "CountDownTextView";
    private Context mContext;
    private long lastTimeSecond;
    private boolean isAttachedTimes = false;
    private float textSize;
    private int numTextColor;
    private float textColot;

    @BindView(R.id.tv_day)
    TextView mTvDay;
    @BindView(R.id.tv_hour)
    TextView mTvHour;
    @BindView(R.id.tv_minute)
    TextView mTvMinute;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        textSize = typedArray.getDimension(R.styleable.CountDownView_cd_textSize, 12.0f);
        numTextColor = typedArray.getColor(R.styleable.CountDownView_cd_numTextColor, Color.WHITE);
        textColot = typedArray.getColor(R.styleable.CountDownView_cd_textColor, Color.WHITE);

        typedArray.recycle();

        initView(context);
    }

    private void initView(Context context) {
        View containerView = LayoutInflater.from(context).inflate(R.layout.view_count_down_layout, this);
        ButterKnife.bind(this, containerView);
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
        mTvDay.setText(String.valueOf(times[0]));
        mTvHour.setText(String.valueOf(times[1]));
        mTvMinute.setText(String.valueOf(times[2]));
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
