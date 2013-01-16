package org.love320.go12306.services;

import org.springframework.stereotype.Service;

import com.sina.sae.mail.SaeMail;

@Service
public class SaeMailServices {
	
	public int sendEmail(String toAddress,String content){
		SaeMail mail = new SaeMail();
		//快速发送邮件
		//mail.quickSend("from@sina.com",new String[]{"to@sina.cn"},"邮件标题","邮件内容","smtpaccount@gmail.com","password");
		mail.clean();//重用此对象 再次发送
		mail.setFrom("12306l@sina.com");
		mail.setSmtpUsername("12306l@sina.com");
		mail.setSmtpPassword("311527");
		//mail.setCc(new String[] { "cc@sina.cn" });//抄送地址
		mail.setTo(toAddress.split(";"));//接收地址
		mail.setSubject("邮件主题");
		mail.setContentType("HTML");//邮件内容形式可选HTML/TEXT
		mail.setContent("邮件内容"+content);
		//mail.setAttach(new String[]{"test.txt"});//设置附件
		//mail.setTls(true);//gmail需要设置此项 其他邮箱不需要
		//mail.setCallbackUrl("callbackurl");//设置发送失败后回调的url
		boolean isOk = mail.send();//发送邮件
		//失败输出错误码和错误信息
		if(!isOk){
		     System.out.println(mail.getErrno());
		     System.out.println(mail.getErrmsg());
		}
		return 1;
	}

}
