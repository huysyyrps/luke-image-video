package com.example.luke_imagevideo_send.cehouyi.bean;

import java.io.Serializable;
import java.util.List;

public class DataBean implements Serializable {
    /**
     * dataItem : 01
     * itemData : [{"A":"1","B":"1","C":"1"}]
     */

    private int dataItem;
    private List<ItemDataBean> itemData;

    public int getDataItem() {
        return dataItem;
    }

    public void setDataItem(int dataItem) {
        this.dataItem = dataItem;
    }

    public List<ItemDataBean> getItemData() {
        return itemData;
    }

    public void setItemData(List<ItemDataBean> itemData) {
        this.itemData = itemData;
    }
}
