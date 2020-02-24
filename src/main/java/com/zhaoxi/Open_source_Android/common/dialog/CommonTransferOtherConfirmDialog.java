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
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;

/**
 * des:第三方支付转账确认页面
 * Created by ztt on 2018/6/21.
 */

public class CommonTransferOtherConfirmDialog extends LinearLayout {
    private BaseActivity mContext;
    private View mCurrentFocusView;
    private PopupWindow mPopupWindow;

    private ImageView mImgClose;
    private TextView mTxtToAddress, mTxtFormAddress, mTxtMoney, mTxtFee, mTxtDes;
    private Button mBtnSubmit;
    private BigDecimal mCurAcountMoney;
    private BigDecimal mPayMoney;

    public interface OnSubmitListener {
        void setOnSubmitListener(String address);
    }


    public void setOnSubmitListener(OnSubmitListener mOnSubmitListener) {
        this.mOnSubmitListener = mOnSubmitListener;
    }

    private OnSubmitListener mOnSubmitListener;

    public CommonTransferOtherConfirmDialog(BaseActivity activity, View currentFocusView) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        LayoutInflater.from(activity).inflate(R.layout.view_transfer_comfirm_other_dialog, this);
        mImgClose = findViewById(R.id.transfer_other_dialog_img_close);
        mTxtToAddress = findViewById(R.id.transfer_other_dialog_txt_to_address);
        mTxtFormAddress = findViewById(R.id.transfer_other_dialog_txt_form_address);
        mTxtMoney = findViewById(R.id.transfer_other_dialog_txt_money);
        mTxtFee = findViewById(R.id.transfe_other_dialog_txt_fee);
        mBtnSubmit = findViewById(R.id.transfer_other_dialog_btn_submit);
        mTxtDes = findViewById(R.id.transfe_other_dialog_txt_des);

        mImgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBtnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MoneyHandle()) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_05));
                    return;
                }
                mOnSubmitListener.setOnSubmitListener(mTxtFormAddress.getText().toString());
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

    public void show(BigDecimal curMoney, String money, String fee, String toAddress, String des) {
        mCurAcountMoney = curMoney;
        mPayMoney = new BigDecimal(money);
        mTxtToAddress.setText(toAddress);
        String fromaddress = SharedPreferencesUtil.getSharePreString(mContext, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mTxtFormAddress.setText(fromaddress);
        String payMoney = money;
        float size = 25f;
        if (payMoney.length() <= 20) {
            size = 25f;
        } else if (payMoney.length() > 20 && payMoney.length() <= 23) {
            size = 23f;
        } else if (payMoney.length() > 23 && payMoney.length() <= 26) {
            size = 20f;
        } else
            size = 14f;
        mTxtMoney.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        mTxtMoney.setText(payMoney + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_01));
        mTxtFee.setText(fee);
        mTxtDes.setText(StrUtil.isEmpty(des) ? "暂无描述" : des);
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 计算转账金额是否小于剩余金额
     *
     * @return
     */
    private boolean MoneyHandle() {
        String fee = mTxtFee.getText().toString();
        BigDecimal money1 = Convert.toWei(mPayMoney, Convert.Unit.ETHER).add(new BigDecimal(fee));
        if (money1.compareTo(Convert.toWei(mCurAcountMoney, Convert.Unit.ETHER)) > 0) {
            return false;
        }
        return true;
    }


    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
