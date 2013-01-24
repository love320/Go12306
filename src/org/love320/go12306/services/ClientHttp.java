package org.love320.go12306.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class ClientHttp {

	//private static HttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager());;// 浏览器
	private static HttpClient client = new DefaultHttpClient();;// 浏览器
	private static boolean isLogin = false;// 登录
	
	ClientHttp(){
		//client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
		client = WebClientDevWrapper.wrapClient(client);
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
		String url = "https://dynamic.12306.cn/otsweb/passengerAction.do?method=getPagePassengerAll&pageIndex=0&pageSize=10";
		String data = urlMsg(url);
		try {
			Gson gson = new Gson();
			Map map = gson.fromJson(data, Map.class);
			isLogin = true;
			System.out.println(data);
		} catch (Exception e) {
			isLogin = false;
			client = new DefaultHttpClient();// 浏览器
		}
		
		//判断 -10 
        /*String[] msg =  data.split(",");
         if(msg.length > 0 && isLogin == false ){
        	 try {
        		 Integer num = Integer.parseInt(msg[0]);
            	 if(num >= 0) isLogin = true;
			} catch (Exception e) {
				isLogin = false;
			}
         }*/
		return isLogin;
	}

	// 获取图片
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#newImage()
	 */
	public byte[] newImage() throws ClientProtocolException, IOException {
		String url = "https://dynamic.12306.cn/otsweb/passCodeAction.do?rand=sjrand"+ Math.random();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response =manyHttp(httpGet);
		InputStream is = response.getEntity().getContent();
		byte[] contentBytes = IOUtils.toByteArray(is);
		httpGet.releaseConnection();
		return contentBytes;
	}
	
	//登录
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean login(String loginname,String password,String code) throws ClientProtocolException, IOException {
		
	    //模拟登录页面/otsweb/loginAction.do?method=login
		 HttpPost post = new HttpPost("https://dynamic.12306.cn/otsweb/loginAction.do?method=login");
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
		 
		 HttpResponse response = manyHttp(post);  
         String entity = EntityUtils.toString(response.getEntity()); 
         post.releaseConnection();
		return vailed();
	}
	
	//获取loginRandGet
	/* (non-Javadoc)
	 * @see org.love320.go12306.services.IClientHttp#loginRandGet()
	 */
	public String loginRandGet(){
		//获取登录Rand
        	String url = "https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest";
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
			HttpResponse response =manyHttp(httpGet);
		    entity = EntityUtils.toString(response.getEntity(),"utf-8");
		    httpGet.releaseConnection();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(entity);
        return entity;
	}
	
	public String urlPostMsg(String url,Map map){
		String entity = "";
		try {
			HttpPost post = new HttpPost("https://dynamic.12306.cn/otsweb/loginAction.do?method=login");
		 	List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
		 	
		 	Set<String> key = map.keySet();
	        for (Iterator it = key.iterator(); it.hasNext();) {
	            String s = (String) it.next();
	            nvps.add(new BasicNameValuePair(s,map.get(s).toString()));
	        }
		 	
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = manyHttp(post);  
			
			int status = response.getStatusLine().getStatusCode();
			if(status == 302){
				String reUrl =response.getLastHeader("location").getValue();
				post.releaseConnection();
			    response = redirect(reUrl);
			}
			entity = EntityUtils.toString(response.getEntity()); 
			post.releaseConnection();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return entity;
	}
	
	public HttpResponse sendHttp(HttpGet httpGet){
		HttpResponse response = manyHttp(httpGet);
		httpGet.releaseConnection();
		return redirect(response);
	}
	
	public HttpResponse sendHttp(HttpPost httpPost){
		HttpResponse response = manyHttp(httpPost);
		httpPost.releaseConnection();
		return redirect(response);
	}
	
	//多线程使用 
	public HttpResponse manyHttp(HttpUriRequest  httpUriRequest){
		try {
			synchronized(client){
				HttpResponse response = client.execute(httpUriRequest);
				return response;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	}
	
	public HttpResponse redirect(HttpResponse response ){
		int status = response.getStatusLine().getStatusCode();
		if(status == 302){
			String reUrl =response.getLastHeader("location").getValue();
		    response = redirect(reUrl);
		}
		return response;
	}
	
	//重写向
	public HttpResponse redirect(String url){

			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "text/plain, */*");
			httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httpGet.addHeader("Referer", "http://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=init");
			httpGet.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; QQDownload 734; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
			//httpGet.addHeader("x-requested-with", "XMLHttpRequest");
			HttpResponse response =manyHttp(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if(status == 302){
				String reUrl =response.getLastHeader("location").getValue();
				httpGet.releaseConnection();
			    response = redirect(reUrl);
			}
			return response;
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
	
	public static class WebClientDevWrapper {  
        public static HttpClient wrapClient(HttpClient base) {  
            try {  
                SSLContext ctx = SSLContext.getInstance("TLS");  
                X509TrustManager tm = new X509TrustManager() {  
                    @Override  
                    public X509Certificate[] getAcceptedIssuers() {  
                        return null;  
                    }  
  
                    @Override  
                    public void checkClientTrusted(  
                            java.security.cert.X509Certificate[] chain,  
                            String authType)  
                            throws java.security.cert.CertificateException {  
                          
                    }  
  
                    @Override  
                    public void checkServerTrusted(  
                            java.security.cert.X509Certificate[] chain,  
                            String authType)  
                            throws java.security.cert.CertificateException {  
                          
                    }  
                };  
                ctx.init(null, new X509TrustManager[] { tm }, null);  
                SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
                ClientConnectionManager ccm = base.getConnectionManager();  
                SchemeRegistry sr = ccm.getSchemeRegistry();  
                sr.register(new Scheme("https", 443, ssf));  
                return new DefaultHttpClient(ccm, base.getParams());  
            } catch (Exception ex) {  
                ex.printStackTrace();  
                return null;  
            }  
        }
        }  

}
