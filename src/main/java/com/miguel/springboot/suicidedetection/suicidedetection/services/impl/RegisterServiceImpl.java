package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.RegisterLocationResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.repositories.RegisterRepository;
import com.miguel.springboot.suicidedetection.suicidedetection.services.RegisterService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final RegisterRepository registerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public RegisterServiceImpl(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    @Override
    public List<RegisterLocationResponse> getAllRegisterLocations() {
        List<Object[]> results = entityManager.createNativeQuery(
                "SELECT id, ST_X(location) as longitude, ST_Y(location) as latitude FROM registers"
        ).getResultList();

        return results.stream()
                .map(obj -> new RegisterLocationResponse(
                        ((Number) obj[0]).longValue(),
                        ((Number) obj[2]).doubleValue(),
                        ((Number) obj[1]).doubleValue()
                ))
                .collect(Collectors.toList());
    }
}
