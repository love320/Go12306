package org.love320.go12306.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListMsgServices {
	
	@Autowired
	private ClientHttp client ;
	
	//业务处理
	public int business(String url){
		List<String[]> list = msgCar(urlCSV(url));
		for(String[] rows : list){
			rows = rows(rows);
		}
		return 1;
	}
	
	//获取url信息
	public String urlCSV(String url){
		String msgCSV = client.urlMsg(url);
		return msgCSV;
	}

	//信息分析有效车辆
	public List<String[]> msgCar(String msgCSV){
		String[] lineMsgS = msgCSV.split("\\n");
		List list = new ArrayList();
		for(String lineMsg : lineMsgS){
			String res = "getSelected(";
			if(lineMsg.indexOf(res) >= 0 ){
				String[] rows = lineMsg.split(",");
				list.add(rows);
			}
			
		}
		return list;
	}
	
	//处理条信息
	public String[] rows(String[] rows){
		for(String str : rows){
			System.out.println(str);
		}
		return rows;
	}
	
	
	
}
