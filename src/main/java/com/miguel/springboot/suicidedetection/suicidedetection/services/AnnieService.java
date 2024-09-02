package com.miguel.springboot.suicidedetection.suicidedetection.services;

import gate.util.GateException;

public interface AnnieService {
    void processWithAnnie(String text) throws GateException;
}
