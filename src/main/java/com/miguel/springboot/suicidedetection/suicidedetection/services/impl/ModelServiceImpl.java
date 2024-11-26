package com.miguel.springboot.suicidedetection.suicidedetection.services.impl;

import com.miguel.springboot.suicidedetection.suicidedetection.services.ModelService;
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
public class ModelServiceImpl implements ModelService {

    private final SerialAnalyserController modelController;

    @Autowired
    public ModelServiceImpl(SerialAnalyserController modelController) {
        this.modelController = modelController;
    }

    @Override
    public StringBuilder processWithModel(String text) throws GateException {
        Corpus corpus = Factory.newCorpus("Mi Corpus");
        modelController.setCorpus(corpus);
        Document document = Factory.newDocument(text);
        corpus.add(document);
        modelController.execute();

        StringBuilder result = new StringBuilder();
        AnnotationSet defaultAnnotSet = document.getAnnotations("Suicide");
        for (Annotation annotation : defaultAnnotSet) {
            result.append(annotation.getFeatures());
            //result.append("Texto Anotado: ").append(document.getContent().getContent(annotation.getStartNode().getOffset(), annotation.getEndNode().getOffset())).append("\n");
        }
        return new StringBuilder(result.toString().trim());
    }
}
