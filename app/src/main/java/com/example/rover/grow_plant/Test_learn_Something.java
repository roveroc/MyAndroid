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
import android.widget.PopupWindow;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.graphics.drawable.ColorDrawable;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Handler;
import java.lang.Runnable;
import java.util.Arrays;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.widget.Toast;
import java.io.IOException;
import java.util.UUID;
import android.bluetooth.BluetoothSocket;

import java.util.List;

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


    private PopupWindow     popupWindow;
    private ListView        deviceListView;

    String[] strs = {"正在搜索附近的蓝牙设备..."};
    private ArrayAdapter<String> adapter = null;

    private ArrayList<BluetoothDevice> deviceArray;



    private Bluetooth bluetooth;
    private BluetoothAdapter bluetoothAdapter;    //本地蓝牙适配器
    private BluetoothSocket mmSocket = null;
    private BluetoothDevice mmDevice = null;
    private BluetoothGattCharacteristic gattCharacteristic;  //写数据


    private List<BluetoothGattService> list_service;

    private Bluetooth.GetBluetoothGattService getBluetoothGattService=new Bluetooth.GetBluetoothGattService() {
        @Override
        public void bluetoothGattService(List<BluetoothGattService> list) {
            list_service = list;

            Log.i("server == " ," " + list);

            Log.i("server == " ,"list.cont =  " + list.size());

            for(int i = 0 ;i < list.size() ; i++){
                System.out.println(list.get(i).getUuid());
                if(list.get(i).getUuid().toString().equals("0000fff0-0000-1000-8000-00805f9b34fb")){
                    BluetoothGattService bs = (BluetoothGattService) list_service.get(i);
                    String UUID_Service = bs.getUuid().toString();

                    List<BluetoothGattCharacteristic> clist = bs.getCharacteristics();
                    for(int j = 0 ;j < clist.size() ; j++) {
                        BluetoothGattCharacteristic cc = (BluetoothGattCharacteristic)clist.get(j);
                        Log.i("clist = ", " uuid == " + cc.getUuid());
                        if(cc.getUuid().toString().equals("0000fffa-0000-1000-8000-00805f9b34fb"));{
                            bluetooth.getData("0000fff0-0000-1000-8000-00805f9b34fb","0000fffa-0000-1000-8000-00805f9b34fb",onDataChangeListener);

                            gattCharacteristic = cc;

                        }
                    }
                }
            }
        }
    };

    Bluetooth.OnDataChangeListener onDataChangeListener=new Bluetooth.OnDataChangeListener() {
        @Override
        public void onDataChange(String data) {
            Log.i("state === ","data" + data);
        }

        @Override
        public void onStateChange(String state) {
            Log.i("state === ","state" + state);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_learn_something);

        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(strs));
        adapter = new ArrayAdapter<String>
                (Test_learn_Something.this,android.R.layout.simple_expandable_list_item_1,lst);

        deviceArray = new ArrayList<BluetoothDevice>();

        //ble
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String Address = bluetoothAdapter.getAddress(); //获取本机蓝牙MAC地址
        String Name = bluetoothAdapter.getName();   //获取本机蓝牙名称
        // 若蓝牙没打开
        if(!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();  //打开蓝牙，需要BLUETOOTH_ADMIN权限
        }
        // 打印信息
        Log.i("getAddress() : ", Address);
        Log.i("getName() : ", Name);
        // 注册用以接收到已搜索到的蓝牙设备的receiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        // 注册搜索完时的receiver
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

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
            public void onClick(final View view) {
                if(switchFlag == true){
                    switchImageView.setBackgroundResource(R.mipmap.off);
                    switchFlag = false;

                    //搜索蓝牙设备
                    //如果正在搜索，先取消
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    bluetoothAdapter.startDiscovery();

                    getPopupWindow();
                    popupWindow.showAtLocation(Test_learn_Something.this.colorView, Gravity.CENTER,0,0);
                    deviceListView.setAdapter(adapter);

                }else{
                    switchImageView.setBackgroundResource(R.mipmap.on);
                    switchFlag = true;

                    byte [] params = new byte[4];
                    gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                    gattCharacteristic.setValue(params);
                    bluetooth.writeValueToDevice(gattCharacteristic);
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

    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    /**
     * 获取PopipWinsow实例
     */
    private  void  getPopupWindow(){
        if (null != popupWindow){
            popupWindow.dismiss();
            return;
        }else {
            initPopupWindow();
        }
    }


    /**
     * 创建PopupWindow
     *
     */
    protected  void  initPopupWindow(){

        Log.i("init "," popview");

        final View popipWindow_view = getLayoutInflater().inflate(R.layout.device_pop_view_layout,null,false);
        //创建Popupwindow 实例，200，LayoutParams.MATCH_PARENT 分别是宽高
        popupWindow = new PopupWindow(popipWindow_view,850, 1150,true);

        popipWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popipWindow_view != null && popipWindow_view.isShown()) {

                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //创建ArrayAdapter
        deviceListView = (ListView) popupWindow.getContentView().findViewById(R.id.device_pop_list);
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("点击的行 == ","   " + i);
                popupWindow.dismiss();
                bluetoothAdapter.cancelDiscovery();
                BluetoothDevice device = (BluetoothDevice)deviceArray.get(i-1);

                bluetooth=new Bluetooth(Test_learn_Something.this);
                bluetooth.connectDevice(device.getAddress(),getBluetoothGattService);




//                String UUID_Characteristic=((BluetoothGattCharacteristic)adapter.getChild(groupPosition,childPosition)).getUuid().toString();
//
//                bluetooth.getData(UUID_Service,UUID_Characteristic,onDataChangeListener);

            }
        });
    }




    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //搜索到不是已经绑定的蓝牙设备
                Log.i("fuck: " ,device.getName());
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    deviceArray.add(device);
                    adapter.add(device.getName());
                    Log.i("Devices: " ,device.getName());
                    Log.i("Devices Mac: ", device.getAddress());
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) { //搜索完成
                setProgressBarIndeterminateVisibility(false);
                Toast.makeText(Test_learn_Something.this, "搜索完成", Toast.LENGTH_SHORT).show();
                bluetoothAdapter.cancelDiscovery();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}


