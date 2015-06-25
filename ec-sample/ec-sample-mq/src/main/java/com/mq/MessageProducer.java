package com.mq;

import java.io.Serializable;

import javax.jms.Topic;

import org.springframework.jms.core.JmsTemplate;

public class MessageProducer {
	
	protected JmsTemplate template;

	protected Topic destination;

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public void setDestination(Topic destination) {
		this.destination = destination;
	}

	public void send(Serializable message) {

		template.convertAndSend(this.destination, message);
	}
	
}
