package com.miguel.springboot.suicidedetection.suicidedetection.controllers.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.controllers.ArchiveApi;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ArchiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ArchiveController implements ArchiveApi {
    private final ArchiveService archiveService;

    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }
    @Override
    public ResponseEntity<ArchiveResponse> processArchive(ArchiveRequest archiveRequest, MultipartFile[] files) {
        return ResponseEntity.ok(archiveService.processArchive(files));
    }
}
