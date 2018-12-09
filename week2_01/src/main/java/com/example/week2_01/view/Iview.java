package com.example.week2_01.view;

public interface Iview<T> {
    void success(T t);
    void error(String str);
}
