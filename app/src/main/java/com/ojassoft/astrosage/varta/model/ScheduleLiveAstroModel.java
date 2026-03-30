package com.ojassoft.astrosage.varta.model;

import com.google.gson.annotations.SerializedName;

public class ScheduleLiveAstroModel {
    @SerializedName("name")
    private String name;
    @SerializedName("date")
    private String date;
    @SerializedName("image")
    private String image;
    @SerializedName("prId")
    private String id;
    @SerializedName("topic")
    private String topic;
    @SerializedName("time")
    private String time;
    @SerializedName("url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
