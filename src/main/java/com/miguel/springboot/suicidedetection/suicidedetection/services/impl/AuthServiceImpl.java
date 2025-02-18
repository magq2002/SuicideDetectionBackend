package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TokenResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.UserRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.entities.UserModel;
import com.miguel.springboot.suicidedetection.suicidedetection.repositories.UserRepository;
import com.miguel.springboot.suicidedetection.suicidedetection.services.AuthService;
import com.miguel.springboot.suicidedetection.suicidedetection.services.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenResponse loginUser(UserRequest userRequest) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userRequest.getEmail());
        if (passwordEncoder.matches(userRequest.getPassword(), userDetails.getPassword())) {
            Long userId = ((UserModel) userDetails).getId();
            TokenResponse token = jwtService.generateToken(userId);
            return new TokenResponse(token.getAccessToken());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public TokenResponse createUser(UserRequest userRequest) {
        return Optional.of(userRequest)
                .map(this::mapToEntity)
                .map(userRepository::save)
                .map(userCreated -> jwtService.generateToken(userCreated.getId()))
                .orElseThrow(() -> new RuntimeException("Error creating user"));
    }

    private UserModel mapToEntity(UserRequest userRequest) {
        return UserModel.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role("USER")
                .build();
    }
}
