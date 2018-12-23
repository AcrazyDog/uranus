$(function() {
	
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '/api/knowledge/list',
 		where: {
	  		token : getToken()
		},
		method: 'POST', //方式
		page: true,
		limit:15,
		cols: [[
			{type:'numbers'},
			//{field:'id', sort: true, title: 'ID'},
			{field:'title', sort: true, title: '标题'},
			{field:'createName', sort: true, title: '创建人'},
			{field:'createTime', sort: true, templet:function(d){ return layui.util.toDateString(d.createTime); }, title: '创建时间'},
			{field:'updateName', sort: true, title: '修改人'},
			{field:'updateTime', sort: true, templet:function(d){ return layui.util.toDateString(d.updateTime); }, title: '修改时间'},
			{align:'center', toolbar: '#barTpl', minWidth: 150, title: '操作'}
    	]]
	});
	
//	var $mylist = $(".layui-table");
//	var $mybody = $mylist.find("tbody");
//	$mybody.dblclick(function(event){//监听双击
//		alert($(event.target).closest("tr")[0].outerHTML)
//	});
	
	//工具条点击事件
	layui.table.on('tool(table)', function(obj){
		var data = obj.data;
		var layEvent = obj.event;
 
		if(layEvent === 'view'){ //查看
			doView(data);
		}else if(layEvent === 'edit'){ //查看
			doEdit(data);
		}else if(layEvent === 'delete'){//删除
			doDelete(data);
		}
	});
	
	//时间范围
	layui.laydate.render({
		elem: '#searchDate',
		type: 'date',
		range: true,
		theme: '#393D49'
	});
	
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		doSearch();
	});
});

function doView(obj){
	$.ajax({
		url: "api/knowledge/getKnowledgeInfo?token="+getToken(), 
		data : {"id":obj.id},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			layer.closeAll('loading');
			if(data.code==200){
				var knowledge = data.knowledge;
				layer.open({
					type: 1,
					title: "查看知识",
					area: '1300px',
					offset: 'auto',
					content: $("#viewKnowledgeModel").html()
				});
				var converter = new showdown.Converter();
			    var html = converter.makeHtml( knowledge.context);
			    document.getElementById("result").innerHTML =html;
			}else{
				layer.msg(data.msg,{icon: 2});
			}
		}
	});
}

function doEdit(obj){
	//加入参数到父页面
	var knowledgeEditParam = {id:obj.id};
	rootParam = {knowledgeEditParam:knowledgeEditParam}; 
	showPage("knowledge/knowledgeinfo.html");
}

function doDelete(obj){
	layer.confirm('确定删除？', function(index){
		layer.close(index);
		layer.load(1);
		$.ajax({
			url: "api/knowledge/deleteKnowledge", 
			data : {"id":obj.id,"createName":obj.createName},
			type: "POST", 
			dataType: "JSON", 
			success: function(data){
				layer.closeAll('loading');
				if(data.code == 200){
					//重新刷新table
					layui.table.reload("table");
				}else{
					layer.msg(data.msg);
				}
			}
		});
	});
}

//搜索
function doSearch(){
	var searchDate = $("#searchDate").val().split(" - ");
	var createName = $("#createUserName").val();
	var title = $("#title").val();
	var param ={startDate:searchDate[0], endDate: searchDate[1],createName:createName,title:title};
	layui.table.reload('table', {where: {startDate:searchDate[0], endDate: searchDate[1],createName:createName,title:title}});
}

