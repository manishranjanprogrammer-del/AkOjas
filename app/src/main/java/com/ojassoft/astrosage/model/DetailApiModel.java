package com.ojassoft.astrosage.model;

import java.io.Serializable;

/**
 * Created by ojas-02 on 2/2/18.
 */

public class DetailApiModel implements Serializable {

    String langCode;
    String year;
    String cityId;

    public DetailApiModel(String langCode, String year,String cityId) {
        this.langCode = langCode;
        this.year = year;
        this.cityId = cityId;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
