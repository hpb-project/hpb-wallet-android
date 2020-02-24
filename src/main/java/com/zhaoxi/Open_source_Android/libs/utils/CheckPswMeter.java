package com.zhaoxi.Open_source_Android.libs.utils;

/**
 * des:计算密码强度
 * Created by ztt on 2018/6/12.
 */

public class CheckPswMeter {
    /**
     * 计算密码强度
     *
     * @param psw
     * @return 1：弱   2：中   3：强
     */
    public static int checkStrong(String psw){
        if (psw.length()<=4){
            return 1; //密码太短
        }
        int Modes=0;
        for (int i=0;i<psw.length();i++){
            //测试每一个字符的类别并统计一共有多少种模式.
            Modes |= CharMode(psw.charAt(i));
        }
        return bitTotal(Modes);
    }

    private static int CharMode(char iN) {
        if (iN >= 48 && iN <= 57) //数字
            return 1;
        if (iN >= 65 && iN <= 90) //大写字母
            return 2;
        if (iN >= 97 && iN <= 122) //小写
            return 4;
        else
            return 8; //特殊字符
    }

    private static int bitTotal(int num){
        int modes=0;
        for (int i=0;i<4;i++){
            if ((num & 1) == 1) {
                modes++;
            }
            num>>>=1;
        }
        return modes;
    }

}
