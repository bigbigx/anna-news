package com.zuohaomeng.anna_news.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zuohaomeng.anna_news.NewsDetailPopActivity;
import com.zuohaomeng.anna_news.R;
import com.zuohaomeng.anna_news.bean.News;

import org.xutils.x;

import java.util.List;

import okhttp3.OkHttpClient;

public class NewsFragmentAdapter extends RecyclerView.Adapter<NewsFragmentAdapter.ViewHolder> {

    //新闻对象集合
    private List<News> news;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView tv_title;
        TextView tv_content;
        private ImageView iv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            iv_content = (ImageView)itemView.findViewById(R.id.iv_content);
        }
    }

    public NewsFragmentAdapter(List<News> news){
        this.news = news;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_content_fragment,
                parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        x.view().inject(itemView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(parent.getContext(),holder.getAdapterPosition()+"", Toast.LENGTH_SHORT).show();
                //Toast.makeText(parent.getContext(),holder.tv_content.getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(parent.getContext(), NewsDetailPopActivity.class);
                intent.putExtra("url", holder.tv_content.getText());
                parent.getContext().startActivity(intent);
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_title.setText(news.get(position).getTitle());
        holder.tv_content.setText(news.get(position).getContent());
        //设置网络图片
        String picUrl = news.get(position).getPicUrl();
        Log.i("picUrl::::::",picUrl);
        x.image().bind(holder.iv_content, picUrl);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

}
