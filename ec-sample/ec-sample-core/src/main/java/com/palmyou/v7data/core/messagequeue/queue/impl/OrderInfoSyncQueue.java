package com.palmyou.v7data.core.messagequeue.queue.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.palmyou.v7data.api.domain.message.EmailMessage;
import com.palmyou.v7data.core.messagequeue.queue.Queue;

public class OrderInfoSyncQueue implements Queue<EmailMessage> {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderInfoSyncQueue.class);
			
	private final static String queueName = "splitOrderThenSendERPQueue"; 
	@Resource
	private RedisTemplate<String,EmailMessage> redisTemplate;
	
	@Override
	public void push(EmailMessage message) {
		synchronized(queueName) {
			try{
				boolean needPush = true;
				if(needPush){
					redisTemplate.opsForList().leftPush(queueName, message);
				}
			}catch(Exception e){
				logger.error("发布失败message:"+message.toString(), e);
			}
		}
	}
	
	@Override
	public EmailMessage pop(){
		EmailMessage message = null;
		synchronized(queueName) {
			try{
				message = redisTemplate.opsForList().rightPop(queueName);
			}catch(Exception e){
				logger.error("获取消息失败", e);
			}
		}
		return message;
	}

	@Override
	public String getQueueName() {
		return queueName;
	}

}
