package com.example.luke_imagevideo_send.main.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.camera.CameraUtils;
import com.example.luke_imagevideo_send.camera.bean.HandShakeBean;
import com.example.luke_imagevideo_send.camera.bean.PulseBean;
import com.example.luke_imagevideo_send.camera.bean.RedirectException;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.impl.client.action.ActionDispatcher;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import com.xuhao.didi.socket.client.sdk.client.connection.NoneReconnect;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class TestMainActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.rbCamera)
    RadioButton rbCamera;
    @BindView(R.id.rbVideo)
    RadioButton rbVideo;
    @BindView(R.id.rbAlbum)
    RadioButton rbAlbum;
    @BindView(R.id.rbSetting)
    RadioButton rbSetting;
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private static final String TAG = "PlayActivity";
    private CameraUtils cameraUtils;
    private String path, name;
    int x = 0;
    private ConnectionInfo mInfo;
    private IConnectionManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        makeManagerInfor();
        setListener();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        surfaceView.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        List<PermissionItem> mList = new ArrayList<PermissionItem>();
        mList.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_phone));
        mList.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        mList.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "位置", R.drawable.permission_ic_location));
        mList.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取文件", R.drawable.permission_ic_storage));
        mList.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入文件", R.drawable.permission_ic_storage));

        HiPermission.create(TestMainActivity.this)
                .title("亲爱的用户")
                .permissions(mList)
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.green, getTheme()))//图标的颜色
                .animStyle(R.style.PermissionAnimScale)//设置动画
                .msg("此应用需要获取以下权限")
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.e("TAG", "close");
                    }

                    @Override
                    public void onFinish() {
                        //"所有权限申请完成"
                        Toast.makeText(TestMainActivity.this, "所有权限申请完毕", Toast.LENGTH_SHORT).show();
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
                        cameraUtils = new CameraUtils();
                        cameraUtils.create(surfaceView, TestMainActivity.this);
                        surfaceView.setVisibility(View.VISIBLE);
                        radioGroup.setVisibility(View.VISIBLE);
                        header.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                    }
                });
    }

    /**
     * 设置socket地址
     */
    private void makeManagerInfor() {
        mInfo = new ConnectionInfo("111.38.216.169", 50003);

        final Handler handler = new Handler(Looper.getMainLooper());
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder();
        builder.setReconnectionManager(new NoneReconnect());
        builder.setCallbackThreadModeToken(new OkSocketOptions.ThreadModeToken() {
            @Override
            public void handleCallbackEvent(ActionDispatcher.ActionRunnable runnable) {
                handler.post(runnable);
            }
        });
        mManager = OkSocket.open(mInfo).option(builder.build());
    }

    private void setListener() {
        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        mManager.registerReceiver(adapter);

        if (mManager == null) {
            return;
        }
        if (!mManager.isConnect()) {
            //调用通道进行连接
            mManager.connect();
        } else {
            mManager.disconnect();
        }
    }

    /**
     * socketAdapter(socket回调)
     */
    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            mManager.send(new HandShakeBean());
            initSwitch();
            //获取到心跳管理器,用来配置心跳参数和心跳行为
            mManager.getPulseManager().setPulseSendable(new PulseBean());
        }

        private void initSwitch() {
            OkSocketOptions okSocketOptions = mManager.getOption();
        }

        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
            if (e != null) {
                if (e instanceof RedirectException) {
                    Toast.makeText(TestMainActivity.this, "正在重定向连接(Redirect Connecting)...", Toast.LENGTH_SHORT).show();
                    mManager.switchConnectionInfo(((RedirectException) e).redirectInfo);
                    mManager.connect();
                }
            }
        }

        @Override
        public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
            Log.e("XXX","连接失败(Connecting Failed)");
        }

        //通讯从服务器读取到消息后的响应
        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            String str = new String(data.getBodyBytes(), Charset.forName("utf-8"));
            JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
            int cmd = jsonObject.get("cmd").getAsInt();
            if (cmd == 54) {//登陆成功
                String handshake = jsonObject.get("handshake").getAsString();
                Log.e("XXX","握手成功! 握手信息(Handshake Success):" + handshake + ". 开始心跳(Start Heartbeat)..");
            } else if (cmd == 57) {//切换,重定向.(暂时无法演示,如有疑问请咨询github)
                String ip = jsonObject.get("data").getAsString().split(":")[0];
                int port = Integer.parseInt(jsonObject.get("data").getAsString().split(":")[1]);
                ConnectionInfo redirectInfo = new ConnectionInfo(ip, port);
                redirectInfo.setBackupInfo(mInfo.getBackupInfo());
                mManager.getReconnectionManager().addIgnoreException(RedirectException.class);
                mManager.disconnect(new RedirectException(redirectInfo));
            } else if (cmd == 14) {//心跳
                Log.e("XXX","收到心跳,喂狗成功(Heartbeat Received,Feed the Dog)");
                mManager.getPulseManager().feed();
            } else {
                Toast.makeText(TestMainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }

        //通讯写出后的响应回调
        @Override
        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
            byte[] bytes = data.parse();
            bytes = Arrays.copyOfRange(bytes, 4, bytes.length);
            String str = new String(bytes, Charset.forName("utf-8"));
            JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
            int cmd = jsonObject.get("cmd").getAsInt();
            switch (cmd) {
                case 54: {
                    String handshake = jsonObject.get("handshake").getAsString();
                    Log.e("XXX","发送握手数据(Handshake Sending):" + handshake);
                    mManager.getPulseManager().pulse();
                    break;
                }
                case 14: {
                    mManager.getPulseManager().feed();
                    break;
                }
                default:
                    Toast.makeText(TestMainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }

        //心跳发送后的回调
        @Override
        public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
            byte[] bytes = data.parse();
            bytes = Arrays.copyOfRange(bytes, 4, bytes.length);
            String str = new String(bytes, Charset.forName("utf-8"));
            JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
            int cmd = jsonObject.get("cmd").getAsInt();
            if (cmd == 14) {
                Log.e("XXX","发送心跳包(Heartbeat Sending)");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        cameraUtils.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        cameraUtils.destroy();
        if (mManager != null) {
            mManager.disconnect();
            mManager.unRegisterReceiver(adapter);
        }
    }

    @OnClick({R.id.rbCamera, R.id.rbVideo, R.id.rbAlbum, R.id.rbSetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbCamera:
                cameraUtils.takePicture(path, getNowDate() + ".png");
                break;
            case R.id.rbVideo:
                if (x == 0) {
                    // cameraUtils.changeCamera();
                    cameraUtils.startRecord(path, getNowDate());
//                    btn.setImageResource(R.drawable.video);
                    x = 1;
                } else if (x == 1) {
                    cameraUtils.stopRecord();
//                    btn.setImageResource(R.drawable.video1);
                    x = 0;
                }
                break;
            case R.id.rbAlbum:
//                Intent intent = new Intent(this, PhotoActivity.class);
//                startActivity(intent);
                break;
            case R.id.rbSetting:
                cameraUtils.changeCamera();
                break;
        }
    }

    /**
     * 获取当前时间,用来给文件夹命名
     */
    private String getNowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }
}