package com.example.rover.grow_plant;

import android.content.Intent;
import android.os.Bundle;
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
import android.view.MotionEvent;
import android.graphics.drawable.ColorDrawable;

import java.util.ArrayList;
import java.util.Arrays;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.widget.Toast;
import android.bluetooth.BluetoothSocket;

import java.util.List;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by rover on 2016/12/20.
 */

public class Test_learn_Something extends AppCompatActivity implements ColorPickerView.OnColorChangedListener {

    private static final String MY_BROADCAST_TAG = "com.example.localbroadcasttest";
    private MyReceiver mRecevier;

    private ColorPickerView colorView;
    private ImageView red, yellow, green, sky, blue,perple;

    private SeekBar         lightBar;
    private TextView        lightBarValueText;

    private SeekBar         speedBar;
    private TextView        speedBarValueText;

    private LoopView        mLoopView;              //模式选择滚轮

    private ImageView       switchImageView;        //开关
    private ImageView       playPauseImageView;     //播放暂停
    private Button          customBtn;              //自定义
    private Button          searchDevice;           //搜索设备

    private boolean         switchFlag;
    private boolean         playFlag;

    private PopupWindow     popupWindow;
    private ListView        deviceListView;

    int                     modeValue = 0;
    int                     lightValue = 100;
    int                     speedValue = 100;

    private MyToast         myToast;

    boolean                 isConnect = false;  //蓝牙是否连接


    ArrayList<String> list = new ArrayList<String>();
    String [] colors = new String[]{
            "红色输出","绿色输出", "蓝色输出","黄色输出","紫色输出","青色输出","白色输出",
            "三色跳变","七色跳变", "三色渐变","七色渐变","红色频闪","绿色频闪","蓝色频闪",
            "黄色频闪","紫色频闪","青色频闪","白色频闪","红蓝交替渐变","蓝绿交替渐变","红绿交替渐变",
            "红色爆闪","绿色爆闪","蓝色爆闪","黄色爆闪","紫色爆闪","青色爆闪","白色爆闪",
            "三色爆闪","七色爆闪","三色闪变","七色闪变","红色呼吸渐变","绿色呼吸渐变","蓝色呼吸渐变","黄红色呼吸渐变",
            "红紫呼吸渐变","青色呼吸渐变","白色呼吸渐变","三色呼吸渐变"
    };

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
            Log.i("state 11===11 ","state" + state);
            if(state.equals("连接成功")){
                myToast.cancel();
                isConnect = true;
                Toast.makeText(Test_learn_Something.this, "连接成功", Toast.LENGTH_SHORT).show();
            }else{
                myToast.cancel();
                Toast.makeText(Test_learn_Something.this, "连接失败", Toast.LENGTH_SHORT).show();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_learn_something);


        context = getApplicationContext();
        receiver = new MyReceiver();
        filter = new IntentFilter();
        filter.addAction(MY_BROADCAST_TAG);

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

//        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//        startActivity(discoverableIntent);

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

                    sendData((byte) 1,(byte) mRed,(byte) mGreen,(byte) mBlue);
                }
            }
        });


        //常用六中颜色
        red    = (ImageView) findViewById(R.id.red);
        yellow = (ImageView) findViewById(R.id.yellow);
        green  = (ImageView) findViewById(R.id.green);
        sky    = (ImageView) findViewById(R.id.sky);
        blue   = (ImageView) findViewById(R.id.blue);
        perple = (ImageView) findViewById(R.id.perple);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendData((byte) 1,(byte) 255,(byte) 0,(byte) 0);
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
				sendData((byte) 1,(byte) 255,(byte) 255,(byte) 0);
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendData((byte) 1,(byte) 0,(byte) 255,(byte) 0);
            }
        });
        sky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendData((byte) 1,(byte) 0,(byte) 255,(byte) 255);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendData((byte) 1, (byte) 0, (byte) 0, (byte) 255);
            }
        });
        perple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendData((byte) 1,(byte) 255,(byte) 0,(byte) 255);
            }
        });


        //模式选择
        mLoopView = (LoopView)findViewById(R.id.looper_view);
        for(int i=0; i<colors.length; i++){
            list.add(colors[i]);
        }
        mLoopView.setItems(list);
        //设置初始位置
        mLoopView.setInitPosition(0);
        //设置字体大小
        mLoopView.setTextSize(20);

        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.i("选中的模式索引"," == " + index);
                modeValue = index;
                sendData((byte)2,(byte)modeValue,(byte)lightValue,(byte)speedValue);
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
                lightValue = i;
                sendData((byte)2,(byte)modeValue,(byte)lightValue,(byte)speedValue);
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
                speedValue = 100-i;
                sendData((byte)2,(byte)modeValue,(byte)lightValue,(byte)speedValue);
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

                    sendData((byte) 4,(byte) 3,(byte) 0,(byte) 0);

                }else{
                    switchImageView.setBackgroundResource(R.mipmap.on);
                    switchFlag = true;

                    sendData((byte) 4,(byte) 2,(byte) 0,(byte) 0);

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
                    sendData((byte)3,(byte)2,(byte)0,(byte)0);
                }else{
                    playPauseImageView.setBackgroundResource(R.mipmap.play);
                    playFlag = true;
                    sendData((byte)3,(byte)3,(byte)0,(byte)0);
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


        //搜索设备
        searchDevice = (Button)findViewById(R.id.searchDevice);
        searchDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnect){
                    Toast.makeText(Test_learn_Something.this, "已经连接成功", Toast.LENGTH_SHORT).show();
                }else {
                    //搜索蓝牙设备
                    //如果正在搜索，先取消
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    bluetoothAdapter.startDiscovery();

                    getPopupWindow();
                    popupWindow.showAtLocation(Test_learn_Something.this.colorView, Gravity.CENTER, 0, 0);
                    deviceListView.setAdapter(adapter);
                }
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
                if(i == 0){

                }else {
                    BluetoothDevice device = (BluetoothDevice) deviceArray.get(i - 1);
                    if(device.getName().equals("AwiseLight")) {
                        popupWindow.dismiss();
                        bluetoothAdapter.cancelDiscovery();

                        bluetooth = new Bluetooth(Test_learn_Something.this);
                        bluetooth.connectDevice(device.getAddress(), getBluetoothGattService);


                        View view1 = getLayoutInflater().inflate(R.layout.mytoast_layout, null);
                        View v = view1.findViewById(R.id.toast_backview);//找到你要设透明背景的layout 的id
                        v.getBackground().setAlpha(50);//0~255透明度值
                        myToast = new MyToast(Test_learn_Something.this);
                        myToast.setText("连接设备中...");//设置要显示的内容
                        myToast.setView(view1);
                        myToast.show(10000);
                    }else{
                        Toast.makeText(Test_learn_Something.this, "请连接AwiseLight蓝牙设备", Toast.LENGTH_SHORT).show();
                    }
                }
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
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    deviceArray.add(device);
                    adapter.add(device.getName());
                    Log.i("Devices: " ,device.getName());
                    Log.i("Devices Mac: ", device.getAddress());
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) { //搜索完成
                setProgressBarIndeterminateVisibility(false);
//                Toast.makeText(Test_learn_Something.this, "搜索完成", Toast.LENGTH_SHORT).show();

            }
        }
    };


    private Context context;
    private IntentFilter filter;

    @Override
    protected void onResume() {
        super.onResume();
//        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);

        mRecevier = new MyReceiver();
        registerReceiver(mRecevier, new IntentFilter(MY_BROADCAST_TAG));
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if(arg1.getAction().equals(MY_BROADCAST_TAG)) {
                int r = arg1.getIntExtra("r", 255);
                int g = arg1.getIntExtra("g", 255);
                int b = arg1.getIntExtra("b", 255);

                Log.i("接受到广播", " = " + r + g + b);

                sendData((byte) 1, (byte) r, (byte) g, (byte) b);
            }else{
                String action = arg1.getAction();
                // 获得已经搜索到的蓝牙设备
                if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
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
                    Log.i("fuck: " ,"here");
//                    Toast.makeText(Test_learn_Something.this, "搜索完成", Toast.LENGTH_SHORT).show();
//                    bluetoothAdapter.cancelDiscovery();
                }
            }
        }
    }


    //发送数据
    public void sendData(byte v1,byte v2,byte v3,byte v4){
        if(gattCharacteristic != null) {
            byte[] params = new byte[4];
            params[0] = v1;
            params[1] = v2;
            params[2] = v3;
            params[3] = v4;
            gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            gattCharacteristic.setValue(params);
            bluetooth.writeValueToDevice(gattCharacteristic);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(mRecevier);
    }

}


