package com.bhd.myolapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * description: HeaterFragment 电热水器<br>
 * date: 2021/4/10 1:02 <br>
 * author: DaTao <br>
 * version: 1.0 <br>
 */
public class HeaterFragment extends Fragment{
    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";
    private ImageView heaterDpxs;

    private Button btnColdHeater;
    private Button btnHotHeater;

    private String ip;
    private Integer port;


    //private ImageView dpxs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view=inflater.inflate(R.layout.heater_fragment,null);
        mSharedPreference = this.getActivity().getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);

        btnColdHeater = view.findViewById(R.id.btn_heater_cold);
        btnHotHeater = view.findViewById(R.id.btn_heater_hot);
        heaterDpxs = view.findViewById(R.id.heater_dpxs);
        //dpxs = getActivity().findViewById(R.id.dpxs);


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

        //冷水模式
        btnColdHeater.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                sendMsg(ip,port,"cold mood");
            }
        });
        //洗衣模式
        btnHotHeater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip,port,"washing_mood");
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
                            if("cold_mood".equals(msg)||"washer_mood".equals(msg)||"shower_mood".equals(msg)){
                                heaterDpxs.setImageResource(R.drawable.dp_light);
                                Toast.makeText(getActivity().getApplicationContext(), "已经处于该模式", Toast.LENGTH_SHORT).show();
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
}
