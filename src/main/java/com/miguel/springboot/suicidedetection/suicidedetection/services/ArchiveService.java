package com.miguel.springboot.suicidedetection.suicidedetection.services;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.common.entities.Register;
import org.springframework.web.multipart.MultipartFile;

public interface ArchiveService {
    ArchiveResponse processArchive(MultipartFile[] files, ArchiveRequest archiveRequest);

}
