package com.ojassoft.astrosage.ui.fragments.astrochatfragment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;

/**
 * Created by ojas-10 on 7/3/16.
 */
public class MessageDecode implements Parcelable{


    /*
    *
    * "MsgSentFrom": "System",
      "MsgReceivedTo": "swat@ojassoft.com",
      "Message": "",
      "MsgDate": "Sep 28 2016 11:37AM",
      "OrderId": "0",
      "AnswerRating": "",
      "PropertyUserType": "System",
      "PropertyColorOfMsg": "blue",
      "PropertyRatingToShow": "False",
      "PropertyShareLinkToShow": "True"
    * */
    @SerializedName("MsgTitle")
    private String messageTextTitle;

    @SerializedName("MsgReceivedTo")
    private String msgRecivedto;

    @SerializedName("Message")
    private String messageText;

    @SerializedName("MsgSentFrom")
    private String userType;

    @SerializedName("PropertyColorOfMsg")
    private String colorOfMessage;

    @SerializedName("PropertyRatingToShow")
    private String rateShow;

    //  @SerializedName("key")
    private String notPaidLayoutShow;

    @SerializedName("PropertyShareLinkToShow")
    private String shareLinkShow;

    @SerializedName("MsgDate")
    private String dateTimeShow;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @SerializedName("ChatId")
    private String chatId;

    // @SerializedName("key")

    public String getMsgRecivedto() {
        return msgRecivedto;
    }

    public void setMsgRecivedto(String msgRecivedto) {
        this.msgRecivedto = msgRecivedto;
    }

    private int noOfQuestion;

    @SerializedName("AnswerRating")
    private String rating;

    public String getRateShow() {
        return rateShow;
    }

    public String getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(String ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @SerializedName("key")
    private String ratingStar;

    public String getAstrologerName() {
        return astrologerName;
    }

    public void setAstrologerName(String astrologerName) {
        this.astrologerName = astrologerName;
    }

    @SerializedName("OrderId")
    private String orderId;

    public String getAstrologerImagePath() {
        return astrologerImagePath;
    }

    public void setAstrologerImagePath(String astrologerImagePath) {
        this.astrologerImagePath = astrologerImagePath;
    }

    @SerializedName("AstrologerName")

    private String astrologerName;


    @SerializedName("AstrologerImagePath")
    private String astrologerImagePath;

    public AstrologerServiceInfo getAstrologerServiceInfo() {
        return astrologerServiceInfo;
    }

    public void setAstrologerServiceInfo(AstrologerServiceInfo astrologerServiceInfo) {
        this.astrologerServiceInfo = astrologerServiceInfo;
    }

    private AstrologerServiceInfo astrologerServiceInfo;


    public MessageDecode()
    {

    }
    public MessageDecode(Parcel parcel)
    {
        messageText=parcel.readString();
        userType=parcel.readString();
        colorOfMessage=parcel.readString();
        rateShow=parcel.readString();
        shareLinkShow=parcel.readString();
        notificationShow=(parcel.readString());
        notPaidLayoutShow=(parcel.readString());
        dateTimeShow=parcel.readString();
        noOfQuestion=parcel.readInt();
        rating=parcel.readString();
        ratingStar=parcel.readString();
        orderId=parcel.readString();
        msgRecivedto=parcel.readString();
        chatId=parcel.readString();
        astrologerName=parcel.readString();
        astrologerImagePath=parcel.readString();
        messageTextTitle=parcel.readString();
        astrologerServiceInfo = (AstrologerServiceInfo) parcel.readSerializable();
    }

    public String getShareLinkShow() {
        return shareLinkShow;
    }

    public void setShareLinkShow(String shareLinkShow) {
        this.shareLinkShow = shareLinkShow;
    }




    public String isRateShow() {
        return rateShow;
    }


    public void setRateShow(String rateShow) {
        this.rateShow = rateShow;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getColorOfMessage() {
        return colorOfMessage;
    }

    public void setColorOfMessage(String colorOfMessage) {
        this.colorOfMessage = colorOfMessage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getNotificationShow() {
        return notificationShow;
    }

    public void setNotificationShow(String notificationShow) {
        this.notificationShow = notificationShow;
    }




    private String notificationShow;

    public String getDateTimeShow() {
        return dateTimeShow;
    }

    public void setDateTimeShow(String dateTimeShow) {
        this.dateTimeShow = dateTimeShow;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageText);
        dest.writeString(userType);
        dest.writeString(colorOfMessage);
        if(rateShow!=null)
            dest.writeString(rateShow);
        if(shareLinkShow!=null)
            dest.writeString(shareLinkShow);
        else
            dest.writeString("False");

        if(notificationShow!=null)
            dest.writeString(notificationShow);
        else
            dest.writeString("False");

        if(notPaidLayoutShow!=null)
            dest.writeString(notPaidLayoutShow);
        else
            dest.writeString("False");
        dest.writeString(dateTimeShow);
        dest.writeInt(noOfQuestion);
        dest.writeString(rating);
        dest.writeString(ratingStar);
        dest.writeString(orderId);
        dest.writeString(msgRecivedto);
        dest.writeString(chatId);
        dest.writeString(astrologerName);
        dest.writeString(astrologerImagePath);
        dest.writeString(messageTextTitle);
        dest.writeSerializable(astrologerServiceInfo);
    }


    public static final Creator<MessageDecode> CREATOR
            = new Creator<MessageDecode>() {
        public MessageDecode createFromParcel(Parcel in) {
            return new MessageDecode(in);
        }
        public MessageDecode[] newArray(int size) {
            return new MessageDecode[size];
        }
    };

    public int getNoOfQuestion() {
        return noOfQuestion;
    }

    public void setNoOfQuestion(int noOfQuestion) {
        this.noOfQuestion = noOfQuestion;
    }

    public String getNotPaidLayoutShow() {
        return notPaidLayoutShow;
    }

    public void setNotPaidLayoutShow(String notPaidLayoutShow) {
        this.notPaidLayoutShow = notPaidLayoutShow;
    }

    public String getMessageTextTitle() {
        return messageTextTitle;
    }

    public void setMessageTextTitle(String messageTextTitle) {
        this.messageTextTitle = messageTextTitle;
    }
}
