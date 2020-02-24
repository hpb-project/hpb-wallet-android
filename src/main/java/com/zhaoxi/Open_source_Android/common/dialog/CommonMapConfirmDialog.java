package com.zhaoxi.Open_source_Android.common.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;

/**
 * des:映射确认页面
 * Created by ztt on 2018/6/21.
 */

public class CommonMapConfirmDialog extends LinearLayout {
    private BaseActivity mContext;
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;

    private ImageView mImgClose;
    private TextView mTxtToAddress, mTxtFormAddress, mTxtMoney, mTxtFee;
    private Button mBtnSubmit;

    public interface OnSubmitListener {
        void setOnSubmitListener(String money);
    }


    public void setOnSubmitListener(OnSubmitListener mOnSubmitListener) {
        this.mOnSubmitListener = mOnSubmitListener;
    }

    private OnSubmitListener mOnSubmitListener;

    public CommonMapConfirmDialog(BaseActivity activity, View currentFocusView) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.view_map_comfirm_dialog, this);
        mImgClose = findViewById(R.id.map_transfer_dialog_img_close);
        mTxtToAddress = findViewById(R.id.map_transfer_dialog_txt_to_address);
        mTxtFormAddress = findViewById(R.id.map_transfer_dialog_txt_from_address);
        mTxtMoney = findViewById(R.id.map_transfer_dialog_txt_money);
        mTxtFee = findViewById(R.id.map_transfer_dialog_txt_fee);
        mBtnSubmit = findViewById(R.id.map_transfer_dialog_btn_submit);

        mImgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSubmitListener.setOnSubmitListener(mTxtMoney.getText().toString());
                dismiss();
            }
        });
        initDialogPopupWindow();
    }

    private void initDialogPopupWindow() {
        mPopupWindow = new PopupWindow(this,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(dw);
    }

    public void show(String money, String toAddress, String fromAddress, String fee,int style) {
        mTxtToAddress.setText(toAddress);
        mTxtFormAddress.setText(fromAddress);
//        String mm = "";
//        if(style == 0){
//            mm = money.replace(",", "");
//        }else{
//            if(money.contains(",")){
//                String m1 = money.replace(".", ",");
//                StringBuilder sb = new StringBuilder(m1);
//                int index = m1.lastIndexOf(",");
//                mm = (sb.replace(index, index+1, ".")).toString();
//            }else{
//                mm = money;
//            }
//        }
//        mm = mm.replace(",", "");
        String curMoney = money;
        float size = 25f;
        if (curMoney.length() <= 20) {
            size = 25f;
        } else if (curMoney.length() > 20 && curMoney.length() <= 23) {
            size = 23f;
        } else if(curMoney.length() > 23 && curMoney.length() <= 26){
            size = 20f;
        }else
            size = 14f;
        mTxtMoney.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        mTxtMoney.setText(curMoney);
        mTxtFee.setText(fee);
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
