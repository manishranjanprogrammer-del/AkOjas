package com.ojassoft.astrosage.varta.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LiveAstrologerModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @SerializedName("astrologerid")
    private String id;
    @SerializedName("astrologername")
    private String name;
    @SerializedName("channelname")
    private String channelName;
    @SerializedName("audiencetoken")
    private String token;
    @SerializedName("imageurl")
    private String profileImgUrl;
    @SerializedName("expertise")
    private String expertise;
    @SerializedName("urltext")
    private String urltext;
    @SerializedName("liveaudioprice")
    private String liveaudioprice;
    @SerializedName("liveaudiointroprice")
    private String liveaudiointroprice;
    @SerializedName("actualliveaudioprice")
    private String actualliveaudioprice;
    @SerializedName("experience")
    private String experience;

    @SerializedName("livevideoprice")
    private String livevideoprice;
    @SerializedName("livevideointroprice")
    private String livevideointroprice;
    @SerializedName("actuallivevideoprice")
    private String actuallivevideoprice;

    @SerializedName("liveanonymousprice")
    private String liveanonymousprice;
    @SerializedName("liveanonymousintroprice")
    private String liveanonymousintroprice;
    @SerializedName("actualliveanonymousprice")
    private String actualliveanonymousprice;

    @SerializedName("isavailableforcall")
    private String isavailableforcall;

    @SerializedName("privateintrooffer")
    private boolean privateIntroOffer;

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

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrltext() {
        return urltext;
    }

    public void setUrltext(String urltext) {
        this.urltext = urltext;
    }

    public String getLiveaudioprice() {
        return liveaudioprice;
    }

    public void setLiveaudioprice(String liveaudioprice) {
        this.liveaudioprice = liveaudioprice;
    }

    public String getLiveaudiointroprice() {
        return liveaudiointroprice;
    }

    public void setLiveaudiointroprice(String liveaudiointroprice) {
        this.liveaudiointroprice = liveaudiointroprice;
    }

    public String getActualliveaudioprice() {
        return actualliveaudioprice;
    }

    public void setActualliveaudioprice(String actualliveaudioprice) {
        this.actualliveaudioprice = actualliveaudioprice;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLivevideoprice() {
        return livevideoprice;
    }

    public void setLivevideoprice(String livevideoprice) {
        this.livevideoprice = livevideoprice;
    }

    public String getLivevideointroprice() {
        return livevideointroprice;
    }

    public void setLivevideointroprice(String livevideointroprice) {
        this.livevideointroprice = livevideointroprice;
    }

    public String getActuallivevideoprice() {
        return actuallivevideoprice;
    }

    public void setActuallivevideoprice(String actuallivevideoprice) {
        this.actuallivevideoprice = actuallivevideoprice;
    }

    public String getLiveanonymousprice() {
        return liveanonymousprice;
    }

    public void setLiveanonymousprice(String liveanonymousprice) {
        this.liveanonymousprice = liveanonymousprice;
    }

    public String getLiveanonymousintroprice() {
        return liveanonymousintroprice;
    }

    public void setLiveanonymousintroprice(String liveanonymousintroprice) {
        this.liveanonymousintroprice = liveanonymousintroprice;
    }

    public String getActualliveanonymousprice() {
        return actualliveanonymousprice;
    }

    public void setActualliveanonymousprice(String actualliveanonymousprice) {
        this.actualliveanonymousprice = actualliveanonymousprice;
    }

    public String getIsavailableforcall() {
        return isavailableforcall;
    }

    public void setIsavailableforcall(String isavailableforcall) {
        this.isavailableforcall = isavailableforcall;
    }

    public boolean isPrivateIntroOffer() {
        return privateIntroOffer;
    }

    public void setPrivateIntroOffer(boolean privateIntroOffer) {
        this.privateIntroOffer = privateIntroOffer;
    }
}
