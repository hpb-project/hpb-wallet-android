package com.zhaoxi.Open_source_Android.common.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * create by fangz
 * create date:2019/9/9
 * create time:9:47
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    // 间隔
    private int spacing;
    // 列数
    private int spanCount;
    // 是否包含边缘
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spacing, int spanCount, boolean includeEdge) {
        this.spacing = spacing;
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = spacing * (Math.abs(column - 1));
            outRect.right = column * spacing;

            if (position < spanCount) {
                outRect.top = spacing;
            }

        } else {
            if (position >= spanCount) {
                outRect.top = spacing;
            }
        }
    }
}
