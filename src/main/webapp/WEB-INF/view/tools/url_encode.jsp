<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<jsp:include page="header.jsp" />
</head>
<body>
	<jsp:include page="js.jsp" />
	<div class="am-g">
		<div class="am-u-md-12">
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd">${utils.title }</div>
				<div class="am-panel-bd">
					<div class="am-form">
						<fieldset>
							<div class="am-form-group am-form-success">
								<textarea id="currId" class="am-field-valid"
									style="height:13.5rem"></textarea>
							</div>
							<p>
								<button type="button" onclick="encode()"
									class="am-btn am-btn-danger am-round">
									<i class="am-icon-spinner am-icon-spin"></i> 编码
								</button>
								<button type="button" onclick="decode()"
									class="am-btn am-btn-success am-round">
									<i class="am-icon-spinner am-icon-spin"></i> 解码
								</button>
							</p>
							<div class="am-form-group">
								<textarea id="targeId" style="height:13.5rem"></textarea>
							</div>
						</fieldset>
					</div>
				</div>
			</div>
			<jsp:include page="foot.jsp" />
		</div>
	</div>
</body>
<script type='text/javascript'>
	function encode() {
		var htm = escape($('#currId').val())
		$('#targeId').val(htm);
	}
	function decode() {
		var htm = unescape($('#currId').val())
		$('#targeId').val(htm);
	}
</script>
</html>
