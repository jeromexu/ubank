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
<div region="north" border="false" style="height:60px;background:#78B4F0;"><h2 style="font-size:20px;font-weight:bolder;font-family:Arial;">&nbsp;&nbsp;UBank</h2></div>
<div region="west" split="true" title="网络硬盘" style="width:150px;padding:10px;">
  <h1>Tree</h1>
  <ul id="tt1" class="easyui-tree">
  
    <li> <span>我的网盘</span>
      <ul>
        <li> <span>文件</span>
          <ul>
            <li> <span>Sub Folder 1</span>
              <ul>
              </ul>
            </li>
          </ul>
        </li>
        <li> <span>软件</span>
          <ul>
            <li> <span><a href="#">File 11</a></span>
              <ul>
              </ul>
            </li>
          </ul>
        </li>
        <li> <span>照片</span>
          <ul>
            <li> <span><a href="#">File 11</a></span>
              <ul>
              </ul>
            </li>
          </ul>
        </li>
      </ul>
    </li>
  </ul>
  <!-- 这是一个文件夹 
                <li>
                    <span>Sub Folder 1</span>
                    <ul>
                        <li>
                            <span><a href="#">File 11</a></span>
                        </li>
                    </ul>
                </li>
                 -->

 <ul id="tt2"></ul>
</div>
<div region="center" title="网络硬盘 > 
	<a href='#'>我的网盘</a>">
	<div style=""><table id="test"></table></div>
	<div id="information" style="height:6px;background:#D0D8D8;padding:10px;position:relative;width:100%;"></div>
</div>

</body>
</html>
    