package com.zhaoxi.Open_source_Android.libs.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * des:
 * Created by ztt on 2018/6/5.
 */

public class SystemInfoUtil {
    /**
     * @param context
     * @return 返回当前的版本名称
     */
    public static String getAppVersionName(Context context) {
        PackageManager pagePackageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = pagePackageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过拼接名字获取drawable下的图片
     * @param name
     * @return
     */
    public static Bitmap getBitmapByName(Context context, String name) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        int resID = context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
        return BitmapFactory.decodeResource(context.getResources(), resID);
    }

    /**
     * 获取设备好唯一标识
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = "";
        if (EasyPermissions.hasPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
        }
        return deviceId;
    }
}
