<%@ page language="java" contentType="text/html; charset=gb2312"  import="java.io.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Ping IP≤‚ ‘“≥√Ê</title>
</head>
<body>
<div align="center">
  <h2>Ping IP≤‚ ‘“≥√Ê</h2>
</div>
<%  
   Runtime  runtime  =  Runtime.getRuntime();  
   Process  process  =null;  
   String  line=null;  
   InputStream  is  =null;  
   InputStreamReader  isr=null;  
   BufferedReader  br  =null;  
   String  ip="dynamic.12306.cn";  //¥˝Pingµƒµÿ÷∑
    try  
   {  
       process  =runtime.exec("ping  "+ip);  
       is  =  process.getInputStream();  
       isr=new  InputStreamReader(is);  
       br  =new  BufferedReader(isr);  
       out.println("<pre>");  
       while(  (line  =  br.readLine())  !=  null  )  
       {  
           out.println(line);  
           out.flush();  
       }  
       out.println("</pre>");  
       is.close();  
       isr.close();  
       br.close();  
   }  
   catch(IOException  e  )  
   {  
       out.println(e);  
       runtime.exit(1);  
   }  
%> 
</body>
</html>