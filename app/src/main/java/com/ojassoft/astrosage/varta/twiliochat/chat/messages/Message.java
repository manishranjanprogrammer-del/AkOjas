package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.text.TextUtils;

public class Message {

    private String author = "";
    private String dateCreated = "";
    private String messageBody = "";
    private boolean isSeen;
    private long chatId;
    private int like;
    private int unlike;
    private boolean isError;
    private boolean isDelayed;
    private int[] planetsInRashiLagna;
    private double[] planetDegreeArray;
    private String answerId;
    private long timeStamp;
    public String getAuthor() {
        if (author == null){
            return "";
        }
        return author;
    }
    private boolean isAnswerIdMissing = false;

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateCreated() {
        if (dateCreated == null){
            return "";
        }
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getMessageBody() {
        if (messageBody == null){
            return "";
        }
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }


    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }


    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setLike(int like) {
        this.like = like;
    }
    public void setUnlike(int unlike) {
        this.unlike = unlike;
    }

    public int getLike() {
        return like;
    }
    public int getUnlike() {
        return unlike;
    }

    public boolean getIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }
    public boolean isDelayed() {
        return isDelayed;
    }

    public void setDelayed(boolean delayed) {
        isDelayed = delayed;
    }

    public int[] getPlanetsInRashiLagna() {
        return planetsInRashiLagna;
    }

    public void setPlanetsInRashiLagna(int[] planetsInRashiLagna) {
        this.planetsInRashiLagna = planetsInRashiLagna;
    }

    public double[] getPlanetDegreeArray() {
        return planetDegreeArray;
    }

    public void setPlanetDegreeArray(double[] planetDegreeArray) {
        this.planetDegreeArray = planetDegreeArray;
    }


    public String getAnswerId() {
        if (TextUtils.isEmpty(this.answerId)){
            this.answerId = "";
        }
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public boolean isAnswerIdMissing() {
        return isAnswerIdMissing;
    }

    public void setAnswerIdMissing(boolean answerIdMissing) {
        isAnswerIdMissing = answerIdMissing;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}