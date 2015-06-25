package com.mq.sms;

import java.io.Serializable;


public class SmsMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5772618687504923832L;

    private String orderSn;
    
    private String phone;
    
    private String status;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	 


}
