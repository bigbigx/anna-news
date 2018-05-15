package com.zuohaomeng.anna_news;

import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zuohaomeng.anna_news.Util.Constant;
import com.zuohaomeng.anna_news.bean.News;
import com.zuohaomeng.anna_news.bean.Site;
import com.zuohaomeng.anna_news.fragment.NewsContentFragment;
import com.zuohaomeng.anna_news.fragment.SiteAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public final static int CLICK_SITE = 0x001;
    public final static int PULL_REFRESH = 0x002;
    public final static int REFRESH_COMPLETED = 0x003;
    private static final int NETWORK_FAILURE = 0X004;

    private static boolean isExit = false;


    DrawerLayout dl_parent;

    View title;
    TextView tv_title;
    ImageView iv_title;
    SwipeRefreshLayout srl_content;

   // RecyclerView rv_bottom_navigation;
    RecyclerView rv_site_names;

    List<Site> mSitesList;

    FloatingActionButton fab_shared;

    OkHttpClient client = new OkHttpClient();

    Site site;
    String url;
    String news_title;

    @SuppressLint("HandlerLeak")
    public
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
            Log.i("handler1", handler+"");
            Bundle bundle = msg.getData();
            switch (msg.what){
                case CLICK_SITE:
                    site = (Site) bundle.get("Site");
                    url = site.getUrl();
                    // Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                    flashContent(site);
                    break;
                case PULL_REFRESH:
                    try {
                        refrushNews(site);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case REFRESH_COMPLETED:
                    srl_content.setRefreshing(false);
                    break;
                case NETWORK_FAILURE:
                    Toast.makeText(getApplicationContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    srl_content.setRefreshing(false);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        site = new Site();
        site.setUrl("http://news.163.com/");
        site.setTitle("网易");
        site.setPic(R.drawable.news_logo_163);
        //site.setName("騰訊");
        findviewById();
        setOnClickListener();
        setAnimation();

        initSitesData();
        initSlideView();
        initContent();

        flashContent(site);
    }

    private void findviewById() {
        title = findViewById(R.id.title);
        tv_title = (TextView)title.findViewById(R.id.tv_title);
        iv_title = (ImageView)title.findViewById(R.id.iv_title);
        fab_shared=(FloatingActionButton)findViewById(R.id.fab_shared);
        dl_parent = (DrawerLayout)findViewById(R.id.dl_parent);
        srl_content = (SwipeRefreshLayout)findViewById(R.id.srl_content);
    }

    private void setOnClickListener(){
        fab_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dl_parent.isDrawerOpen(rv_site_names)){
                    dl_parent.closeDrawers();
                } else {
                    dl_parent.openDrawer(GravityCompat.START);
                }
            }
        });
        srl_content.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessage(MainActivity.PULL_REFRESH);
            }
        });
    }

    private void setAnimation(){
//        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.floating_rotate);
//        Animation translate = AnimationUtils.loadAnimation(this, R.anim.floating_translate);
        //fab_shared.setAnimation(rotate);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.floating_rotate_and_translate);
        fab_shared.setAnimation(animation);
    }

    //初始化数据
    private void initContent() {
        tv_title.setText("随机获取的新闻");
    }

    private void initSitesData() {
        mSitesList = new ArrayList<Site>();
        Site tencent = new Site();
        tencent.setPic(R.drawable.news_logo_tencent);
        tencent.setUrl(Constant.baseUrl + Constant.TECENT_NEWS_URL_INTERFACE);
        tencent.setTitle("腾讯");

        Site netease = new Site();
        netease.setPic(R.drawable.news_logo_163);
        netease.setUrl(Constant.baseUrl + Constant.NETEASE_NEWS_URL_INTERFACE);
        netease.setTitle("网易");

        Site sohu = new Site();
        sohu.setPic(R.drawable.news_logo_sohu);
        sohu.setUrl(Constant.baseUrl + Constant.SOHU_NEWS_URL_INTERFACE);
        sohu.setTitle("搜狐");

        mSitesList.add(tencent);
        mSitesList.add(netease);
        mSitesList.add(sohu);
    }

    private void initSlideView() {

        rv_site_names = (RecyclerView)findViewById(R.id.rv_site_names);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       // linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_site_names.setLayoutManager(linearLayoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv_site_names);
        SiteAdapter siteAdapter = new SiteAdapter(mSitesList);
        rv_site_names.setAdapter(siteAdapter);

    }
    private void flashContent(Site site) {
        //修改标题
        tv_title.setText(site.getTitle() + "新闻");
        iv_title.setImageResource(site.getPic());

        dl_parent.closeDrawers();
        String url = site.getUrl();
        //获取新闻
        try {
            refrushNews(site);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void refrushNews(Site site) throws IOException {
        //okhttp异步请求
        String url = site.getUrl();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.i("failure", e.getMessage());
                Message msg = handler.obtainMessage();
                msg.what = NETWORK_FAILURE;
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<News> mNewsList = new ArrayList<>();
                String result_json = response.body().string();
                Gson gson = new Gson();
                try {
                    ArrayList listNews = gson.fromJson(result_json, ArrayList.class);
                    int newsCount = listNews.size();
                    for (int i = 0; i < newsCount; i++) {
                        mNewsList.add(gson.fromJson(gson.toJson(listNews.get(i)), News.class));
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    for (int i = 0; i < 1000; i++) {
                        News news1 = new News();
                        news1.setTitle(site.getTitle()+"标题"+i);
                        news1.setPicUrl("");
                        news1.setContent("内容"+i);
                        mNewsList.add(news1);
                    }
                }


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               // NewsContentFragment fragment = new NewsContentFragment();
                //TODO 将返回结果转换为List<News>
//
//                for (int i = 0; i < 1000; i++) {
//                    News news1 = new News();
//                    news1.setTitle(site.getTitle()+"标题"+i);
//                    news1.setPicUrl("");
//                    news1.setContent("内容"+i);
//                    mNewsList.add(news1);
//                }
                NewsContentFragment fragment = NewsContentFragment.getInstance(mNewsList);
                fragmentTransaction.replace(R.id.fl_content, fragment);
               // fragmentTransaction.addToBackStack(null);
                int commit = fragmentTransaction.commit();
                handler.sendEmptyMessage(MainActivity.REFRESH_COMPLETED);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
