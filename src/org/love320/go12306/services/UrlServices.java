package org.love320.go12306.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlServices {
	
	@Autowired
	private JdbcTemplate resJdbc;
	
	//获取指定用户的url 监制的车辆
	public List newCarByUserid(Integer id){
		String sql = "SELECT  `url`,  `comment` FROM `url` t where t.userid = ? LIMIT 1000 ";
		return resJdbc.queryForList(sql,id);
	}
	
}
