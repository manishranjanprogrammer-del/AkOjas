package com.ojassoft.astrosage.varta.model;

public class Language {
    private String name;
    private int flagResId; // drawable resource id for flag
    private int languageId; // language id from server


    public Language(String name, int flagResId, int languageId) {
        this.name = name;
        this.flagResId = flagResId;
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public int getFlagResId() {
        return flagResId;
    }
    public int getLanguageId() {
        return languageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlagResId(int flagResId) {
        this.flagResId = flagResId;
    }
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }
}

