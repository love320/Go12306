package org.love320.interfacem.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InterfaceServices extends ServicesBase {
	@Autowired
	private JdbcTemplate resJdbc;
	
	@Autowired
	private ParameterServices parameterServices; 

	/** 获取接口组名列表 */
	public List newFaceGroup() {
		String sql = "SELECT  `id`,  `name`,  `type`,  `status`,  LEFT(`text`, 256) as text,  `createtime` FROM `interfacegroup` order by id asc";
		return resJdbc.queryForList(sql);
	}

	/** 获取指定组接口列表 */
	public List newFaceList(Integer id) {
		String sql = "SELECT * FROM interface ";
		sql += " where groupid = ? order by id asc";
		return resJdbc.queryForList(sql, id);
	}
	
	/**获取指定组接口列表并加入参数信息*/
	public List newFaceListInfo(Integer id) {
		List list = newFaceList(id);
		return newAddParaInfo(list);
	}
	
	//加入参数信息
	public List newAddParaInfo(List list){
		for(Object object : list){
			Map sing = (Map)object;
			Integer id = (Integer)sing.get("id");
			//传入信息
			List inList = parameterServices.newListByIdAndSuperior(id, 1);
			//传出信息
			List outList = parameterServices.newListByIdAndSuperior(id, 2);
			sing.put("inlist",inList);
			sing.put("outlist", outList);
		}
		return list;
	}
	

	public Map newInfo(Integer id) {
		String sql = "SELECT * FROM interface WHERE id = ?";
		try {
			return resJdbc.queryForMap(sql, id);
		} catch (Exception e) {
		}
		return null;
	}

	/** 创建接口名 */
	public int newCreateFace(String name,Integer groupid) {
		// 创建接口
		String sql = "INSERT INTO `interface` (`name`,`groupid`,`createtime`) VALUES (?,?,now());";
		int id = resJdbc.update(sql, name,groupid);
		// 查询接口id
		// 使用LAST_INSERT_ID()数据库方式获取新增id （线程是安全的）
		id = resJdbc.queryForInt("SELECT LAST_INSERT_ID() from interface limit 1");
		if (id <= 0) {
			// 使用获取新增最大id，获取新新增id (线程是不安全)
			id = resJdbc.queryForInt("select max(id) from interface ");
		}
		return id;
	}

	/**保存接口信息*/
	public int save(String name, Integer id,Integer groupid, Integer type, Integer status,
			String faceurl, Integer method, String url, String writers,
			String version, String text, String returntext) {
		
		String sql = "UPDATE `interface` SET `groupid`=?, `name`=?, `type`=?, `status`=?, `faceurl`=?,  `method`=? , `url`=? , `writers`=? , `version`=? , `text`=?  , `returntext`=?,`updatetime`=now() WHERE  `id`=? LIMIT 1;";
		return resJdbc.update(sql,groupid,name,type,status,faceurl,method,url,writers,version,text,returntext,id);
	}

	public int delete(Integer id) {
		String sql = "DELETE FROM `interface` WHERE  `id`=? LIMIT 1;";
		resJdbc.update(sql,id);
		sql = "DELETE FROM `parameter` WHERE  `faceid`=? LIMIT 1;";
		return resJdbc.update(sql,id);
	}

}
