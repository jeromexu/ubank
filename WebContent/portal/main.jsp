<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="../css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../css/themes/icon.css">
<script type="text/javascript" src="../js/jquery-1.2.6.js"></script>
<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/ubank.js"></script>
	
</head>
<body class="easyui-layout">
<div region="north" border="false" style="height:60px;background:#78B4F0;">
  <h2 style="font-size:20px;font-weight:bolder;font-family:Arial;">&nbsp;&nbsp;UBank
  <a href="logout.do">退出</a>
  </h2>
</div>

<div region="west" split="true" title="网络硬盘" style="width:150px;padding:10px;">
  <h1>Tree</h1>


 <ul id="tt2"></ul>
</div>
<div region="center" title="网络硬盘 > 
	<a href='#'>我的网盘</a>">
	<div style=""><table id="test"></table></div>
	<div id="information" style="height:6px;background:#D0D8D8;padding:10px;position:relative;width:100%;"></div>
</div>

</body>
</html>
    