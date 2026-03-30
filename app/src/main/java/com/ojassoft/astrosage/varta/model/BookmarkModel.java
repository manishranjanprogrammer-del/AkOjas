package com.ojassoft.astrosage.varta.model;

public class BookmarkModel {

    int id =0;
    String astrologerId="";
    String bookmarkStatus="";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAstrologerId() {
        return astrologerId;
    }

    public void setAstrologerId(String astrologerId) {
        this.astrologerId = astrologerId;
    }

    public String getBookmarkStatus() {
        return bookmarkStatus;
    }

    public void setBookmarkStatus(String bookmarkStatus) {
        this.bookmarkStatus = bookmarkStatus;
    }
}
