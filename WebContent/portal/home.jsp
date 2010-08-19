<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>

<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript" src="../js/home.js"></script>
		<link href="../css/home.css" rel="stylesheet" type="text/css">
		<title>Home</title>	
	</head>

	<body>
		<div class="mainDiv">
			<div class="topDiv"></div>
			
			<div class="leftDiv">
				
				<div class="search_div">
					<form action="search.do" method="post">
						<select class="file_size_select" name="fileSize">
							<option value="0">请选择文件大小</option>
							<option value="1">1KB - 1MB</option>
							<option value="2">1MB - 5MB</option>
							<option value="3">5MB - 10MB</option>
							<option value="4">10MB - 50MB</option>
						</select>
						
						<select class="file_range_select" name="publishDate">
							<option value="0">请选择发布日期</option>
							<option value="1">近一天</option>
							<option value="2">近三天</option>
							<option value="3">近一周</option>
							<option value="4">近一月</option>
							<option value="4">更早的</option>
						</select>
						
						<input class="file_name_ipt" id="fileNameId" type="text" name="fileName" maxlength="50" value="请输入文件名" onfocus="clearInput(this.id, '请输入文件名')" onblur="resetInput(this.id, '请输入文件名')">
						
						<input class="search_btn" type="button" value="搜索" onclick="submit(this.id)" id="searchBtn">
					</form>
				</div>
				
				<div class="resultset_div">
					
					<table cellpadding="0" cellspacing="0" border="1">
						<tr align="center">
							<td width="260">File Name</td>
							<td width="100">Size</td>
							<td width="150">Owner</td>
							<td width="200">Date</td>
							<td width="100">Download</td>							
						</tr>
						
						<tr align="center">
							<td>Thinking in Java</td>
							<td>15.2MB</td>
							<td>xuezhongde</td>
							<td>2010-8-18 12:00:00</td>
							<td>Download</td>							
						</tr>

						<tr align="center">
							<td>Thinking in Java</td>
							<td>15.2MB</td>
							<td>xuezhongde</td>
							<td>2010-8-18 12:00:00</td>
							<td>Download</td>							
						</tr>

						<tr align="center">
							<td>Thinking in Java</td>
							<td>15.2MB</td>
							<td>xuezhongde</td>
							<td>2010-8-18 12:00:00</td>
							<td>Download</td>							
						</tr>
						
						<tr align="center">
							<td>Thinking in Java</td>
							<td>15.2MB</td>
							<td>xuezhongde</td>
							<td>2010-8-18 12:00:00</td>
							<td>Download</td>							
						</tr>

						<tr align="center">
							<td>Thinking in Java</td>
							<td>15.2MB</td>
							<td>xuezhongde</td>
							<td>2010-8-18 12:00:00</td>
							<td>Download</td>							
						</tr>

						<tr align="center">
							<td>Thinking in Java</td>
							<td>15.2MB</td>
							<td>xuezhongde</td>
							<td>2010-8-18 12:00:00</td>
							<td>Download</td>							
						</tr>

						<tr align="center">
							<td>Thinking in Java</td>
							<td>15.2MB</td>
							<td>xuezhongde</td>
							<td>2010-8-18 12:00:00</td>
							<td>Download</td>							
						</tr>

					</table>
				
				</div>
				
				<div class="pagination_div">
					<a href="#">1</a>
					<a href="#">2</a>
					<a href="#">3</a>
					<a href="#">4</a>
					<a href="#">5</a>
					<a href="#">6</a>
					<a href="#">7</a>
					<a href="#">8</a>
					<a href="#">9</a>
				</div>
				
			</div>
			
			<div class="rightDiv">
				
				<div class="loginDiv">
					<form action="login.do" method="post">
						账号:<input class="username_clz" type="text" name="username" maxlength="50"><br>
						密码:<input class="password_clz" type="password" name="password" maxlength="50"><br>
						<input class="login_btn" type="button" value="登录" onclick="submit(this.id)" id="loginBtn" >
                        <input class="register_btn" type="button" value="注册" onclick="goPage('register.html')">
					</form>
				</div>
        
                <c:if test="${not empty error_msg}">
                  <div class="errorMsgDiv">
                    <span title="${error_msg}">${error_msg}</span>
                  </div>
                </c:if>
				
			</div>	
			
			<div class="footDiv"></div>
		</div>	
	</body>
</html>