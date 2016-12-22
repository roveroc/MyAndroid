package com.example.rover.grow_plant;



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
 * Created by rover on 2016/12/22.
 */

public class Cutom_Color_Activity extends AppCompatActivity{

    private ColorPickerView colorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cutom_color_layout);

        //色盘取色
        colorView = (ColorPickerView) findViewById(R.id.custom_colorView);
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

                    Log.i("value ", " = " + mWhite + "  " + mRed + "  " + mGreen + "  " + mBlue);

                }
            }
        });
    }


}
