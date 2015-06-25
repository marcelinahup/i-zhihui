package com.mq.sms;


public class SmsMessageConsumer {
	

	public void receive(SmsMessage message) {
		System.out.println("core*********** Topic : " + message.getOrderSn()+","+message.getPhone());
		
	}
}
