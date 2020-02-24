package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.zhaoxi.Open_source_Android.dapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 百分比进度条
 * create by fangz
 * create date:2019/8/22
 * create time:14:18
 */
public class PercentProgressBar extends FrameLayout {
    // 进度容器
    @BindView(R.id.progress_container)
    FrameLayout progressContainer;
    // 支持进度条
    @BindView(R.id.progress_support)
    ProgressBar progressBarSupport;
    // 不支持进度条
    @BindView(R.id.progress_un_support)
    ProgressBar progressBarUnSupport;

    // 百分比进度条样式
    private int style;

    // 支持与不支持都显示时，支持部分显示的进度条
    private Drawable drawableSupportProgressNormal;
    // 只显示不支持进度条时，显示的进度条
    private Drawable drawableUnSupportProgressNormal;
    // 支持与不支持都显示时，不支持部分显示的进度条
    private Drawable drawableUnSupportProgressReverse;
    // 支持率大于等于100%时显示的进度条
    private Drawable drawableSupportProgressFill;
    // 反对率大于等于100%时显示的进度条
    private Drawable drawableUnSupportProgressFill;


    public PercentProgressBar(Context context) {
        this(context, null);
    }

    public PercentProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PercentProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View parentLayout = LayoutInflater.from(context).inflate(R.layout.percent_progress_bar_layout, this);
        ButterKnife.bind(this, parentLayout);
        drawableSupportProgressNormal = ContextCompat.getDrawable(context, R.drawable.drawable_vote_support_progress_normal);
        drawableUnSupportProgressNormal = ContextCompat.getDrawable(context, R.drawable.drawable_vote_un_support_progress_normal);
        drawableUnSupportProgressReverse = ContextCompat.getDrawable(context, R.drawable.drawable_vote_un_support_progress_reverse);
        drawableSupportProgressFill = ContextCompat.getDrawable(context, R.drawable.drawable_vote_support_progress_fill);
        drawableUnSupportProgressFill = ContextCompat.getDrawable(context, R.drawable.drawable_vote_un_support_progress_fill);
    }

    public void setStyle(int style) {
        this.style = style;
        switch (style) {
            case Style.ALL_SHOW:
                // 支持与不支持都显示
                this.progressBarSupport.setProgressDrawable(drawableSupportProgressNormal);
                this.progressBarUnSupport.setProgressDrawable(drawableUnSupportProgressReverse);
                break;
            case Style.ONLY_SHOW_SUPPORT:
                // 只显示支持进度条
                this.progressBarSupport.setProgressDrawable(drawableSupportProgressNormal);
                this.progressBarUnSupport.setVisibility(GONE);
                progressContainer.setBackgroundResource(R.drawable.shape_vote_status_percent_bg);
                break;
            case Style.ONLY_SHOW_UN_SUPPORT:
                // 只显示不支持进度条
                this.progressBarUnSupport.setProgressDrawable(drawableUnSupportProgressNormal);
                this.progressBarSupport.setVisibility(GONE);
                progressContainer.setBackgroundResource(R.drawable.shape_vote_status_percent_bg);
                break;
        }
    }

    /**
     * 设置支持进度条的当前进度，当当前进度大于等于100时显示支持率大于等于100%时显示的进度条
     * @param progressSupport 当前支持进度
     */
    public void setProgressSupport(int progressSupport) {
        if (progressSupport >= 100) {
            progressSupport = 100;
            // 当前支持进度大于等于100时显示支持率大于等于100%时显示的进度条
            this.progressBarSupport.setProgressDrawable(drawableSupportProgressFill);
            if (style == Style.ALL_SHOW)
                // 当前支持进度大于等于100可将不支持的进度条隐藏
                this.progressBarUnSupport.setVisibility(GONE);
        }else {
            // 为了让支持与不支持有间隔，当前进度减1
            progressSupport -= 1;
        }

        this.progressBarSupport.setProgress(progressSupport);

        if (progressSupport <= 0) {
            // 当前支持进度小于等于0时，显示进度背景
            progressContainer.setBackgroundResource(R.drawable.shape_vote_status_percent_bg);
        }
    }

    /**
     * 设置支持进度条的当前进度，当当前进度大于等于100时显示不支持率大于等于100%时显示的进度条
     * @param progressUnSupport 当前不支持进度
     */
    public void setProgressUnSupport(int progressUnSupport) {
        if (progressUnSupport >= 100){
            progressUnSupport = 100;
            // 当前不支持进度大于等于100时显示不支持率大于等于100%时显示的进度条
            this.progressBarUnSupport.setProgressDrawable(drawableUnSupportProgressFill);
            if (style == Style.ALL_SHOW)
                // 当前不支持进度大于等于100可将支持的进度条隐藏
                this.progressBarSupport.setVisibility(GONE);
        }

        this.progressBarUnSupport.setProgress(progressUnSupport);

        if (progressUnSupport <= 0) {
            // 当前不支持进度小于等于0时，显示进度背景
            progressContainer.setBackgroundResource(R.drawable.shape_vote_status_percent_bg);
        }
    }

    /**
     * 百分比进度条样式
     */
    public static class Style {
        /**
         * 支持与不支持进度都显示
         */
        public static final int ALL_SHOW = 0;
        /**
         * 只显示支持进度条
         */
        public static final int ONLY_SHOW_SUPPORT = 1;
        /**
         * 只显示不支持进度条
         */
        public static final int ONLY_SHOW_UN_SUPPORT = 2;
    }
}
