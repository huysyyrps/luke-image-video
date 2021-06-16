package com.example.luke_imagevideo_send.chifen.magnetic.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.luke_imagevideo_send.MyApplication;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * SSH工具类
 */
public class SSHExcuteCommandHelper {
    Session session = null;
    ChannelExec openChannel = null;
    ChannelShell channel = null;
    private Handler mHandler;

    /**
     * @param host 主机ip
     *             //     * @param user 用户名
     *             //     * @param pwd 密码
     *             //     * @param port ssh端口
     */
    public SSHExcuteCommandHelper(String host) {
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
            Toast.makeText(MyApplication.getContext(), "IP获取为空,请检查设备与手机是否建立网络连接", Toast.LENGTH_LONG).show();
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
//        mHandler = new Handler();
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                try {
//                    if (session!=null){
//                        session.connect();
//                    }
//                } catch (JSchException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 3500); //延迟3.5秒跳转
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
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command + "\nexit");
            //int exitStatus = openChannel.getExitStatus();
            openChannel.connect();
            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String tmpStr = "";
            while ((tmpStr = reader.readLine()) != null) {
                result.append(tmpStr);
            }
        } catch (JSchException | IOException e) {
            e.printStackTrace();
            result.append(e.getMessage());
        } finally {
            disconnect();
        }
        return result.toString();
    }


    public String execCommand1(String command) {
        try {
            channel = (ChannelShell) session.openChannel("shell");
            InputStream inputStream = channel.getInputStream();
            OutputStream outputStream = channel.getOutputStream();//写入该流的所有数据都将发送到远程端。
            //使用PrintWriter流的目的就是为了使用println这个方法
            //好处就是不需要每次手动给字符串加\n
            PrintWriter printWriter = new PrintWriter(outputStream);

            String cmd2 = command;
            printWriter.println(cmd2 + "\nexit");
//            printWriter.println("exit");//加上个就是为了，结束本次交互
            printWriter.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String msg = null;
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return "XXX";
    }

    public static void writeBefor(String address, String data, final SSHCallBack SSHCallBack) {
        SSHExcuteCommandHelper execute = new SSHExcuteCommandHelper(address);
        boolean ss = execute.canConnection();
        if (ss) {
            //发送指令
            String s = null;
            try {
                s = execute.sendCmd(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SSHCallBack.confirm(s);
        } else {
            SSHCallBack.error("连接失败,请检查设备热点链接是否成功");
        }
    }


    public String sendCmd(String command) throws Exception {
        byte[] tmp = new byte[1024]; //读数据缓存
        StringBuffer strBuffer = new StringBuffer();  //执行SSH返回的结果
        ChannelExec ssh = (ChannelExec) session.openChannel("exec");
        //返回的结果可能是标准信息,也可能是错误信息,所以两种输出都要获取
        //一般情况下只会有一种输出.
        //但并不是说错误信息就是执行命令出错的信息,如获得远程java JDK版本就以
        //ErrStream来获得.
        InputStream InputStream = ssh.getInputStream();
        InputStream ErrStream = ssh.getErrStream();
        ssh.setCommand(command);
        ssh.connect();
        //开始获得SSH命令的结果
        while (true) {
            //获得错误输出
            while (ErrStream.available() > 0) {
                int i = ErrStream.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                strBuffer.append(new String(tmp, 0, i));
            }
            //获得标准输出
            while (InputStream.available() > 0) {
                int i = InputStream.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                strBuffer.append(new String(tmp, 0, i));
                return strBuffer.toString();
            }
            if (ssh.isClosed()) {
                Log.e("XXX", "XXXXX");
                break;
            }
            try {
                Thread.sleep(100);
            } catch (Exception ee) {
            }
        }
        return strBuffer.toString();
    }
}
