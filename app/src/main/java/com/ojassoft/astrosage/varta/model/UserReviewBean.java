package com.ojassoft.astrosage.varta.model;

public class UserReviewBean {
    String username="";
    String maskedusername="";
    String comment="";
    String rate="";
    String date="";
    String feedbackId="";
    String actualFeedbackId="";
    String countrycode="";

    public String getUserRatingType() {
        return userRatingType;
    }

    public void setUserRatingType(String userRatingType) {
        this.userRatingType = userRatingType;
    }

    public String getUserRatingTypeValue() {
        return userRatingTypeValue;
    }

    public void setUserRatingTypeValue(String userRatingTypeValue) {
        this.userRatingTypeValue = userRatingTypeValue;
    }

    String userRatingType="";
    String userRatingTypeValue="";

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMaskedusername() {
        return maskedusername;
    }

    public void setMaskedusername(String maskedusername) {
        this.maskedusername = maskedusername;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getActualFeedbackId() {
        return actualFeedbackId;
    }

    public void setActualFeedbackId(String actualFeedbackId) {
        this.actualFeedbackId = actualFeedbackId;
    }
}
