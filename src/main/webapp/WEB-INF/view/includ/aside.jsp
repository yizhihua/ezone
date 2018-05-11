<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<aside>
	<div class="avatar">
		<a href="http://user.qzone.qq.com/644556636"><span>关于Coody</span></a>
	</div>
	<div class="topspaceinfo">
		<h1>执子之手，与子偕老</h1>
		<p>于千万人之中，我遇见了我所遇见的人....</p>
	</div>
	<div class="about_c">
		<p>博主：Coody</p>
		<p>职业：包子店铺老板</p>
		<p>籍贯：广东-广州</p>
		<p>邮箱：644556636@qq.com</p>
	</div>
	<jsp:include page="type.jsp" />
	<div class="tj_news">
		<h2>
			<p class="tj_t1">最新文章</p>
		</h2>
		<ul>
			<c:forEach items="${newJournals }" var="journal">
				<li><a href="${basePath }article_${journal.id}.${defSuffix}">${fn:length(journal.title)>26?fn:substring(journal.title, 0, 26):journal.title }</a></li>
			</c:forEach>
		</ul>
		<h2>
			<p class="tj_t2">推荐文章</p>
		</h2>
		<ul>
			<c:forEach items="${hotJournals }" var="journal">
				<li><a href="${basePath }article_${journal.id}.${defSuffix}">${fn:length(journal.title)>26?fn:substring(journal.title, 0, 26):journal.title }</a></li>
			</c:forEach>
		</ul>
	</div>
	<div class="links">
		<h2>
			<p>友情链接</p>
		</h2>
		<ul>
			<li><a href="http://coody.org">imxss</a></li>
			<li><a href="http://www.itxueke.com">IT学客网</a></li>
			<li><a href="http://blog.techauch.com">code life博客</a></li>
		</ul>
	</div>
	<div class="copyright">
		<ul>
			<p>Copyright © 2014-2019</p>
			<p>代码人生</p>
		</ul>
	</div>
</aside>