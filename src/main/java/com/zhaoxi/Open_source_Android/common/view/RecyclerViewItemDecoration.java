package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhangtao on 2017/11/29.
 */

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    // 分割线的高度，分割线的所需要的画笔（自定义分割线用），分割线方向以及默认分割线
    private Drawable mDivider;
    private int mDividerHeight;
    private int mOrientation;
    // 默认的分割线
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    /**
     * 默认分割线
     * @param context 上下文
     * @param mOrientation 方向
     */
    public RecyclerViewItemDecoration(Context context, int mOrientation) {
        if (mOrientation != LinearLayoutManager.HORIZONTAL && mOrientation != LinearLayoutManager.VERTICAL){
            throw new IllegalArgumentException("参数错误");
        }

        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        this.mDivider = typedArray.getDrawable(0);
        if (mDivider != null) {
            this.mDividerHeight = mDivider.getIntrinsicHeight();
        }

        this.mOrientation = mOrientation;
        typedArray.recycle();
    }


    /**
     * 自定义分割线
     * @param context 上下文
     * @param mOrientation 方向
     * @param drawableId:可以是图片，可以是drawable资源文件
     */
    public RecyclerViewItemDecoration(Context context, int mOrientation,int drawableId) {
        if (mOrientation != LinearLayoutManager.HORIZONTAL && mOrientation != LinearLayoutManager.VERTICAL){
            throw new IllegalArgumentException("参数错误");
        }

        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        this.mDivider = ContextCompat.getDrawable(context, drawableId);
        if (mDivider != null) {
            this.mDividerHeight = mDivider.getIntrinsicHeight();
        }

        this.mOrientation = mOrientation;
        typedArray.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL){
            drawVertical(c,parent);
        }else {
            drawHorizontal(c,parent);
        }
    }

    // 绘制水平的分割线
    private void drawVertical(Canvas c, RecyclerView parent) {
        // 左右
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() + parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerHeight;
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }

    }

    // 绘制垂直的分割线
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() + parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + layoutParams.rightMargin;
            int right = left + mDividerHeight;
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL){
            outRect.set(0,0,0,mDividerHeight);
        }else {
            outRect.set(0,0,mDividerHeight,0);
        }
    }
}