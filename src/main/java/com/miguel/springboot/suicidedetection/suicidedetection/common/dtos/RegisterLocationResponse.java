package com.miguel.springboot.suicidedetection.suicidedetection.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterLocationResponse {
    private Long id;
    private double latitude;
    private double longitude;
}
