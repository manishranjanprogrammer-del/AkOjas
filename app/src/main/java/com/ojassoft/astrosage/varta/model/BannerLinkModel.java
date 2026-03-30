package com.ojassoft.astrosage.varta.model;

public class BannerLinkModel {
    private boolean loginrequired;
    private String link;

    public boolean isLoginrequired() {
        return loginrequired;
    }

    public void setLoginrequired(boolean loginrequired) {
        this.loginrequired = loginrequired;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
