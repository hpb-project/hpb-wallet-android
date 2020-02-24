package com.zhaoxi.Open_source_Android.common.dialog;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

/**
 * @author ztt
 * @des 输入密码框
 * @date 2019/9/11.
 */

public class CommonPsdPop {
    private BaseActivity activity;
    private PopupWindow popupWindow;
    private View mCurrentFocusView;
    private HandlePsd mHandlePsd;

    private TextView mTxtTitle,mTxtMsg;
    private TextView mBtnPositive,mBtnCancle;
    private EditText mEditPsd;

    public CommonPsdPop(BaseActivity activity, View currentFocusView) {
        this.activity = activity;
        this.mCurrentFocusView = currentFocusView;
        initPopupWindow();
    }

    private void initPopupWindow(){
        View view = LayoutInflater.from(activity).inflate(R.layout.view_common_psd_dialog, null, false);
        mTxtTitle = view.findViewById(R.id.dialog_common_edit_txt_title);
        mTxtMsg = view.findViewById(R.id.dialog_common_edit_txt_msg);
        mEditPsd = view.findViewById(R.id.common_tip_edit_psd);
        mBtnPositive = view.findViewById(R.id.common_notitle_dialog_btn_ok);
        mBtnCancle = view.findViewById(R.id.common_notitle_dialog_btn_cancle);

        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.7f;
        activity.getWindow().setAttributes(lp);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        mBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psd = mEditPsd.getText().toString();
                if (StrUtil.isEmpty(psd)) {
                    DappApplication.getInstance().showToast(activity.getResources().getString(R.string.dailog_psd_edit_hint));
                    return;
                }
                mHandlePsd.getInputPsd(psd);
                popupWindow.dismiss();
            }
        });

        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public boolean isShowing(){
        return popupWindow.isShowing();
    }

    public interface HandlePsd {
        void getInputPsd(String psd);
    }

    public void setHandlePsd(HandlePsd mHandlePsd) {
        this.mHandlePsd = mHandlePsd;
    }

    public void show(String content){
        show(null,content);
    }

    public void show(String title,String content){
        if(title != null){
            mTxtTitle.setText(title);
        }else  mTxtTitle.setText(activity.getString(R.string.dialog_common_title_text));

        if(content != null){
            mTxtMsg.setText(content);
        }else  mTxtMsg.setText(activity.getString(R.string.dialog_common_edit_defult_msg));

        popupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }
}
