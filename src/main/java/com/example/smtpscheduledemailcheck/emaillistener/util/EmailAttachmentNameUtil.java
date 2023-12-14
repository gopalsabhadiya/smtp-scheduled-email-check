package com.example.smtpscheduledemailcheck.emaillistener.util;

public class EmailAttachmentNameUtil {
    public static String getFileNameFromContentType(String contentType) {
        return contentType.split(";")[1].split("=")[1];
    }
}
