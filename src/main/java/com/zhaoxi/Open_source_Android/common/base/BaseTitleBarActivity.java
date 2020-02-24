package com.zhaoxi.Open_source_Android.common.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;

public class BaseTitleBarActivity extends BaseActivity {
    public Toolbar mCommonToobar;
    private ImageView mImgBack, mImgRight;
    protected TextView mTxtTitle, mTxtLeft, mTxtRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        initTitles();
    }

    private void initTitles() {
        mCommonToobar = findViewById(R.id.common_toolbar);
        mImgBack = findViewById(R.id.common_toolbar_img_back);
        mImgRight = findViewById(R.id.common_toolbar_img_right);
        mTxtTitle = findViewById(R.id.common_toolbar_tv_title);
        mTxtLeft = findViewById(R.id.common_toolbar_txt_left);
        mTxtRight = findViewById(R.id.common_toolbar_txt_right);
        if (mCommonToobar != null) {
            ImmersionBar.with(this)
                    .titleBar(mCommonToobar)
                    .init();
        }
    }

    /**
     * 设置标题
     *
     * @param titleResId
     */
    public void setTitle(int titleResId) {
        setTitle(getString(titleResId), false);
    }

    public void setTitle(int titleResId, boolean isBack) {
        setTitle(getString(titleResId), isBack);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (mTxtTitle != null)
            mTxtTitle.setText(title);
    }

    public void setTitle(String title, boolean isBack) {
        if (mTxtTitle != null)
            mTxtTitle.setText(title);
        if (isBack)
            mImgBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    /**
     * 设置返回键点击事件
     *
     * @param listener
     */
    public void setImgBackListener(OnClickListener listener) {
        if (mImgBack != null) {
            mImgBack.setOnClickListener(listener);
        }
    }

    public void leftTxtWithTextListener(String text, OnClickListener listener) {
        if (mTxtLeft != null) {
            mTxtLeft.setText(text);
            mTxtLeft.setOnClickListener(listener);
        }
    }

    /**
     * 显示左键，修改文字及添加listener
     *
     * @param text
     * @param listener
     */
    public void showLeftTxtWithTextListener(String text, OnClickListener listener) {
        if (mTxtLeft != null) {
            mTxtLeft.setText(text);
            mTxtLeft.setOnClickListener(listener);
            mTxtLeft.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示右键，textView  listener
     *
     * @param text
     * @param listener
     */
    public void showRightTxtWithTextListener(String text, OnClickListener listener) {
        if (mTxtRight != null) {
            mTxtRight.setText(text);
            mTxtRight.setOnClickListener(listener);
            mTxtRight.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示右键，img  listener
     *
     * @param imageId
     * @param listener
     */
    public void rightImgWithImgListener(int imageId, OnClickListener listener) {
        if (mImgRight != null) {
            mImgRight.setImageResource(imageId);
            mImgRight.setOnClickListener(listener);
        }
    }

    public void showRightImgWithImgListener(int imageId, OnClickListener listener) {
        if (mImgRight != null) {
            mImgRight.setImageResource(imageId);
            mImgRight.setOnClickListener(listener);
            mImgRight.setVisibility(View.VISIBLE);
        }
    }

    /*设置标题栏为F5F5F5，文字主题色*/
    public void setTitleGray() {
        mCommonToobar.setBackgroundColor(getResources().getColor(R.color.color_F5F5F5));
        mImgBack.setImageResource(R.mipmap.icon_back_black);
//        Drawable nav_up = getResources().getDrawable(R.mipmap.icon_title_back);
//        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
//                nav_up.getMinimumHeight());
//        mTextViewLeft.setCompoundDrawables(nav_up, null, null, null);
        mTxtTitle.setTextColor(getResources().getColor(R.color.color_191E25));
        mTxtLeft.setTextColor(getResources().getColor(R.color.color_191E25));
        mTxtRight.setTextColor(getResources().getColor(R.color.color_191E25));
    }

    /*设置标题栏为透明色，默认白色*/
    public void setTitleTransparent() {
        mCommonToobar.setBackgroundColor(getResources().getColor(R.color.transparent));
        mImgBack.setImageResource(R.mipmap.back_normal);
        mTxtTitle.setTextColor(getResources().getColor(R.color.white));
        mTxtLeft.setTextColor(getResources().getColor(R.color.white));
        mTxtRight.setTextColor(getResources().getColor(R.color.white));
    }

    public void setTitleBgColor(int color, boolean isBalck) {
        mCommonToobar.setBackgroundColor(getResources().getColor(color));
        if (isBalck) {
            mImgBack.setImageResource(R.mipmap.icon_back_black);
            mTxtTitle.setTextColor(getResources().getColor(R.color.color_191E25));
            mTxtLeft.setTextColor(getResources().getColor(R.color.color_191E25));
            mTxtRight.setTextColor(getResources().getColor(R.color.color_191E25));
        } else {
            mImgBack.setImageResource(R.mipmap.back_normal);
            mTxtTitle.setTextColor(getResources().getColor(R.color.white));
            mTxtLeft.setTextColor(getResources().getColor(R.color.white));
            mTxtRight.setTextColor(getResources().getColor(R.color.white));
        }
    }


    /**
     * 判断是否显示隐藏左边的文字
     *
     * @param isShow
     */
    public void hideOrShowLeftText(boolean isShow) {
        mTxtLeft.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 判断是否显示隐藏右边的图片
     *
     * @param isShow
     */
    public void hideOrShowRightImg(boolean isShow) {
        mImgRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 重置右键的图片及点击事件
     *
     * @param imageId
     * @param listener
     */
//    public void showRightButtonWithImageAndListener(int imageId,
//                                                    OnClickListener listener) {
//        if (mTextViewRight != null) {
//            Drawable nav_up = getResources().getDrawable(imageId);
//            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
//                    nav_up.getMinimumHeight());
//            mTextViewRight.setCompoundDrawables(null, null, nav_up, null);
//            mTextViewRight.setOnClickListener(listener);
//            mTextViewRight.setVisibility(View.VISIBLE);
//        }
//    }

    protected void setTextSize(String value, TextView tv, TextView... tvs) {
        float size = 20f;
        if (value.length() > 16 && value.length() <= 18) {
            size = 18f;
        } else if (value.length() > 18)
            size = 16f;

        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        if (tvs != null && tvs.length > 0) {
            for (int i = 0; i < tvs.length; i++) {
                tvs[i].setTextSize(size);
            }
        }
    }

}
