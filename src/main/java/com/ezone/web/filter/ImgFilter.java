package com.ezone.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coody.framework.util.FileUtils;
import org.coody.framework.util.GZIPUtils;
import org.coody.framework.util.RequestUtil;
import org.coody.framework.util.StringUtil;

import com.ezone.web.queue.ImageQueue;

public class ImgFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;
		res.setContentType("image/jpeg");
		String uri=RequestUtil.getRequestUri(req);
		if(StringUtil.isNullOrEmpty(uri)){
			req.getRequestDispatcher("/WEB-INF/view/404.jsp").forward(req, res);
			return;
		}
		while(uri.contains("../")){
			uri=uri.replace("../", "/");
		}
		String path=System.getProperty("ezone.root")+uri;
		byte []data=FileUtils.readFileByte(path);
		if(StringUtil.isNullOrEmpty(data)){
			req.getRequestDispatcher("/WEB-INF/view/404.jsp").forward(req, res);
			ImageQueue.imageQueue.offer(uri.replace("upload/", ""));
			return;
		}
		data=GZIPUtils.uncompress(data);
		response.getOutputStream().write(data);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
	}

}
