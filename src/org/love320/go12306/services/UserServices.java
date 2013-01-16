package org.love320.go12306.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
	
	@Autowired
	private JdbcTemplate resJdbc;

	//获取所有用户
	public List newUserAll(){
		String sql = "SELECT  `id`,  `username`,  `password`,  `email`,  `state` FROM `user` ORDER BY `username` DESC LIMIT 1000 ";
		return resJdbc.queryForList(sql);
	}
	
	
}
