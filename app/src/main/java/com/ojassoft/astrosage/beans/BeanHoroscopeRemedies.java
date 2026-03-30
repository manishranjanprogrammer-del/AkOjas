package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ojas-20 on 25/4/18.
 */

public class BeanHoroscopeRemedies {
    @SerializedName("Rashi")
    @Expose
    private String rashi;
    @SerializedName("Remedy")
    @Expose
    private String remedy;
    @SerializedName("Health")
    @Expose
    private String health;
    @SerializedName("Wealth")
    @Expose
    private String wealth;
    @SerializedName("Family")
    @Expose
    private String family;
    @SerializedName("LoveMatters")
    @Expose
    private String loveMatters;

    @SerializedName("Occupation")
    @Expose
    private String occupation;
    @SerializedName("MarriedLife")
    @Expose
    private String marriedLife;

    @SerializedName("Sentence")
    @Expose
    private String sentence;
    @SerializedName("LuckyNumber")
    @Expose
    private String luckyNumber;

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
    public String getLuckyNumber() {
        return luckyNumber;
    }

    public void setLuckyNumber(String luckyNumber) {
        this.luckyNumber = luckyNumber;
    }
    public String getMarriedLife() {
        return marriedLife;
    }

    public void setMarriedLife(String marriedLife) {
        this.marriedLife = marriedLife;
    }

    public String getRashi() {
        return rashi;
    }

    public void setRashi(String rashi) {
        this.rashi = rashi;
    }

    public String getRemedy() {
        return remedy;
    }

    public void setRemedy(String remedy) {
        this.remedy = remedy;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getWealth() {
        return wealth;
    }

    public void setWealth(String wealth) {
        this.wealth = wealth;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getLoveMatters() {
        return loveMatters;
    }

    public void setLoveMatters(String loveMatters) {
        this.loveMatters = loveMatters;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
