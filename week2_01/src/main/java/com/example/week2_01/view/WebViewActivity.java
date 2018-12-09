package com.example.week2_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.week2_01.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //获取资源id
        webView = findViewById(R.id.web);
        //接收值
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        //支持js语言
        webView.getSettings().setJavaScriptEnabled(true);
        //展示
        webView.loadDataWithBaseURL(null,path,"text/html","utf-8",null);
    }
}
