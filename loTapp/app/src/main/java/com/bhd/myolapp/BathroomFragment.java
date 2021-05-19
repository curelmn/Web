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
import java.util.Timer;
import java.util.TimerTask;

/**
 * description: BathroomFragment 卫生间<br>
 * date: 2021/4/10 1:11 <br>
 * author: DaTao <br>
 * version: 1.0 <br>
 */
public class BathroomFragment extends Fragment {

    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";
    private ImageView bathroomDpxs;

    private Button btnOpenBathroom;
    private Button btnCloseBathroom;

    private String ip;
    private Integer port;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view=inflater.inflate(R.layout.bathroom_fragment,null);
        mSharedPreference = this.getActivity().getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);


        btnOpenBathroom = view.findViewById(R.id.btn_open_bathroom);
        btnCloseBathroom = view.findViewById(R.id.btn_close_bathroom);
        bathroomDpxs = view.findViewById(R.id.bathroom_dpxs);

//        timeSelect = view.findViewById(R.id.time_select);
//        txtTime= view.findViewById(R.id.txtTime);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // final User user ;

        ip = get("ip");
        String portStr = get("port");
        if(portStr!=null){
            port = Integer.parseInt(portStr);
        }else{
            port = 7016;
        }

        //开灯
        btnOpenBathroom.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                sendMsg(ip,port,"bathroom_open");
            }
        });
        //关灯
        btnCloseBathroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip,port,"bathroom_close");
            }
        });

//        //定时
//        timeSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar= Calendar.getInstance(Locale.CHINA);
//                showTimePickerDialog(getActivity(),  4, txtTime, calendar);
//            }
//        });

    }


    /**
     * 时间选择
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public  void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog( activity,themeResId,
            // 绑定监听器
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    tv.setText("您选择了：" + hourOfDay + "时" + minute  + "分");

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR,hourOfDay);
                    c.set(Calendar.MINUTE,minute);
                    c.set(Calendar.SECOND,0);
                    c.set(Calendar.MILLISECOND,0);


                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            sendMsg(ip,port,"bathroom_open");
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

    private String get(String key){
        return mSharedPreference.getString(key, null);
    }


    /**
     * 发送消息
     * @param ip
     * @param port
     * @param msg
     */
    private void sendMsg(final String ip, final int port, final String msg){

        new Thread(){
            @Override
            public void run() {

                Socket socket = null;

                try {
                    SocketAddress saAdd = new InetSocketAddress(ip.trim(), port);
                    socket = new Socket();
                    socket.connect(saAdd,1000);
//                    socket = new Socket(ip,port);

                    final OutputStream outputStream = socket.getOutputStream();
                    // 将String转换成byte[]传输数据，使用UTF-8编码，服务端也使用UTF-8转换，支持中文
                    outputStream.write(msg.getBytes(StandardCharsets.UTF_8));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("bathroom_open".equals(msg)){
                                bathroomDpxs.setImageResource(R.drawable.dp_light);
                                Toast.makeText(getActivity().getApplicationContext(), "已经打开", Toast.LENGTH_SHORT).show();
                            }else{
                                bathroomDpxs.setImageResource(R.drawable.dp_dark);
                                Toast.makeText(getActivity().getApplicationContext(), "已经关闭", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    outputStream.close();
                } catch (UnknownHostException e) {
                    sendStatus("未知IP");
                }catch (SocketTimeoutException e) {
                    sendStatus("连接超时");
                }catch (IOException e) {
                    sendStatus("发送失败");
                    //e.printStackTrace();
                }catch (Exception e) {
                    sendStatus("发送失败");
                }finally {
                    try {
                        if(socket != null) {
                            socket.close();
                        }
                    }catch (Exception e) {
                    }
                }
            }
        }.start();
    }

    private void sendStatus(final String status){
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