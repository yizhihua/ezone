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
<div class="bloglist" id="listJournal">
	<h2>
		<p>
			<span>工具</span>列表
		</p>
	</h2>
	<div id="bloglist1">
		<c:if test="${!empty tools }">
			<c:forEach items="${tools }" var="tool" varStatus="index">
				<div class="blogs">
					<h3>
						<a href="${basePath }${tool.url}.${defSuffix}">${tool.title }
						</a>
					</h3>
					<figure>
						<img src="${basePath}${tool.logo }"
							onerror="this.src='${basePath}/assets/images/default_journal_logo.jpg'" style="width: 172px;height: 114px">
					</figure>
					<ul>
						<p class="articleP">${tool.remark }</p>
						<a href="${basePath }${tool.url}.${defSuffix}" class="readmore" style="background:#457B6A">立即使用&gt;&gt;</a>
					</ul>
					<p class="autor" style="height:5px">
					</p>
					<div class="dateview">
						${tool.title }
					</div>
				</div>

			</c:forEach>
		</c:if>
	</div>
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