$(function() {
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '/redis/getAllRedisInfo/',
 		where: {
	  		token : getToken()
		},
		page: true,
		cols: [[
			{type:'numbers'},
//			{field:'id', sort: true, title: 'ID'},
			{field:'key', sort: true, title: 'KEY'},
			{field:'url', sort: true, title: '地址'},
//			{field:'bugCloser', sort: true, title: '操作人'},
//			{field:'reason', sort: true, title: '原因'},
//			{field:'logType', sort: true, title: '操作类型'},
			{align:'center', toolbar: '#barTpl', minWidth: 180, title: '操作'}
    	]]
	});

	//工具条点击事件
	layui.table.on('tool(table)', function(obj){
		var data = obj.data;
		var layEvent = obj.event;
 
		if(layEvent === 'view'){ //修改
			showViewModel(data);
		} else if(layEvent === 'del'){ //删除
			doDelete(data);
		} 
	});
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		doSearch();
	});
});

//显示表单弹窗
function showViewModel(data){
	layer.open({
		type: 1,
		title: "查看值",
		area: '900px',
		offset: 'auto',
		content: $("#viewModel").html()
	});
	$.ajax({
		url: "redis/getRedisInfo", 
		data : {"url":data.url,"key":data.key},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			 var resultJson = formatJson(JSON.stringify( data)).replace("\\\"","\"");
			$("#content").html(resultJson);
		}
	});
}


function doDelete(data){
	
	layer.confirm('确定删除？', function(index){
		layer.close(index);
		layer.load(1);
		$.ajax({
			url: "redis/removeKey", 
			data : {"url":data.url,"key":data.key},
			type: "POST", 
			dataType: "JSON", 
			success: function(data){
				layer.closeAll('loading');
				if(data.code == "200"){
					layer.msg("删除成功");
				}else{
					layer.msg(data.msg);
				}
			}
		});
	});
	
}

//搜索
function doSearch(){
	var url = $("#url").val();
	if(url ==null || url == undefined ||url == ""){
		layer.alert("请选择redis的url");
		return;
	}
	var key = $("#key").val();
	if(url ==null || url == undefined ||url == ""){
		layer.alert("请输入查询的key");
		return;
	}
	layui.table.reload('table', {where: {url: url,key: key}});
}
