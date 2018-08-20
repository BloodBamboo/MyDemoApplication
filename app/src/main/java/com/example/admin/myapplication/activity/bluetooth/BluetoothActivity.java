package com.example.admin.myapplication.activity.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.adapter.BluetoothAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BluetoothActivity extends AppCompatActivity{

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "bluetooth";

    @BindView(R.id.lianjiezhuangtai)
    public TextView lianjiezhuangtai;
    @BindView(R.id.recycle)
    public RecyclerView recycle;

    android.bluetooth.BluetoothAdapter bluetoothBluetoothAdapter;

    List<BluetoothDevice> deviceList = new ArrayList<>();
    MyLeScanCallback callback;
    ScanCallback scanCallback;
    BluetoothAdapter bluetoothAdapter;

    BluetoothDevice contentDevice;
    BluetoothGatt bluetoothGatt;

    private BluetoothGattCallback gattcallback = new BluetoothGattCallback(){
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String status;
                    switch (newState) {
                        //已经连接
                        case BluetoothGatt.STATE_CONNECTED:
                            lianjiezhuangtai.setText("已连接");

                            //该方法用于获取设备的服务，寻找服务
                            bluetoothGatt.discoverServices();
                            break;
                        //正在连接
                        case BluetoothGatt.STATE_CONNECTING:
                            lianjiezhuangtai.setText("正在连接");
                            break;
                        //连接断开
                        case BluetoothGatt.STATE_DISCONNECTED:
                            lianjiezhuangtai.setText("已断开");
                            break;
                        //正在断开
                        case BluetoothGatt.STATE_DISCONNECTING:
                            lianjiezhuangtai.setText("断开中");
                            break;
                    }
                    //pd.dismiss();
                }
            });
        }
    };


    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.BLUETOOTH})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        //蓝牙管理，这是系统服务可以通过getSystemService(BLUETOOTH_SERVICE)的方法获取实例
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        //通过蓝牙管理实例获取适配器，然后通过扫描方法（scan）获取设备(device)
        bluetoothBluetoothAdapter = bluetoothManager.getAdapter();
        //开始扫描前开启蓝牙
        if(!bluetoothBluetoothAdapter.enable()) {
            Intent turn_on = new Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turn_on, 0);
        }
        callback = new MyLeScanCallback();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }

        bluetoothAdapter = new BluetoothAdapter();
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(bluetoothAdapter);
        bluetoothAdapter.setListener((View v, int pos) -> {
            contentDevice = deviceList.get(pos);
            bluetoothGatt = contentDevice.connectGatt(BluetoothActivity.this, false, gattcallback);
            lianjiezhuangtai.setText("连接" + contentDevice.getName() + "中...");
        });
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = result.getDevice();
                if (!deviceList.contains(device)) {
                    //将设备加入列表数据中
                    deviceList.add(device);
                    bluetoothAdapter.list = deviceList;
                    bluetoothAdapter.notifyDataSetChanged();
                }
                Log.d(TAG, "Device name: " + device.getName());
                Log.d(TAG, "Device address: " + device.getAddress());
                Log.d(TAG, "Device service UUIDs: " + device.getUuids());
                ScanRecord record = result.getScanRecord();
                Log.d(TAG, "Record advertise flags: 0x" + Integer.toHexString(record.getAdvertiseFlags()));
                Log.d(TAG, "Record Tx power level: " + record.getTxPowerLevel());
                Log.d(TAG, "Record device name: " + record.getDeviceName());
                Log.d(TAG, "Record service UUIDs: " + record.getServiceUuids());
                Log.d(TAG, "Record service data: " + record.getServiceData());
            }

//            @Override
//            public void onBatchScanResults(List<ScanResult> results) {
//                super.onBatchScanResults(results);
//                for (ScanResult result : results) {
//                    BluetoothDevice device = result.getDevice();
//                    if (!deviceList.contains(device)) {
//                        //将设备加入列表数据中
//                        deviceList.add(device);
//                    }
//                    Log.d(TAG, "BatchScan Device name: " + device.getName());
//                    Log.d(TAG, "BatchScan Device address: " + device.getAddress());
//                    Log.d(TAG, "BatchScan Device service UUIDs: " + device.getUuids());
//                    ScanRecord record = result.getScanRecord();
//                    Log.d(TAG, "BatchScan Record advertise flags: 0x" + Integer.toHexString(record.getAdvertiseFlags()));
//                    Log.d(TAG, "BatchScan Record Tx power level: " + record.getTxPowerLevel());
//                    Log.d(TAG, "BatchScan Record device name: " + record.getDeviceName());
//                    Log.d(TAG, "BatchScan Record service UUIDs: " + record.getServiceUuids());
//                    Log.d(TAG, "BatchScan Record service data: " + record.getServiceData());
//                }
//
//            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }

    @OnClick(R.id.saomiao)
    public void onClicksaomiao() {
        deviceList.clear();
//        bluetoothBluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
        bluetoothBluetoothAdapter. startLeScan(callback);
    }
    @OnClick(R.id.stop)
    public void onClickstop() {
//        bluetoothBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        bluetoothBluetoothAdapter.stopLeScan(callback);
    }

    @OnClick(R.id.bin_content)
    public void onClicklianbin_content() {

    }


    class MyLeScanCallback implements android.bluetooth.BluetoothAdapter.LeScanCallback{

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d("TAG", "onLeScan: " + device.getName() + " /t " + device.getAddress() + " /t " + device.getBondState() +" /t uuid :" + device.getUuids() + " /t " );

            //重复过滤方法，列表中包含不该设备才加入列表中，并刷新列表
            if (!deviceList.contains(device)) {
                //将设备加入列表数据中
                deviceList.add(device);
                bluetoothAdapter.list = deviceList;
                bluetoothAdapter.notifyDataSetChanged();
            }
        }
    }



}
