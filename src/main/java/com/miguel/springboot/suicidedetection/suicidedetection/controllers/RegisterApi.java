package com.miguel.springboot.suicidedetection.suicidedetection.controllers;

import com.miguel.springboot.suicidedetection.suicidedetection.common.constants.ApiPathConstants;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.RegisterLocationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RequestMapping(ApiPathConstants.V1_ROUTE +  ApiPathConstants.AUTH_ROUTE)
public interface RegisterApi {

    @GetMapping("/locations")
    ResponseEntity<List<RegisterLocationResponse>> getRegisterLocations();
}
