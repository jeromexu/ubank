<%@ include file="/WEB-INF/taglib/taglibs.jinc" %>

<html>
	<head>
		<script type="text/javascript" src="../js/home.js"></script>
		<link href="../css/home.css" rel="stylesheet" type="text/css">
		<title>Home</title>	
	</head>

	<body>
		<div class="mainDiv">
			<div class="topDiv"></div>
			
			<div class="leftDiv">
				
				<div class="search_div">
					<form action="#" method="post">
						<select class="file_size_select" name="fileSize">
							<option value="0">Please choose size</option>
							<option value="1">1KB - 1MB</option>
							<option value="2">1MB - 5MB</option>
							<option value="3">5MB - 10MB</option>
							<option value="4">10MB - 50MB</option>
						</select>
						
						<select class="file_range_select" name="publishDate">
							<option value="0">Please choose range</option>
							<option value="1">recent one day</option>
							<option value="2">recent three days</option>
							<option value="3">recent one week</option>
							<option value="4">recent one month</option>
							<option value="4">earlier</option>
						</select>
						
						<input class="file_name_ipt" id="fileNameId" type="text" name="fileName" maxlength="50" value="input file name" onfocus="clearInput(this.id, 'input file name')" onblur="resetInput(this.id, 'input file name')">
						
						<input class="search_btn" type="button" value="Search">
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
						Username:<input class="username_clz" type="text" name="username" maxlength="50"><br>
						Password:<input class="password_clz" type="password" name="password" maxlength="50"><br>
						<input class="login_btn" type="button" value="Login" onclick="submit(this.id)" id="loginBtn" >
                        <input class="register_btn" type="button" value="Register" onclick="goPage('register.html')">
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