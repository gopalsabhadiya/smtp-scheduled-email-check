package com.example.smtpscheduledemailcheck.emaillistener.dto.file;

import com.example.smtpscheduledemailcheck.emaillistener.util.exceptions.UnsupportedFileType;

import java.util.Optional;
import java.util.stream.Stream;

public enum SupportedFile {
    PDF_INVOICE("application/pdf");

    private final String contentType;

    SupportedFile(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public static SupportedFile from(String value) {
        Optional<SupportedFile> supportedFileOptional =
            Stream.of(SupportedFile.values())
                .filter(supportedFile -> supportedFile.getContentType().equalsIgnoreCase(value))
                .findFirst();

        if(supportedFileOptional.isPresent()) {
            return supportedFileOptional.get();
        }

        throw new UnsupportedFileType(value);
    }
}
