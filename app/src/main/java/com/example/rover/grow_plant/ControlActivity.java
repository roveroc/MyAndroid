package com.example.rover.grow_plant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

/**
 * Created by rover on 2016/12/7.
 */

public class ControlActivity extends AppCompatActivity implements DeviceFragment.enterDimmerController,MoreFragment.moreFragmentInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.control_activity);

        Log.i("this value = ","= "+this);

    }

    public void roverListItemSelected(DeviceStruct device){
        Intent intent = new Intent();
        intent.setClass(this, DeviceControlActivity.class);

//        Bundle mBundle = new Bundle();
//        mBundle.putSerializable("deviceinfo",device);

        intent.putExtra("deviceInfo", device);
        startActivity(intent);
    }

    //实现More界面接口
    public void startOtherFragment(){
        Intent intent = new Intent();
        intent.setClass(this, Test_learn_Something.class);
        startActivity(intent);
    }

}
