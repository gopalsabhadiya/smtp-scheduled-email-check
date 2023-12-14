package com.example.smtpscheduledemailcheck.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ConfigurationProperties("email.listener")
@Component
public class EmailListenerProperties {
    private String host;
    private String mailStoreType;
    private String username;
    private String password;
    private Integer port;
}
