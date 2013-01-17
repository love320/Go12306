package org.love320.go12306.services;

import org.love320.go12306.mail.MailSenderInfo;
import org.love320.go12306.mail.SimpleMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServices {
	
    //这个类主要来发送邮件     
    SimpleMailSender sms = new SimpleMailSender();    
		
	public int sendMail(String toMail,String title,String content){
		String[] tos = toMail.split(";");
		for(String sing : tos){
			sendSingMail(sing, title, content);
		}
	    
		return 1;
	}
	
	public int sendSingMail(String toMail,String title,String content){
		MailSenderInfo mailInfo = new MailSenderInfo();      
	    mailInfo.setMailServerHost("smtp.qq.com");      
	    mailInfo.setMailServerPort("25");      
	    mailInfo.setValidate(true);      
	    mailInfo.setUserName("277191621@qq.com");      
	    mailInfo.setPassword("love#@)");//您的邮箱密码      
	    mailInfo.setFromAddress("277191621@qq.com");      
	    mailInfo.setToAddress(toMail);      
	    mailInfo.setSubject(title);      
	    mailInfo.setContent(content);      
	    SimpleMailSender sms = new SimpleMailSender();    
	    //sms.sendTextMail(mailInfo);//发送文体格式      
	    sms.sendHtmlMail(mailInfo);//发送html格式 
	    
	    return 1;
	}
}
