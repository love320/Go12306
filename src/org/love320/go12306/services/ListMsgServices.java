package org.love320.go12306.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListMsgServices {

	@Autowired
	private ClientHttp client;

	@Autowired
	private UserServices userServices;

	@Autowired
	private UrlServices urlServices;
	
	@Autowired
	private MailServices mailServices;

	// 定时(http)业务处理
	public int businessAuto() {
		
		if(!client.isLogin()) {
			//mailServices.sendMail("277191621@qq.com", "12306 定时任务 没登录！", "12306 定时任务 没登录");
			client.sendMail("277191621@qq.com", "12306 定时任务 没登录！", "12306 定时任务 没登录");
			return 0;
		}

		// 获取所有用户
		List<Map> userList = userServices.newUserValidAll();
		for (Map user : userList) {
			// 邮件
			String content = "";

			// 获取用户url
			List<Map> urList = urlServices.newCarByUseridValidAll((Integer) user.get("id"));
			for (Map url : urList) {
				// 处理url
				List<String> list = msgCar(urlCSV(url.get("url").toString()));
				
				 try {  
	                    Thread.sleep(1000);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }
				if(list.size() > 0 ) content += url.get("comment")+ "<br/>";
				for (String line : list) {
					String[] rows = rows(line);
					content += rowsToLook(rows) + "<br/>";
				}
			}

			// 装载信息
			user.put("content", content);
		}

		for (Map user : userList) {
			// 发邮件
			String email = user.get("email").toString();
			String content = user.get("content").toString();
			if(content != null && content.trim().length() >0 ) client.sendMail(email, "12306 有票了！", content); ///mailServices.sendMail(email, "12306 有票了！", content);
		}
		return 1;
	}
	
	//测试url
	public String urlTest(String url){
		String content = "";
		// 处理url
		List<String> list = msgCar(urlCSV(url));
		for (String line : list) {
			String[] rows = rows(line);
			content += rowsToLook(rows) + "<br/>";
		}
		return content;
	}

	// 业务处理
	public int business(String url) {
			List<String> list = msgCar(urlCSV(url));
			for (String line : list) {
				String[] rows = rows(line);
			}
		return 1;
	}

	// 获取url信息
	public String urlCSV(String url) {
		String msgCSV = client.urlMsg(url);
		return msgCSV;
	}

	// 信息分析有效车辆
	public List<String> msgCar(String msgCSV) {
		String[] lineMsgS = msgCSV.split("\\\\n");
		List list = new ArrayList();
		for (String lineMsg : lineMsgS) {
			String res = "getSelected(";
			if (lineMsg.indexOf(res) >= 0) {
				//System.out.println(lineMsg);
				list.add(lineMsg);
			}
		}
		return list;
	}

	//条信息转 列
	public String[] rows(String lineMsg) {
		String[] rows = lineMsg.split(",");
		for (String str : rows) {
			//System.out.println(str);
		}
		return rows;
	}
	
	//处理列信息
	public String rowsToLook(String[] rows){
		if(rows.length != 17) return null;
		
		//rows[0]  ;// 条次
		rows[1] = filterHtml(rows[1]) + " 列车  "; //车次
		rows[2] = filterHtml(rows[2].replace("&nbsp;","")) + " - "; //起点
		rows[3] = filterHtml(rows[3].replace("&nbsp;","")); //终点
		
		rows[4] = "  历时:" + filterHtml(rows[4].replace("&nbsp;","")); //历时
		
		rows[4+1] =  location(rows[4+1],"商务座"); 
		rows[4+2] =  location(rows[4+2],"特等座"); 
		rows[4+3] =  location(rows[4+3],"一等座"); 
		rows[4+4] =  location(rows[4+4],"二等座");
		rows[4+5] =  location(rows[4+5],"高级软卧");
		rows[4+6] =  location(rows[4+6],"软卧");
		rows[4+7] =  location(rows[4+7],"硬卧");
		rows[4+8] =  location(rows[4+8],"软座");
		rows[4+9] =  location(rows[4+9],"硬座"); 
		rows[4+10] = location(rows[4+10],"无座"); 
		rows[4+11] =  location(rows[4+11],"其它");
		
		rows[4+12] =  filterHtml(rows[4+12].replace("&nbsp;","")); //预订
		
		//处理无效
		String line = "";
		for(int i  = 1 ;i<rows.length-1 ;i++){
			line += rows[i];
		}
		//System.out.println(""+line);
		return line;
	}
	
	public String location(String row,String comment){
		String str = filterHtml(row.replace("&nbsp;",""));
		if(str.trim().equals("有")) return "  "+comment +":有";
		try {
			Integer num = Integer.parseInt(str);
			if(num > 0 ) return "  "+comment +":"+num;
		} catch (Exception e) {
		}
		return "";
	}
	
	public String filterHtml(String str) { 
		  String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
		  String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签
		  String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性
		  Pattern pattern = Pattern.compile(regxpForHtml); 
		  Matcher matcher = pattern.matcher(str); 
		  StringBuffer sb = new StringBuffer(); 
		  boolean result1 = matcher.find(); 
		  while (result1) { 
		   matcher.appendReplacement(sb, ""); 
		   result1 = matcher.find(); 
		  } 
		  matcher.appendTail(sb); 
		  return sb.toString(); 
		}

}
