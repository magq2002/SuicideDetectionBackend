package com.miguel.springboot.suicidedetection.suicidedetection.services;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TokenResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.UserRequest;

public interface AuthService {
    TokenResponse loginUser(UserRequest userRequest);
    TokenResponse createUser(UserRequest userRequest);
}
