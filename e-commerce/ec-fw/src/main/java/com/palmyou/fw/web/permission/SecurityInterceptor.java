package com.palmyou.fw.web.permission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.palmyou.fw.web.session.SessionManager;
import com.palmyou.fw.web.session.SystemSession;

/**
 * 判断用户权限，未登录用户跳转到登录页面
 * @author James
 *
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	// 不过滤URL列表
	private List<String> excludedUrls;
	
	// debug默认不开启
	private boolean debug = false;
	
	// 登录url，默认是login.html
	private String loginUrl = "login.html";
	
	// 保存session的key，默认：sessionKey
	private String sessionKey = "sessionKey";
	
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	// session保存用户的key，默认：loginUser
	private String sessionUserKey = "loginUser";

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void setSessionUserKey(String sessionUserKey) {
		this.sessionUserKey = sessionUserKey;
	}

	public void setExcludedUrls(List<String> excludedUrls) {
		this.excludedUrls = excludedUrls;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	private Authentication authentication;
	
	// 不可访问提示页面
	private String noAccessUrl = "/noAccess.html";
	
	public void setNoAccessUrl(String noAccessUrl) {
		this.noAccessUrl = noAccessUrl;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		// 把session放入request，方便后面在controller里面取用
		SystemSession session = SessionManager.getSession(request, response);
		request.setAttribute(this.sessionKey, session);
		
		// 1, debug环境不作过滤
		if(debug){
			return super.preHandle(request, response, handler);
		}
		
		// 2, 非debug环境再作过滤
		
		String requestUrl = request.getRequestURI();
		
		// 2.1, 先排除不需求过滤的请求(不需要登录的请求，自然也就不需要判断权限)
		if(excludedUrls != null){

		    for (String url : excludedUrls) {
		      if (requestUrl.contains(url)){
		    	  
		    	  // 继续之前的拦截，不能直接return true
		    	  return super.preHandle(request, response, handler);
		      }
		    }
		}
		
		// 2.2，过滤未登录的请求，跳转到登录
		if(session == null || session.getAttribute(sessionUserKey)==null) {
			logger.debug("用户未成功登录系统");
			response.getWriter().write("<script language=javascript>top.location.href='" + request.getContextPath() + loginUrl + "';</script>"); 
			return false;
		}
		
		// 2.3.1，登录成功的请求，除了没权限访问的请求跳转页面，可以跳转允许noAccessUrl成功跳转
		//if(requestUrl.endsWith(noAccessUrl)) {
		if(requestUrl.contains(noAccessUrl)) {	
			 // 继续之前的拦截，不能直接return true
			 return super.preHandle(request, response, handler);
		}
		
//		String userId="";
//		
		WebUser loginUser = (WebUser)session.getAttribute(this.sessionUserKey);
//		 if(loginUser!=null){
//				userId=loginUser.getUserId();
//		}
		
		Authentication sessionAuthentication = (Authentication) session.getAttribute("auth");
		if (sessionAuthentication == null) {
			authentication.setUser(loginUser);
			sessionAuthentication = authentication;
			session.setAttribute("auth", authentication);
		}
		
		// 2.3.2，请求的权限判定，不能访问，跳转到noAccess
		if(sessionAuthentication != null && !sessionAuthentication.isPermitted(requestUrl)) {
		   logger.debug("重定向到不可访问的提示页面");
		   request.getRequestDispatcher(noAccessUrl).forward(request, response);
		   return false;
		}
		
		logger.debug("成功登录，并有权访问请求页面");
		
		// 2.3.3，能访问成功跳转
		return super.preHandle(request, response, handler);
	}

}
