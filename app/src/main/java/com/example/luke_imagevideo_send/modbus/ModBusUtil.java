package com.example.luke_imagevideo_send.modbus;

import com.example.luke_imagevideo_send.http.base.ModbusInstanceCallBack;
import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

public class ModBusUtil {
    public void modbusInstance(ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().setParam(new ModbusParam()
                .setHost("192.168.1.22")
                .setPort(502)
                .setEncapsulated(false)
                .setKeepAlive(true)
                .setTimeout(5000)
                .setRetries(0))
                .init(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        modbusInstanceCallBack.confirm(s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        modbusInstanceCallBack.error(msg);
                    }
                });
    }
}
