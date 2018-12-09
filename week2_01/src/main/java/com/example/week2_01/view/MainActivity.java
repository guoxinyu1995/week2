package com.example.week2_01.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.week2_01.R;
import com.example.week2_01.bean.PhoneBean;
import com.example.week2_01.presenter.PresenterImpl;
import com.example.week2_01.util.NonNull;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Iview, View.OnClickListener {

    private PresenterImpl presenter;
    private EditText name;
    private EditText password;
    private CheckBox remember;
    private CheckBox auto;
    private Button login;
    private Button register;
    private String url = "http://120.27.23.105/user/login?mobile=%s&password=%s";
    private ImageView qq;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new PresenterImpl(this);
        preferences = getSharedPreferences("User", MODE_PRIVATE);
        edit = preferences.edit();
        //出始化view
        initView();
    }

    //出始化view
    private void initView() {
        //获取资源id
        //用户名
        name = findViewById(R.id.ed_name);
        //密码
        password = findViewById(R.id.ed_pass);
        //记住密码
        remember = findViewById(R.id.remember);
        //QQ登录
        qq = findViewById(R.id.qq);
        //自动登录
        auto = findViewById(R.id.auto);
        //登录
        login = findViewById(R.id.login);
        //注册
        register = findViewById(R.id.register);
        //登录按钮
        login.setOnClickListener(this);
        //注册按钮
        register.setOnClickListener(this);
        //qq登录按钮
        qq.setOnClickListener(this);
        //当登录按钮为6位数是登录按钮可点击
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                login.setEnabled(s.length() >= 6);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //将记住密码的状态取出
        boolean r_check = preferences.getBoolean("r_check", false);
        if (r_check) {
            //取值
            String names = preferences.getString("names", null);
            String passwords = preferences.getString("passwords", null);
            //设置值
            name.setText(names);
            password.setText(passwords);
            remember.setChecked(true);
        }
        //将自动登录的状态值取出
        boolean a_check = preferences.getBoolean("a_check", false);
        if (a_check) {
            String names = name.getText().toString().trim();
            String passwords = password.getText().toString().trim();
            presenter.startResult(String.format(url, names, passwords), null, PhoneBean.class);
            auto.setChecked(true);
        }
        //自动登录勾选时记住密码勾选
        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    remember.setChecked(true);
                }else{
                    remember.setChecked(false);
                }
            }
        });
        //动态注册权限
        stateNetWork();
    }
    //动态注册权限
    private void stateNetWork() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            String[] mStatenetwork = new String[]{
                    //写的权限
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    //读的权限
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    //入网权限
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    //WIFI权限
                    Manifest.permission.ACCESS_WIFI_STATE,
                    //读手机权限
                    Manifest.permission.READ_PHONE_STATE,
                    //网络权限
                    Manifest.permission.INTERNET,
                    };
            ActivityCompat.requestPermissions(this,mStatenetwork,100);
        }
    }
    //动态注册的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission  = false;
        if(requestCode == 100){
            for (int i = 0;i<grantResults.length;i++){
                if(grantResults[i] == -1){
                    hasPermission = true;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 登录
             * */
            case R.id.login:
                //获取输入框得值
                String names = name.getText().toString().trim();
                String passwords = password.getText().toString().trim();
                //判断记住密码是否勾选
                if (remember.isChecked()) {
                    //存值
                    edit.putString("names", names);
                    edit.putString("passwords", passwords);
                    //存入一个勾选转态
                    edit.putBoolean("r_check", true);
                    //提交
                    edit.commit();
                } else {
                    //清空
                    edit.clear();
                    //提交
                    edit.commit();
                }
                //非空
                if (NonNull.getInstance().isNull(names, passwords)) {
                    presenter.startResult(String.format(url, names, passwords), null, PhoneBean.class);
                } else {
                    Toast.makeText(MainActivity.this, "手机号和密码不能为空", Toast.LENGTH_SHORT).show();
                }
                //正则
                if(NonNull.getInstance().isMobileNO(names)){
                    Toast.makeText(MainActivity.this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                }else{
                    presenter.startResult(String.format(url, names, passwords), null, PhoneBean.class);
                }
                //自动登录
                if (auto.isChecked()) {
                    //存入一个勾选转态
                    edit.putBoolean("a_check", true);
                    //提交
                    edit.commit();
                }
                break;
            case R.id.register:
                //注册
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.qq:
                //qq登录
                UMShareAPI umShareAPI = UMShareAPI.get(MainActivity.this);
                umShareAPI.getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        //Log.i("gxy","UMAuthListener onComplete");
                        //18 = {HashMap$HashMapEntry@831427910936} "screen_name" -> "丢丢"
                        //19 = {HashMap$HashMapEntry@831427838112} "profile_image_url" -> "http://thirdqq.qlogo.cn/qqapp/100424468/974986458467CB74970B57B32EFC321F/100"
                        String screen_name = map.get("screen_name");
                        //String profile_image_url = map.get("profile_image_url");
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        intent1.putExtra("screen_name",screen_name);
                        //intent1.putExtra("profile_image_url",profile_image_url);
                        startActivity(intent1);
                        finish();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void success(Object o) {
        PhoneBean bean = (PhoneBean) o;
        Toast.makeText(MainActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void error(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }
    //qq登录的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
