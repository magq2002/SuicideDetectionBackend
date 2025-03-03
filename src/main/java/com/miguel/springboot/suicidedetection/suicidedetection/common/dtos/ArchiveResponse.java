package com.miguel.springboot.suicidedetection.suicidedetection.common.dtos;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveResponse {
    private List<String> messages;
    private byte[] pdfBytes;

    public ArchiveResponse(List<String> messages) {
        this.messages = messages;
        this.pdfBytes = null;
    }
}
