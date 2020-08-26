package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.Manifest;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.LoadingDialog;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.StatusBarUtils;
import com.example.luke_imagevideo_send.chifen.magnetic.view.RecyclerViewDelegate;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.SweetSheet;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 磁粉检测上传方式选择页
 */
public class SendSelectActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private ImageView ivBack;
    private TextView tvHeader;
    private EditText etCompName,etWorkName,etWorkCode;
    //富有动感的Sheet弹窗
    private SweetSheet sheet;
    Intent intent;
    private static boolean isExit = false;
    private static AlertDialogUtil alertDialogUtil;
    SharePreferencesUtils sharePreferencesUtils;
    MediaProjectionManager projectionManager;
    //推出程序
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //推出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            alertDialogUtil.showDialog("您确定要退出程序吗", new AlertDialogCallBack() {

                @Override
                public void confirm(String name) {
                    finish();
                }

                @Override
                public void cancel() {

                }

                @Override
                public void save(String name) {

                }

                @Override
                public void checkName(String name) {

                }
            });
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_select);
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        alertDialogUtil = new AlertDialogUtil(this);
        sharePreferencesUtils = new SharePreferencesUtils();
        new StatusBarUtils().setWindowStatusBarColor(SendSelectActivity.this, R.color.color_bg_selected);
        relativeLayout = findViewById(R.id.relativeLayout);
        ivBack = findViewById(R.id.iv_left);
        tvHeader = findViewById(R.id.tv_tittle);
        etCompName = findViewById(R.id.etCompName);
        etWorkName = findViewById(R.id.etWorkName);
        etWorkCode = findViewById(R.id.etWorkCode);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LoadingDialog loadingDialog = new LoadingDialog(this,"连接服务中",R.mipmap.ic_dialog_loading);
        loadingDialog.show();
        connectModBus();

        initData();
    }

    /**
     * 连接modbus
     */
    private void connectModBus() {
        String host = "";
        String port = "";
//        param = TcpParam.create(host, port)
//                .setTimeout(1000)
//                .setRetries(0)
//                .setEncapsulated(false)
//                .setKeepAlive(true);
//
//        ModbusManager.get().closeModbusMaster();
//        ModbusManager.get().init(param, new ModbusCallback<ModbusMaster>() {
//        @Override
//        public void onSuccess(ModbusMaster modbusMaster) {
//            showOneToast("打开成功");
//        }
//
//        @Override
//        public void onFailure(Throwable tr) {
//            showOneToast("打开失败," + tr);
//        }
//
//        @Override
//        public void onFinally() {
//            updateDeviceSwitchButton();
//        }
//    });
    }

//    /**
//     * 获取权限
//     */
//    private void initPermission() {
//        Intent intent = new Intent();
//        intent.setClassName("com.android.systemui", "com.android.systemui.media.MediaProjectionPermissionActivity");
//        startActivityForResult(intent, Constant.TAG_ONE);
//    }

//    /**
//     * 录屏权限回掉
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode==Constant.TAG_ONE){
////            if (resultCode==RESULT_OK){
//        Constant.mediaProjection = projectionManager.getMediaProjection(resultCode, data);
//        initData();
////            }
////        }
//    }

    //设置SweetSheet上的数据
    public void initData() {

        ArrayList<MenuEntity> list = new ArrayList<>();
        //添加测试数据
        MenuEntity bean1 = new MenuEntity();
        bean1.title = "上传方式";

        MenuEntity bean2 = new MenuEntity();
        bean2.iconId = R.drawable.ic_bendi;
        bean2.title = "本地存储";

        MenuEntity bean3 = new MenuEntity();
        bean3.iconId = R.drawable.ic_shishi;
        bean3.title = "实时上传";

        list.add(bean1);
        list.add(bean2);
        list.add(bean3);

        //声明SweetSheet 控件,根据 layout 确认位置
        sheet = new SweetSheet(relativeLayout);
        //设置数据源 (数据源支持设置 list 数组,也支持从menu 资源中获取)
        sheet.setMenuList(list);
        //根据设置不同的 Delegate 来显示不同的风格
        sheet.setDelegate(new RecyclerViewDelegate(true,this));
//        sheet.setDelegate(new ViewPagerDelegate(2));
        //根据设置不同Effect来设置背景效果:BlurEffect 模糊效果.DimEffect 变暗效果,NoneEffect 没有效果
        sheet.setBackgroundEffect(new DimEffect(8));
        //设置点击事件
        sheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
            @Override
            public boolean onItemClick(int position, MenuEntity menuEntity) {
//                Toast.makeText(SendSelectActivity.this, "点击了：" + menuEntity.title, Toast.LENGTH_SHORT).show();
                //根据返回值, true 会关闭 SweetSheet ,false 则不会
                sharePreferencesUtils.setString(SendSelectActivity.this,"compName",etCompName.getText().toString());
                sharePreferencesUtils.setString(SendSelectActivity.this,"workName",etWorkName.getText().toString());
                sharePreferencesUtils.setString(SendSelectActivity.this,"workCode",etWorkCode.getText().toString());
                if (menuEntity.title.equals("本地存储")){
                    sharePreferencesUtils.setString(SendSelectActivity.this,"sendSelect","本地存储");
                    intent = new Intent(SendSelectActivity.this,MainActivity.class);
//                    intent = new Intent(SendSelectActivity.this, TestActivity.class);
                    startActivity(intent);
                    finish();
                }else if (menuEntity.title.equals("实时上传")){
                    sharePreferencesUtils.setString(SendSelectActivity.this,"sendSelect","实时上传");
                    intent = new Intent(SendSelectActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        sheet.toggle();
    }
}