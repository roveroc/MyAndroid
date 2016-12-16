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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.more_fragment, container, false);

        ImageButton btn = (ImageButton)view.findViewById(R.id.connectBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GrowPlantGlobal.getInstance().connectToHost();

            }
        });



        ImageButton sendBtn = (ImageButton)view.findViewById(R.id.sendDataBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GrowPlantGlobal.getInstance().sendDataToHost("text");

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
