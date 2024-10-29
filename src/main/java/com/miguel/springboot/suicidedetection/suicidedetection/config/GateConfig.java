package com.miguel.springboot.suicidedetection.suicidedetection.config;

import gate.Factory;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.Plugin;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@Configuration
public class GateConfig {

    @Bean
    public SerialAnalyserController serialAnalyserController() throws GateException, MalformedURLException {
        // Inicializar GATE
        Gate.init();

        // Cargar ANNIE
        Plugin anniePlugin = new Plugin.Maven(
                "uk.ac.gate.plugins", "annie", gate.Main.version);
        Gate.getCreoleRegister().registerPlugin(anniePlugin);

        Plugin learningPlugin = new Plugin.Maven(
                "uk.ac.gate.plugins", "learningframework", "4.2");
        Gate.getCreoleRegister().registerPlugin(learningPlugin);

        // create a serial analyser controller to run ANNIE with
        SerialAnalyserController annieController =
                (SerialAnalyserController) Factory.createResource(
                        "gate.creole.SerialAnalyserController",
                        Factory.newFeatureMap(),
                        Factory.newFeatureMap(), "ANNIE");
        for (String pr : Arrays.asList(
                "gate.creole.annotdelete.AnnotationDeletePR",
                "gate.creole.tokeniser.DefaultTokeniser",
                "gate.creole.splitter.SentenceSplitter",
                "gate.creole.POSTagger",
                "gate.creole.ANNIETransducer",
                "gate.creole.orthomatcher.OrthoMatcher"
                )) {
            annieController.add((gate.LanguageAnalyser) Factory.createResource(pr));
        }

        ProcessingResource evaluatePR = (ProcessingResource) Factory.createResource(
                "gate.plugin.learningframework.LF_ApplyClassification");

        //evaluatePR.setParameterValue("dataDirectory", "file:/C:/Users/magq2/Documents/proyectoGradoTwitter/datos/trainedModel");
        //evaluatePR.setParameterValue("instanceType", "Key");
        //evaluatePR.setParameterValue("outputASName", "Learning");
        //evaluatePR.setParameterValue("modelURL", new URL("file:/C:/Users/magq2/Documents/proyectoGradoTwitter/datos/trainedModel/lf.model"));

        //annieController.add(evaluatePR);
        return annieController;
    }


    // Cargar el modelo de clasificación de textos


}
