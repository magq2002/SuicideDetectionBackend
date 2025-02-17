package com.miguel.springboot.suicidedetection.suicidedetection.controllers.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.controllers.TextApi;
import com.miguel.springboot.suicidedetection.suicidedetection.services.TextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextController implements TextApi {
    private final
    TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @Override
    public ResponseEntity<TextResponse> processText(TextRequest textRequest ) {
        return ResponseEntity.ok(textService.processText(textRequest));
    }
}
