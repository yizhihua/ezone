<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<div class="tj_news">
	<h2>
		<p class="tj_t0">文章分类</p>
	</h2>
	<p style="height: 10px;"></p>
	<div class="type_list">
		<script>
			function isHidden(typeName) {
				$("." + typeName + "").slideToggle("slow", function() {});
			}
		</script>

		<c:forEach items="${types }" var="type" varStatus="index">
			<div>
				<span style="color:#b0e516;">【</span> <a target="_parent"
					<c:if test="${!empty type.childs }">
			href="javascript:isHidden('articleType${index.index}')"
			</c:if>
					<c:if test="${empty type.childs }">
			href="${basePath }journal_1_${type.id }.${defSuffix}"
			</c:if>
					style="cursor:hand;font-size: 16px;color:white;font-family:黑体;"><B>${type.typeName }(<span
						style="color: red;">${type.count==null?0:type.count }</span>)
				</B></a> <span style="color:#b0e516;">】</span>
			</div>
			<c:forEach items="${type.childs }" var="childType">
				<ul class="articleType${index.index}" style="display: none;">
					<li id="hotjournal" style="line-height:16px;height:16px"><a
						href="${basePath }journal_1_${childType.id }.${defSuffix}">${childType.typeName }
							(<span style="color: red;">${childType.count }</span>)
					</a></li>
				</ul>
			</c:forEach>
		</c:forEach>


	</div>
	<br />
</div>
<style>
.hiddenA {
	width: 95%;
	display: block;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	float: left;
}
.type_list ul{
	margin: 3px 0 3px 20px !important;
}
</style>