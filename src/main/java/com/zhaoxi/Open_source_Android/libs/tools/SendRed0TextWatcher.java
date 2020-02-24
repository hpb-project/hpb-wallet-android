package com.zhaoxi.Open_source_Android.libs.tools;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.math.BigDecimal;

/**
 * des:
 * Created by ztt on 2018/11/6.
 */

public class SendRed0TextWatcher implements TextWatcher {
    private Context mContext;
    private int mRedType, mNumStyle;
    private Button mBtn;
    private EditText mEditMoney, mEditNum;
    private TextView mTxtShowMoney;
    private String mOldContent;

    public SendRed0TextWatcher(Context context, int type, Button button,
                               EditText editText, EditText editNum,
                               TextView txtshowMony, int numStyle) {
        mContext = context;
        mRedType = type;
        mBtn = button;
        mEditMoney = editText;
        mEditNum = editNum;
        mTxtShowMoney = txtshowMony;
        mNumStyle = numStyle;
    }

    public void setRedType(int type) {
        mRedType = type;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mOldContent = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String num = mEditNum.getText().toString();
        if (s.length() > 0 && num.length() > 0) {
            mBtn.setEnabled(true);
            mBtn.setTextColor(mContext.getResources().getColor(R.color.white));
            mBtn.setBackgroundResource(R.drawable.draw_btn_defult_bg_03);
        } else {
            mBtn.setEnabled(false);
            mBtn.setTextColor(mContext.getResources().getColor(R.color.color_2E2F47));
            mBtn.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
        }

        String text = s.toString();

        // 输入为0.的时候 光标移到最末
        if (text.equals("0."))
            Selection.setSelection((Spannable) s, s.length());
        //输入00时 改为0.
        if (text.equals("00")) {
            String text2 = "0.";
            mEditMoney.setText(text2);
            return;
        }
        // 直接输入.的时候 改为0.
        if (text.equals(".")) {
            String text3 = "0.";
            mEditMoney.setText(text3);
            return;
        }

        //去掉多余的.
        if (!StrUtil.isEmpty(text)) {
            int ct = text.length() - text.replace(".", "").length();
            if (ct > 1) {
                mEditMoney.setText(mOldContent);
                mEditMoney.setSelection(mOldContent.length()-1);
                return;
            }
        }

        // 限制小数点后 只能输入2位
        int posDot = text.indexOf(".");
        if (posDot > 0 && text.length() - posDot - 1 > 2) {
            s.delete(posDot + 3, posDot + 4);
            return;
        }

        if (mRedType == 2) {
            String zzMaoney = text;
            if (!StrUtil.isEmpty(text)) {
                if (mNumStyle == 1 && zzMaoney.contains(",")) {
                    zzMaoney = zzMaoney.replace(",", ".");
                }
                if (!zzMaoney.equals("0.")) {
                    zzMaoney = SlinUtil.tailClearAll(mContext, SlinUtil.NumberFormat2(mContext, new BigDecimal(zzMaoney)));
                } else {
                    zzMaoney = "0";
                }
            } else {
                zzMaoney = "0";
            }
            mTxtShowMoney.setText(zzMaoney);
//            mTxtShowMoney.setText(text);
        } else {
            String curMoney = text;
            if (mNumStyle == 1) {//,
                if (text.contains(",")) {
                    curMoney = text.replace(",", ".");
                }
            }
            String txtMoney = "0";
            if (!StrUtil.isEmpty(curMoney) && !StrUtil.isEmpty(num)) {
                if (!curMoney.equals("0.")) {
                    BigDecimal showMoney = new BigDecimal(curMoney).multiply(new BigDecimal(num));
                    txtMoney = String.valueOf(showMoney);
                    if (mNumStyle == 1) {
                        if (txtMoney.contains(".")) {
                            txtMoney = txtMoney.replace(".", ",");
                        }
                    }
                } else txtMoney = "0";
            }
            String total = txtMoney;
            if (mNumStyle == 1 && txtMoney.contains(",")) {
                total = txtMoney.replace(",", ".");
            }
            String ssTol = SlinUtil.tailClearAll(mContext, total);
            if (mNumStyle == 1 && ssTol.contains(".")) {
                ssTol = ssTol.replace(".", ",");
            }

//            mTxtShowMoney.setText(ssTol);

            //------
            String zMoney = ssTol;
            if (mNumStyle == 1 && zMoney.contains(",")) {
                zMoney = zMoney.replace(",", ".");
            }
            zMoney = SlinUtil.tailClearAll(mContext, SlinUtil.NumberFormat2(mContext, new BigDecimal(zMoney)));
            mTxtShowMoney.setText(zMoney);
        }

        if (!"".equals(text)) {
            if (mRedType == 1) {//普通
                if (!text.equals(".") && (Float.valueOf(text) >= 100 && text.contains(".")) || (Float.valueOf(text) > 100 && !text.contains("."))) {
                    mEditMoney.setText("100");
                    mEditMoney.setSelection(3);
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_red_send_toast_06));
                }
            } else {
                if (!text.equals(".") && ((Float.valueOf(text) >= 10000 && text.contains(".")) || (Float.valueOf(text) > 10000 && !text.contains(".")))) {
                    mEditMoney.setText("10000");
                    mEditMoney.setSelection(5);
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_red_send_toast_03));
                }
            }
        }
    }
}
