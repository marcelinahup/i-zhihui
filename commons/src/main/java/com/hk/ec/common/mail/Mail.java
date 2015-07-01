package com.hk.ec.common.mail;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.hk.ec.common.mail.CoreMailSender.MailType;

/**
 * 邮件
 * 
 * @author tongc 2012.11.09
 */
public class Mail implements Serializable{

	private static final long serialVersionUID = -1389134493300596559L;

	private String subject;
	private String userName;
	private String fromAddr;
	/** 模板名含目录 */
	private String vimName;
	private Map<String,Object> vmodel = new HashMap<String, Object>();
	private String[] toAddrList;
	private String[] ccAddrList;
	private String mailType = MailType.HTML;
 
	public String getVimName() {
		return vimName;
	}

	public void setVimName(String vimName) {
		this.vimName = vimName;
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
	
	public Map<String, Object> getVmodel() {
		return vmodel;
	}

	public void setVmodel(Map<String, Object> vmodel) {
		this.vmodel = vmodel;
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

	/**
	 * 给模板赋值
	 * @param key
	 * @param value
	 * @throws UnsupportedEncodingException 
	 */
	public void put(String key,String value) {
		if (value == null) {
			this.vmodel.put(key,"");
		} else {
			this.vmodel.put(key,value);
		}
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