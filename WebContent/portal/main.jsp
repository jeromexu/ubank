<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<link rel="shortcut icon" href="../favicon.ico">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UBank 网络硬盘</title>
<link rel="stylesheet" type="text/css"
	href="../css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="../css/home.css">
<script type="text/javascript" src="../js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/ubank.js"></script>

</head>
<body class="easyui-layout">

<div region="north" border="false"
	style="height: 75px; background: #78B4F0;">
<h2 style="font-size: 20px; font-weight: bolder; font-family: Arial; margin:0;">
	<span style="float:right;margin-right:35px;">
	
	<a
	href="home.html" style="text-decoration: underline;color:green;font-size:15px;">共享文件下载</a>
	
<a id="point" style="margin-left:200px;">积分:</a> <a
	id="userPoint">${session_user.point}</a></span>
&nbsp;&nbsp;<img src="../imgs/UBank.JPG"/> <a href="logout.do">退出</a> &nbsp;&nbsp;&nbsp;&nbsp;
</div>
</h2>
<div region="west" split="true" title="网络硬盘"
	style="width: 150px; padding: 10px;">

<ul id="dirTree"></ul>
</div>
<div  region="center" title="网络硬盘">
<div style="">
<table id="test"></table>
</div>
<div id="moveTo" icon="icon-save" style="padding: 5px;">
<ul id="tt1"></ul>
</div>
<div id="copyTo" icon="icon-save" style="padding: 5px;">
<ul id="tt3"></ul>
</div>
</div>
</body>
</html>
