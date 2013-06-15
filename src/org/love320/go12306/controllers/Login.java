package org.love320.go12306.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.love320.go12306.services.ListMsgServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

@Controller
public class Login {
	
	@Autowired
	private ListMsgServices listMsgServices;

	@RequestMapping("/kingdom")
	public String loginPage(Model model,@RequestParam(required=false)String start,@RequestParam(required=false)String end){
		if(start == null || start.trim().length() <0) start= "00:00:00";
		if(end == null || end.trim().length() <0) end= "23:59:59";
		Gson gson = new Gson();
		 java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd ");  
		Calendar calendar = Calendar .getInstance();
		calendar.add(Calendar.HOUR, 8);
		String datetime = format.format(calendar.getTime());
		List onlines = listMsgServices.carNum(datetime+start, datetime+end, 1);
		List offlines = listMsgServices.carNum(datetime+start, datetime+end, 0);
		model.addAttribute("onlines", gson.toJson(onlines));
		model.addAttribute("offlines",  gson.toJson(offlines));
		
		//cal.add(Calendar.DATE,   -1);
		calendar.add(Calendar.HOUR, 8-24);
		String yesterday = format.format(calendar.getTime());
		List onlinesyesterday = listMsgServices.carNum(yesterday+start, yesterday+end, 1);
		List offlinesyesterday = listMsgServices.carNum(yesterday+start, yesterday+end, 0);
		model.addAttribute("onlinesyesterday", gson.toJson(onlinesyesterday));
		model.addAttribute("offlinesyesterday",  gson.toJson(offlinesyesterday));
		
		
		return "line-labels/index";
	}
	
	
}
