package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.ArchiveResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.AnalysisResult;
import com.miguel.springboot.suicidedetection.suicidedetection.common.entities.Register;
import com.miguel.springboot.suicidedetection.suicidedetection.repositories.RegisterRepository;
import com.miguel.springboot.suicidedetection.suicidedetection.repositories.TypeRegisterRepository;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ArchiveService;
import jakarta.servlet.http.HttpServletRequest;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArchiveServiceImpl implements ArchiveService {
    private final DocumentProcessorServiceImpl documentProcessorServiceImpl;
    private final ReportServiceImpl reportServiceImpl;
    private final RegisterRepository registerRepository;
    private final TypeRegisterRepository typeRegisterRepository;
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public ArchiveServiceImpl(DocumentProcessorServiceImpl documentProcessorServiceImpl,
                              ReportServiceImpl reportServiceImpl,
                              RegisterRepository registerRepository,
                              TypeRegisterRepository typeRegisterRepository) {
        this.documentProcessorServiceImpl = documentProcessorServiceImpl;
        this.reportServiceImpl = reportServiceImpl;
        this.registerRepository = registerRepository;
        this.typeRegisterRepository = typeRegisterRepository;
    }

    @Override
    public ArchiveResponse processArchive(MultipartFile[] files, ArchiveRequest archiveRequest) {
        List<Map<String, Object>> documents = new ArrayList<>();
        List<String> respuestas = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                AnalysisResult result = documentProcessorServiceImpl.analyzeDocument(file);
                respuestas.add(result.getText());

                // Agregar el documento a la lista con su nombre y clasificación
                documents.add(Map.of(
                        "name", file.getOriginalFilename(),
                        "isSuicidal", result.isSuicide()
                ));

            } catch (Exception e) {
                e.printStackTrace();
                respuestas.add("Error procesando archivo: " + file.getOriginalFilename());
            }
        }

        // Generar el PDF con la lista de documentos procesados
        byte[] pdfBytes = reportServiceImpl.generateReport(documents);

        // Guardar el registro en la base de datos
        saveRegister(archiveRequest);

        if (respuestas.isEmpty()) {
            respuestas.add("No se pudieron procesar los archivos.");
        }

        return new ArchiveResponse(respuestas, pdfBytes);
    }

    private void saveRegister(ArchiveRequest archiveRequest) {
        Coordinate coordinate = new Coordinate(
                archiveRequest.getLocation().getX(),
                archiveRequest.getLocation().getY()
        );
        Point location = geometryFactory.createPoint(coordinate);

        Register register = new Register();
        register.setLocation(location);
        register.setIpAddress(getClientIp());
        register.setTypeRegister(typeRegisterRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("TypeRegister predeterminado no encontrado")));

        this.registerRepository.save(register);
    }

    private String getClientIp() {
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
