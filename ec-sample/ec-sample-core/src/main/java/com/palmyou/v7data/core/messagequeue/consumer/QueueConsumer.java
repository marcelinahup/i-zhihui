package com.palmyou.v7data.core.messagequeue.consumer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palmyou.v7data.api.domain.message.Message;
import com.palmyou.v7data.core.messagequeue.queue.Queue;

/**
 * 消费者
 *
 * @param <T>
 */
public abstract class QueueConsumer<T extends Message> implements Runnable,Shutdownable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(QueueConsumer.class);

	protected Queue<T> queue;
	private int fetchSize = 1; // default 1
	private boolean shutdown = false;
	private long sleepTime = 5 * 1000; // 5s
	
	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	public void setQueue(Queue<T> queue) {
		this.queue = queue;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = Math.max(1,fetchSize);
	}
	
	public synchronized void shutdown(){
		shutdown = true;
	}
	
	public synchronized boolean isShutdown(){
		return shutdown ;
	}
	
	@Override
	public void run() {
		if (queue == null) {
			throw new IllegalArgumentException("Queue can't be null.");
		}

		while (!isShutdown()) {

			List<T> list = new ArrayList<T>();
			try {
				for (int i = 0; i < fetchSize; i++) {
					T object = queue.pop();
					if (object == null) {
						break;
					} else {
						list.add(object);
						if (logger.isDebugEnabled()){
							logger.debug(Thread.currentThread().getName() + "从" + queue.getClass().getSimpleName() + " 中获取" + object);
						}
					}
				}

				if (list.size() > 0) {
					consume(list);
				}else{
					sleep();
				}
				
			} catch (Throwable t) {
				logger.warn("run() - exception ignored", t); 
				sleep();
			}

		}
	}

	private void sleep(){
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
		}
	}
	
	protected abstract void consume(List<T> list);

}
