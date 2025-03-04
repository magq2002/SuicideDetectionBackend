package com.miguel.springboot.suicidedetection.suicidedetection.controllers.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.RegisterLocationResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.controllers.RegisterApi;
import com.miguel.springboot.suicidedetection.suicidedetection.services.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class RegisterController implements RegisterApi {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    public ResponseEntity<List<RegisterLocationResponse>> getRegisterLocations() {
        List<RegisterLocationResponse> locations = registerService.getAllRegisterLocations();
        return ResponseEntity.ok(locations);
    }
}
