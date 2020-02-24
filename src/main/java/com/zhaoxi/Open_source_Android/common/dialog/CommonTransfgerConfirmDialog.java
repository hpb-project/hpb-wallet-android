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
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

/**
 * des:转账确认页面
 * Created by ztt on 2018/6/21.
 */

public class CommonTransfgerConfirmDialog extends LinearLayout {
    private BaseActivity mContext;
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;

    private ImageView mImgClose;
    private TextView mTxtToAddress, mTxtFormAddress, mTxtMoney, mTxtFee;
    private Button mBtnSubmit;
    private final TextView mTvTokenType;
    private final TextView mTransferTitle;

    public interface OnSubmitListener {
        void setOnSubmitListener(String money);
    }

    public interface OnDismissListener {
        void onDismiss();
    }


    public void setOnSubmitListener(OnSubmitListener mOnSubmitListener) {
        this.mOnSubmitListener = mOnSubmitListener;
    }

    public void setOnDismissListener(OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }

    private OnSubmitListener mOnSubmitListener;

    private OnDismissListener mOnDismissListener;

    public CommonTransfgerConfirmDialog(BaseActivity activity, View currentFocusView) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.view_transfer_comfirm_dialog, this);
        mImgClose = findViewById(R.id.transfer_dialog_img_close);
        mTxtToAddress = findViewById(R.id.transfer_dialog_txt_to_address);
        mTxtFormAddress = findViewById(R.id.transfer_dialog_txt_form_address);
        mTxtMoney = findViewById(R.id.transfer_dialog_txt_money);
        mTxtFee = findViewById(R.id.transfe_dialog_txt_fee);
        mBtnSubmit = findViewById(R.id.transfer_dialog_btn_submit);
        mTvTokenType = findViewById(R.id.transfer_dialog_txt_token_type);
        mTransferTitle = findViewById(R.id.transfer_tv_token_721_title);

        mImgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDismissListener != null) {
                    mOnDismissListener.onDismiss();
                }
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
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(dw);
    }

    public void setTokenTypeName(CharSequence tokenTypeName) {
        mTvTokenType.setText(tokenTypeName);
    }

    public void setTransferTitleVisibility() {
        mTransferTitle.setVisibility(VISIBLE);
        mTvTokenType.setVisibility(GONE);

    }

    public void setTokenTypeNameVisibility() {
        mTransferTitle.setVisibility(GONE);
        mTvTokenType.setVisibility(VISIBLE);
    }

    public void show(String money, String fee, String toAddress, int style) {
        mTxtToAddress.setText(toAddress);
        String fromaddress = SharedPreferencesUtil.getSharePreString(mContext, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mTxtFormAddress.setText(fromaddress);
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
        } else if (curMoney.length() > 23 && curMoney.length() <= 26) {
            size = 20f;
        } else
            size = 14f;
        mTxtMoney.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        if (mTransferTitle.getVisibility() == VISIBLE) {
            mTransferTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
        mTxtMoney.setText(curMoney);
        mTxtFee.setText(fee);
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }


    public void show(String money, String fee, String toAddress, String fromAddress, int style) {
        mTxtToAddress.setText(toAddress);
        mTxtFormAddress.setText(fromAddress);
//        String mm = "";
//        if(style == 0){
//            mm = money.replace(",", "");
//        }else{
//            String m1 = money.replace(".", ",");
//            StringBuilder sb = new StringBuilder(m1);
//            int index = m1.lastIndexOf(",");
//            mm = (sb.replace(index, index+1, ".")).toString();
//        }
        String curMoney = money;
//        String curMoney = SlinUtil.NumberFormat8(mContext,new BigDecimal(mm));
        float size = 25f;
        if (curMoney.length() <= 20) {
            size = 25f;
        } else if (curMoney.length() > 20 && curMoney.length() <= 23) {
            size = 23f;
        } else if (curMoney.length() > 23 && curMoney.length() <= 26) {
            size = 20f;
        } else
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
