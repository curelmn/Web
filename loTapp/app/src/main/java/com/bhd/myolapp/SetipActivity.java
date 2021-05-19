package com.bhd.myolapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class SetipActivity extends AppCompatActivity  implements View.OnClickListener {

    private EditText editTextIp;
    private EditText editTextPort;
    private Button btnSave;
    private Button btnReset;

    private String ip;
    private int port;


    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";


    Calendar calendar= Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏

        setContentView(R.layout.activity_setip);

        this.initView();
        mSharedPreference = getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);
        ip = get("ip");
        String portStr = get("port");
        if (portStr != null) {
            port = Integer.parseInt(portStr);
            editTextPort.setText(portStr);
        } else {
            port = 7016;
        }

        if(ip!=null && !"".equals(ip)){
            editTextIp.setText(ip);
        }
    }

    private void initView() {


        btnSave = findViewById(R.id.btn_save);
        btnReset = findViewById(R.id.btn_reset);

        editTextIp = findViewById(R.id.editText_ip);
        editTextPort = findViewById(R.id.editText_port);


        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        ip = editTextIp.getText().toString();
        String paramPort = editTextPort.getText().toString();

        //验证ip
        String ipReg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern ipPattern = Pattern.compile(ipReg);
        boolean flag = ipPattern.matcher(ip).matches();
        if(!flag){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SetipActivity.this, "请输入合法的ip地址", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        //验证端口
        port = Integer.parseInt(paramPort);
        if(port > 65535 || port<1024){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SetipActivity.this, "请输入1024-65535之间的合法端口", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        switch (v.getId()) {
            case R.id.btn_save:

                save("ip",ip);
                save("port",String.valueOf(port));

                Intent intent = new Intent();
                intent.setClass(SetipActivity.this,MainActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_reset:
                editTextIp.setText("");
                editTextPort.setText("");
                break;
        }
    }

    private void save(String key, String data){
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(key, data);
        editor.apply(); // 或者 editor.apply();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetipActivity.this, "ip和端口已经更新", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String get(String key) {
        return mSharedPreference.getString(key, null);
    }


}
