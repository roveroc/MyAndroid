package com.example.rover.grow_plant;

import android.util.Log;

import java.io.IOException;

/**
 * Created by rover on 2016/12/16.
 */

public class GrowPlantGlobal {


    private RoverTCPSocket roverSocket;


    private static GrowPlantGlobal instance = null;

    public static GrowPlantGlobal getInstance(){

        if (instance == null) {
            instance = new GrowPlantGlobal();
        }
        return instance;

    }


    //连接主机
    public void connectToHost(){
        if(roverSocket == null){
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
            roverSocket = new RoverTCPSocket(lcon);
        }
        roverSocket.host = "192.168.1.183";
        roverSocket.hostPort = 5000;
        try {
            roverSocket.connectToHost();
            Log.i("connect","here");
        }catch (IOException ex){
            Log.i("异常 ","ex = ",ex);
        }
    }


    //发送数据
    public void sendDataToHost(String str){
        try {
            roverSocket.sendData(str);
            Log.i("发送数据","发送数据");
        }catch (IOException io){
            Log.i("发送数据异常","don't do this");
        }

    }


}
