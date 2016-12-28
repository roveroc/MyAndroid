package com.example.rover.grow_plant;


import android.widget.Toast;
import android.content.Context;
import android.os.Handler;
import android.view.View;

/**
 * Created by rover on 2016/12/28.
 */

public class MyToast {


    private Context mContext = null;
    private Toast mToast = null;
    private Handler mHandler = null;
    private int duration = 0;
    private int currDuration = 0;
    private final int DEFAULT=2000;
    private Runnable mToastThread = new Runnable() {

        public void run() {
            mToast.show();
            mHandler.postDelayed(mToastThread, DEFAULT);// 每隔2秒显示一次
            if (duration != 0) {
                if (currDuration <= duration) {
                    currDuration += DEFAULT;
                }
                else {
                    cancel();
                }
            }

        }
    };

    public MyToast(Context context) {
        mContext = context;
        currDuration=DEFAULT;
        mHandler = new Handler(mContext.getMainLooper());
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
    }

    public void setText(String text) {
        mToast.setText(text);
    }

    public void show(int duration) {
        this.duration = duration;
        mHandler.post(mToastThread);
    }

    public void setGravity(int gravity, int xOffset, int yOffset){
        mToast.setGravity(gravity, xOffset, yOffset);
    }

    public void setDuration(int duration){
        mToast.setDuration(duration);
    }

    public void setView(View view){
        mToast.setView(view);
    }

    public void cancel( ) {
        mHandler.removeCallbacks(mToastThread);// 先把显示线程删除
        mToast.cancel();// 把最后一个线程的显示效果cancel掉，就一了百了了
        currDuration = DEFAULT;
    }


}
