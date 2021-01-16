package com.example.luke_imagevideo_send.cehouyi.bean;

import java.io.Serializable;

public class DictUnit implements Serializable {

	public String id;
	public String name;
	public String flag;
	public String tag;
	public String field;

	public DictUnit() {

	}
	public DictUnit(String id, String name, String flag, String tag, String field) {
		this.id = id;
		this.name = name;
		this.flag = flag;
		this.tag = tag;
		this.field = field;
	}

}
