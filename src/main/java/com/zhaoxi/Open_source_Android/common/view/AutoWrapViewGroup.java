package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

/**
 * 自动换行ViewGroup （可做自动换行 LinearLayout 用）
 */
public class AutoWrapViewGroup extends RadioGroup {

    public AutoWrapViewGroup(Context context) {
        super(context);
    }

    public AutoWrapViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;

        for (int index = 0; index < childCount; index++) {
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                int width = child.getMeasuredWidth() + 20;
                int height = child.getMeasuredHeight();
                x += width;
                y = row * (height + 20) + height;
                if (x > maxWidth) {
                    x = width + 20;
                    row++;
                    y = row * (height + 20) + height;
                }
            }
        }
        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int maxWidth = r - l;
        int x = 0;
        int y = 0;
        int row = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width + 20;
                y = row * (height + 20) + height;
                if (x > maxWidth) {
                    x = width + 20;
                    row++;
                    y = row * (height + 20) + height;
                }
                child.layout(x - width, y - height, x, y);
            }
        }
    }
}