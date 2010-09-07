// upload window
var newWindow;
var currTreeNode;

$(function() {
			// 初始用户目录树
			dirTree();
			showContent();
		});

function dirTree() {
	$('#dirTree').tree({
				url : '/ubank/portal/showTree.do',
				onClick : function(node) {
					if (node.text != '共享文件夹') {
						currTreeNode = node;
						showContent(node.id);
						setFolderId(node.id);
					}
				}
			});
};

$(function() {
			$('#moveTo').dialog({
				height : 350,
				width : 200,
				modal : true,
				title : '移动到...',
				buttons : [{
					text : '确定',
					iconCls : 'icon-ok',
					handler : function() {
						var node = $('#tt1').tree('getSelected');
						if (node) {
							if (node.attributes.type == 'R') {
								$.messager.alert('提示 ', '不能移到根目录下!', 'info');
								return;
							}
							if (node.attributes.layer > 9) {
								$.messager.alert('提示 ', '文件夹层次不能超过 10 层！!',
										'info');
								return;
							}
							var record = $('#test').datagrid('getSelected');

							var url = '/ubank/portal/moveTo.do';
							var parentId = node.id;
							var id;
							var type = 'file';
							if (record) {
								id = record.id;
								if (record.type == '文件夹') {
									type = 'folder';
								}
							} else {
								id = currTreeNode.id;
								type = 'folder';
							}
							$.get(url, {
										'parentId' : parentId,
										'id' : id,
										'type' : type
									}, function(data) {
										if (data == 'success') {
											returnResult(true);
											reload();
											showContent(parentId);
										} else {
											returnResult(false);
										}
									});
						}
						$('#moveTo').dialog('close');
					}
				}, {
					text : '取消',
					handler : function() {
						$('#moveTo').dialog('close');
					}
				}]
			});
			$('#moveTo').dialog('close');
		});

$(function() {
			$('#copyTo').dialog({
				height : 350,
				width : 200,
				modal : true,
				title : '复制到...',
				buttons : [{
					text : '确定',
					iconCls : 'icon-ok',
					handler : function() {
						var node = $('#tt3').tree('getSelected');
						if (!node) {
							$.messager.alert('提示 ', '你没有选择操作的目录', 'info');
							return;
						}
						if (node) {
							if (node.attributes.type == 'R') {
								$.messager.alert('提示 ', '不能复制到根目录下!', 'info');
								return;
							}
							if (node.attributes.layer > 9) {
								$.messager.alert('提示 ', '文件夹层次不能超过 10 层！!',
										'info');
								return;
							}
							var record = $('#test').datagrid('getSelected');
							var url = '/ubank/portal/copyTo.do';
							var targetId = node.id;
							var id;
							var type = 'file';
							var pid;
							if (record) {
								id = record.id;
								pid = record.pid
								if (record.type == '文件夹') {
									type = 'folder';
								}
							} else {
								id = currTreeNode.id;
								pid = currTreeNode.attributes.pid;
								type = 'folder';
							}
							if (id == targetId || pid == targetId) {
								$.messager.alert('提示 ', '不能复制到自身及所在父亲目录下!',
										'info');
								return;
							}
							$.get(url, {
										'parentId' : targetId,
										'id' : id,
										'type' : type
									}, function(data) {
										if (data == 'success') {
											returnResult(true);
											reload();
											showContent(parentId);
										} else {
											returnResult(false);
										}
									});
						}
						$('#copyTo').dialog('close');
					}
				}, {
					text : '取消',
					handler : function() {
						$('#copyTo').dialog('close');
					}
				}]
			});
			$('#copyTo').dialog('close');
		});

function reload() {
	$('#dirTree').tree('reload');
}

function showContent(param) {
	reqUrl = '/ubank/portal/showFolderContent.do?folderId=';
	if (param != null) {
		reqUrl = reqUrl + param;
	}
	$('#test').datagrid({
		height : 570,
		nowrap : false,
		striped : true,
		url : reqUrl,
		sortName : 'modTime',
		sortOrder : 'desc',
		idField : 'id',
		frozenColumns : [[{
					title : '名称',
					field : 'name',
					width : 450,
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
				}]],
		rownumbers : false,
		singleSelect : true,

		toolbar : [{
			text : '新建目录',
			iconCls : 'icon-add',
			handler : function() {
				if (!currTreeNode || currTreeNode.attributes.type == 'R') {
					$.messager.alert('提示', '根目录下能创建子目录!', 'warning');
					return;
				}
				var layer = currTreeNode.attributes.layer;
				if (layer > 9) {
					$.messager.alert('提示', '最大目录层数不能超过 10 层!', 'warning');
					return;
				}
				$.messager.prompt('新建文件夹', '新建文件夹名称：', function(name) {

							if (trim(name).length == 0) {
								$.messager.alert('提示', '名称不能这空，或空字符串!',
										'warning');
								return;
							}
							var parentId = currTreeNode.id;
							var url = '/ubank/portal/addFolder.do';
							$.get(url, {
										'parentId' : parentId,
										'folderName' : encodeURI(name),
										'layer' : layer
									}, function(data) {
										if (data == 'success') {
											returnResult(true, "");
											reload();
											showContent(parentId);
										} else {
											returnResult(false, data);
										}
									});
						});

			}
		}, {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var record = $('#test').datagrid('getSelected');
				var result = executeCheck(currTreeNode, record);
				if (result) {
					var url = '/ubank/portal/delFolderOrFile.do';
					$.messager.confirm('Warning', 'Are you sure  ?',
							function(r) {
								if (r) {
									var id;
									var pid;
									var type = 'file';
									if (record) {
										id = record.id;
										pid = record.pid;
										if (record.type == '文件夹') {
											type = 'folder';
										}
									} else {
										id = currTreeNode.id;
										pid = currTreeNode.pid;
										type = 'folder';
									}
									$.get(url, {
												'id' : id,
												'type' : type
											}, function(data) {

												if (data == 'success') {
													returnResult(true);
													if (record == null) {
														reload();
													}
													showContent(pid);
												} else {
													returnResult(false);
												}
											});
								}
							});
				}
			}

		}, {
			text : '共享目录',
			iconCls : 'icon-share',
			handler : function() {
				var record = $('#test').datagrid('getSelected');
				if (record != null && record.type != '文件夹') {
					$.messager.alert('提示 ', '文件不能共享，请选择文件夹', 'info');
					return;
				}
				var result = checkRoot(currTreeNode, record);
				if (result) {
					var url = '/ubank/portal/shareFolder.do';
					var id;
					var folderName;
					if (record) {
						id = record.id;
						folderName = record.name;
					} else {
						id = currTreeNode.id;
						folderName = currTreeNode.text;
					}
					$.messager.confirm('My Title', '你确定要共享“' + folderName
									+ '”文件夹吗？', function(r) {
								if (r) {
									$.get(url, {
												'folderId' : id
											}, function(data) {
												if (data == 'success') {
													returnResult(true);
													reload();
												} else {
													returnResult(false);
												}
											});
								}
							});
				}
			}
		}, {
			text : '取消共享',
			iconCls : 'icon-share',
			handler : function() {
				var record = $('#test').datagrid('getSelected');
				if (record != null && record.type != '文件夹') {
					$.messager.alert('提示 ', '文件不会被共享，请选择文件夹', 'info');
					return;
				}
				var result = checkRoot(currTreeNode, record);
				if (result) {
					var url = '/ubank/portal/cancelShare.do';
					var id;
					var folderName;
					if (record) {
						id = record.id;
						folderName = record.name;
					} else {
						id = currTreeNode.id;
						folderName = currTreeNode.text;
					}
					$.messager.confirm('My Title', '你确定要取消“' + folderName
									+ '”及子文件夹的共享吗？', function(r) {
								if (r) {
									$.get(url, {
												'folderId' : id
											}, function(data) {
												if (data == 'success') {
													returnResult(true);
													reload();
												} else {
													returnResult(false);
												}
											});
								}
							});
				}
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
					newWindow = window.open('fileUpload.jsp', 'uploadwindow',
							'top=' + (window.screen.height - wHeight) / 2
									+ ',left=' + (window.screen.width - wWidth)
									/ 2 + ',width=' + wWidth + ',height='
									+ wHeight + ',location=no, scrollbars=yes');
				} else {
					alert("Upload window is already open!");
				}
			}
		}, {
			text : '重命名',
			iconCls : 'icon-redo',
			handler : function() {
				var record = $('#test').datagrid('getSelected');
				var result = executeCheck(currTreeNode, record);
				if (result) {
					var url = '/ubank/portal/rename.do';
					$.messager.prompt('重命名', '新名称：', function(name) {
								if (name) {
									var id;
									var type = 'file';
									var parentId;
									if (record) {
										id = record.id;
										parentId = record.pid
										if (record.type == '文件夹') {
											type = 'folder';
										}
									} else {
										id = currTreeNode.id;
										type = 'folder';
										parentId = id;
									}

									$.get(url, {
												'id' : id,
												'name' : encodeURI(name),
												'type' : type,
												'parentId' : parentId
											}, function(data) {
												if (data == 'success') {
													returnResult(true, '');
													reload();
													showContent(parentId);
												} else {
													returnResult(false, data);
												}
											});
								}
							});
				}
			}
		}, {
			text : '移动到...',
			iconCls : 'icon-cut',
			handler : function() {
				var record = $('#test').datagrid('getSelected');
				var result = executeCheck(currTreeNode, record);
				if (result) {
					$('#moveTo').dialog('open');
					$('#tt1').tree({
								url : '/ubank/portal/showTree.do'
							});
				}
			}
		}, {
			text : '复制到...',
			iconCls : 'icon-edit',
			handler : function() {
				var record = $('#test').datagrid('getSelected');
				var result = executeCheck(currTreeNode, record);
				if (result) {
					$('#copyTo').dialog('open');
					$('#tt3').tree({
								url : '/ubank/portal/showTree.do'
							});
				}
			}
		}],
		onSortColumn : function(sort, order) {
			alert(sort + ":" + order + "---");
		},
		onDblClickRow : function(rowIndex, rowData) {
			if (rowData.type == '文件夹') {
				showContent(rowData.id);
			}
		}
	});
	$('#test').datagrid('clearSelections');
};

function returnResult(status, data) {
	var message = '操作成功！';
	if (!status) {
		message = '操作失败:' + data;
	}
	$.messager.show({
				title : '回执',
				msg : message,
				showType : 'show'
			});
}

function executeCheck(node, record) {
	if (node == null && record == null) {
		$.messager.alert('提示 ', '请选择要操作的对象', 'info');
		return false;
	}
	if (record != null) {
		if (record.init == 'true') {
			$.messager.alert('提示 ', '不能对系统初始目录操作!', 'info');
			return false;
		}
	} else {
		if (node != null && node.attributes.type != 'C') {
			$.messager.alert('提示 ', '不能对系统初始目录操作!', 'info');
			return false;
		}
	}
	return true;
}

function checkRoot(node, record) {
	if (node == null && record == null) {
		$.messager.alert('提示 ', '请选择要操作的对象', 'info');
		return false;
	}
	if (record == null && node != null && node.attributes.type == 'R') {
		$.messager.alert('提示 ', '不能对系统根目录进行此操作!', 'info');
		return false;
	}
	return true;
}

function setFolderId(folderId) {
	$.get("setCurrentFolder.do?currentFolderId=" + folderId);
}
function trim(str) { // 删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
