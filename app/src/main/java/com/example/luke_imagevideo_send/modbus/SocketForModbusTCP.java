package com.example.luke_imagevideo_send.modbus;

import android.os.SystemClock;
import android.util.Log;

import com.example.luke_imagevideo_send.http.base.ModbusInstanceCallBack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public abstract class SocketForModbusTCP {
    private String ip;
    private int port;

    private Socket mSocket;
    private SocketAddress mSocketAddress;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private boolean _isConnected = false;

    public SocketForModbusTCP(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect(ModbusInstanceCallBack modbusInstanceCallBack) {

        try {
            this.mSocket = new Socket();
            this.mSocket.setKeepAlive(true);
            this.mSocketAddress = new InetSocketAddress(ip, port);
            this.mSocket.connect(mSocketAddress, 3000);// 设置连接超时时间为3秒

            this.mOutputStream = mSocket.getOutputStream();
            this.mInputStream = mSocket.getInputStream();

            this.mReadThread = new ReadThread();
            this.mReadThread.start();
            this._isConnected = true;
            modbusInstanceCallBack.confirm("");
        } catch (IOException e) {
            e.printStackTrace();
            modbusInstanceCallBack.error(e.toString());
        }
    }

    public void close() {
        if (this.mReadThread != null) {
            this.mReadThread.interrupt();
        }
        if (this.mSocket != null) {
            try {
                this.mSocket.close();
                this.mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this._isConnected = false;
    }

    public boolean isConnected() {
        return this._isConnected;
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (SocketForModbusTCP.this.mInputStream == null) {
                        return;
                    }
//                    ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//                    byte[] buffer1 = new byte[1024];
//                    int len = -1;
//                    while ((len = mInputStream.read(buffer1)) != -1) {
//                        outSteam.write(buffer1, 0, len);
//                    }
//                    outSteam.close();
//                    mInputStream.close();
//                    byte[] buffer12 =  outSteam.toByteArray();
//                    Log.e("XXX","XXX");

                    int available = SocketForModbusTCP.this.mInputStream.available();
                    if (available > 0) {
                        byte[] buffer = new byte[available];
                        int size = SocketForModbusTCP.this.mInputStream.read(buffer);
                        if (size > 0) {
                            SocketForModbusTCP.this.onDataReceived(buffer, size);
                        }
                    } else {
                        SystemClock.sleep(50);
                    }


                } catch (Throwable e) {
                    Log.e("error", e.getMessage());
                    return;
                }
            }
        }
    }

    public void send(byte[] bOutArray) {
        try {
            this.mOutputStream.write(bOutArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendHex(String sHex) {
        byte[] bOutArray = ByteUtil.HexToByteArr(sHex);//转成16进制
        send(bOutArray);
    }

    protected abstract void onDataReceived(byte[] readBuffer, int size);
}

