<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglib/taglibs.jinc"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UBank --- 注册</title>
<link href="../css/home.css" rel="stylesheet" type="text/css"></link>
</head>
<body>

<div class="mainDiv">
<div class="topDiv">
<h2>UBank</h2>
</div>
<div class="main_Div">
<div align="right"><a href="###">登陆</a> | <a href="###">注册</a></div>
<div style="text-align: center;">
<h1 style="color: #555;">注册</h1>
</div>
<hr width="380px" align="center" />
<div align="center"><c:if test="${not empty register_msg }">
				${register_msg}
			</c:if>
<form action="${contextPath}/portal/register.do" method="post"
	id="userFomr" name="userFomr" onsubmit="return checkUserForm();">
<table width="380px" height="220px">
	<tbody>
		<tr>
			<th>帐 号：</th>
			<td align="left"><input type="text" id="userName"
				name="userName" /></td>
			<td><c:if test="${not empty userName_error_msg }">
				${userName_error_msg}
			</c:if></td>
		</tr>

		<tr>
			<th>密 码：</th>
			<td align="left"><input type="password" id="password"
				name="password" /></td>
			<td><c:if test="${not empty pass_error_msg }">
				${pass_error_msg}
			</c:if></td>
		</tr>

		<tr>
			<th>重复密码：</th>
			<td align="left"><input type="password" id="repassword"
				name="repassword" /></td>
			<td><c:if test="${not empty repass_error_msg }">
				${repass_error_msg}
			</c:if></td>
		</tr>

		<tr>
			<th>验 证 码：</th>
			<td align="left"><input id="captchaCode" name="captchaCode"
				title="输入验证码" /> <img id="validateImg" name="validateImg"
				src="${contextPath}/jcaptcha"
				style="display: block; margin-bottom: 5px; margin-top: 5px; height: 50px; width: 150px;">
			<a href="javascript:;"
				onclick="validateImg.src='${contextPath}/jcaptcha?now='+ new Date().getTime()">看不清</a>
			</td>
			<td><c:if test="${not empty captcha_error_msg }">
				${captcha_error_msg}
			</c:if></td>
		</tr>
		<tr>
			<th colspan="2" align="left"><input id="regsubmit" type="submit"
				name="regsubmit" value="注册" /></th>
		</tr>
	</tbody>
</table>
</form>
</div>

<script type="text/javascript">
	function checkUserForm() {
		var userName = document.getElementById("userName").value;
		if (userName == '' || userName.length == 0) {
			alert("请输入用户名！");
			return false;
		} else if (userName.length > 20) {
			alert("用户名过长！");
			return false;
		}
		var password = document.getElementById("password").value;
		var passpord2 = document.getElementById("repassword").value;
		if (password.replace(/ /g, "") == "") {
			alert("请输入密码");
			return false;
		}
		if (password.length < 6) {
			alert("密码长度不能小于6");
			return false;
		}
		if (password2.replace(/ /g, "") == "") {
			alert("请输入密码确认");
			return false;
		}
		if (password != password2) {
			alert("两次输入的密码不一致");
			return false;
		}
		var captchaCode = document.getElementById("captchaCode").value;
		if (captchaCode == '' || captchaCode.length == 0) {
			alert("验证码不能为空！");
			return false;
		}
	}
</script></div>
</div>
<div class="footDiv"></div>
</div>
</body>
</html>