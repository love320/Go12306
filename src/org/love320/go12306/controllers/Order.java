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
		listMsgServices.orderPost();
		return true+"";
	}
}
