<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link rel="shortcut icon" href="../favicon.ico">
  <script type="text/javascript" src="../js/jquery-1.2.6.js"></script>
  <script type="text/javascript" src="../js/alias-tablesorter.js"></script>
  <script type="text/javascript" src="../js/home.js"></script>
  <link href="../css/home.css" rel="stylesheet" type="text/css">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>${u:getText('ubank.home.message.title')}</title>
</head>

<body bgcolor="#D0DEE9">
  
  <a href="home.html">${u:getText('errorpage.message.homepage')}</a>
  <hr>
  
  <center>
    
    <div class="loginDiv">
    
    <h1>${u:getText('welcome.login') }</h1>
    
    <c:if test="${not empty error_msg}">
      <span title="${error_msg}"><font color="red">${error_msg}</font></span>
    </c:if>
    
      <form action="login.do" method="post" id="loginForm">
        <c:if test="${not empty param.eventPath}">
          <c:set var="eventPath" value="${param.eventPath}" />
        </c:if>
        
        <input type="hidden" name="eventPath" id="eventPath" value="${eventPath}">
                
        <c:set var="userNameLabel" value="${u:getText('ubank.home.login.username.ipt')}" />
        <c:set var="passwordLabel" value="${u:getText('ubank.home.login.password.ipt')}" />
        <c:set var="usernameEmpty" value="${u:getText('ubank.login.must.enter.username')}" />
        <c:set var="passwordEmpty" value="${u:getText('ubank.login.must.enter.password')}" />
        <c:set var="loginBtnText" value="${u:getText('ubank.home.login.login.btn')}" />
        <c:set var="registerBtnText" value="${u:getText('ubank.home.login.register.btn')}" />
                
        ${userNameLabel}:<input class="username_clz" type="text" name="userName" maxlength="50" id="userName"><br>
        ${passwordLabel}:<input class="password_clz" type="password" name="password" maxlength="50" id="password"><br>
        <input class="login_btn" type="button" onclick="return login('${usernameEmpty}', '${passwordEmpty}')"  id="loginBtn" value=${loginBtnText} />
        <input class="register_btn" type="button" onclick="goPage('register.jsp')" value=${registerBtnText} />
     </form>                      
    </div>
    
  </center>
</body>

</html>