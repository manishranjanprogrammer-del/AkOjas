package com.ojassoft.astrosage.beans;

/**
 * Created by ojas on 6/2/17.
 */

public class Bean1 {

    private String title;
    private String desc;
    private boolean withUrl;
    private String url;
    private boolean isLiabraryNeed;

    public Bean1(String title, String desc, boolean withUrl, String url, boolean isLiabraryNeed) {
        this.title = title;
        this.desc = desc;
        this.withUrl = withUrl;
        this.url = url;
        this.isLiabraryNeed = isLiabraryNeed;
    }

    public String getTitle() {

        return title;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isWithUrl() {
        return withUrl;
    }

    public String getUrl() {
        return url;
    }

    public boolean isLiabraryNeed() {
        return isLiabraryNeed;
    }
}
