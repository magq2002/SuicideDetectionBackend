package com.miguel.springboot.suicidedetection.suicidedetection.config;

import gate.*;
import gate.creole.Plugin;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

@Configuration
public class GateConfig {

    @Bean
    public SerialAnalyserController serialAnalyserController() throws GateException, MalformedURLException {
        Gate.init();
        Plugin anniePlugin = new Plugin.Maven(
                "uk.ac.gate.plugins", "annie", "9.1");
        Gate.getCreoleRegister().registerPlugin(anniePlugin);
        Plugin pythonPlugin = new Plugin.Maven(
                "uk.ac.gate.plugins", "python", "3.0.7");
        Gate.getCreoleRegister().registerPlugin(pythonPlugin);
        SerialAnalyserController modelController =
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
            modelController.add((gate.LanguageAnalyser) Factory.createResource(pr));
        }
        URL scriptUrl = new File("src/main/resources/apply_prediction.py").toURI().toURL();
        String modelPathUrl = "C:\\Users\\magq2\\Documents\\entrenoBert\\modelo_final.pt";

        FeatureMap params = Factory.newFeatureMap();
        params.put("pythonProgram", scriptUrl.toString());
        params.put("pythonBinary", "C:\\Users\\magq2\\.conda\\envs\\GATEPython\\python.exe");
        FeatureMap programParams = Factory.newFeatureMap();
        programParams.put("model_path", modelPathUrl);
        programParams.put("gpu", "true");
        programParams.put("workingSet", "Suicide");
        params.put("programParams", programParams);
        ProcessingResource pythonPR = (ProcessingResource) Factory.createResource("gate.plugin.python.PythonPr", params);
        modelController.add(pythonPR);
        return modelController;
    }
}
