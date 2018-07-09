package com.lottery.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Exchanger;

/**
 * Created by lxzh on 2018/6/20.
 * Description:
 */
public class Common {
    public final static boolean DEBUG=true;

    /**
     * 获取日期格式化字符串
     * @param date 日期
     * @return yyyy-M-d格式日期字符串
     */
    public static String getFormatDateString(Date date){
        String dateStr = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
            Calendar begin=Calendar.getInstance();
            begin.setTime(date);
            dateStr = df.format(begin.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }
    /**
     * 获取增加days天后的日期字符串
     * @param timeNow 日期
     * @param days 增加的天数
     * @return yyyy-M-d格式日期字符串
     */
    public static String addDaysToDate(Date date,int days){
        String dateStr = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
            Calendar begin=Calendar.getInstance();
            begin.setTime(date);
            begin.add(Calendar.DAY_OF_MONTH,days);
            dateStr = df.format(begin.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 根据日期时间字符串获取Date实例
     * @param dateStr date time String, yyyy-MM-dd hh:mm:ss format
     * @return Date
     */
    public static Date getDateFromString(String dateStr){
        Date date;
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            date = df.parse(dateStr);
        }catch (Exception ex){
            date=new Date();
            ex.printStackTrace();
        }
        return date;
    }

    public static int DateCompare(Date date1,Date date2){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String dStr1=df.format(date1);
        String dStr2=df.format(date2);
        return dStr1.compareTo(dStr2);
    }

    public static int getDeltaDays(Date date1,Date date2){
        Calendar c1=Calendar.getInstance();c1.setTime(date1);
        Calendar c2=Calendar.getInstance();c2.setTime(date2);
        LocalDate ld1=LocalDate.of(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.DAY_OF_MONTH));
        LocalDate ld2=LocalDate.of(c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),c2.get(Calendar.DAY_OF_MONTH));
        return (int)(ld1.toEpochDay()-ld2.toEpochDay());
    }
}
