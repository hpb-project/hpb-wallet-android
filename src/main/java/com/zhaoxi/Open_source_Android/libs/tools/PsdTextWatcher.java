package com.zhaoxi.Open_source_Android.libs.tools;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.utils.CheckPswMeter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * des:
 * Created by ztt on 2018/6/12.
 */

public class PsdTextWatcher implements TextWatcher{
    int length = 0;
    private BaseActivity mActivity;
    private EditText mEditPsd;
    private TextView mTxtPsdWarn;
    private ImageView mImgEyesOne;
    private TextView mTxtStrength;
    public PsdTextWatcher(BaseActivity activity, EditText editPsd, TextView txtPsdWarn, ImageView imgEyesOne, TextView txtStrength){
        this.mActivity = activity;
        this.mEditPsd = editPsd;
        this.mTxtPsdWarn = txtPsdWarn;
        this.mImgEyesOne = imgEyesOne;
        this.mTxtStrength = txtStrength;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String txt = s.toString();
        //注意返回值是char数组
        char[] stringArr = txt.toCharArray();
        for (int i = 0; i < stringArr.length; i++) {
            //转化为string
            String value = new String(String.valueOf(stringArr[i]));
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(value);
            if (m.matches()) {
                mEditPsd.setText(mEditPsd.getText().toString().substring(0, mEditPsd.getText().toString().length() - 1));
                mEditPsd.setSelection(mEditPsd.getText().toString().length());
                return;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        length = s.length();
        if(length>0){
            mTxtPsdWarn.setVisibility(View.INVISIBLE);
            mImgEyesOne.setVisibility(View.VISIBLE);
            mTxtStrength.setVisibility(View.VISIBLE);
        }else{
            mImgEyesOne.setVisibility(View.GONE);
            mTxtStrength.setVisibility(View.GONE);
            mTxtPsdWarn.setVisibility(View.VISIBLE);
        }
        int result = CheckPswMeter.checkStrong(s.toString());
        if(result == 1){//弱
            mTxtStrength.setTextColor(mActivity.getResources().getColor(R.color.color_FF4465));
            mTxtStrength.setText(mActivity.getResources().getString(R.string.create_wallet_txt_strgenth_01));
        }else if(result == 2){//中
            mTxtStrength.setTextColor(mActivity.getResources().getColor(R.color.color_FF7416));
            mTxtStrength.setText(mActivity.getResources().getString(R.string.create_wallet_txt_strgenth_02));
        }else{//强
            mTxtStrength.setTextColor(mActivity.getResources().getColor(R.color.color_29CF7D));
            mTxtStrength.setText(mActivity.getResources().getString(R.string.create_wallet_txt_strgenth_03));
        }
    }
}
