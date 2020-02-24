package com.zhaoxi.Open_source_Android.ui.dialog;

import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
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
 * @des 分红比例设置
 * @date 2019/9/11.
 */

public class NodeDividendBLPop {
    private BaseActivity activity;
    private PopupWindow popupWindow;
    private View mCurrentFocusView;
    private HandleData mHandleData;

    private TextView mBtnPositive,mBtnCancle;
    private EditText mEditBiLie;

    public NodeDividendBLPop(BaseActivity activity, View currentFocusView) {
        this.activity = activity;
        this.mCurrentFocusView = currentFocusView;
        initPopupWindow();
    }

    private void initPopupWindow(){
        View view = LayoutInflater.from(activity).inflate(R.layout.view_nodedividend_bl_dialog, null, false);
        mEditBiLie = view.findViewById(R.id.dialog_bl_edit_bilv);
        mBtnPositive = view.findViewById(R.id.common_bl_dialog_btn_ok);
        mBtnCancle = view.findViewById(R.id.common_bl_dialog_btn_cancle);
        mEditBiLie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!"".equals(text) && Integer.valueOf(text) > 100) {
                    mEditBiLie.setText("100");
                    mEditBiLie.setSelection(3);
                    DappApplication.getInstance().showToast(activity.getResources().getString(R.string.activity_cion_fh_txt_32));
                }
            }
        });

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
                String bilie = mEditBiLie.getText().toString();
                if (StrUtil.isEmpty(bilie)) {
                    DappApplication.getInstance().showToast(activity.getResources().getString(R.string.activity_cion_fh_txt_31));
                    return;
                }
                //大于0  小于100
                if(Integer.valueOf(bilie)>100){
                    DappApplication.getInstance().showToast(activity.getResources().getString(R.string.activity_cion_fh_txt_32));
                    return;
                }
                mHandleData.getData(bilie);
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

    public interface HandleData {
        void getData(String bilie);
    }

    public void setHandleData(HandleData handleData) {
        this.mHandleData = handleData;
    }

    public void show(String bilie){
        mEditBiLie.setText(bilie);
        mEditBiLie.setSelection(bilie.length());
        popupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }
}
