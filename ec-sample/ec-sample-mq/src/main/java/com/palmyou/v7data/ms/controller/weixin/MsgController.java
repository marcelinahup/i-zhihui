package com.palmyou.v7data.ms.controller.weixin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.WxMpTemplateMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("msgController")
public class MsgController extends WxBaseController{

	String nonce = null;
	String timestamp = null;
	@RequestMapping(method=RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String get = request.getParameter("get");
		if (!StringUtils.isBlank(get)) {
			
			String code = request.getParameter("code");
			WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
			try {
				wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
				WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
				System.out.println(wxMpUser.getCity());
				
			} catch (WxErrorException e) {
				e.printStackTrace();
			}
		}
		
		// 构造网页授权url
		String url = wxMpService.oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_USER_INFO, null);
		System.out.println("url" + url);
		// 
		
		doPost(request, response);
		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String tp = request.getParameter("t");
		String openid = "oHA7GuAhd-epRwnQAT3R7ZRIQNc8";
//		String openid = "oHA7GuLqkFrIA7rsRsVlY0jgsSUQ";
		
		WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
		if (tp == null || tp.length() == 0) {
			
//			templateMessage.setToUser(openid);
//			templateMessage.setTemplateId("PB7K42MzvzOeIaJA0_KysHxXPZMsq5zBY0fVC17-HQE");
//			templateMessage.setUrl("www.baidu.com");
//			templateMessage.setTopColor("#FF0000");
//			templateMessage.getDatas().add(new WxMpTemplateData("orderid", "123123", "#173177"));
//			templateMessage.getDatas().add(new WxMpTemplateData("product", "【你的商品】", "#173177"));
			templateMessage.setToUser(openid);
			templateMessage.setTemplateId("BQ3hrMq1YUFH1KWKWoZm38XMhmpDgSSNhkZMB-nj2Ns");
//			templateMessage.setUrl("www.baidu.com");
			templateMessage.setTopColor("#FF0000");
			templateMessage.getDatas().add(new WxMpTemplateData("content", "这些都是我的消息内容，使用一个标签标识，这样微信会拦截吗？我测试看一下：订单号是123，商品名是：xxx保险，支付成功啦！", "#173177"));
			
			String lang = "zh_CN"; //语言
			try {
				openid = "oHA7GuG6O3c8GSydg47ri1279yPM";
				WxMpUser user = wxMpService.userInfo(openid, lang);
				
				System.out.println(user.getUnionId());
				System.out.println(user.getNickname());
				
			} catch (WxErrorException e) {
				e.printStackTrace();
			}
		}
		else {
			
			templateMessage.setToUser("oHA7GuAA5TaV8d-S_t8hzELkEy1M");
			templateMessage.setTemplateId("EA_boUyvAVUM9TW5-C5ATq3pIQ5lGCWUSO9tLH4ajBU");
			templateMessage.setUrl("www.baidu.com");
			templateMessage.setTopColor("#FF0000");
			templateMessage.getDatas().add(new WxMpTemplateData("code", "22222222222", "#173177"));
			templateMessage.getDatas().add(new WxMpTemplateData("detail", "www.goldpalm.net", "#173177"));
		}

		try {
			wxMpService.templateSend(templateMessage);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		return;
	}

}