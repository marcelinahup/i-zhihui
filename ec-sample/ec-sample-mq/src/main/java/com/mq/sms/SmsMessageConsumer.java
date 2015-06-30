package com.mq.sms;

import com.palmyou.v7data.ms.controller.weixin.WxBaseController;

import me.chanjar.weixin.mp.bean.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.WxMpTemplateMessage;

public class SmsMessageConsumer extends WxBaseController {
	
	/**
	 * 接收处理消息
	 * @param message
	 */
	public void receive(SmsMessage message) {
		System.out.println("core*********** Topic : " + message.getOrderSn()+","+message.getPhone());
		
		WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
		templateMessage.setToUser("oHA7GuAhd-epRwnQAT3R7ZRIQNc8");
		templateMessage.setTemplateId("EA_boUyvAVUM9TW5-C5ATq3pIQ5lGCWUSO9tLH4ajBU");
		templateMessage.setUrl("www.baidu.com");
		templateMessage.setTopColor("#FF0000");
		templateMessage.getDatas().add(new WxMpTemplateData("code", "22222222222", "#173177"));
		templateMessage.getDatas().add(new WxMpTemplateData("detail", "www.goldpalm.net", "#173177"));

		try {
			System.out.println("短连接：" + wxMpService.shortUrl("www.baidu.com"));
			wxMpService.templateSend(templateMessage);
		}catch(Exception exception){
			
		}
	}
}
