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
 * description: MainActivity 主页面类<br>
 * date: 2021/4/10 1:08 <br>
 * author: DaTao <br>
 * version: 1.0 <br>
 */
public class LightActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView dpxs;
    private ImageView setipImg;
    private Button livingBtn;
    private Button bedroomBtn;
    private Button bathroomBtn;
    private Button kitchenBtn;
    private Button balconyBtn;

    private Button allOpenBtn;
    private Button allCloseBtn;

    private String ip;
    private Integer port;

    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";

    private Fragment cacheFragment;

    Fragment fg1 = new LivingRoomFragment();
    Fragment fg2 = new BedroomFragment();
    Fragment fg3 = new BathroomFragment();
    Fragment fg4 = new KitchenFragment();
    Fragment fg5 = new BalconyFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏


        setContentView(R.layout.activity_light);
        mSharedPreference = getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);

        ip = get("ip");
        String portStr = get("port");
        if (portStr != null) {
            port = Integer.parseInt(portStr);
        } else {
            port = 7016;
        }

        initView();
    }

    private void initView() {
        dpxs = findViewById(R.id.dpxs);
        livingBtn = findViewById(R.id.living_btn);
        bedroomBtn = findViewById(R.id.bedroom_btn);
        bathroomBtn = findViewById(R.id.bathroom_btn);
        kitchenBtn = findViewById(R.id.kitchen_btn);
        balconyBtn = findViewById(R.id.balcony_btn);
        setipImg = findViewById(R.id.setip_img);
        allOpenBtn = findViewById(R.id.all_open_btn);
        allCloseBtn = findViewById(R.id.all_close_btn);

        livingBtn.setOnClickListener(this);
        bedroomBtn.setOnClickListener(this);
        bathroomBtn.setOnClickListener(this);
        kitchenBtn.setOnClickListener(this);
        balconyBtn.setOnClickListener(this);

        setipImg.setOnClickListener(this);

        allOpenBtn.setOnClickListener(this);
        allCloseBtn.setOnClickListener(this);

        SwithFragment(fg1, this, R.id.fragment).commit();
    }


    @Override
    public void onClick(View v) {

        Fragment f = null;
        switch (v.getId()) {
            case R.id.living_btn:
                setEnable(livingBtn);

                //替换Fragment
                SwithFragment(fg1, LightActivity.this, R.id.fragment).commit();
                break;
            case R.id.bedroom_btn:
                setEnable(bedroomBtn);
                //替换Fragment
                SwithFragment(fg2, LightActivity.this, R.id.fragment).commit();
                break;
            case R.id.bathroom_btn:
                setEnable(bathroomBtn);
                //替换Fragment
                SwithFragment(fg3, LightActivity.this, R.id.fragment).commit();
                break;
            case R.id.kitchen_btn:
                setEnable(kitchenBtn);
                //替换Fragment
                SwithFragment(fg4, LightActivity.this, R.id.fragment).commit();
                break;
            case R.id.balcony_btn:
                setEnable(balconyBtn);
                //替换Fragment
                SwithFragment(fg5, LightActivity.this, R.id.fragment).commit();
                break;
            case R.id.setip_img:
                Intent intent = new Intent();
                intent.setClass(LightActivity.this, SetipActivity.class);
                startActivity(intent);
                break;

            case R.id.all_open_btn:
                sendMsg(ip, port, "living_open");
                sendMsg(ip, port, "bedroom_open");
                sendMsg(ip, port, "bathroom_open");
                sendMsg(ip, port, "kitchen_open");
                sendMsg(ip, port, "balcony_open");
                break;
            case R.id.all_close_btn:
                sendMsg(ip, port, "living_close");
                sendMsg(ip, port, "bedroom_close");
                sendMsg(ip, port, "bathroom_close");
                sendMsg(ip, port, "kitchen_close");
                sendMsg(ip, port, "balcony_close");
                break;
            default:
                break;
        }
    }


    private String get(String key) {
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
                                Toast.makeText(LightActivity.this, "灯已经打开", Toast.LENGTH_SHORT).show();
                            } else {
                                dpxs.setImageResource(R.drawable.dp_dark);
                                Toast.makeText(LightActivity.this, "灯已经关闭", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LightActivity.this, status, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public FragmentTransaction SwithFragment(Fragment fragment, FragmentActivity fma, int idResources) {
        FragmentManager fm = fma.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (!fragment.isAdded()) {//还没添加 Fragment
            if (cacheFragment != null) {
                transaction.hide(cacheFragment);
            }
            transaction.add(idResources, fragment, fragment.getClass().getName());
        } else {//已经添加过了 Fragment
            transaction.hide(cacheFragment).show(fragment);
        }
        cacheFragment = fragment;
        return transaction;
    }


    @Override
    public void onResume() {
        super.onResume();
        mSharedPreference = this.getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);
        ip = get("ip");
        String portStr = get("port");
        if (portStr != null) {
            port = Integer.parseInt(portStr);
        } else {
            port = 7016;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreference = this.getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);
        ip = get("ip");
        String portStr = get("port");
        if (portStr != null) {
            port = Integer.parseInt(portStr);
        } else {
            port = 7016;
        }
    }
    private void setEnable(Button btn){
        List<Button> buttonList=new ArrayList<>();
        if (buttonList.size()==0){
            buttonList.add(livingBtn);
            buttonList.add(bedroomBtn);
            buttonList.add(bathroomBtn);
            buttonList.add(kitchenBtn);
            buttonList.add(balconyBtn);
        }
        for (int i=0;i<buttonList.size();i++){
            buttonList.get(i).setEnabled(true);
        }
        btn.setEnabled(false);

    }

}