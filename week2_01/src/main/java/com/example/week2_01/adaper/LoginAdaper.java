package com.example.week2_01.adaper;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.week2_01.fragment.HomeFragment;
import com.example.week2_01.fragment.MainFragment;

public class LoginAdaper extends FragmentPagerAdapter {
    String[] str = new String[]{
            "首页","我的"
    };
    public LoginAdaper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new HomeFragment();
            case 1:
                return new MainFragment();
                default:
                    return new Fragment();
        }

    }

    @Override
    public int getCount() {
        return str.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return str[position];
    }
}
