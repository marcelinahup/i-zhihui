package com.hk.ec.fw.web.permission;

import java.io.Serializable;

public abstract class Authentication implements Serializable {
	
	private static final long serialVersionUID = 3434875798078585405L;
	private WebUser user;
	
	public WebUser getUser() {
		return user;
	}

	public void setUser(WebUser user) {
		this.user = user;
	}

	/**
	 * 验证登录用户权限
	 * @param requestUrl
	 * @return
	 */
	public abstract boolean isPermitted(String requestUrl);

}
