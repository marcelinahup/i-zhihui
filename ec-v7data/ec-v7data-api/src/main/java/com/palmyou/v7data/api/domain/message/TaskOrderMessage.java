package com.palmyou.v7data.api.domain.message;

/*
 * 订单定时任务消息
 */
public class TaskOrderMessage  extends Message {

	private static final long serialVersionUID = -5607937478878499565L;
	
	private String orderSn;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	
}
