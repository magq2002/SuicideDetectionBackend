package com.miguel.springboot.suicidedetection.suicidedetection.services;

import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextRequest;
import com.miguel.springboot.suicidedetection.suicidedetection.common.dtos.TextResponse;
import gate.util.GateException;

public interface TextService {
    TextResponse processText(TextRequest textRequest);
}
