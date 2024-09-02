package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.services.AnnieService;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.Annotation;
import gate.AnnotationSet;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnieServiceImpl implements AnnieService {

    private final SerialAnalyserController annieController;

    @Autowired
    public AnnieServiceImpl(SerialAnalyserController annieController) {
        this.annieController = annieController;
    }

    @Override
    public void processWithAnnie(String text) throws GateException {
        Corpus corpus = Factory.newCorpus("Mi Corpus");
        annieController.setCorpus(corpus);
        Document document = Factory.newDocument(text);
        corpus.add(document);
        annieController.execute();

        StringBuilder result = new StringBuilder();
        AnnotationSet defaultAnnotSet = document.getAnnotations();
        for (Annotation annotation : defaultAnnotSet) {
            result.append("Tipo: ").append(annotation.getType()).append("\n");
            result.append("Características: ").append(annotation.getFeatures()).append("\n");
            result.append("Texto Anotado: ").append(document.getContent().getContent(annotation.getStartNode().getOffset(), annotation.getEndNode().getOffset())).append("\n");
        }
        //System.out.println(result.toString());
    }
}
