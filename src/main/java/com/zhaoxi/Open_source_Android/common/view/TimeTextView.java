package com.zhaoxi.Open_source_Android.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * Created by zhutt on 2016/11/28.
 */
@SuppressLint("AppCompatCustomView")
public class TimeTextView extends TextView implements Runnable {
    Paint mPaint; //画笔,包含了画几何图形、文本等的样式和颜色信息
    private long[] times;
    private long mday, mhour, mmin;//天，小时，分钟
    private boolean isRun = false; //是否启动了
    private OnRunEndListener mEndListener;
    private Context mContext;

    public TimeTextView(Context context) {
        this(context, null);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);
        array.recycle(); //一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public void setOnRunEndListener(OnRunEndListener listener) {
        mEndListener = listener;
    }

    public interface OnRunEndListener {
        void end();
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        if(mmin > 0 || mhour > 0 || mday > 0){
            mmin--;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mhour = 24;
                    mday--;
                }
            }
            if (mmin == 0 && mhour == 0 && mday == 0) {
                isRun = false;
                if (mEndListener == null) {
                    mEndListener.end();
                }
            }
        }else{
//            if (mmin == 0 && mhour == 0 && mday == 0) {
                isRun = false;
                if (mEndListener == null) {
                    mEndListener.end();
                }
//            }
        }
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        this.isRun = run;
    }

    @Override
    public void run() {
        //标示已经启动
        if (isRun) {
            ComputeTime();
            String strTime = "<font color='#FFFFFF'>" + mday + "</font><font color='#C0C0C0' >" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_03)
                    + "</font><font color='#FFFFFF'>" + mhour + "</font><font color='#C0C0C0'>" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_04)
                    + "</font><font color='#FFFFFF'>" + mmin + "</font><font color='#C0C0C0' >" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_05)
                    + "</font>";
            this.setText(Html.fromHtml(strTime));
            postDelayed(this, 1000 * 60);
        }
    }
}
