package com.example.luke_imagevideo_send.cehouyi.util;

import android.widget.Toast;

import com.example.luke_imagevideo_send.chifen.magnetic.activity.MainActivity;
import com.example.luke_imagevideo_send.modbus.ModbusCallback;
import com.example.luke_imagevideo_send.modbus.ModbusManager;
import com.licheedev.modbus4android.param.TcpParam;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

public class ModbusConnection {
    short[] data = new short[31];
    //建立modbus连接
    public void makeConnection(MainActivity mainActivity) {
//        LoadingDialog loadingDialog = new LoadingDialog(this, "连接服务中", R.mipmap.ic_dialog_loading);
//        loadingDialog.show();
        // TCP
        TcpParam param;
        param = TcpParam.create("172.16.16.128", 502)
                .setTimeout(1000)
                .setRetries(0)
                .setEncapsulated(false)
                .setKeepAlive(true);

        ModbusManager.get().closeModbusMaster(); // 先关闭一下
        ModbusManager.get().init(param, new ModbusCallback<ModbusMaster>() {
            @Override
            public void onSuccess(ModbusMaster modbusMaster) {
                ModbusManager.get().writeRegisters(1, 0, getData(), new ModbusCallback<WriteRegistersResponse>() {
                    @Override
                    public void onSuccess(WriteRegistersResponse writeRegistersResponse) {
                        // 发送成功
                        Toast.makeText(mainActivity, "F16写入成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable tr) {
//                        Toast.makeText(MainActivity.this, tr.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinally() {
//                        Toast.makeText(MainActivity.this, "tr.toString()", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable tr) {
//                Toast.makeText(mNotifications, "modbus连接失败，只能使用本地模式.", Toast.LENGTH_SHORT).show();
//                AlertDialogUtil alertDialogUtil = new AlertDialogUtil(MainActivity.this);
//                alertDialogUtil.showDialog("modbus连接失败，只能使用本地模式，是否继续使用", new AlertDialogCallBack() {
//                    @Override
//                    public void confirm(String name) {
//
//                    }
//
//                    @Override
//                    public void cancel() {
////                        finish();
//                    }
//
//                    @Override
//                    public void save(String name) {
//
//                    }
//
//                    @Override
//                    public void checkName(String name) {
//
//                    }
//                });
//                loadingDialog.dismiss();
            }

            @Override
            public void onFinally() {
                // todo updateDeviceSwitchButton();
            }
        });
    }

    /**
     * 数据组装
     */
    private short[] getData() {
        //传输标识
        data[0] = 0;
        data[1] = 0;
        //协议标识
        data[2] = 0;
        data[3] = 0;
        //字节长度
        data[4] = 0;
        data[5] = 5;
//        //单位标识符
//        data[6] = 1;
//        //功能码
//        data[7] = 10;
//        //地址码
//        data[8] = 0;
//        data[9] = 1;
//        //寄存器个数
//        data[10] = 0;
//        data[11] = 6;
//        //字节数
//        data[12] = 12;
//        //型号
////        data[13] = 0x01;
//        data[13] = 1;
//        data[14] = 0;
//        data[15] = 0;
//        data[16] = 0;
//        data[17] = 0;
//        //时间
//        data[18] = 2;
//        data[19] = 0;
//        data[20] = 0;
//        data[21] = 0;
//        data[22] = 0;
//
////        //mac
////        data[23] = 3;
////        data[24] = 0;
////        data[25] = 0;
////        data[26] = 0;
////        data[27] = 0;
//
//        //交直模式
//        data[23] = 6;
//        data[24] = 0;
////        //电量
////        data[28] = 5;
////        data[29] = 0;
//        //联动时长
//        data[25] = 7;
//        data[26] = 0;
//        //黑白光切换
//        data[27] = 8;
//        data[28] = 0;
//        //点动联动切换
//        data[29] = 9;
//        data[30] = 0;
        return data;
    }
}
