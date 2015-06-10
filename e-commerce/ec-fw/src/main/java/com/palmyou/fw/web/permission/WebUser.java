package com.palmyou.fw.web.permission;

import java.io.Serializable;

/**
 * WEB端用户基类
 * @author James
 */

public class WebUser implements Serializable{
	
	private static final long serialVersionUID = 8346150848796307128L;

	public WebUser(){
	}
	public WebUser(String userId){
		this.userId = userId;
	}
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
