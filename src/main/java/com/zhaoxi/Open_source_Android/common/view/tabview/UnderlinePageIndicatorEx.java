package com.zhaoxi.Open_source_Android.common.view.tabview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.zhaoxi.Open_source_Android.dapp.R;

public class UnderlinePageIndicatorEx extends UnderlinePageIndicator {

    public UnderlinePageIndicatorEx(Context context) {
        super(context, null);
    }

    public UnderlinePageIndicatorEx(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.vpiUnderlinePageIndicatorStyle);
    }

    public UnderlinePageIndicatorEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //自身的 ViewPage传递过去
    @Override
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }

        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException(" pager 没有 加入 adapter");
        }

        mViewPager = viewPager;
        invalidate();

        post(new Runnable() {
            @Override
            public void run() {
                if (mFades) {
                    post(mFadeRunnable);
                }
            }
        });
    }

}