package com.mockuai.seckillcenter.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class DateUtils {

    public static final ThreadLocal<Calendar> defaultCalender = new ThreadLocal() {

        protected Calendar initialValue() {

            return Calendar.getInstance();

        }

    };
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_FORMAT2 = "yyyyMMddHHmmss";
    public static final String DATE_ZEROTIME_FORMAT = "yyyy-MM-dd 00:00:00";
    public static final String DATE_ZEROTIME_FORMAT2 = "yyyyMMdd000000";
    public static final String DATE_FULLTIME_FORMAT = "yyyy-MM-dd 23:59:59";
    public static final String DATE_FULLTIME_FORMAT2 = "yyyyMMdd235959";
    public static final String DATETIME12_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String DATETIME12_FORMAT2 = "yyyyMMddhhmmss";
    public static final String DATE_FORMAT_CS = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SHORT = "yyyyMMdd";
    public static final String DATE_FORMAT_UNDER_LINE = "yyyy_MM_dd_hhmmss";
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
    public static final String YEAR_MONTH_FORMAT2 = "yyyyMM";
    public static final String YEAR_MONTH_FIRSTDAY = "yyyy-MM-01";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String MONTH_FORMAT = "MM";
    public static final String DAY_FORMAT = "dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String TIME_FORMAT2 = "HHmmss";
    public static final String DATETIME_SLASH_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_SLASH_FORMAT = "yyyy/MM/dd";
    public static final String DATE_DOT_FORMAT_1 = "yyyy.MM.dd";
    public static final String DATE_DOT_FORMAT_2 = "dd.MM.yyyy";
    private static final Log logger = LogFactory.getLog(DateUtils.class);
    private static final long MS_IN_DAY = 86400000L;
    private static final long MS_IN_HOUR = 3600000L;
    private static final long MS_IN_MINUTE = 60000L;
    private static final long MS_IN_SECOND = 1000L;
    private static final int DAYS_OF_YEAR = 365;

    public static Date getCurrentDate() {

        return new Date();
    }

    public static long currentTimeMillis() {

        return System.currentTimeMillis();
    }

    public static int monthsBetween(int paramInt1, int paramInt2) {

        return paramInt2 / 100 * 12 + paramInt2 % 100 - (paramInt1 / 100 * 12 + paramInt1 % 100);
    }

    public static int monthsBetween(Date paramDate1, Date paramDate2) {

        return monthsBetween(getYM(paramDate1).intValue(), getYM(paramDate2).intValue());
    }

    private static Integer getYM(Date paramDate) {

        if (paramDate == null) {

            return null;
        }

        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(paramDate);
        int i = localCalendar.get(1);
        int j = localCalendar.get(2) + 1;

        return Integer.valueOf(i * 100 + j);
    }

    public static long daysBetween(Date date1, Date date2) {

        return (date2.getTime() - date1.getTime()) / 86400000L;
    }

    public static long daysBetweenForAis(Date date1, Date date2) {

        date1 = startOfDay(date1);
        date2 = startOfDay(date2);

        return daysBetween(date1, date2) + 1L;
    }

    public static Date addDays(Date aDate, long days) {

        long timeInMs = aDate.getTime() + days * 86400000L;

        return new Date(timeInMs);
    }

    public static Date addMonths(Date aDate, int delta) {

        Date newDate = org.apache.commons.lang.time.DateUtils.addMonths(aDate, delta);

        return new Date(newDate.getTime());
    }

    public static Date addYears(Date aDate, int delta) {

        Date newDate = org.apache.commons.lang.time.DateUtils.addYears(aDate, delta);

        return new Date(newDate.getTime());
    }

    public static Date startOfDay(Date aDate) {

        return org.apache.commons.lang.time.DateUtils.truncate(aDate, 5);
    }

    public static Date endOfDay(Date aDate) {

        Date newDate = org.apache.commons.lang.time.DateUtils.truncate(aDate, 5);
        long timeImMs = newDate.getTime() + 86400000L - 1L;

        return new Date(timeImMs);
    }

    public static Date endOfMonth(Date ts) {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(ts.getTime());
        c.add(2, 1);
        c.set(5, 1);
        c.add(5, -1);
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        c.set(14, 0);

        return new Date(c.getTimeInMillis());
    }

    public static Date startOfMonth(Date ts) {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(ts.getTime());
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);

        return new Date(c.getTimeInMillis());
    }

    public static Date startOfYear(Date ts) {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(ts.getTime());
        c.set(2, 0);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);

        return new Date(c.getTimeInMillis());
    }

    public static Date endOfYear(Date ts) {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(ts.getTime());
        c.set(2, 11);
        c.set(5, 31);
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        c.set(14, 0);

        return new Date(c.getTimeInMillis());
    }

    public static Date getLastYearDate() {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(getCurrentDate().getTime());
        c.set(1, c.get(1) - 1);

        return new Date(c.getTimeInMillis());
    }

    public static short stringToShort(String data) {

        short ret = 0;

        try {

            ret = Short.parseShort(data);

        } catch (NumberFormatException ex) {

            if ((null != data) && (!"".equals(data.trim()))) {

                logger.info("DataFormat.stringToShort方法错误提示：该关键字[" + data + "]不是短整数类型!");

            }

        }


        return ret;
    }

    public static int stringToInt(String data) {

        int keyId = 0;

        try {

            keyId = Integer.parseInt(data);

        } catch (NumberFormatException ex) {

            if ((null != data) && (!"".equals(data.trim()))) {

                logger.info("DataFormat.stringToInt方法错误提示：该关键字[" + data + "]不是整数类型!");

            }

        }


        return keyId;
    }

    public static long stringToLong(String data) {

        long keyId = 0L;

        try {

            keyId = Long.parseLong(data);

        } catch (NumberFormatException ex) {

            if ((null != data) && (!"".equals(data.trim()))) {

                logger.info("DataFormat.stringToLong方法错误提示：该关键字[" + data + "]不是长整数类型!");

            }

        }


        return keyId;
    }

    public static double stringToDouble(String data) {

        double ret = 0.0D;

        try {

            ret = Double.parseDouble(data);

        } catch (NumberFormatException ex) {

            String errMsg = "该字段不是double类型!" + ex.getMessage();

            logger.error(errMsg);

        }

        return ret;
    }

    public static float stringToFloat(String data) {

        float ret = 0.0F;

        try {

            ret = Float.parseFloat(data);

        } catch (NumberFormatException ex) {

            String errMsg = "该字段不是float类型!" + ex.getMessage();

            logger.error(errMsg);

        }

        return ret;
    }

    public static Date addDateTime(Date original, int field, int amount) {

        Calendar calOriginal = Calendar.getInstance();

        calOriginal.setTime(original);


        GregorianCalendar calendar = new GregorianCalendar(calOriginal.get(1), calOriginal.get(2), calOriginal.get(5), calOriginal.get(11), calOriginal.get(12), calOriginal.get(13));


        calendar.add(field, amount);

        return new Date(calendar.getTime().getTime());
    }

    public static Date endOfAMonth(Date ts) {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(ts.getTime());

        c.add(2, 1);

        c.set(5, 1);

        c.add(5, -1);

        c.set(11, 23);

        c.set(12, 59);

        c.set(13, 59);

        c.set(14, 0);

        return new Date(c.getTimeInMillis());
    }

    public static Date startOfAmonth(Date ts) {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(ts.getTime());

        c.set(5, 1);

        c.set(11, 0);

        c.set(12, 0);

        c.set(13, 0);

        c.set(14, 0);

        return new Date(c.getTimeInMillis());
    }

    public static boolean isValidDate(String dateStr, String pattern) {

        SimpleDateFormat df = new SimpleDateFormat(pattern);

        try {

            df.setLenient(false);

            df.parse(dateStr);

            return true;
        } catch (ParseException e) {

        }

        return false;
    }

    /**
     * 判断两个时间段是否存在交集
     *
     * @param startA
     * @param endA
     * @param startB
     * @param endB
     * @return
     */
    public static boolean isOverlappingDates(Date startA, Date endA, Date startB, Date endB) {

        if (startA == null || startB == null || endA == null || endB == null) {
            return false;
        }
        return (startA.before(startB) || startA.equals(startB)) && startB.before(endA)
                || (startB.before(startA) || startB.equals(startA)) && startA.before(endB);
    }

    public static Date getNextDay(int periodType, int periodUnit, int offerDay, Date validDate, Date expireDate) {

        Date targetDate = null;

        Date today = getCurrentDate();

        if ((validDate != null) && (today.before(validDate))) {

            today = validDate;

        }

        offerDay--;

        if (periodType == 1) {

            if (offerDay + 1 >= 365) {

                offerDay = 364;

            }

            targetDate = org.apache.commons.lang.time.DateUtils.addDays(org.apache.commons.lang.time.DateUtils.truncate(today, 1), offerDay);


            if (targetDate.before(today)) {

                targetDate = org.apache.commons.lang.time.DateUtils.addDays(org.apache.commons.lang.time.DateUtils.truncate(org.apache.commons.lang.time.DateUtils.addYears(today, periodUnit), 1), offerDay);

            }


        } else if (periodType == 2) {

            int dayOfMonth = 28;

            if (dayOfMonth - 1 <= offerDay) {

                offerDay = dayOfMonth - 1;

            }

            targetDate = org.apache.commons.lang.time.DateUtils.addDays(org.apache.commons.lang.time.DateUtils.truncate(today, 2), offerDay);


            if (targetDate.before(today)) {

                targetDate = org.apache.commons.lang.time.DateUtils.addDays(org.apache.commons.lang.time.DateUtils.truncate(org.apache.commons.lang.time.DateUtils.addMonths(today, periodUnit), 2), offerDay);

            }


        } else if (periodType == 3) {

            Calendar calendar = Calendar.getInstance();

            if (offerDay >= 7) {

                offerDay = 6;

            }

            calendar.setFirstDayOfWeek(1);

            calendar.setTime(today);

            calendar.add(5, 1 - calendar.get(7));

            targetDate = org.apache.commons.lang.time.DateUtils.addDays(org.apache.commons.lang.time.DateUtils.truncate(calendar.getTime(), 5), offerDay);


            if (targetDate.before(today)) {

                targetDate = org.apache.commons.lang.time.DateUtils.addDays(org.apache.commons.lang.time.DateUtils.addWeeks(org.apache.commons.lang.time.DateUtils.truncate(calendar.getTime(), 5), periodUnit), offerDay);

            }


        } else if (periodType == 4) {

            Calendar calendar = Calendar.getInstance();

            targetDate = org.apache.commons.lang.time.DateUtils.addDays(org.apache.commons.lang.time.DateUtils.truncate(calendar.getTime(), 5), ++offerDay);

        }


        return (expireDate != null) && (targetDate.after(expireDate)) ? null : targetDate;
    }

    public static Date secToDate(long second) {

        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(second * 1000L);

        return c.getTime();
    }

    public static String msToDuration(long mss) {

        StringBuffer str = new StringBuffer();

        long days = mss / 86400000L;

        if (days != 0L) {

            str.append(days);

        }

        long hours = mss % 86400000L / 3600000L;

        if (str.length() == 0) {

            if (hours != 0L)
                str.append(hours);

        } else {

            str.append(" days ").append(hours);

        }

        long minutes = mss % 3600000L / 60000L;

        if (str.length() == 0) {

            if (minutes != 0L)
                str.append(minutes);

        } else {

            str.append(" hours ").append(minutes);

        }

        long seconds = mss % 60000L / 1000L;

        if (str.length() == 0) {

            if (seconds != 0L)
                str.append(seconds);

        } else {

            str.append(" minutes ").append(seconds);

        }

        long millseconds = mss % 1000L;

        if (str.length() == 0)
            str.append(millseconds);

        else {

            str.append(" seconds ").append(millseconds);

        }

        str.append(" millseconds");

        return str.toString();
    }

    public static long getDaySub(Date beginDate, Date endDate) {

        long day = 0L;

        day = (endDate.getTime() - beginDate.getTime()) / 86400000L;

        return day;
    }

    public static String getFormatMonth(int i, String formatMonth) {

        Calendar c = Calendar.getInstance();

        c.add(2, i);

        SimpleDateFormat format = new SimpleDateFormat(formatMonth);

        return format.format(c.getTime());
    }

    public static String cvtUTCDate(Date date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(date);
    }

    public static Date addSecond(Date date, int second) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(13, second);

        return calendar.getTime();
    }
}