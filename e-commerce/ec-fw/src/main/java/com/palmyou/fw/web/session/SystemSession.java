package com.palmyou.fw.web.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.palmyou.fw.web.session.SessionManager;

/**
 * 自定义Session类
 * @author James
 *
 */
public class SystemSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3596476624020390228L;
	/**
	 * session数据存储map
	 */
	private Map<String,Object> sessionData = new HashMap<String,Object>();
	/**
	 * sessionId  session标识
	 */
	private String sessionId = null;
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Object getAttribute(String name){
		Object value = sessionData.get(name);
		return value;
	}
	
	public void setAttribute(String name,Object value){
		sessionData.put(name, value);
		SessionManager.updateSession(this.sessionId, this);
	}
	
	public void removeAttribute(String name){
		sessionData.remove(name);
		SessionManager.updateSession(this.sessionId, this);
	}
	
	public void removeAllAttribute(){
		sessionData.clear();
		SessionManager.updateSession(this.sessionId, this);
	}
	
	public void disable(){
		SessionManager.deleteSession(this.sessionId);
	}
	
	public boolean hasAttributeName(String name){
		return sessionData.containsKey(name);
	}
	
	public Set<String> getAttributeNames(){
		return sessionData.keySet();
	}

	public Map<String, Object> getSessionData() {
		return sessionData;
	}

	public void setSessionData(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}

}
