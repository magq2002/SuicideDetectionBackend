package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.common.entities.Register;
import com.miguel.springboot.suicidedetection.suicidedetection.repositories.RegisterRepository;
import com.miguel.springboot.suicidedetection.suicidedetection.repositories.TypeRegisterRepository;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ArchiveService;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ModelService;
import gate.util.GateException;
import jakarta.servlet.http.HttpServletRequest;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


import java.io.IOException;

@Service
public class ArchiveServiceImpl implements ArchiveService {
    private final ModelService modelService;
    private final RegisterRepository registerRepository;
    private final TypeRegisterRepository typeRegisterRepository;
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @Autowired
    private HttpServletRequest request;

    public ArchiveServiceImpl(ModelService modelService, RegisterRepository registerRepository, TypeRegisterRepository typeRegisterRepository) {
        this.modelService = modelService;
        this.registerRepository = registerRepository;
        this.typeRegisterRepository = typeRegisterRepository;
    }

    @Override
    public ArchiveResponse processArchive(MultipartFile[] files, ArchiveRequest archiveRequest) {

        StringBuilder respuesta = null;
        List<String> respuestas = new ArrayList<>();

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
                    respuestas.add(respuesta.toString());

                } catch (GateException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
                respuestas.add("Error procesando archivo: " + file.getOriginalFilename());
            }
        }

        Coordinate coordinate = new Coordinate(
                archiveRequest.getLocation().getX(),
                archiveRequest.getLocation().getY()
        );
        Point location = geometryFactory.createPoint(coordinate);

        Register register = new Register();
        register.setLocation(location);
        String clientIp = getClientIp();
        register.setIpAddress(clientIp);
        register.setTypeRegister(typeRegisterRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("TypeRegister predeterminado no encontrado")));


        this.registerRepository.save(register);

        return new ArchiveResponse(respuestas);
    }

    public String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    public String getClientIp() {
        String[] headerNames = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];
            }
        }

        return request.getRemoteAddr();
    }

}
