package com.example.rover.grow_plant;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import java.io.IOException;

/**
 * Created by rover on 2016/12/7.
 */

public class MoreFragment extends Fragment implements RoverTCPSocket.ConnectListener{


    private RoverTCPSocket tcpSocket;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.more_fragment, container, false);

        ImageButton btn = (ImageButton)view.findViewById(R.id.connectBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tcpSocket == null){
                    RoverTCPSocket.ConnectListener lcon = new RoverTCPSocket.ConnectListener() {
                        @Override
                        public void onReceiveData(String data) {
                            Log.i("TCP主机返回的数据"," = "+data);
                        }

                        @Override
                        public void connected() {
                            Log.i("TCP主机返回的数据"," 连接成功   123");
                        }
                    };
                    tcpSocket = new RoverTCPSocket(lcon);
                    tcpSocket.host = "192.168.1.183";
                    tcpSocket.hostPort = 5000;
                    try {
                        tcpSocket.connectToHost();
                        Log.i("connect","here");
                    }catch (IOException ex){
                        Log.i("异常 ","ex = ",ex);
                    }
                }
            }
        });



        ImageButton sendBtn = (ImageButton)view.findViewById(R.id.sendDataBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tcpSocket.sendData("fuck");
                    Log.i("发送数据","发送数据");
                }catch (IOException io){
                    Log.i("发送数据异常","don't do this");
                }

            }
        });


        return view;
    }


    public void onReceiveData(String data){


        Log.i("TCP主机返回的数据"," = "+data);

    }

    public void connected(){
        Log.i("TCP主机返回的数据"," 连接成功  000");
    }


}
