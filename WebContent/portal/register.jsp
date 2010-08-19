<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
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
<form action="${contextPath}/portal/register.do" method="post" id="userFomr" name="userFomr" >
<table  width="380px" height="220px">
	<tbody>
		<tr>
			<th>帐 号：</th>
			<td align="left"><input type="text" id="userName" name="userName"/></td>
			<td>
			<c:if test="${not empty userName_error_msg }">
				${userName_error_msg}
			</c:if>
			</td>
		</tr>
		
		<tr>
			<th>密 码：</th>
			<td align="left"><input type="password" id="password" name="password"/></td>
			<td><c:if test="${not empty pass_error_msg }">
				${pass_error_msg}
			</c:if></td>
		</tr>
		
		<tr>
			<th>重复密码：</th>
			<td align="left"><input type="password" id="repassword" name="repassword"/></td>
			<td><c:if test="${not empty repass_error_msg }">
				${repass_error_msg}
			</c:if></td>
		</tr>
		
		<tr>
			<th>验 证 码：</th>
			<td align="left">
			<input  id="captchaCode" name="captchaCode"  title="输入验证码"/> 
			<img id="validateImg" name="validateImg" src="${contextPath}/jcaptcha"  style="display: block; margin-bottom: 5px; margin-top: 5px;height: 50px;width: 150px;">
			<a href="###" onclick="validateImg.src='${contextPath}/jcaptcha?now='+ new Date().getTime()" >看不清</a>
			</td>
			<td>
			<c:if test="${not empty captcha_error_msg }">
				${captcha_error_msg}
			</c:if>
			</td>
		</tr>
		<tr>
			<th colspan="2" align="left">
			<input id="regsubmit" type="submit" name="regsubmit" value="注册"/></th>
		</tr>
	</tbody>
</table>
</form>
</div>
</body>
</html>