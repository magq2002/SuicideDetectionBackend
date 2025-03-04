package com.miguel.springboot.suicidedetection.suicidedetection.services;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.RegisterLocationResponse;
import java.util.List;

public interface RegisterService {
    List<RegisterLocationResponse> getAllRegisterLocations();
}
