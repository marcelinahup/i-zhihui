package com.palmyou.v7data.api.domain.message;


/**
 * email消息实体
 * 
 * @author James
 *
 */
public class EmailMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1354139073945214711L;
	
	private String email ;
	private String content;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
