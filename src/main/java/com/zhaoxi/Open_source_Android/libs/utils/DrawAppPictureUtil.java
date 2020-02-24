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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nanchen.compresshelper.CompressHelper;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.ShareAddressBean;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.ui.activity.MainActivity;

import java.io.File;
import java.io.IOException;

public class DrawAppPictureUtil extends LinearLayout {
    private final int IMAGE_WIDTH = MainActivity.width;
    private final int IMAGE_HEIGHT = MainActivity.height;
    private final String TAG = "DrawLongPictureUtil";
    private Context context;
    private Listener listener;

    private ShareAddressBean shareInfo;

    private View rootView;
    private LinearLayout mLayoutAddress;
    private ImageView imgQrCode;
    private ImageView mImgDes;
    private String mAddress;

    // 长图的宽度，默认为屏幕宽度
    private int longPictureWidth = MainActivity.width;
    // 最终压缩后的长图宽度
    private int finalCompressLongPictureWidth;

    public void removeListener() {
        this.listener = null;
    }

    public interface Listener {

        /**
         * 生成长图成功的回调
         *
         * @param path 长图路径
         */
        void onSuccess(String path, String thumbPath);

        /**
         * 生成长图失败的回调
         */
        void onFail();
    }

    public DrawAppPictureUtil(Context context) {
        super(context);
        init(context);
    }

    public DrawAppPictureUtil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawAppPictureUtil(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        this.context = context;

        rootView = LayoutInflater.from(context).inflate(R.layout.layout_draw_app_canvas, this, false);
        initView();
    }

    private void initView() {
        imgQrCode = rootView.findViewById(R.id.img_share_app_qr);
        mImgDes = rootView.findViewById(R.id.img_share_app_des);
        mLayoutAddress = rootView.findViewById(R.id.draw_app_layout);
        layoutView(mLayoutAddress);
    }

    /**
     * 手动测量view宽高
     */
    private void layoutView(View v) {

        v.measure(IMAGE_WIDTH, IMAGE_HEIGHT);
        v.layout(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    /**
     * 填充数据
     *
     * @param info
     */
    public void setData(ShareAddressBean info) {
        this.shareInfo = info;
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

    private Bitmap getLinearLayoutBitmap(LinearLayout linearLayout, int w, int h) {
        Bitmap originBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(originBitmap);
        linearLayout.draw(canvas);
        return ImageUtil.resizeImage(originBitmap, longPictureWidth, h);
    }

    private void draw() {
        // 计算出最终生成的长图的高度 = 上、中、图片总高度、下等个个部分加起来
        imgQrCode.setImageBitmap(shareInfo.getBm());
        mAddress = shareInfo.getAddress();
        if(shareInfo.getShowDes() == 1){
            mImgDes.setImageResource(R.mipmap.icon_share_address_des_zh);
        }else{
            mImgDes.setImageResource(R.mipmap.icon_share_address_des_en);
        }
        // 创建空白画布
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmapAll;
        try {
            bitmapAll = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, config);
        } catch (Exception e) {
            e.printStackTrace();
            config = Bitmap.Config.RGB_565;
            bitmapAll = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, config);
        }
        Canvas canvas = new Canvas(bitmapAll);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);

        canvas.drawBitmap(getLinearLayoutBitmap(mLayoutAddress, IMAGE_WIDTH, IMAGE_HEIGHT), 0, 0, paint);
        canvas.save();
        // 生成最终的文件，并压缩大小，这里使用的是：implementation 'com.github.nanchen2251:CompressHelper:1.0.5'
        String path = "";
        String result = "";
        try {
            path = ImageUtil.saveBitmapBackPath(true, context, bitmapAll, mAddress);
            float imageRatio = ImageUtil.getImageRatio(path);
            if (imageRatio >= 10) {
                finalCompressLongPictureWidth = 750;
            } else if (imageRatio >= 5 && imageRatio < 10) {
                finalCompressLongPictureWidth = 900;
            } else {
                finalCompressLongPictureWidth = longPictureWidth;
            }

            // 由于长图一般比较大，所以压缩时应注意OOM的问题，这里并不处理OOM问题，请自行解决。
            try {
                result = new CompressHelper.Builder(context).setMaxWidth(finalCompressLongPictureWidth)
                        .setMaxHeight(Integer.MAX_VALUE) // 默认最大高度为960
                        .setQuality(80)    // 默认压缩质量为80
                        .setFileName(mAddress) // 设置你需要修改的文件名
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                        .setDestinationDirectoryPath(
                                FileManager.createPic(DAppConstants.PATH_PIC_FILE_SMALL))
                        .build()
                        .compressToFile(new File(path))
                        .getAbsolutePath();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                finalCompressLongPictureWidth = finalCompressLongPictureWidth / 2;
                result = new CompressHelper.Builder(context).setMaxWidth(finalCompressLongPictureWidth)
                        .setMaxHeight(Integer.MAX_VALUE) // 默认最大高度为960
                        .setQuality(50)    // 默认压缩质量为50
                        .setFileName(mAddress) // 设置你需要修改的文件名
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                        .setDestinationDirectoryPath(
                                FileManager.createPic(DAppConstants.PATH_PIC_FILE_SMALL))
                        .build()
                        .compressToFile(new File(path))
                        .getAbsolutePath();
            }
            SystemLog.D(TAG, "最终生成的长图路径为：" + path + " 缩略图;" + result);
            if (listener != null) {
                listener.onSuccess(path, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFail();
            }
        }
    }
}