<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Multiple Files Upload</title>
		<link rel="stylesheet" type="text/css"
			href="../css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="../css/themes/icon.css">
		<script type="text/javascript" src="../js/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../js/ubank.js"></script>
	</head>

	<body onload="">
		<form id="uploadForm" name="uploadForm" method="post"
			action="upload.do" ENCTYPE="multipart/form-data" target="upload_frame">
			<div id="uploadedFiles"></div>
			<input type="file" id="file1" name="file1" size="50" />
			<br />
			<input type="file" id="file2" name="file2" size="50" />
			<br />
			<input type="file" id="file3" name="file3" size="50" />
			<br />
			<input type="file" id="file4" name="file4" size="50" />
			<br />

			<div id="progressBar" style="display: none;">
				<div id="theMeter">
					<div id="progressBarText"></div>
					<div id="progressBarBox">
						<div id="progressBarBoxContent"></div>
					</div>
				</div>
			</div>

			<input type="button" id="uploadbutton" onclick="doUpload();"
				value="upload" />
			<input type="button" id="controlbutton" onclick="controlUpload();"
				value="pause" style="display: none;"/>
			<br>
			
			<div id="hiddenDiv">
				<iframe id='upload_frame' name='upload_frame' src=''
					style='display: none'></iframe>
			</div>
		</form>

		<script type="text/javascript">
			var pauseOrContinue = true;
			var isUpload = false;
			var getInfo;
			
			function doUpload(){
				getInfo = window.setInterval("getUploadInfo()",1000);
				isUpload = true;
				$("#uploadForm").submit();
			}
			
			
			function getUploadInfo(){
				if(isUpload){
					$.ajax({
						type: "GET",
  						url: "getInfo.do",
						dataType: "json",
						success: function(json){
						  updateProgress(json);
						}
					})
				}
			}
			
			function updateProgress(uploadInfo){
			    init();
			    
				var progressPercent = Math.ceil((uploadInfo.bytesRead / uploadInfo.totalSize) * 100);
			    var secondsElapsed = Math.ceil(uploadInfo.deltaTime/1000);
			    var speed = Math.ceil(uploadInfo.bytesRead / (uploadInfo.deltaTime/1000 * 1024));
			    $("#uploadedFiles").html(uploadInfo.uploadedFiles);
			    $("#progressBarText").html('Time elapsed: <b>' + secondsElapsed + '</b> seconds;Average Speed: <b>' + speed + 'KB/s</b><br/>Uploading <b>' + uploadInfo.curFileName + '.. [' + progressPercent + '%]</b>');
			    $("#progressBarBoxContent").attr("style","width:"+ parseInt(progressPercent * 3.5) + "px");
			    
			    if(uploadInfo.errorMsg){
			    	$("#uploadedFiles").html('<font color="red"><b>' + uploadInfo.errorMsg + '</b></font><br/>');
			    }else{
			    	$("#uploadedFiles").html(uploadInfo.uploadedFiles + '<br/><b>Total size: ' + Math.ceil(uploadInfo.totalSize/1024) + 'KB</b>');
			   	}
			   	
			   	if(uploadInfo.completed == "true" || uploadInfo.inProgress == "false"){
				  	window.clearInterval(getInfo);
				  	$("#uploadbutton").show();
				  	$("#controlbutton").hide();
				  	pauseOrContinue = true;
				}
			}

			function init(){
				$("#progressBar").attr("style","display: block");
				$("#progressBarText").html("<b>Loading...</b>");
				$("#uploadbutton").hide();
				$("#controlbutton").show();
				$("#uploadedFiles").html("");
			}
			
			function controlUpload(){
				if(pauseOrContinue){
					pauseOrContinue = false;
					$.get("pause.do"); 
					$("#controlbutton").attr("value","continue");
				}else{
					pauseOrContinue = true;
					$("#controlbutton").attr("value","pause");
					$.get("continueUpload.do"); 
				}
			}
		</script>
	</body>
</html>
