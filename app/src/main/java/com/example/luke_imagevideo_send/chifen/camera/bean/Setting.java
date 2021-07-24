package com.example.luke_imagevideo_send.chifen.camera.bean;

import java.io.Serializable;

public class Setting implements Serializable {


    /**
     * date : Sat Jun 26 20:45:38 CST 2021
     * data : {"id":"xxxxxxxx","date":"Fri Jan 8 03:23:55 CST 2021","mac":"xx xx xx xx xx","power":"","acdc":"0","mode":"0","bw":"0","auto":"0","auto_time":"xx","ip":"113.104.182.107"}
     */

    private String date;
    private DataBean data;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : xxxxxxxx
         * date : Fri Jan 8 03:23:55 CST 2021
         * mac : xx xx xx xx xx
         * power :
         * acdc : 0
         * mode : 0
         * bw : 0
         * auto : 0
         * auto_time : xx
         * ip : 113.104.182.107
         */

        private String id;
        private String date;
        private String mac;
        private String power;
        private String acdc;
        private String mode;
        private String bw;
        private String auto;
        private String auto_time;
        private String ip;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getPower() {
            return power;
        }

        public void setPower(String power) {
            this.power = power;
        }

        public String getAcdc() {
            return acdc;
        }

        public void setAcdc(String acdc) {
            this.acdc = acdc;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getBw() {
            return bw;
        }

        public void setBw(String bw) {
            this.bw = bw;
        }

        public String getAuto() {
            return auto;
        }

        public void setAuto(String auto) {
            this.auto = auto;
        }

        public String getAuto_time() {
            return auto_time;
        }

        public void setAuto_time(String auto_time) {
            this.auto_time = auto_time;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }
}
