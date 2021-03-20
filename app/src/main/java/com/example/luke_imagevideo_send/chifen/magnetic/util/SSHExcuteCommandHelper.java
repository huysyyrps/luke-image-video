package com.example.luke_imagevideo_send.chifen.magnetic.util;

import android.widget.Toast;

import com.example.luke_imagevideo_send.MyApplication;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * SSH工具类
 *
 */
public class SSHExcuteCommandHelper {
    Session session = null;
    ChannelExec openChannel = null;
    /**
     * @param host  主机ip
//     * @param user 用户名
//     * @param pwd 密码
//     * @param port ssh端口
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
        }
    }
    /**
     * 是否连接成功,调用如果不需要调用execCommand方法那么必须调用 disconnect方法关闭session
     * @return
     */
    public boolean canConnection(){
        try {
            session.connect();
            return true;
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 关闭连接
     */
    public void disconnect(){
        if (openChannel != null && !openChannel.isClosed()) {
            openChannel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }


    /**
     * 执行命令
     * @param command
     * @return
     */
    public String execCommand(String command) {
        StringBuffer result = new StringBuffer();
        try {
            if(!session.isConnected()){
                session.connect();
            }
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            //int exitStatus = openChannel.getExitStatus();
            openChannel.connect();
            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));

            String tmpStr = "";
            while ((tmpStr = reader.readLine()) != null) {
                result.append(new String(tmpStr.getBytes("gbk"), "UTF-8")).append("\n");
            }
        } catch (JSchException | IOException e) {
            e.printStackTrace();
            result.append(e.getMessage());
        } finally {
            disconnect();
        }
        return result.toString();
    }
    /**
     * 解析
     * @param result
     * @return
     */
    public List<List<String>> parseResult(String result){
        List<List<String>> parseResult = new ArrayList<>();
        List<String> list = null;
//
        for (String line : result.split("\n")) {
            list = new ArrayList<String>();
            String[] columns = {};
//这个是针对df命令的 [Mounted on] 其实就一个,如果用空格就会分割出两个
            if(line.contains("Mounted ")){
                columns = line.replace("Mounted ", "Mounted-").split(" ");
            }else{
                columns = line.split(" ");
            }

            for (String column : columns) {
                if (!" ".equals(column) && !"".equals(column)) {
                    list.add(column);
                }
            }
            parseResult.add(list);
        }
        return parseResult;
    }

    public static void main(String address, final SSHCallBack SSHCallBack) {
        SSHExcuteCommandHelper execute = new SSHExcuteCommandHelper(address);
        boolean ss = execute.canConnection();
        System.out.println("是否连接成功"+execute.canConnection());
        if (execute.canConnection()){
            //发送指令
            String s = execute.execCommand("uci show system.id");
//            System.out.println("解析前");
//            System.out.println(s);
//            System.out.println("解析后");
//            List<List<String>> parseResult = execute.parseResult(s);
//            SSHCallBack.confirm(parseResult);
            SSHCallBack.confirm(s);
        }else {
            Toast.makeText(MyApplication.getContext(), "连接失败", Toast.LENGTH_SHORT).show();
        }
//        String s = execute.execCommand("uci show system.id");
//        System.out.println("解析前");
//        System.out.println(s);
//        System.out.println("解析后");
//        List<List<String>> parseResult = execute.parseResult(s);
//        SSHCallBack.confirm(parseResult);
    }

    public static void read(String address, final SSHCallBack SSHCallBack) {
        SSHExcuteCommandHelper execute = new SSHExcuteCommandHelper(address);
        boolean ss = execute.canConnection();
        System.out.println("是否连接成功"+execute.canConnection());
        if (execute.canConnection()){
            //发送指令
            String s = execute.execCommand("read_data");
//            System.out.println("解析前");
//            System.out.println(s);
//            System.out.println("解析后");
//            List<List<String>> parseResult = execute.parseResult(s);
//            SSHCallBack.confirm(parseResult);
            SSHCallBack.confirm(s);
        }else {
            Toast.makeText(MyApplication.getContext(), "连接失败", Toast.LENGTH_SHORT).show();
        }
//        String s = execute.execCommand("uci show system.id");
//        System.out.println("解析前");
//        System.out.println(s);
//        System.out.println("解析后");
//        List<List<String>> parseResult = execute.parseResult(s);
//        SSHCallBack.confirm(parseResult);
    }
}
