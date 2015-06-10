package com.palmyou.v7data.ms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.palmyou.fw.web.permission.WebUser;
import com.palmyou.fw.web.session.SessionManager;
import com.palmyou.fw.web.session.SystemSession;
import com.palmyou.v7data.ms.util.MsgUtil;

@Controller
public class IndexController extends BaseController{
	private static final Logger logger = Logger.getLogger(IndexController.class);

	@RequestMapping({"/login","/404","500"})
    public void login(){
		// 原始密码输入有误！
		System.out.println(MsgUtil.getMsg("CE-00001"));
		logger.debug("html loading ...");
	}
	
	@RequestMapping("/doLogin")
	public ModelAndView doLogin(ModelAndView mav, HttpServletRequest request,HttpServletResponse response,
			@RequestParam String op,@RequestParam String p2){
		
		System.out.println(op);
		System.out.println(p2);
		
		// 保存用户信息
		SystemSession session = SessionManager.getSession(request, response);
		if ("0".equals(op)) {
			
			session.setAttribute("loginUser", new WebUser("123"));
		}else {
			
			session.setAttribute("loginUser", new WebUser("456"));
		}
		mav.setViewName("home");
		return mav;
	}
	
}
