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
}


