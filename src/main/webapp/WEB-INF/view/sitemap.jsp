<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %><%
String suniffx=(String)request.getAttribute("suffix");
if(suniffx.equalsIgnoreCase("XML")){
	%>${siteMap.xml}<%
}
if(suniffx.equalsIgnoreCase("HTML")){
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>站点地图 - ${setting.siteName }</title>
<meta name="keywords" content="${setting.keywords }" />
<meta name="description" content="${setting.description }" />
<meta name="generator" content="Baidu SiteMap Generator" />
<meta name="author" content="Liucheng.Name" />
<meta name="copyright" content="Liucheng.Name" />
<style type="text/css">
	body {font-family: Verdana;FONT-SIZE: 12px;MARGIN: 0;color: #000000;background: #ffffff;}
	img {border:0;}
	li {margin-top: 8px;}
	.page {padding: 4px; border-top: 1px #EEEEEE solid}
	.author {background-color:#EEEEFF; padding: 6px; border-top: 1px #ddddee solid}
	#nav, #content, #footer {padding: 8px; border: 1px solid #EEEEEE; clear: both; width: 95%; margin: auto; margin-top: 10px;}
</style>
</head>
<body vlink="#333333" link="#333333">
<h2 style="text-align: center; margin-top: 20px">站点地图 - ${setting.siteName } </h2>
<center>
</center>
<div id="nav"><a href="${siteMap.domain }"><strong> ${setting.siteName }</strong></a> &raquo; <a href="${basePath }/sitemap.html">站点地图</a></div>
<div id="content">
  <h3>最新文章</h3>
  <ul>
  ${siteMap.html}  
  </ul>
</div>
<div id="content">
  <h3>文章分类</h3>
  <ul>
  <c:forEach items="${types}" var="list"
				varStatus="index">
	  <c:forEach items="${list.childs}" var="type"
					varStatus="index">
	    <li><a href="${basePath }journal_1_${type.id }.html" target="_blank">${list.typeName}>>${type.typeName }</a></li>
	  </c:forEach>
  </c:forEach>
  </ul>
</div>
</body>
</html>
	
	<%
	return;
}
	response.setStatus(404);
%>