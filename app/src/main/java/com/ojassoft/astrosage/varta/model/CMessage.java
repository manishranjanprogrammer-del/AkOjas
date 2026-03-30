package com.ojassoft.astrosage.varta.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class CMessage implements Comparable<CMessage> {
    static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
    private String title;
    private URL link;

    private String postId;
    private String loveMarriagePersonalReltaion;
    private String career;
    private String health;
    private String advice;
    private String general;
    private String finance;
    private String tradeFinance;
    private String familyFriends;
    private String imageUrl;
    private String creator;
    private String category;
    private String encoded;

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    private String description;
    private Date date;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // getters and setters omitted for brevity
    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate() {
        return FORMATTER.format(this.date);
    }

    public void setDate(String date) {
        // pad the date if necessary
       /* while (!date.endsWith("00")) {
            date += "0";
        }*/
        try {
            //FORMATTER.setTimeZone(TimeZone.getTimeZone("IST"));
            this.date = FORMATTER.parse(date.trim());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* @Override
     public String toString() {
              // omitted for brevity
     }

     @Override
     public int hashCode() {
             // omitted for brevity
     }

     @Override
     public boolean equals(Object obj) {
             // omitted for brevity
     }*/
    // sort by date
    public int compareTo(CMessage another) {
        if (another == null) return 1;
        // sort descending, most recent first
        return another.date.compareTo(date);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setLoveMarriagePersonalReltaion(String loveMarriagePersonalReltaion) {
        this.loveMarriagePersonalReltaion = loveMarriagePersonalReltaion;
    }

    public String getLoveMarriagePersonalReltaion() {
        return loveMarriagePersonalReltaion;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getCareer() {
        return career;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getHealth() {
        return health;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public void setGeneral(String genral) {
        this.general = genral;
    }

    public String getGeneral() {
        return general;
    }

    public String getFinance() {
        return finance;
    }

    public void setFinance(String finance) {
        this.finance = finance;
    }

    public String getFamilyFriends() {
        return familyFriends;
    }

    public String getTradeFinance() {
        return tradeFinance;
    }

    public void setTradeFinance(String tradeFinance) {
        this.tradeFinance = tradeFinance;
    }

    public void setFamilyFriends(String familyFriends) {
        this.familyFriends = familyFriends;
    }
}

