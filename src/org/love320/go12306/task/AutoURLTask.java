package org.love320.go12306.task;

import java.util.TimerTask;

import org.love320.go12306.services.ListMsgServices;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoURLTask extends TimerTask {

	
	@Autowired
	private ListMsgServices listMsgServices;
	
	@Override
	public void run() {
		try {
			System.out.println("开始自动处理");
			listMsgServices.businessAuto();
			System.out.println("结束自动处理");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("定时任务：出错");
		}
		
	}

}
