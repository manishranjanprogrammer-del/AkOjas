package com.ojassoft.astrosage.varta.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Horoscope {
    private HoroscopeOutput horoscope_output;
    private ForecastOutput forecast_output;
    private List<Score> scores_output;
    private String message;
    private String status;

    public HoroscopeOutput getHoroscope_output() {
        return horoscope_output;
    }

    public ForecastOutput getForecast_output() {
        return forecast_output;
    }



    public List<Score> getScoresOutput() { return scores_output; }
    public void setScoresOutput(List<Score> scoresOutput) { this.scores_output = scores_output; }


}


