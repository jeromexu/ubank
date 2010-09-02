/**
 * Clear specified input value, if the current input value is val
 * 
 * @param input_id input id
 * @param val given val
 * @return void
 * @author zdxue
 */
function clearInput(input_id, val) {
	var inputVal = document.getElementById(input_id).value;
	if(inputVal == val) {
		document.getElementById(input_id).value='';
	}
}

/**
 * Reset input value to the given value, if the current input value was empty
 * 
 * @param input_id input id
 * @param val given value
 * @return void
 * @author zdxue
 */
function resetInput(input_id, val) {
	var inputVal = document.getElementById(input_id).value;
	var reg = /^[\s]+$/;
	if(inputVal.length == 0 || reg.test(inputVal)) {
		document.getElementById(input_id).value=val;	
	}	
}

/**
 * Goto the specified page
 * 
 * @param url given page url
 * @return void
 * @author zdxue
 */
function goPage(url) {
	location.href=url;	//open new page in current window
	//window.open(url);  //open new page in new window
}

function login() {
	var userName = $("#userName").val();
	var pwd = $("#password").val();
	
	if($.trim(userName) == ''){
		alert("请输入用户名");
		$("#userName").focus();
		return false;
	}
	
	if(pwd.length == 0) {
		alert("请输入密码");
		$("#password").focus();
		return false;
	}
	
	submit('loginForm');
}

/**
 * Submit a form
 * 
 * @param form_id form id
 * @return void
 * @author zdxue
 */
function submit(form_id) {
	document.getElementById(form_id).submit();
}

function search() {
	var url = 'search.do';
	var rnd = new Date();
	var data = "fileSize=" + $("#fileSize").val() + "&publishDate=" + $("#publishDate").val() + "&fileName=" + $("#fileNameId").val() + "&rnd=" + rnd;
	
	ajaxRequest(url, data);
}

function searchForPager(pageNum, url) {
	var url = 'search.do';
	var rnd = new Date();
	var data = "fileSize=" + $("#hidFileSize").val() + "&publishDate=" + $("#hidPublishDate").val() + "&fileName=" + $("#hidFileName").val() + "&pageNum=" + pageNum + "&rnd=" + rnd;
	
	ajaxRequest(url, data);
}

function ajaxRequest(url, data) {
	var timeoutMillsec = 180 * 1000;
	
	if(data){
		$.ajax({
			type:'POST',
			url:url,
			data:data,
	        beforeSend:loading,
			success:callback,
	        timeout:timeoutMillsec,
	        error:requestTimeout
	        
		});
	}else{
		$.ajax({
			type:'POST',
			url:url,
	        beforeSend:loading,
			success:callback,
	        timeout:timeoutMillsec,
	        error:requestTimeout
		});
	}
}

function loading(){
	var text = "<div class='loadingImage'><img src='../imgs/loading.gif'/></div>"
	$('#searchResultDiv').empty().append(text);
}

function callback(text){
	$('#searchResultDiv').empty().append(text);
}

function requestTimeout(){
	var text = "<script>location.href='../common/500.jsp';</script>";
	$('#searchResultDiv').empty().append(text);
}

function download(confirmMsg, pointNotEnough, mustLogin, isLogin, url, point, needPoint) {
	if(confirm(confirmMsg)) {
		if(isLogin) {
			/*
			if(point < needPoint){
				alert(pointNotEnough);
				return false;
			}
			*/
			location.href=url;
		}else{
			alert(mustLogin);
			$('#eventPath').val("../portal/home.html");
			$('#userName').focus();
			//location.href="../portal/home.html";
		}
	}
	return false;
}