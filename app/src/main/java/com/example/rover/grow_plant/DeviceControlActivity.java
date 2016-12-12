package com.example.rover.grow_plant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by rover on 2016/12/9.
 */

public class DeviceControlActivity extends Activity implements SeekBar.OnSeekBarChangeListener{


    private ImageButton switchBtn;
    private SeekBar     slider1;
    private SeekBar     slider2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        DeviceStruct device = (DeviceStruct) intent.getExtras().get("deviceInfo");

        Log.i("传过来信息","为 = " + device.getDevice_id() + device.getDevice_name());

        setContentView(R.layout.device_control_fragment);

        switchBtn = (ImageButton)this.findViewById(R.id.switchBtn_device);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("switch btn","Clicked");
            }
        });

        slider1   = (SeekBar)this.findViewById(R.id.bar1);
        slider1.setMax(90);

        slider2   = (SeekBar)this.findViewById(R.id.bar2);
        slider1.setMax(80);

        slider1.setOnSeekBarChangeListener(this);
        slider2.setOnSeekBarChangeListener(this);

    }


    /*
     * SeekBar停止滚动的回调函数
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /*
     * SeekBar开始滚动的回调函数
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /*
     * SeekBar滚动时的回调函数
     */
    @Override
    public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
        switch (seekBar.getId()){
            case R.id.bar1:{
                Log.d("slider11  slider11", "value = :" + progress);
            }
                break;
            case R.id.bar2:{
                Log.d("slider22  slider22", "value = :" + progress);
            }
                break;
            default:
                break;
        }

    }

}
