<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <script type="text/javascript" src="../js/jquery-1.2.6.js"></script>
        <script type="text/javascript" src="../js/alias-tablesorter.js"></script>
        <script type="text/javascript" src="../js/home.js"></script>
		<link href="../css/home.css" rel="stylesheet" type="text/css">
		<title>${u:getMessage('ubank.home.message.title') }</title>	
	</head>

	<body>
		<div class="mainDiv">
			<div class="topDiv">
			<h2>UBank</h2>
			</div>
			
			<div class="leftDiv">
				
				<div class="search_div">
						<select class="file_size_select" name="fileSize" id="fileSize">
							<option value="0">${u:getMessage('ubank.home.search.filesize.0') }</option>
							<option value="1">${u:getMessage('ubank.home.search.filesize.1') }</option>
							<option value="2">${u:getMessage('ubank.home.search.filesize.2') }</option>
							<option value="3">${u:getMessage('ubank.home.search.filesize.3') }</option>
							<option value="4">${u:getMessage('ubank.home.search.filesize.4') }</option>
						</select>
						
						<select class="file_range_select" name="publishDate" id="publishDate">
							<option value="0">${u:getMessage('ubank.home.search.pubdate.0') }</option>
							<option value="1">${u:getMessage('ubank.home.search.pubdate.1') }</option>
							<option value="2">${u:getMessage('ubank.home.search.pubdate.2') }</option>
							<option value="3">${u:getMessage('ubank.home.search.pubdate.3') }</option>
							<option value="4">${u:getMessage('ubank.home.search.pubdate.4') }</option>
							<option value="5">${u:getMessage('ubank.home.search.pubdate.5') }</option>
						</select>
						
						<input class="file_name_ipt" id="fileNameId" type="text" name="fileName" maxlength="50" value="${u:getMessage('ubank.home.search.input')}" onfocus="clearInput(this.id, '${u:getMessage('ubank.home.search.input')}')" onblur="resetInput(this.id, '${u:getMessage('ubank.home.search.input')}')">
						
						<input class="search_btn" type="button" value="${u:getMessage('ubank.home.search.button')}" onclick="search()" id="searchBtn">
				</div>
				
				<div class="resultset_div" id="searchResultDiv">
				  <jsp:include page="searchResult.jsp" />
				</div>
				
			</div>
			
			<div class="rightDiv">
      
				<div class="loginDiv">
                    <c:if test="${not empty error_msg}">
                      <div class="errorMsgDiv">
                        <span title="${error_msg}">${error_msg}</span>
                      </div>
                    </c:if>
        
					<form action="login.do" method="post" id="loginForm">
						${u:getMessage('ubank.home.login.username.ipt')}:<input class="username_clz" type="text" name="userName" maxlength="50" id="userName"><br>
						${u:getMessage('ubank.home.login.password.ipt')}:<input class="password_clz" type="password" name="password" maxlength="50" id="password"><br>
						<input class="login_btn" type="button" onclick="return login()"  id="loginBtn" value=${u:getMessage('ubank.home.login.login.btn')} />
                        <input class="register_btn" type="button" onclick="goPage('register.jsp')" value=${u:getMessage('ubank.home.login.register.btn')} />
					</form>
				</div>
        
			</div>	
			
			<div class="footDiv"></div>
		</div>	
	</body>
</html>