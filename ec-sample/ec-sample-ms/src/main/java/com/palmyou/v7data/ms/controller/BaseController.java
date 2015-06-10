package com.palmyou.v7data.ms.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.palmyou.fw.web.AbstractController;
import com.palmyou.fw.web.permission.WebUser;
import com.palmyou.fw.web.session.SystemSession;
import com.palmyou.v7data.ms.web.MySessionConfig;

/**
 * Crontroller类的基础类，可以 除掉参数中的空格
 */
public class BaseController extends AbstractController {
	
	/**
	 * 获取登录用户对象
	 * @param request
	 * @return
	 */
	protected WebUser getLoginUser(HttpServletRequest request){
		WebUser loginUser = null;

		SystemSession session = getSystemSession(request);
		if(session != null) {
			
			// 注意和disp里面的sessionkey配置一致
			loginUser = (WebUser)session.getAttribute("sessionKey");
		}
		return loginUser;
	}

	/**
	 * 获取系统Session
	 * @param request
	 * @return
	 */
	protected SystemSession getSystemSession(HttpServletRequest request){
		SystemSession session = (SystemSession)request.getAttribute(MySessionConfig.OS_SESSION_KEY);
		return session;
	}
	
	/**
	 * 在SystemSession中存储信息
	 * @param sessionName
	 * @param sessionValue
	 * @param request
	 */
	protected void setSessionAttr(String sessionName,Object sessionValue,HttpServletRequest request){
		
		SystemSession session = getSystemSession(request);
		session.setAttribute(sessionName, sessionValue);	
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
			
			cookie.setMaxAge(MySessionConfig.OS_SESSION_TIMEOUT);
		}
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	protected ModelAndView setTablePageParams(ModelAndView mv,
			int count,
			int startIndex,
			int onePageCount){
		mv.addObject("totalRecord",count);
		//当前页码
		int nowPage=startIndex/onePageCount+1;
		
		//页码导航页码数组计算
		int allPageCount=0;
		
		if(count%onePageCount == 0){
			allPageCount=count/onePageCount;
		}else{
			allPageCount=count/onePageCount+1;
		}
		//原则：最多有5页，如果没有，仅显示仅有的几页
		int pageNavStart=1;
		int pageNavEnd=1;
		
		if(allPageCount<=5){
			//页码导航开始页码
			pageNavStart=1;
			pageNavEnd=allPageCount;
			//页码导航结束页码
		}else{
			//尽量使得当前页居中
			int tryPageNavStart=nowPage-2;
			
			if(tryPageNavStart<1){
				tryPageNavStart=1;
			}
			
			//页码导航开始页码
			pageNavStart=tryPageNavStart;
			
			int tryPageNavEnd=pageNavStart+4;
			
			if(tryPageNavEnd>allPageCount){
				tryPageNavEnd=allPageCount;
				
				//tryPageNavStart  -4 不小于1则使用
				tryPageNavStart=tryPageNavEnd-4;
				
				if(tryPageNavStart<1){
					tryPageNavStart=1;
				}
				
				pageNavStart=tryPageNavStart;
			}
			//页码导航结束页码
			pageNavEnd=tryPageNavEnd;
		}
		
		List<Integer> pageNavList = new ArrayList<Integer>();
		for(int i = pageNavStart;i <= pageNavEnd;i ++){
			pageNavList.add(i);
		}
		mv.addObject("nowPage",nowPage);
		mv.addObject("pageNavList",pageNavList);
		
		mv.addObject("start",startIndex);
		mv.addObject("count",onePageCount);
		mv.addObject("maxPage",allPageCount);
		
		return mv;
	}
	
}
