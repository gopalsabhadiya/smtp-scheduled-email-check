
package com.example.smtpscheduledemailcheck.emaillistener.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String ONLY_DATE_FORMAT = "MM-dd-yyyy";

    public static String asDateString(Date date) {
        return new DateTime(date).toString("yyyy-MM-dd");
    }

    public static String asDateTimeString(Date date) {
        return new DateTime(date).toString("yyyy-MM-dd HH:mm:ss a");
    }

    public static Long getCurrentTimeMillis() {
        DateTime utc = new DateTime(DateTimeZone.UTC);
        return utc.getMillis();
    }

    public static String onlyDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(ONLY_DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date getDateFrom(Integer year, Integer month, Integer day) {
        return new DateTime(year, month, day, 0, 0, 0).toDate();
    }
}
