package com.miguel.springboot.suicidedetection.suicidedetection.services;

import gate.util.GateException;

public interface ModelService {
    StringBuilder processWithModel(String text) throws GateException;
}
