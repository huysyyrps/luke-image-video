package com.example.luke_imagevideo_send.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cunoraz.gifview.library.GifView;
import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.views.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主要负责打开、搜索、显示蓝牙
 */
public class BluetoothListActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.gifView)
    GifView gifView;
    BaseRecyclerAdapter baseRecyclerAdapter;
    List<BluetoothDevice> bluetoothList = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    BaseViewHolder mHolder;
    BluetoothGattService linkLossService;
    BluetoothGattCharacteristic alertLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "该机不支持BLE模式", Toast.LENGTH_SHORT).show();
            finish();
        }

        baseRecyclerAdapter = new BaseRecyclerAdapter<BluetoothDevice>(BluetoothListActivity.this, R.layout.bluetooth_device_name_item, bluetoothList) {
            @Override
            public void convert(BaseViewHolder holder, final BluetoothDevice device) {
                if (bluetoothList.size() == 0) {
                    AlertDialogUtil alertDialogUtil = new AlertDialogUtil(BluetoothListActivity.this);
                    alertDialogUtil.showSmallDialog("暂无可连接蓝牙");
                } else {
                    if (device.getName() == null) {
                        holder.setText(R.id.textView, device.getAddress());
                    } else {
                        holder.setText(R.id.textView, device.getName() + "---" + device.getAddress());
                    }

                    holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mHolder = holder;
                            holder.setVisitionTextView(R.id.text);
                            mBluetoothAdapter.stopLeScan(callback);
                            //停止搜索一般需要一定的时间来完成，最好调用停止搜索函数之后加以100ms的延时，
                            // 保证系统能够完全停止搜索蓝牙设备。停止搜索之后启动连接过程。
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mBluetoothGatt = device.connectGatt(BluetoothListActivity.this, true, gattCallback);
                                }
                            }, 100);
                        }
                    });
                }
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        initBluetooth();

        gifView.setVisibility(View.VISIBLE);
        gifView.setGifResource(R.drawable.bleloading);
//        gifView.getGifResource();
        gifView.play();
//        gifView.pause();
    }

    /**
     * 初始化蓝牙
     */
    private void initBluetooth() {
        // 获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 检查蓝牙是否可用
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        } else {
            // 检查蓝牙是否打开
            if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                //搜索设备
                bluetoothList.clear();
                mBluetoothAdapter.startLeScan(callback);
            } else {
                //搜索设备
                bluetoothList.clear();
                mBluetoothAdapter.startLeScan(callback);
            }
        }
    }

    private BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int arg1, byte[] arg2) {
            Log.e("XXXX", device.getAddress());
            if (bluetoothList.size() == 0) {
                bluetoothList.add(device);
                baseRecyclerAdapter.notifyDataSetChanged();
            } else if (bluetoothList.size() != 0) {
                if (!bluetoothList.contains(device)) {
                    bluetoothList.add(device);
                    baseRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    /**
     * 连接回调
     */
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        //成功连接到设备回调
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            BluetoothDevice dev = gatt.getDevice();
            Log.i("XXX", String.format("onConnectionStateChange:%s,%s,%s,%s", dev.getName(), dev.getAddress(), status, newState));
            if (status == BluetoothGatt.GATT_SUCCESS ){
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    //启动服务发现  通过服务去获取可以操作的属性
                    gatt.discoverServices();
                    mHolder.setText(R.id.text, "连接成功");
                    gifView.setVisibility(View.GONE);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    closeConn();
                    mHolder.setText(R.id.text, "断开连接");
                    gifView.setVisibility(View.GONE);
                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                    System.out.println("---------------------------->正在连接");
                }
            }else {
                Toast.makeText(BluetoothListActivity.this, "错误码："+status, Toast.LENGTH_SHORT).show();
                mHolder.setText(R.id.text, "连接失败");
            }
        }

        //扫描到设备服务后回调
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i("XXX", String.format("onServicesDiscovered:%s,%s,%s", gatt.getDevice().getName(), gatt.getDevice().getAddress(), status));
            if (status == BluetoothGatt.GATT_SUCCESS) { //BLE服务发现成功
                // 遍历获取BLE服务Services/Characteristics/Descriptors的全部UUID
                for (BluetoothGattService service : gatt.getServices()) {
                    StringBuilder allUUIDs = new StringBuilder("UUIDs={\nS=" + service.getUuid().toString());
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        allUUIDs.append(",\nC=").append(characteristic.getUuid());
                        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors())
                            allUUIDs.append(",\nD=").append(descriptor.getUuid());
                    }
                    allUUIDs.append("}");
                    Log.i("XXXXX", "onServicesDiscovered:" + allUUIDs.toString());
                }
            }
        }

        //若开启监听成功回调
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Toast.makeText(BluetoothListActivity.this, "写入数据成功1", Toast.LENGTH_SHORT).show();
            }else {
                Log.e("XXXXX",status+"");
            }
        }

         //发送数据后的回调
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                UUID uuid = characteristic.getUuid();
                String valueStr = new String(characteristic.getValue());
                Toast.makeText(BluetoothListActivity.this, valueStr, Toast.LENGTH_SHORT).show();
            }else {
                Log.e("XXXXX",status+"");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("XXX", "数据接收了哦"+(characteristic.getValue()));
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.e("XXX", "4");
        }
    };

    // BLE中心设备连接外围设备的数量有限(大概2~7个)，在建立新连接之前必须释放旧连接资源，否则容易出现连接错误133
    private void closeConn() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
        }
    }

    //写入数据
    private void writeData() {
        BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(""));
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(""));
        characteristic.setValue("");
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.dialog_bluetooth_list;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {
        write();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }



    /**
     * 向蓝牙发送数据
     */
    // 注意：连续频繁读写数据容易失败，读写操作间隔最好200ms以上，或等待上次回调完成后再进行下次读写操作！
    // 写入数据成功会回调->onCharacteristicWrite()
    public void write() {
        BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString("00001828-0000-1000-8000-00805f9b34fb"));
        if (service != null) {
            String data = "A3 FF 00 00 00 00 02 00 FF FF C2 11 02 C4 01 01 00";
            byte[] value = getInputBytes(data);
//            byte[] value = new byte[17];
//            value[0] = (byte) 0xA3;
//            value[1] = (byte) 0xFF;
//            value[2] = (byte) 0x00;
//            value[3] = (byte) 0x00;
//            value[4] = (byte) 0x00;
//            value[5] = (byte) 0x00;
//            value[6] = (byte) 0x02;
//            value[7] = (byte) 0x00;
//            value[8] = (byte) 0xFF;
//            value[9] = (byte) 0xFF;
//            value[10] = (byte) 0xC2;
//            value[11] = (byte) 0x11;
//
//            value[12] = (byte) 0x02;
//            value[13] = (byte) 0xC4;
//            value[14] = (byte) 0x01;
//            value[15] = (byte) 0x01;
//            value[16] = (byte) 0x00;
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("00002add-0000-1000-8000-00805f9b34fb"));//通过UUID获取可写的Characteristic
            characteristic.setValue(value); //单次最多20个字节
            mBluetoothGatt.writeCharacteristic(characteristic);
        }else {
            Toast.makeText(this, "没有找到服务UUID", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getInputBytes(String input) {
        String[] byteStrs = input.split(" ");
        if (input.equals("")) {
            return null;
        }
        byte[] result = new byte[byteStrs.length];
        for (int i = 0; i < byteStrs.length; i++) {
//            result[i] = Byte.parseByte(byteStrs[i], 16);
            result[i] = (byte) (Integer.parseInt(byteStrs[i], 16) & 0xFF);
        }
        return result;

    }

}
