package com.palmyou.v7data.ms.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hk.ec.fw.web.permission.WebUser;
import com.palmyou.v7data.ms.util.MsgUtil;

@Controller
public class IndexController extends BaseController{
	private static final Logger logger = Logger.getLogger(IndexController.class);

	@RequestMapping({"/home"})
	public void home(HttpSession session){
		
		session.setAttribute("111", "222");
		System.out.println(session.getAttribute("111"));
		
		// 原始密码输入有误！
		System.out.println(MsgUtil.getMsg("CE-00001"));
		logger.debug("html loading ...");
	}
	
	@RequestMapping("/ajaxView")
    public ModelAndView showContentPart(ModelAndView model) {
        model.addObject("name", "myvalue");
        model.addObject("name1", "111111111111");
        model.setViewName("tpls/userTpls :: content");
        return model;
    }
	
	@RequestMapping("/doLogin")
	public ModelAndView doLogin(ModelAndView mav, HttpSession session,
			@RequestParam String op,@RequestParam String p2){
		
		System.out.println(op);
		System.out.println(p2);
		
		// 保存用户信息
		if ("0".equals(op)) {
			
			session.setAttribute("loginUser", new WebUser("123"));
		}else {
			
			session.setAttribute("loginUser", new WebUser("456"));
		}
		mav.setViewName("home");
		return mav;
	}
	
}
