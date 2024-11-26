package com.miguel.springboot.suicidedetection.suicidedetection.controllers;

import com.miguel.springboot.suicidedetection.suicidedetection.common.constants.ApiPathConstants;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE)
public interface TextApi {
    @PostMapping(value = "/text" )
    ResponseEntity<TextResponse> processText(
            @RequestBody TextRequest textRequest
    );
}
