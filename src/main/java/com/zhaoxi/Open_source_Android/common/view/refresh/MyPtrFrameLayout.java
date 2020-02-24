package com.zhaoxi.Open_source_Android.common.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zhaoxi.Open_source_Android.dapp.R;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MyPtrFrameLayout extends PtrClassicFrameLayout {

    /**
     * 用于控制是否禁用下拉刷新 true:禁用 false:允许
     */
    private boolean mIsDisallow = false;

    public MyPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSetparameters();
    }

    public MyPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSetparameters();
    }

    public MyPtrFrameLayout(Context context) {
        super(context);
        initSetparameters();
    }

    private void initSetparameters() {
        // show empty view
        this.setVisibility(View.VISIBLE);
        // 阻尼系数    默认: 1.7f，越大，感觉下拉时越吃力。
        this.setResistance(1.7f);
        //触发刷新时移动的位置比例  默认，1.2f，移动达到头部高度1.2倍时可触发刷新操作。
        this.setRatioOfHeaderHeightToRefresh(1.2f);
        //回弹延时 默认 200ms，回弹到刷新高度所用时间
        this.setDurationToClose(200);
        //头部回弹时间 默认1000ms
        this.setDurationToCloseHeader(500);
        //下拉刷新 / 释放刷新 默认为释放刷新
        this.setPullToRefresh(false);
        //刷新是保持头部 默认值 true.
        this.setKeepHeaderWhenRefresh(true);
        //横着滑动时隐藏 用于viewpager
        this.disableWhenHorizontalMove(true);
        // 设置背景颜色
        this.setBackgroundColor(getResources().getColor(R.color.color_F5F5F5));
    }

    /**
     * 将背景色修改为透明
     */
    public void changeThemeBackgroud() {
        changeFlshBackgroud(R.color.color_2E2F47);
    }

    public void changeWhiteBackgroud() {
        changeFlshBackgroud(R.color.white);
    }

    /**
     * 将背景色修改为透明
     */
    public void changeBackgroud() {
        changeFlshBackgroud(R.color.transparent);
    }

    /**
     * 修改刷新颜色
     *
     * @param color
     */
    public void changeFlshBackgroud(int color) {
        this.setBackgroundColor(getResources().getColor(color));
    }

    public void setUltraPullToRefresh(final OnRefreshListener refreshListener, final View InternalView) {
        this.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                if (refreshListener != null) {
                    // 刷新代码
                    refreshListener.refresh(frame);
                }
                // 十秒后自动关闭刷新动画
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, 10000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                if (InternalView != null) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                            InternalView, header);
                } else {
                    return false;
                }
            }
        });
    }


    public void setUltraPullToRefreshWithDisallow(final OnRefreshListener refreshListener, final View InternalView) {
        this.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                if (refreshListener != null) {
                    // 刷新代码
                    refreshListener.refresh(frame);
                }
                // 十秒后自动关闭刷新动画
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, 10000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (InternalView != null) {
                    if (!mIsDisallow) {
                        return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                                InternalView, header);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * 设置是否禁用下拉刷新 true:禁用  false:允许
     *
     * @param isDisallow 是否禁用
     */
    public void setDisallowPullRefresh(boolean isDisallow) {
        this.mIsDisallow = isDisallow;
    }

    public interface OnRefreshListener {
        void refresh(PtrFrameLayout frame);
    }

}
