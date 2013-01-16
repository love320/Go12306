package org.love320.go12306.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.stereotype.Service;

import com.sina.sae.fetchurl.SaeFetchurl;

@Service
public class SaeClientHttp implements IClientHttp {
	
	private static SaeFetchurl fetchUrl = new SaeFetchurl();
	
	static final String LOGON_SITE = "http://dynamic.12306.cn";
	static final int LOGON_PORT = 80;
	private static boolean isLogin = false;// 登录

	@Override
	public byte[] newImage() {
		return  fetchUrl.fetch(LOGON_SITE+"/otsweb/passCodeAction.do?rand=sjrand"+ Math.random()).getBytes();
	}
	
	@Override
	public boolean vailed() throws HttpException, IOException {
		String content = fetchUrl.fetch(LOGON_SITE+"/otsweb/order/querySingleAction.do?method=queryLeftTicket&orderRequest.train_date=2013-02-02&orderRequest.from_station_telecode=SZQ&orderRequest.to_station_telecode=AEQ&orderRequest.train_no=&trainPassType=QB&trainClass=QB%23D%23Z%23T%23K%23QT%23&includeStudent=00&seatTypeAndNum=&orderRequest.start_time_str=00%3A00--24%3A00");
        String[] msg = content.split(",");
         if(msg.length > 0 && isLogin == false ){
        	 Integer num = Integer.parseInt(msg[0]);
        	 if(num >= 0) isLogin = true;
         }
		return isLogin;
	}

	@Override
	public boolean login(String loginname, String password, String code)
			throws HttpException, IOException {
		// 设置请求的方法(POST/GET/PUT... )
		fetchUrl.setMethod("post");
		//设置POST方法的数据
		Map maps = new HashMap();
		maps.put("loginUser.user_name",loginname);     
		maps.put("user.password", password);    
		maps.put("randCode", code);  
		maps.put("loginRand", loginRandGet());  
		maps.put("refundLogin", "");
		maps.put("refundFlag", "Y");
		        
		maps.put("nameErrorFocus", "");
		maps.put("passwordErrorFocus", "");
		maps.put("randErrorFocus", "");
		fetchUrl.setPostData(maps);
		// 当请求页面是转向页时,是否允许跳转,SAE最大支持5次跳转(默认允许跳转)
		fetchUrl.setAllowRedirect(true);
		//添加cookie数据
		//fetchUrl.setCookie("key", "value");
		/*
		 * 是否允许截断，默认为不允许 如果设置为true,当发送数据超过允许大小时,自动截取符合大小的部分;
		 * 如果设置为false,当发送数据超过允许大小时,直接返回false
		 */
		fetchUrl.setAllowTrunc(true);
		//设置读取超时时间,此时间必须小于SAE系统设置的时间,否则以SAE系统设置为准（默认为60秒）
		fetchUrl.setReadTimeout(20);
		//设置发送超时时间,此时间必须小于SAE系统设置的时间,否则以SAE系统设置为准（默认为20秒）
		fetchUrl.setSendTimeout(10);
		//设置HTTP认证用户名密码
		//fetchUrl.setHttpAuth("user", "passwd");
		//抓取url并返回响应体内容
		String result = fetchUrl.fetch(LOGON_SITE+"/otsweb/loginAction.do?method=login");
		//返回HTTP状态码
		int stat=fetchUrl.getHttpCode();
		//返回网页内容
		String body=fetchUrl.body();
		//返回数据的响应头信息
		fetchUrl.responseHeaders();
		//返回错误码
		fetchUrl.getErrno();
		//返回错误信息
		fetchUrl.getErrmsg();
		//将对象的数据重新初始化,用于多次重用一个SaeFetchurl对象
		fetchUrl.clean();
		
		System.out.println(stat);
		
		return vailed();
	}

	@Override
	public String loginRandGet() {
		 String strsing = fetchUrl.fetch("/otsweb/loginAction.do?method=loginAysnSuggest");
	        System.out.println(strsing.subSequence(14, 17));
	        strsing =strsing.subSequence(14, 17).toString();
	        return strsing;
	}

	@Override
	public boolean loginGet(String loginname, String password, String code)
			throws HttpException, IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String urlMsg(String url) {
		return fetchUrl.fetch(LOGON_SITE+url);
	}



}
