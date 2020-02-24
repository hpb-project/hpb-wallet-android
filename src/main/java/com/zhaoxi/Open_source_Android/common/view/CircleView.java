package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * create by dino
 * create date:2019/8/16
 * create time:12:20
 */
public class CircleView extends View {

    private static final String TAG = "CircleView";

    /**
     * 原图副本
     */
    private Bitmap cloneBitmap;
    /**
     * 图片资源ID
     */
    private int imageResId;
    private float borderWidth;
    private Paint imagePaint;
    private Paint borderPaint;
    private boolean isBorderVisibility;


    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCircleView(context, attrs);
    }

    private void initCircleView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        imageResId = typedArray.getResourceId(R.styleable.CircleView_src, R.drawable.shape_default_img);
        borderWidth = typedArray.getFloat(R.styleable.CircleView_border_thickness, 3.0f);
        int borderAlpha = typedArray.getInteger(R.styleable.CircleView_border_alpha, 255);
        int borderColor = typedArray.getColor(R.styleable.CircleView_border_colors, Color.GREEN);
        int borderDashWidth = typedArray.getInteger(R.styleable.CircleView_border_dash_width, 0);
        isBorderVisibility = typedArray.getBoolean(R.styleable.CircleView_border_visibility, false);
        typedArray.recycle();

        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);

        if (isBorderVisibility) {
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setAntiAlias(true);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setAlpha(borderAlpha);
            borderPaint.setPathEffect(new DashPathEffect(new float[]{borderDashWidth, borderDashWidth}, 0));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取测量值
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        // 获取圆心，即中心点
        float cx = measuredWidth / 2.0f;
        float cy = measuredHeight / 2.0f;
        float radius = Math.min(measuredWidth, measuredHeight) / 2.0f;
        // 创建原位图
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), imageResId);
        //  如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
        Bitmap copyBitmap = Bitmap.createScaledBitmap(srcBitmap, measuredWidth, measuredHeight, true);
        // 创建图片副本
        cloneBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        // 创建跟副本一样大小的画布
        Canvas mCanvas = new Canvas(cloneBitmap);
        // 绘制遮罩层
        if (isBorderVisibility) {
            mCanvas.drawCircle(cx, cy, radius - borderWidth, imagePaint);
        } else {
            mCanvas.drawCircle(cx, cy, radius, imagePaint);
        }

        // 设置绘制模式为SRC_IN
        imagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制图片
        mCanvas.drawBitmap(copyBitmap, 0, 0, imagePaint);
        if (isBorderVisibility) {
            // 绘制边框
            mCanvas.drawCircle(cx, cy, radius - borderWidth / 2.0f, borderPaint);
        }
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWH(widthMeasureSpec), measureWH(heightMeasureSpec));
    }


    private int measureWH(int measureSpec) {
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        int measureValue = dp2px(100);
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                measureValue = Math.min(measureValue, size);
                break;
            case MeasureSpec.EXACTLY:
                measureValue = size;
                break;
        }

        return measureValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(cloneBitmap, 0, 0, null);
    }

}
