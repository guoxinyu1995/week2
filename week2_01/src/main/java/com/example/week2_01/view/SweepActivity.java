package com.example.week2_01.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.week2_01.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class SweepActivity extends AppCompatActivity implements QRCodeView.Delegate {

    private ZXingView xingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep);
        //获取资源id
        xingView = findViewById(R.id.zxing);
        xingView.setDelegate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //打开后置摄像头
        xingView.startCamera();
        //延迟0.1s
        xingView.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭摄像头
        xingView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁
        xingView.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Intent intent = new Intent(SweepActivity.this,WebViewActivity.class);
        intent.putExtra("path",result);
        startActivity(intent);
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.i("gxy","打开相机出错");
    }
}
