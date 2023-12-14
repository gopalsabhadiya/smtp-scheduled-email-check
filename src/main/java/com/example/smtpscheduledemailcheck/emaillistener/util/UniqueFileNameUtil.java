package com.example.smtpscheduledemailcheck.emaillistener.util;

public class UniqueFileNameUtil {
    public static String getUniqueFileName(String originalFileName) {
        return DateUtil.getCurrentTimeMillis() + StrConstants.DASH + originalFileName;
    }
}
