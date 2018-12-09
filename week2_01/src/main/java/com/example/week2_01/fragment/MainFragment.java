package com.example.week2_01.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.week2_01.R;
import com.example.week2_01.view.LoginActivity;
import com.example.week2_01.view.RegisterActivity;
import com.example.week2_01.view.SweepActivity;

import java.lang.ref.WeakReference;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class MainFragment extends Fragment {

    private ImageView produce;
    private ImageView sweep;
    private ImageView codeimage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取资源id
        produce = view.findViewById(R.id.produce);
        sweep = view.findViewById(R.id.sweep);
        codeimage = view.findViewById(R.id.code_image);
        //生成二维码
        produce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).setmCallBack(new LoginActivity.CallBack() {
                    @Override
                    public void setChange(String str) {
                        generateQRCode(str);
                    }
                });
            }
        });
        //扫描二维码
        sweep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweepCode();
            }
        });
    }
    //扫描二维码
    private void sweepCode() {
        //判断是否是6.0以上
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //添加相机权限
            if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)!=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},100);
            }else{
                Intent intent = new Intent(getActivity(),SweepActivity.class);
                startActivity(intent);
            }
            //添加网络权限
            if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.INTERNET)!=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.INTERNET},200);
            }else{
                Intent intent = new Intent(getActivity(),SweepActivity.class);
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(getActivity(),SweepActivity.class);
            startActivity(intent);
        }
    }
    //生成二维码
    private void generateQRCode(String str) {
        QRTask qrTask = new QRTask(getActivity(),codeimage);
        qrTask.execute(str);
    }

    //创建静态内部类处理传过来的值
    public static class QRTask extends AsyncTask<String, Void, Bitmap> {
        //软引用类型
        private WeakReference<Context> mContext;
        private WeakReference<ImageView> mImageView;

        public QRTask(Context context,ImageView image) {
            mContext = new WeakReference<>(context);
            mImageView = new WeakReference<>(image);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
                String str = strings[0];
                if(TextUtils.isEmpty(str)){
                    return null;
                }
            int size = mContext.get().getResources().getDimensionPixelOffset(R.dimen.qr_code_size);
           return QRCodeEncoder.syncEncodeQRCode(str,size);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                mImageView.get().setImageBitmap(bitmap);
            }else{
                Toast.makeText(mContext.get(),"生成失败 ",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //动态添加网络权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(getActivity(),SweepActivity.class);
            startActivity(intent);
        }else if(requestCode == 200 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(getActivity(),SweepActivity.class);
            startActivity(intent);
        }
    }
}
