package com.example.luke_imagevideo_send.cehouyi.bean;

import java.io.Serializable;

public class SaveData implements Serializable {
    public String time;
    public String soundVelocity;
    public String data;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSoundVelocity() {
        return soundVelocity;
    }

    public void setSoundVelocity(String soundVelocity) {
        this.soundVelocity = soundVelocity;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
