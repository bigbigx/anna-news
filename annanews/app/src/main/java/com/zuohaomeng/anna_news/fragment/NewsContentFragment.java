package com.zuohaomeng.anna_news.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zuohaomeng.anna_news.bean.News;
import com.zuohaomeng.anna_news.R;

import java.util.List;

public class NewsContentFragment extends Fragment {

    RecyclerView rv_content;

    private static List<News> mNewsList;

    public static NewsContentFragment getInstance(List<News> mNewsList){
        NewsContentFragment.mNewsList = mNewsList;
        NewsContentFragment fragment = new NewsContentFragment();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_content_fragment, container, false);
        rv_content = view.findViewById(R.id.rv_content);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_content.setLayoutManager(linearLayoutManager);

        rv_content.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, 1);
            }
        });

        NewsFragmentAdapter adapter = new NewsFragmentAdapter(mNewsList);
        rv_content.setBackgroundColor(Color.DKGRAY);
        rv_content.setAdapter(adapter);
        return view;
    }

}
