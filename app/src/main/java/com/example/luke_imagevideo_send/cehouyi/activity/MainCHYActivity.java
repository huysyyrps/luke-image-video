package com.example.luke_imagevideo_send.cehouyi.activity;

import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.BleConstant;
import com.example.luke_imagevideo_send.cehouyi.util.BleWriteCallBack;
import com.example.luke_imagevideo_send.cehouyi.util.BytesHexChange;
import com.example.luke_imagevideo_send.cehouyi.util.ConstandData;
import com.example.luke_imagevideo_send.chifen.magnetic.util.CallPolice;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.DialogCallBack;
import com.example.luke_imagevideo_send.http.base.DialogCallBackTwo;
import com.example.luke_imagevideo_send.http.views.Header;
import com.github.gzuliyujiang.wheelpicker.NumberPicker;
import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnNumberPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionSelectedListener;
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangdg.singalviewlib.SignalView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainCHYActivity extends BaseActivity implements OnNumberPickedListener, OnOptionPickedListener {
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.tvSS)
    TextView tvSS;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvData)
    TextView tvData;
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
    @BindView(R.id.tvGroup)
    TextView tvGroup;

    List<String> valueList = new ArrayList<>();

    Timer timer = new Timer();
    String tag = "";
    String unit = "", nowData = "";
    Dialog myDialog;
    String HD = "";
    boolean isFirst = true;
    boolean fmOpen = true;
    boolean mmUnit = true;
    boolean smOpen = true;
    String selectTag = "";
    BleDevice myBleDevice = ConstandData.myBleDevice;
    BluetoothGattCharacteristic characteristicWrite = ConstandData.characteristicWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("ExitApp");
        this.registerReceiver(this.broadcastReceiver, filter);

        new BleConstant().indicateset(ConstandData.myBleDevice, ConstandData.characteristicRead, this, new BleWriteCallBack() {
            @Override
            public void onBleWriteCallBack(BleDevice bleDevice, BluetoothGattCharacteristic characteristicWrite) {
                sendData();
            }

            @Override
            public void onBleWriteBackCallBack(String data) {
                makeBackData(data);
            }
        });
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
    }


    /**
     * 写入数据
     */
    private void sendData() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                writeCommand(ConstandData.myBleDevice, ConstandData.characteristicWrite, "5b0001000000005d");
            }
        }, 100);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                writeCommand(ConstandData.myBleDevice, ConstandData.characteristicWrite, "5b0008000000005d");
            }
        }, 600);
    }

    /**
     * 写入数据回调
     */
    public void writeCommand(final BleDevice bleDevice, final BluetoothGattCharacteristic characteristic, String sengData) {
        BleManager.getInstance().write(bleDevice, ConstandData.ecServerId, ConstandData.ecWriteCharacteristicId, new BytesHexChange().hexStringToBytes(sengData), new BleWriteCallback() {
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

//                if (isFirst) {
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            writeCommand(myBleDevice, characteristicWrite, "5b0005000000005d");
//                        }
//                    }, 100);
//                } else {
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
//                        }
//                    }, 100);
//                }
                writeCommand(myBleDevice, characteristicWrite, "5b0005000000005d");
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
                } else {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
                        }
                    }, 100);
                }
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
                } else {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            writeCommand(myBleDevice, characteristicWrite, "5b0001000000Ff5d");
                        }
                    }, 100);
                }
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

            if (data.substring(4, 6).equals("09")) {
                Log.e("XXXXX", data);
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

    @OnClick({R.id.tvSS, R.id.tvFMZT, R.id.tvUnit, R.id.tvFTop, R.id.tvFBot, R.id.btnBack, R.id.tvSM, R.id.tvGroup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSS:
                selectTag = "ssTag";
                String JsonData = "";
                JsonData = getJson(this, "sound_velocity.json");//获取assets目录下的json文件数据
                List<String> data = new Gson().fromJson(JsonData, new TypeToken<List<String>>() {
                }.getType());
                OptionPicker picker = new OptionPicker(this);
                picker.setData(data);
                picker.setDefaultValue(5920);
                picker.setOnOptionPickedListener(this);
                picker.getWheelLayout().setOnOptionSelectedListener(new OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int position, Object item) {
                        picker.getTitleView().setText(picker.getWheelView().formatItem(position));
                    }
                });
                picker.getWheelView().setStyle(R.style.WheelStyleDemo);
                picker.show();
                break;
            case R.id.tvGroup:
                Intent intent = new Intent(this,GroupDataActivity.class);
                if (tvUnit.getText().toString().equals("MM")){
                    intent.putExtra("unit","dataMM");
                }else if (tvUnit.getText().toString().equals("IN")){
                    intent.putExtra("unit","dataIN");
                }
                startActivity(intent);
                break;
            case R.id.tvFMZT:
                writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
                if (tvFMZT.getText().toString().equals("开")) {
                    tvFMZT.setText("关");
                    if (mmUnit) {
                        if (smOpen) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010001015d");
                                }
                            }, 300);
                        } else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010000015d");
                                }
                            }, 300);

                        }
                    } else if (!mmUnit) {
                        if (smOpen) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010001005d");
                                }
                            }, 300);
                        } else {
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
                    if (mmUnit) {
                        if (smOpen) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010101015d");
                                }
                            }, 300);
                        } else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010100015d");
                                }
                            }, 300);
                        }
                    } else if (!mmUnit) {
                        if (smOpen) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010101005d");
                                }
                            }, 300);
                        } else {
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
                    if (fmOpen) {
                        if (smOpen) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010101005d");
                                }
                            }, 300);
                        } else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010100005d");
                                }
                            }, 300);

                        }
                    } else if (!fmOpen) {
                        if (smOpen) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010001005d");
                                }
                            }, 300);
                        } else {
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
                    if (fmOpen) {
                        if (smOpen) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010101015d");
                                }
                            }, 300);
                        } else {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010100015d");
                                }
                            }, 300);

                        }
                    } else if (!fmOpen) {
                        if (smOpen) {
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {//时间
                                    writeCommand(myBleDevice, characteristicWrite, "5b0008010001015d");
                                }
                            }, 300);
                        } else {
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
                selectTag = "fTopTag";
                NumberPicker fTopPicker = new NumberPicker(this);
                fTopPicker.setBodyWidth(120);
                fTopPicker.setOnNumberPickedListener(this);
                fTopPicker.setFormatter(new WheelFormatter() {
                    @Override
                    public String formatItem(@NonNull Object item) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        return df.format(item);
                    }
                });
                if (tvUnit.getText().toString().equals("MM")) {
                    fTopPicker.setRange(495f, 500f, 0.01f);
                } else {
                    fTopPicker.setRange(19f, 18f, 0.001f);
                }
                fTopPicker.setDefaultValue(5f);
                fTopPicker.getTitleView().setText("自定义阈值");
                fTopPicker.getTitleView().setTextColor(getResources().getColor(R.color.color_bg_selected1));
                fTopPicker.getTitleView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFDialog("请输入阈值上限");
                        fTopPicker.dismiss();
                    }
                });
                fTopPicker.show();
                break;
            case R.id.tvFBot:
                tag = "FBOT";
                selectTag = "fTopTag";
                NumberPicker fBotPicker = new NumberPicker(this);
                fBotPicker.setBodyWidth(120);
                fBotPicker.setOnNumberPickedListener(this);
                fBotPicker.setFormatter(new WheelFormatter() {
                    @Override
                    public String formatItem(@NonNull Object item) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        return df.format(item);
                    }
                });
                if (tvUnit.getText().toString().equals("MM")) {
                    fBotPicker.setRange(495f, 500f, 0.01f);
                } else {
                    fBotPicker.setRange(19f, 18f, 0.001f);
                }
                fBotPicker.setDefaultValue(5f);
                fBotPicker.getTitleView().setText("自定义阈值");
                fBotPicker.getTitleView().setTextColor(getResources().getColor(R.color.color_bg_selected1));
                fBotPicker.getTitleView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFDialog("请输入阈值下限");
                        fBotPicker.dismiss();
                    }
                });
                fBotPicker.show();
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
                myDialog.dismiss();
            }

            @Override
            public void cancel(String name2, Dialog dialog) {
            }
        });
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

    //读取Json文件
    private static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    public void onNumberPicked(int position, Number item) {
        if (selectTag.equals("fTopTag")) {
            String fTopData = String.valueOf(new Double(Double.valueOf(String.valueOf(item)) * 1000).intValue());
            writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {//时间
                    String strHex2 = String.format("%08x", Integer.parseInt(fTopData)).toUpperCase();//高位补0
                    strHex2 = strHex2.substring(6, 8) + strHex2.substring(4, 6) + strHex2.substring(2, 4) + strHex2.substring(0, 2);
                    //5b0006000000005d
                    writeCommand(myBleDevice, characteristicWrite, "5b0006" + strHex2 + "5d");
                }
            }, 300);
        }
        if (selectTag.equals("fBotTag")) {
            String fBotData = String.valueOf(new Double(Double.valueOf(String.valueOf(item)) * 1000).intValue());
            writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {//时间
                    String strHex2 = String.format("%08x", Integer.parseInt(fBotData)).toUpperCase();//高位补0
                    strHex2 = strHex2.substring(6, 8) + strHex2.substring(4, 6) + strHex2.substring(2, 4) + strHex2.substring(0, 2);
                    //5b0006000000005d
                    writeCommand(myBleDevice, characteristicWrite, "5b0007" + strHex2 + "5d");
                }
            }, 300);
        }
    }

    /**
     * 底部弹出框选择数据后的操作
     *
     * @param position
     * @param item
     */
    @Override
    public void onOptionPicked(int position, Object item) {
        if (selectTag.equals("ssTag")) {
            tvSS.setText(item.toString());
            writeCommand(myBleDevice, characteristicWrite, "5b0001000000005d");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {//时间
                    String strHex2 = String.format("%04x", Integer.parseInt(item.toString())).toUpperCase();//高位补0
                    strHex2 = strHex2.substring(2, 4) + strHex2.substring(0, 2);
                    writeCommand(myBleDevice, characteristicWrite, "5b0005" + strHex2 + "00005d");
                }
            }, 300);
        }
    }

    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

}