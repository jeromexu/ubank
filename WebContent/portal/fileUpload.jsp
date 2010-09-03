<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Multiple Files Upload</title>
		<link rel="stylesheet" type="text/css"
			href="../css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="../css/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="../css/upload.css">
		<script type="text/javascript" src="../js/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../js/ubank.js"></script>
		<script type="text/javascript">
		    function Map() {   
			 var struct = function(key, value) {   
			  this.key = key;   
			  this.value = value;   
			 }   
			    
			 var put = function(key, value){   
			  for (var i = 0; i < this.arr.length; i++) {   
			   if ( this.arr[i].key === key ) {   
			    this.arr[i].value = value;   
			    return;   
			   }   
			  }   
			   this.arr[this.arr.length] = new struct(key, value);   
			 }   
			    
			 var get = function(key) {   
			  for (var i = 0; i < this.arr.length; i++) {   
			   if ( this.arr[i].key === key ) {   
			     return this.arr[i].value;   
			   }   
			  }   
			  return null;   
			 }   
			    
			 var remove = function(key) {   
			  var v;   
			  for (var i = 0; i < this.arr.length; i++) {   
			   v = this.arr.pop();   
			   if ( v.key === key ) {   
			    continue;   
			   }   
			   this.arr.unshift(v);   
			  }   
			 }   
			    
			 var size = function() {   
			  return this.arr.length;   
			 }   
			    
			 var isEmpty = function() {   
			  return this.arr.length <= 0;   
			 }    
			 this.arr = new Array();   
			 this.get = get;   
			 this.put = put;   
			 this.remove = remove;   
			 this.size = size;   
			 this.isEmpty = isEmpty;   
			}   
		</script>
	</head>

	<body onload="addUploadForm()">
		<input type="button" id="addFormButton" onclick="addUploadForm()"
				value="增加文件"/>
		<hr><br><br>
		<div id = "mainUpload"></div>
		<input type="button" id="uploadbutton" onclick="doUpload();"
				value="上传" />
				
		<script type="text/javascript">
			var index = 1;
			var uploadTag = new Array();
			var getInfoMap = new Map();  
			var controllMap = new Map();  
			var isUpload = false;
			var main = $("#mainUpload");
			
			window.setInterval("getIframeInfo()",1000);
			
			function getIframeInfo(){
				for(tag in uploadTag){
					var tagUpload = uploadTag[tag];
					var errorMsg = $(window.frames["upload_frame"+tagUpload+""].document).find("#uploadError").html();
					if(errorMsg != null && errorMsg != ""){
						window.clearInterval(getInfoMap.get(tagUpload));
						$("#upload_frame"+tagUpload).show();
						
						$("#progressBar"+tagUpload).attr("style","display: none");
						$("#progressBarText"+tagUpload).html("");
						$("#controlbutton"+tagUpload).hide();
						$("#uploadedFiles"+tagUpload).html("");
						$("#progressBarText"+tagUpload).hide();
						$("#progressBarBoxContent"+tagUpload).hide();
						$("#uploadbutton").hide();
					}
				}
			}
			
			function addUploadForm(){
				main.append(
					"<form id='uploadForm"+index+"' name='uploadForm"+index+"' method='post' "
					+ "action='upload.do' ENCTYPE='multipart/form-data' target='upload_frame"+index+"'>"
					+ "<div id='uploadedFiles"+index+"'></div>"
					+ "<input type='file' id='file"+index+"' name='"+index+"' size='30' />"
					+ "<div id='progressBar"+index+"' style='display: none;'>"
					+ "	<div id='theMeter"+index+"'>"
					+ "	<div id='progressBarText"+index+"'></div>"
					+ "	<div id='progressBarBox"+index+"'>"
					+ "	<div id='progressBarBoxContent"+index+"' class='progress'></div>"
					+ "	</div>"
					+ "	</div>"
					+ " </div>"
					+ "<input type='button' id='controlbutton"+index+"' onclick='controlUpload("+index+");'"
					+ "	value='暂停' style='display: none;'/>"
					+ "<br>"
					+ "<iframe id='upload_frame"+index+"' name='upload_frame"+index+"' style='display: none;' height='50' width='400'></iframe>"
					+ "</form>");
				
				uploadTag.push(index);
				controllMap.put(index,true);
				index++;
			}
			
			function doUpload(){
				isUpload = true;
				for(tag in uploadTag){
					var tagUpload = uploadTag[tag];
					if($("#file" + tagUpload).attr("value")!=""){
						$("#uploadForm" + tagUpload).submit(); 
						var getInfo = window.setInterval("getUploadInfo("+tagUpload+")",1000);
						getInfoMap.put(tagUpload,getInfo);  
						$("#addFormButton").hide();
					}
				}
			}
			
			
			function getUploadInfo(filedName){
				if(isUpload){
					$.ajax({
						type: "GET",
  						url: "getInfo.do?filedName="+filedName+"&d="+new Date(),
						dataType: "json",
						success: function(json){
						  updateProgress(json,filedName);
						}
					})
				}
			}
			
			function updateProgress(uploadInfo,filedName){
			    init(filedName);
			    
				var progressPercent = Math.ceil((uploadInfo.bytesRead / uploadInfo.totalSize) * 100);
			    var secondsElapsed = Math.ceil(uploadInfo.deltaTime/1000);
			    var speed = Math.ceil(uploadInfo.bytesRead / (uploadInfo.deltaTime/1000 * 1024));
			    if(speed < 0){
			    	speed = 0;
			    }
			    
			    $("#uploadedFiles"+filedName).html(uploadInfo.uploadedFiles);
			    $("#progressBarText"+filedName).html('花费时间: <b>' + secondsElapsed + '</b> 秒;平局速度: <b>' + speed + 'KB/s</b><br/>上传 <b>' + uploadInfo.curFileName + '.. [' + progressPercent + '%]</b>');
			    $("#progressBarBoxContent"+filedName).attr("style","width:"+ parseInt(progressPercent * 3.5) + "px");
			    
			    if(uploadInfo.errorMsg){
			    	$("#progressBarText"+filedName).hide();
				  	$("#progressBarBoxContent"+filedName).hide();
			    	$("#uploadedFiles"+filedName).html('<font color="red"><b>' + uploadInfo.errorMsg + '</b></font><br/>');
			    }else{
			    	var displaySize;
			    	var size = Math.ceil(uploadInfo.totalSize/1024);
			    	if(size >= 1024){
			    		displaySize = Math.round(size/1024*100)/100 + "MB";
			    	}else{
			    		displaySize = size + "KB";
			    	}
			    
			    	$("#uploadedFiles"+filedName).html(uploadInfo.uploadedFiles + '<br/><b>文件大小: ' + displaySize + '</b>');
			   	}
			   	
			   	if(uploadInfo.inProgress == "false"){
			   		var pauseOrContinue = controllMap.get(filedName);
			   		controllMap.put(filedName,true);
				  	window.clearInterval(getInfoMap.get(filedName));
				  	
				  	$("#controlbutton"+filedName).hide();
				  	if(uploadInfo.completed == "true"){
				  		 $("#progressBarText"+filedName).html('花费时间: <b>' + secondsElapsed + '</b> 秒;平局速度: <b>' + speed + 'KB/s</b><br/>上传 <b>' + uploadInfo.curFileName + '.. [100%]</b>');
				  		 $("#progressBarBoxContent"+filedName).attr("style","width:"+ parseInt(100 * 3.5) + "px");
				  	}
				}
			}

			function init(filedName){
				$("#progressBar"+filedName).attr("style","display: block");
				$("#progressBarText"+filedName).html("<b>读取中...</b>");
				$("#controlbutton"+filedName).show();
				$("#uploadedFiles"+filedName).html("");
				$("#progressBarText"+filedName).show();
				$("#progressBarBoxContent"+filedName).show();
				$("#uploadbutton").hide();
			}
			
			function controlUpload(filedName){
				var pauseOrContinue = controllMap.get(filedName);
				if(pauseOrContinue){
					controllMap.put(filedName,false);
					$.get("pause.do?filedName="+filedName); 
					$("#controlbutton"+filedName).attr("value","继续");
				}else{
					controllMap.put(filedName,true);
					$.get("continueUpload.do?filedName="+filedName); 
					$("#controlbutton"+filedName).attr("value","暂停");
				}
			}
			
		</script>
	</body>
</html>
