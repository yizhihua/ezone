<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<!doctype html>
<html>
<head>
<jsp:include page="includ/head.jsp" />
<link href="${basePath }css/style.css" rel="stylesheet">
</head>
<body>
	<div class="ibody">
		<jsp:include page="includ/header.jsp" />
		<article>
			<h2 class="about_h">
				您现在的位置是：<a href="${basePath }">首页</a>><a
					href="${basePath }journal_1_${journal.type.id }.${defSuffix }">${journal.type.typeName }</a>
			</h2>
			<div class="index_about">
				<h2 class="c_titile">${journal.title }</h2>
				<p class="box_c">
					<span class="d_time">发布时间：<fmt:formatDate
							value="${journal.time}" pattern="yyyy-MM-dd" /></span><span>浏览(${journal.views })</span>
				</p>
				<ul class="infos">${journal.html }
				</ul>
			</div>
		</article>
		<jsp:include page="includ/aside.jsp" />

		<script src="${basePath }js/silder.js"></script>
		<div class="clear"></div>
		<!-- 清除浮动 -->
	</div>
	<style>
img {
	max-width: 100% !important;
}

.infos {
	word-break: break-all;
	white-space: normal;
}
</style>
</body>
</html>
