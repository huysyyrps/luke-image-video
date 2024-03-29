package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.util.BytesHexChange;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Setting;
import com.example.luke_imagevideo_send.chifen.magnetic.util.MainUI;
import com.example.luke_imagevideo_send.chifen.magnetic.util.MyCallBack;
import com.example.luke_imagevideo_send.chifen.magnetic.util.RegionalChooseUtil;
import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.chifen.magnetic.view.CircleMenu;
import com.example.luke_imagevideo_send.chifen.magnetic.view.CircleMenuAdapter;
import com.example.luke_imagevideo_send.chifen.magnetic.view.CustomNumberPicker;
import com.example.luke_imagevideo_send.chifen.magnetic.view.ItemInfo;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.base.Constant;
import com.example.luke_imagevideo_send.http.base.DialogCallBackTwo;
import com.example.luke_imagevideo_send.http.base.ModbusInstanceCallBack;
import com.example.luke_imagevideo_send.http.views.Header;
import com.example.luke_imagevideo_send.modbus.Crc16Util;
import com.example.luke_imagevideo_send.modbus.SocketForModbusTCP;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpideMainActivity extends BaseActivity implements NumberPicker.Formatter {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.btnStop)
    Button btnStop;
    @BindView(R.id.frameLayoutitem)
    FrameLayout frameLayoutitem;
    @BindView(R.id.tvSpeed)
    TextView tvSpeed;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvCHTime)
    TextView tvCHTime;
    @BindView(R.id.tvPatternSelect)
    TextView tvPatternSelect;
    @BindView(R.id.tvCEControl)
    TextView tvCEControl;
    @BindView(R.id.tvLightSelect)
    TextView tvLightSelect;
    @BindView(R.id.tvSearchlightControl)
    TextView tvSearchlightControl;
    @BindView(R.id.tvCHControl)
    TextView tvCHControl;
    @BindView(R.id.pickerData)
    CustomNumberPicker pickerData;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.llNumberPicker)
    LinearLayout llNumberPicker;
    @BindView(R.id.ivVisition)
    ImageView ivVisition;
    @BindView(R.id.rlGone)
    FrameLayout rlGone;
    @BindView(R.id.linSetting)
    LinearLayout linSetting;
    @BindView(R.id.tvSource)
    TextView tvSource;
    @BindView(R.id.tvCH)
    TextView tvCH;
    @BindView(R.id.tvLight)
    TextView tvLight;
    @BindView(R.id.tvActualDistance)
    TextView tvActualDistance;
    @BindView(R.id.cm_main)
    CircleMenu mCircleMenu;

    String tag = "";
    boolean loadError = false;
    String address = null;
    String address1 = null;
    String[] one;
    Timer timer = new Timer();
    @BindView(R.id.btnCEControl1)
    Button btnCEControl1;
    @BindView(R.id.btnCHControl1)
    Button btnCHControl1;
    @BindView(R.id.btnSearchlightControl1)
    Button btnSearchlightControl1;
    @BindView(R.id.btnLightSelect1)
    Button btnLightSelect1;
    private WindowManager mWindowManager;
    SocketForModbusTCP socketForModbusTCP;
    private String[] itemName = new String[4];
    private int[] mItemImgs = new int[4];
    private CircleMenuAdapter circleMenuAdapternew;
    List<ItemInfo> data = new ArrayList<>();
    static String TAG = "SpideMainActivity";
    Timer timer1 = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        pickerData.setNumberPickerDividerColor(pickerData);
        setUiData();
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        header.setVisibility(View.GONE);
        frameLayout.setBackgroundColor(getResources().getColor(R.color.black));
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        webView.setBackgroundColor(Color.BLACK);
        webView.getSettings().setJavaScriptEnabled(true);
        try {
            address = new getIp().getConnectIp();
            address1 = new getIp().getConnectIp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        address = "http://" + address + ":8080";
        webView.loadUrl(address);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.setVisibility(View.GONE);
//                imageView.setVisibility(View.GONE);
                linearLayout.setVerticalGravity(View.GONE);
                loadError = true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //DialogShow.showRoundProcessDialog();
            }
        });
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //设置自适应屏幕，两者合用
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //全屏设置
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        instanceModBus();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_spidemain;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {
    }

    private void setUiData() {
        mItemImgs[0] = R.drawable.ic_right;
        mItemImgs[1] = R.drawable.ic_botton;
        mItemImgs[2] = R.drawable.ic_left;
        mItemImgs[3] = R.drawable.ic_top;

        itemName[0] = "右";
        itemName[1] = "下";
        itemName[2] = "左";
        itemName[3] = "上";

        ItemInfo item = null;
        ItemInfo itemne = null;
        for (int i = 0; i < itemName.length; i++) {
            item = new ItemInfo(mItemImgs[i], itemName[i]);
            data.add(item);
        }
        circleMenuAdapternew = new CircleMenuAdapter(this, data);
        mCircleMenu.setAdapter(circleMenuAdapternew);
        setClient();
    }

    private void setClient() {
        mCircleMenu.setOnItemClickListener(new CircleMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (itemName[position].equals("上")) {
                    sendData("010600040001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600040001")), 300);
                }
                if (itemName[position].equals("左")) {
                    sendData("010600040003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600040003")), 300);
                }
                if (itemName[position].equals("右")) {
                    sendData("010600040004" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600040004")), 300);
                }
                if (itemName[position].equals("下")) {
                    sendData("010600040002" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600040002")), 300);
                }
//                if (itemName[position].equals("左上")) {
//                    sendData("010600040005" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600040005")),300);
//                }
//                if (itemName[position].equals("右上")) {
//                    sendData("010600040006" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600040006")),300);
//                }
            }
        });
    }

    /**
     * modbus建立连接
     */
    private void instanceModBus() {
        if (address1 == null) {
            Toast.makeText(this, "ip为空", Toast.LENGTH_SHORT).show();
        } else {
            socketForModbusTCP = new SocketForModbusTCP(address1, 502) {
                @Override
                protected void onDataReceived(byte[] readBuffer, int size) {
                    //todo 根据业务解析数据
                    if (readBuffer[1] == 3) {
                        if (readBuffer[2] == 48 && readBuffer.length == 53) {
                            byte[] byteActualDistance = new byte[4];
                            byteActualDistance[0] = readBuffer[35];
                            byteActualDistance[1] = readBuffer[36];
                            byteActualDistance[2] = readBuffer[37];
                            byteActualDistance[3] = readBuffer[38];
                            String actualDistanceHex = new BytesHexChange().ByteArrToHex(byteActualDistance);
                            actualDistanceHex = actualDistanceHex.substring(4, 8) + actualDistanceHex.substring(0, 4);
                            int ieee754ActualDistanceHex = Integer.parseInt(actualDistanceHex, 16);
                            float realActualDistanceHex = Float.intBitsToFloat(ieee754ActualDistanceHex);
                            tvActualDistance.setText(realActualDistanceHex + "mm");

                            byte[] byteSpeed = new byte[4];
                            byteSpeed[0] = readBuffer[39];
                            byteSpeed[1] = readBuffer[40];
                            byteSpeed[2] = readBuffer[41];
                            byteSpeed[3] = readBuffer[42];
                            String speenHex = new BytesHexChange().ByteArrToHex(byteSpeed);
                            speenHex = speenHex.substring(4, 8) + speenHex.substring(0, 4);
                            int ieee754Speed = Integer.parseInt(speenHex, 16);
                            float realApeed = Float.intBitsToFloat(ieee754Speed);
                            tvSpeed.setText(realApeed + "mm/min");

                            byte[] byteDistance = new byte[4];
                            byteDistance[0] = readBuffer[43];
                            byteDistance[1] = readBuffer[44];
                            byteDistance[2] = readBuffer[45];
                            byteDistance[3] = readBuffer[46];
                            String distanceHex = new BytesHexChange().ByteArrToHex(byteDistance);
                            distanceHex = distanceHex.substring(4, 8) + distanceHex.substring(0, 4);
                            int ieee754Distance = Integer.parseInt(distanceHex, 16);
                            float realDistance = Float.intBitsToFloat(ieee754Distance);
                            tvDistance.setText(realDistance + "mm");

                            byte[] byteCHTime = new byte[4];
                            byteCHTime[0] = readBuffer[47];
                            byteCHTime[1] = readBuffer[48];
                            byteCHTime[2] = readBuffer[49];
                            byteCHTime[3] = readBuffer[50];
                            String cHTimeHex = new BytesHexChange().ByteArrToHex(byteCHTime);
                            cHTimeHex = cHTimeHex.substring(4, 8) + cHTimeHex.substring(0, 4);
                            int ieee754CHTime = Integer.parseInt(cHTimeHex, 16);
                            float realCHTime = Float.intBitsToFloat(ieee754CHTime);
                            tvCHTime.setText(realCHTime + "s");

                            String strHex2 = "010400000009" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010400000009"));
                            sendData1(strHex2);
                        }
                    }
                    if (readBuffer[1] == 4) {
                        if (readBuffer[2] == 18 && readBuffer.length == 23) {
                            byte[] byteOperation = new byte[2];
                            String OCMode = "";
                            byteOperation[0] = readBuffer[3];
                            byteOperation[1] = readBuffer[4];
                            String operationHex = new BytesHexChange().ByteArrToHex(byteOperation);
                            if (operationHex.equals("0001")) {
                                OCMode = "自动\t\t";
                            } else if (operationHex.equals("0003")) {
                                OCMode = "手动\t\t";
                            }

                            byte[] byteCrawl = new byte[2];
                            byteCrawl[0] = readBuffer[5];
                            byteCrawl[1] = readBuffer[6];
                            String crawlHex = new BytesHexChange().ByteArrToHex(byteCrawl);
                            if (crawlHex.equals("0001")) {
                                OCMode = OCMode + "连续";
                            } else if (crawlHex.equals("0003")) {
                                OCMode = OCMode + "断续";
                            }
                            tvPatternSelect.setText(OCMode);

                            byte[] byteCHMode = new byte[2];
                            byteCHMode[0] = readBuffer[7];
                            byteCHMode[1] = readBuffer[8];
                            String cHModeHex = new BytesHexChange().ByteArrToHex(byteCHMode);
                            if (cHModeHex.equals("0001")) {
                                tvCH.setText("单击");
                            } else if (cHModeHex.equals("0003")) {
                                tvCH.setText("双击");
                            }

                            byte[] byteLight = new byte[2];
                            byteLight[0] = readBuffer[9];
                            byteLight[1] = readBuffer[10];
                            String lightHex = new BytesHexChange().ByteArrToHex(byteLight);
                            if (lightHex.equals("0001")) {
                                tvLight.setText("自动");
                            } else if (lightHex.equals("0003")) {
                                tvLight.setText("手动");
                            }

                            byte[] byteCEControl = new byte[2];
                            byteCEControl[0] = readBuffer[13];
                            byteCEControl[1] = readBuffer[14];
                            String cEControlHex = new BytesHexChange().ByteArrToHex(byteCEControl);
                            if (cEControlHex.equals("0001")) {
                                tvCEControl.setText("抬起");
                                btnCEControl1.setText("磁轭抬起");
                            } else if (cEControlHex.equals("0003")) {
                                tvCEControl.setText("落下");
                                btnCEControl1.setText("磁轭落下");
                            }

                            byte[] byteLightSelect = new byte[2];
                            byteLightSelect[0] = readBuffer[15];
                            byteLightSelect[1] = readBuffer[16];
                            String lightSelectHex = new BytesHexChange().ByteArrToHex(byteLightSelect);
                            if (lightSelectHex.equals("0001")) {
                                tvLightSelect.setText("黑光");
                                btnLightSelect1.setText("黑光");
                            } else if (lightSelectHex.equals("0003")) {
                                tvLightSelect.setText("白光");
                                btnLightSelect1.setText("黑光");
                            }else {
                                tvLightSelect.setText("关闭");
                                btnLightSelect1.setText("关闭");
                            }

                            byte[] byteSearchlight = new byte[2];
                            byteSearchlight[0] = readBuffer[17];
                            byteSearchlight[1] = readBuffer[18];
                            String searchlightHex = new BytesHexChange().ByteArrToHex(byteSearchlight);
                            if (searchlightHex.equals("0001")) {
                                tvSearchlightControl.setText("开启");
                                btnSearchlightControl1.setText("探照灯开启");
                            } else if (searchlightHex.equals("0003")) {
                                tvSearchlightControl.setText("关闭");
                                btnSearchlightControl1.setText("探照灯关闭");
                            }

                            byte[] byteSource = new byte[2];
                            byteSource[0] = readBuffer[19];
                            byteSource[1] = readBuffer[20];
                            String sourceHex = new BytesHexChange().ByteArrToHex(byteSource);
                            if (sourceHex.equals("0001")) {
                                tvSource.setText("手机使能");
                            } else if (sourceHex.equals("0002")) {
                                tvSource.setText("遥控器使能");
                            } else if (sourceHex.equals("0003")) {
                                tvSource.setText("键盘使能");
                            }
                        }
                    }
                }
            };
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    socketForModbusTCP.connect(new ModbusInstanceCallBack() {
                        @Override
                        public void confirm(Object string) {
                            sendTimerData();
                        }

                        @Override
                        public void error(String string) {
                            Message message = new Message();
                            message.what = Constant.TAG_ONE;
                            message.obj = string;
                            handler.sendMessage(message);
                        }
                    });
                }
            }, 300);
        }

//        new ModBusUtil().modbusInstance(new ModbusInstanceCallBack() {
//            @Override
//            public void confirm(Object string) {
//                Log.e(TAG, string.toString());
//                frameLayoutitem.setVisibility(View.VISIBLE);
//                new ModBusUtil().modbusReadHoldingRegisters(1, 0, 1, new ModbusInstanceCallBack() {
//                    @Override
//                    public void confirm(Object string) {
//                        Log.e(TAG, "readHoldingRegisters onSuccess " +string.toString());
//                    }
//
//                    @Override
//                    public void error(String string) {
//                        Log.e(TAG, "readHoldingRegisters onFailed " + string.toString());
//                    }
//                });
//            }
//
//            @Override
//            public void error(String string) {
//                Log.e(TAG, string);
//                frameLayoutitem.setVisibility(View.GONE);
//            }
//        });
    }

    private void sendTimerData() {
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                String strHex1 = "0103000000018" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("0103000000018"));
                sendData(strHex1, 300);
            }
        },100, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        webView.loadUrl(address);
    }

    @OnClick({R.id.btnStop, R.id.tvSpeed, R.id.tvDistance, R.id.tvCHTime, R.id.tvPatternSelect,
            R.id.tvCEControl, R.id.tvLightSelect, R.id.tvSearchlightControl, R.id.tvCHControl,
            R.id.pickerData, R.id.tvCancle, R.id.tvSure, R.id.ivVisition, R.id.tvCH, R.id.rlGone,
            R.id.tvLight, R.id.tvSource,R.id.btnCEControl1, R.id.btnCHControl1, R.id.btnSearchlightControl1, R.id.btnLightSelect1})
    public void onClick(View view1) {
        switch (view1.getId()) {
            case R.id.ivVisition:
                linSetting.setVisibility(View.VISIBLE);
                ivVisition.setVisibility(View.GONE);
                rlGone.setVisibility(View.VISIBLE);
                break;
            case R.id.rlGone:
                linSetting.setVisibility(View.GONE);
                ivVisition.setVisibility(View.VISIBLE);
                rlGone.setVisibility(View.GONE);
                break;
            case R.id.btnStop:
                sendData("010600040007" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600040007")), 300);
                break;
            case R.id.tvSource://控制源
                tag = "Source";
                initPickerData();
                llNumberPicker.setVisibility(View.VISIBLE);
                break;
            case R.id.tvCH://磁化模式
                tag = "CH";
                initPickerData();
                llNumberPicker.setVisibility(View.VISIBLE);
                break;
            case R.id.tvLight://黑白光模式
                tag = "Light";
                initPickerData();
                llNumberPicker.setVisibility(View.VISIBLE);
                break;
            case R.id.tvSpeed://爬行速度
                tag = "Speed";
                initPickerData();
                llNumberPicker.setVisibility(View.VISIBLE);
                break;
            case R.id.tvDistance://行走距离
                tag = "Distance";
                showFDialog();
                break;
            case R.id.tvCHTime://磁化时间
                tag = "CHTime";
                showFDialog();
                break;
            case R.id.tvPatternSelect://运行模式
                RegionalChooseUtil.initJsonData(SpideMainActivity.this, "pxq");
                RegionalChooseUtil.showPickerView(SpideMainActivity.this, new MyCallBack() {
                    @Override
                    public void callBack(Object object) {
                        if (object.toString().equals("自动模式连续模式")) {
                            tvPatternSelect.setText("自动\t连续");
                            sendData("010600000001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600000001")), 300);
                            sendData1("010600010001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600010001")));
                        } else if (object.toString().equals("自动模式断续模式")) {
                            tvPatternSelect.setText("自动\t断续");
                            sendData("010600000001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600000001")), 300);
                            sendData1("010600010003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600010003")));
                        } else if (object.toString().equals("手动模式单击模式")) {
                            tvPatternSelect.setText("手动\t单击");
                            sendData("010600000003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600000003")), 300);
                            sendData1("010600010001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600010001")));
                        } else if (object.toString().equals("手动模式双击模式")) {
                            tvPatternSelect.setText("手动\t双击");
                            sendData("010600000003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600000003")), 300);
                            sendData1("010600010003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600010003")));
                        }
                    }
                });
                break;
            case R.id.btnCEControl1:
                tag = "CEControl";
                if (btnCEControl1.getText().toString().equals("磁轭落下")){
                    btnCEControl1.setText("磁轭抬起");
                    sendData("010600050001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600050001")), 300);
                }else {
                    btnCEControl1.setText("磁轭落下");
                    sendData("010600050003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600050003")), 300);
                }
                break;
            case R.id.btnCHControl1:
                tag = "CHControl";
                if (btnCHControl1.getText().toString().equals("磁化开启")){
                    btnCHControl1.setText("磁化关闭");
//                    sendData("010600050001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600050001")), 300);
                }else {
                    btnCHControl1.setText("磁化开启");
//                    sendData("010600050003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600050003")), 300);
                }
                break;
            case R.id.btnSearchlightControl1:
                tag = "SearchlightControl";
                if (btnSearchlightControl1.getText().toString().equals("探照灯关闭")){
                    btnSearchlightControl1.setText("探照灯开启");
                    sendData("010600070001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600070001")), 300);
                }else {
                    btnSearchlightControl1.setText("探照灯关闭");
                    sendData("010600070003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600070003")), 300);
                }
                break;
            case R.id.btnLightSelect1:
                tag = "LightSelect";
                new MainUI().showPopupMenuLight(btnLightSelect1, this, new AlertDialogCallBack() {
                    @Override
                    public void confirm(String name) {
                        btnLightSelect1.setText("关闭");
                    }

                    @Override
                    public void cancel() {
                        btnLightSelect1.setText("黑光");
                        sendData("010600060001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600060001")), 300);
                    }

                    @Override
                    public void save(String name) {

                    }

                    @Override
                    public void checkName(String name) {
                        btnLightSelect1.setText("白光");
                        sendData("010600060003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600060003")), 300);
                    }
                });
                break;
            case R.id.tvCEControl://磁轭控制
                tag = "CEControl";
                initPickerData();
                llNumberPicker.setVisibility(View.VISIBLE);
                break;
            case R.id.tvLightSelect://黑白选择
                tag = "LightSelect";
                initPickerData();
                llNumberPicker.setVisibility(View.VISIBLE);
                break;
            case R.id.tvSearchlightControl://探照灯
                tag = "SearchlightControl";
                initPickerData();
                llNumberPicker.setVisibility(View.VISIBLE);
                break;
            case R.id.tvCHControl://磁化控制
                tag = "CHControl";
                initPickerData();
                llNumberPicker.setVisibility(View.VISIBLE);
                break;
            case R.id.tvCancle:
                llNumberPicker.setVisibility(View.GONE);
                break;
            case R.id.tvSure:
                llNumberPicker.setVisibility(View.GONE);
                if (tag.equals("CH")) {
                    if (pickerData.getValue() == 0) {
                        tvCH.setText("单击");
                        sendData("010600020001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600020001")), 300);
                    } else if (pickerData.getValue() == 1) {
                        tvCH.setText("双击");
                        sendData("010600020003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600020003")), 300);
                    }
                }
                if (tag.equals("Light")) {
                    if (pickerData.getValue() == 0) {
                        tvLight.setText("自动");
                        sendData("010600030001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600030001")), 300);
                    } else if (pickerData.getValue() == 1) {
                        tvLight.setText("手动");
                        sendData("010600030003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600030003")), 300);
                    }
                }
                if (tag.equals("Source")) {
                    one = new String[]{"手机使能", "遥控器使能", "键盘使能"};
                    if (pickerData.getValue() == 0) {
                        tvSource.setText("手机使能");
                        sendData("010600080001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600080001")), 300);
                    } else if (pickerData.getValue() == 1) {
                        tvSource.setText("遥控器使能");
                        sendData("010600080002" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600080002")), 300);
                    } else if (pickerData.getValue() == 2) {
                        tvSource.setText("键盘使能");
                        sendData("010600080003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600080003")), 300);
                    }
                }

                if (tag.equals("Speed")) {
                    String ssdata = String.valueOf(pickerData.getValue() * 0.5 + 0.5);
                    tvSpeed.setText(ssdata + "mm/min");
                    float data = Float.parseFloat(ssdata);
                    String s = Integer.toHexString(Float.floatToRawIntBits(data)).substring(4, 8) + Integer.toHexString(Float.floatToRawIntBits(data)).substring(0, 4);
                    String data1 = "01100018000204" + s + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("01100018000204" + s));
                    sendData("01100018000204" + s + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("01100018000204" + s)), 300);
                }

                if (tag.equals("CEControl")) {
                    if (pickerData.getValue() == 0) {
                        tvCEControl.setText("抬起");
                        sendData("010600050001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600050001")), 300);
                    } else if (pickerData.getValue() == 1) {
                        tvCEControl.setText("落下");
                        sendData("010600050003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600050003")), 300);
                    }
                }
                if (tag.equals("LightSelect")) {
                    if (pickerData.getValue() == 0) {
                        tvLightSelect.setText("黑光");
                        sendData("010600060001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600060001")), 300);
                    } else if (pickerData.getValue() == 1) {
                        tvLightSelect.setText("白光");
                        sendData("010600060003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600060003")), 300);
                    }
                }
                if (tag.equals("SearchlightControl")) {
                    if (pickerData.getValue() == 0) {
                        tvSearchlightControl.setText("开启");
                        sendData("010600070001" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600070001")), 300);
                    } else if (pickerData.getValue() == 1) {
                        tvSearchlightControl.setText("关闭");
                        sendData("010600070003" + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("010600070003")), 300);
                    }
                }
                if (tag.equals("CHControl")) {
                    if (pickerData.getValue() == 0) {
                        tvCHControl.setText("磁化开启");
                    } else if (pickerData.getValue() == 1) {
                        tvCHControl.setText("磁化关闭");
                    }
                }
                break;
        }
    }

    public void sendData(String data, int time) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//运行模式
                Log.e(TAG, data);
                if (socketForModbusTCP != null) {
                    socketForModbusTCP.send(new BytesHexChange().HexToByteArr(data));
                } else {
                    Message message = new Message();
                    message.what = Constant.TAG_ONE;
                    message.obj = "未建立连接";
                    handler.sendMessage(message);
                }
            }
        }, time);
    }

    public void sendData1(String data) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//运行模式
                Log.e(TAG, data);
                if (socketForModbusTCP != null) {
                    socketForModbusTCP.send(new BytesHexChange().HexToByteArr(data));
                } else {
                    Looper.prepare();
                    Toast.makeText(SpideMainActivity.this, "未建立连接", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }, 600);
    }

    private void showFDialog() {
        String hint = "";
        if (tag.equals("Distance")) {
            hint = "请输入行走距离";
        } else if (tag.equals("CHTime")) {
            hint = "请输入磁化时间";
        }
        new AlertDialogUtil(this).showDialogF(hint, new DialogCallBackTwo() {
            @Override
            public void confirm(String name1, Dialog dialog, EditText editText) {
                if (tag.equals("Distance")) {
                    if (name1 != null && !name1.trim().equals("")) {
                        Double data = new Double(name1);
                        if (data > 80.0 || data < 10.0) {
                            Toast.makeText(SpideMainActivity.this, "行走距离范围：10.0~80.0，请重新输入", Toast.LENGTH_SHORT).show();
                        } else {
                            tvDistance.setText(name1 + "mm");
                            float distanceData = Float.parseFloat(name1);
                            String s = Integer.toHexString(Float.floatToRawIntBits(distanceData)).substring(4, 8)
                                    + Integer.toHexString(Float.floatToRawIntBits(distanceData)).substring(0, 4);
                            sendData("01100020000204" + s + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("01100020000204" + s)), 300);
                            dialog.dismiss();
                        }
                    } else {
                        Toast.makeText(SpideMainActivity.this, "请输入行走距离", Toast.LENGTH_SHORT).show();
                    }
                }
                if (tag.equals("CHTime")) {
                    if (name1 != null && !name1.trim().equals("")) {
                        tvCHTime.setText(name1 + "s");
                        Double data = new Double(name1);
                        if (data > 30.0 || data < 1.0) {
                            Toast.makeText(SpideMainActivity.this, "磁化时间范围：1.0~30.0，请重新输入", Toast.LENGTH_SHORT).show();
                        } else {
                            float timeData = Float.parseFloat(name1);
                            String s = Integer.toHexString(Float.floatToRawIntBits(timeData)).substring(4, 8)
                                    + Integer.toHexString(Float.floatToRawIntBits(timeData)).substring(0, 4);
                            sendData("01100022000204" + s + new Crc16Util().getTableCRC(new BytesHexChange().hexStringToBytes("01100022000204" + s)), 300);
                            dialog.dismiss();
                        }
                    } else {
                        Toast.makeText(SpideMainActivity.this, "请输入磁化时间", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void cancel(String name2, Dialog dialog) {
            }
        });
    }

    public void initPickerData() {
        if (tag.equals("Speed")) {
            one = new String[]{"0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5"};
        }
        if (tag.equals("Distance")) {
            one = new String[]{"10.0", "20.0", "30.0", "40.0", "50.0", "60.0", "70.0", "80.0"};
        }
        if (tag.equals("CHTime")) {
            one = new String[]{"1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0", "8.0", "9.0", "10.0",
                    "11.0", "12.0", "13.0", "14.0", "15.0", "16.0", "17.0", "18.0", "19.0", "20.0",
                    "21.0", "22.0", "23.0", "24.0", "25.0", "26.0", "27.0", "28.0", "29.0", "30.0",};
        }
        if (tag.equals("CEControl")) {
            one = new String[]{"抬起", "落下"};
        }
        if (tag.equals("LightSelect")) {
            one = new String[]{"黑光", "白光"};
        }
        if (tag.equals("SearchlightControl")) {
            one = new String[]{"开启", "关闭"};
        }
        if (tag.equals("CHControl")) {
            one = new String[]{"磁化开启", "磁化关闭"};
        }
        if (tag.equals("CH")) {
            one = new String[]{"单击", "双击"};
        }
        if (tag.equals("Light")) {
            one = new String[]{"自动", "手动"};
        }
        if (tag.equals("Source")) {
            one = new String[]{"手机使能", "遥控器使能", "键盘使能"};
        }
        pickerData.setFormatter(this);
        pickerData.setDisplayedValues(null);
        pickerData.setMinValue(0);
        pickerData.setMaxValue(one.length - 1);
        pickerData.setDisplayedValues(one);
        pickerData.setValue(0);
        pickerData.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        //这里设置为不循环显示，默认值为true
        pickerData.setWrapSelectorWheel(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent backdata) {
        super.onActivityResult(requestCode, resultCode, backdata);
        switch (requestCode) {
            case Constant.TAG_TWO:
                if (resultCode == Constant.TAG_ONE) {
                    Setting setting = (Setting) backdata.getSerializableExtra("data");
                    Gson gson = new Gson();
                    String obj2 = gson.toJson(setting);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ModbusReq.getInstance().destory();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        socketForModbusTCP.close();
        finish();
        return super.onKeyDown(keyCode, event);
    }

    //消息处理者,创建一个Handler的子类对象,目的是重写Handler的处理消息的方法(handleMessage())
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TAG_ONE:
                    Toast.makeText(SpideMainActivity.this, String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}