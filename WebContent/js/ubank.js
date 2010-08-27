// upload window
var newWindow;
var currTreeNode;


$(function() {
			//初始化对话框
			dialog(true);
			//初始用户目录树
			dirTtree();
			showContent();
			
		});

function dirTtree() {
			$('#tt2').tree({
						url : '/ubank/portal/showTree.do',
						onClick : function(node) {
							currTreeNode = node;
							showContent(node.id);
						}
					});
//			showContent();
		};

function reload() {
	$('#tt2').tree('reload');
}

function showContent(param) {
	reqUrl = '/ubank/portal/showFolderContent.do?folderId=';
	if(param!=null){
		reqUrl = reqUrl+param;
	}
	$('#test').datagrid({
		height : 570,
		nowrap : false,
		striped : true,
		url : reqUrl,
		sortName : 'name',
		sortOrder : 'desc',
		idField : 'id',
		frozenColumns : [[{
					title : '名称',
					field : 'name',
					width : 380,
					sortable : true
				}]],
		columns : [[{
					field : 'size',
					title : '大小',
					width : 120,
					align : 'right'
				}, {
					field : 'type',
					title : '类型',
					width : 120,
					align : 'right',
					sortable : true
				}, {
					field : 'modTime',
					title : '修改时间',
					width : 200,
					align : 'right'
				}, {
					field : 'opt',
					title : '操作',
					width : 100,
					align : 'center',
					rowspan : 2,
					formatter : function(value, rec) {
						return '<span style="color:red">Edit Delete</span>';
					}
				}]],
		pagination : true,
		rownumbers : true,
		singleSelect : true,

		toolbar : [{
					text : '新建目录',
					iconCls : 'icon-add',
					handler : function() {
						if (!currTreeNode || currTreeNode.text == '我的网盘') {
							alert("不能在根目录下创建新目录！");
						} else {
							dialog(false);
						}
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
							newWindow = window.open('fileUpload.jsp',
									'uploadwindow', 'top='
											+ (window.screen.height - wHeight)
											/ 2 + ',left='
											+ (window.screen.width - wWidth)
											/ 2 + ',width=' + wWidth
											+ ',height=' + wHeight
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
};

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

function closeDia() {
	$('#dlg1').dialog({
				closed : true
			});
}
function dialog(show) {
	var dlg = $('#dlg1').dialog({
				iconCls : 'icon-save',
				width : 400,
				modal : true,
				closed : show,
				buttons : {
					'Save' : function() {
						var parentId = currTreeNode.id;
						var url = '/ubank/portal/addFolder.do';
						var name = $('#newFolder').attr('value');
						$.get(url, {
									'parentId' : parentId,
									'folderName' : name
								}, function(data) {
									if (data.vale = 'success') {
										closeDia()
										alert("操作成功");
										dirTtree();
										reload();
										showContent(parentId);
									} else {
										alert("操作失败");
									}
								});

					},
					'Cancel' : function() {
						$('#dlg1').dialog({
									closed : true
								});
					}
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
