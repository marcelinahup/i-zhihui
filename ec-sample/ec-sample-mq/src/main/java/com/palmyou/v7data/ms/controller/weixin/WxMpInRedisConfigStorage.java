package com.palmyou.v7data.ms.controller.weixin;

import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;

public class WxMpInRedisConfigStorage extends WxMpInMemoryConfigStorage {

	/**
	 * 从redis里面取出来
	 */
	@Override
	public String getAccessToken() {
		// TODO Auto-generated method stub
		return super.getAccessToken();
	}
	
	/**
	 * 更新redis里面的数据
	 */
	@Override
	public synchronized void updateAccessToken(String accessToken,
			int expiresInSeconds) {
		// TODO Auto-generated method stub
		super.updateAccessToken(accessToken, expiresInSeconds);
	}
	
	/**
	 * 更新redis里面的数据
	 */
	@Override
	public synchronized void updateAccessToken(WxAccessToken accessToken) {
		// TODO Auto-generated method stub
		super.updateAccessToken(accessToken);
	}
	
	/**
	 * 更新redis里面的数据
	 */
	@Override
	public synchronized void updateJsapiTicket(String jsapiTicket,
			int expiresInSeconds) {
		// TODO Auto-generated method stub
		super.updateJsapiTicket(jsapiTicket, expiresInSeconds);
	}
	
	/**
	 * 从redis里面取数据
	 */
	@Override
	public String getJsapiTicket() {
		// TODO Auto-generated method stub
		return super.getJsapiTicket();
	}

}
