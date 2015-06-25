package com.palmyou.v7data.ms.controller.weixin;

import javax.annotation.PostConstruct;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

public class WxBaseController {
	
	protected WxMpConfigStorage wxMpConfigStorage;
	protected WxMpService wxMpService;
	protected WxMpMessageRouter wxMpMessageRouter;
	
	@PostConstruct
	public void init() {

		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId("wxe33b6dd899bf9ec7"); // 设置微信公众号的appid
		config.setSecret("7ac99ea1b1408885fb9e3c44d6aa419e"); // 设置微信公众号的app corpSecret
		config.setToken("tongchao"); // 设置微信公众号的token
//		config.setAesKey("..."); // 设置微信公众号的EncodingAESKey
		config.setOauth2redirectUri("http://gwt.tunnel.mobi/guide-wechat/msgController?get=1");
		
	    wxMpConfigStorage = config;
		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage);

	}

}
