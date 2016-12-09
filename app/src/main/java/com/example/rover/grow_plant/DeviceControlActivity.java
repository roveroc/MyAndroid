package com.example.rover.grow_plant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.content.Intent;

/**
 * Created by rover on 2016/12/9.
 */

public class DeviceControlActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        DeviceStruct device = (DeviceStruct) intent.getExtras().get("deviceInfo");

        Log.i("传过来信息","为 = " + device.getDevice_id() + device.getDevice_name());

        setContentView(R.layout.device_control_fragment);



    }


}
