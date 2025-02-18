package com.miguel.springboot.suicidedetection.suicidedetection.services;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TokenResponse;
import io.jsonwebtoken.Claims;

public interface JwtService {
    TokenResponse generateToken(Long userId);
    Claims getClaims(String token);
    boolean isExpired(String token);
    Long extractUserId(String token);


}
