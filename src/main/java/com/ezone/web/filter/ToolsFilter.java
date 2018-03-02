package com.ezone.web.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.coody.framework.util.PropertUtil;
import org.coody.framework.util.RequestUtil;
import org.coody.framework.util.SpringContextHelper;
import org.coody.framework.util.StringUtil;

import com.ezone.web.domain.ToolsInfo;
import com.ezone.web.service.ToolsService;

public class ToolsFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		ToolsService toolsService=SpringContextHelper.getBean(ToolsService.class);
		List<ToolsInfo> tools=toolsService.loadToolsAsFormat();
		if(StringUtil.isNullOrEmpty(tools)){
			arg2.doFilter(arg0, arg1);
			return;
		}
		
		Map<String, ToolsInfo> toolsMap=(Map<String, ToolsInfo>) PropertUtil.listToMap(tools, "url");
		HttpServletRequest request = (HttpServletRequest) arg0;
		String url=getRequestURI(request);
		if(!toolsMap.containsKey(url)){
			arg2.doFilter(arg0, arg1);
			return;
		}
		ToolsInfo util=toolsMap.get(url);
		request.setAttribute("utils", util);
		request.getRequestDispatcher(util.getFilePath()).forward(arg0, arg1);
	}

	private String getRequestURI(HttpServletRequest request){
		String url=RequestUtil.getRequestUri(request);
		String suffix=RequestUtil.getURLSuffix(request);
		if(!StringUtil.isNullOrEmpty(suffix)){
			url=url.replace("."+suffix, "");
		}
		while(url.contains("//")){
			url=url.replace("//", "/");
		}
		return url;
	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
