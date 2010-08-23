<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>

<table cellpadding="0" cellspacing="0" border="1">
  <tr align="center">
    <td width="260">文件名称</td>
    <td width="100">大小</td>
    <td width="150">所属人</td>
    <td width="200">修改日期</td>
    <td width="100">下载</td>             
  </tr>
  
  <c:choose>
    <c:when test="${not empty filePager.pageRecords}">
      <c:forEach var="file" items="${filePager.pageRecords}">
          <tr align="center">
            <td>${file.name }</td>
            <td>${file.size }KB</td>
            <td>${file.user.username }</td>
            <td>${file.modifyDate }</td>
            <td><a href="#">Download</a></td>
          </tr>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <tr align="center">
        <td colspan="5">${u:getMessage("message.sharefile.search.result.empty")}</td>
      </tr>
    </c:otherwise>
  </c:choose>

</table>