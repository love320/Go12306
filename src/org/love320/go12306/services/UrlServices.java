package org.love320.go12306.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlServices {
	
	@Autowired
	private JdbcTemplate resJdbc;
	
	//获取指定用户的url 监制的车辆
	public List newCarByUserid(Integer id){
		String sql = "SELECT  `id`,`url`,  `comment` FROM `url` t where t.userid = ?  LIMIT 1000 ";
		return resJdbc.queryForList(sql,id);
	}
	
	public List newCarByUseridValidAll(Integer id){
		String sql = "SELECT  `id`,  `url`,  `comment`,  `state`   FROM `url` t where t.userid = ? and state = 1  LIMIT 1000 ";
		return resJdbc.queryForList(sql,id);
	}
	
	public List newCarByUseridAll(Integer id){
		String sql = "SELECT  `id`,  `url`,  `comment`,  `state`   FROM `url` t where t.userid = ? and state = 1 LIMIT 1000 ";
		return resJdbc.queryForList(sql,id);
	}
	
	
	//获取单条url
	public Map newSing(Integer id){
		String sql = "SELECT  `id`,  `userid`,  `url`,  `comment`,  `state` FROM `url` where id = ? LIMIT 1";
		return resJdbc.queryForMap(sql,id);
	}
	
	//保存
	public int save(Integer id,Integer userid,String url,String comment,Integer state){
		if(state == null) state = 0;
		if(id != null){
			String sql = "UPDATE `url` SET `userid`=?, `url`=?, `comment`=?, `state`=? WHERE  `id`=? LIMIT 1;";
			return resJdbc.update(sql,userid,url,comment,state,id);
		}else{
			String sql = "INSERT INTO `url` (`userid`, `url`, `comment`, `state`) VALUES (?,?,?,?)";
			return resJdbc.update(sql,userid,url,comment,state);
		}
		
	}
	
	
}
