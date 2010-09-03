<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <script type="text/javascript" src="../js/jquery-1.2.6.js"></script>
  <script type="text/javascript" src="../js/alias-tablesorter.js"></script>
  <script type="text/javascript" src="../js/home.js"></script>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>${file.fileName }</title>
</head>

<body>
  <center>
  
    <table width="800" bgcolor="gray" height="50">
      <tr align="center">
        <td align="center">
          <c:if test="${not empty error_msg}">
            <span title="${error_msg}"><font color="red">${error_msg}</font></span>
          </c:if>
        </td>
      </tr>
    </table>
    
    <table>
      <tr>
        <td width="260" class="th_title pointer">${u:getText('ubank.home.thead.filename')}</td>
        <td width="100" class="th_title pointer">${u:getText('ubank.home.thead.filesize')}</td>
        <td width="150" class="th_title pointer">${u:getText('ubank.home.thead.owner')}</td>
        <td width="200" class="th_title pointer">${u:getText('ubank.home.thead.modifydate')}</td>
      </tr>
      
      <tr>
        <td><c:out value="${file.fileName}" escapeXml="true" /></td>
        <td>${file.size }</td>
        <td><c:out value="${file.folder.user.userName}" escapeXml="true" /></td>
        <td><fmt:formatDate value="${file.modifyTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
      </tr>
    </table>
    
    <table width="800" height="50">
      <tr>
        <td>
          <c:set var="isLogin" value="${not empty sessionScope.session_user}" />
          <c:set var="currenthasPoint" value="${req_user.point}" />
          <c:if test="${empty currenthasPoint}">
            <c:set var="currenthasPoint" value="0" />
          </c:if>
          <c:set var="needPoint" value="${u:getValue('download.point')}" />
          <c:set var="params" value="${fn:split(needPoint, ',')}" />
          <c:set var="confirmMsg" value="${u:getTextWithParams('download.msg', params)}" />
          <c:set var="pointNotEnough" value="${u:getText('download.point.notenough')}" />
          <c:set var="mustLogin" value="${u:getText('must.login')}" />
              
          <a onclick="download('${confirmMsg}' , '${pointNotEnough}' , '${mustLogin}' , ${isLogin} , 'download.do?id=${file.fileId}&eventPath=${eventPath}', '${eventPath}')" href="javascript:void(0);">
            <img style="border: medium none;" alt="" src="${contextPath}/imgs/download_icon.png">
          </a>
        </td>
      </tr>
    </table>
    
  </center>
</body>

</html>