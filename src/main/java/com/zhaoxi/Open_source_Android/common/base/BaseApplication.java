package com.zhaoxi.Open_source_Android.common.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * 基础Application
 * Created by 51973 on 2018/5/9.
 */

public class BaseApplication extends Application {

    private Toast mToast;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

    /**
     * 纯文字提示
     *
     * @param id
     */
    public void showToast(int id) {
        showToast(getString(id));
    }

    /**
     * 带成功图片的toast
     *
     * @param message
     */
    public void showSucceseToast(String message) {
        TextImgToast(message, R.mipmap.icon_toast_succse);
    }

    /**
     * 带失败图片的toast
     *
     * @param message
     */
    public void showErrorToast(String message) {
        TextImgToast(message, R.mipmap.icon_toast_error);
    }

    /**
     * 根据具体情况 显示具体的图片
     *
     * @param message
     * @param imgId
     */
    public void showTxtImgToast(String message, int imgId) {
        TextImgToast(message, imgId);
    }

    /**
     * 显示图文Toast
     *
     * @param message
     * @param imgId
     */
    public void TextImgToast(String message, int imgId) {
        Toast t = new Toast(this);
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(layoutParams);
        textView.setPadding(60, 30, 60, 0);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setText(message);
        textView.setTextSize(15f);
        textView.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(imgId);
        //组合文本加图片,可以设置线性布局
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundResource(R.drawable.bg_error_toast);//设置背景颜色
        layout.setOrientation(LinearLayout.VERTICAL);//设置LinearLayout垂直
        layout.setGravity(Gravity.CENTER);//设置LinearLayout里面内容中心分布
        layout.addView(imageView);//先添加image
        layout.addView(textView);//再添加text

        t.setView(layout);//只需要把layout设置进入Toast
        t.setGravity(Gravity.CENTER, 0, 0);
        t.setDuration(Toast.LENGTH_SHORT);
        t.show();
    }

    /**
     * 只显示文字
     *
     * @param message
     */
    public void showToast(String message) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setBackgroundResource(R.drawable.bg_error_toast);
        textView.setTextColor(getResources().getColor(R.color.white));
        if (mToast == null)
            mToast = new Toast(this);
        mToast.setView(textView);
        mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
