package com.zhaoxi.Open_source_Android.libs.tools;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

/**
 * des:
 * Created by ztt on 2018/11/6.
 */

public class Number1TextWatcher implements TextWatcher {
    private Context mContext;
    private Button mBtn;
    private EditText mEdit;
    private String mOldContent;

    public Number1TextWatcher(Context context, Button button, EditText editText) {
        mContext = context;
        mBtn = button;
        mEdit = editText;
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
        if (s.length() > 0) {
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

        if (text.equals("0,"))
            Selection.setSelection((Spannable) s, s.length());
        //输入00时 改为0，
        if (text.equals("00")) {
            String text2 = "0,";
            mEdit.setText(text2);
            return;
        }
        // 直接输入.的时候 改为0,
        if (text.equals(",")) {
            String text3 = "0,";
            mEdit.setText(text3);
            return;
        }

        //去掉多余的,
        if (!StrUtil.isEmpty(text)) {
            int ct = text.length() - text.replace(",", "").length();
            if (ct > 1) {
                mEdit.setText(mOldContent);
                mEdit.setSelection(mOldContent.length()-1);
                return;
            }
        }

        // 限制小数点后 只能输入8位
        int posDot = text.indexOf(",");
        if (posDot > 0 && text.length() - posDot - 1 > 8) {
            s.delete(posDot + 9, posDot + 10);
            return;
        }
    }
}
