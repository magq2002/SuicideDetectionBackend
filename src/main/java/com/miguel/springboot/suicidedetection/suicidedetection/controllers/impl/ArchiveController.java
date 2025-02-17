package com.miguel.springboot.suicidedetection.suicidedetection.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.controllers.ArchiveApi;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ArchiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
@RestController
public class ArchiveController implements ArchiveApi {
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;

    public ArchiveController(ArchiveService archiveService, ObjectMapper objectMapper) {
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
    }
    @Override
    public ResponseEntity<ArchiveResponse> processArchive(MultipartFile[] files, String archiveRequestJson) {
        try {
            ArchiveRequest archiveRequest = objectMapper.readValue(archiveRequestJson, ArchiveRequest.class);

            ArchiveResponse response = archiveService.processArchive(files, archiveRequest);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArchiveResponse(Collections.singletonList("Error procesando la solicitud.")));
        }
    }
}
