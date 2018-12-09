package com.example.week2_01.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class NetUtil {
    private static NetUtil instance;
    private Gson gson;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    public NetUtil() {
        gson = new Gson();
    }

    public static NetUtil getInstance() {
        if (instance == null){
            instance = new NetUtil();
        }
        return instance;
    }
    //执行网络请求返回String
    public String getRequest(String strUrl){
        String result = "";
        try {
            //定义url地址
            URL url = new URL(strUrl);
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            urlConnection.setRequestMethod("GET");
            //设置超时
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            //获取请求码
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200){
                result = stream2String(urlConnection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //将字节流转换为字符流
    private String stream2String(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        for(String tmp = br.readLine();tmp!=null;tmp = br.readLine()){
            builder.append(tmp);
        }
        return builder.toString();
    }
    //执行网络请求返回String
    public <E> E getRequest(String strUrl,Class clazz){
        return (E) gson.fromJson(getRequest(strUrl),clazz);
    }
    //定义接口
    public interface CallBack<E>{
        void onSuccess(E e);
    }
    //handler
    public void getRequest(final String strUrl, final Class clazz, final CallBack callBack){
        new Thread(){
            @Override
            public void run() {
                super.run();
                final Object o = getRequest(strUrl, clazz);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(o);
                    }
                });
            }
        }.start();
    }
}
