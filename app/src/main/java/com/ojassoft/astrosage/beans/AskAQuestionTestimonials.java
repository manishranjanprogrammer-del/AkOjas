package com.ojassoft.astrosage.beans;

/**
 * Created by ojas on 6/6/17.
 */

public class AskAQuestionTestimonials {

    private int imgUrl;
    private String content;
    private String contentBy;

    public AskAQuestionTestimonials(int imgUrl,String content,String contentBy){
        this.imgUrl = imgUrl;
        this.content = content;
        this.contentBy = contentBy;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public String getContent() {
        return content;
    }

    public String getContentBy() {
        return contentBy;
    }

}
