package com.example.week2_01.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.week2_01.R;
import com.example.week2_01.adaper.NewsAdaper;
import com.example.week2_01.bean.BannerBean;
import com.example.week2_01.bean.NewsBean;
import com.example.week2_01.presenter.PresenterImpl;
import com.example.week2_01.view.Iview;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoaderInterface;

public class HomeFragment extends Fragment implements Iview {

    private Banner banner;
    private PullToRefreshListView listView;
    private int mPage;
    private String url = "http://www.xieast.com/api/news/news.php?page=%d";
    private String bannerUrl = "http://www.zhaoapi.cn/ad/getAd?token=05329B0DCBE400ED03760D7B27627FC7";
    private PresenterImpl presenter;
    private NewsAdaper adaper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPage = 1;
        presenter = new PresenterImpl(this);
        //获取资源id
        banner = view.findViewById(R.id.banner_image);
        listView = view.findViewById(R.id.pull);
        //创建适配器
        adaper = new NewsAdaper(getActivity());
        listView.setAdapter(adaper);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage=1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
        initData();
    }

    private void initData() {
        presenter.startResult(String.format(url,mPage),null,NewsBean.class);
        presenter.startResult(bannerUrl,null,BannerBean.class);
    }

    @Override
    public void success(Object o) {
        if(o instanceof NewsBean){
            NewsBean bean = (NewsBean) o;
            Toast.makeText(getActivity(),bean.getMsg(),Toast.LENGTH_SHORT).show();
            if(mPage == 1){
                adaper.setmData(bean.getData());
            }else{
                adaper.addmData(bean.getData());
            }
            mPage++;
            listView.onRefreshComplete();
        }else if(o instanceof BannerBean){
            BannerBean bannerBean = (BannerBean) o;
            //设置样式
            banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new ImageLoaderInterface<ImageView>() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    BannerBean.DataBean dataBean = (BannerBean.DataBean) path;
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                            dataBean.setImage(),imageView
                    );
                }
                @Override
                public ImageView createImageView(Context context) {
                    ImageView imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return imageView;
                }
            });
            //设置图片集合
            banner.setImages(bannerBean.getData());
            banner.start();
        }
    }

    @Override
    public void error(String str) {
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
