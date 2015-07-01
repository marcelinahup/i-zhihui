package com.hk.ec.fw.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.hk.ec.fw.spring.ext.PropertiesUtils;

/**
 * 返回值包装对象
 * @param <T>
 * 
 */
public class JsonResult<T> implements Serializable {

	private static final long serialVersionUID = -6652076509848001811L;

	public JsonResult(){}
	public JsonResult(boolean ok, String message){
		this.ok = ok;
		this.message = message;
	}

	public JsonResult(T data, boolean isOk, String message){
		this.data = data;
		this.ok = isOk;
		this.message = message;
	}
	
	private boolean ok = false;
	
	/**
	 *  msg 状态0表示异常、1表示正确
	 */
	private int messageType = 0;
	
	/**
	 * 备注
	 */
	private String comment;
	/**
	 * 消息code
	 */
	private String messageCode;
	private String message;
	/**
	 * 数据(单类型对象)
	 */
	private T data;
	/**
	 * 数据Map（多类型对象）
	 */
	private Map<String, Object> dataMap = new HashMap<String, Object>();

	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	
	public int getMessageType(){
		return this.messageType;
	}
	public String getMessageCode() {
		return messageCode;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 根据msg第二位字母取得msg类型
	 * @param messageCode
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
		try {
			char state = messageCode.charAt(1);
			switch (state) {
			case 'I':
				this.messageType = 1;
				break;
			case 'Q':
				this.messageType = 1;
				break;
			case 'E':
				this.messageType = 0;
				break;
				
			default:
				this.messageType = 0;
				break;
			}
			
			// 到properties里面取得消息
			this.message = PropertiesUtils.getString(messageCode);
			
		} catch (Exception e) {
			this.messageType = 0;
		}
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	public void putData(String key, Object value) {
		this.dataMap.put(key, value);
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

}
