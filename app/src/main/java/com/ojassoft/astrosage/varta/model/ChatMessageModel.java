package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;

public class ChatMessageModel implements Serializable {


    private String From = "", To = "", Text = "";

    private long MsgTime;

    private String Type;

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public long getMsgTime() {
        return MsgTime;
    }

    public void setMsgTime(long msgTime) {
        MsgTime = msgTime;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
