package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ChalisaDataModel implements Serializable {

    @SerializedName("AudioVideoid")
    private String audioVideoid;
    @SerializedName("Title")
    private String title;
    @SerializedName("Description")
    private String description;
    @SerializedName("Thumbnails")
    private String thumbnail;
    @SerializedName("VideoURL")
    private String videoURL;
    @SerializedName("AudioURL")
    private String audioURL;
    @SerializedName("categoryName")
    private String CategoryName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAudioVideoid() {
        return audioVideoid;
    }

    public void setAudioVideoid(String audioVideoid) {
        this.audioVideoid = audioVideoid;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
