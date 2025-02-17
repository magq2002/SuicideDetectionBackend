package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextResponse;
import com.miguel.springboot.suicidedetection.suicidedetection.common.entities.Register;
import com.miguel.springboot.suicidedetection.suicidedetection.repositories.RegisterRepository;
import com.miguel.springboot.suicidedetection.suicidedetection.repositories.TypeRegisterRepository;
import com.miguel.springboot.suicidedetection.suicidedetection.services.ModelService;
import com.miguel.springboot.suicidedetection.suicidedetection.services.TextService;
import gate.util.GateException;
import jakarta.servlet.http.HttpServletRequest;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TextServiceImpl implements TextService {
    private final ModelService modelService;
    private final RegisterRepository registerRepository;
    private final TypeRegisterRepository typeRegisterRepository;
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public TextServiceImpl(ModelService modelService, RegisterRepository registerRepository, TypeRegisterRepository typeRegisterRepository) {
        this.modelService = modelService;
        this.registerRepository = registerRepository;
        this.typeRegisterRepository = typeRegisterRepository;
    }

    @Autowired
    private HttpServletRequest request;

    @Override
    public TextResponse processText(TextRequest textRequest) {
        StringBuilder respuesta = null;
        try {
            String text = textRequest.getText();
            respuesta = modelService.processWithModel(text);
        } catch (GateException e) {
            throw new RuntimeException(e);
        }

        Coordinate coordinate = new Coordinate(
                textRequest.getLocation().getX(),
                textRequest.getLocation().getY()
        );
        Point location = geometryFactory.createPoint(coordinate);

        Register register = new Register();
        register.setLocation(location);
        String clientIp = getClientIp();
        register.setIpAddress(clientIp);
        register.setTypeRegister(typeRegisterRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("TypeRegister predeterminado no encontrado")));


        this.registerRepository.save(register);

        assert respuesta != null;
        return new TextResponse(respuesta.toString());
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
