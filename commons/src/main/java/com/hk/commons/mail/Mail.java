package com.hk.commons.mail;

import java.io.Serializable;

import com.hk.commons.mail.CoreMailSender.MailType;

/**
 * 邮件
 * 
 * @author James 2012.11.09
 */
public class Mail implements Serializable{

	private static final long serialVersionUID = -1150490029820753996L;

	private String subject;
	private String userName;
	private String fromAddr;
	private String content;
	private String[] toAddrList;
	private String[] ccAddrList;
	private String mailType = MailType.HTML;
 
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getFromAddr() {
		return fromAddr;
	}

	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}
	
	/**
	 * 邮件类型
	 * @return 邮件类型
	 */
	public String getMailType() {
		return mailType;
	}

	/**
	 * 设定邮件类型
	 * @param mailType 邮件类型
	 */
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	/**
	 * 主题
	 * 
	 * @return String 主题
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设定主题
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getToAddrList() {
		return toAddrList;
	}

	public void setToAddrList(String ... toAddrs) {
		this.toAddrList = toAddrs;
	}

	public String[] getCcAddrList() {
		return ccAddrList;
	}
	
	public void setCcAddrList(String ... ccAddrs) {
		this.ccAddrList = ccAddrs;
	}

}