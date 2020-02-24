package com.zhaoxi.Open_source_Android.libs.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonDownloadDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DialogDownload {
    private static final int DOWNLOAD = 1;// 下载中
    private static final int DOWNLOAD_FINISH = 2;// 下载结束
    private static final int DOWNLOAD_FAIL = 3;// 下载失败
    private static final int DOWNLOAD_CANLE = 4;// 取消下载
    private static final int NOSDCARD = 0x04;// 没有sdcard
    public static final int DOWNLOAD_CONNECT_TIME_OUT = 30000;// 下载时链接服务器的超时时间
    public static final int DOWNLOAD_READ_TIME_OUT = 30000;// 下载时读取文件的超时时间

    private int mProgress;// 进度
    private int count = 0; // 已读取文件大小
    private int numread;// 剩余读取文件大小
    private int fileSize;// 资源文件大小
    private CommonDownloadDialog mDownLoadBuilder; // 下载对话框
    private boolean isInstal = false; // 下载中断标志位
    private File apkfile;
    private String downloaUrl;
    private DownThread mDownThread;
    private OnDownLoadCompletedListener mListener;
    private Context mContext;

    public interface OnDownLoadCompletedListener {
        void downloadSuccess(File apkfile);

        void downloadFailed(String error);

        void downloadCancle(String str);
    }

    /**
     * 构造函数
     *
     * @param context
     */
    public DialogDownload(Context context, OnDownLoadCompletedListener listener) {
        mContext = context;
        mDownLoadBuilder = new CommonDownloadDialog(context);
        mListener = listener;
    }

    /**
     * 构造函数
     *
     * @param context
     */
    public DialogDownload(Context context, OnDownLoadCompletedListener listener, View currentFocus) {
        mContext = context;
        mDownLoadBuilder = new CommonDownloadDialog(context);
        mDownLoadBuilder.setCurrentView(currentFocus);
        mListener = listener;
    }

    /**
     * 更新进度条handler
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                // 下载中
                case DOWNLOAD:
                    // 更新进度
                    mDownLoadBuilder.setProgressBarInfo(mProgress);
                    break;
                // 下载结束
                case DOWNLOAD_FINISH:
                    // 关闭线程
                    if (mDownThread.isAlive()) {
                        mDownThread.stopThread(true);
                    }
                    if (mListener != null)
                        mListener.downloadSuccess(apkfile);
                    // 关闭下载对话框
                    if(mContext instanceof Activity && mContext != null && !((Activity) mContext).isFinishing()) {
                        mDownLoadBuilder.dismiss();
                    }
                    break;
                // 下载失败
                case DOWNLOAD_FAIL:
                    // 关闭线程
                    if (mDownThread.isAlive()) {
                        mDownThread.stopThread(true);
                    }
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.download_apk_txt_01));
                    if (mListener != null)
                        mListener.downloadFailed(mContext.getResources().getString(R.string.download_apk_txt_02));
                    // 关闭下载对话框
                    if(mContext instanceof Activity && mContext != null && !((Activity) mContext).isFinishing()) {
                        mDownLoadBuilder.dismiss();
                    }
                    break;
                case DOWNLOAD_CANLE://取消下载
                    // 关闭线程
                    if (mDownThread.isAlive()) {
                        mDownThread.stopThread(true);
                    }
                    if (mListener != null)
                        mListener.downloadCancle(mContext.getResources().getString(R.string.download_apk_txt_03));
                    // 关闭下载对话框
                    if(mContext instanceof Activity && mContext != null && !((Activity) mContext).isFinishing()) {
                        mDownLoadBuilder.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 选择下载提示框 SD卡存在并可用，启动下载线程
     */
    public void showChoiceDownLoadDialog(String url) {
        downloaUrl = url;
        if(!downloaUrl.endsWith(".apk")){
            DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.download_apk_txt_05));
            return;
        }
        if (FileManager.hasSDCard()) {
            mDownLoadBuilder.setButtonCancleListener(new CommonDownloadDialog.OnCancleListener() {
                @Override
                public void cancle() {
                    isInstal = true;
                    handler.sendEmptyMessage(DOWNLOAD_CANLE);
                }
            });
            // 创建下载对话框并显示
            mDownLoadBuilder.show();
            DownLoad();
        } else {
            DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.download_apk_txt_04));
        }
    }

    /**
     * 启动下载线程
     */
    private void DownLoad() {
        mDownThread = new DownThread();
        mDownThread.start();
    }

    /**
     * 下载线程内部类
     *
     * @author Administrator
     */
    class DownThread extends Thread {
        private boolean _run = true;

        public void stopThread(boolean run) {
            this._run = !run;
            Thread thread = Thread.currentThread();
            if (thread != null) {
                thread = null;
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                if (_run) {
                    if (!FileManager.hasSDCard()) {
                        handler.sendEmptyMessage(NOSDCARD);
                        return;
                    }

                    URL url = new URL(DecodeURIUtil.encodeURIComponent(downloaUrl));
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setConnectTimeout(DOWNLOAD_CONNECT_TIME_OUT);
                    connection.setReadTimeout(DOWNLOAD_READ_TIME_OUT);
                    connection.setRequestProperty("Content-type", "text/html");
                    connection.setRequestProperty("contentType", "utf-8");
                    connection.setRequestProperty("Accept-Charset", "utf-8");
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    fileSize = connection.getContentLength();
                    String fileName = downloaUrl.substring(downloaUrl
                            .lastIndexOf("/") + 1);
                    String savaPath = DAppConstants.PATH_DOWNLOAD_FILE;
                    apkfile = new File(Environment.getExternalStorageDirectory() + savaPath + fileName);
                    if (apkfile.exists()) {
                        apkfile.delete();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            apkfile);
                    byte[] buffer = new byte[fileSize];

                    // 读取服务端文件并保存在本地
                    while ((numread = inputStream.read(buffer)) != -1) {
                        count += numread;
                        mProgress = (int) (((float) count / fileSize) * 100);
                        handler.sendEmptyMessage(DOWNLOAD);// 发送下载进度
                        // 保存文件
                        fileOutputStream.write(buffer, 0, numread);
                        if (isInstal) {
                            break;
                        }
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    if (!isInstal) {//判断是否正常（取消）
                        // 判断是否下载完成，当下载的内容小于文件大小或者为0时
                        if ((count < fileSize) || (count == 0)) {
                            handler.sendEmptyMessage(DOWNLOAD_FAIL);
                        } else {
                            handler.sendEmptyMessage(DOWNLOAD_FINISH);
                        }
                    }
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(DOWNLOAD_FAIL);
            }
        }
    }
}
