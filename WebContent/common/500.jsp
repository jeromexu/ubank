<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${contextPath}/css/home.css" rel="stylesheet" type="text/css">
<title>500 Error</title>
</head>

<body bgcolor="#D0DEE9">

  <div class="errorDiv">
     <span>${u:getText('errorpage.message.500')}</span>
     <a href="${contextPath}/index.html">${u:getText('errorpage.message.homepage')}</a>
  </div>

</body>
</html>