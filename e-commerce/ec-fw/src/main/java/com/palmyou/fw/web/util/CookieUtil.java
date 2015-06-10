package com.palmyou.fw.web.util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
/**
 * cookie工具类
 * @author James
 *
 */
public class CookieUtil {
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static Cookie[] getAllCookies(HttpServletRequest request){
		return request.getCookies();
	}
	
	/**
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String cookieName){
		Cookie[] cookies = getAllCookies(request);
		if(cookies == null || cookies.length == 0){
			return null;
		}
		Cookie resultCookie = null;
		for(Cookie cookie : cookies){
			if(cookieName.equals(cookie.getName())){
				resultCookie = cookie;
				break;
			}
		}
		return resultCookie;
	}
	
}
