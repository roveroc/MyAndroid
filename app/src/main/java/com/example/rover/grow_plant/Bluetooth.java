package com.example.rover.grow_plant;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * setLeScanCallback方法:扫描蓝牙设备,监听器返回蓝牙设备
 * connectDevice方法:连接蓝牙设备,监听器返回BluetoothService集合
 * getData方法:获取数据,监听器返回数据
 * Created by xudongjian on 16/11/30.
 */

public class Bluetooth {

    private Context mContext;//上下文

    private BluetoothManager mBluetoothManager;//蓝牙管理员

    private BluetoothAdapter mBluetoothAdapter;//蓝牙控制器

    private BluetoothGatt mBluetoothGatt;//用于控制蓝牙设备

    private GetBluetoothGattService getBluetoothGattService;//用于向调用端返回BluetoothService集合

    private OnDataChangeListener onDataChangeListener;//用于向调用端返回数据

    private Map<String,Object> map_handler;

    //蓝牙设备的Service的UUID
    private static UUID UUID_SERVICE;
    //蓝牙设备的Characteristic的UUID
    private static UUID UUID_CHARACTERISTIC;

    //用于接收callback的各种数据
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<String,Object> map= (Map<String, Object>) msg.obj;
            Iterator iterator=map.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry= (Map.Entry) iterator.next();
                if(entry.getKey().equals("services")){//message中
                    getBluetoothGattService.bluetoothGattService((List<BluetoothGattService>)entry.getValue());
                }else if (entry.getKey().equals("data")){
                    onDataChangeListener.onDataChange((String) entry.getValue());
                }else if (entry.getKey().equals("state")){
                    if(onDataChangeListener!=null)onDataChangeListener.onStateChange((String) entry.getValue());
                }
            }
        }
    };

    /*
    返回蓝牙设备的状态和数据
    注意:方法体不在主线程,请勿进行更新UI的操作
     */
    private BluetoothGattCallback callback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            Message message=new Message();


            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    mBluetoothGatt.discoverServices();
                    map_handler.put("state","连接成功");
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    map_handler.put("state","断开连接");
                }
                message.obj= map_handler;
                handler.sendMessage(message);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (UUID_SERVICE == null && UUID_CHARACTERISTIC == null) {
                Message message = new Message();
                map_handler.put("services",mBluetoothGatt.getServices());
                message.obj = map_handler;
                handler.sendMessage(message);
            } else {
                BluetoothGattCharacteristic characteristic = gatt.getService(UUID_SERVICE).getCharacteristic(UUID_CHARACTERISTIC);
                mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                mBluetoothGatt.readCharacteristic(characteristic);

            }

        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Message message = new Message();
            map_handler.put("data", new String(characteristic.getValue()));
            message.obj =map_handler;
            handler.sendMessage(message);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Message message = new Message();
            map_handler.put("data",new String(characteristic.getValue()));
            message.obj = map_handler;
            handler.sendMessage(message);
        }
    };

    /**
     * 构造函数
     * @param context
     */
    public Bluetooth(Context context) {
        mContext = context;

        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        map_handler=new HashMap<>();


    }

    /**
     * 扫描蓝牙设备
     * @param leScanCallback
     */
    public void setLeScanCallback(BluetoothAdapter.LeScanCallback leScanCallback) {
        mBluetoothAdapter.startLeScan(leScanCallback);
    }

    /**
     * 连接设备
     * @param bluetoothAddress 设备的蓝牙地址
     * @param getBluetoothGattService 返回BluetoothService集合监听器
     */
    public void connectDevice(String bluetoothAddress, GetBluetoothGattService getBluetoothGattService) {
        this.getBluetoothGattService = getBluetoothGattService;
        BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
        mBluetoothGatt = bluetoothDevice.connectGatt(mContext, true, callback);
    }

    /*
    返回BluetoothService集合监听器
     */
    public interface GetBluetoothGattService {
        /**
         * @param list BluetoothService集合
         */
        void bluetoothGattService(List<BluetoothGattService> list);
    }


    /**
     * 获取数据,必须先调用connectDevice这个方法,或者使用带蓝牙地址参数的那个方法
     * @param UUID_service 需要获取的数据的Service
     * @param UUID_characteristic 需要获取数据的Characteristic
     * @param onDataChangeListener 数据变化监听器
     */
    public void getData(String UUID_service, String UUID_characteristic, OnDataChangeListener onDataChangeListener) {

        UUID_SERVICE = UUID.fromString(UUID_service);
        UUID_CHARACTERISTIC = UUID.fromString(UUID_characteristic);

        BluetoothGattCharacteristic characteristic = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_CHARACTERISTIC);

        mBluetoothGatt.setCharacteristicNotification(characteristic, true);
        mBluetoothGatt.readCharacteristic(characteristic);

        this.onDataChangeListener = onDataChangeListener;

    }

    /**
     * 获取数据
     * @param bluetoothAddress 蓝牙地址
     * @param UUID_service 需要获取数据的Characteristic的Service
     * @param UUID_characteristic 需要获取数据的Characteristic
     * @param onDataChangeListener 数据变化监听器
     */
    public void getData(String bluetoothAddress, String UUID_service, String UUID_characteristic, OnDataChangeListener onDataChangeListener) {

        UUID_SERVICE = UUID.fromString(UUID_service);
        UUID_CHARACTERISTIC = UUID.fromString(UUID_characteristic);

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(bluetoothAddress);

        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }

        mBluetoothGatt = device.connectGatt(mContext, true, callback);

        this.onDataChangeListener = onDataChangeListener;

    }


    public void writeValueToDevice(BluetoothGattCharacteristic gattCharacteristic){
        mBluetoothGatt.writeCharacteristic(gattCharacteristic);
    }



    /*
    数据变化监听器
     */
    public interface OnDataChangeListener {
        /**
         * 当数据变化时返回
         * @param data 数据
         */
        void onDataChange(String data);

        /**
         * 当状态变化时返回
         * @param state 状态
         */
        void onStateChange(String state);
    }
}
