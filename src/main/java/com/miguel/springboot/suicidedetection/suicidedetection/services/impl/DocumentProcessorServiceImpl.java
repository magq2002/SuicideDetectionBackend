package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.AnalysisResult;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ModelService;
import gate.util.GateException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentProcessorServiceImpl {

    private final ModelService modelService;

    public DocumentProcessorServiceImpl(ModelService modelService) {
        this.modelService = modelService;
    }

    public AnalysisResult analyzeDocument(MultipartFile file) throws IOException, GateException {
        String extractedText = file.getOriginalFilename().endsWith(".pdf")
                ? extractTextFromPdf(file)
                : new String(file.getBytes());

        StringBuilder analysisResult = modelService.processWithModel(extractedText);
        System.out.println(analysisResult.toString());
        boolean isSuicide = "{type=Ideación Suicida}".equalsIgnoreCase(analysisResult.toString().trim());

        return new AnalysisResult(analysisResult.toString(), isSuicide);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}
