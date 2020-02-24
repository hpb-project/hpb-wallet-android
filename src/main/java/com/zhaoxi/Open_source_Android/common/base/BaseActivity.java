package com.zhaoxi.Open_source_Android.common.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.gyf.immersionbar.ImmersionBar;
import com.umeng.message.PushAgent;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.dialog.CommonDialogProgress;
import com.zhaoxi.Open_source_Android.common.dialog.CommonProgressDialog;
import com.zhaoxi.Open_source_Android.ui.dialog.RedHasKLDialog;
import com.zhaoxi.Open_source_Android.libs.utils.ClipboardUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.activity.GetRedActivity;

public class BaseActivity extends FragmentActivity {
    /**
     * 请求服务器加载框
     */
    public CommonProgressDialog mProgressDialog = null;
    private CommonDialogProgress.Builder mDialogBuilder = null;
    private CommonDialogProgress mDialog = null;
    private RedHasKLDialog mRedDialog = null;
    public boolean mShowCopy = true;
    private String mCopyContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mProgressDialog = new CommonProgressDialog(this, false, false);
        mDialogBuilder = new CommonDialogProgress.Builder(this);
        // 设置为U-APP场景
        ImmersionBar.with(this).init();
    }

    @Override
    protected void onDestroy() {
        //判断无文字加载进度条是否正在显示
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog = null;

        //判断带文字加载进度条是否存在并正在显示
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog = null;
        }
        super.onDestroy();
    }

    /**
     * 显示带文字的加载进度条
     *
     * @param message
     */
    public void showTextProgressDialog(String message) {
        mDialogBuilder.setMassage(message);
        mDialog = mDialogBuilder.create();
        mDialog.show();
    }

    /**
     * 取消带文字的加载进度条
     */
    public void dismissTextProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public boolean isShowDialog() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }else return false;
    }

    /**
     * 显示加载进度条
     */
    public void showProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.show();
    }

    /**
     * 取消加载进度条
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void copyLable(String content) {
        //复制地址
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        DappApplication.getInstance().showToast(getResources().getString(R.string.export_private_key_toast_copy));
    }

    // BaseActivity中统一调用MobclickAgent 类的 onResume/onPause 接口
    // 子类中无需再调用
    @Override
    protected void onResume() {
        super.onResume();
        if (mShowCopy) {
            mCopyContent = ClipboardUtil.GetClipboardContent(this);
            if (!StrUtil.isEmpty(mCopyContent)) {
                //弹出框
                showGoCopyDialog(mCopyContent);
            }
        }
    }


    private void showGoCopyDialog(String content) {
        String[] ct = content.split("hpbrp");
        String redId = ct[0];
        String redKey = ct[1];
        if (mRedDialog != null && mRedDialog.isShowing()) {
            mRedDialog.dismiss();
            mRedDialog = null;
        }
        RedHasKLDialog.Builder builder = new RedHasKLDialog.Builder(BaseActivity.this);
        builder.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent goto_details = new Intent(BaseActivity.this, GetRedActivity.class);
                goto_details.putExtra(GetRedActivity.RED_RECORD_ID, redId);
                goto_details.putExtra(GetRedActivity.RED_RECORD_KEY, redKey);
                startActivity(goto_details);
                ClipboardUtil.copyLable(BaseActivity.this, "");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardUtil.copyLable(BaseActivity.this, "");
                dialog.dismiss();
            }
        });
        mRedDialog = builder.create();
        mRedDialog.show();

        WindowManager.LayoutParams params = mRedDialog.getWindow().getAttributes();
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 1);
        mRedDialog.getWindow().setAttributes(params);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean moveTaskToBack(boolean nonRoot) {
        return super.moveTaskToBack(true);
    }
}
