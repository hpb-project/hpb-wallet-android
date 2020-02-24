package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 全部展示的ListView
 * @author zhutt on 2018-06-14
 */
public class ListViewShowAll extends ListView {

    public ListViewShowAll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewShowAll(Context context) {
        super(context);
    }

    public ListViewShowAll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
