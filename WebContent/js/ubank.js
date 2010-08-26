//upload window
var newWindow;

$(function() {
			// $('#tt1').tree();
			$('#tt2').tree({
						url : '/ubank/portal/showTree.do',
						onClick : function(node) {

							alert('you click ' + node.text);
						}

					});
		});

function reload() {
	$('#tt2').tree('reload');
}

$(function() {
			$('#test').datagrid({
				height : 570,
				nowrap : false,
				striped : true,
				url : 'datagrid_data.json',
				sortName : 'code',
				sortOrder : 'desc',
				idField : 'code',
				frozenColumns : [[{
							field : 'ck',
							checkbox : true
						}, {
							title : '文件名',
							field : 'code',
							width : 80,
							sortable : true
						}]],
				columns : [[{
							field : 'name',
							title : '名称',
							width : 120
						}, {
							field : 'addr',
							title : '地址',
							width : 120,
							rowspan : 2,
							sortable : true
						}, {
							field : 'col4',
							title : 'Col41',
							width : 150,
							rowspan : 2
						}]],
				pagination : false,
				rownumbers : true,
				singleSelect : false,

				toolbar : [{
							text : '新建目录',
							iconCls : 'icon-add',
							handler : function() {
								resize();
								alert('add');
							}
						}, {
							text : '删除目录',
							iconCls : 'icon-remove',
							handler : function() {
								alert('add');
							}
						}, {
							text : '共享目录',
							iconCls : 'icon-share',
							handler : function() {
								alert('cut');
							}
						}, '-', {
							text : '上传文件',
							iconCls : 'icon-undo',
							handler : function() {
								var wHeight = 350;
								var wWidth = 450;
								var top = (window.screen.height - wHeight) / 2;
								var left = (window.screen.width - wWidth) / 2;
								if (newWindow == undefined || newWindow.closed) {
									newWindow = window
											.open(
													'fileUpload.jsp',
													'uploadwindow',
													'top='
															+ (window.screen.height - wHeight)
															/ 2
															+ ',left='
															+ (window.screen.width - wWidth)
															/ 2
															+ ',width='
															+ wWidth
															+ ',height='
															+ wHeight
															+ ',location=no, scrollbars=yes');
								} else {
									alert("Upload window is already open!");
								}
							}
						}, {
							text : '删除文件',
							iconCls : 'icon-cancel'
						}],
				onSortColumn : function(sort, order) {
					alert(sort + ":" + order);
				}
			});
		});

function resize() {
	$('#test').datagrid({
				title : '新标题',
				striped : true,
				width : 650,
				loadMsg : '正在处理，请稍待。。。',
				queryParams : {
					a : 'abc',
					name : 'sjx'
				}
			});
}

function getSelected() {
	var selected = $('#test').datagrid('getSelected');
	alert(selected.code + ":" + selected.name);
}

function getSelections() {
	var ids = [];
	var rows = $('#test').datagrid('getSelections');
	for (var i = 0; i < rows.length; i++) {
		ids.push(rows[i].code);
	}
	alert(ids.join(':'));
}

function myformatter(value, rec) {
	return 'a:' + value + '>' + rec.name;
}

function setFolderId(folderId) {
	$.get("setCurrentFolder.do?currentFolderId=" + folderId);
}
