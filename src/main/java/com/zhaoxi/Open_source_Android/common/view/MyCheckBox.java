package com.zhaoxi.Open_source_Android.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * create by fangz
 * create date:2019/9/28
 * create time:23:17
 */
public class MyCheckBox extends android.support.v7.widget.AppCompatCheckBox {
    public MyCheckBox(Context context) {
        super(context);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
