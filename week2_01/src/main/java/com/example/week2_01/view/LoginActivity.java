package com.example.week2_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.week2_01.R;
import com.example.week2_01.adaper.LoginAdaper;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LoginAdaper adaper;
    private CallBack mCallBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        //获取资源ID
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.v_pager);
        //创建适配器
        adaper = new LoginAdaper(getSupportFragmentManager());
        viewPager.setAdapter(adaper);
        tabLayout.setupWithViewPager(viewPager);
    }
    //定义接口
    public interface CallBack{
        void setChange(String str);
    }
    //定义方法
    public void setmCallBack(CallBack callBack){
        mCallBack = callBack;
        Intent intent = getIntent();
        String screen_name = intent.getStringExtra("screen_name");
        if(mCallBack!=null){
            mCallBack.setChange(screen_name);
        }
    }
}
