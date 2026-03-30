package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;

public class FollowAstrologerModel implements Serializable {

    private String astrologerName;
    private String astrologerImage;
    private String followingStatus;
    private String astrologerId;
    private String userId;


    public void setAstrologerImage(String astrologerImage) {
        this.astrologerImage = astrologerImage;
    }

    public void setAstrologerName(String astrologerName) {
        this.astrologerName = astrologerName;
    }

    public void setFollowingStatus(String followingStatus) {
        this.followingStatus = followingStatus;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAstrologerId(String astrologerId) {
        this.astrologerId = astrologerId;
    }

    public String getAstrologerImage() {
        return astrologerImage;
    }

    public String getAstrologerName() {
        return astrologerName;
    }

    public String getFollowingStatus() {
        return followingStatus;
    }

    public String getUserId() {
        return userId;
    }

    public String getAstrologerId() {
        return astrologerId;
    }
}
