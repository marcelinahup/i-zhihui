package com.palmyou.v7data.core.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.palmyou.v7data.api.domain.message.Message;
import com.palmyou.v7data.core.messagequeue.consumer.QueueConsumer;
import com.palmyou.v7data.core.messagequeue.consumer.Shutdownable;

/**
 * 装配系统组件服务
 * @author James
 *
 */
public class SystemComponentAssemble implements InitializingBean,ApplicationContextAware{
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SystemComponentAssemble.class);
	
	private static final int DEFAULT_THREAD_NUM = 3; // 默认消费线程的数量
	
    // 消费线程的数量

	private int queueConsumerNum = DEFAULT_THREAD_NUM ; 
    // bean name
//	private String splitOrderThenSendERPConsumer ="splitOrderThenSendERPConsumer" ;
//	private String updateOrderShipmentTrackConsumer="updateOrderShipmentTrackConsumer";
	
	private ApplicationContext applicationContext;

	private List<Shutdownable> consumers = new ArrayList<Shutdownable>();
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext; 
	}
	
	private void init(){
		//拆单队列
//		initConsumer(splitOrderThenSendERPConsumer, queueConsumerNum);
		//更新快递在途信息队列
//		initConsumer(updateOrderShipmentTrackConsumer, queueConsumerNum);
	}

	@SuppressWarnings("unchecked")
	private void initConsumer(String beanName,int threadNum) {
		QueueConsumer<Message> consumer = (QueueConsumer<Message>)applicationContext.getBean(beanName);
		consumer.initData();
		for (int i = 0; i < threadNum; i++) {
			Thread t = new Thread(consumer,beanName);
			t.start();
		}
		consumers.add(consumer);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("开始装配系统组件!");
		}
		
		init();
		
		if(logger.isDebugEnabled()){
			logger.debug("系统组件装配完成!");
		}
	}
	
	/**
	 * 关闭组件
	 */
	public synchronized void destory(){
		for (Shutdownable consumer : consumers) {
			consumer.shutdown();
		}
	}
}
