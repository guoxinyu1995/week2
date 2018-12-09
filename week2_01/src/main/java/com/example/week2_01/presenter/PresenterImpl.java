package com.example.week2_01.presenter;

import com.example.week2_01.bean.BannerBean;
import com.example.week2_01.bean.NewsBean;
import com.example.week2_01.bean.PhoneBean;
import com.example.week2_01.model.ModelImpl;
import com.example.week2_01.model.MyCallBack;
import com.example.week2_01.view.Iview;
import com.example.week2_01.view.MainActivity;

public class PresenterImpl implements Ipresenter {
    private Iview mIview;
    private ModelImpl model;

    public PresenterImpl(Iview iview) {
        mIview = iview;
        model = new ModelImpl();
    }

    @Override
    public void startResult(String url, String preams, Class clazz) {
        model.RequestData(url, null, clazz, new MyCallBack() {
            @Override
            public void setData(Object data) {

                mIview.success(data);
//                if(data instanceof PhoneBean){
//                    PhoneBean bean = (PhoneBean) data;
//                    if(bean==null || !bean.isSuccess()){
//                        mIview.error(bean.getMsg());
//                    }else{
//                        mIview.success(bean);
//                    }
//                }else if(data instanceof NewsBean){
//                    NewsBean newsBean = (NewsBean) data;
//                    if(newsBean==null || !newsBean.isSuccess()){
//                        mIview.error(newsBean.getMsg());
//                    }else{
//                        mIview.success(newsBean);
//                    }
//                }else if(data instanceof BannerBean){
//                    BannerBean bannerBean = (BannerBean) data;
//                    if(bannerBean==null || !bannerBean.isSuccess()){
//                        mIview.error(bannerBean.getMsg());
//                    }else{
//                        mIview.success(bannerBean);
//                    }
//                }
            }
        });
    }
    public void onDetach(){
        if(model!=null){
            model = null;
        }
        if(mIview!=null){
            mIview = null;
        }
    }
}
