// upload window
var newWindow;

$(function() {
			// $('#tt1').tree();
			$('#tt2').tree({
						url : '/ubank/portal/showTree.do',
						onClick : function(node) {

							alert('you click ' + node.id);
						}

					});
		});

function reload() {
	$('#tt2').tree('reload');
}

$(function() {
			$('#test').datagrid({
				// title : 'My Title',
				//iconCls : 'icon-save',
				// width : 600,
				height : 570,
				nowrap : false,
				striped : true,
				url : 'datagrid_data.json',
				sortName : 'code',
				sortOrder : 'desc',
				idField : 'code',
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
						}

				]],
				pagination : true,
				rownumbers : true,
				singleSelect : true,

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
