package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;

import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class SlinUtil {
    /**
     * BigDecimal 保留小数
     *
     * @param value    数据
     * @param newScale 保留小数位
     * @return
     */
    public static BigDecimal ValueScale(BigDecimal value, int newScale) {
        if (value == null)
            return new BigDecimal("0");
        return value.setScale(newScale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 格式化数字
     *
     * @param context
     * @param count
     * @return
     */
    public static String FormatNum(Context context, BigDecimal count) {
        BigDecimal money = Convert.fromWei(count, Convert.Unit.ETHER);
        int style = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        String curMoney = money.toString();
        if (style == 1) {
            if (curMoney.contains(".")) {
                curMoney = curMoney.replace(".", ",");
            }
        }
        return curMoney;
    }

    public static String formatNumFromWei(BigDecimal count, int decimals){
        Convert.Unit fromWei = Convert.Unit.ETHER;
        switch (decimals) {
            case 0:
                fromWei = Convert.Unit.WEI;
                break;
            case 3:
                fromWei = Convert.Unit.KWEI;
                break;
            case 6:
                fromWei = Convert.Unit.MWEI;
                break;
            case 8:
                fromWei = Convert.Unit.EWEI;
                break;
            case 9:
                fromWei = Convert.Unit.GWEI;
                break;
            case 12:
                fromWei = Convert.Unit.SZABO;
                break;
            case 15:
                fromWei = Convert.Unit.FINNEY;
                break;
            case 18:
                fromWei = Convert.Unit.ETHER;
                break;
            case 21:
                fromWei = Convert.Unit.KETHER;
                break;
            case 24:
                fromWei = Convert.Unit.METHER;
                break;
            case 27:
                fromWei = Convert.Unit.GETHER;
                break;
        }

        //step 1:÷ 位数
        BigDecimal money = Convert.fromWei(count, fromWei);
        if (money.compareTo(new BigDecimal("0")) == 0) {
            return "0";
        }
        //step 2: 保留八位
        BigDecimal value = ValueScale(money, 8);
        return value.toPlainString();
    }

    /**
     * 格式化数据
     *
     * @param context
     * @param count
     * @param decimals
     * @return
     */
    public static String formatNumFromWeiT(Context context, BigDecimal count, int decimals) {
        Convert.Unit fromWei = Convert.Unit.ETHER;
        switch (decimals) {
            case 0:
                fromWei = Convert.Unit.WEI;
                break;
            case 3:
                fromWei = Convert.Unit.KWEI;
                break;
            case 6:
                fromWei = Convert.Unit.MWEI;
                break;
            case 8:
                fromWei = Convert.Unit.EWEI;
                break;
            case 9:
                fromWei = Convert.Unit.GWEI;
                break;
            case 12:
                fromWei = Convert.Unit.SZABO;
                break;
            case 15:
                fromWei = Convert.Unit.FINNEY;
                break;
            case 18:
                fromWei = Convert.Unit.ETHER;
                break;
            case 21:
                fromWei = Convert.Unit.KETHER;
                break;
            case 24:
                fromWei = Convert.Unit.METHER;
                break;
            case 27:
                fromWei = Convert.Unit.GETHER;
                break;
        }

        //step 1:÷ 位数
        BigDecimal money = Convert.fromWei(count, fromWei);
        if (money.compareTo(new BigDecimal("0")) == 0) {
            return "0";
        }
        //step 2: 保留八位
        BigDecimal value = ValueScale(money, 8);
        //step 3.格式化
        int style = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        String data = formatNumStyle(value, style);
        //step 4:去0
        String result = tailClearAll(context, data);
        return result;
    }

    /**
     * 指定除以数据
     *
     * @param context
     * @param count
     * @return
     */
    public static String formatNumFromWeiS(Context context, BigDecimal count) {
        return formatNumFromWeiT(context, count, 18);
    }

    /**
     * 格式化数据
     *
     * @param text
     * @param style
     * @return
     */
    private static String formatNumStyle(BigDecimal text, int style) {
        DecimalFormat decimalFormat = null;
        if (text.compareTo(new BigDecimal("0")) == 0) {
            return "0";
        }
        String resultStr = "";
        if (style == 0) {
            decimalFormat = new DecimalFormat("#,##0.00000000");
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols());
            resultStr = decimalFormat.format(text);
        } else {
            if (text.compareTo(new BigDecimal("0")) == 0) {
                return "0";
            }
            resultStr = init2String(text.toPlainString());
        }
        return resultStr;
    }

    private static DecimalFormatSymbols decimalFormatSymbols() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        return symbols;
    }

    /**
     * 去掉多余的0
     *
     * @param context
     * @param data
     * @return
     */
    public static String tailClearAll(Context context, String data) {
        if (data.equals("0")) {
            return "0";
        }
        int style = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        String[] strArry;
        char separator;
        if (style == 0) {
            strArry = data.split("\\.");
            separator = '.';
        } else {
            strArry = data.split("\\,");
            separator = ',';
        }

        if (strArry.length < 2) {
            return data;
        }

        String hou = strArry[1];

        if (new BigDecimal(hou).compareTo(new BigDecimal("0")) == 0) {
            data = strArry[0];
        } else {
            for (int i = hou.length(); i >= 0; i--) {
                if (hou.endsWith("0")) {
                    hou = hou.substring(0, i - 1);
                } else break;
            }
            data = strArry[0] + separator + hou;
        }
        return data;
    }

    public static void main(String[] args) {
//        System.out.println("===> " + tailClear("10126.24685529"));
    }

    /**
     * 保留两位小数
     *
     * @param context
     * @param money
     * @return
     */
    public static String NumberFormat2(Context context, BigDecimal money) {
        BigDecimal value = ValueScale(money, 2);
        String resultStr = "";
        int style = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        if (style == 0) {
            if (value.compareTo(new BigDecimal("0")) == 0) {
                return "0.00";
            }
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols());
            resultStr = decimalFormat.format(value);
        } else {
            if (value.compareTo(new BigDecimal("0")) == 0) {
                return "0,00";
            }
            resultStr = init2String(value.toPlainString());
        }

        return resultStr;
    }

    public static String NumberFormat8(Context context, BigDecimal money) {
        BigDecimal value = ValueScale(money, 8);
        String resultStr = "";
        int style = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        DecimalFormat decimalFormat = null;
        if (style == 0) {
            if (value.compareTo(new BigDecimal("0")) == 0) {
                return "0.00000000";
            }
            decimalFormat = new DecimalFormat("#,##0.00000000");
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols());
            resultStr = decimalFormat.format(value);
        } else {
            if (value.compareTo(new BigDecimal("0")) == 0) {
                return "0,00000000";
            }
            resultStr = initString(value.toPlainString());
        }

        return resultStr;
    }

    /**
     * 格式化数据 100,00  整数
     *
     * @param context
     * @param value
     * @return
     */
    public static String NumberFormat0(Context context, BigDecimal value) {
        int style = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        String strValue = "";
        if (style == 0) {
            DecimalFormat nf = new DecimalFormat("#,##0");
            nf.setDecimalFormatSymbols(decimalFormatSymbols());
            strValue = nf.format(value);
        } else {
            strValue = displayWithComma(value.toString());
        }
        return strValue;
    }

    public static String FeeValueString(Context context, BigInteger gasPrice, BigInteger gasLimit) {
        BigDecimal fee = Convert.fromWei(new BigDecimal(gasLimit.multiply(gasPrice)), Convert.Unit.ETHER);
        int style = SharedPreferencesUtil.getSharePreInt(context, SharedPreferencesUtil.CHANGE_NUMBER_STYLE);
        String feeValue = "";
        if (style == 0) {
            feeValue = NumberFormat8(context, fee);
            if (feeValue.length() > 10) {
                feeValue = feeValue.substring(0, 10);
            }
        } else {
            String a = fee.toPlainString();
            if (a.length() > 10) {
                a = a.substring(0, 10);
            }
            feeValue = initString(a);
        }
        return feeValue;
    }

    /**
     * 数据格式化
     *
     * @param str
     * @return
     */
    public static String init2String(String str) {
        String[] strArry = str.split("\\.");
        String hou = "00";
        if (strArry.length > 1) {
            hou = strArry[1];
            if (strArry[1].length() < 2) {
                int offsetSize = 2 - strArry[1].length();
                hou = strArry[1] + String.valueOf((int) Math.pow(10, offsetSize)).substring(1);
            }
        }
        String resultStr = displayWithComma(strArry[0]) + "," + hou;
        return resultStr;
    }

    /**
     * 数据格式化
     *
     * @param str
     * @return
     */
    public static String initString(String str) {
        String[] strArry = str.split("\\.");
        String hou = "00000000";
        if (strArry.length > 1) {
            hou = strArry[1];
            if (strArry[1].length() < 8) {
                int offsetSize = 8 - strArry[1].length();
                hou = strArry[1] + String.valueOf((int) Math.pow(10, offsetSize)).substring(1);
            }
        }
        String resultStr = displayWithComma(strArry[0]) + "," + hou;
        return resultStr;
    }

    /**
     * 将字符串从右至左每三位加一逗号
     *
     * @param str 需要加逗号的字符串
     * @return 以从右至左每隔3位加一逗号显示
     */
    private static String displayWithComma(String str) {
        str = new StringBuffer(str).reverse().toString(); // 先将字符串颠倒顺序
        String str2 = "";
        int size = (str.length() % 3 == 0) ? (str.length() / 3) : (str.length() / 3 + 1); // 每三位取一长度
        /*
         * 比如把一段字符串分成n段,第n段可能不是三个数,有可能是一个或者两个,
         * 现将字符串分成两部分.一部分为前n-1段,第二部分为第n段.前n-1段，每一段加一",".而第n段直接取出即可
         */
        for (int i = 0; i < size - 1; i++) { // 前n-1段
            str2 += str.substring(i * 3, i * 3 + 3) + ".";
        }
        for (int i = size - 1; i < size; i++) { // 第n段
            str2 += str.substring(i * 3, str.length());
        }
        str2 = new StringBuffer(str2).reverse().toString();
        return str2;
    }
}
