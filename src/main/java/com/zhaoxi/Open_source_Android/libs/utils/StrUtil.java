package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * des:
 * Created by ztt on 2018/6/5.
 */

public class StrUtil {
    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0);
    }

    public static boolean isNull(String str) {
        return str == null;
    }


    /**
     * 地址中间对齐
     * @param address
     * @return
     */
    public static String addressAlign(String address) {
        try {
            String str = address.substring(0, 22) + "\n"
                    + address.substring(address.length() - 22, address.length());
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化地址
     *
     * @param address
     * @return
     */
    public static String formatAddress(String address) {
        if(!StrUtil.isEmpty(address)){
            String preStr = address.substring(0, 5);
            String rearStr = address.substring(address.length() - 5);
            address = preStr
                    + DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.fragment_main_home_ellipsis)
                    + rearStr;
        }
        return address;
    }


    /**
     * 地址截取 前6后6
     *
     * @param address
     * @return
     */
    public static String addressFilter(String address) {
        try {
            String str = address.substring(0, 6) + "......"
                    + address.substring(address.length() - 6, address.length());
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    public static String addressFilte10r(String address) {
        try {
            String str = address.substring(0, 5) + "..."
                    + address.substring(address.length() - 5, address.length());
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    public static String readContent(Context context, int count) {
        String str = context.getResources().getString(R.string.fragment_find_nwes_read_content);
        String resultStr = null;
        //判断中英文
        int curType = ChangeLanguageUtil.languageType(context);
        if (curType == 1) {
            if (count >= 10000) {
                resultStr = count % 10000 + context.getResources().getString(R.string.fragment_find_nwes_read_content_unit) + " " + str;
            } else {
                resultStr = count + " " + str;
            }
        } else {
            if (count >= 1000) {
                resultStr = count % 1000 + context.getResources().getString(R.string.fragment_find_nwes_read_content_unit) + " " + str;
            } else {
                resultStr = count + " " + str;
            }
        }
        return resultStr;
    }

    /**
     * 禁止EditText输入空格 特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpaChat(EditText editText) {
        InputFilter inputFilter = new InputFilter() {

            Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher = pattern.matcher(charSequence);
                if (!matcher.find()) {
                    return null;
                } else {
                    return "";
                }

            }
        };
        editText.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(20)});
    }

    /**
     * 只能输入字母+数字
     *
     * @param editText
     */
    public static void setEditTextInputSpaChat(EditText editText) {
        InputFilter inputFilter = new InputFilter() {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher = pattern.matcher(charSequence);
                if (!matcher.find()) {
                    return null;
                } else {
                    return "";
                }

            }
        };
        editText.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(42)});
    }

    /**
     * 判断是否为小写字母
     * @param str
     * @return
     */
    public static boolean isCheckSpaChat(String str) {
        Pattern pattern = Pattern.compile("[^a-z]");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否纯数字
     * @param str
     * @return
     */
    public static boolean isCheckAllNum(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum  = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 获取钱包名称首字母
     * @param name
     * @return
     */
    public static String getWalletHeaderName(String name) {
        String mFisetN = "";
        if(!StrUtil.isEmpty(name)){
            String headerName = name.substring(0, 1);
            if(!StrUtil.isCheckSpaChat(headerName)){
                //判断是否为小写字母
                mFisetN = headerName.toUpperCase();;
            }else{
                mFisetN = headerName;
            }
        }
        return mFisetN;
    }
}
