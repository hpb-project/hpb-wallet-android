package com.zhaoxi.Open_source_Android.common.view.banner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.net.bean.BannerInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerView extends LinearLayout {
    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mLayoutDots;

    private List<BannerInfo.BaseInfo> mBannerDatas;
    private BanviewImageClick mBanviewImageClick;

    private List<ImageView> mDotsViewList = new ArrayList<>();
    private int mCurrentItem = 100;
    private BannerAdapter mBannerAdapter;

    // 循环播放
    private Timer mCycleTimer; //轮询
    private TimerTask mCycleTask;
    private Timer mResumingTimer; //重启task
    private TimerTask mResumingTask;
    private boolean mCycling;

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        View.inflate(getContext(), R.layout.view_banner, this);
        mViewPager = (ViewPager) findViewById(R.id.banner_view_viewpager);
        mLayoutDots = (LinearLayout) findViewById(R.id.banner_view_dot_layout);
        mLayoutDots.removeAllViews();
        mDotsViewList.clear();
    }

    /**
     * banner数据填充
     */
    public void setBannerData(List<BannerInfo.BaseInfo> bannerResponses
            , BanviewImageClick banviewImageClick) {
        mBannerDatas = bannerResponses;
        mBanviewImageClick = banviewImageClick;
        mCurrentItem = 100;
        initDatas();
    }

    private void initDatas() {
        mBannerAdapter = new BannerAdapter(mContext, mBannerDatas, mBanviewImageClick);
        mViewPager.setAdapter(mBannerAdapter);
        if (mBannerDatas.size() > 1) {
            initDots();//初始化小点点
            mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        }else{//如果为1 不进行滚动和小点点绘制
            mLayoutDots.removeAllViews();
            mDotsViewList.clear();
        }
        //设置动画效果
        AccordionTransformer transformer = new AccordionTransformer();
        mViewPager.setPageTransformer(true, transformer);

        mViewPager.setCurrentItem(100);

        // 设置切换动画时间
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext());
            // scroller.setFixedDuration(5000);
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {
        }

        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        recoverCycle();
                        break;
                }
                return false;
            }
        });
        start();
//        }

    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        public void onPageSelected(int position) {
            mCurrentItem = position;
            int virtualPosition = position % mBannerAdapter.getRealCount();
            mDotsViewList.get(virtualPosition).setImageResource(R.mipmap.icon_point);
            mDotsViewList.get(oldPosition).setImageResource(R.mipmap.icon_point_pre);
            oldPosition = virtualPosition;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }


    // 初始化点点
    private void initDots() {
        mLayoutDots.removeAllViews();
        mDotsViewList.clear();
        for (int i = 0; i < mBannerDatas.size(); i++) {
            boolean isSetGB = false;
            if (this.mLayoutDots.getChildCount() == 0)
                isSetGB = true;
            ImageView v = getDotView(this.getContext(), isSetGB);
            mLayoutDots.addView(v);
            mDotsViewList.add(v);
        }
    }

    private ImageView getDotView(Context context, Boolean isSetBG) {
        ImageView v = new ImageView(context);
        v.setScaleType(ImageView.ScaleType.CENTER);
        v.setPadding(8,0,8,0);
        if (isSetBG)
            v.setImageResource(R.mipmap.icon_point);
        else
            v.setImageResource(R.mipmap.icon_point_pre);
        return v;
    }

    /*自动播放*/
    public void start() {
        if (mCycleTimer != null) mCycleTimer.cancel();
        if (mCycleTask != null) mCycleTask.cancel();
        if (mResumingTask != null) mResumingTask.cancel();
        if (mResumingTimer != null) mResumingTimer.cancel();
        mCycleTimer = new Timer();
        mCycleTask = new ScrollTask();
        mCycleTimer.schedule(mCycleTask, 1000, 3000);//设置轮播器播放间隔
        mCycling = true;
    }

    private class ScrollTask extends TimerTask {
        public void run() {
            changeHandler.obtainMessage().sendToTarget(); // 通过Handler切换图片
        }
    }

    private Handler changeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            synchronized (mViewPager) {
                if (mViewPager != null)
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);// 切换当前显示的图片
            }
        }
    };

    public interface BanviewImageClick {
        void setImageOnClick(View view, BannerInfo.BaseInfo bannerInfo);
    }

    /*bannerview 设置切换动画时间*/
    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseAutoCycle();
                break;
        }
        return false;
    }

    /**
     * pause auto cycle.
     */
    private void pauseAutoCycle() {
        if (mCycling) {
            mCycleTimer.cancel();
            mCycleTask.cancel();
            mCycling = false;
        } else {
            if (mResumingTimer != null && mResumingTask != null) {
                recoverCycle();
            }
        }
    }

    /**
     * when paused cycle, this method can weak it up.
     */
    private void recoverCycle() {
        if (!mCycling) {
            if (mResumingTask != null && mResumingTimer != null) {
                mResumingTimer.cancel();
                mResumingTask.cancel();
            }
            mResumingTimer = new Timer();
            mResumingTask = new TimerTask() {
                @Override
                public void run() {
                    start();
                }
            };
            mResumingTimer.schedule(mResumingTask, 3000);
        }
    }
}
