package org.love320.go12306.ontrollers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.love320.go12306.services.ClientHttp;
import org.love320.go12306.services.ListMsgServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Login {

	@Autowired
	private ClientHttp clientHttp;
	
	@Autowired
	private ListMsgServices listMsgServices;
	
	//下载验证码
	@RequestMapping("/imagecode")
	public ResponseEntity<byte[]> download() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		//headers.setContentDispositionFormData("attachment", "dict.txt");
		return new ResponseEntity<byte[]>(clientHttp.newImage(), headers,HttpStatus.CREATED);
	}
	
	@RequestMapping("/login")
	public String loginPage(){
		return "go12306/login";
	}
	
	@RequestMapping("/loginAction")
	public @ResponseBody Object loginAction(String name,String pwd,String code){
		boolean stat = false;
		try {
			stat = clientHttp.login(name, pwd, code);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stat+":";
	}
	
	@RequestMapping("/getMsgAction")
	public  @ResponseBody Object getMsgAction(){
		listMsgServices.business("/otsweb/order/querySingleAction.do?method=queryLeftTicket&orderRequest.train_date=2013-02-02&orderRequest.from_station_telecode=SZQ&orderRequest.to_station_telecode=AEQ&orderRequest.train_no=&trainPassType=QB&trainClass=QB%23D%23Z%23T%23K%23QT%23&includeStudent=00&seatTypeAndNum=&orderRequest.start_time_str=00%3A00--24%3A00");
		return true+"";
	}
	
	@RequestMapping("/getMsgAutoAction")
	public  @ResponseBody Object getMsgAutoAction(){
		listMsgServices.businessAuto();
		return true+"";
	}

}
