package com.bhd.myolapp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * description: MainActivity 主页面类<br>
 * date: 2021/4/10 1:08 <br>
 * author: DaTao <br>
 * version: 1.0 <br>
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView dpxs;
    private ImageView setipImg;
    private Button lightBtn;
    private Button applianceBtn;
    private Button anfangBtn;
    private  Button windBtn;

    private String ip;
    private Integer port;

    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";

    private Fragment cacheFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏


        setContentView(R.layout.activity_main);
        mSharedPreference = getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);

        ip = get("ip");
        String portStr = get("port");
        if(portStr!=null){
            port = Integer.parseInt(portStr);
        }else{
            port = 7016;
        }

        initView();
    }

    private void initView(){
        dpxs = findViewById(R.id.dpxs);
        lightBtn = findViewById(R.id.light_btn);//后续修改点
        applianceBtn = findViewById(R.id.appliance_btn);//后续修改点
        anfangBtn = findViewById(R.id.anfang_btn);//后续修改点
        windBtn = findViewById(R.id.wind_btn);
        setipImg = findViewById(R.id.setip_img);

        lightBtn.setOnClickListener(this);
        applianceBtn.setOnClickListener(this);
        anfangBtn.setOnClickListener(this);
        windBtn.setOnClickListener(this);

        setipImg.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        Fragment f = null;
        switch (v.getId()) {
            case R.id.light_btn://后续修改点
                Intent intent_l = new Intent();
                intent_l.setClass(MainActivity.this, LightActivity.class);
                startActivity(intent_l);
                break;
            case R.id.appliance_btn://后续修改点
                //替换Fragment
                Intent intent_c = new Intent();
                intent_c.setClass(MainActivity.this, AppliancesActivity.class);
                startActivity(intent_c);
                break;
            case R.id.anfang_btn://后续修改点
                //替换Fragment
                Intent intent_a = new Intent();
                intent_a.setClass(MainActivity.this,AnfangActivity.class);
                startActivity(intent_a);
                break;
            case R.id.wind_btn:
                Intent intent_w = new Intent();
                intent_w.setClass(MainActivity.this,WindActivity.class);
                startActivity(intent_w);
                break;
            case R.id.setip_img:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SetipActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private String get(String key){
        return mSharedPreference.getString(key, null);
    }



}