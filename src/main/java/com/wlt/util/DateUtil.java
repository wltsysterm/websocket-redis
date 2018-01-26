package com.wlt.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public final static String SDS = "yyyyMMdd";
    public final static long DAY = 24L * 60L * 60L * 1000L;
    /**
     * 两个时间之间相差距离多少天
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static int getDistanceDays(String str1, String str2){
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date one;
        Date two;
        long days=0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int)days;
    }
    /**
     * 获取当前时间，全值yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTimeFull() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
    /**
     * 获取当前时间，全值yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTimeFull(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
    /**
     * 获取当前时间，yyyyMMdd
     *
     * @return
     */
    public static String getCurrentDate8() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    public static boolean compareDate(String startDate, String endDate) throws ParseException {
        if (startDate != null) {
            if (startDate.length() != 0) {
                if (endDate != null) {
                    if (endDate.length() != 0) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                        if (format.parse(startDate).getTime() > format.parse(endDate).getTime()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    /**
     * 获取日期比较结果：
     *      0：相等
     *      1：startDate > endDate
     *      -1: startDate < endDate
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    public static int compareDate2Int(String startDateStr,String endDateStr){
        if ( VerifyUtils.isDate(startDateStr) && VerifyUtils.isDate(endDateStr)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            try {
                Date startDate = format.parse(startDateStr);
                Date endDate = format.parse(endDateStr);
                return startDate.compareTo(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 2;
    }
    /**
     * 格式化日期，yyyy.MM.dd HH:mm:ss
     *
     * @return
     */
    public static String formateDatePoint(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * <p>函数名称：        </p>
     * <p>功能说明：获取指定间隔天数的日期
     * <p>参数说明：</p>
     * @param dt   日期
     * @param days 天数
     * @return
     */
    public static Date getDateDays(Date dt, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.setTimeInMillis(cal.getTimeInMillis() + days * DAY);
        return cal.getTime();
    }

    /**
     * <p>函数名称：        </p>
     * <p>功能说明：获取指定间隔天数的日期
     * <p>参数说明：</p>
     * @param dt     日期
     * @param format 格式
     * @param days   天数
     * @return date + days的日期
     */
    public static String getDateByDays(String dt, String format, int days) {
        Date dateTmp = getDateDays(stringToDate(dt, format), days);
        return formatDateTime(dateTmp, format);
    }
    /**
     * 获取当前日期指定偏移日期
     * @param offsetDays 偏移天数 (可正负)
     * @return yyyyMMdd
     */
    public static String getCurrentDayOffset(int offsetDays){
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day + offsetDays);
        String dayAfter=new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        return dayAfter;
    }

    /**
     * 获取指定日期指定偏移日期
     * @param offsetDays 偏移天数 (可正负)
     * @return yyyyMMdd
     */
    public static String getOneDayOffset(String oneDayDate, int offsetDays){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = simpleDateFormat.parse(oneDayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day + offsetDays);
        String dayAfter = simpleDateFormat.format(c.getTime());
        return dayAfter;
    }

    /**
     * <p>函数名称：        </p>
     * <p>功能说明：格式化日期
     * <p>参数说明：</p>
     * @param dt     日期
     * @param format 格式串
     * @return
     */
    public static String formatDateTime(Date dt, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(dt);
    }
    public static Date stringToDate(String dt) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        try {
            return sdf.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * <p>函数名称：        </p>
     * <p>功能说明：字符串日期转化成日期格式
     * <p>参数说明：</p>
     *
     * @param date   日期字符
     * @param format 日期格式
     * @return
     */
    public static Date stringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        Date resDate;
        try {
            resDate = sdf.parse(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("非有效日期型数据转换失败!");
        }
        return resDate;
    }

    /**
     * <p>函数名称：        </p>
     * <p>功能说明：获取指定间隔天数的日期
     * <p>参数说明：</p>
     * @param dt   日期(格式:yyyyMMdd)
     * @param days 天数
     * @return date + days的日期
     */
    public static String getDateByDays(String dt, int days) {
        return getDateByDays(dt, SDS, days);
    }
}
