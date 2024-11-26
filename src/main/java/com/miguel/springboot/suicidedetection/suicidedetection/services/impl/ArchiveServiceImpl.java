package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ArchiveService;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ModelService;
import gate.util.GateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArchiveServiceImpl implements ArchiveService {
    private final ModelService modelService;

    public ArchiveServiceImpl(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public ArchiveResponse processArchive(MultipartFile[] files) {

        StringBuilder respuesta = null;
        for (MultipartFile file : files) {
            try {
                String extractedText;
                if (file.getOriginalFilename().endsWith(".pdf")) {
                    extractedText = extractTextFromPdf(file);
                } else {
                    extractedText = new String(file.getBytes());
                }
                try {
                    respuesta = modelService.processWithModel(extractedText.toString());
                    System.out.println(respuesta);

                } catch (GateException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(new ArchiveResponse("Error procesando archivos")).getBody();
            }
        }
        assert respuesta != null;
        return new ArchiveResponse(respuesta.toString());
    }

    public String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

}
