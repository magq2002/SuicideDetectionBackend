package com.miguel.springboot.suicidedetection.suicidedetection.controllers.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TokenResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.UserRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.controllers.AuthApi;
import com.miguel.springboot.suicidedetection.suicidedetection.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<TokenResponse> loginUser(UserRequest userRequest) {
        return ResponseEntity.ok(authService.loginUser(userRequest));
    }

    @Override
    public ResponseEntity<TokenResponse> createUser(UserRequest userRequest) {
        return ResponseEntity.ok(authService.createUser(userRequest));
    }

    @Override
    public ResponseEntity<String> getUser(String userId) {
        return ResponseEntity.ok(userId);
    }
}
