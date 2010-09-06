<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <script type="text/javascript" src="../js/jquery-1.2.6.js"></script>
  <script type="text/javascript" src="../js/alias-tablesorter.js"></script>
  <script type="text/javascript" src="../js/home.js"></script>
  <link href="../css/home.css" rel="stylesheet" type="text/css">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Login</title>
</head>

<body bgcolor="#D0DEE9">
  
  <a href="home.html">${u:getText('errorpage.message.homepage')}</a>
  <hr>
  
  <center>
    
    <div class="loginDiv">
    <h1>欢迎您的登陆</h1>
      <form action="login.do" method="post" id="loginForm">
        <c:if test="${not empty param.eventPath}">
          <c:set var="eventPath" value="${param.eventPath}" />
        </c:if>
        
        <input type="hidden" name="eventPath" id="eventPath" value="${eventPath}">
                
        ${u:getText('ubank.home.login.username.ipt')}:<input class="username_clz" type="text" name="userName" maxlength="50" id="userName"><br>
        ${u:getText('ubank.home.login.password.ipt')}:<input class="password_clz" type="password" name="password" maxlength="50" id="password"><br>
        <input class="login_btn" type="button" onclick="return login('${u:getText('ubank.login.must.enter.username')}', '${u:getText('ubank.login.must.enter.password')}')"  id="loginBtn" value=${u:getText('ubank.home.login.login.btn')} />
        <input class="register_btn" type="button" onclick="goPage('register.jsp')" value=${u:getText('ubank.home.login.register.btn')} />
     </form>                      
    </div>
    
    <c:if test="${not empty error_msg}">
      <span title="${error_msg}"><font color="red">${error_msg}</font></span>
    </c:if>
  
  </center>
</body>

</html>