package com.zhaoxi.Open_source_Android.libs.tools;

import android.content.DialogInterface;
import android.view.WindowManager;

import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.dialog.CommonTipDialog;

/**
 * des:
 * Created by ztt on 2018/6/22.
 */

public class CommonDilogTool {
    private CommonTipDialog.Builder mAlterDialog;
    private CommonTipDialog mDialog;
    private BaseActivity mActivity;

    public CommonDilogTool(BaseActivity activity){
        mActivity = activity;
        mAlterDialog = new CommonTipDialog.Builder(activity);
    }

    public void show(String title,String msg1,String msg2,String leftTxt,DialogInterface.OnClickListener leftlistener,
                     String rightTxt,DialogInterface.OnClickListener rightlistener){
        mAlterDialog.setTitle(title);
        mAlterDialog.setMessage(msg1);
        mAlterDialog.setMessage2(msg2);
        mAlterDialog.setNegativeButton(leftTxt,leftlistener);
        mAlterDialog.setPositiveButton(rightTxt,rightlistener);
        mDialog = mAlterDialog.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = (int)(mActivity.getWindowManager().getDefaultDisplay().getWidth()*0.8);
        mDialog.getWindow().setAttributes(params);
    }

    public void show(String title,String msg1,String msg2,String leftTxt,DialogInterface.OnClickListener leftlistener,
                     String rightTxt,DialogInterface.OnClickListener rightlistener,boolean isTouchOutside){
        mAlterDialog.setTitle(title);
        mAlterDialog.setMessage(msg1);
        mAlterDialog.setMessage2(msg2);
        mAlterDialog.setNegativeButton(leftTxt,leftlistener);
        mAlterDialog.setPositiveButton(rightTxt,rightlistener);
        mDialog = mAlterDialog.create();
        mDialog.setCanceledOnTouchOutside(isTouchOutside);
        mDialog.setCancelable(isTouchOutside);
        mDialog.show();

        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = (int)(mActivity.getWindowManager().getDefaultDisplay().getWidth()*0.8);
        mDialog.getWindow().setAttributes(params);
    }

    public void dismiss(){
        mDialog.dismiss();
    }
}
