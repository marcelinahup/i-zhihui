package com.palmyou.v7data.api.domain.message;

import java.io.Serializable;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * 消息基类
 *
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){

		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		try {
			json = objectMapper.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
