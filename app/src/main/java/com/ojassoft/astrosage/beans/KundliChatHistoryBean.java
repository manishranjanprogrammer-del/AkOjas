package com.ojassoft.astrosage.beans;

import androidx.annotation.NonNull;

import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;

import java.util.List;

public class KundliChatHistoryBean {

    private String title = "";
    private String description = "";
    private String astrologerId = "";
    private String conversationId = "";
    private String lastConversationDate = "";
    private String localChartId="";
    private String onlineChartId="";
    private String moduleName="";

    private List<UserMessage> messagesList;
    private boolean isSelected;


    public String getLastConversationDate() {
        return lastConversationDate;
    }


    public String getOnlineChartId() {
        return onlineChartId;
    }

    public void setOnlineChartId(String onlineChartId) {
        this.onlineChartId = onlineChartId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAstrologerId() {
        return astrologerId;
    }

    public void setAstrologerId(String astrologerId) {
        this.astrologerId = astrologerId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public void setConversationEndTime(String conversationEndTime) {
        this.lastConversationDate = conversationEndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getLocalChartId() {
        return localChartId;
    }

    public void setLocalChartId(String localChartId) {
        this.localChartId = localChartId;
    }

    public String getModuleName(){
        return moduleName;
    }

    public void setModuleName(String moduleName){
        this.moduleName = moduleName;
    }

    public List<UserMessage> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<UserMessage> messagesContent) {
        this.messagesList = messagesContent;
    }

    @NonNull
    @Override
    public String toString() {
        return "{ "
                    +"conversationId : " + conversationId+
                    " conversation_date_&_time : " + lastConversationDate+
                " }";
    }
}