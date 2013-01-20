package org.love320.go12306.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
	
	@Autowired
	private JdbcTemplate resJdbc;

	//获取所有用户
	public List newUserAll(){
		String sql = "SELECT  `id`,  `username`,  `password`,  `email`,  `state` FROM `user`  ORDER BY `username` DESC LIMIT 1000 ";
		return resJdbc.queryForList(sql);
	}
	
	//获取所有有效用户
		public List newUserValidAll(){
			String sql = "SELECT  `id`,  `username`,  `password`,  `email`,  `state` FROM `user`  where state = 1 ORDER BY `username` DESC LIMIT 1000 ";
			return resJdbc.queryForList(sql);
		}
	
	//单用户信息
	public Map newUserSing(Integer userid){
		String sql =  "SELECT  `id`,  `username`,  `password`,  `email`,  `state` FROM `user`  where id = ? LIMIT 1 ";
		return resJdbc.queryForMap(sql,userid);
	}
	
	//保存
	public int save(Integer id,  String  username, String  password, String  email,  Integer state ){
		if(state == null) state = 0;
		if(id != null ){
			String sql = "UPDATE `user` SET `username`=?, `password`=?, `email`=?, `state`=? WHERE  `id`=? LIMIT 1;";
			return resJdbc.update(sql,username,password,email,state,id);
		}else{
			String sql = "INSERT INTO `user` (`username`, `password`, `email`,`state`) VALUES (?,?,?,?)";
			return resJdbc.update(sql,username,password,email,state);
		}
		
	}
	
}
