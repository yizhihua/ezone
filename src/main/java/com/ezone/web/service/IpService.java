package com.ezone.web.service;

import java.net.InetAddress;
import java.net.URI;

import javax.annotation.Resource;

import org.coody.framework.context.annotation.CacheWrite;
import org.coody.framework.context.base.BaseLogger;
import org.coody.framework.core.jdbc.JdbcHandle;
import org.coody.framework.util.PrintException;
import org.springframework.stereotype.Service;

import com.ezone.web.constant.CacheFinal;

@Service
public class IpService {
	
	@Resource
	JdbcHandle jdbcHandle;
	
	private static final BaseLogger logger = BaseLogger.getLoggerPro(IpService.class);

	@CacheWrite(key=CacheFinal.SHELL_IP_CACHE ,validTime=60*60*24*30,fields="url")
	public  String getIp(String url){
		try {
			URI uri = new URI(url);
			String domain=uri.getHost();
			InetAddress address = InetAddress.getByName(domain);
			return address.getHostAddress().toString();
		} catch (Exception e) {
			PrintException.printException(logger, e);
		}
		return null;
	}
}
