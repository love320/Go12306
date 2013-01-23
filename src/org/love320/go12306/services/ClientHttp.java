package org.love320.go12306.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class ClientHttp {

	private static HttpClient client = new DefaultHttpClient();// 浏览器
	private static boolean isLogin = false;// 登录
	
	ClientHttp(){
		//client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
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
	public boolean vailed() throws ClientProtocolException, IOException   {
		String url = "http://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=queryLeftTicket&orderRequest.train_date=2013-02-02&orderRequest.from_station_telecode=SZQ&orderRequest.to_station_telecode=AEQ&orderRequest.train_no=&trainPassType=QB&trainClass=QB%23D%23Z%23T%23K%23QT%23&includeStudent=00&seatTypeAndNum=&orderRequest.start_time_str=00%3A00--24%3A00";
        String[] msg =  urlMsg(url).split(",");
         if(msg.length > 0 && isLogin == false ){
        	 try {
        		 Integer num = Integer.parseInt(msg[0]);
            	 if(num >= 0) isLogin = true;
			} catch (Exception e) {
				isLogin = false;
			}
         }
		return isLogin;
	}

	// 获取图片
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#newImage()
	 */
	public byte[] newImage() throws ClientProtocolException, IOException {
		String url = "http://dynamic.12306.cn/otsweb/passCodeAction.do?rand=sjrand"+ Math.random();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response =client.execute(httpGet);
		InputStream is = response.getEntity().getContent();
		byte[] contentBytes = IOUtils.toByteArray(is);
		return contentBytes;
	}
	
	//登录
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean login(String loginname,String password,String code) throws ClientProtocolException, IOException {
		
	    //模拟登录页面/otsweb/loginAction.do?method=login
		 HttpPost post = new HttpPost("http://dynamic.12306.cn/otsweb/loginAction.do?method=login");
		 List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
		 nvps.add(new BasicNameValuePair("loginUser.user_name",loginname));
		 nvps.add(new BasicNameValuePair("user.password", password));
		 nvps.add(new BasicNameValuePair("randCode", code));
		 nvps.add(new BasicNameValuePair("loginRand", loginRandGet()));
		 nvps.add(new BasicNameValuePair("refundLogin", ""));
		 nvps.add(new BasicNameValuePair("refundFlag", "Y"));
		 nvps.add(new BasicNameValuePair("nameErrorFocus", ""));
		 nvps.add(new BasicNameValuePair("passwordErrorFocus", ""));
		 nvps.add(new BasicNameValuePair("randErrorFocus", ""));
		 post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		 
		 HttpResponse response = client.execute(post);  
         String entity = EntityUtils.toString(response.getEntity()); 
         //System.out.println(entity);
         
		return vailed();
	}
	
	//获取loginRandGet
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#loginRandGet()
	 */
	public String loginRandGet(){
		//获取登录Rand
        	String url = "http://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest";
	        String strsing =  urlMsg(url);
	        System.out.println(strsing.subSequence(14, 17));
	        strsing =strsing.subSequence(14, 17).toString();
	        return strsing;
	}
	
	public String urlMsg(String url){
		
		String entity = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "text/plain, */*");
			httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httpGet.addHeader("Referer", "http://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=init");
			httpGet.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; QQDownload 734; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
			httpGet.addHeader("x-requested-with", "XMLHttpRequest");
			HttpResponse response =client.execute(httpGet);
		    entity = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return entity;
	}
	
	//远程发邮件 
	public int sendMail(String toMail,String title,String content){
		title = URLEncoder.encode(title);
		if(content.length() > 2000) content = content.subSequence(0, 2000).toString()+"...";
		content = URLEncoder.encode(content);
		String[] mails = toMail.split(";");
		for(String email : mails ){
			String url = "http://1000.love320.sinaapp.com/app/sendmail/send12306l.php?email="+email+"&title="+title+"&content="+content;
			urlMsg(url);
		}
		return 1;
	}

}
