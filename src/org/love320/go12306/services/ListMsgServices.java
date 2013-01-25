package org.love320.go12306.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

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
	
	public int orderPost(String selectStr ){
		//String selectStr = "K9075#12:02#22:28#6a000K907507#AEQ#BJQ#10:30#益阳#深圳东#02#10#1*****30444*****00021*****01413*****0000#20C5191549116E23B373E0FAE816A8F507ADF8DE8899C7F4FCA054E3#Q6";
		String[] StrS = selectStr.split("#");
		Map postData = new HashMap<String,String>();
		postData.put("station_train_code", StrS[0]);
		postData.put("lishi", StrS[1]);
		postData.put("train_start_time", StrS[2]);
		postData.put("trainno4", StrS[3]);
		postData.put("from_station_telecode", StrS[4]);
		postData.put("to_station_telecode", StrS[5]);
		postData.put("arrive_time", StrS[6]);
		postData.put("from_station_name", StrS[7]);
		postData.put("to_station_name", StrS[8]);
		postData.put("from_station_no", StrS[9]);
		postData.put("to_station_no", StrS[10]);
		postData.put("ypInfoDetail", StrS[11]);
		postData.put("mmStr", StrS[12]);
		postData.put("locationCode", StrS[13]);
		
		postData.put("train_date", "2013-02-12");
		postData.put("seattype_num", "");
		postData.put("include_student", "00");
		postData.put("from_station_telecode_name", StrS[7]);
		postData.put("to_station_telecode_name",  StrS[8]);
		postData.put("round_train_date", "2013-02-13");
		postData.put("round_start_time_str", "00:00--24:00");
		postData.put("single_round_type", "1");
		postData.put("train_pass_type", "QB");
		postData.put("train_class_arr", "QB#D#Z#T#K#QT#");
		postData.put("start_time_str", "00:00--24:00");
		
		String url = "https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=submutOrderRequest";
		String contant = "";
		contant = client.urlPostMsg(url, postData);
		contant = client.urlMsg("https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
		Map orderMap = htmlParse(contant);
		String rand = null;
		System.out.println();
		orderMap.put("randCode", rand);
		
		//验证是否可以预订
		orderMap.put("tFlag", "dc");
		contant= client.urlPostMsg("https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=checkOrderInfo&rand="+rand,orderMap);
		
		//验证提交
		Gson gson = new Gson();
		Map checkOrder = gson.fromJson(contant, Map.class);
		System.out.println(checkOrder.get("errMsg"));
		if(checkOrder.get("errMsg").toString().equals("Y")){
			//获取排队情况
			String par = "train_date="+orderMap.get("orderRequest.train_date")+
					"&train_no="+orderMap.get("orderRequest.train_no")+
					"&station="+orderMap.get("orderRequest.station_train_code")+
					"&seat="+orderMap.get("passenger_1_seat")+
					"&from="+orderMap.get("orderRequest.from_station_telecode")+
					"&to="+orderMap.get("orderRequest.to_station_telecode")+
					"&ticket="+orderMap.get("leftTicketStr");
			//
			contant = client.urlMsg("http://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=getQueueCount&"+par);
			//提交
			orderMap.put("orderRequest.reserve_flag","A");
			orderMap.put("checkbox0",0);
			contant= client.urlPostMsg("https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=confirmSingleForQueue",orderMap);
		    checkOrder = gson.fromJson(contant, Map.class);
			//获取预订号
		    if(checkOrder.get("errMsg").toString().equals("Y")){
		    	contant = client.urlMsg("https://dynamic.12306.cn/otsweb/order/myOrderAction.do?method=queryOrderWaitTime&tourFlag=dc");
		    	//支持
				//contant = client.urlMsg("https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=payOrder&orderSequence_no="+rand);
		    }
			System.out.println(contant);
		}
		return 1;
	}
	
	//分析html并返回 map
	public Map htmlParse(String html){
		Map inputMap = new HashMap<String,String>();
		Document doc = Jsoup.parse(html);
		Elements links = doc.getElementsByTag("input");
		for (Element link : links) {
		  String name = link.attr("name");
		  String value =  link.attr("value");
		  inputMap.put(name, value);
		}
		
		inputMap.put("passenger_1_seat",1);
		inputMap.put("passenger_1_ticket",1);
		inputMap.put("passenger_1_cardtype",1);
		
		inputMap.put("checkbox1", 1);
		inputMap.put("orderRequest.train_date", "2013-01-25");
		inputMap.put("passengerTickets", "1,0,1,向文韬,1,430681198902062057,,Y");
		inputMap.put("passenger_1_name", "向文韬");
		inputMap.put("passenger_1_cardno", "430681198902062057");
		inputMap.put("oldPassengers", "向文韬,1,430681198902062057");
		
		return inputMap;
	}

}
