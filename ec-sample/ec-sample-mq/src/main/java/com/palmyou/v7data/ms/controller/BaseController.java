package com.palmyou.v7data.ms.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.palmyou.fw.web.AbstractController;
import com.palmyou.fw.web.permission.WebUser;

/**
 * Crontroller类的基础类，可以 除掉参数中的空格
 */
public class BaseController extends AbstractController {
	
	/**
	 * 获取登录用户对象
	 * @param request
	 * @return
	 */
	protected WebUser getLoginUser(HttpSession session){
		WebUser loginUser = null;

		if(session != null) {
			
			// 注意和disp里面的sessionkey配置一致
			loginUser = (WebUser)session.getAttribute("sessionKey");
		}
		return loginUser;
	}

	/**
	 * 删除指定名称的cookie数据
	 * @param cookieName
	 * @param response
	 */
	protected void delCookie(String cookieName,HttpServletResponse response){
		Cookie cookie=new Cookie(cookieName,"");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	/**
	 * 指定cookie名称存储数据（支持中文 utf-8）
	 * @param cookieName
	 * @param cookieValue
	 * @param maxAge null 使用默认值 60*60*24
	 * @param response
	 */
	protected void setCookie(String cookieName,String cookieValue, Integer maxAge, HttpServletResponse response){
		
		try {
			cookieValue = URLEncoder.encode(cookieValue,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Cookie cookie = new Cookie(cookieName,cookieValue);
		if(maxAge != null){
			
			cookie.setMaxAge(maxAge.intValue());
		}else{
			
			cookie.setMaxAge(30 * 60);
		}
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
}
