<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<article>
	<div class="banner">
		<ul class="texts">
			<p>倘若人生是一项编程，我愿用枯燥的代码，谱写人生的点点滴滴;</p>
			<p>倘若人生是一套工序，我愿用笨拙的双手，刻录人生的朝朝暮暮;</p>
			<p>倘若人生只是一场戏，我愿用诚实的面孔，演奏人生的始始终终.</p>
		</ul>
	</div>
	<div class="bloglist">
		<h2>
			<p>
				<span>推荐</span>文章
			</p>
		</h2>
		<c:forEach items="${journalPager.data }" var="journal">
			<div class="blogs">
				<h3>
					<a class="journal_context" href="${basePath }article_${journal.id}.${defSuffix}">${journal.title }</a>
				</h3>
				<figure>
					<img class="journal_logo"
						src="${basePath }images/journal/logo (${journal.logo }).jpg">
				</figure>
				<ul>
					<p class="journal_context">${journal.html }</p>
					<a href="${basePath }article_${journal.id}.${defSuffix}"
						target="_blank" class="readmore">阅读全文&gt;&gt;</a>
				</ul>
				<p class="autor">
					<span>作者：coody</span><span>分类：【<a href="${basePath }journal_1_${journal.type.id }.${defSuffix }">${journal.type.typeName }</a>】
					</span><span>浏览（<a href="#">${journal.views }</a>）
					</span>
				</p>
				<div class="dateview">
					<fmt:formatDate value="${journal.time}" pattern="yyyy-MM-dd" />
				</div>
			</div>
		</c:forEach>
		<c:if test="${!empty journalPager.data }">
			<div class="blogs">
				<jsp:include page="page.jsp" />
			</div>
		</c:if>
		
	</div>
</article>
<style>
.journal_context {
	word-break: break-all;
	white-space: normal;
}

.journal_logo {
	width: 352px;
	height: 112px;
}
</style>