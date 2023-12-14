package com.example.smtpscheduledemailcheck.scheduled;


import com.example.smtpscheduledemailcheck.emaillistener.SMTPGoodListener;
import com.example.smtpscheduledemailcheck.properties.EmailListenerProperties;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
@EnableScheduling
public class EmailCheckScheduled {
    private final EmailListenerProperties emailListenerProperties;
    private final SMTPGoodListener emailListener;

    @Scheduled(fixedRate = 60000)
    private void checkForNewEmails() throws MessagingException, IOException {
        emailListener.check(
            emailListenerProperties.getHost(),
            emailListenerProperties.getPort(),
            emailListenerProperties.getMailStoreType(),
            emailListenerProperties.getUsername(),
            emailListenerProperties.getPassword()
        );
    }

}
