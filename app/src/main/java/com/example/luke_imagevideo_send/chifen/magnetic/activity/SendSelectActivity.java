package com.example.luke_imagevideo_send.chifen.magnetic.activity;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.magnetic.bean.Test01;
import com.example.luke_imagevideo_send.chifen.magnetic.module.TestContract;
import com.example.luke_imagevideo_send.chifen.magnetic.presenter.TestPresenter;
import com.example.luke_imagevideo_send.http.base.AlertDialogCallBack;
import com.example.luke_imagevideo_send.http.base.AlertDialogUtil;
import com.example.luke_imagevideo_send.http.base.LoadingDialog;
import com.example.luke_imagevideo_send.http.utils.SharePreferencesUtils;
import com.example.luke_imagevideo_send.http.views.StatusBarUtils;
import com.example.luke_imagevideo_send.chifen.magnetic.view.RecyclerViewDelegate;
import com.example.luke_imagevideo_send.modbus.Modbus4jWriteUtils;
import com.example.luke_imagevideo_send.modbus.ModbusCallback;
import com.example.luke_imagevideo_send.modbus.ModbusManager;
import com.example.luke_imagevideo_send.yingduji.activity.MainYDJActivity;
import com.licheedev.modbus4android.param.TcpParam;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.SweetSheet;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

import java.util.ArrayList;

/**
 * 磁粉检测上传方式选择页
 */
public class SendSelectActivity extends AppCompatActivity implements TestContract.View {
    private RelativeLayout relativeLayout;
    private ImageView ivBack;
    private TextView tvHeader;
    private EditText etCompName, etWorkName, etWorkCode;
    //富有动感的Sheet弹窗
    private SweetSheet sheet;
    Intent intent;
    private short[] mRegValues;
    private static boolean isExit = false;
    private static AlertDialogUtil alertDialogUtil;
    SharePreferencesUtils sharePreferencesUtils;
    MediaProjectionManager projectionManager;
    TestPresenter testPresenter;
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
        initData();

        testPresenter = new TestPresenter(this,this);
        testPresenter.getTest();
    }
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
        bean3.title = "实时上传（点击进入硬度计界面）";

        list.add(bean1);
        list.add(bean2);
        list.add(bean3);

        //声明SweetSheet 控件,根据 layout 确认位置
        sheet = new SweetSheet(relativeLayout);
        //设置数据源 (数据源支持设置 list 数组,也支持从menu 资源中获取)
        sheet.setMenuList(list);
        //根据设置不同的 Delegate 来显示不同的风格
        sheet.setDelegate(new RecyclerViewDelegate(true, this));
//        sheet.setDelegate(new ViewPagerDelegate(2));
        //根据设置不同Effect来设置背景效果:BlurEffect 模糊效果.DimEffect 变暗效果,NoneEffect 没有效果
        sheet.setBackgroundEffect(new DimEffect(8));
        //设置点击事件
        sheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
            @Override
            public boolean onItemClick(int position, MenuEntity menuEntity) {
//                Toast.makeText(SendSelectActivity.this, "点击了：" + menuEntity.title, Toast.LENGTH_SHORT).show();
                //根据返回值, true 会关闭 SweetSheet ,false 则不会
                sharePreferencesUtils.setString(SendSelectActivity.this, "compName", etCompName.getText().toString());
                sharePreferencesUtils.setString(SendSelectActivity.this, "workName", etWorkName.getText().toString());
                sharePreferencesUtils.setString(SendSelectActivity.this, "workCode", etWorkCode.getText().toString());
                if (menuEntity.title.equals("本地存储")) {
                    sharePreferencesUtils.setString(SendSelectActivity.this, "sendSelect", "本地存储");
                    intent = new Intent(SendSelectActivity.this, MainActivity.class);
                    intent.putExtra("etCompName",etCompName.getText().toString());
                    intent.putExtra("etWorkName",etWorkName.getText().toString());
                    intent.putExtra("etWorkCode",etWorkCode.getText().toString());
                    startActivity(intent);
                } else if (menuEntity.title.equals("实时上传（点击进入硬度计界面）")) {
//                    sharePreferencesUtils.setString(SendSelectActivity.this, "sendSelect", "实时上传");
//                    intent = new Intent(SendSelectActivity.this, MainActivity.class);
//                    intent.putExtra("etCompName",etCompName.getText().toString());
//                    intent.putExtra("etWorkName",etWorkName.getText().toString());
//                    intent.putExtra("etWorkCode",etWorkCode.getText().toString());
                    intent = new Intent(SendSelectActivity.this, MainYDJActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        sheet.toggle();
    }

    /**
     * 测试接口网络返回数据
     * @param test
     */
    @Override
    public void setTest(Test01 test) {
        Toast.makeText(SendSelectActivity.this, test.getLog_id(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTestMessage(String message) {
        Toast.makeText(SendSelectActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}