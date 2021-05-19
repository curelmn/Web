package com.bhd.myolapp;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * description: BedroomFragment 卧室<br>
 * date: 2021/4/10 1:08 <br>
 * author: DaTao <br>
 * version: 1.0 <br>
 */
public class CurtainFragment extends Fragment {

    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";
    private ImageView curtainDpxs;

    private Button btnOpenCurtain;
    private Button btnCloseCurtain;

    private String ip;
    private Integer port;

    private Button timeSelectopenCurtain;
    private Button timeSelectcloseCurtain;
    private TextView txtTimeopenCurtain;
    private TextView txtTimecloseCurtain;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.curtain_fragment, null);

        mSharedPreference = this.getActivity().getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);

        btnOpenCurtain = view.findViewById(R.id.btn_open_curtain);
        btnCloseCurtain = view.findViewById(R.id.btn_close_curtain);
        curtainDpxs = view.findViewById(R.id.curtain_dpxs);

        timeSelectopenCurtain = view.findViewById(R.id.time_select_opencurtain);
        timeSelectcloseCurtain = view.findViewById(R.id.time_select_closecurtain);
        txtTimeopenCurtain = view.findViewById(R.id.txt_time_open_curtain);
        txtTimecloseCurtain = view.findViewById(R.id.txt_time_close_curtain);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ip = get("ip");
        String portStr = get("port");
        if (portStr != null) {
            port = Integer.parseInt(portStr);
        } else {
            port = 7016;
        }


        //开灯
        btnOpenCurtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "curtain_open");
            }
        });
        //关灯
        btnCloseCurtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "curtain_close");
            }
        });

        //定时开
        timeSelectopenCurtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                showTimePickerDialog(getActivity(), 5, txtTimeopenCurtain, calendar);
            }
        });
        //定时关
        timeSelectcloseCurtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                showTimePickerDialog(getActivity(), 6, txtTimecloseCurtain, calendar);
            }
        });
    }


    /**
     * 时间选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public void showTimePickerDialog(Activity activity, final int themeResId,final TextView tv,Calendar calendar) {

        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    switch (themeResId) {
                        case 5:
                        tv.setText("您选择的开启时间为：" + hourOfDay + "时" + minute + "分");break;
                        case 6:
                        tv.setText("您选择的关闭时间为：" + hourOfDay + "时" + minute + "分");break;
                    }
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);

                    switch (themeResId) {
                        case 5:
                        final Timer timer = new Timer();

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendMsg(ip, port, "curtain_open");
                                System.out.println("定时开启到了....");
                                timer.cancel();
                            }
                        }, c.getTime());break;
                        case 6:
                        final Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendMsg(ip, port, "curtain_close");
                                System.out.println("定时关闭到了....");
                                timer2.cancel();
                            }
                        }, c.getTime());break;
                    }

                    }
                }
                // 设置初始时间和关闭时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("android:support:fragments", null);
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("curtain_open".equals(msg)) {
                                curtainDpxs.setImageResource(R.drawable.dp_light);
                                Toast.makeText(getActivity().getApplicationContext(), "窗帘已经打开", Toast.LENGTH_SHORT).show();
                            } else {
                                curtainDpxs.setImageResource(R.drawable.dp_dark);
                                Toast.makeText(getActivity().getApplicationContext(), "窗帘已经关闭", Toast.LENGTH_SHORT).show();
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), status, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mSharedPreference = this.getActivity().getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);
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
        mSharedPreference = this.getActivity().getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);
        ip = get("ip");
        String portStr = get("port");
        if(portStr!=null){
            port = Integer.parseInt(portStr);
        }else{
            port = 7016;
        }
    }

}