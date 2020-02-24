package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.Context;

import com.zhaoxi.Open_source_Android.dapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * des:
 * Created by ztt on 2018/6/15.
 */

public class DateUtilSL {

    /**
     * 生成时间戳
     *
     * @return
     */
    public static String getCurTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getCurTimestamp2() {
        return String.valueOf(System.currentTimeMillis()/1000);
    }

    /**
     * 根据long转换date
     *
     * @param timestamp
     * @param type      0.需要在后面+000
     * @return
     */
    public static Date getDateByLong(long timestamp, int type) {
        Date date;
        if (type == 0) {
            date = new Date(Long.valueOf(timestamp + "000"));
        } else {
            date = new Date(Long.valueOf(timestamp));
        }
        return date;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrymd(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String dateToStrymdhm(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 dd/MM/yyyy HH:mm:ss
     * 06/11/2018 01:23:51
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrymdhms(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String dateToStrymdhm2(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String dateToStryhm(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 dd/MM/yyyy HH:mm:ss
     * 06/11/2018 01:23:51
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrymdhms2(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String dateToStrymdhms3(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 得到现在时间
     *
     * @param type 1.yyyy-MM 2.yyyy-MM-dd 3.yyyyMMdd 4.yyyy 5.HHmm 6.201909101549
     * @return
     */
    public static String getCurrentDate(int type) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = null;
        switch (type) {
            case 1:
                formatter = new SimpleDateFormat("yyyy-MM");
                break;
            case 2:
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 3:
                formatter = new SimpleDateFormat("yyyyMMdd");
                break;
            case 4:
                formatter = new SimpleDateFormat("yyyy");
                break;
            case 5:
                formatter = new SimpleDateFormat("HHmm");
                break;
            case 6:
                formatter = new SimpleDateFormat("yyyyMMddHHmm");
                break;
        }
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 转Date MM-dd 如果等于今天显示 “今天 HH:mm” 小于1天的显示“昨天 HH:mm” 其他显示“”
     *
     * @param timestamp
     * @param type      0.不判断5分钟  2.判断5分钟
     * @return
     */
    public static String getTransferDate(Context context, long timestamp, int type) {
        String resultDate = "";
        Date date = getDateByLong(timestamp, type);
        SimpleDateFormat formatterYmd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatteryy = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatterHm = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat HmFormatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatAll = new SimpleDateFormat("HHmm");
        String dateString = formatter.format(date);
        String curDate = getCurrentDate(3);
        if (curDate.equals(dateString)) {
            if (type == 2) {//来源于新闻
                int oldTime = Integer.valueOf(formatAll.format(date));
                int curTime = Integer.valueOf(getCurrentDate(5));
                if (curTime - oldTime <= 5) {//判断与当前时间是否相差5分钟
                    resultDate = context.getResources().getString(R.string.item_tansfer_recode_txt_06);
                } else {
                    resultDate = context.getResources().getString(R.string.item_tansfer_recode_txt_02) + HmFormatter.format(date);
                }
            } else {
                resultDate = context.getResources().getString(R.string.item_tansfer_recode_txt_02) + HmFormatter.format(date);
            }

        } else if ((Integer.parseInt(curDate) - Integer.parseInt(dateString)) == 1) {
            resultDate = context.getResources().getString(R.string.item_tansfer_recode_txt_03) + HmFormatter.format(date);
        } else {
            if (getCurrentDate(4).equals(formatteryy.format(date)))
                resultDate = formatterHm.format(date);
            else resultDate = formatterYmd.format(date);

        }
        return resultDate;
    }

    /**
     * 转Date MM-dd 如果等于今天显示 “今天 HH:mm” 小于1天的显示“昨天 HH:mm” 其他显示“”
     *
     * @param timestamp
     * @param type      0.不判断5分钟  2.判断5分钟
     * @return
     */
    public static String getNewsDate(Context context, long timestamp, int type) {
        String resultDate = "";
        Date date = getDateByLong(timestamp, type);
        SimpleDateFormat formatterYmd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatteryy = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatterHm = new SimpleDateFormat("MM-dd");
        SimpleDateFormat HmFormatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatAll = new SimpleDateFormat("HHmm");
        String dateString = formatter.format(date);
        String curDate = getCurrentDate(3);
        if (curDate.equals(dateString)) {
            int oldTime = Integer.valueOf(formatAll.format(date));
            int curTime = Integer.valueOf(getCurrentDate(5));
            if (curTime - oldTime <= 5) {//判断与当前时间是否相差5分钟
                resultDate = context.getResources().getString(R.string.item_tansfer_recode_txt_06);
            } else {
                resultDate = HmFormatter.format(date);
            }
        } else if ((Integer.parseInt(curDate) - Integer.parseInt(dateString)) == 1) {
            resultDate = context.getResources().getString(R.string.item_tansfer_recode_txt_03);
        } else {
            if (getCurrentDate(4).equals(formatteryy.format(date)))
                resultDate = formatterHm.format(date);
            else resultDate = formatterYmd.format(date);
        }
        return resultDate;
    }

    public static String getKuaixunTime(Context context, long timestamp) {
        String result = "";
        Date date = getDateByLong(timestamp, 2);
        SimpleDateFormat formatterHm = new SimpleDateFormat("MM/dd");
        String hm = formatterHm.format(date);
        result = hm + " " + getWeek(context, DateUtilSL.dateToStrymd(date));
        return result;
    }

    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     *
     * @return
     */
    public static String getWeek(Context context, String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int wek = c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += context.getResources().getString(R.string.time_year_month_day_01);
            ;
        }
        if (wek == 2) {
            Week += context.getResources().getString(R.string.time_year_month_day_02);
        }
        if (wek == 3) {
            Week += context.getResources().getString(R.string.time_year_month_day_03);
        }
        if (wek == 4) {
            Week += context.getResources().getString(R.string.time_year_month_day_04);
        }
        if (wek == 5) {
            Week += context.getResources().getString(R.string.time_year_month_day_05);
        }
        if (wek == 6) {
            Week += context.getResources().getString(R.string.time_year_month_day_06);
        }
        if (wek == 7) {
            Week += context.getResources().getString(R.string.time_year_month_day_07);
        }
        return Week;
    }


    /**
     * 是否显示广告
     *
     * @param context
     * @return
     */
    public static boolean isShowAdNow(Context context) {
        String lastShowTime = SharedPreferencesUtil.getSharePreString(context, SharedPreferencesUtil.AD_SHOW_TIME);
        if (StrUtil.isEmpty(lastShowTime)) {
            return true;
        }
        //获取当前时间戳
        String nowTime = getCurTimestamp();
        long between_hs = (Long.valueOf(nowTime) - Long.valueOf(lastShowTime)) / (1000 * 3600);
        //判断相差时间是否超过24小时
        if (between_hs >= 24) {
            return true;
        }
        return false;
    }

    /**
     * 格式化倒计时，倒计时格式为：30/17/23
     *
     * @param millisStr 毫秒字符串形式
     * @return
     */
    public static String formatCountDown(Context context, long millisStr) {
        long minutes = millisStr / 1000 / 60;
        int minutesOfDay = 24 * 60;
        int minutesOfHour = 60;

        // 天
        int day = (int) minutes / minutesOfDay;
        // 剩余分钟数
        int minutesOfOver = (int) minutes % minutesOfDay;
        // 小时
        int hour = minutesOfOver / minutesOfHour;
        // 分钟
        int minute = minutesOfOver % minutesOfHour;

        return "<font color='#C0C0C0'>" + context.getResources().getString(R.string.activity_vote_gl_txt_06_01)
                + "</font><font color='#FFFFFF'>" + day + "</font><font color='#C0C0C0' >" + context.getResources().getString(R.string.activity_vote_gl_txt_06_03)
                + "</font><font color='#FFFFFF'>" + hour + "</font><font color='#C0C0C0'>" + context.getResources().getString(R.string.activity_vote_gl_txt_06_04)
                + "</font><font color='#FFFFFF'>" + minute + "</font><font color='#C0C0C0' >" + context.getResources().getString(R.string.activity_vote_gl_txt_06_05)
                + "</font>";
    }

    /**
     * 倒计时
     *
     * @param rasieEndDate 结束时间戳
     * @param nowDate      现在时间戳
     * @return long[]
     */
    public static long[] ComputeCountdown(long rasieEndDate, long nowDate) {
        Long ss = (rasieEndDate - nowDate) % 60;
        Long min = (rasieEndDate - nowDate) / (60) % 60;
        Long hous = (rasieEndDate - nowDate) / (60 * 60) % 24;
        Long day = (rasieEndDate - nowDate) / (60 * 60 * 24);
        return new long[]{day, hous, min, ss};
    }

    public static long[] ComputeCountdown(long rasieEndDate) {
        long nowDate = System.currentTimeMillis() / 1000;
        if (rasieEndDate - nowDate >= 0) {
            Long ss = (rasieEndDate - nowDate) % 60;
            Long min = (rasieEndDate - nowDate) / (60) % 60;
            Long hous = (rasieEndDate - nowDate) / (60 * 60) % 24;
            Long day = (rasieEndDate - nowDate) / (60 * 60 * 24);
            return new long[]{day, hous, min, ss};
        } else {
            return new long[]{0, 0, 0, 0};
        }
    }

    /**
     * 倒计时：0天0小时0分
     *
     * @param lastTimeSecond 结束时间：秒
     * @return 天时分数组times times[0]：天 times[1]：时 times[2]：分
     */
    public static long[] calculateCountDown(long lastTimeSecond) {
        long[] times = new long[3];
        long currentTimeSecond = System.currentTimeMillis() / 1_000;
        if (lastTimeSecond > currentTimeSecond) {
            // 剩余毫秒值
            long residueTimeSecond = lastTimeSecond - currentTimeSecond;
            // 每分钟所占毫秒值
            long eachMinuteOfSecond = 60;
            // 每小时所占毫秒值
            long eachHourOfSecond = 60 * eachMinuteOfSecond;
            // 每天所占毫秒值
            long eachDayOfSecond = 24 * eachHourOfSecond;

            // 天
            long day = residueTimeSecond / eachDayOfSecond;
            // 不足一天，则用剩余时间计算小时
            long hour = residueTimeSecond % eachDayOfSecond / eachHourOfSecond;
            // 不足一小时，则用剩余时间计算分钟
            long minute = residueTimeSecond % eachHourOfSecond / eachMinuteOfSecond;

            times[0] = day;
            times[1] = hour;
            times[2] = minute;
        }

        return times;
    }


    /**
     * @param percent
     * @return
     */
    public static Integer formatPercent(String percent) {
        Float s = Float.valueOf(percent);
        int a = (int) (s * 100);
        return a;
    }

    public static String formatSPercent(String percent) {
        Float s = Float.valueOf(percent);
        int a = (int) (s * 100);
        return a + "%";
    }

    public static String formatAPercent(String percent) {
        return percent + "%";
    }


    public static void main(String[] args) {
        long[] times = calculateCountDown(1567580522);
        System.out.println("时间:" + times[0] + "天" + times[1] + "时" + times[2] + "分");
    }

}
