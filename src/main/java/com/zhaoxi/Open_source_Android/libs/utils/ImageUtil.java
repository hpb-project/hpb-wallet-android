package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zhaoxi.Open_source_Android.DAppConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static int getPhoneWid(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getPhoneHei(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int[] getWidthHeight(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return new int[]{0, 0};
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            Bitmap originBitmap = BitmapFactory.decodeFile(imagePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 使用第一种方式获取原始图片的宽高
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        // 使用第二种方式获取原始图片的宽高
        if (srcHeight <= 0 || srcWidth <= 0) {
            try {
                ExifInterface exifInterface = new ExifInterface(imagePath);
                srcHeight =
                        exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);
                srcWidth =
                        exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 使用第三种方式获取原始图片的宽高
        if (srcWidth <= 0 || srcHeight <= 0) {
            Bitmap bitmap2 = BitmapFactory.decodeFile(imagePath);
            if (bitmap2 != null) {
                srcWidth = bitmap2.getWidth();
                srcHeight = bitmap2.getHeight();
                try {
                    if (!bitmap2.isRecycled()) {
                        bitmap2.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new int[]{srcWidth, srcHeight};
    }

    public static float getImageRatio(String imagePath) {
        int[] wh = getWidthHeight(imagePath);
        if (wh[0] > 0 && wh[1] > 0) {
            return (float) Math.max(wh[0], wh[1]) / (float) Math.min(wh[0], wh[1]);
        }
        return 1;
    }

    public static Bitmap resizeImage(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    /**
     * 保存图片
     *
     * @param bm
     * @param address
     * @return
     * @throws IOException
     */
    public static String saveBitmapBackPath(boolean isShare, Context context, Bitmap bm, String address) throws IOException {
        String path = "";
        if (isShare) {//不保存在相册中，保存到HPBWallet目录下
            path = FileManager.createPic(DAppConstants.PATH_PIC_FILE);
        } else {//保存图片 保存在相册中
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/";
        }
        File targetDir = new File(path);
        if (!targetDir.exists()) {
            try {
                targetDir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fileName = address + ".jpg";
        File savedFile = new File(path + fileName);
        if (!savedFile.exists()) {
            savedFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savedFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        if (!isShare) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(savedFile);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
        return savedFile.getAbsolutePath();
    }

    /**
     * 保存头像图片
     *
     * @param bm
     * @param address
     * @return
     * @throws IOException
     */
    public static String saveBitmapBackPath(Bitmap bm, String address) throws IOException {
        String path = "";
        path = FileManager.createPic(DAppConstants.PATH_PIC_HEADER_FILE);

        File targetDir = new File(path);
        if (!targetDir.exists()) {
            try {
                targetDir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fileName = address + ".png";
        File savedFile = new File(path + fileName);
        if (!savedFile.exists()) {
            savedFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savedFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();

        return savedFile.getAbsolutePath();
    }

    /**
     * 保存广告图片
     *
     * @param bm
     * @param name
     * @return
     * @throws IOException
     */
    public static String saveAdFilePath(Bitmap bm, String name,String type) throws IOException {
        String path = "";
        path = FileManager.createPic(DAppConstants.PATH_PIC_FILE);

        File targetDir = new File(path);
        if (!targetDir.exists()) {
            try {
                targetDir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fileName = name;
        File savedFile = new File(path + fileName +"."+type);
        if (!savedFile.exists()) {
            savedFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savedFile));
        if(type.equals("png")){
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        }else bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        bos.flush();
        bos.close();

        return savedFile.getAbsolutePath();
    }
}