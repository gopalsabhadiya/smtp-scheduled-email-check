package com.example.smtpscheduledemailcheck.emaillistener.dto;

import com.example.smtpscheduledemailcheck.emaillistener.dto.file.FileHolder;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EmailContent {
    private List<String> from;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String flags;
    private String folder;
    private Integer messageNumber;
    private Integer size;
    private Date sentDate;
    private Date receivedDate;
    private String subject;
    private String content;
    private String htmlContent;
    private List<FileHolder> attachmentList;
}
