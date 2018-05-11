package com.ezone.web.service;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.CacheWrite;
import org.coody.framework.context.base.BaseLogger;
import org.coody.framework.context.entity.HttpEntity;
import org.coody.framework.core.jdbc.JdbcHandle;
import org.coody.framework.util.HttpHandle;
import org.springframework.stereotype.Service;

import com.ezone.web.constant.CacheFinal;
import com.ezone.web.domain.ShellInfo;


@Service
public class ShellService  {
	
	
	BaseLogger logger=BaseLogger.getLogger(this.getClass());
	
	@Resource
	JdbcHandle jdbcHandle;

	@CacheWrite(key=CacheFinal.SHELL_CACHE ,time=60)
	public  List<ShellInfo> loadShells(){
		return jdbcHandle.findBean(ShellInfo.class);
	}
	@CacheWrite(key=CacheFinal.SHELL_IP_CACHE ,time=60*60*24,fields="url")
	public  String getShellIp(String url){
		try {
			URI uri = new URI(url);
			String domain=uri.getHost();
			InetAddress address = InetAddress.getByName(domain);
			return address.getHostAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	@CacheWrite(key=CacheFinal.SHELL_HTML_CACHE ,time=3)
	public HttpEntity getShellHtml(String url,String postData,String cookie){
		logger.info("请求地址:"+url);
		logger.info("请求Cookie:"+cookie);
		HttpHandle http=new HttpHandle();
		http.config.setEncode("GBK");
		http.config.setCookie(cookie);
		HttpEntity entity=http.Post(url,postData);
		if(entity==null){
			return null;
		}
		return entity;
	}
	public static void main(String[] args) throws URISyntaxException {
		
	}
}
