package org.love320.go12306.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.love320.go12306.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class User {
	
	@Autowired
	private UserServices userServices ;

	@RequestMapping("/list")
	public String userList(Model model){
		List list = userServices.newUserValidAll();
		model.addAttribute("list", list);
		return "go12306/userlist";
	}
	
	@RequestMapping("/input")
	public String userinput(Model model ,@RequestParam(value="",required=false) Integer userid){
		Map user = null;
		if(userid != null){
		    user = userServices.newUserSing(userid);
		}
		model.addAttribute("user", user);
		return "go12306/userinput";
	}
	
	@RequestMapping("/save")
	public ModelAndView  save(@RequestParam(required=false)Integer id,  String  username, String  password, String  email,  @RequestParam(required=false)Integer state ){
		userServices.save(id, username, password, email, state);
		return new ModelAndView("redirect:/user/list.do"); 
	}

	
	
}
