package com.bhd.myolapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class VisualFragment extends Fragment {
    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";


    private Button btnAgreeVisual;
    private Button btnRefuseVisual;
    private Button btnMenweiVisual;
    private Button btnWuyeVisual;
    private Button btnZhibanVisual;

    private String ip;
    private Integer port;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.visual_fragment, null);

        mSharedPreference = this.getActivity().getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);

        btnAgreeVisual = view.findViewById(R.id.btn_agree_visual);
        btnRefuseVisual = view.findViewById(R.id.btn_refuse_visual);
        btnMenweiVisual = view.findViewById(R.id.btn_menwei_visual);
        btnWuyeVisual = view.findViewById(R.id.btn_wuye_visual);
        btnZhibanVisual = view.findViewById(R.id.btn_zhiban_visual);


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


        //开门
        btnAgreeVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "visual_open");
            }
        });
        //关门
        btnRefuseVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "visual_close");
            }
        });
        btnMenweiVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "menwei_visual");
            }
        });
        btnWuyeVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "wuye_visual");
            }
        });
        btnZhibanVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "zhiban_visual");
            }
        });
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
                            if ("visual_agree".equals(msg)) {
                                Toast.makeText(getActivity().getApplicationContext(), "同意接听", Toast.LENGTH_SHORT).show();
                            }
                            if ("menwei_visual".equals(msg)) {
                               Toast.makeText(getActivity().getApplicationContext(), "已连接门卫室", Toast.LENGTH_SHORT).show();
                           }if ("wuye_visual".equals(msg)) {
                               Toast.makeText(getActivity().getApplicationContext(), "已连接物业", Toast.LENGTH_SHORT).show();
                           }if ("zhiban_visual".equals(msg)) {
                               Toast.makeText(getActivity().getApplicationContext(), "已连接值班室", Toast.LENGTH_SHORT).show();
                           }if ("visual_refyse".equals(msg)) {
                               Toast.makeText(getActivity().getApplicationContext(), "拒绝接听", Toast.LENGTH_SHORT).show();
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
