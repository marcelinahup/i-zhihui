package com.palmyou.v7data.ms;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.palmyou.v7data.ms.controller.BaseController;
import com.palmyou.v7data.ms.util.MsgUtil;

@Controller
public class TestController extends BaseController{
	private static final Logger logger = Logger.getLogger(TestController.class);

	@RequestMapping("/msg")
    public void login(){ 
		
		// 原始密码输入有误！
		System.out.println(MsgUtil.getMsg("CE-00001"));
		logger.debug("html loading ...");
	}
	
}
