package com.ojassoft.astrosage.varta.model;

import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageBean implements Serializable {

    //@SerializedName("From")
    private String From="";

    //@SerializedName("Text")
    private String Text="";

    //@SerializedName("To")
    private String To="";

    private long MsgTime = 0L;

    @PropertyName("MsgTime")
    public long getMsgTime() {
        return MsgTime;
    }

    @PropertyName("MsgTime")
    public void setMsgTime(long msgTime) {
        MsgTime = msgTime;
    }

    @PropertyName("From")
    public String getFrom() {
        return From;
    }

    @PropertyName("From")
    public void setFrom(String from) {
        From = from;
    }

    @PropertyName("Text")
    public String getText() {
        return Text;
    }

    @PropertyName("Text")
    public void setText(String text) {
        Text = text;
    }

    @PropertyName("To")
    public String getTo() {
        return To;
    }

    @PropertyName("To")
    public void setTo(String to) {
        To = to;
    }

}
