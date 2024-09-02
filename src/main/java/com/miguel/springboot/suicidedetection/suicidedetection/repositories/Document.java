package com.miguel.springboot.suicidedetection.suicidedetection.repositories;

public class Document {

    private String id;
    private String name;
    private String corpus;
    private String isSuicide;

    public Document() {
    }

    public Document(String id, String name, String corpus, String isSuicide) {
        this.id = id;
        this.name = name;
        this.corpus = corpus;
        this.isSuicide = isSuicide;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getIsSuicide() {
        return isSuicide;
    }

    public void setIsSuicide(String isSuicide) {
        this.isSuicide = isSuicide;
    }
}
