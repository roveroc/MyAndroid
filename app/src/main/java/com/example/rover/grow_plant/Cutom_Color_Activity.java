package com.example.rover.grow_plant;



import android.graphics.Color;
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
import android.os.Handler;
import android.view.ViewGroup;
import android.content.Context;
import android.content.SharedPreferences;

import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;

import org.w3c.dom.Text;

/**
 * Created by rover on 2016/12/22.
 */

public class Cutom_Color_Activity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    private static final String MY_BROADCAST_TAG = "com.example.localbroadcasttest";


    private ColorPickerView colorView;
    private ImageView       view1;
    private ImageView       view2;
    private ImageView       view3;
    private ImageView       view4;
    private ImageView       view5;
    private ImageView       view6;
    private ImageView       view7;
    private ImageView       view8;

    private String          color1 = null;
    private String          color2 = null;
    private String          color3 = null;
    private String          color4 = null;
    private String          color5 = null;
    private String          color6 = null;
    private String          color7 = null;
    private String          color8 = null;

    private SeekBar         r_bar;
    private SeekBar         g_bar;
    private SeekBar         b_bar;

    private TextView        r_bar_textview;
    private TextView        g_bar_textview;
    private TextView        b_bar_textview;

    private ImageView       tempView = null;
    private int             tempid   = -1;

    private boolean         scalFlag = true;


    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cutom_color_layout);

        context = getApplicationContext();

        //按钮
        view1 = (ImageView)findViewById(R.id.custom_1);
        view2 = (ImageView)findViewById(R.id.custom_2);
        view3 = (ImageView)findViewById(R.id.custom_3);
        view4 = (ImageView)findViewById(R.id.custom_4);
        view5 = (ImageView)findViewById(R.id.custom_5);
        view6 = (ImageView)findViewById(R.id.custom_6);
        view7 = (ImageView)findViewById(R.id.custom_7);
        view8 = (ImageView)findViewById(R.id.custom_8);

        //取数据
        SharedPreferences sharedPreferences = this.getSharedPreferences("test", MODE_PRIVATE);
        color1 = sharedPreferences.getString("custom_1","");
        color2 = sharedPreferences.getString("custom_2","");
        color3 = sharedPreferences.getString("custom_3","");
        color4 = sharedPreferences.getString("custom_4","");
        color5 = sharedPreferences.getString("custom_5","");
        color6 = sharedPreferences.getString("custom_6","");
        color7 = sharedPreferences.getString("custom_7","");
        color8 = sharedPreferences.getString("custom_8","");

        if(color1.length() > 0){
            String[] c = color1.split("#");
            view1.setBackgroundColor(Color.rgb(Integer.parseInt(c[0]),Integer.parseInt(c[1]),Integer.parseInt(c[2])));
        }
        if(color2.length() > 0){
            String[] c = color2.split("#");
            view2.setBackgroundColor(Color.rgb(Integer.parseInt(c[0]),Integer.parseInt(c[1]),Integer.parseInt(c[2])));
        }
        if(color3.length() > 0){
            String[] c = color3.split("#");
            view3.setBackgroundColor(Color.rgb(Integer.parseInt(c[0]),Integer.parseInt(c[1]),Integer.parseInt(c[2])));
        }
        if(color4.length() > 0){
            String[] c = color4.split("#");
            view4.setBackgroundColor(Color.rgb(Integer.parseInt(c[0]),Integer.parseInt(c[1]),Integer.parseInt(c[2])));
        }
        if(color5.length() > 0){
            String[] c = color5.split("#");
            view5.setBackgroundColor(Color.rgb(Integer.parseInt(c[0]),Integer.parseInt(c[1]),Integer.parseInt(c[2])));
        }
        if(color6.length() > 0){
            String[] c = color6.split("#");
            view6.setBackgroundColor(Color.rgb(Integer.parseInt(c[0]),Integer.parseInt(c[1]),Integer.parseInt(c[2])));
        }
        if(color7.length() > 0){
            String[] c = color7.split("#");
            view7.setBackgroundColor(Color.rgb(Integer.parseInt(c[0]),Integer.parseInt(c[1]),Integer.parseInt(c[2])));
        }
        if(color8.length() > 0){
            String[] c = color8.split("#");
            view8.setBackgroundColor(Color.rgb(Integer.parseInt(c[0]),Integer.parseInt(c[1]),Integer.parseInt(c[2])));
        }

        r_bar = (SeekBar)findViewById(R.id.red_bar);
        r_bar_textview = (TextView)findViewById(R.id.red_bar_value);
        r_bar.setMax(255);

        g_bar = (SeekBar)findViewById(R.id.green_bar);
        g_bar_textview = (TextView)findViewById(R.id.green_bar_value);
        g_bar.setMax(255);

        b_bar = (SeekBar)findViewById(R.id.blue_bar);
        b_bar_textview = (TextView)findViewById(R.id.blue_bar_value);
        b_bar.setMax(255);

        //色盘取色
        colorView = (ColorPickerView) findViewById(R.id.custom_colorView);
        colorView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChange(int color) {

                String pwm = Integer.toHexString(color).toUpperCase();
                if (Utils.isHexChar(pwm) == true) {
                    byte[] PwmValue = Utils.hexStringToBytes(pwm);

                    int mWhite = PwmValue[0] & 0xff;
                    int mRed   = PwmValue[1] & 0xff;
                    int mGreen = PwmValue[2] & 0xff;
                    int mBlue  = PwmValue[3] & 0xff;

                    if(tempView != null){
                        ViewGroup.LayoutParams  temp = tempView.getLayoutParams();
                        if(temp.width > 100){
                            tempView.setBackgroundColor(Color.rgb(mRed,mGreen,mBlue));
                        }
                    }

                    r_bar.setProgress(mRed);
                    g_bar.setProgress(mGreen);
                    b_bar.setProgress(mBlue);

                    r_bar_textview.setText(mRed+"");
                    g_bar_textview.setText(mGreen+"");
                    b_bar_textview.setText(mBlue+"");

                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", mRed);
                    order.putExtra("g", mGreen);
                    order.putExtra("b", mBlue);
                    context.sendBroadcast(order);
                }
            }
        });

        view1.setLongClickable(true);
        view2.setLongClickable(true);
        view3.setLongClickable(true);
        view4.setLongClickable(true);
        view5.setLongClickable(true);
        view6.setLongClickable(true);
        view7.setLongClickable(true);
        view8.setLongClickable(true);

        view1.setOnClickListener(this);
        view1.setOnLongClickListener(this);

        view2.setOnClickListener(this);
        view2.setOnLongClickListener(this);

        view3.setOnClickListener(this);
        view3.setOnLongClickListener(this);

        view4.setOnClickListener(this);
        view4.setOnLongClickListener(this);

        view5.setOnClickListener(this);
        view5.setOnLongClickListener(this);

        view6.setOnClickListener(this);
        view6.setOnLongClickListener(this);

        view7.setOnClickListener(this);
        view7.setOnLongClickListener(this);

        view8.setOnClickListener(this);
        view8.setOnLongClickListener(this);


    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.custom_1:{
                if(color1.length() > 0) {
                    String[] c = color1.split("#");
                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", Integer.parseInt(c[0]));
                    order.putExtra("g", Integer.parseInt(c[1]));
                    order.putExtra("b", Integer.parseInt(c[2]));
                    context.sendBroadcast(order);
                }
            }
                break;
            case R.id.custom_2:{
                if(color2.length() > 0) {
                    String[] c = color2.split("#");
                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", Integer.parseInt(c[0]));
                    order.putExtra("g", Integer.parseInt(c[1]));
                    order.putExtra("b", Integer.parseInt(c[2]));
                    context.sendBroadcast(order);
                }
            }
                break;
            case R.id.custom_3:{
                if(color3.length() > 0) {
                    String[] c = color3.split("#");
                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", Integer.parseInt(c[0]));
                    order.putExtra("g", Integer.parseInt(c[1]));
                    order.putExtra("b", Integer.parseInt(c[2]));
                    context.sendBroadcast(order);
                }
            }
                break;
            case R.id.custom_4:{
                if(color4.length() > 0) {
                    String[] c = color4.split("#");
                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", Integer.parseInt(c[0]));
                    order.putExtra("g", Integer.parseInt(c[1]));
                    order.putExtra("b", Integer.parseInt(c[2]));
                    context.sendBroadcast(order);
                }
            }
                break;
            case R.id.custom_5:{
                if(color1.length() > 0) {
                    String[] c = color1.split("#");
                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", Integer.parseInt(c[0]));
                    order.putExtra("g", Integer.parseInt(c[1]));
                    order.putExtra("b", Integer.parseInt(c[2]));
                    context.sendBroadcast(order);
                }
            }
                break;
            case R.id.custom_6:{
                if(color6.length() > 0) {
                    String[] c = color6.split("#");
                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", Integer.parseInt(c[0]));
                    order.putExtra("g", Integer.parseInt(c[1]));
                    order.putExtra("b", Integer.parseInt(c[2]));
                    context.sendBroadcast(order);
                }
            }
                break;
            case R.id.custom_7:{
                if(color6.length() > 0) {
                    String[] c = color7.split("#");
                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", Integer.parseInt(c[0]));
                    order.putExtra("g", Integer.parseInt(c[1]));
                    order.putExtra("b", Integer.parseInt(c[2]));
                    context.sendBroadcast(order);
                }
            }
                break;
            case R.id.custom_8:{
                if(color8.length() > 0) {
                    String[] c = color8.split("#");
                    Intent order = new Intent(MY_BROADCAST_TAG);
                    order.putExtra("r", Integer.parseInt(c[0]));
                    order.putExtra("g", Integer.parseInt(c[1]));
                    order.putExtra("b", Integer.parseInt(c[2]));
                    context.sendBroadcast(order);
                }
            }
                break;
            default:
                break;

        }

    }

    public boolean onLongClick(View v) {
        if(tempView != null){
            ViewGroup.LayoutParams  temp = tempView.getLayoutParams();
            if(temp.width > 100 && tempid != v.getId()){
                temp.width -= 20;
                temp.height -= 20;
                tempView.setLayoutParams(temp);
                scalFlag = true;

                saveColorValue();
            }
        }
        tempView = (ImageView)findViewById(v.getId());
        tempid   = v.getId();

        if(scalFlag == true){
            ViewGroup.LayoutParams  lp = tempView.getLayoutParams();
            lp.width+=20;
            lp.height+=20;
            scalFlag = false;
            tempView.setLayoutParams(lp);
        }else {
            ViewGroup.LayoutParams  lp = tempView.getLayoutParams();
            lp.width-=20;
            lp.height-=20;
            scalFlag = true;
            tempView.setLayoutParams(lp);
        }
        return true;
    }


    //返回上一个界面
    public void onBackPressed(){
        saveColorValue();
        super.onBackPressed();
    }


    public void saveColorValue(){
        String str = "custom_1";
        switch (tempid){
            case R.id.custom_1:{
                str = "custom_1";
            }
                break;
            case R.id.custom_2:{
                str = "custom_2";
            }
                break;
            case R.id.custom_3:{
                str = "custom_3";
            }
                break;
            case R.id.custom_4:{
                str = "custom_4";
            }
                break;
            case R.id.custom_5:{
                str = "custom_5";
            }
                break;
            case R.id.custom_6:{
                str = "custom_6";
            }
                break;
            case R.id.custom_7:{
                str = "custom_7";
            }
                break;
            case R.id.custom_8:{
                str = "custom_8";
            }
                break;
            default:
                break;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);

        String valueStr = r_bar.getProgress() + "#" + g_bar.getProgress() + "#" + b_bar.getProgress();

        //得到SharedPreferences.Editor对象，并保存数据到该对象中
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(str, valueStr);

        //保存key-value对到文件中
        editor.commit();

        //取数据
        color1 = sharedPreferences.getString("custom_1","");
        color2 = sharedPreferences.getString("custom_2","");
        color3 = sharedPreferences.getString("custom_3","");
        color4 = sharedPreferences.getString("custom_4","");
        color5 = sharedPreferences.getString("custom_5","");
        color6 = sharedPreferences.getString("custom_6","");
        color7 = sharedPreferences.getString("custom_7","");
        color8 = sharedPreferences.getString("custom_8","");
    }



}
