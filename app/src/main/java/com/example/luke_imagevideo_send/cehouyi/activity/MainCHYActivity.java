package com.example.luke_imagevideo_send.cehouyi.activity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.TimePickerView;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.cunoraz.gifview.library.GifView;
import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.BytesHexChange;
import com.example.luke_imagevideo_send.cehouyi.util.PickerDivider;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerPositionAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.DialogCallBackTwo;
import com.example.luke_imagevideo_send.http.base.ProgressDialogUtil;
import com.example.luke_imagevideo_send.http.utils.DateSetting;
import com.example.luke_imagevideo_send.http.views.Header;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainCHYActivity extends BaseActivity implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, NumberPicker.Formatter {
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.rbSave)
    RadioButton rbSave;
    @BindView(R.id.rbMenu)
    RadioButton rbMenu;
    @BindView(R.id.tvSS)
    TextView tvSS;
    @BindView(R.id.tvSMZT)
    TextView tvSMZT;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvData)
    TextView tvData;
    @BindView(R.id.onePicker)
    NumberPicker onePicker;
    @BindView(R.id.tenPicker)
    NumberPicker tenPicker;
    @BindView(R.id.hundredPicker)
    NumberPicker hundredPicker;
    @BindView(R.id.thousandPicker)
    NumberPicker thousandPicker;
    @BindView(R.id.linearLayout)
    RelativeLayout linearLayout;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.tvMax)
    TextView tvMax;
    @BindView(R.id.tvMin)
    TextView tvMin;
    @BindView(R.id.gifView)
    GifView gifView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.linBleList)
    LinearLayout linBleList;
    @BindView(R.id.llNumberPicker)
    LinearLayout llNumberPicker;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.tvFTop)
    TextView tvFTop;
    @BindView(R.id.tvFBot)
    TextView tvFBot;
    @BindView(R.id.tvFMZT)
    TextView tvFMZT;
    @BindView(R.id.tvOHZT)
    TextView tvOHZT;

    int i = 0;
    int Max = 0, Min = 0;
    Handler handler;
    boolean exit = false;
    Threads thread = new Threads();
    List<Integer> valueList = new ArrayList<>();
    List<List<Integer>> myValueList = new ArrayList<>();
    BaseRecyclerPositionAdapter baseRecyclerAdapter;
    List<BleDevice> bluetoothList = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    BleDevice myBleDevice;
    String ecServerId = "0000FFF0-0000-1000-8000-00805F9B34FB";
    String ecWriteCharacteristicId = "0000FFF2-0000-1000-8000-00805F9B34FB";
    String ecReadCharacteristicId = "0000FFF1-0000-1000-8000-00805F9B34FB";
    BluetoothGattCharacteristic characteristicWrite;
    BluetoothGattCharacteristic characteristicRead;
    BluetoothGattCharacteristic characteristicNotify;
    Timer timer = new Timer();
    String getDate, tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        new PickerDivider().setNumberPickerDivider(onePicker);
        new PickerDivider().setNumberPickerDivider(tenPicker);
        new PickerDivider().setNumberPickerDivider(hundredPicker);
        new PickerDivider().setNumberPickerDivider(thousandPicker);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        baseRecyclerAdapter = new BaseRecyclerPositionAdapter<BleDevice>(MainCHYActivity.this, R.layout.bluetooth_device_name_item, bluetoothList) {
            @Override
            public void convert(BaseViewHolder holder, BleDevice device, int selectposition) {
                if (bluetoothList.size() == 0) {
                    AlertDialogUtil alertDialogUtil = new AlertDialogUtil(MainCHYActivity.this);
                    alertDialogUtil.showSmallDialog("暂无可连接蓝牙");
                } else {
                    if (device.getName() != null) {
                        holder.setText(R.id.textView, device.getName());
                    }

                    holder.setOnClickListener(R.id.textView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BleManager.getInstance().cancelScan();
                            gifView.setVisibility(View.GONE);
                            gifView.isPaused();
                            //连接
                            connect(device);
                        }
                    });
                }
            }
        };
        recyclerView.setAdapter(baseRecyclerAdapter);

        initBluetooth();

        gifView.setVisibility(View.VISIBLE);
        gifView.setGifResource(R.drawable.bleloading1);
        gifView.play();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main_chy;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {
        bluetoothList.clear();
        baseRecyclerAdapter.notifyDataSetChanged();
        gifView.play();
        gifView.setVisibility(View.VISIBLE);
        startScan();
    }

    /**
     * 初始化扫描蓝牙
     */
    private void initBluetooth() {
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setOperateTimeout(10000);

        boolean bl = BleManager.getInstance().isSupportBle();
        if (!bl) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }
        // 检查蓝牙是否开启
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, Constant.TAG_ONE);
                return;
            }
        }
        startScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.TAG_ONE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, R.string.please_open_bluetooth_to_use_ble_function, Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, R.string.please_open_bluetooth_to_use_ble_function, Toast.LENGTH_SHORT).show();
                // 扫描蓝牙设备
                startScan();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 蓝牙扫描
     */
    private void startScan() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(15000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override//会回到主线程
            public void onScanStarted(boolean success) {
            }

            @Override//扫描过程中所有被扫描到的结果回调
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override//扫描过程中的所有过滤后的结果回调
            public void onScanning(BleDevice bleDevice) {
                Log.e("MainCHYActivity", bleDevice.getName() + "111");
                if (!bluetoothList.contains(bleDevice)) {
                    if (bleDevice.getName() != null && !bleDevice.getName().equals("")) {
                        bluetoothList.add(bleDevice);
                        baseRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override//本次扫描时段内所有被扫描且过滤后的设备集合
            public void onScanFinished(List<BleDevice> scanResultList) {
                gifView.pause();
                gifView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 连接
     */
    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override//开始连接
            public void onStartConnect() {
                ProgressDialogUtil.startLoad(MainCHYActivity.this, getResources().getString(R.string.connecting));
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                ProgressDialogUtil.stopLoad();
                Toast.makeText(MainCHYActivity.this, exception.getCode() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                ProgressDialogUtil.stopLoad();
                Toast.makeText(MainCHYActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                linBleList.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                header.setRightTv(false);
                myBleDevice = bleDevice;
                BluetoothGattService service = gatt.getService(UUID.fromString(ecServerId));
                characteristicWrite = service.getCharacteristic(UUID.fromString(ecWriteCharacteristicId));
                characteristicRead = service.getCharacteristic(UUID.fromString(ecReadCharacteristicId));
                characteristicNotify = service.getCharacteristic(UUID.fromString(ecReadCharacteristicId));
                indicateset(bleDevice, characteristicRead);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                ProgressDialogUtil.stopLoad();
                if (bluetoothList.contains(bleDevice)) {
                    bluetoothList.remove(bleDevice);
                    baseRecyclerAdapter.notifyDataSetChanged();
                }
                Toast.makeText(MainCHYActivity.this, getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void indicateset(final BleDevice bleDevice, final BluetoothGattCharacteristic characteristic) {
        Log.e("mainactivity-notify", characteristic.getService().getUuid().toString());
        BleManager.getInstance().notify(bleDevice, ecServerId, ecReadCharacteristicId, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                sendData();
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                Toast.makeText(MainCHYActivity.this, "通知服务开启失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                makeBackData(new BytesHexChange().bytes2hex(data));
                // 打开通知后，设备发过来的数据将在这里出现
                Log.e("mainchyactivity-notify", new BytesHexChange().bytes2hex(data));
            }
        });
    }

    /**
     * 写入数据
     */
    private void sendData() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//时间
                writeCommand(myBleDevice, characteristicWrite, "5b0004000000005d");
            }
        }, 100);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//声速
                writeCommand(myBleDevice, characteristicWrite, "5b0005000000005d");
            }
        }, 400);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//阀值上限
                writeCommand(myBleDevice, characteristicWrite, "5b0006000000005d");
            }
        }, 700);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//阀值下限
                writeCommand(myBleDevice, characteristicWrite, "5b0007000000005d");
            }
        }, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//当前状态
                writeCommand(myBleDevice, characteristicWrite, "5b0008000000005d");
            }
        }, 1300);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//工件厚度
                writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
            }
        }, 1600);
    }

    /**
     * 写入数据回调
     */
    private void writeCommand(final BleDevice bleDevice, final BluetoothGattCharacteristic characteristic, String sengData) {
        BleManager.getInstance().write(bleDevice, ecServerId, ecWriteCharacteristicId, new BytesHexChange().hexStringToBytes(sengData), new BleWriteCallback() {
            @Override
            public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                String justStringWrite = new BytesHexChange().bytes2hex(justWrite);
            }

            @Override
            public void onWriteFailure(final BleException exception) {
                Looper.prepare();
                Toast.makeText(MainCHYActivity.this, R.string.write_failure, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

    /**
     * 返回数据处理
     *
     * @param data
     */
    private void makeBackData(String data) {
        String s = data.substring(4, 6);
        if (data.substring(4, 6).equals("01")) {
            String HD = data.substring(6, 14);
            Log.e("mainchyactivity-HD", HD);
        }
        if (data.substring(4, 6).equals("04")) {
            long hour = Long.parseLong(data.substring(8, 10), 16);
            long min = Long.parseLong(data.substring(10, 12), 16);
            long second = Long.parseLong(data.substring(12, 14), 16);
            if (hour < 10) {
                hour = Long.parseLong("0" + hour);
            }
            if (min < 10) {
                min = Long.parseLong("0" + min);
            }
            if (second < 10) {
                second = Long.parseLong("0" + second);
            }
            getDate = hour + ":" + min + ":" + second;
            initTime();
        }
        if (data.substring(4, 6).equals("05")) {
            String L = data.substring(10, 14);
            //把并接的16进制字符串转换成10进制long
            long ss = Long.parseLong(L, 16);
            tvSS.setText(String.valueOf(ss) + "m/s");
            Log.e("mainchyactivity-ss", String.valueOf(ss));
        }
        if (data.substring(4, 6).equals("06")) {
            String FTop = data.substring(6, 14);
            Log.e("mainchyactivity-FTop", FTop);
        }
        if (data.substring(4, 6).equals("07")) {
            String FBot = data.substring(6, 14);
            Log.e("mainchyactivity-FBot", FBot);
        }

        if (data.substring(4, 6).equals("08")) {
            String FM = data.substring(8, 10);
            String SM = data.substring(10, 12);
            String OH = data.substring(12, 14);
            Log.e("mainchyactivity-FM", FM);
        }
    }

    // 初始化时间方法
    public void initTime() {
        // 时间变化
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                tvTime.setText((String) msg.obj);
                int f = (int) ((Math.random()) * 20 + 50);
                tvData.setText(f + "mm");
                showChart(f);
            }
        };
        thread.start();
    }

    class Threads extends Thread {
        @Override
        public void run() {
            try {
                while (!exit) {
                    String curryDate = new DateSetting().getDateToString(new DateSetting().getTime(getDate) + 1000);
                    handler.sendMessage(handler.obtainMessage(100, curryDate));
                    getDate = curryDate;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void showChart(int f) {
        if (valueList.size() < 30) {
            valueList.add(f);
        } else {
            myValueList.add(valueList);
            valueList = new ArrayList<>();
            valueList.add(f);
        }
        if (myValueList.size() >= 100) {
            Toast.makeText(this, "数据集合已满请删除数据", Toast.LENGTH_SHORT).show();
        }
        i++;

        if (tvMax.getText().toString().equals("0.0")) {
            tvMax.setText(f + "mm");
            Max = f;
        } else if (f > Max) {
            tvMax.setText(f + "mm");
            Max = f;
        }
        if (tvMin.getText().toString().equals("0.0")) {
            tvMin.setText(f + "mm");
            Min = f;
        } else if (f < Min) {
            tvMin.setText(f + "mm");
            Min = f;
        }
    }

    @Override
    public String format(int value) {
        Log.i("XXX", "format: value" + value);
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch (scrollState) {
            case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:
                Log.i("XXX", "onScrollStateChange: 后续滑动(飞呀飞，根本停下来)");
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                Log.i("XXX", "onScrollStateChange: 不滑动");
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                Log.i("XXX", "onScrollStateChange: 滑动中");
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("XXX", "onValueChange: 原来的值 " + oldVal + "--新值: " + newVal);
    }


    @OnClick({R.id.rbSave, R.id.rbMenu, R.id.tvSS, R.id.tvCancle, R.id.tvSure, R.id.tvSMZT, R.id.tvFMZT, R.id.tvOHZT, R.id.tvTime, R.id.tvUnit, R.id.tvFTop, R.id.tvFBot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbSave:
                break;
            case R.id.rbMenu:
                exit = true;
                Intent intent = new Intent(this, ValueActivity.class);
                if (valueList.size() != 0) {
                    myValueList.add(valueList);
                    EventBus.getDefault().postSticky(myValueList);
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tvSS:
                initSS();
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tvCancle:
                linearLayout.setVisibility(View.GONE);
                break;
            case R.id.tvSMZT:
                tag = "SMZT";
                initZT();
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tvFMZT:
                tag = "FMZT";
                initZT();
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tvOHZT:
                tag = "OHZT";
                initZT();
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tvTime:
                selectTime();
                break;
            case R.id.tvUnit:
                tag = "UNIT";
                initZT();
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tvFTop:
                tag = "FTOP";
                showFDialog();
                break;
            case R.id.tvFBot:
                tag = "FBOT";
                showFDialog();
                break;
            case R.id.tvSure:
                linearLayout.setVisibility(View.GONE);
                if (tag.equals("SS")) {
                    tvSS.setText(String.valueOf(onePicker.getValue())
                            + String.valueOf(tenPicker.getValue())
                            + String.valueOf(hundredPicker.getValue())
                            + String.valueOf(thousandPicker.getValue()));
                }
                if (tag.equals("SMZT")) {
                    if (onePicker.getValue() == 0) {
                        tvSMZT.setText("开");
                    } else {
                        tvSMZT.setText("关");
                    }
                }
                if (tag.equals("FMZT")) {
                    if (onePicker.getValue() == 0) {
                        tvFMZT.setText("开");
                    } else {
                        tvFMZT.setText("关");
                    }
                }
                if (tag.equals("OHZT")) {
                    tvOHZT.setText(String.valueOf(onePicker.getValue()));
                }
                if (tag.equals("UNIT")) {
                    if (onePicker.getValue() == 0) {
                        tvUnit.setText("MM");
                    } else {
                        tvUnit.setText("IN");
                    }
                }
                if (tag.equals("FTOP")) {
                    tvOHZT.setText(String.valueOf(onePicker.getValue()));
                }
                break;
        }
    }

    private void showFDialog() {
        new AlertDialogUtil(this).showDialogF(new DialogCallBackTwo() {
            @Override
            public void confirm(String name1, Dialog dialog, EditText editText) {
                if (tag.equals("FTOP")) {
                    if (name1 != null && !name1.trim().equals("")) {
                        tvFTop.setText(name1);
                        dialog.dismiss();
                    }else {
                        Toast.makeText(MainCHYActivity.this, "请输入阀值", Toast.LENGTH_SHORT).show();
                    }
                }
                if (tag.equals("FBOT")) {
                    if (name1 != null && !name1.trim().equals("")) {
                        tvFBot.setText(name1);
                        dialog.dismiss();
                    }else {
                        Toast.makeText(MainCHYActivity.this, "请输入阀值", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void cancel(String name2, Dialog dialog) {
            }
        });
    }

    private void initSS() {
        tag = "SS";
        String[] one = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        onePicker.setFormatter(this);
        onePicker.setOnValueChangedListener(this);
        onePicker.setOnScrollListener(this);
        onePicker.setDisplayedValues(one);
        onePicker.setMinValue(0);
        onePicker.setMaxValue(one.length - 1);
        onePicker.setValue(1);
        //设置为对当前值不可编辑
        onePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] ten = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        tenPicker.setFormatter(this);
        tenPicker.setOnValueChangedListener(this);
        tenPicker.setOnScrollListener(this);
        tenPicker.setDisplayedValues(ten);
        tenPicker.setMinValue(0);
        tenPicker.setMaxValue(ten.length - 1);
        tenPicker.setValue(4);
        //设置为对当前值不可编辑
        tenPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] hundred = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        hundredPicker.setFormatter(this);
        hundredPicker.setOnValueChangedListener(this);
        hundredPicker.setOnScrollListener(this);
        hundredPicker.setDisplayedValues(hundred);
        hundredPicker.setMinValue(0);
        hundredPicker.setMaxValue(hundred.length - 1);
        hundredPicker.setValue(5);
        //设置为对当前值不可编辑
        hundredPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] thousand = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        thousandPicker.setFormatter(this);
        thousandPicker.setOnValueChangedListener(this);
        thousandPicker.setOnScrollListener(this);
        thousandPicker.setDisplayedValues(thousand);
        thousandPicker.setMinValue(0);
        thousandPicker.setMaxValue(thousand.length - 1);
        thousandPicker.setValue(2);
        //设置为对当前值不可编辑
        thousandPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        onePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        tenPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        hundredPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        thousandPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        onePicker.setVisibility(View.VISIBLE);
        tenPicker.setVisibility(View.VISIBLE);
        hundredPicker.setVisibility(View.VISIBLE);
        thousandPicker.setVisibility(View.VISIBLE);
        //这里设置为不循环显示，默认值为true
        onePicker.setWrapSelectorWheel(false);
        tenPicker.setWrapSelectorWheel(false);
        hundredPicker.setWrapSelectorWheel(false);
        thousandPicker.setWrapSelectorWheel(false);
    }

    private void initZT() {
        if (tag.equals("SMZT") || tag.equals("FMZT")) {
            String[] one = {"开", "关"};
            onePicker.setFormatter(this);
            onePicker.setOnValueChangedListener(this);
            onePicker.setOnScrollListener(this);
            onePicker.setDisplayedValues(one);
            onePicker.setMinValue(0);
            onePicker.setMaxValue(one.length - 1);
            onePicker.setValue(0);
            tenPicker.setVisibility(View.GONE);
            hundredPicker.setVisibility(View.GONE);
            thousandPicker.setVisibility(View.GONE);
            //这里设置为不循环显示，默认值为true
            onePicker.setWrapSelectorWheel(false);
        }
        if (tag.equals("OHZT")) {
            String[] one = {"0", "1", "2", "3", "4", "5", "6", "7"};
            onePicker.setFormatter(this);
            onePicker.setOnValueChangedListener(this);
            onePicker.setOnScrollListener(this);
            onePicker.setDisplayedValues(one);
            onePicker.setMinValue(0);
            onePicker.setMaxValue(one.length - 1);
            onePicker.setValue(0);
            tenPicker.setVisibility(View.GONE);
            hundredPicker.setVisibility(View.GONE);
            thousandPicker.setVisibility(View.GONE);
            //这里设置为不循环显示，默认值为true
            onePicker.setWrapSelectorWheel(false);
        }
        if (tag.equals("UNIT")) {
            String[] one = {"MM", "IN"};
            onePicker.setFormatter(this);
            onePicker.setOnValueChangedListener(this);
            onePicker.setOnScrollListener(this);
            onePicker.setDisplayedValues(one);
            onePicker.setMinValue(0);
            onePicker.setMaxValue(one.length - 1);
            onePicker.setValue(0);
            tenPicker.setVisibility(View.GONE);
            hundredPicker.setVisibility(View.GONE);
            thousandPicker.setVisibility(View.GONE);
            //这里设置为不循环显示，默认值为true
            onePicker.setWrapSelectorWheel(false);
        }
    }

    private void selectTime() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
                dateFormat = new SimpleDateFormat("hh:mm:ss");
                tvTime.setText(dateFormat.format(date));
            }
        })
                .setType(new boolean[]{false, false, false, true, true, true})// 默认全部显示
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentSize(16)//滚轮文字大小
                .setTitleSize(16)//标题文字大小
//    //.setTitleText("Title")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
//    .isCyclic(true)//是否循环滚动
                //.setTitleColor(Color.16dp)//标题文字颜色
//                .setSubmitColor(Color.YELLOW)//确定按钮文字颜色
                .setCancelColor(Color.RED)//取消按钮文字颜色
                .setLineSpacingMultiplier(3)
                .setTitleBgColor(getResources().getColor(R.color.shouye))//标题背景颜色 Night mode
//    .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
////    .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
////    .setRangDate(startDate,endDate)//起始终止年月日设定
//    //.setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                //.isDialog(true)//是否显示为对话框样式
                .build();

        pvTime.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }
}