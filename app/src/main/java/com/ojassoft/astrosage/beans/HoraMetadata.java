package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class HoraMetadata implements Serializable {

    private String Planetdata;
    private String Entertimedata;
    private String Exittimedata;
    private String Planetmeaning;
    private String PlanetCurrentHorameaning;
    private String doghatiSecondMeaning;
    private String doghatiSecondMeaningwikipedia;
    private String doghatimuhurat;

    public String getDoghatimuhurat() {
        return doghatimuhurat;
    }

    public void setDoghatimuhurat(String doghatimuhurat) {
        this.doghatimuhurat = doghatimuhurat;
    }

    public String getPlanetCurrentHorameaning() {
        return PlanetCurrentHorameaning;
    }

    public void setPlanetCurrentHorameaning(String planetCurrentHorameaning) {
        PlanetCurrentHorameaning = planetCurrentHorameaning;
    }

    public String getPlanetmeaning() {
        return Planetmeaning;
    }

    public String getDoghatiSecondMeaning() {
        return doghatiSecondMeaning;
    }

    public void setDoghatiSecondMeaning(String doghatiSecondMeaning) {
        this.doghatiSecondMeaning = doghatiSecondMeaning;
    }

    public String getDoghatiSecondMeaningwikipedia() {
        return doghatiSecondMeaningwikipedia;
    }

    public void setDoghatiSecondMeaningwikipedia(
            String doghatiSecondMeaningwikipedia) {
        this.doghatiSecondMeaningwikipedia = doghatiSecondMeaningwikipedia;
    }

    public void setPlanetmeaning(String planetmeaning) {
        Planetmeaning = planetmeaning;
    }

    public String getPlanetdata() {
        return Planetdata;
    }

    public void setPlanetdata(String planetdata) {
        Planetdata = planetdata;
    }

    public String getEntertimedata() {
        return Entertimedata;
    }

    public void setEntertimedata(String entertimedata) {
        Entertimedata = entertimedata;
    }

    public String getExittimedata() {
        return Exittimedata;
    }

    public void setExittimedata(String exittimedata) {
        Exittimedata = exittimedata;
    }


}
