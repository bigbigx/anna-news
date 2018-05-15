package com.zuohaomeng.anna_news.bean;


import java.util.List;

public class News {
    private String title;
    private String content;
    private String picUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    private List<NewsComment> comments;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String comment;

    public News(String title, String content, String picUrl, List<NewsComment> comments, String comment) {
        this.title = title;
        this.content = content;
        this.picUrl = picUrl;
        this.comments = comments;
        this.comment = comment;
    }

    public News() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<NewsComment> getComments() {
        return comments;
    }

    public void setComments(List<NewsComment> comments) {
        this.comments = comments;
    }
}
