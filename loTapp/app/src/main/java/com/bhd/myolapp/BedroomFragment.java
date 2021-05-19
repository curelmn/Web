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
public class BedroomFragment extends Fragment {

    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";
    private ImageView bedroomDpxs;

    private Button btnOpenBedroom;
    private Button btnCloseBedroom;
    private Button btnOpenTopbedroom;
    private Button btnCloseTopbedroom;

    private String ip;
    private Integer port;

    private Button timeSelectopenBedroom;
    private Button timeSelectcloseBedroom;
    private TextView txtTimeopenBedroom;
    private TextView txtTimecloseBedroom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.bedroom_fragment, null);

        mSharedPreference = this.getActivity().getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);

        btnOpenBedroom = view.findViewById(R.id.btn_open_bedroom);
        btnCloseBedroom = view.findViewById(R.id.btn_close_bedroom);
        btnOpenTopbedroom = view.findViewById(R.id.btn_open_topbedroom);
        btnCloseTopbedroom = view.findViewById(R.id.btn_close_topbedroom);
        bedroomDpxs = view.findViewById(R.id.bedroom_dpxs);

        timeSelectopenBedroom = view.findViewById(R.id.time_select_openbedroom);
        timeSelectcloseBedroom = view.findViewById(R.id.time_select_closebedroom);
        txtTimeopenBedroom = view.findViewById(R.id.txt_time_openbedroom);
        txtTimecloseBedroom = view.findViewById(R.id.txt_time_closebedroom);
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
        btnOpenTopbedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "topbedroom_open");
            }
        });
        //关灯
        btnCloseTopbedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "topbedroom_close");
            }
        });
        btnOpenBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "bedroom_open");
            }
        });
        //关灯
        btnCloseBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "bedroom_close");
            }
        });

        //定时
        timeSelectopenBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance(Locale.CHINA);
                showTimePickerDialog(getActivity(),  5, txtTimeopenBedroom, calendar);
            }
        });
        timeSelectcloseBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance(Locale.CHINA);
                showTimePickerDialog(getActivity(),  6, txtTimecloseBedroom, calendar);
            }
        });
    }


    /**
     * 时间选择
     *
     * @param activity
     * @param themeResId
     * @param bed
     * @param calendar
     */
    public  void showTimePickerDialog(Activity activity, final int themeResId, final TextView bed, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog( activity,themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        switch(themeResId){
                            case 5:
                                bed.setText("您选择了开启时间：" + hourOfDay + "时" + minute  + "分");
                                break;
                            case 6:
                                bed.setText("您选择了关闭时间：" + hourOfDay + "时" + minute  + "分");
                                break;}

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.set(Calendar.SECOND,0);
                        c.set(Calendar.MILLISECOND,0);


                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                switch(themeResId){
                                    case 5:
                                        sendMsg(ip,port,"living_open");
                                        break;
                                    case 6:
                                        sendMsg(ip,port,"living_close");
                                        break;}
                                System.out.println("定时到了....");
                                timer.cancel();
                            }
                        },c.getTime());

                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
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


                            if ("bedroom_open".equals(msg)) {
                                bedroomDpxs.setImageResource(R.drawable.dp_light);
                                Toast.makeText(getActivity().getApplicationContext(), "已经打开", Toast.LENGTH_SHORT).show();
                            } else  if("bedroom_close".equals(msg)){
                                bedroomDpxs.setImageResource(R.drawable.dp_dark);
                                Toast.makeText(getActivity().getApplicationContext(), "已经关闭", Toast.LENGTH_SHORT).show();
                            }
                            if ("topbedroom_open".equals(msg)) {
                                bedroomDpxs.setImageResource(R.drawable.dp_light);
                                Toast.makeText(getActivity().getApplicationContext(), "已经打开", Toast.LENGTH_SHORT).show();
                            } else if("topbedroom_close".equals(msg)){
                                bedroomDpxs.setImageResource(R.drawable.dp_dark);
                                Toast.makeText(getActivity().getApplicationContext(), "已经关闭", Toast.LENGTH_SHORT).show();
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