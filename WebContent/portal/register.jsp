<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ubank-注册</title>
</head>
<body>
<div align="right"><a href="###">登陆</a> | <a href="###">注册</a></div>
<div style="margin-left: 510px">
<h1 style="color: #00FF00;">注册</h1>
</div>
<hr width="380px" align="center" />
<div align="center">
<table  width="380px" height="220px">
	<tbody>
		<tr>
			<th>帐 号：</th>
			<td align="left"><input type="text" id="userName" name="userName"/></td>
		</tr>
		
		<tr>
			<th>密 码：</th>
			<td align="left"><input type="password" id="password" name="password"/></td>
		</tr>
		
		<tr>
			<th>重复密码：</th>
			<td align="left"><input type="password" id="repassword" name="repassword"/></td>
		</tr>
		
		<tr>
			<th>验 证 码：</th>
			<td align="left">
			<input  id="captchaCode" title="输入验证码" maxlength="4" name="captchaCode" /> 
			<img src="<%=basePath%>/jcaptcha" ><br/> 看不清，换一张
				
			</td>
		</tr>
		
		<tr>
			<th colspan="2" align="left"><input id="regsubmit" type="submit"
				name="regsubmit" value="注册"/></th>
		</tr>
	</tbody>
</table>
</div>
</body>
</html>