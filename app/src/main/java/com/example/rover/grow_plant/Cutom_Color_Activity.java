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
import android.view.ViewTreeObserver;

import org.w3c.dom.Text;

/**
 * Created by rover on 2016/12/22.
 */

public class Cutom_Color_Activity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    private ColorPickerView colorView;
    private ImageView       view1;
    private ImageView       view2;
    private ImageView       view3;
    private ImageView       view4;
    private ImageView       view5;
    private ImageView       view6;
    private ImageView       view7;
    private ImageView       view8;

    private SeekBar         r_bar;
    private SeekBar         g_bar;
    private SeekBar         b_bar;

    private TextView        r_bar_textview;
    private TextView        g_bar_textview;
    private TextView        b_bar_textview;

    private ImageView       tempView = null;
    private int             tempid   = -1;

    private boolean         scalFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cutom_color_layout);

        r_bar = (SeekBar)findViewById(R.id.red_bar);
        r_bar_textview = (TextView)findViewById(R.id.red_bar_value);

        g_bar = (SeekBar)findViewById(R.id.green_bar);
        g_bar_textview = (TextView)findViewById(R.id.green_bar_value);

        b_bar = (SeekBar)findViewById(R.id.blue_bar);
        b_bar_textview = (TextView)findViewById(R.id.blue_bar_value);

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

                    Log.i("value ", " = " + mWhite + "  " + mRed + "  " + mGreen + "  " + mBlue);
                    if(tempView != null){
                        ViewGroup.LayoutParams  temp = tempView.getLayoutParams();
                        if(temp.width > 120){
                            tempView.setBackgroundColor(Color.rgb(mRed,mGreen,mBlue));
                        }
                    }




                }
            }
        });

        //按钮
        view1 = (ImageView)findViewById(R.id.custom_1);
        view2 = (ImageView)findViewById(R.id.custom_2);
        view3 = (ImageView)findViewById(R.id.custom_3);
        view4 = (ImageView)findViewById(R.id.custom_4);
        view5 = (ImageView)findViewById(R.id.custom_5);
        view6 = (ImageView)findViewById(R.id.custom_6);
        view7 = (ImageView)findViewById(R.id.custom_7);
        view8 = (ImageView)findViewById(R.id.custom_8);

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


        //获取控件大小
//        view1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int height = view1.getHeight();
//                int width = view1.getWidth();
//                Log.i("size "," == " + height + "  " +  width);
//            }
//        });

    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.custom_1:{

            }
                break;
            default:
                break;
        }
    }

    public boolean onLongClick(View v) {
        if(tempView != null){
            ViewGroup.LayoutParams  temp = tempView.getLayoutParams();
            if(temp.width > 120 && tempid != v.getId()){
                temp.width -= 20;
                temp.height -= 20;
                tempView.setLayoutParams(temp);
                scalFlag = true;
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

    }


}
