package org.love320.go12306.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	private SaeMailServices saeMailServices;

	// 定时(http)业务处理
	public int businessAuto() {

		// 获取所有用户
		List<Map> userList = userServices.newUserAll();
		for (Map user : userList) {
			// 邮件
			String content = "";

			// 获取用户url
			List<Map> urList = urlServices.newCarByUserid((Integer) user
					.get("id"));
			for (Map url : urList) {
				// 处理url
				List<String> list = msgCar(urlCSV(url.get("url").toString()));
				for (String line : list) {
					String[] rows = rows(line);
					content += line + "<br/>";
				}
			}

			// 装载信息
			user.put("content", content);
		}

		for (Map user : userList) {
			// 发邮件
			String email = user.get("email").toString();
			String content = user.get("content").toString();
			// if(content != null && content.trim().length() >0 )
			// saeMailServices.sendEmail(email,content);
		}

		return 1;
	}

	// 业务处理
	public int business(String url) {
		if (client.isLogin()) {
			List<String> list = msgCar(urlCSV(url));
			for (String line : list) {
				String[] rows = rows(line);
			}
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
		String[] lineMsgS = msgCSV.split("\\n");
		List list = new ArrayList();
		for (String lineMsg : lineMsgS) {
			String res = "getSelected(";
			if (lineMsg.indexOf(res) >= 0) {
				list.add(lineMsg);
			}
		}
		return list;
	}

	// 处理条信息
	public String[] rows(String lineMsg) {
		String[] rows = lineMsg.split(",");
		for (String str : rows) {
			System.out.println(str);
		}
		return rows;
	}

}
