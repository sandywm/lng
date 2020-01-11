package com.lng.tools;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sysconfig")
public class ContantsProperties {

	String driver;//mysql驱动
	String url;//mysql地址
	String username;//账号
	String password;//密码
	String weburl;//前台项目地址
	String wxUrl;//微信授权地址
	String grantType;//微信授权类型
	String appId;//微信APPID
	String secretKey;//微信密钥
	String defaultNewsImg;//新闻默认图片
	
	public ContantsProperties() {
		
	}
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getWeburl() {
		return weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

	public String getWxUrl() {
		return wxUrl;
	}

	public void setWxUrl(String wxUrl) {
		this.wxUrl = wxUrl;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getDefaultNewsImg() {
		return defaultNewsImg;
	}

	public void setDefaultNewsImg(String defaultNewsImg) {
		this.defaultNewsImg = defaultNewsImg;
	}
	
	
}
