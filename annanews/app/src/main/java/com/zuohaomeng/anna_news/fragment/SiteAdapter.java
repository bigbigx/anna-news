package com.zuohaomeng.anna_news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuohaomeng.anna_news.MainActivity;
import com.zuohaomeng.anna_news.R;
import com.zuohaomeng.anna_news.bean.Site;

import java.util.List;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder> {

    private List<Site> mSitesList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View siteView;
        TextView tv_site_name;
        ImageView iv_site_ico;

        public ViewHolder(View itemView) {
            super(itemView);
            siteView = itemView;
            tv_site_name = (TextView)itemView.findViewById(R.id.tv_site_name);
            iv_site_ico = (ImageView)itemView.findViewById(R.id.iv_site_ico);
        }
    }

    public SiteAdapter(List<Site> mSitesList){
        this.mSitesList = mSitesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sites_item, parent,
                false);

        //添加点击事件
        ViewHolder holder = new ViewHolder(itemView);
        holder.siteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MainActivity mainActivity = (MainActivity) parent.getContext();
                Handler handler = mainActivity.handler;
                handlerMsg(handler,position);
            }
        });


        return holder;
    }

    public void handlerMsg(Handler handler, int position){
        Handler h = handler;
        int p = position;
        Log.i("handler1 p:", p+"");
        new Thread(){
            @Override
            public void run() {
                super.run();
                Message message = h.obtainMessage();
                Bundle bundle = new Bundle();
                //bundle.putString("title",mSitesList.get(p).getTitle());
                bundle.putSerializable("Site", mSitesList.get(p));
                message.setData(bundle);
                message.what = 0x001;
                h.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Site site = mSitesList.get(position);
        holder.iv_site_ico.setImageResource(site.getPic());
        holder.tv_site_name.setText(site.getName());
        Log.i("size", site.getPic()+"");
    }

    @Override
    public int getItemCount() {
        Log.i("size", mSitesList.size()+"");
        return mSitesList.size();
    }
}
