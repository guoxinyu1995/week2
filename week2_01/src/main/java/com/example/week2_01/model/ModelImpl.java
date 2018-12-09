package com.example.week2_01.model;

import com.example.week2_01.bean.PhoneBean;
import com.example.week2_01.util.NetUtil;

public class ModelImpl implements Imodel {
    @Override
    public void RequestData(String url, String pames, Class clazz, final MyCallBack myCallBack) {
        NetUtil.getInstance().getRequest(url,clazz, new NetUtil.CallBack() {
            @Override
            public void onSuccess(Object o) {
                myCallBack.setData(o);
            }
        });
    }
}
