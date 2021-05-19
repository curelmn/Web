package com.bhd.myolapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * description: MainActivity 家电页面<br>
 * date: 2021/4/10 1:08 <br>
 * author: DaTao <br>
 * version: 1.0 <br>
 */

public class AppliancesActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView dpxs;
    private ImageView setipImg;
    private Button tvBtn;
    private Button airBtn;
    private Button washerBtn;
    private Button heaterBtn;

    private Button allOpenBtn;
    private Button allCloseBtn;

    private String ip;
    private Integer port;

    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";

    private Fragment cacheFragment;

    Fragment fg1 = new TVFragment();
    Fragment fg2 = new AirFragment();
    Fragment fg3 = new WasherFragment();
    Fragment fg4 = new HeaterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏


        setContentView(R.layout.activity_appliances);
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
        tvBtn = findViewById(R.id.tv_btn);
        airBtn = findViewById(R.id.air_btn);
        washerBtn = findViewById(R.id.washer_btn);
        heaterBtn = findViewById(R.id.heater_btn);
        setipImg = findViewById(R.id.setip_img);
        allOpenBtn = findViewById(R.id.all_open_btn);
        allCloseBtn = findViewById(R.id.all_close_btn);

        tvBtn.setOnClickListener(this);
        airBtn.setOnClickListener(this);
        washerBtn.setOnClickListener(this);
        heaterBtn.setOnClickListener(this);

        setipImg.setOnClickListener(this);

        allOpenBtn.setOnClickListener(this);
        allCloseBtn.setOnClickListener(this);

        SwithFragment(fg1,this, R.id.fragment).commit();
    }


    @Override
    public void onClick(View v) {

        Fragment f = null;
        switch (v.getId()) {
            case R.id.tv_btn:
                 setEnable(tvBtn);
                //替换Fragment
                SwithFragment(fg1, AppliancesActivity.this, R.id.fragment).commit();
                break;
            case R.id.air_btn:
                //替换Fragment
                setEnable(airBtn);
                SwithFragment(fg2, AppliancesActivity.this, R.id.fragment).commit();
                break;
            case R.id.washer_btn:
                //替换Fragment
                setEnable(washerBtn);
                SwithFragment(fg3, AppliancesActivity.this, R.id.fragment).commit();
                break;
            case R.id.heater_btn:
                //替换Fragment
                setEnable(heaterBtn);
                SwithFragment(fg4, AppliancesActivity.this, R.id.fragment).commit();
                break;
            case R.id.setip_img:
                Intent intent = new Intent();
                intent.setClass(AppliancesActivity.this, SetipActivity.class);
                startActivity(intent);
                break;

            case R.id.all_open_btn:
                sendMsg(ip, port, "tv_open");
                sendMsg(ip, port, "air_open");
                sendMsg(ip, port, "washer_open");
                sendMsg(ip, port, "cold_mood");
                break;
            case R.id.all_close_btn:
                sendMsg(ip, port, "tv_close");
                sendMsg(ip, port, "air_close");
                sendMsg(ip, port, "washer_close");
                sendMsg(ip, port, "shower_mood");
                break;
            default:
                break;
        }
    }


    private String get(String key){
        return mSharedPreference.getString(key, null);
    }


    /**
     * 发送消息
     *
     * @param ip
     * @param port
     * @param msg
     */
    private void sendMsg(final String ip, final int port, final String msg) {

        new Thread() {
            @Override
            public void run() {

                Socket socket = null;

                try {
                    SocketAddress saAdd = new InetSocketAddress(ip.trim(), port);
                    socket = new Socket();
                    socket.connect(saAdd, 1000);


                    final OutputStream outputStream = socket.getOutputStream();
                    // 将String转换成byte[]传输数据，使用UTF-8编码，服务端也使用UTF-8转换，支持中文
                    outputStream.write(msg.getBytes(StandardCharsets.UTF_8));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (msg.endsWith("open")) {
                                dpxs.setImageResource(R.drawable.dp_light);
                                Toast.makeText(AppliancesActivity.this, "家电已经全部打开", Toast.LENGTH_SHORT).show();
                            } else {
                                dpxs.setImageResource(R.drawable.dp_dark);
                                Toast.makeText(AppliancesActivity.this, "家电已经全部关闭", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    outputStream.close();
                } catch (UnknownHostException e) {
                    sendStatus("未知IP");
                } catch (SocketTimeoutException e) {
                    sendStatus("连接超时");
                } catch (IOException e) {
                    sendStatus("发送失败");
                    //e.printStackTrace();
                } catch (Exception e) {
                    sendStatus("发送失败");
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }

    private void sendStatus(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AppliancesActivity.this, status, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public FragmentTransaction SwithFragment(Fragment fragment, FragmentActivity fma, int idResources){
        FragmentManager fm= fma.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (!fragment.isAdded()){//还没添加 Fragment
            if (cacheFragment !=null){
                transaction.hide(cacheFragment);
            }
            transaction.add(idResources,fragment,fragment.getClass().getName());
        }else{//已经添加过了 Fragment
            transaction.hide(cacheFragment).show(fragment);
        }
        cacheFragment =fragment;
        return transaction;
    }


    @Override
    public void onResume() {
        super.onResume();
        mSharedPreference = this.getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);
        ip = get("ip");
        String portStr = get("port");
        if(portStr!=null){
            port = Integer.parseInt(portStr);
        }else{
            port = 7016;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreference = this.getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);
        ip = get("ip");
        String portStr = get("port");
        if(portStr!=null){
            port = Integer.parseInt(portStr);
        }else{
            port = 7016;
        }
    }


    private void setEnable(Button btn){
        List<Button> buttonList=new ArrayList<>();
        if (buttonList.size()==0){
            buttonList.add(tvBtn);
            buttonList.add(airBtn);
            buttonList.add(washerBtn);
            buttonList.add(heaterBtn);
        }
        for (int i=0;i<buttonList.size();i++){
            buttonList.get(i).setEnabled(true);
        }
        btn.setEnabled(false);

    }

}
