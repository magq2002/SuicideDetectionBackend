package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ModelService;
import com.miguel.springboot.suicidedetection.suicidedetection.services.TextService;
import gate.util.GateException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TextServiceImpl implements TextService {
    private final ModelService modelService;

    public TextServiceImpl(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public TextResponse processText(String text) {
        StringBuilder respuesta = null;
        try {
            respuesta = modelService.processWithModel(text);
        } catch (GateException e) {
            throw new RuntimeException(e);
        }
        assert respuesta != null;
        return new TextResponse(respuesta.toString());
    }


}
