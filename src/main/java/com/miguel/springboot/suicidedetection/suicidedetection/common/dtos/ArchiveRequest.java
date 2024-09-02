package com.miguel.springboot.suicidedetection.suicidedetection.common.dtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveRequest {
    private String location;
}
