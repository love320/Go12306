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
			listMsgServices.businessAuto();
		} catch (Exception e) {
			System.out.println("定时任务：出错");
		}
		
	}

}
