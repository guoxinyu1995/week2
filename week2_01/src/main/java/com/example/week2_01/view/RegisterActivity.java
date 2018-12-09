package com.example.week2_01.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week2_01.R;
import com.example.week2_01.bean.PhoneBean;
import com.example.week2_01.presenter.PresenterImpl;
import com.example.week2_01.util.NonNull;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity implements Iview {

    private PresenterImpl presenter;
    private EditText name;
    private EditText password;
    private Button register;
    private String url = "http://120.27.23.105/user/reg?mobile=%s&password=%s";
    private EditText num;
    private TextView textView;
    private int n = 5;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (n > 0) {
                n--;
                textView.setText(n + "s");
                handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                n = 10;
                textView.setText("获取验证码");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        presenter = new PresenterImpl(this);
        initView();
    }

    private void initView() {
        //获取资源id
        name = findViewById(R.id.ed_name);
        password = findViewById(R.id.ed_pass);
        num = findViewById(R.id.ed_num);
        textView = findViewById(R.id.text);
        register = findViewById(R.id.register);
        //当密码长度为大于6位是注册按钮可点击
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                register.setEnabled(s.length() >= 6);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //当手机号长度为11位时获取验证码可点击
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    textView.setEnabled(true);
                } else {
                    textView.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框得值
                String names = name.getText().toString();
                String passwords = password.getText().toString();
                if (NonNull.getInstance().isNull(names, passwords)) {
                    presenter.startResult(String.format(url, names, passwords), null, PhoneBean.class);
                } else {
                    Toast.makeText(RegisterActivity.this, "手机号和密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //随机数点击
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        });
    }

    private void initData() {
        for (int i = 0; i <= 100; i++) {
            int next = new Random().nextInt(999999);
            if (next < 100000) {
                next += 100000;
            }
            num.setText(next + "");
        }
    }

    @Override
    public void success(Object o) {
        if (o instanceof PhoneBean) {
            PhoneBean bean = (PhoneBean) o;
            Toast.makeText(RegisterActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void error(String str) {
        Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
