package com.example.week2_01.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week2_01.R;
import com.example.week2_01.bean.NewsBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NewsAdaper extends BaseAdapter {
    private List<NewsBean.DataBean> mData;
    private Context mContext;

    public NewsAdaper(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    public void setmData(List<NewsBean.DataBean> datas){
        mData.clear();
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public void addmData(List<NewsBean.DataBean> datas){
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public NewsBean.DataBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.news_item,parent,false);
            holder = new ViewHolder(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bind(getItem(position));
        return convertView;
    }
    class ViewHolder{
        ImageView image;
        TextView title,date;

        public ViewHolder(View convertView) {
            image = convertView.findViewById(R.id.image);
            title = convertView.findViewById(R.id.title);
            date = convertView.findViewById(R.id.date);
            convertView.setTag(this);
        }

        public void bind(NewsBean.DataBean item) {
            title.setText(item.getTitle());
            date.setText(item.getDate());
            ImageLoader.getInstance().displayImage(item.getThumbnail_pic_s(),image);
        }
    }

}
