<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>

<table cellpadding="0" cellspacing="0" border="1" id="mainTable">
  <tr align="center">
    <td width="260">${u:getMessage('ubank.home.thead.filename')}</td>
    <td width="100">${u:getMessage('ubank.home.thead.filesize')}</td>
    <td width="150">${u:getMessage('ubank.home.thead.owner')}</td>
    <td width="200">${u:getMessage('ubank.home.thead.modifydate')}</td>
    <td width="100">${u:getMessage('ubank.home.thead.download')}</td>             
  </tr>
  
  <c:choose>
    <c:when test="${not empty filePager.pageRecords}">
      <c:forEach var="file" items="${filePager.pageRecords}">
          <tr align="center">
            <td><c:out value="${file.fileName}" escapeXml="true" /></td>
            <td>${file.size }</td>
            <td><c:out value="${file.folder.user.userName}" escapeXml="true" /></td>
            <td><fmt:formatDate value="${file.modifyTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><a href="download.do?id=${file.fileId }">${u:getMessage('ubank.home.thead.download') }</a></td>
          </tr>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <tr align="center">
        <td colspan="5"><font color="red" size="2">${u:getMessage("message.sharefile.search.result.empty")}</font></td>
      </tr>
    </c:otherwise>
  </c:choose>

</table>

<c:if test="${not empty filePager.pageRecords}">
  <input type="hidden" value="${fileSize}" id="hidFileSize">
  <input type="hidden" value="${publishDate}" id="hidPublishDate">
  <input type="hidden" value="${fileName }" id="hidFileName">

  <jsp:include page="pager.jsp">
    <jsp:param name="pageNum" value="${filePager.currentPage}" />
    <jsp:param name="pageCount" value="${filePager.pageCount}" />
    <jsp:param name="targetUrl" value="search.do" />
  </jsp:include>
</c:if>

<script type="text/javascript">
  var sorter=new syj.Sort('mainTable');
  sorter.bindCell(0,'STRING_GE');
  sorter.bindCell(1,'NUMBER');
  sorter.bindCell(2,'STRING_GE');
  sorter.bindCell(3,'DATE');
</script>