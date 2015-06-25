package com.palmyou.v7data.ms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mq.MessageProducer;
import com.mq.sms.SmsMessage;
import com.palmyou.fw.result.JsonResult;

@Controller
public class IndexController extends BaseController{
	private static final Logger logger = Logger.getLogger(IndexController.class);

	@Resource
	MessageProducer smsMessageProducer;
	
	@RequestMapping({"/mq"})
	@ResponseBody
	public JsonResult home(JsonResult result){
		
		SmsMessage message = new SmsMessage();
		message.setOrderSn("xxxxxxxxxxxxxxxxx");
		smsMessageProducer.send(message);
		logger.debug("message sent successly");
		result.setOk(true);
		return result;
	}
	
}
