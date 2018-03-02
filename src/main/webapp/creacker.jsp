<%@page import="org.coody.framework.context.entity.MsgEntity"%>
<%@page import="org.coody.framework.context.entity.HttpEntity"%>
<%@page import="com.alibaba.fastjson.TypeReference"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="org.coody.framework.util.StringUtil"%>
<%@page import="org.coody.framework.util.HttpHandle"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String mobile=request.getParameter("mobile");
	String invite=request.getParameter("invite");
	if(!StringUtil.hasNull(mobile,invite)){
		Boolean result=LaiTeJiTest.reg(mobile, invite);
		response.setContentType("application/json");
		if(result){
			 response.getWriter().print(JSON.toJSONString(new MsgEntity(0,"添加成功")));
			 return;
		}
		response.getWriter().print(JSON.toJSONString(new MsgEntity(-1,"添加失败")));
			 return;
	}
%>

<%!

public static class LaiTeJiTest {

	static HttpHandle http = new HttpHandle();

	static {
		http.config.setRequestProperty("accept", "application/json");
		http.config.setRequestProperty("content-type", "application/json; charset=utf-8");
	}

	public static void main(String[] args) {
		System.out.println(reg("13852415263", "w808i9"));
	}

	public static boolean reg(String mobile, String inviteCode) {
		inviteCode = inviteCode.trim();
		mobile = mobile.trim();
		String verCode = getVercode(mobile);
		if (StringUtil.isNullOrEmpty(verCode)) {
			return false;
		}
		try {
			String userName = createUserName();
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", userName);
			map.put("password", "123456");
			map.put("password_confirmation", "123456");
			map.put("mobile", mobile);
			map.put("code", verCode);
			map.put("recommend_code", inviteCode);
			String data = JSON.toJSONString(map);
			HttpEntity entity = http.Post("https://www.ltchick.com/api/register", data);
			Map<String, String> resultmap = JSON.parseObject(entity.getHtml(),
					new TypeReference<Map<String, String>>() {
					});
			if (resultmap.get("status_code").equals("200")) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	private static String createUserName() {
		Integer length = StringUtil.getRanDom(6, 12);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int code = StringUtil.getRanDom(97, 122);
			result.append((char) code);
		}
		return result.toString();
	}

	private static String getVercode(String mobile) {
		try {
			HttpEntity entity = http.Post("https://www.ltchick.com/api/sms/code/send/noauth",
					"{\"mobile\": \"" + mobile + "\"}");
			Map<String, String> map = JSON.parseObject(entity.getHtml(), new TypeReference<Map<String, String>>() {
			});
			return map.get("message");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>自助创建莱特鸡账号</title>
<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
  </head>
  
  <body>
  <form action="" method="post" id="dataform" onsubmit="return submitForm()">
   <table>
   	<tr><td>手机号：</td><td><input name="mobile" value=""></td></tr>
   	<tr><td>邀请码：</td><td><input name="invite" value=""></td></tr>
   	<tr><td colspan="2"><input type="submit" name="mobile" value="立即添加"></td></tr>
   </table>
   </form>
  </body>
  <script>
  	function submitForm() {
		$.ajax({
			type : "POST",
			dataType : 'json',
			data : $("#dataform").serialize(),
			url : '',
			timeout : 60000,
			success : function(json) {
				if (json.code != 0) {
					alert(json.msg);
					return;
				}
			},
			error : function() {
				alert("系统繁忙");
				
			}
		});
		return false;
	}
  </script>
</html>
