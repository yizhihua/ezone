package com.ezone.web.util;

import java.util.Date;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.coody.framework.context.base.BaseLogger;
import org.coody.framework.context.entity.HttpEntity;
import org.coody.framework.util.HttpHandle;
import org.coody.framework.util.PrintException;
import org.coody.framework.util.StringUtil;

public class BaiduHttpUtil {

	public static String cookie="";
	
	private static final BaseLogger logger = BaseLogger.getLoggerPro(BaiduHttpUtil.class);
	
	
	public BaiduHttpUtil(){
		
	}
	
	
	public static HttpEntity initCookie(){
		try {
			String baseURL="http://www.myexception.cn/";
			HttpHandle http=new HttpHandle();
			http.config.setRequestProperty("If-Modified-Since", new Date().toString());
			http.config.setRequestProperty("Cache-Control", "max-age=0");
			http.config.setRequestProperty("Upgrade-Insecure-Requests", "1");
			http.config.setKeepAlive(true);
			HttpEntity entity = http.Get(baseURL);
			String html = entity.getHtml();
			String temp = html.replace(" ", "");
			if(!temp.contains("jschl_vc\"value=\"")){
				cookie="test";
				return null;
			}
			String jschl_vc = StringUtil.textCutCenter(temp, "jschl_vc\"value=\"", "\"");
			String pass = StringUtil.textCutCenter(temp, "pass\"value=\"", "\"");

			String funcCode = StringUtil.textCutCenter(html, "setTimeout(function(){", "f.submit();");

			funcCode = funcCode.replace("a.value", "a");
			funcCode = funcCode.replace("  ", " ");
			String[] tabs = funcCode.split("\n");
			funcCode = tabs[1];
			funcCode += "\r\nt=\"" + baseURL + "\";";
			funcCode += "\r\nr = t.match(/https?:\\/\\//)[0];";
			funcCode += "\r\nt = t.substr(r.length);";
			funcCode += "\r\nt = t.substr(0, t.length - 1);";
			funcCode += tabs[8];
			funcCode += "\r\n return a;";

			funcCode = "function jschl_answer(){\r\n" + funcCode + "\r\n}";

			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("js");
			engine.eval(funcCode);
			Invocable invocable = (Invocable) engine;
			Double jschl_answer = (Double) invocable.invokeFunction("jschl_answer");
			String url=baseURL+"/cdn-cgi/l/chk_jschl?jschl_vc="+jschl_vc+"&pass="+pass+"&jschl_answer="+jschl_answer.intValue();
			http.config.allowRedirects(false);
			Thread.sleep(3800l);
			http.config.setGzip(true);
			entity=http.Get(url);
			cookie=entity.getCookie();
			if(!cookie.contains("cf_clearance")){
				return null;
			}
			return entity;
		} catch (Exception e) {
			PrintException.printException(logger, e);
			return null;
		}
	}
	
	public HttpEntity Get(String url){
		if(StringUtil.isNullOrEmpty(cookie)){
			loadCookie();
		}
		HttpHandle http=new HttpHandle();
		http.config.setRequestProperty("If-Modified-Since", new Date().toString());
		http.config.setRequestProperty("Cache-Control", "max-age=0");
		http.config.setRequestProperty("Upgrade-Insecure-Requests", "1");
		http.config.setKeepAlive(true);
		http.config.setGzip(true);
		http.config.setCookie(cookie);
		HttpEntity entity=http.Get(url);
		if(entity.getCode()!=200){
			loadCookie();
			http.config.setCookie(cookie);
			entity=http.Get(url);
		}
		return entity;
	}
	
	public void loadCookie(){
		cookie=null;
		initCookie();
		while(cookie==null){
			initCookie();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
