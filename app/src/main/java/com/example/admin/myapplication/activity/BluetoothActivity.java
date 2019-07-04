package com.example.admin.myapplication.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.ByteUtils;
import com.example.admin.myapplication.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 10000;
    private static final String TAG = "BluetoothActivity";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    int SCAN_PERIOD = 10000;
    BluetoothManager bluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mBluetoothDevice;
    Handler mHandler = new Handler();
    boolean mScanning;
    DeviceListAdapter adapter, adapter2;
    String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    StringBuilder statusPackRecv;

    /**
     * 连接蓝牙设备 重连等操作
     */
    private BluetoothGatt mBluetoothGatt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        registerBleListenerReceiver();
        findViewById(R.id.button).setOnClickListener(v -> {
            scanDevice(!mScanning);
        });
        adapter = new DeviceListAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mBluetoothDevice = (BluetoothDevice) adapter.getItem(position);
                if (mBluetoothDevice == null) {
                    ToastUtil.showShort(getBaseContext(), "设备信息没有获取到" + position);
                }
                Log.e(TAG, "连接设备地址为：" + mBluetoothDevice.getAddress());
                mBluetoothGatt = mBluetoothDevice.connectGatt(getBaseContext(), false, bluetoothGattCallback);
            }
        });

        getBluetoothAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        permissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mReceiver);
            unregisterReceiver(bleListenerReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mBluetoothAdapter.disable();
        releaseResource();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "蓝牙没有打开", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getBluetoothAdapter() {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the mBluetoothDevice.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "没有蓝牙模块", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        if (mBluetoothAdapter.getBondedDevices().isEmpty()) {
            return;
        }

        getBoundDevices();
    }

    /**
     * 获取绑定蓝牙设备
     */
    private void getBoundDevices() {
        adapter2 = new DeviceListAdapter();
        List<BluetoothDevice> list = new ArrayList<>(mBluetoothAdapter.getBondedDevices());
        adapter2.setNewData(list);
        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mBluetoothDevice = adapter2.getItem(position);
                Log.e(TAG, "连接设备地址为：" + mBluetoothDevice.getAddress());
                mBluetoothGatt = mBluetoothDevice.connectGatt(getBaseContext(), false, bluetoothGattCallback);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycle_view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter2);
    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            Toast.makeText(this, "开始搜索", Toast.LENGTH_SHORT).show();
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BluetoothActivity.this, "关闭搜索" + adapter.getData().size(), Toast.LENGTH_SHORT).show();
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Toast.makeText(this, "关闭搜索" + adapter.getData().size(), Toast.LENGTH_SHORT).show();
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void scanNewLeDevice(final boolean enable) {
        if (enable) {
            Toast.makeText(this, "开始搜索", Toast.LENGTH_SHORT).show();
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BluetoothActivity.this, "关闭搜索" + adapter.getData().size(), Toast.LENGTH_SHORT).show();
                    mScanning = false;
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
        } else {
            Toast.makeText(this, "关闭搜索" + adapter.getData().size(), Toast.LENGTH_SHORT).show();
            mScanning = false;
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        }
    }

    private void scanDevice(final boolean enable) {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        if (enable) {
            Toast.makeText(this, "开始搜索", Toast.LENGTH_SHORT).show();
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BluetoothActivity.this, "关闭搜索" + adapter.getData().size(), Toast.LENGTH_SHORT).show();
                    mScanning = false;
                    mBluetoothAdapter.cancelDiscovery();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startDiscovery();
        } else {
            Toast.makeText(this, "关闭搜索" + adapter.getData().size(), Toast.LENGTH_SHORT).show();
            mScanning = false;
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!adapter.getData().contains(device)) {
                                adapter.addData(device);
                                Log.i(TAG, "scan--" + device.getName());
                            }
                        }
                    });
                }
            };


    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (!adapter.getData().contains(result.getDevice())) {
                adapter.addData(result.getDevice());
                Log.i(TAG, "scan--" + result.getDevice().getName());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            ToastUtil.showShort(getBaseContext(), "搜索失败");
        }
    };


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
// When discovery finds a mBluetoothDevice
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
// Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
// Add the name and address to an array adapter to show in a ListView
                if (!adapter.getData().contains(device)) {
                    adapter.addData(device);
                    Log.i(TAG, "scan--" + device.getName());
                }
            }
        }
    };

    public class DeviceListAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

        public DeviceListAdapter() {
            super(R.layout.layout_device_item, null);
        }

        @Override
        protected void convert(BaseViewHolder helper, BluetoothDevice item) {
            helper.setText(R.id.text, item.getName() + "/" + item.getAddress());
        }
    }


    private void permissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "checkSelfPermission");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.i(TAG, "shouldShowRequestPermissionRationale");
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this,
                        permissions,
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

            } else {
                Log.i(TAG, "requestPermissions");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        permissions,
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                // MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i(TAG, "onRequestPermissionsResult denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showWaringDialog();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                        finish();
                    }
                }).show();
    }

    /**
     * 蓝牙连接成功回调
     */

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        //不要执行耗时操作
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.e(TAG, "gatt: " + gatt.getServices().toString());
            if (newState == BluetoothProfile.STATE_CONNECTED) {//连接成功
                Log.e(TAG, "onConnectionStateChange 蓝牙连接");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.e(TAG, "onConnectionStateChange 蓝牙断连");
                //关闭当前新的连接
                gatt.close();
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {//以上的方法gatt.discoverServices();才会回调
            super.onServicesDiscovered(gatt, status);
            Log.e(TAG, "gatt: " + gatt.getServices().toString());

            for (BluetoothGattService service : gatt.getServices()) {
                Log.e(TAG, "gatt: " + service.getUuid() + "---IncludedServices: " + service.getIncludedServices()+
                        "--------Characteristics: "+service.getCharacteristics() + "---Characteristic: "
//                        + service.getCharacteristic((UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")))
                );
            }

            //回调之后，设备之间才真正通信连接起来
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));


                    for (BluetoothGattCharacteristic bluetoothGattService :service.getCharacteristics()) {
                        int charaProp = bluetoothGattService.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                             Log.e("nihao","gattCharacteristic的UUID为:"+bluetoothGattService.getUuid());
                             Log.e("nihao","gattCharacteristic的属性为:  可读");
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                             Log.e("nihao","gattCharacteristic的UUID为:"+bluetoothGattService.getUuid());
                             Log.e("nihao","gattCharacteristic的属性为:  可写");
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                             Log.e("nihao","gattCharacteristic的UUID为:"+bluetoothGattService.getUuid()+bluetoothGattService);
                             Log.e("nihao","gattCharacteristic的属性为:  具备通知属性");
                        }
                    }
                BluetoothGattCharacteristic characteristic1 = service.getCharacteristic(UUID.fromString("0000ffe3-0000-1000-8000-00805f9b34fb"));
                for(BluetoothGattDescriptor dp: characteristic1.getDescriptors()){
                    if (dp != null) {
//                                if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                        dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                                } else if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
//                                    dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
//                                }
                        mBluetoothGatt.writeDescriptor(dp);
                    }
                }
                boolean isNotification = mBluetoothGatt.setCharacteristicNotification(characteristic1, true);
                Log.e(TAG, "isNotification 蓝牙正常===" + isNotification);
                //                if (isNotification) {
//                List<BluetoothGattDescriptor>list = characteristic.getDescriptors();
//                if (list != null) {
//                    Log.e(TAG, "BluetoothGattDescriptor 信息个数===" + list.size() + "---"+list.get(0).getUuid());
//// 来源：http://stackoverflow.com/questions/38045294/oncharacteristicchanged-not-called-with-ble

//                }
//                }

                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .subscribe(next -> {
                            BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                            byte[] data = new byte[1];
                            data[0] = 0x18;
                            characteristic.setValue(data);
                            boolean is = mBluetoothGatt.writeCharacteristic(characteristic);//执行之后，会执行下面的onCharacteristicRead的回调方法
                            Log.e(TAG, " 蓝牙写入数据===" + is);
                        });
            } else {
                Log.e(TAG, " 蓝牙连接失败");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.e(TAG, "callback characteristic read status " + status
                    + " in thread " + Thread.currentThread());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "read value: " + characteristic.getValue());
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.e(TAG, "callback characteristic write status " + status
                    + " in thread " + Thread.currentThread());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "write value: " + characteristic.getValue());

            }
        }

        //设备发出通知时会调用到该接口
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.e(TAG, "DevicesLink kevin DeviceInfoBroadcast:" + characteristic.getValue());
            a(characteristic.getValue());
        }
    };

    /**
     * 显示监听状态变化
     *
     * @param intent
     */
    private void showBleStateChange(Intent intent) {
        String action = intent.getAction();
        //连接的设备信息
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.e(TAG, "蓝牙监听广播…………………………" + action);

        if (mBluetoothDevice != null && mBluetoothDevice.equals(device)) {
            Log.e(TAG, "收到广播-->是当前连接的蓝牙设备");

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.e(TAG, "广播 蓝牙已经连接");

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.e(TAG, "广播 蓝牙断开连接");
                releaseResource();
            }
        } else {
            Log.e(TAG, "收到广播-->不是当前连接的蓝牙设备");
        }
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.e(TAG, "STATE_OFF 蓝牙关闭");
//                    adapter.clear();
                    releaseResource();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.e(TAG, "STATE_TURNING_OFF 蓝牙正在关闭");
                    //停止蓝牙扫描
                    scanLeDevice(false);
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d(TAG, "STATE_ON 蓝牙开启");
                    //扫描蓝牙设备
                    scanLeDevice(true);
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.e(TAG, "STATE_TURNING_ON 蓝牙正在开启");
                    break;
            }
        }
    }

    /**
     * 蓝牙监听广播接受者
     */
    private BroadcastReceiver bleListenerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showBleStateChange(intent);
        }
    };

    /**
     * 注册蓝牙监听广播
     */
    private void registerBleListenerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bleListenerReceiver, intentFilter);
    }

    /**
     * 释放资源
     */

    private void releaseResource() {
        Log.e(TAG, "断开蓝牙连接，释放资源");
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }


    public static boolean enableNotification(BluetoothGatt bluetoothGatt, boolean enable, BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null || characteristic == null) {
            return false;
        }
        if (!bluetoothGatt.setCharacteristicNotification(characteristic, enable)) {
            return false;
        }
        //获取到Notify当中的Descriptor通道  然后再进行注册
        BluetoothGattDescriptor clientConfig = characteristic.getDescriptor(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
        if (clientConfig == null) {
            return false;
        }
        if (enable) {
            clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        return bluetoothGatt.writeDescriptor(clientConfig);
    }

    private void a(byte[] str) {
        Log.i("PCR", str+"=========DevicesLink kevin DeviceInfoBroadcast:" + ByteUtils.byteToString(str));
        if (statusPackRecv == null) {
            statusPackRecv = new StringBuilder();
        }
        statusPackRecv.append(ByteUtils.byteToString(str));
        Log.i("PCR" ,"DevicesLink kevin:"+statusPackRecv.length());
        if(statusPackRecv.length()>=186*2){/*sizeof(sensorStatusFromDevice)*/
            Log.i("PCR" ,"DevicesLink kevin:"+statusPackRecv.toString());
            statusPackRecv = null;
        }
    }
}
