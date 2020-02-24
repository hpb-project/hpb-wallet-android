package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;

import java.io.IOException;

/**
 * @author ztt
 * @des
 * @date 2019/5/23.
 */

public class CreateHeaderPicUtil extends LinearLayout {
    private final int IMAGE_WIDTH = 120;
    private final int IMAGE_HEIGHT = 120;

    private View rootView;
    private LinearLayout mLayoutBg;
    private TextView mTxtName;

    private Context context;
    private String mHeaderName;
    private String mAddress;
    private int bgColor = Color.parseColor("#4D4F73");
    private int[] colorArray = new int[]{Color.parseColor("#FF5151"),
            Color.parseColor("#FF7373"), Color.parseColor("#FFEB55"),
            Color.parseColor("#4BEFDC"), Color.parseColor("#59D4FF"),
            Color.parseColor("#5981FF"), Color.parseColor("#3E54FF"),
            Color.parseColor("#8261FF"), Color.parseColor("#FF43C2"),
            Color.parseColor("#BF61ED")};

    public interface CreateHeaderListener {

        /**
         * @param address 地址
         * @param path 图路径
         */
        void onSuccess(String address,String path);

        /**
         * 生成图失败的回调
         */
        void onFail();
    }

    public void setCreateHeaderListener(CreateHeaderListener createHeaderListener) {
        this.mListener = createHeaderListener;
    }

    private CreateHeaderListener mListener;



    public CreateHeaderPicUtil(Context context) {
        this(context, null);
    }

    public CreateHeaderPicUtil(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public CreateHeaderPicUtil(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_draw_header_canvas, this, false);
        initView();
    }


    private void initView() {
        mLayoutBg = rootView.findViewById(R.id.create_header_layout_bg);
        mTxtName = rootView.findViewById(R.id.create_header_txt_name);
        layoutView(mLayoutBg);
    }

    /**
     * 手动测量view宽高
     */
    private void layoutView(View v) {
        v.measure(DisplayUtils.dp2px(context, IMAGE_WIDTH), DisplayUtils.dp2px(context, IMAGE_HEIGHT));
        v.layout(0, 0, DisplayUtils.dp2px(context, IMAGE_WIDTH), DisplayUtils.dp2px(context, IMAGE_HEIGHT));
    }

    /**
     * 填充数据
     * @param address
     * @param name
     */
    public void setData(String address, String name) {
        mAddress = address;
        String headerName = name.substring(0, 1);
        if(!StrUtil.isCheckSpaChat(headerName)){
            //判断是否为小写字母
            mHeaderName = headerName.toUpperCase();;
        }else{
            mHeaderName = headerName;
        }

    }

    /**
     * 开始绘制
     */
    public void startDraw() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开始绘制view
                draw();
            }
        }).start();
    }

    private void draw() {
        // 计算出最终生成的长图的高度 = 上、中、图片总高度、下等个个部分加起来
        mTxtName.setText(mHeaderName);
        //随机生成背景色
//        Random random = new Random();
//        int index = random.nextInt(colorArray.length);
//        int colorId = colorArray[index];
        mLayoutBg.setBackgroundColor(bgColor);
        // 创建空白画布
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmapAll;
        try {
            bitmapAll = Bitmap.createBitmap(DisplayUtils.dp2px(context, IMAGE_WIDTH), DisplayUtils.dp2px(context, IMAGE_HEIGHT), config);
        } catch (Exception e) {
            e.printStackTrace();
            config = Bitmap.Config.RGB_565;
            bitmapAll = Bitmap.createBitmap(DisplayUtils.dp2px(context, IMAGE_WIDTH), DisplayUtils.dp2px(context, IMAGE_HEIGHT), config);
        }
        Canvas canvas = new Canvas(bitmapAll);
        canvas.drawColor(context.getResources().getColor(R.color.transparent));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);

        canvas.drawBitmap(getLinearLayoutBitmap(mLayoutBg, DisplayUtils.dp2px(context, IMAGE_WIDTH), DisplayUtils.dp2px(context, IMAGE_HEIGHT)), 0, 0, paint);
        canvas.save();
        String path = "";
        try {
            path = ImageUtil.saveBitmapBackPath(bitmapAll, mAddress);
            if (mListener != null) {
                mListener.onSuccess(mAddress,path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mListener.onFail();
        }
    }

    private Bitmap getLinearLayoutBitmap(LinearLayout linearLayout, int w, int h) {
        Bitmap originBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(originBitmap);
        linearLayout.draw(canvas);
        return ImageUtil.resizeImage(originBitmap, DisplayUtils.dp2px(context, IMAGE_WIDTH), h);
    }
}
