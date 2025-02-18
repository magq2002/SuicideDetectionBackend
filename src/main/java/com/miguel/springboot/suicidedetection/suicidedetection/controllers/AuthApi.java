package com.miguel.springboot.suicidedetection.suicidedetection.controllers;

import com.miguel.springboot.suicidedetection.suicidedetection.common.constants.ApiPathConstants;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TokenResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE)
public interface AuthApi {

    @PostMapping(value = "/login")
    ResponseEntity<TokenResponse> loginUser(@RequestBody @Valid UserRequest userRequest);

    @PostMapping(value = "/register")
    ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserRequest userRequest);

    @GetMapping
    ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") String userId);
}
