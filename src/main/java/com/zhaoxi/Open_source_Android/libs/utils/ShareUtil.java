package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * des:
 * Created by ztt on 2018/11/19.
 */

public class ShareUtil {
    private static String sinaPackageName = "com.sina.weibo";
    private static String wechatPackageName = "com.tencent.mm";
    private static String qqPackageName = "com.tencent.mobileqq";

    public static boolean shareImageLocal(Context context, SHARE_MEDIA share_media) {//纯图片分享
        boolean isisWeiboInstalled;
        if (SHARE_MEDIA.SINA.equals(share_media)) {
            isisWeiboInstalled = isAppInstalled(context, sinaPackageName);
            if (!isisWeiboInstalled) {
                DappApplication.getInstance().showToast(context.getResources().getString(R.string.share_kx_txt_06));
                return false;
            }
        }
        if (SHARE_MEDIA.QQ.equals(share_media)) {
            isisWeiboInstalled = isAppInstalled(context, qqPackageName);
            if (!isisWeiboInstalled) {
                DappApplication.getInstance().showToast(context.getResources().getString(R.string.share_kx_txt_07));
                return false;
            }
        }
        if (SHARE_MEDIA.WEIXIN.equals(share_media) || SHARE_MEDIA.WEIXIN_CIRCLE.equals(share_media)) {
            isisWeiboInstalled = isAppInstalled(context, wechatPackageName);
            if (!isisWeiboInstalled) {
                DappApplication.getInstance().showToast(context.getResources().getString(R.string.share_kx_txt_08));
                return false;
            }
        }
        return true;
    }

    public static boolean isAppInstalled(@NonNull Context context, String packageName) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if (packageName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void shareNationTextMore(BaseActivity baseActivity, String url){
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, url);
        baseActivity.startActivity(Intent.createChooser(textIntent, "分享"));
    }

    public static void shareMore(BaseActivity baseActivity, String path) {
        Intent share_intent = new Intent();
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri imageContentUri = FileProvider.getUriForFile(baseActivity, baseActivity.getPackageName() + ".fileprovider", new File(path));
            imageUris.add(imageContentUri);
        } else {
            imageUris.add(Uri.fromFile(new File(path)));
        }
        share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
        share_intent.setType("image/*");//设置分享内容的类型
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        baseActivity.startActivity(Intent.createChooser(share_intent, "分享"));
    }


    /**
     *
     * @param baseActivity
     * @param path
     */
    public static void shareTxtMore(BaseActivity baseActivity, String path) {
        Intent share_intent = new Intent();
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(baseActivity, baseActivity.getPackageName() + ".fileprovider", new File(path));
        } else {
            uri = Uri.fromFile(new File(path));
        }
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("*/*");// 文件分享
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share_intent.putExtra(Intent.EXTRA_STREAM, uri);
        baseActivity.startActivity(Intent.createChooser(share_intent, "分享"));
    }


    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(Context context,String url) {
        try {
            if(StrUtil.isEmpty(url)){
                return SystemInfoUtil.getBitmapByName(context, "right_01");
            }
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
