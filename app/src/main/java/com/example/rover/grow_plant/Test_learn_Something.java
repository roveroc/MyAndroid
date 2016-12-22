package com.example.rover.grow_plant;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageView;


/**
 * Created by rover on 2016/12/20.
 */

public class Test_learn_Something extends AppCompatActivity implements ColorPickerView.OnColorChangedListener {

    private ColorPickerView colorView;

    private SeekBar         lightBar;
    private TextView        lightBarValueText;

    private SeekBar         speedBar;
    private TextView        speedBarValueText;

    private ImageView       switchImageView;        //开关
    private ImageView       playPauseImageView;     //播放暂停
    private Button          customBtn;              //自定义

    private boolean         switchFlag;
    private boolean         playFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_learn_something);

        switchFlag = true;
        playFlag   = true;

        //色盘取色
        colorView = (ColorPickerView)findViewById(R.id.colorView);
        colorView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChange(int color) {

                String pwm = Integer.toHexString(color).toUpperCase();
                if (Utils.isHexChar(pwm) == true) {
                    byte[] PwmValue = Utils.hexStringToBytes(pwm);

                    int mWhite = PwmValue[0] & 0xff;
                    int mRed = PwmValue[1] & 0xff;
                    int mGreen = PwmValue[2] & 0xff;
                    int mBlue = PwmValue[3] & 0xff;

                    Log.i("value "," = " + mWhite + "  " + mRed + "  " + mGreen + "  " + mBlue);

                }
            }
        });


        //亮度滑动条
        lightBar = (SeekBar)findViewById(R.id.lightBar);
        lightBar.setMax(100);
        lightBarValueText = (TextView)findViewById(R.id.lightBarValue);

        lightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lightBarValueText.setText(i+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //速度滑动条
        speedBar = (SeekBar)findViewById(R.id.speedBar);
        speedBar.setMax(100);
        speedBarValueText = (TextView)findViewById(R.id.speedBarValue);

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speedBarValueText.setText(i+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //开关
        switchImageView = (ImageView)findViewById(R.id.switchBtn);
        switchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchFlag == true){
                    switchImageView.setBackgroundResource(R.mipmap.off);
                    switchFlag = false;
                }else{
                    switchImageView.setBackgroundResource(R.mipmap.on);
                    switchFlag = true;
                }
            }
        });

        //播放暂停
        playPauseImageView = (ImageView)findViewById(R.id.playPauseBtn);
        playPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playFlag == true){
                    playPauseImageView.setBackgroundResource(R.mipmap.pause);
                    playFlag = false;
                }else{
                    playPauseImageView.setBackgroundResource(R.mipmap.play);
                    playFlag = true;
                }
            }
        });

        //自定义
        customBtn = (Button)findViewById(R.id.CustomBtn);
        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(Test_learn_Something.this, Cutom_Color_Activity.class);
                startActivity(intent);

            }
        });


    }


    public void onColorChange(int color) {

    }

}
