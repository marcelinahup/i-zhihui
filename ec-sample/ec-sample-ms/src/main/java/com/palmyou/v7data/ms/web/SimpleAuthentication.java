package com.palmyou.v7data.ms.web;

import com.hk.ec.fw.web.permission.Authentication;

public class SimpleAuthentication extends Authentication {

	private static final long serialVersionUID = -2786124576522423502L;

	public SimpleAuthentication(){
	}
	
	@Override
	public boolean isPermitted(String requestUrl) {
		
//		System.out.println("requestUrl: " + requestUrl);
		System.out.println(this.getUser().getUserId());
		
		return true;
		// 简单实现，允许访问
//		if(requestUrl.endsWith("main.html") || requestUrl.endsWith("top.html")){
//			
//			return true;
//		}
		
	
	}
}
