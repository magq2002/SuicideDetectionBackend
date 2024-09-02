package com.miguel.springboot.suicidedetection.suicidedetection.config;

import gate.Factory;
import gate.Gate;
import gate.creole.Plugin;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class GateConfig {

    @Bean
    public SerialAnalyserController serialAnalyserController() throws GateException {
        // Inicializar GATE
        Gate.init();

        // Cargar ANNIE
        Plugin anniePlugin = new Plugin.Maven(
                "uk.ac.gate.plugins", "annie", gate.Main.version);
        Gate.getCreoleRegister().registerPlugin(anniePlugin);

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
                "gate.creole.orthomatcher.OrthoMatcher")) {
            annieController.add((gate.LanguageAnalyser) Factory.createResource(pr));
        }
        return annieController;
    }


    // Cargar el modelo de clasificación de textos


}
