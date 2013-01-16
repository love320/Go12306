package org.love320.go12306.services;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

public interface IClientHttp {

	// 获取图片
	public abstract byte[] newImage();
	
	// 验证是否登录
	public boolean vailed()throws HttpException, IOException;

	//登录
	public abstract boolean login(String loginname, String password, String code)
			throws HttpException, IOException;

	//获取loginRandGet
	public abstract String loginRandGet();

	//登录
	public abstract boolean loginGet(String loginname, String password,
			String code) throws HttpException, IOException;

	public abstract String urlMsg(String url);

}