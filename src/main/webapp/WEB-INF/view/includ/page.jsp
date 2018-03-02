<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<link href="${basePath }css/page.css" rel="stylesheet">
<div class="page">
	<div id="kkpager" class="nowrap">
		<div>
			<span class="pageBtnWrap"> <a href="javascript:aheadPage()"
				${(journalPager.currPage<=1)?"class='disabled'":""}>上一页</a> <span
				class="curr">${journalPager.currPage }/${journalPager.totalPage }页</span>
				<a href="javascript:nextPage()"
				${(journalPager.currPage>=journalPager.totalPage)?"class='disabled'":""}>下一页</a>
				 <input type="text" id="jumpPager"
				class="am-form-field am-field-valid"
				onkeydown="javascript:if(event.keyCode==13){jumpPage();}"
				value="${(journalPager.currPage+2>=journalPager.totalPage)?journalPager.totalPage:journalPager.currPage+2 }"
				style="width: 36px"> <input type="button" value="跳转"
				onclick="jumpPage()" style="cursor: pointer;" /></span> <input name="page"
				id="currPage" value="${journalPager.currPage }" type="hidden">
			<script>
			function indexPage() {
		$("#currPage").val('1');
		callPager();
	}
	function callPager(){
		var page=$("#currPage").val();
		var type="${type}";
		var url="${basePath}journal_"+page+".${defSuffix}";
		if(type!=""){
			url="${basePath}journal_"+page+"_${type}"+".${defSuffix}";
		}
		location.href=url;
		return false;
	} 
	function nextPage() {
		var curr = ${journalPager.currPage};
		var total = ${journalPager.totalPage};
		if (curr >= total) {
			return false;
		}
		var next = ${journalPager.currPage}+1;
		$("#currPage").val(next);
		callPager();
	}
	function lastPage() {
		$("#currPage").val('${pager.totalPage}');
		callPager();
	}
	function aheadPage() {
		var curr = ${journalPager.currPage};
		if (curr <= 1) {
			return false;
		}
		var page = ${journalPager.currPage}-1;
		$("#currPage").val(page);
		callPager();
	}
	function jumpPage() {
		var jump = $("#jumpPager").val();
		var totalPage = ${journalPager.totalPage};
		if (jump > totalPage || jump < 1) {
			return false;
		}
		$("#currPage").val(jump);
		callPager();
	}
			</script>
		</div>
		<div style="clear:both;"></div>
	</div>
</div>