package com.zuohaomeng.anna_news;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.zuohaomeng.anna_news.Util.Constant;
import com.zuohaomeng.anna_news.bean.News;
import com.zuohaomeng.anna_news.fragment.NewsContentFragment;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivityTest extends MainActivity{

    @Test
    public void refrushNews() {
//        Site tencent = new Site();
//        tencent.setPic(R.drawable.news_logo_tencent);
//        tencent.setUrl(Constant.baseUrl + Constant.TECENT_NEWS_URL_INTERFACE);
//        tencent.setTitle("腾讯");
//        try {
//            refrushNews(tencent);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String url = Constant.baseUrl+Constant.TECENT_NEWS_URL_INTERFACE;
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.i("failure", e.getMessage());
                System.out.println("onResponse: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("onResponse", "onResponse: " + response.body().string());
                System.out.println("onResponse: " + response.body().string());
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // NewsContentFragment fragment = new NewsContentFragment();
                //TODO 将返回结果转换为List<News>
                List<News> mNewsList = new ArrayList<>();

                for (int i = 0; i < 1000; i++) {
                    News news1 = new News();
                    news1.setTitle(site.getTitle() + "标题" + i);
                    news1.setPicUrl("");
                    news1.setContent("内容" + i);
                    mNewsList.add(news1);
                }
                NewsContentFragment fragment = NewsContentFragment.getInstance(mNewsList);
                fragmentTransaction.replace(R.id.fl_content, fragment);
                // fragmentTransaction.addToBackStack(null);
                int commit = fragmentTransaction.commit();
                handler.sendEmptyMessage(MainActivity.REFRESH_COMPLETED);
            }
        });
    }
}