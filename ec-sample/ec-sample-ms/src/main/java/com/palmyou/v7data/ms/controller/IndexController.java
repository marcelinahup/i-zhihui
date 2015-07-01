package com.palmyou.v7data.ms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.fragment.IFragmentSpec;
import org.thymeleaf.standard.fragment.StandardDOMSelectorFragmentSpec;

import com.hk.ec.fw.result.JsonResult;
import com.hk.ec.fw.web.permission.WebUser;
import com.palmyou.v7data.ms.util.MsgUtil;

@Controller
public class IndexController extends BaseController{
	private static final Logger logger = Logger.getLogger(IndexController.class);

	@Resource
	TemplateEngine templateEngine;
	
	@RequestMapping("/thtemplate")
	@ResponseBody
    public JsonResult<String> getString(HttpServletRequest request, HttpServletResponse response) {
		
		WebContext context = new WebContext(request, response, request.getServletContext());
		String templateName = "tpls/userTpls";
		// 标准DOM选择器是th:fragment="content"
		IFragmentSpec fragmentSpec = new StandardDOMSelectorFragmentSpec("content");
		context.setVariable("name", "我是名字");
		context.setVariable("name1", "我是名字1");
		String html = templateEngine.process(templateName, context, fragmentSpec);
		System.out.println(html);
        return new JsonResult<String>(html,false,"xxx");
    }
	
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
