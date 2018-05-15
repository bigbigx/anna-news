package com.zuohaomeng.anna_news.test;

import com.google.gson.Gson;
import com.zuohaomeng.anna_news.Util.Constant;
import com.zuohaomeng.anna_news.bean.News;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainTest {

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();


        String url = Constant.baseUrl+Constant.TECENT_NEWS_URL_INTERFACE;
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result_json = response.body().string();
                //System.out.println(result_json);
                Gson gson = new Gson();
                ArrayList listNews = gson.fromJson(result_json, ArrayList.class);
                int newsCount = listNews.size();
                for (int i = 0; i < newsCount; i++) {
//                   System.out.println(listNews.get(i));
                    News news = gson.fromJson(gson.toJson(listNews.get(i)), News.class);
                    System.out.println(news.getTitle());
                    System.out.println(news.getContent());
                    System.out.println(news.getPicUrl());
                }

            }
        });
    }
}
