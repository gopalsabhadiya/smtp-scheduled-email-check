package com.example.smtpscheduledemailcheck.emaillistener;

import com.example.smtpscheduledemailcheck.emaillistener.dto.EmailContent;

public interface EmailProcessor {
    void process(EmailContent email);
}
