package com.example.rover.grow_plant;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        ImageButton btn = (ImageButton)this.findViewById(R.id.iamgebutton);
        btn.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iamgebutton:{
                Log.i("123","321");

                Intent intent = new Intent();
                intent.setClass(this, ControlActivity.class);
                startActivity(intent);

            }
                break;
            default:
                break;
        }
    }


}
