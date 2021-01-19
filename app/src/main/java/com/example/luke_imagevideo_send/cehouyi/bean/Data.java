package com.example.luke_imagevideo_send.cehouyi.bean;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    /**
     * dataName : f001
     * data : [{"dataItem":"01","itemData":[{"A":"1","B":"1","C":"1"}]},{"dataItem":"02","itemData":[{"A":"1","B":"1","C":"1"}]}]
     */

    private String dataName;
    private List<DataBean> data;

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
}
