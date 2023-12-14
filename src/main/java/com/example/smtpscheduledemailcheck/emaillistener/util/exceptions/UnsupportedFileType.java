
package com.example.smtpscheduledemailcheck.emaillistener.util.exceptions;

public class UnsupportedFileType extends RuntimeException {
    public UnsupportedFileType(String provided) {
        super("Unsupported file type: " + provided);
    }
}
