package com.example.luke_imagevideo_send.chifen.magnetic.util;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.luke_imagevideo_send.MyApplication;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * SSH工具类
 */
public class SSHDataExcuteCommandHelper {
    Session session = null;
    ChannelSftp openChannel = null;
    private Handler mHandler;

    /**
     * @param host 主机ip
     *             //     * @param user 用户名
     *             //     * @param pwd 密码
     *             //     * @param port ssh端口
     */
    public SSHDataExcuteCommandHelper(String host) {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession("root", host, 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setTimeout(6000);
            session.setConfig(config);
            session.setPassword("root");
        } catch (JSchException e) {
            e.printStackTrace();
            Looper.prepare();
            Toast.makeText(MyApplication.getContext(), "IP获取为空,请检查wifi板设备与手机是否建立网络连接", Toast.LENGTH_LONG).show();
            Looper.loop();
            return;
        }
    }

    /**
     * 是否连接成功,调用如果不需要调用execCommand方法那么必须调用 disconnect方法关闭session
     *
     * @return
     */
    public boolean canConnection() {
        while (true){
            if (session!=null){
                try {
                    session.connect();
                    return true;
                } catch (JSchException e) {
                    e.printStackTrace();
                   break;
                }
            }
        }
        return false;
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        if (openChannel != null && !openChannel.isClosed()) {
            openChannel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }


    /**
     * 执行命令
     *
     * @param command
     * @return
     */
    public String execCommand(String command) {
        StringBuffer result = new StringBuffer();
        try {
            if (!session.isConnected()) {
                session.connect();
            }
            openChannel = (ChannelSftp) session.openChannel("sftp");
            openChannel.connect();
            //path  SFTP服务器的文件路径
            openChannel.cd("/");
            File file = new File(Environment.getExternalStorageDirectory() + "/LUKEDaily.txt");
            //json.log服务器上的文件名
            openChannel.get("json.log", new FileOutputStream(file));
        } catch (JSchException | IOException e) {
            e.printStackTrace();
            result.append(e.getMessage());
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return result.toString();
    }

    public static void writeBefor(String address, String data, final SSHCallBack SSHCallBack) {
        SSHDataExcuteCommandHelper execute = new SSHDataExcuteCommandHelper(address);
        boolean ss = execute.canConnection();
        if (ss) {
            //发送指令
            String s = null;
            try {
                s = execute.execCommand(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SSHCallBack.confirm(s);
        } else {
            SSHCallBack.error("连接失败,请检查设备热点链接是否成功");
        }
    }
}
