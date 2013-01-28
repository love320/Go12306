package org.love320.go12306.controllers;

import org.love320.go12306.services.ListMsgServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class Order {
	
	@Autowired
	private ListMsgServices listMsgServices;
	
	@RequestMapping("/test")
	public  @ResponseBody Object getMsgAction(){
		listMsgServices.orderPost("K9075#12:02#22:28#6a000K907507#AEQ#BJQ#10:30#益阳#深圳东#02#10#1*****30444*****00021*****01413*****0000#20C5191549116E23B373E0FAE816A8F507ADF8DE8899C7F4FCA054E3#Q6");
		return true+"";
	}
}
