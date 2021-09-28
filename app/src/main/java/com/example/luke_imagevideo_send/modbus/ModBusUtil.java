package com.example.luke_imagevideo_send.modbus;

import com.example.luke_imagevideo_send.chifen.magnetic.util.getIp;
import com.example.luke_imagevideo_send.http.base.ModbusInstanceCallBack;
import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

public class ModBusUtil {
    /**
     * 初始化
     * @param modbusInstanceCallBack
     */
    public void modbusInstance(ModbusInstanceCallBack modbusInstanceCallBack) throws Exception {
        String address1 = new getIp().getConnectIp();
        ModbusReq.getInstance().setParam(new ModbusParam()
                .setHost(address1)
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

    /**
     * 读线圈
     * slaveId----从站地址,
     * startAddress----起始地址,
     * quantityOfRegister----读取寄存器个数
     * @param modbusInstanceCallBack
     */
    public void modbusReadCoil(int slaveId,int startAddress,int quantityOfRegisterx,ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().readCoil(new OnRequestBack<boolean[]>() {
            @Override
            public void onSuccess(boolean[] data) {
                modbusInstanceCallBack.confirm(data);
            }

            @Override
            public void onFailed(String msg) {
                modbusInstanceCallBack.error(msg);
            }
        }, slaveId, startAddress, quantityOfRegisterx);
    }

    /**
     * 读单个/多个离散输入寄存器
     * slaveId----从站地址,
     * startAddress----起始地址,
     * quantityOfRegister----读取寄存器个数
     * @param modbusInstanceCallBack
     */
    public void modbusReadDiscreteInput(int slaveId,int startAddress,int quantityOfRegisterx,ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().readDiscreteInput(new OnRequestBack<boolean[]>() {
            @Override
            public void onSuccess(boolean[] data) {
                modbusInstanceCallBack.confirm(data);
            }

            @Override
            public void onFailed(String msg) {
                modbusInstanceCallBack.error(msg);
            }
        }, slaveId, startAddress, quantityOfRegisterx);
    }

    /**
     * 读单个/多个保持寄存器
     * slaveId----从站地址,
     * startAddress----起始地址,
     * quantityOfRegister----读取寄存器个数
     * @param modbusInstanceCallBack
     */
    public void modbusReadHoldingRegisters(int slaveId,int startAddress,int quantityOfRegisterx,ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                modbusInstanceCallBack.confirm(data);
            }

            @Override
            public void onFailed(String msg) {
                modbusInstanceCallBack.error(msg);
            }
        }, slaveId, startAddress, quantityOfRegisterx);
    }

    /**
     * 读单个/多个输入寄存器
     * slaveId----从站地址,
     * startAddress----起始地址,
     * quantityOfRegister----读取寄存器个数
     * @param modbusInstanceCallBack
     */
    public void modbusReadInputRegisters(int slaveId,int startAddress,int quantityOfRegisterx,ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().readInputRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                modbusInstanceCallBack.confirm(data);
            }

            @Override
            public void onFailed(String msg) {
                modbusInstanceCallBack.error(msg);
            }
        }, slaveId, startAddress, quantityOfRegisterx);
    }

    /**
     * 写线圈
     * writeAddress----写入寄存器地址，
     * quantityOfRegister----写入寄存器个数
     * booleanValue----true/false值,
     * booleanValues-----boolean[]数组，
     * intValue----int值，
     * shortValues-------short[]数组
     * @param modbusInstanceCallBack
     */
    public void modbusWriteCoil(int slaveId,int writeAddress,boolean values,ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
            @Override
            public void onSuccess(String data) {
                modbusInstanceCallBack.confirm(data);
            }

            @Override
            public void onFailed(String msg) {
                modbusInstanceCallBack.error(msg);
            }
        }, slaveId, writeAddress, values);
    }

    /**
     * 写多个线圈
     * writeAddress----写入寄存器地址，
     * quantityOfRegister----写入寄存器个数
     * booleanValue----true/false值,
     * booleanValues-----boolean[]数组，
     * intValue----int值，
     * shortValues-------short[]数组
     * @param modbusInstanceCallBack
     */
    public void modbusWriteCoils(int slaveId,int writeAddress,boolean[] values,ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().writeCoils(new OnRequestBack<String>() {
            @Override
            public void onSuccess(String data) {
                modbusInstanceCallBack.confirm(data);
            }

            @Override
            public void onFailed(String msg) {
                modbusInstanceCallBack.error(msg);
            }
        }, slaveId, writeAddress, values);
    }

    /**
     * 写单寄存器
     * writeAddress----写入寄存器地址，
     * quantityOfRegister----写入寄存器个数
     * booleanValue----true/false值,
     * booleanValues-----boolean[]数组，
     * intValue----int值，
     * shortValues-------short[]数组
     * @param modbusInstanceCallBack
     */
    public void modbusWriteRegister(int slaveId,int writeAddress,int values,ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().writeRegister(new OnRequestBack<String>() {
            @Override
            public void onSuccess(String data) {
                modbusInstanceCallBack.confirm(data);
            }

            @Override
            public void onFailed(String msg) {
                modbusInstanceCallBack.error(msg);
            }
        }, slaveId, writeAddress, values);
    }

    /**
     * 写多个寄存器
     * writeAddress----写入寄存器地址，
     * quantityOfRegister----写入寄存器个数
     * booleanValue----true/false值,
     * booleanValues-----boolean[]数组，
     * intValue----int值，
     * shortValues-------short[]数组
     * @param modbusInstanceCallBack
     */
    public void modbusWriteRegisters(int slaveId,int quantityOfRegister,short[] values,ModbusInstanceCallBack modbusInstanceCallBack){
        ModbusReq.getInstance().writeRegisters(new OnRequestBack<String>() {
            @Override
            public void onSuccess(String data) {
                modbusInstanceCallBack.confirm(data);
            }

            @Override
            public void onFailed(String msg) {
                modbusInstanceCallBack.error(msg);
            }
        }, slaveId, quantityOfRegister, values);
    }
}
