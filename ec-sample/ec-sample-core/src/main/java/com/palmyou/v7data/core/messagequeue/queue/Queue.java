package com.palmyou.v7data.core.messagequeue.queue;

import com.palmyou.v7data.api.domain.message.Message;

/**
 * 队列接口
 *
 */
public interface Queue<T extends Message>  {
	
	/**
	 * 将元素放到队列后面
	 * @param object
	 */
	public  void push(T message);
	
	/**
	 * 从队列头出取一个元素
	 * @param object
	 */
	public  T pop();
	
	/**
	 * 获取队列名
	 * @return
	 */
	public String getQueueName();
	
}
