package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 描述：
 * 作者：zhutt on 2017/3/6 16:11
 * 邮箱：519733108@qq.com
 */
public class SelectGridView extends GridView {

    public SelectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectGridView(Context context) {
        super(context);
    }

    public SelectGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
