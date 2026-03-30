package com.ojassoft.astrosage.varta.receiver;

public class AstroLiveDataManager {
    private static final AstroAcceptLiveData astroAcceptLiveData = new AstroAcceptLiveData();

    public static void updateViews(String message){
        astroAcceptLiveData.setView(message);
    }

    public static AstroAcceptLiveData getAstroAcceptLiveData() {
        return astroAcceptLiveData;
    }
}