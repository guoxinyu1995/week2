package com.example.week2_01.util;

import android.text.TextUtils;

public class NonNull {
    private static NonNull instance;

    public NonNull() {
    }

    public static NonNull getInstance() {
        if (instance == null){
            instance = new NonNull();
        }
        return instance;
    }
    //非空判断
    public boolean isNull(String name,String password){
        return !TextUtils.isEmpty(name)&&!TextUtils.isEmpty(password);
    }
    //正则验证手机号
    public boolean isMobileNO(String name){
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        return !name.matches(telRegex);
    }
}
