package com.zuohaomeng.anna_news.bean;

import com.zuohaomeng.anna_news.bean.News;

public class ResponseNewsList {
    News news;

    public ResponseNewsList(News news) {
        this.news = news;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
