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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.luke_imagevideo_send.chifen.magnetic.util.CallPolice;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.BaseRecyclerPositionAdapter;
import com.example.luke_imagevideo_send.http.base.BaseViewHolder;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.DialogCallBack;
import com.example.luke_imagevideo_send.http.base.DialogCallBackTwo;
import com.example.luke_imagevideo_send.http.base.ProgressDialogUtil;
import com.example.luke_imagevideo_send.http.views.Header;
import com.jiangdg.singalviewlib.SignalView;

import org.litepal.tablemanager.Connector;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
    //    @BindView(R.id.rbSave)
//    Button btnSave;
    @BindView(R.id.tvSS)
    TextView tvSS;
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
    @BindView(R.id.btnBack)
    TextView btnBack;
    @BindView(R.id.signaView)
    SignalView signaView;
    @BindView(R.id.tvSM)
    TextView tvSM;

    List<String> valueList = new ArrayList<>();
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
    String getDate, tag = "";
    String unit = "", nowData = "";
    Dialog myDialog;
    String HD = "";
    double com = 1;
    boolean isFirst = true;
    boolean fmOpen = true;
    boolean mmUnit = true;
    boolean smOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //创建数据库
        Connector.getDatabase();
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
                if (myDialog != null) {
                    myDialog.dismiss();
                }
                Toast.makeText(MainCHYActivity.this, getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                linBleList.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                header.setRightTv(true);
                bluetoothList.clear();
                baseRecyclerAdapter.notifyDataSetChanged();
                gifView.play();
                gifView.setVisibility(View.VISIBLE);
                startScan();
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
            public void run() {
                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
            }
        }, 100);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                writeCommand(myBleDevice, characteristicWrite, "5b0008000000005d");
            }
        }, 600);
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
                Message message = Message.obtain();
                messageHandler.sendMessage(message);
            }
        });
    }

    /**
     * 返回数据处理
     *
     * @param data
     */
    private void makeBackData(String data) {
        Log.e("mainchyactivity----", data);
        //测厚仪在设置界面APP不允许操作
        if (data.substring(4, 6).equals("0b")) {
            Toast.makeText(this, "请退出测厚仪设置界面后设置", Toast.LENGTH_SHORT).show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
                }
            }, 100);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    writeCommand(myBleDevice, characteristicWrite, "5b0005000000005d");
                }
            }, 600);
        } else {
            if (data.substring(4, 6).equals("08")) {
                Log.e("mainchyactivity-08", data.substring(0, 16));
                if (Long.parseLong(data.substring(8, 10), 16) == 0) {
                    tvFMZT.setText("关");
                    fmOpen = false;
                } else if (Long.parseLong(data.substring(8, 10), 16) == 1) {
                    tvFMZT.setText("开");
                    fmOpen = true;
                }

                if (Long.parseLong(data.substring(10, 12), 16) == 0) {
                    tvSM.setText("CLOSE");
                    smOpen = false;
                } else if (Long.parseLong(data.substring(10, 12), 16) == 1) {
                    tvSM.setText("OPEN");
                    smOpen = true;
                }

                if (Long.parseLong(data.substring(12, 14), 16) == 0) {
                    tvUnit.setText("IN");
                    mmUnit = false;
                } else if (Long.parseLong(data.substring(12, 14), 16) == 1) {
                    tvUnit.setText("MM");
                    mmUnit = true;
                }

                if (isFirst) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            writeCommand(myBleDevice, characteristicWrite, "5b0005000000005d");
                        }
                    }, 100);
                } else {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
                        }
                    }, 100);
                }
            }
            if (data.substring(4, 6).equals("05")) {
                String L = data.substring(8, 10) + data.substring(6, 8);
                DecimalFormat df = new DecimalFormat("###.0000");
                makeFData(tvSS, df, L, tag);
                //把并接的16进制字符串转换成10进制long
                long ss = Long.parseLong(L, 16);
                if (tvUnit.getText().toString().equals("MM")) {
                    tvSS.setText(String.valueOf(ss));
                } else {
                    makeFData(tvSS, df, L, "ss");
                }
                if (isFirst) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            writeCommand(myBleDevice, characteristicWrite, "5b0006000000005d");
                        }
                    }, 100);
                }else {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
                        }
                    }, 100);
                }
//                if (tag.equals("SS")) {
//                    tag = "";
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {//时间
//                            writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
//                        }
//                    }, 300);
//                } else if (tag.equals("") || tag.equals("UNIT")) {
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {//时间
//                            writeCommand(myBleDevice, characteristicWrite, "5b0006000000005d");
//                        }
//                    }, 100);
//                }
//                Log.e("mainchyactivity-ss", data.substring(0, 16));
            }
            if (data.substring(4, 6).equals("06")) {
                String L = data.substring(12, 14) + data.substring(10, 12) + data.substring(8, 10) + data.substring(6, 8);
                if (tvUnit.getText().toString().equals("MM")) {
                    DecimalFormat df = new DecimalFormat("###.00");
                    makeFData(tvFTop, df, L, "utilMF");
                } else if (tvUnit.getText().toString().equals("IN")) {
                    DecimalFormat df = new DecimalFormat("###.000");
                    makeFData(tvFTop, df, L, "utilIF");
                }
                if (isFirst) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            writeCommand(myBleDevice, characteristicWrite, "5b0007000000005d");
                        }
                    }, 100);
                }else {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
                        }
                    }, 100);
                }
//                if (tag != null && tag.equals("FTOP")) {
//                    tag = "";
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
//                        }
//                    }, 300);
//                } else {
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            writeCommand(myBleDevice, characteristicWrite, "5b0007000000005d");
//                        }
//                    }, 300);
//                }
//                Log.e("mainchyactivity-FTop", data.substring(0, 16));
            }
            if (data.substring(4, 6).equals("07")) {
                String L = data.substring(12, 14) + data.substring(10, 12) + data.substring(8, 10) + data.substring(6, 8);
                if (tvUnit.getText().toString().equals("MM")) {
                    DecimalFormat df = new DecimalFormat("###.00");
                    makeFData(tvFBot, df, L, "utilMF");
                } else if (tvUnit.getText().toString().equals("IN")) {
                    DecimalFormat df = new DecimalFormat("###.000");
                    makeFData(tvFBot, df, L, "utilIF");
                }
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
                    }
                }, 100);
                isFirst = false;
            }
            if (data.substring(4, 6).equals("01")) {
                if (data.length() == 20) {
                    HD = data.substring(12, 14) + data.substring(10, 12) + data.substring(8, 10) + data.substring(6, 8);
                    String OH = data.substring(16, 18);
                    if (tvUnit.getText().toString().equals("MM")) {
                        DecimalFormat df = new DecimalFormat("###.00");
                        makeFData(tvData, df, HD, "dataMM");
                    } else if (tvUnit.getText().toString().equals("IN")) {
                        DecimalFormat df = new DecimalFormat("###.000");
                        makeFData(tvData, df, HD, "dataIN");
                    }
                    signaView.setSignalValue((int) (8 - Long.parseLong(OH, 16)));
                    Log.e("mainchyactivity-HD", HD);
                    unit = tvUnit.getText().toString();
                    nowData = tvData.getText().toString();
                }
            }

            if (data.substring(4, 6).equals("0a")) {
                isFirst = true;
                valueList.clear();
                signaView.setSignalValue(0);
                tvData.setText("0.00");
                tag = "";
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {//时间
                        writeCommand(myBleDevice, characteristicWrite, "5b0008000000005d");
                    }
                }, 300);
            }
        }
    }

    private void makeFData(TextView tvF, DecimalFormat df, String L, String tag) {
        if (tag.equals("dataMM")) {
            String FTopData = "";
            if (df.format((double) Long.parseLong(L, 16) / 1000).substring(0, 1).equals("\\.")) {
                FTopData = "0" + (double) Long.parseLong(L, 16) / 1000 + "";
            } else {
                FTopData = (double) Long.parseLong(L, 16) / 1000 + "";
            }
            if (FTopData.split("\\.")[1].length() >= 2) {
                String data = FTopData.substring(0, FTopData.split("\\.")[0].length() + 3);
                if (Double.valueOf(data) > Double.valueOf(tvFTop.getText().toString())
                        || Double.valueOf(data) < Double.valueOf(tvFBot.getText().toString())) {
                    tvF.setTextColor(getResources().getColor(R.color.red));
                    if (tvFMZT.getText().toString().equals("开")) {
                        new CallPolice().sendPolice(MainCHYActivity.this);
                    }
                } else {
                    tvF.setTextColor(getResources().getColor(R.color.black));
                }
                tvF.setText(FTopData.substring(0, FTopData.split("\\.")[0].length() + 3));
            } else {
                if (Double.valueOf(FTopData) > Double.valueOf(tvFTop.getText().toString())
                        || Double.valueOf(FTopData) < Double.valueOf(tvFBot.getText().toString())) {
                    tvF.setTextColor(getResources().getColor(R.color.red));
                    if (tvFMZT.getText().toString().equals("开")) {
                        new CallPolice().sendPolice(MainCHYActivity.this);
                    }
                } else {
                    tvF.setTextColor(getResources().getColor(R.color.black));
                }
                tvF.setText(FTopData + "0");
            }
        }
        if (tag.equals("dataIN")) {
            String FTopData = "";
            if (df.format((double) Long.parseLong(L, 16) / 25400).substring(0, 1).equals(".")) {
                FTopData = "0" + (double) Long.parseLong(L, 16) / 25400 + "";
            } else {
                FTopData = (double) Long.parseLong(L, 16) / 25400 + "";
            }
            String[] all = FTopData.split("\\.");
            if (FTopData.split("\\.")[1].length() > 3) {
                String s = FTopData.split("\\.")[0];
                if (s.equals("00")) {
                    String data = FTopData.substring(1, FTopData.split("\\.")[0].length() + 4);
                    if (Double.valueOf(data) > Double.valueOf(tvFTop.getText().toString())
                            || Double.valueOf(data) < Double.valueOf(tvFBot.getText().toString())) {
                        tvF.setTextColor(getResources().getColor(R.color.red));
                        if (tvFMZT.getText().toString().equals("开")) {
                            new CallPolice().sendPolice(MainCHYActivity.this);
                        }
                    } else {
                        tvF.setTextColor(getResources().getColor(R.color.black));
                    }
                    tvF.setText(data);
                } else {
                    String data = FTopData.substring(0, FTopData.split("\\.")[0].length() + 4);
                    if (Double.valueOf(data) > Double.valueOf(tvFTop.getText().toString())
                            || Double.valueOf(data) < Double.valueOf(tvFBot.getText().toString())) {
                        tvF.setTextColor(getResources().getColor(R.color.red));
                        if (tvFMZT.getText().toString().equals("开")) {
                            new CallPolice().sendPolice(MainCHYActivity.this);
                        }
                    } else {
                        tvF.setTextColor(getResources().getColor(R.color.black));
                    }
                    tvF.setText(data);
                }
            } else {
                if (Double.valueOf(FTopData) > Double.valueOf(tvFTop.getText().toString())
                        || Double.valueOf(FTopData) < Double.valueOf(tvFBot.getText().toString())) {
                    tvF.setTextColor(getResources().getColor(R.color.red));
                    if (tvFMZT.getText().toString().equals("开")) {
                        new CallPolice().sendPolice(MainCHYActivity.this);
                    }
                } else {
                    tvF.setTextColor(getResources().getColor(R.color.black));
                }
                tvF.setText(FTopData + "0");
            }
        }
        if (tag.equals("ss")) {
            String FTopData = "";
            if (df.format((double) Long.parseLong(L, 16) / 25400).substring(0, 1).equals(".")) {
                FTopData = "0" + (double) Long.parseLong(L, 16) / 25400 + "";
            } else {
                FTopData = (double) Long.parseLong(L, 16) / 25400 + "";
            }
            String[] all = FTopData.split("\\.");
            if (FTopData.split("\\.")[1].length() > 3) {
                String s = FTopData.split("\\.")[0];
                if (tag.equals("utilIF")) {
                    if (s.equals("00")) {
                        tvF.setText(FTopData.substring(1, FTopData.split("\\.")[0].length() + 4));
                    } else {
                        tvF.setText(FTopData.substring(0, FTopData.split("\\.")[0].length() + 4));
                    }
                } else {
                    if (s.equals("00")) {
                        tvF.setText(FTopData.substring(1, FTopData.split("\\.")[0].length() + 5));
                    } else {
                        tvF.setText(FTopData.substring(0, FTopData.split("\\.")[0].length() + 5));
                    }
                }
            } else {
                tvF.setText(FTopData);
            }
        }

        if (tag.equals("utilMF")) {
            String FTopData = "";
            if (df.format((double) Long.parseLong(L, 16) / 1000).substring(0, 1).equals("\\.")) {
                FTopData = "0" + (double) Long.parseLong(L, 16) / 1000 + "";
            } else {
                FTopData = (double) Long.parseLong(L, 16) / 1000 + "";
            }
            if (FTopData.split("\\.")[1].length() >= 2) {
                String data = FTopData.substring(0, FTopData.split("\\.")[0].length() + 3);
                tvF.setText(FTopData.substring(0, FTopData.split("\\.")[0].length() + 3));
            } else {
                tvF.setText(FTopData + "0");
            }
        }
        if (tag.equals("utilIF")) {
            String FTopData = "";
            if (df.format((double) Long.parseLong(L, 16) / 25400).substring(0, 1).equals(".")) {
                FTopData = "0" + (double) Long.parseLong(L, 16) / 25400 + "";
            } else {
                FTopData = (double) Long.parseLong(L, 16) / 25400 + "";
            }
            String[] all = FTopData.split("\\.");
            if (FTopData.split("\\.")[1].length() > 3) {
                String s = FTopData.split("\\.")[0];
                if (tag.equals("utilIF")) {
                    if (s.equals("00")) {
                        tvF.setText(FTopData.substring(1, FTopData.split("\\.")[0].length() + 4));
                    } else {
                        tvF.setText(FTopData.substring(0, FTopData.split("\\.")[0].length() + 4));
                    }
                }
            } else {
                tvF.setText(FTopData + "0");
            }
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


    @OnClick({R.id.tvSS, R.id.tvCancle, R.id.tvSure, R.id.tvFMZT, R.id.tvUnit, R.id.tvFTop, R.id.tvFBot, R.id.btnBack, R.id.tvSM})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCancle:
                linearLayout.setVisibility(View.GONE);
                break;
            case R.id.tvSure:
                linearLayout.setVisibility(View.GONE);
                String ssdata = String.valueOf(onePicker.getValue() + 1)
                        + String.valueOf(tenPicker.getValue())
                        + String.valueOf(hundredPicker.getValue())
                        + String.valueOf(thousandPicker.getValue());
                tvSS.setText(ssdata);
                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {//时间
                        String strHex2 = String.format("%04x", Integer.parseInt(ssdata)).toUpperCase();//高位补0
                        strHex2 = strHex2.substring(2, 4) + strHex2.substring(0, 2);
                        writeCommand(myBleDevice, characteristicWrite, "5b0005" + strHex2 + "00005d");
                    }
                }, 300);
                break;
            case R.id.tvSS:
                tag = "SS";
                initSS();
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tvFMZT:
                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                if (tvFMZT.getText().toString().equals("开")) {
                    tvFMZT.setText("关");
                    if (mmUnit){
                        if (smOpen){
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010001015d");
                                }
                            }, 300);
                        }else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010000015d");
                                }
                            }, 300);

                        }
                    }else if (!mmUnit){
                        if (smOpen){
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010001005d");
                                }
                            }, 300);
                        }else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010000005d");
                                }
                            }, 300);
                        }
                    }
                } else {
                    tvFMZT.setText("开");
                    if (mmUnit){
                        if (smOpen){
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010101015d");
                                }
                            }, 300);
                        }else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010100015d");
                                }
                            }, 300);
                        }
                    }else if (!mmUnit){
                        if (smOpen){
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010101005d");
                                }
                            }, 300);
                        }else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010100005d");
                                }
                            }, 300);
                        }
                    }
                }
                break;
            case R.id.tvUnit:
                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                if (tvUnit.getText().toString().equals("MM")) {
                    tvUnit.setText("IN");
                    if (fmOpen){
                        if (smOpen){
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010101005d");
                                }
                            }, 300);
                        }else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010100005d");
                                }
                            }, 300);

                        }
                    }else if (!fmOpen){
                        if (smOpen){
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010001005d");
                                }
                            }, 300);
                        }else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010000005d");
                                }
                            }, 300);

                        }
                    }
                } else {
                    tvUnit.setText("MM");
                    if (fmOpen){
                        if (smOpen){
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010101015d");
                                }
                            }, 300);
                        }else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010100015d");
                                }
                            }, 300);

                        }
                    }else if (!fmOpen){
                        if (smOpen){
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010001015d");
                                }
                            }, 300);
                        }else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010000015d");
                                }
                            }, 300);

                        }
                    }
                }
                break;
            case R.id.tvFTop:
                tag = "FTOP";
                showFDialog("请输入阈值上限");
                break;
            case R.id.tvFBot:
                tag = "FBOT";
                showFDialog("请输入阈值下限");
                break;
            case R.id.btnBack:
                tag = "BACK";
                new AlertDialogUtil(this).showSmallDialog("您确定要恢复出厂设置吗？", new DialogCallBack() {
                    @Override
                    public void confirm(String name, Dialog dialog) {
                        writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {//时间
                                writeCommand(myBleDevice, characteristicWrite, "5b000aaa55aa555d");
                            }
                        }, 300);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
        }
    }

    private void showFDialog(String title) {
        new AlertDialogUtil(this).showDialogF(title, new DialogCallBackTwo() {
            @Override
            public void confirm(String name1, Dialog dialog, EditText editText) {
                myDialog = dialog;
                if (tag.equals("FTOP")) {
                    if (name1 != null && !name1.trim().equals("")) {
//                        tvFTop.setText(name1);
                        if (tvUnit.getText().toString().equals("MM")) {
                            if (Double.valueOf(name1) < 0.50 || Double.valueOf(name1) > 500.10) {
                                Toast.makeText(MainCHYActivity.this, "阈值下限取值范围0.5~500.1", Toast.LENGTH_SHORT).show();
                            } else if (Double.valueOf(name1) < Double.valueOf(tvFBot.getText().toString())) {
                                Toast.makeText(MainCHYActivity.this, "阈值上限不能小于阈值下限", Toast.LENGTH_SHORT).show();
                            } else {
                                name1 = String.valueOf(new Double(Double.valueOf(name1) * 1000).intValue());
                                dialog.dismiss();
                                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                                String finalName = name1;
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {//时间
                                        String strHex2 = String.format("%08x", Integer.parseInt(finalName)).toUpperCase();//高位补0
                                        strHex2 = strHex2.substring(6, 8) + strHex2.substring(4, 6) + strHex2.substring(2, 4) + strHex2.substring(0, 2);
                                        //5b0006000000005d
                                        writeCommand(myBleDevice, characteristicWrite, "5b0006" + strHex2 + "5d");
                                    }
                                }, 300);
                            }
                        } else if (tvUnit.getText().toString().equals("IN")) {
                            if (Double.valueOf(name1) < 0.019 || Double.valueOf(name1) > 19.688) {
                                Toast.makeText(MainCHYActivity.this, "阈值下限取值范围0.019~19.688", Toast.LENGTH_SHORT).show();
                            } else if (Double.valueOf(name1) < Double.valueOf(tvFBot.getText().toString())) {
                                Toast.makeText(MainCHYActivity.this, "阈值上限不能小于阈值下限", Toast.LENGTH_SHORT).show();
                            } else {
                                name1 = String.valueOf(new Double(Double.valueOf(name1) * 1000).intValue());
                                dialog.dismiss();
                                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                                String finalName = name1;
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {//时间
                                        String strHex2 = String.format("%08x", Integer.parseInt(finalName)).toUpperCase();//高位补0
                                        strHex2 = strHex2.substring(6, 8) + strHex2.substring(4, 6) + strHex2.substring(2, 4) + strHex2.substring(0, 2);
                                        //5b0006000000005d
                                        writeCommand(myBleDevice, characteristicWrite, "5b0006" + strHex2 + "5d");
                                    }
                                }, 300);
                            }
                        }
                    } else {
                        Toast.makeText(MainCHYActivity.this, title, Toast.LENGTH_SHORT).show();
                    }
                }
                if (tag.equals("FBOT")) {
                    if (name1 != null && !name1.trim().equals("")) {
//                        tvFBot.setText(name1);
                        if (tvUnit.getText().toString().equals("MM")) {
                            if (Double.valueOf(name1) < 0.50 || Double.valueOf(name1) > 500.10) {
                                Toast.makeText(MainCHYActivity.this, "阈值下限取值范围0.5~500.1", Toast.LENGTH_SHORT).show();
                            } else if (Double.valueOf(name1) > Double.valueOf(tvFTop.getText().toString())) {
                                Toast.makeText(MainCHYActivity.this, "阈值下限不能大于阈值上限", Toast.LENGTH_SHORT).show();
                            } else {
                                name1 = String.valueOf(new Double(Double.valueOf(name1) * 1000).intValue());
                                dialog.dismiss();
                                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                                String finalName1 = name1;
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {//时间
                                        String strHex2 = String.format("%08x", Integer.parseInt(finalName1)).toUpperCase();//高位补0
                                        strHex2 = strHex2.substring(6, 8) + strHex2.substring(4, 6) + strHex2.substring(2, 4) + strHex2.substring(0, 2);
                                        //5b0006000000005d
                                        writeCommand(myBleDevice, characteristicWrite, "5b0007" + strHex2 + "5d");
                                    }
                                }, 300);
                            }
                        } else if (tvUnit.getText().toString().equals("IN")) {
                            if (Double.valueOf(name1) < 0.019 || Double.valueOf(name1) > 19.688) {
                                Toast.makeText(MainCHYActivity.this, "阈值下限取值范围0.019~19.688", Toast.LENGTH_SHORT).show();
                            } else if (Double.valueOf(name1) > Double.valueOf(tvFTop.getText().toString())) {
                                Toast.makeText(MainCHYActivity.this, "阈值下限不能大于阈值上限", Toast.LENGTH_SHORT).show();
                            } else {
                                name1 = String.valueOf(new Double(Double.valueOf(name1) * 1000).intValue());
                                dialog.dismiss();
                                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                                String finalName1 = name1;
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {//时间
                                        String strHex2 = String.format("%08x", Integer.parseInt(finalName1)).toUpperCase();//高位补0
                                        strHex2 = strHex2.substring(6, 8) + strHex2.substring(4, 6) + strHex2.substring(2, 4) + strHex2.substring(0, 2);
                                        //5b0006000000005d
                                        writeCommand(myBleDevice, characteristicWrite, "5b0007" + strHex2 + "5d");
                                    }
                                }, 300);
                            }
                        }
                    } else {
                        Toast.makeText(MainCHYActivity.this, title, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void cancel(String name2, Dialog dialog) {
            }
        });
    }

    private void initSS() {
        String[] one = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        onePicker.setFormatter(this);
        onePicker.setOnValueChangedListener(this);
        onePicker.setOnScrollListener(this);
        onePicker.setDisplayedValues(null);
        onePicker.setMaxValue(one.length - 1);
        onePicker.setDisplayedValues(one);
        onePicker.setMinValue(0);
        onePicker.setValue(5);
        //设置为对当前值不可编辑
        onePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] ten = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        tenPicker.setFormatter(this);
        tenPicker.setOnValueChangedListener(this);
        tenPicker.setOnScrollListener(this);
        tenPicker.setDisplayedValues(null);
        tenPicker.setMaxValue(ten.length - 1);
        tenPicker.setDisplayedValues(ten);
        tenPicker.setMinValue(0);
        tenPicker.setValue(9);
        //设置为对当前值不可编辑
        tenPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] hundred = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        hundredPicker.setFormatter(this);
        hundredPicker.setOnValueChangedListener(this);
        hundredPicker.setOnScrollListener(this);
        hundredPicker.setDisplayedValues(null);
        hundredPicker.setMaxValue(hundred.length - 1);
        hundredPicker.setDisplayedValues(hundred);
        hundredPicker.setMinValue(0);
        hundredPicker.setValue(2);
        //设置为对当前值不可编辑
        hundredPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] thousand = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        thousandPicker.setFormatter(this);
        thousandPicker.setOnValueChangedListener(this);
        thousandPicker.setOnScrollListener(this);
        thousandPicker.setDisplayedValues(null);
        thousandPicker.setMaxValue(thousand.length - 1);
        thousandPicker.setDisplayedValues(thousand);
        thousandPicker.setMinValue(0);
        thousandPicker.setValue(0);
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

    Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainCHYActivity.this, R.string.write_failure, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }
}