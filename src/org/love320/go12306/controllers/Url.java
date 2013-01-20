package org.love320.go12306.controllers;

import java.util.List;
import java.util.Map;

import org.love320.go12306.services.ListMsgServices;
import org.love320.go12306.services.UrlServices;
import org.love320.go12306.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/url")
public class Url {

	@Autowired
	private UserServices userServices ;
	
	@Autowired
	private UrlServices urlServices;
	
	@Autowired
	private ListMsgServices listMsgServices;
	
	@RequestMapping("/list")
	public String urlListByuser(Model model,Integer userid){
		List list = urlServices.newCarByUseridValidAll(userid);
		List userlist = userServices.newUserAll();
		model.addAttribute("list",list);
		model.addAttribute("userlist",userlist);
		model.addAttribute("userid",userid);
		return "go12306/urlist";
	}
	
	@RequestMapping("/input")
	public String input(Model model,@RequestParam(required=false)Integer id,@RequestParam(required=false)Integer userid){
		if(id != null){
			Map map  = urlServices.newSing(id);
			model.addAttribute("url",map);
		}else{
			model.addAttribute("userid",userid);
		}
		return "go12306/urlinput";
	}
	
	@RequestMapping("/save")
	public ModelAndView save(@RequestParam(required=false)Integer id,Integer userid,String url,String comment,@RequestParam(required=false)Integer state){
		urlServices.save(id, userid, url, comment, state);
		return new ModelAndView("redirect:/url/list.do?userid="+userid); 
	}
	
	//测试url
	@RequestMapping("/test")
	public String urlTest(Model model,String url){
		String content = listMsgServices.urlTest(url);
		model.addAttribute("content", content);
		model.addAttribute("url", url);
		return "go12306/urltest";
	}
	
	
	
}
