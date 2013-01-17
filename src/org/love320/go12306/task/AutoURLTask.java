package org.love320.go12306.task;

import java.util.TimerTask;

import org.love320.go12306.services.ListMsgServices;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoURLTask extends TimerTask {

	
	@Autowired
	private ListMsgServices listMsgServices;
	
	@Override
	public void run() {
		System.out.println(">>>");
		listMsgServices.businessAuto();
	}

}
