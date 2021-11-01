package com.example.luke_imagevideo_send.cehouyi.bean;

import org.litepal.crud.DataSupport;

public class LK500SValue extends DataSupport {
    public String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
