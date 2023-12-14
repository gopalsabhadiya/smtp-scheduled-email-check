package com.example.smtpscheduledemailcheck.emaillistener.dto.file;

import com.example.smtpscheduledemailcheck.emaillistener.util.UniqueFileNameUtil;
import lombok.Getter;

@Getter
public class FileHolder {

    public FileHolder(String fileName, SupportedFile type, byte[] content) {
        this.fileName = fileName;
        this.uniqueFileName = UniqueFileNameUtil.getUniqueFileName(fileName);
        this.size = content.length;
        this.type = type;
        this.content = content;
    }

    private final String fileName;
    private final String uniqueFileName;
    private final Integer size;
    private final SupportedFile type;
    private final byte[] content;
}
