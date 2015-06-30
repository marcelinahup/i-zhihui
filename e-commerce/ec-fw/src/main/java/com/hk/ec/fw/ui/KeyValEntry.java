package com.hk.ec.fw.ui;

import java.io.Serializable;

public class KeyValEntry implements Serializable{
	
	private static final long serialVersionUID = 3149709955818883706L;
	
	private String key;
	private String val;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}

}
