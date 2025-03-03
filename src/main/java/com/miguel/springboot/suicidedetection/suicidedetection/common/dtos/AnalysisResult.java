package com.miguel.springboot.suicidedetection.suicidedetection.common.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {
    private String text;
    private boolean isSuicide;
}
