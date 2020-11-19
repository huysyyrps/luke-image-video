package com.example.luke_imagevideo_send.cehouyi.bean;

import java.io.Serializable;

public class Test implements Serializable {

    /**
     * date : 2020-11-14T10:36:02.416217+08:00
     * temperatureC : 5
     * temperatureF : 40
     * summary : Bracing
     */

    private String date;
    private int temperatureC;
    private int temperatureF;
    private String summary;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(int temperatureC) {
        this.temperatureC = temperatureC;
    }

    public int getTemperatureF() {
        return temperatureF;
    }

    public void setTemperatureF(int temperatureF) {
        this.temperatureF = temperatureF;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
