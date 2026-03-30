package com.ojassoft.astrosage.model;

public class YoutubeKeySingleton {

    private static YoutubeKeySingleton youtubeKeySingleton;
    private String apiKey;

    private YoutubeKeySingleton() {

    }

    public static YoutubeKeySingleton getInstance() {
        if (youtubeKeySingleton == null) {
            youtubeKeySingleton = new YoutubeKeySingleton();
        }
        return youtubeKeySingleton;
    }

    public String getApiKey() {
        if (apiKey == null) {
            apiKey = "";
        }
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
