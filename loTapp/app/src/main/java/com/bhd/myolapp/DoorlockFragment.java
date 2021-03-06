package com.bhd.myolapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class DoorlockFragment extends Fragment {
    private SharedPreferences mSharedPreference;
    private static final String SP_TEST = "sp_test";


    private Button btnOpenDoorlock;
    private Button btnCloseDoorlock;
    private EditText editTextPassword;
    private Button btnPassword;

    private String ip;
    private Integer port;
    private String PrintPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.doorlock_fragment, null);

        mSharedPreference = this.getActivity().getSharedPreferences(SP_TEST, Context.MODE_PRIVATE);

        btnOpenDoorlock = view.findViewById(R.id.btn_open_doorlock);
        btnCloseDoorlock = view.findViewById(R.id.btn_close_doorlock);
        editTextPassword = view.findViewById(R.id.editText_password);
        btnPassword = view.findViewById(R.id.btn_password);


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


        //??????
        btnOpenDoorlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "door_open");
            }
        });
        //??????
        btnCloseDoorlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "door_close");
            }
        });
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(ip, port, "????????????");
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
     * ????????????
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
                    // ???String?????????byte[]?????????????????????UTF-8???????????????????????????UTF-8?????????????????????
                    outputStream.write(msg.getBytes(StandardCharsets.UTF_8));

                    getActivity().runOnUiThread(new Runnable() {
                       @Override
                        public void run() {
                           if ("????????????".equals(msg)) {
                               Toast.makeText(getActivity().getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                           }
                           if ("door_open".equals(msg)) {
                               if("1234567890".equals(editTextPassword.getText().toString())) {
                                   Toast.makeText(getActivity().getApplicationContext(), "???????????????????????????", Toast.LENGTH_SHORT).show();
                               }else{
                                   Toast.makeText(getActivity().getApplicationContext(), "??????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                               }
                           }
                           if ("door_close".equals(msg)) {
                               if("1234567890".equals(editTextPassword.getText().toString())) {
                                   Toast.makeText(getActivity().getApplicationContext(), "???????????????", Toast.LENGTH_SHORT).show();
                               }else{
                                   Toast.makeText(getActivity().getApplicationContext(), "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                               }

                           }


                       }
                    });
                    outputStream.close();
                } catch (UnknownHostException e) {
                    sendStatus("??????IP");
                } catch (SocketTimeoutException e) {
                    sendStatus("????????????");
                } catch (IOException e) {
                    sendStatus("????????????");
                    //e.printStackTrace();
                } catch (Exception e) {
                    sendStatus("????????????");
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
