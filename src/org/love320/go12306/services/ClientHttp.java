package org.love320.go12306.services;

import java.io.IOException;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Service;

//@Service
public class ClientHttp implements IClientHttp {

	private static HttpClient client = new HttpClient();// 浏览器
	private static boolean isLogin = false;// 登录
	static final String LOGON_SITE = "dynamic.12306.cn";
	static final int LOGON_PORT = 80;
	
	ClientHttp(){
		client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
	}

	// 获取浏览器
	public static HttpClient getClient() {
		if(isLogin){
			return client;
		} else{
			return null;
		}
		
	}

	public static boolean isLogin() {
		return isLogin;
	}

	// 验证是否登录
	@Override
	public boolean vailed() throws HttpException, IOException {
		GetMethod get = new GetMethod("/otsweb/order/querySingleAction.do?method=queryLeftTicket&orderRequest.train_date=2013-02-02&orderRequest.from_station_telecode=SZQ&orderRequest.to_station_telecode=AEQ&orderRequest.train_no=&trainPassType=QB&trainClass=QB%23D%23Z%23T%23K%23QT%23&includeStudent=00&seatTypeAndNum=&orderRequest.start_time_str=00%3A00--24%3A00");
        client.executeMethod(get);
        String[] msg =  get.getResponseBodyAsString().split(",");
         if(msg.length > 0 && isLogin == false ){
        	 Integer num = Integer.parseInt(msg[0]);
        	 if(num >= 0) isLogin = true;
         }
        get.releaseConnection();
		return isLogin;
	}

	// 获取图片
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#newImage()
	 */
	@Override
	public byte[] newImage() {
		GetMethod get = new GetMethod("/otsweb/passCodeAction.do?rand=sjrand"+ Math.random());
		try {
			client.executeMethod(get);
			return get.getResponseBody();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//登录
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean login(String loginname,String password,String code) throws HttpException, IOException{
        
        //模拟登录页面/otsweb/loginAction.do?method=login
        PostMethod post = new PostMethod("/otsweb/loginAction.do?method=login");
        NameValuePair name = new NameValuePair("loginUser.user_name",loginname);     
        NameValuePair pass = new NameValuePair("user.password", password);    
        NameValuePair randCode = new NameValuePair("randCode", code);  
        NameValuePair loginRand = new NameValuePair("loginRand", loginRandGet());  
        NameValuePair refundLogin = new NameValuePair("refundLogin", "");
        NameValuePair refundFlag = new NameValuePair("refundFlag", "Y");
        
        NameValuePair nameErrorFocus = new NameValuePair("nameErrorFocus", "");
        NameValuePair passwordErrorFocus = new NameValuePair("passwordErrorFocus", "");
        NameValuePair randErrorFocus = new NameValuePair("randErrorFocus", "");
        
        post.setRequestBody(new NameValuePair[]{name,pass,randCode,loginRand,refundLogin,refundFlag,nameErrorFocus,passwordErrorFocus,randErrorFocus});
        int status = client.executeMethod(post);
        System.out.println(status);
        post.releaseConnection();  
        
        //查看cookie信息
        CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
          Cookie[] cookies = cookiespec.match(LOGON_SITE, LOGON_PORT, "/", false, client.getState().getCookies());
           if (cookies.length == 0) {
               System.out.println("None");    
           } else {
               for (int i = 0; i < cookies.length; i++) {
                  System.out.println(cookies[i].toString());    
               }
          }
		return vailed();
	}
	
	//获取loginRandGet
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#loginRandGet()
	 */
	@Override
	public String loginRandGet(){
		//获取登录Rand
        GetMethod loginRandGet = new GetMethod("/otsweb/loginAction.do?method=loginAysnSuggest");
        try {
			client.executeMethod(loginRandGet);
	        String strsing = loginRandGet.getResponseBodyAsString();
	        System.out.println(strsing.subSequence(14, 17));
	        strsing =strsing.subSequence(14, 17).toString();
	        return strsing;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	
	//登录
		/* (non-Javadoc)
		 * @see org.love320.go12306.services.IClientHttp#loginGet(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public boolean loginGet(String loginname,String password,String code) throws HttpException, IOException{
			
	        
	        //模拟登录页面/otsweb/loginAction.do?method=login
			String url = "/otsweb/loginAction.do?method=login";
			url += "&amp;loginUser.user_name="+loginname;
			url += "&amp;user.password="+password;
			url += "&amp;randCode="+code;
			url += "&amp;loginRand="+loginRandGet();
			url += "&amp;refundLogin=";
			url += "&amp;refundFlag=Y";
			url += "&amp;nameErrorFocus=";
			url += "&amp;passwordErrorFocus=";
			url += "&amp;randErrorFocus=";
			GetMethod get = new GetMethod(url);
			client.executeMethod(get);
			String msg = get.getResponseBodyAsString();
			System.out.println(msg);
	        
	        //查看cookie信息
	        CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
	          Cookie[] cookies = cookiespec.match(LOGON_SITE, LOGON_PORT, "/", false, client.getState().getCookies());
	           if (cookies.length == 0) {
	               System.out.println("None");    
	           } else {
	               for (int i = 0; i < cookies.length; i++) {
	                  System.out.println(cookies[i].toString());    
	               }
	          }
			return vailed();
		}
	
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#urlMsg(java.lang.String)
	 */
	@Override
	public String urlMsg(String url){
		String msg = null;
		GetMethod get = new GetMethod(url);
        try {
			client.executeMethod(get);
			msg = get.getResponseBodyAsString();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return msg;
	}

}
