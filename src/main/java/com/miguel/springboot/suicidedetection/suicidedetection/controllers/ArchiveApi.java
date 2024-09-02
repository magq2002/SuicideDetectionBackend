package com.miguel.springboot.suicidedetection.suicidedetection.controllers;

import com.miguel.springboot.suicidedetection.suicidedetection.common.constants.ApiPathConstants;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE)
public interface ArchiveApi {
    @PostMapping(value = "/archive", consumes = "multipart/form-data")
    ResponseEntity<ArchiveResponse> processArchive(
            @RequestPart("archiveRequest") ArchiveRequest archiveRequest,
            @RequestPart("files") MultipartFile[] files
    );
}
