package com.example.luke_imagevideo_send.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.camera.activity.SeeImageOrVideoActivity;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.views.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    BaseRecyclerAdapter baseRecyclerAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> bluetoothList = new ArrayList<>();
    public static final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        baseRecyclerAdapter = new BaseRecyclerAdapter<BluetoothDevice>(BluetoothListActivity.this, R.layout.bluetooth_device_name_item, bluetoothList) {
            @Override
            public void convert(BaseViewHolder holder, final BluetoothDevice device) {
                if (device.getName()!=null&&!device.getName().equals("null")){
                    holder.setText(R.id.textView, device.getName());
                }
                if (device.getBluetoothClass().getMajorDeviceClass()==512){
                    holder.setResource(R.id.imageView,R.drawable.ic_phone);
                }
                if (device.getBluetoothClass().getMajorDeviceClass()==256){
                    holder.setResource(R.id.imageView,R.drawable.ic_computer);
                }
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);
        initBluetooth();
        // 设置广播信息过滤 并注册
        // 开始结束
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // 搜索到设备
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // 搜索结束
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
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
            if (!mBluetoothAdapter.isEnabled()) {
//                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                mBluetoothAdapter.enable();
            } else {
                discoveryDevice();
            }
        }
    }

    /**
     * 蓝牙设备列表
     */
    protected void getDeviceList() {
        // 初始化一个数组适配器，用来显示已匹对和未匹对的设备
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // 添加一个item显示信息
        if (pairedDevices.size() > 0) {
            //遍历填充数据
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName()!=null&&!device.getName().equals("null")){
                    bluetoothList.add(device);
                }
            }
        }
        if (bluetoothList.size() == 0) {
            AlertDialogUtil alertDialogUtil = new AlertDialogUtil(BluetoothListActivity.this);
            alertDialogUtil.showSmallDialog("暂无可连接蓝牙");
        }else {
            baseRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                discoveryDevice();
            } else {
                // bluetooth is not open
                Toast.makeText(this, "蓝牙没有开启", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 扫描设备
     */
    private void discoveryDevice() {
        setProgressBarIndeterminateVisibility(true);
        // 添加一个item区分显示信息
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // 开始扫描，每扫描到一个设备，都会发送一个广播
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        unregisterReceiver(mFindBlueToothReceiver);
    }

    /**
     * 接收扫描设备的广播
     * changes the title when discovery is finished
     */
    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 每当发现一个蓝牙设备时
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                //获取设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // 未匹对的情况下添加显示
                if (device.getBondState() != BluetoothDevice.BOND_BONDED&&device.getName()!=null&&!device.getName().equals("null")) {
//                    mDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    bluetoothList.add(device);
                    baseRecyclerAdapter.notifyDataSetChanged();
                }
                // 扫描结束
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("选择蓝牙设备");
                //此处-2是减去我们手动添加的两个区分显示的item
//                Log.i("tag", "finish discovery" + (mDevicesArrayAdapter.getCount() - 2));
                getDeviceList();
            }
        }
    };
}
