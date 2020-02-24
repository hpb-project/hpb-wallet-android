package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanchen.compresshelper.CompressHelper;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.ShareAddressBean;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;

import java.io.File;
import java.io.IOException;

public class DrawKxPictureUtil extends LinearLayout {

    private final String TAG = "DrawKxPictureUtil";
    private Context context;
    private SharedPreferences sp;
    private Listener listener;

    private ShareAddressBean shareInfo;
    private String mKxId;

    private View rootView;
    private LinearLayout llTopView;
    private LinearLayout llContent;
    private LinearLayout llBottomView;

    private TextView tvContent;
    private TextView tvTime;
    private ImageView imgQrCode;
    private ImageView mImgDes;

    // 长图的宽度，默认为屏幕宽度
    private int longPictureWidth;
    // 最终压缩后的长图宽度
    private int finalCompressLongPictureWidth;
    // 长图两边的间距
    private int picMargin;

    // 被认定为长图的长宽比
    private int widthTop = 0;
    private int heightTop = 0;

    private int widthContent = 0;
    private int heightContent = 0;

    private int widthBottom = 0;
    private int heightBottom = 0;

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

    public DrawKxPictureUtil(Context context) {
        super(context);
        init(context);
    }

    public DrawKxPictureUtil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawKxPictureUtil(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        this.context = context;
        this.sp = context.getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);

        longPictureWidth = ImageUtil.getPhoneWid(context);
        picMargin = 40;
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_draw_kx_canvas, this, false);
        initView();
    }

    private void initView() {
        llTopView = rootView.findViewById(R.id.llTopView);
        llContent = rootView.findViewById(R.id.llContent);
        llBottomView = rootView.findViewById(R.id.llBottomView);

        mImgDes = rootView.findViewById(R.id.img_des);
        tvTime = rootView.findViewById(R.id.tvTime);
        tvContent = rootView.findViewById(R.id.tvContent);
        imgQrCode = rootView.findViewById(R.id.img_share_app_long_qr);

        layoutView(llTopView);
        layoutView(llContent);
        layoutView(llBottomView);

        widthTop = llTopView.getMeasuredWidth();
        heightTop = llTopView.getMeasuredHeight();

        widthContent = llContent.getMeasuredWidth();
        // 文字由于高度可变，所以这里不需要测量高度，后面会手动测量

        widthBottom = llBottomView.getMeasuredWidth();
        heightBottom = llBottomView.getMeasuredHeight();
    }

    /**
     * 手动测量view宽高
     */
    private void layoutView(View v) {
        int width = ImageUtil.getPhoneWid(context);
        int height = ImageUtil.getPhoneHei(context);

        v.layout(0, 0, width, height);
        int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int measuredHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
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
        imgQrCode.setImageBitmap(shareInfo.getBm());
        mKxId = shareInfo.getKxId();
        tvTime.setText(shareInfo.getTime());
        if (shareInfo.getShowDes() == 1) {
            mImgDes.setImageResource(R.mipmap.icon_share_kuanxu_top);
        } else {
            mImgDes.setImageResource(R.mipmap.icon_share_kuanxu_top_en);
        }

        // 先绘制中间部分的文字，计算出文字所需的高度

        String content = shareInfo.getContent();
        TextPaint contentPaint = tvContent.getPaint();
        StaticLayout staticLayout = new StaticLayout(content, contentPaint, (ImageUtil.getPhoneWid(context) - picMargin * 2),
                Layout.Alignment.ALIGN_NORMAL, 1.2F, 0, false);
        heightContent = staticLayout.getHeight() + 100;

        // 计算出最终生成的长图的高度 = 上、中、图片总高度、下等个个部分加起来
        int allBitmapHeight = heightTop + heightContent + heightBottom;

        // 创建空白画布
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmapAll;
        try {
            bitmapAll = Bitmap.createBitmap(longPictureWidth, allBitmapHeight, config);
        } catch (Exception e) {
            e.printStackTrace();
            config = Bitmap.Config.RGB_565;
            bitmapAll = Bitmap.createBitmap(longPictureWidth, allBitmapHeight, config);
        }
        Canvas canvas = new Canvas(bitmapAll);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);

        // 绘制top view
        canvas.drawBitmap(getLinearLayoutBitmap(llTopView, widthTop, heightTop), 0, 0, paint);
        canvas.save();

        // 绘制content view
        canvas.translate(ImageUtil.dp2px(context, 20), heightTop);
        staticLayout.draw(canvas);

        // 绘制图片view
        canvas.restore();
        canvas.drawBitmap(getLinearLayoutBitmap(llBottomView, widthBottom, heightBottom), 0,
                (heightTop + heightContent), paint);

        // 生成最终的文件，并压缩大小，这里使用的是：implementation 'com.github.nanchen2251:CompressHelper:1.0.5'
        try {
            String path = ImageUtil.saveBitmapBackPath(true, context, bitmapAll, mKxId);
            float imageRatio = ImageUtil.getImageRatio(path);
            if (imageRatio >= 10) {
                finalCompressLongPictureWidth = 750;
            } else if (imageRatio >= 5 && imageRatio < 10) {
                finalCompressLongPictureWidth = 900;
            } else {
                finalCompressLongPictureWidth = longPictureWidth;
            }
            String result;
            // 由于长图一般比较大，所以压缩时应注意OOM的问题，这里并不处理OOM问题，请自行解决。
            try {
                result = new CompressHelper.Builder(context).setMaxWidth(finalCompressLongPictureWidth)
                        .setMaxHeight(Integer.MAX_VALUE) // 默认最大高度为960
                        .setQuality(80)    // 默认压缩质量为80
                        .setFileName(mKxId) // 设置你需要修改的文件名
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
                        .setFileName(mKxId) // 设置你需要修改的文件名
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                        .setDestinationDirectoryPath(
                                FileManager.createPic(DAppConstants.PATH_PIC_FILE_SMALL))
                        .build()
                        .compressToFile(new File(path))
                        .getAbsolutePath();
            }
            SystemLog.D(TAG, "最终生成的长图路径为：" + path + " 缩略图:" + result);
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