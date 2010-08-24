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
		<title>UBank --- 首页</title>	
	</head>

	<body>
		<div class="mainDiv">
			<div class="topDiv">
			<h2>UBank</h2>
			</div>
			
			<div class="leftDiv">
				
				<div class="search_div">
						<select class="file_size_select" name="fileSize" id="fileSize">
							<option value="0">请选择文件大小</option>
							<option value="1">1KB - 1MB</option>
							<option value="2">1MB - 5MB</option>
							<option value="3">5MB - 10MB</option>
							<option value="4">10MB - 50MB</option>
						</select>
						
						<select class="file_range_select" name="publishDate" id="publishDate">
							<option value="0">请选择发布日期</option>
							<option value="1">近一天</option>
							<option value="2">近三天</option>
							<option value="3">近一周</option>
							<option value="4">近一月</option>
							<option value="5">更早的</option>
						</select>
						
						<input class="file_name_ipt" id="fileNameId" type="text" name="fileName" maxlength="50" value="请输入文件名" onfocus="clearInput(this.id, '请输入文件名')" onblur="resetInput(this.id, '请输入文件名')">
						
						<input class="search_btn" type="button" value="搜索" onclick="search()" id="searchBtn">
				</div>
				
				<div class="resultset_div" id="searchResultDiv">
				
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
						账号:<input class="username_clz" type="text" name="userName" maxlength="50" id="userName"><br>
						密码:<input class="password_clz" type="password" name="password" maxlength="50" id="password"><br>
						<input class="login_btn" type="button" value="登录" onclick="return login()" id="loginBtn" >
                        <input class="register_btn" type="button" value="注册" onclick="goPage('register.jsp')">
					</form>
				</div>
        
			</div>	
			
			<div class="footDiv"></div>
		</div>	
	</body>
</html>