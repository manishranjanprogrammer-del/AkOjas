package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.content.Context;
import android.text.TextUtils;

import com.ojassoft.astrosage.varta.model.MessageBean;

public class UserMessage implements ChatMessage {

    private boolean isError;
    private String author = "";
    private String dateCreated = "";
    private String messageBody = "";
    private boolean isSeen;
    private long chatId;
    private boolean isSpeaking;
    private String answerId = "";

    private int like;
    private int unlike;
    private boolean isDelayed;
    private int[] planetsInRashiLagna;
    private double[] planetDegreeArray;
    private boolean isAnswerIdMissing = false;
    private long timeStamp;
    public UserMessage(){}

    public UserMessage(Message message) {
        this.author = message.getAuthor();
        this.dateCreated = message.getDateCreated();
        this.messageBody = message.getMessageBody();
        this.chatId = message.getChatId();
        this.isSeen = message.isSeen();
        this.like = message.getLike();
        this.unlike = message.getUnlike();
        this.isDelayed = message.isDelayed();
        this.planetDegreeArray = message.getPlanetDegreeArray();
        this.planetsInRashiLagna = message.getPlanetsInRashiLagna();
        this.answerId = message.getAnswerId();
        this.isAnswerIdMissing = message.isAnswerIdMissing();
        this.timeStamp = message.getTimeStamp();
    }


    @Override
    public String getMessageBody(Context context) {
        return messageBody;
    }

    public String getMessageBody(){
        return messageBody;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getDateCreated() {
        return dateCreated;
    }

    @Override
    public boolean isSeen() {
        return isSeen;
    }

    @Override
    public void setSeen(boolean seen) {
        this.isSeen = seen;
    }

    @Override
    public boolean isDelayed() {
        return isDelayed;
    }

    @Override
    public void setDelayed(boolean isDelayed) {
        this.isDelayed = isDelayed;
    }

    @Override
    public long chatId() {
        return chatId;
    }

    @Override
    public int getLike() {
        return like;
    }

    @Override
    public int getUnlike() {
        return unlike;
    }

    @Override
    public void setLike(int like) {
        this.like = like;
    }

    @Override
    public void setUnlike(int unlike) {
        this.unlike = unlike;
    }

    @Override
    public boolean getIsError() {
        return isError;
    }

    @Override
    public void setIsError(boolean isError) {
        this.isError = isError;
    }


    @Override
    public void setMessageBody(String body) {
        this.messageBody = body;
    }

    @Override
    public int[] getPlanetsInRashiLagna() {
        return planetsInRashiLagna;
    }

    @Override
    public void setPlanetsInRashiLagna(int[] planetsInRashiLagna) {
        this.planetsInRashiLagna = planetsInRashiLagna;
    }

    @Override
    public double[] getPlanetDegreeArray() {
        return planetDegreeArray;
    }

    @Override
    public void setPlanetDegreeArray(double[] planetDegreeArray) {
        this.planetDegreeArray = planetDegreeArray;
    }
    @Override
    public String getAnswerId() {
        if (TextUtils.isEmpty(this.answerId)){
            this.answerId = "";
        }
        return answerId;
    }

    @Override
    public boolean getIsSpeaking() {
        return isSpeaking;
    }

    @Override
    public void setIsSpeaking(boolean isSpeaking) {
        this.isSpeaking = isSpeaking;
    }


    @Override
    public boolean isAnswerIdMissing() {
        return isAnswerIdMissing;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public long getTimeStamp() { return timeStamp; }

    @Override
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageBody='" + messageBody + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}