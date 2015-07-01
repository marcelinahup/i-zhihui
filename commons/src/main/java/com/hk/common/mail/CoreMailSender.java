package com.hk.common.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class CoreMailSender {

	private JavaMailSender simpleMailSender;
	@SuppressWarnings("unused")
	private JavaMailSender groupMailSender;
	private VelocityEngine velocityEngine;
 
	public class MailType {
		public static final String TEXT = "text/plain;charset=utf-8";
		public static final String HTML = "text/html;charset=utf-8";
	}
	
	public void sendMail(final Mail mail) throws UnsupportedEncodingException {
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true,"utf-8");
				//设置自定义发件人昵称  
		        String nick="";  
		        try {  
		            nick=javax.mail.internet.MimeUtility.encodeText("红星家品会");  
		        } catch (UnsupportedEncodingException e) {  
		            e.printStackTrace();  
		        }   
		        String from = new String((mail.getFromAddr()).getBytes(),"utf-8");
				//发件人		        
				message.setFrom(new InternetAddress(nick+" <"+from+">"));
				
				// 如果有收件人列表，设置收件人
				if(mail.getToAddrList() != null){
					
					message.setTo(mail.getToAddrList());
				}
				
				// 如果有CC人列表，设置CC人
				if(mail.getCcAddrList() != null){
					
					message.setCc(mail.getCcAddrList());
				}
				
				// 邮件主题
				message.setSubject(mail.getSubject());
				
				// 邮件发送时间
				message.setSentDate(new Date());
				
				// 根据HTML模板设置邮件内容
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, mail.getVimName(), "utf-8", mail.getVmodel());
				//text = new String(text.getBytes(),"utf-8");
			
				// 使用HTML格式发送邮件
				message.setText(text, true);
			}
		};
		try {
			// 发邮件
			this.simpleMailSender.send(preparator);
		} catch (Exception e) {
		
		}
		
	}
	
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setSimpleMailSender(JavaMailSender simpleMailSender) {
		this.simpleMailSender = simpleMailSender;
	}

	public void setGroupMailSender(JavaMailSender groupMailSender) {
		this.groupMailSender = groupMailSender;
	}
}
