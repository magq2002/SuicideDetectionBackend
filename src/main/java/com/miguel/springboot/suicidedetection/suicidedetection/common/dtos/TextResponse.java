package com.miguel.springboot.suicidedetection.suicidedetection.common.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextResponse {
    private String message;

    public TextResponse(StringBuilder respuesta) {
    }
}


