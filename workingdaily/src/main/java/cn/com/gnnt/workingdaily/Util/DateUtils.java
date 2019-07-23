package cn.com.gnnt.workingdaily.Util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DateUtils {

    public static final String FORMAT_YYYY = "yyyy";
    public static final String FORMAT_YYYY_MM = "yyyy-MM";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String FORMAT_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";

    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_MM_SS = "mm:ss";
    public static final String FORMAT_HH_MM_SS = "HH:mm:ss";

    public static final String FORMAT_YYYY2MM2DD = "yyyy.MM.dd";
    public static final String FORMAT_YYYY2MM2DD_HH_MM = "yyyy.MM.dd HH:mm";

    public static final String FORMAT_MMCDD = "MM月dd日";
    public static final String FORMAT_MMCDD_HH_MM = "MM月dd日 HH:mm";
    public static final String FORMAT_YYYYCMMCDD = "yyyy年MM月dd日";

    private HashMap<String, DateFormat> formatsMap = new HashMap<>();

    private static DateUtils utils = new DateUtils();

    private DateUtils() {
        formatsMap.put(FORMAT_YYYY , new DateFormat(FORMAT_YYYY));
        formatsMap.put(FORMAT_YYYY_MM , new DateFormat(FORMAT_YYYY_MM));
        formatsMap.put(FORMAT_YYYY_MM_DD , new DateFormat(FORMAT_YYYY_MM_DD));
        formatsMap.put(FORMAT_YYYYMMDD , new DateFormat(FORMAT_YYYYMMDD));
        formatsMap.put(FORMAT_YYYY_MM_DD_HH_MM , new DateFormat(FORMAT_YYYY_MM_DD_HH_MM));
        formatsMap.put(FORMAT_YYYY_MM_DD_HH_MM_SS , new DateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS));
        formatsMap.put(FORMAT_MM_DD_HH_MM , new DateFormat(FORMAT_MM_DD_HH_MM));
        formatsMap.put(FORMAT_MM_DD_HH_MM_SS , new DateFormat(FORMAT_MM_DD_HH_MM_SS));
        formatsMap.put(FORMAT_HH_MM , new DateFormat(FORMAT_HH_MM));
        formatsMap.put(FORMAT_MM_SS , new DateFormat(FORMAT_MM_SS));
        formatsMap.put(FORMAT_HH_MM_SS , new DateFormat(FORMAT_HH_MM_SS));
        formatsMap.put(FORMAT_YYYY2MM2DD , new DateFormat(FORMAT_YYYY2MM2DD));
        formatsMap.put(FORMAT_YYYY2MM2DD_HH_MM , new DateFormat(FORMAT_YYYY2MM2DD_HH_MM));
        formatsMap.put(FORMAT_MMCDD , new DateFormat(FORMAT_MMCDD));
        formatsMap.put(FORMAT_MMCDD_HH_MM , new DateFormat(FORMAT_MMCDD_HH_MM));
        formatsMap.put(FORMAT_YYYYCMMCDD , new DateFormat(FORMAT_YYYYCMMCDD));
    }

    public static String stringFromDate(String format, Date date) {
        return utils.formatsMap.get(format).stringFromDate(date);
    }

    public static Date dateFromDateString(String format, String dateString) {
        return utils.formatsMap.get(format).dateFromDateString(dateString);
    }

    public static long timeIntervalFromDateString(String format, String dateString) {
        return utils.formatsMap.get(format).timeIntervalFromDateString(dateString);
    }

    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    interface DateFormatInterface {

        String stringFromDate(Date date);

        Date dateFromDateString(String dateString);

        long timeIntervalFromDateString(String dateString);
    }

    class DateFormat implements DateFormatInterface {

        SimpleDateFormat dateFormat;

        public DateFormat (String format) {
            dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        }

        @Override
        public String stringFromDate(Date date) {
            return dateFormat.format(date);
        }

        @Override
        public Date dateFromDateString(String dateString) {
            ParsePosition pos = new ParsePosition(0);
            return dateFormat.parse(dateString, pos);
        }

        @Override
        public long timeIntervalFromDateString(String dateString) {
            Date date = dateFromDateString(dateString);
            return date.getTime();
        }
    }
}
