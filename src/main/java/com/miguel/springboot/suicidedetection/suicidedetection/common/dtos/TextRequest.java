package com.miguel.springboot.suicidedetection.suicidedetection.common.dtos;

import lombok.*;
import org.springframework.data.geo.Point;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextRequest {
    private String text;
    private Point location;
}


