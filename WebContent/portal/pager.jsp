<%@ page contentType="text/html" language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>

<link href="../css/home.css" rel="stylesheet" type="text/css">

<c:set var="pageCount" value='<%=Integer.parseInt(request.getParameter("pageCount"))%>' />
<c:set var="pageNum" value='<%=Integer.parseInt(request.getParameter("pageNum"))%>' />
<c:set var="postToUrl" value='<%=request.getParameter("targetUrl")%>' />

<div class="pagination" id="pageDiv">
	<%
	    String pageCountStr = request.getParameter("pageCount");
	    String pageNumStr = request.getParameter("pageNum");
	    
	    int pageCount = Integer.parseInt(pageCountStr);
	    int pageNum = Integer.parseInt(pageNumStr);
	    int liststep = 10;
	    int listbegin = (pageNum - (int) Math.ceil((double) liststep / 2));
	    if (listbegin < 1) {
	        listbegin = 1;
	    }
	    int listend = pageNum + liststep / 2;
	    if (listend > pageCount) {
	        listend = pageCount + 1;
	    }
	%>

	<%
	    if (pageNum == 1) {
	%>
  
	<input type="button" id="first" value="${u:getText('ubank.pager.button.first')}" disabled="disabled" />
	<input type="button" id="previous" value="${u:getText('ubank.pager.button.prev')}" disabled="disabled" />
  
	<%
	    } else {
	%>
  
    <c:set var="alertMsg" value="${u:getText('search.invalid.input')}" />
	<input type="button" id="first" value="${u:getText('ubank.pager.button.first')}" onclick="return searchForPager('1','${postToUrl }', '${alertMsg}');" />
	<input type="button" id="previous" value="${u:getText('ubank.pager.button.prev')}" onclick="return searchForPager('${pageNum-1}','${postToUrl }', '${alertMsg}');" />
	
  <%
	    }
	    if (listbegin > 1) {
	%>
	...
	<%
	    }
	    for (int i = listbegin; i < listend; i++) {
	%>
	<%
	    if (i == pageNum) {
	%>
	<span class="currentPage"><%=i%></span>
	<%
	    continue;
	        }
	%>


	<a href="javascript:void(0);" onclick="return searchForPager('<%=i%>','${postToUrl }', '${alertMsg}');"><%=i%></a>

	<%
	    }
	    if (listend <= pageCount) {
	%>
	...
	<%
	    }
	    if (pageCount == pageNum) {
	%>
  
	<input type="button" id="next" value="${u:getText('ubank.pager.button.next')}" disabled="disabled" />
	<input type="button" id="last" value="${u:getText('ubank.pager.button.last')}" disabled="disabled" />
	
    <%
	    } else {
	%>
  
	<input type="button" id="next" value="${u:getText('ubank.pager.button.next')}" onclick="return searchForPager('${pageNum+1}','${postToUrl }', '${alertMsg}');" />
	<input type="button" id="last" value="${u:getText('ubank.pager.button.last')}" onclick="return searchForPager('${pageCount}','${postToUrl }', '${alertMsg}');" />
	
    <%
	    }
	%>

</div>

