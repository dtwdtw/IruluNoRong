package com.wf.irulu.common.utils;

import android.annotation.SuppressLint;

import com.wf.irulu.IruluApplication;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @描述: 时间格式工具类
 * @项目名: irulu
 * @包名:com.wf.irulu.common.utils
 * @类名:DateFormatUtil
 * @作者: 左西杰
 * @创建时间:2015-6-4 下午2:01:55
 */
@SuppressLint("SimpleDateFormat")
public class DateFormatUtils {

    // 一天
    private final static long ONE_DAYS = 86400 * 1000;
    public static final String ENG_DATE_FROMAT = "EEE, d MMM yyyy HH:mm:ss z";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY = "yyyy";
    public static final String MM_DD = "MM-dd";
    public static final String HH_MM = "HH:mm";
    private static final String EEE_HH_MM = "EEE HH:mm";
    public static final String MM = "MM";
    public static final String DD = "dd";

    public static long nd = 1000 * 24 * 60 * 60;    // 一天的毫秒数
    public static long nh = 1000 * 60 * 60;            // 一小时的毫秒数
    public static long nm = 1000 * 60;                // 一分钟的毫秒数

    public static DateFormat yyyyMMddHHmmssDateFormat;
    private static DateFormat hhmmFormate;

    public static SimpleDateFormat formatYMDHM = new SimpleDateFormat(YYYY_MM_DD_HH_MM);

    public static SimpleDateFormat ACTIVE_FORMATYMDHM = new SimpleDateFormat("MM-dd HH:mm");

    /**
     * yyyyMMddHHmmss 格式的日期解析器
     */
    public static DateFormat getyyyyMMddHHmmssDateFormat() {
        if (null == yyyyMMddHHmmssDateFormat) {
            //TODO
//			String language = YueXiaApp.getInstance().getResources().getConfiguration().locale.getLanguage();
//			yyyyMMddHHmmssDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", new Locale(language));
            yyyyMMddHHmmssDateFormat.setTimeZone(TimeZoneUtils.getLocalTimeZone());
        }
        return yyyyMMddHHmmssDateFormat;
    }

    /**
     * 得到UTC时间
     *
     * @return
     */
    public static String getUtcTime() {
        return getDateTime(TimeZoneUtils.GMT0_TIME_ZONE);
    }

    public static long getUtcMillis() {
        return System.currentTimeMillis();
    }

    public static String getYMD() {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        Date date = new Date(getUtcMillis());
        String t = format.format(date);
        return t;
    }

    /**
     * 根据utc的毫秒级获取本地日期
     *
     * @param utcMillis
     * @return
     */
    public static Date getLocalDateByUtc(long utcMillis) {
        return new Date(utcMillis);
    }

    /**
     * 根据本地的毫秒级获取UTC日期
     *
     * @param utcMillis
     * @return
     */
    public static long getUtcDateByLocal(long utcMillis) {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.setTimeInMillis(utcMillis);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        return cal.getTime().getTime();
    }

    /**
     * @param @return
     * @return String
     * @Title: getDateTime
     * @Description: 根据当前时间yyyyMMddHHmmss格式的字符串
     */
    public static String getDateTime(TimeZone timeZone) {
        DateFormat dateFormat = getyyyyMMddHHmmssDateFormat();
        dateFormat.setTimeZone(timeZone);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return dateFormat.format(date);
    }

    /**
     * 格式化时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getActiveDate(long startTime, long endTime) {

        String str = "";

        Date start = getLocalDateByUtc(startTime);
        Date end = getLocalDateByUtc(endTime);

        str = formatYMDHM.format(start) + "~" + formatYMDHM.format(end);

        return str;
    }


    private synchronized static DateFormat getHhMmDateFormate() {
        if (hhmmFormate == null) {
            hhmmFormate = new SimpleDateFormat(HH_MM);
        }
        return hhmmFormate;
    }

    /**
     * 时间转换
     */
    public static String DateFormatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        Date date = new Date(time);
        String t = format.format(date);
        return t;
    }

    /**
     * 时间转换
     */
    public static String DateFormatMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        Date date = new Date(time);
        String t = format.format(date);
        return t;
    }

    /**
     * 时间转换
     */
    public static String DateFormat(long time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = new Date(time);
        String t = format.format(date);
        return t;
    }

    /**
     * 时间转换
     */
    public static String DateFormatYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
        Date date = new Date(time);
        String t = format.format(date);
        return t;
    }

    /**
     * 时间转换
     */
    public static String DateFormatYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
        String t = format.format(date);
        return t;
    }

    /**
     * 时间转换
     */
    public static String DateFormatActivityTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(MM_DD_HH_MM);
        String t = format.format(date);
        return t;
    }

    /**
     * 时间转换
     */
    public static String DateFormatUrlTime(Date date) {
//		SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm:ss",Locale.US);
        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        String t = format.format(date);
        return t;
    }

    /**
     * 时间转换
     */
    public static String DateFormatUrlTime(long milles) {
        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy", IruluApplication.getInstance().getResources().getConfiguration().locale);
//		SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm",Locale.US);
        String t = format.format((long) (milles * 1000));
        return t;
    }


    /**
     * 时间转毫秒级
     *
     * @return
     */
    public static long getTimestamp(String time) {
        Date date1 = null;
        Date date2 = null;
        long l = 0;
        try {
            date1 = new SimpleDateFormat(YYYY_MM_DD_HH_MM).parse(time);
            date2 = new SimpleDateFormat(YYYY_MM_DD_HH_MM).parse("1970-01-01 08:00");
            l = date1.getTime() - date2.getTime() > 0 ? date1.getTime() - date2.getTime() : date2.getTime() - date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    public static String getFormatTimeStart(long startTime) {
        String str = "";
        SimpleDateFormat formatMDHM = new SimpleDateFormat("MM月dd日 ");
        SimpleDateFormat formatHH = new SimpleDateFormat(HH_MM);
        Date start = getLocalDateByUtc(startTime);
        String week = getWeekOfDate(startTime);
        str = formatMDHM.format(start) + "," + week + "\r\n" + formatHH.format(start);

        return str;
    }

    //当前时间是星期几
    public static String getWeekOfDate(long time) {
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        Date curDate = new Date(time);
        cal.setTime(curDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * <br>
     * 根据出生日期计算年龄 </br>
     *
     * @param dateStr yyyy-MM-dd格式
     * @return
     */
    public static int getAgeFromDate(String dateStr) {
        int deltaAge = 22;
        if (null == dateStr || "".equals(dateStr) || "0000-00-00".equals(dateStr)) {
            return deltaAge;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD, Locale.CHINA);
        try {
            long deltaTime = sdf.parse(sdf.format(new Date())).getTime()
                    - sdf.parse(dateStr).getTime();
            long date = deltaTime / (1 * 24 * 60 * 60 * 1000) - 1;
            String ageStr = new DecimalFormat("#.00").format(date / 365.25f);
            String age[] = ageStr.split("\\.");
            int tempLength = age[0].replace("-", "0").length();
            if (tempLength < 1) {

            } else {
                deltaAge = Integer.parseInt(age[0].replace("-", "0"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deltaAge;
    }

    public static String getActiveDate(long startTime) {
        String str = "";

        Date start = getLocalDateByUtc(startTime);

        str = ACTIVE_FORMATYMDHM.format(start);

        return str;
    }

    public static String getEngMonth(int i) {
        switch (i) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Dec";
        }
    }

    /**
     * 通过时间撮获取所需格式的时间字符串
     * @param l
     * @return
     */
    public static String getMMddyyyyHHmm(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd,yyyy HH:mm");
        Date d = new Date(l);
        return sdf.format(d);
    }
}
