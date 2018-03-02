<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>用户登录 - ${setting.siteName }</title>
<meta name="description" content="${setting.description }">
<meta name="keywords" content="${setting.keywords }">
<link rel="stylesheet"
	href="//cdn.bootcss.com/amazeui/2.7.2/css/amazeui.min.css" />
<link rel="stylesheet" href="${basePath }assets/css/other.min.css" />
<script src="//cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<link rel="icon" href="${basePath }assets/img/favicon.png"
	type="image/png">
</head>
<body class="login-container">
	<div class="login-box">
		<div class="logo-img">
			<img src="${basePath }assets/img/logo.png" alt="" style="max-width: 260px;" />
		</div>
		<form action="user/login.${defSuffix }" class="am-form line"
			data-am-validator id="dataform" onsubmit="submitForm();return false">
			<div class="am-form-group line">
				<label for="doc-vld-name-2" class="ico-lable"><i class="am-icon-user"></i></label> <input
					type="text" id="userName" name="userName" minlength="3"
					maxlength="32" placeholder="输入用户名（至少 3 个字符）" required />
			</div>

			<div class="am-form-group line">
				<label for="doc-vld-email-2" class="ico-lable"><i class="am-icon-key"></i></label> <input
					type="password" id="userPwd" name="userPwd" minlength="6"
					maxlength="15" placeholder="输入密码" required />
			</div>

			<c:if test="${needVerCode }">
				<div class="am-form-group line">
					<label for="doc-vld-email-2" class="ico-lable"><i class="am-icon-yelp"></i></label> <input
						type="text" id="userName"  name="verCode" minlength="4" maxlength="4" class="verCode"
						placeholder="输入验证码" required />
						<img src="${basePath }admin/verCode.${defSuffix}" class="verCodeImg" id="verCodeImg" onclick="refVerCode()">
				</div>
			</c:if>
			<button class="am-btn am-btn-secondary loginButton" type="button" onclick="submitForm()">登录</button>
		</form>
	</div>
</body>
<script>
	function refVerCode() {
		var imgSrc = "${basePath }admin/verCode.${defSuffix}?"
				+ Math.round(Math.random() * 1000000);
		setTimeout(function() {
			document.getElementById("verCodeImg").src = imgSrc;
		}, 0);

	}
	function submitForm() {
		$.ajax({
			type : "POST",
			dataType : 'json',
			data : $("#dataform").serialize(),
			url : '${basePath}admin/doLogin.${defSuffix}',
			timeout : 60000,
			success : function(json) {
				if (json.code != 0) {
					localStorage.setItem("userName", "");
					localStorage.setItem("userPwd", "");
					alert(json.msg);
					${needVerCode?'refVerCode();':''}
					return;
				}
				localStorage.setItem("userName", $("#userName").val());
				localStorage.setItem("userPwd", $("#userPwd").val());
				location.href = "${basePath}admin/index.${defSuffix}";
			},
			error : function() {
				alert("系统繁忙");
				${needVerCode?'refVerCode();':''}
			}
		});
	}


	function autoLogin() {
		var userName = localStorage.getItem("userName");
		var userPwd = localStorage.getItem("userPwd");
		$("#userName").val(userName);
		$("#userPwd").val(userPwd);
		if (userName != null && userPwd != null && userPwd != '' && userName != '' && userName != 'undefined' && userPwd != 'undefined') {
			$.ajax({
				type : "POST",
				dataType : 'json',
				data : "userName=" + userName + "&userPwd=" + userPwd,
				url : '${basePath}admin/doLogin.${defSuffix}',
				timeout : 60000,
				success : function(json) {
					if (json.code != 0) {
						return;
					}
					location.href = "${basePath}admin/index.${defSuffix}";
				},
				error : function() {}
			});
		}
	}
	${!needVerCode?'autoLogin();':''}
</script>
<style>
.verCode{
	width: 50%!important;  
}
.verCodeImg{
	height: 2.4rem;
	float: right;
	max-width: 30%;
	margin: 1px 1px 1px 1px;
	cursor:pointer;
}
.logoImg{
	width: 160px;
}
.login-container .login-box .am-form .line {
    overflow: auto;
    border-bottom: 1px #68c6de solid!important;  
    margin-bottom: 20px!important;  
}
.am-icon-user{
	line-height: 1!important;  
}

.am-form input[type=number], .am-form input[type=search], .am-form input[type=text], .am-form input[type=password], .am-form input[type=datetime], .am-form input[type=datetime-local], .am-form input[type=date], .am-form input[type=month], .am-form input[type=time], .am-form input[type=week], .am-form input[type=email], .am-form input[type=url], .am-form input[type=tel], .am-form input[type=color], .am-form select, .am-form textarea, .am-form-field {
    font-size: 1.3rem!important;  
    line-height: 1.0!important;  
}
.ico-lable{
	width: 25px!important;  
}
.ico-lable li{
	width: 25px!important;  
}
</style>
</html>
