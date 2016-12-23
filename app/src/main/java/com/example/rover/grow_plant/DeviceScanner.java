package com.example.rover.grow_plant;


import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.example.rover.grow_plant.IbeaconClass.iBeacon;
import com.example.rover.grow_plant.BluetoothLeClass.OnDataAvailableListener;
import com.example.rover.grow_plant.BluetoothLeClass.OnServiceDiscoverListener;

/**
 * Created by rover on 2016/12/23.
 */

public class DeviceScanner extends ListActivity {

    private final static String TAG = "DeviceScanActivity";// DeviceScanActivity.class.getSimpleName();
    public static final int CONNECT_EVENT = 0x000001;
    private final static int REQUEST_CODE = 1;

    private final static String BLE_DEVICE_NAME = "AmoRgbLight"; 	// 设备名称
    boolean AutoConectFlag = true;  								// 是否自动连接


    static BluetoothGattCharacteristic mBluetoothGattCharacteristic = null;


    private BlueDeviceListAdapter mLeDeviceListAdapter = null;
    // 搜索BLE终端
    private BluetoothAdapter mBluetoothAdapter;
    // 读写BLE终端
    static private BluetoothLeClass mBLE;
    public String bluetoothAddress;
    static private byte writeValue_char1 = 0;
    private boolean mScanning;
    private Handler mHandler = null;
//	private MyThread mythread = null;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 100000;

    iBeacon rgb_light_device = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.listitem_device);
        // getActionBar().setTitle(R.string.title_devices);

        getActionBar().setTitle("正在扫描设备....");
        // findViewById(R.id.button_new_encoder).setOnClickListener(this);

        // Use this check to determine whether BLE is supported on the device.
        // Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
//                    .show();
            finish();
        } else {
            Log.i(TAG, "initialize Bluetooth, has BLE system");
        }

        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
//            Toast.makeText(this, R.string.error_bluetooth_not_supported,
//                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.i(TAG, "mBluetoothAdapter = " + mBluetoothAdapter);
        }

        mBluetoothAdapter.enable();
        Log.i(TAG, "mBluetoothAdapter.enable");

        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
        Log.i(TAG, "mBLE = e" + mBLE);

        // 发现BLE终端的Service时回�?
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);

        // 收到BLE终端数据交互的事�?
        mBLE.setOnDataAvailableListener(mOnDataAvailable);


        // 操作
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == CONNECT_EVENT) {
                    if(rgb_light_device != null)
                    {
                        if (mScanning) {
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            mScanning = false;
                        }

                        boolean bRet = mBLE.connect(rgb_light_device.bluetoothAddress);
                        Log.i(TAG, "connect bRet = " + bRet);
                    }
                }
                super.handleMessage(msg);
            }
        };
    }

    static public void WriteCharX(
            BluetoothGattCharacteristic GattCharacteristic, byte[] writeValue) {
        Log.i(TAG, "writeCharX = " + GattCharacteristic);
        if (GattCharacteristic != null) {
            GattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            GattCharacteristic.setValue(writeValue);
            mBLE.writeCharacteristic(GattCharacteristic);
        }
    }

    static public void ReadCharX(BluetoothGattCharacteristic GattCharacteristic) {
        Log.i(TAG, "GattCharacteristic = " + GattCharacteristic);
        if (GattCharacteristic != null) {
            mBLE.readCharacteristic(GattCharacteristic);
        }
    };

    static public void setCharacteristicNotification(
            BluetoothGattCharacteristic gattCharacteristic, boolean enabled) {
        Log.i(TAG, "gattCharacteristic = " + gattCharacteristic);
        if (gattCharacteristic != null) {
            mBLE.setCharacteristicNotification(gattCharacteristic, enabled);
        }
    };

//	public class MyThread extends Thread {
//		public void run() {
//			while (!Thread.currentThread().isInterrupted()) {
//
//				Message msg = new Message();
//				msg.what = REFRESH;
//				mHandler.sendMessage(msg);
//				try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

    public void DisplayStart() {
        Log.i(TAG, "DisplayStart+++");

//		if (mythread == null) {
//			mythread = new MyThread();
//			mythread.start();
//			// mythread.setThread(true);
//		} else {
//			// mythread.setThread(true);
//		}
    }

    public void DisplayStop() {
//		if (mythread != null) {
//			// mythread.setThread(false);
//			// delay(3000);
//		}
        Log.i(TAG, "DisplayStop---");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "---> onResume");
        super.onResume();
        mBLE.close();
        // Initializes list view adapter.
        mLeDeviceListAdapter = new BlueDeviceListAdapter(this);
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "---> onPause");
        super.onPause();
        // scanLeDevice(false);
        // mLeDeviceListAdapter.clear();
        // mBLE.disconnect();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "---> onStop");
        super.onStop();
        DisplayStop();
        // mBLE.close();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "---> onDestroy");
        super.onDestroy();
        Log.e(TAG, "start onDestroy~~~");
        scanLeDevice(false);
        mBLE.disconnect();
        mBLE.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final iBeacon device = mLeDeviceListAdapter.getDevice(position);
        if (device == null)
            return;

        // 只连接固定设备名的设备
	/*	if(device.name.equals(BLE_DEVICE_NAME))
		{*/
//			if (mScanning) {
//				mBluetoothAdapter.stopLeScan(mLeScanCallback);
//				mScanning = false;
//			}

        Log.i(TAG, "mBluetoothAdapter.enable");
        bluetoothAddress = device.bluetoothAddress;

//			boolean bRet = mBLE.connect(device.bluetoothAddress);
//			Log.i(TAG, "connect bRet = " + bRet);

//        Toast toast = Toast.makeText(getApplicationContext(), "正在连接设备并获取服务中", 1500);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();

        rgb_light_device = device;
        Message msg = new Message();
        msg.what = CONNECT_EVENT;
        mHandler.sendMessage(msg);
		/*}
		else
		{
			Toast toast = Toast.makeText(getApplicationContext(), "该app只可以连接 " + BLE_DEVICE_NAME,
					1500);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}*/
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            // mHandler.postDelayed(new Runnable() {
            // @Override
            // public void run() {
            // mScanning = false;
            // mBluetoothAdapter.stopLeScan(mLeScanCallback);
            // invalidateOptionsMenu();
            // }
            // }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    /**
     * 搜索到BLE终端服务的事�?
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener() {

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }

    };

    /**
     * 收到BLE终端数据交互的事�?
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new OnDataAvailableListener() {
        /**
         * BLE终端数据被读的事�?
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            // 执行 mBLE.readCharacteristic(gattCharacteristic); 后就会收到数�? if
            // (status == BluetoothGatt.GATT_SUCCESS)
            Log.e(TAG,
                    "onCharRead " + gatt.getDevice().getName() + " read "
                            + characteristic.getUuid().toString() + " -> "
                            + Utils.bytesToHexString(characteristic.getValue()));

//			MainActivity.onCharacteristicRead(gatt, characteristic);
        }

        /**
         * 收到BLE终端写入数据回调
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic) {
            Log.e(TAG, "onCharWrite " + gatt.getDevice().getName() + " write "
                    + characteristic.getUuid().toString() + " -> "
                    + new String(characteristic.getValue()));

            // StartActivity.onCharacteristicRead(gatt, characteristic);
        }
    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {

            final iBeacon ibeacon = IbeaconClass.fromScanData(device, rssi,
                    scanRecord);

            if(AutoConectFlag == true)
            {
                if(ibeacon.name.equals(BLE_DEVICE_NAME))
                {
                    rgb_light_device = ibeacon;
                    Message msg = new Message();
                    msg.what = CONNECT_EVENT;
                    mHandler.sendMessage(msg);
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(ibeacon);
                    mLeDeviceListAdapter.notifyDataSetChanged();

                    if(mScanning == true){
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                    }
                }
            });

            // rssi
            Log.i(TAG, "rssi = " + rssi);
            Log.i(TAG, "mac = " + device.getAddress());
            Log.i(TAG, "scanRecord.length = " + scanRecord.length);
        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;

        for (BluetoothGattService gattService : gattServices) {
            // -----Service的字段信�?----//
            int type = gattService.getType();
            Log.e(TAG, "-->service type:" + Utils.getServiceType(type));
            Log.e(TAG, "-->includedServices size:"
                    + gattService.getIncludedServices().size());
            Log.e(TAG, "-->service uuid:" + gattService.getUuid());

            // -----Characteristics的字段信�?----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.e(TAG, "---->char uuid:" + gattCharacteristic.getUuid());

                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG,
                        "---->char permission:"
                                + Utils.getCharPermission(permission));

                int property = gattCharacteristic.getProperties();
                Log.e(TAG,
                        "---->char property:"
                                + Utils.getCharPropertie(property));

                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Log.e(TAG, "---->char value:" + new String(data));
                }

                Log.e(TAG, "UUId="+ gattCharacteristic.getUuid().toString());
                mBluetoothGattCharacteristic = gattCharacteristic;

                // -----Descriptors的字段信�?----//
                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic
                        .getDescriptors();
                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                    int descPermission = gattDescriptor.getPermissions();
                    Log.e(TAG,
                            "-------->desc permission:"
                                    + Utils.getDescPermission(descPermission));

                    byte[] desData = gattDescriptor.getValue();
                    if (desData != null && desData.length > 0) {
                        Log.e(TAG, "-------->desc value:" + new String(desData));
                    }
                }
            }
        }//

//        Intent intent = new Intent();
//        intent.setClass(DeviceScanActivity.this, RgbActivity.class);
//        intent.putExtra("mac_addr", bluetoothAddress);
//        startActivityForResult(intent, REQUEST_CODE);
    }

}
