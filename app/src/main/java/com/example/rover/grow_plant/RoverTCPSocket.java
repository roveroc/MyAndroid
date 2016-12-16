package com.example.rover.grow_plant;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

/**
 * Created by rover on 2016/12/14.
 */

public class RoverTCPSocket implements Runnable{


    private Socket socket;

    public String host;

    public int    hostPort;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private InputStream inputStream;
    private OutputStream outputStream;


    private ConnectListener mListener;

    public interface ConnectListener{
        void onReceiveData(String data);
        void connected();
    }


    public void run() {

    }


    public RoverTCPSocket(ConnectListener callback) {
        this.mListener = callback;
    }


    public void connectToHost ()throws IOException
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) {
                        socket = new Socket();

                        SocketAddress address = new InetSocketAddress(host, hostPort);
                        socket.connect(address, 1000);// 连接指定IP和端口

                        if (socket.isConnected()) {
                            inputStream = socket.getInputStream();
                            outputStream = socket.getOutputStream();
                            mListener.connected();
                        }

                    }
                    Log.i("connect", "here");
                } catch (IOException ex) {
                    Log.i("异常 ", "ex = ", ex);
                }


                Log.i("连接socket", "结果" + socket);


                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;

                    while ((len = inputStream.read(buffer)) != -1) {

                        String data = new String(buffer, 0, len);
                        //通过回调接口将获取到的数据推送出去
                        if (mListener != null) {
                            mListener.onReceiveData(data);
                        }
                    }
                }catch (IOException ex) {
                    Log.i("异常 ", "ex = ", ex);
                }
            }
        }).start();
    }



    /**
     * 返回连接服是否成功
     *
     * @return
     */
    public boolean isConnected_rover() {
        if(socket != null){

            Log.i("检测","返回连接服是否成功");

            return socket.isConnected();
        }
        return false;
    }



    public void sendData(String data) throws IOException {
        Log.i("fuck ","fuck fuckfuckfuck大发送方式的发生的");
        new Thread(new Runnable() {

            @Override
            public void run() {

                Log.i("fuck ","fuck fuckfuckfuck");
                OutputStream os = null;

                try {
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    byte data[]="abcd".getBytes();
                    out.write(data);

                    out.flush();


                    Log.i("fuck ","fuck ");

                } catch (IOException e) {

                    Log.i("IOException","IOException",e);
                }
            }
        }).start();

    }


}
