var layTable ;

$(function() {
	//渲染表格
	var layTable =layui.table.render({
		elem : '#table',
		url : 'api/getTaskList',
 		where: {
	  		token : getToken()
		},
		cellMinWidth: 80,
//		page: true,
		cols: [[
			{type:'checkbox'},
//			{field:'bugId', sort: true, title: 'ID'},
//			{field:'bugType', sort: true, title: '任务类型',minWidth:180},
			{field:'group',width:80, sort: true, title: '项目组'},
			{field:'taskName', sort: true, title: '任务名称'},
			//{field:'preTime', sort: true, title: '上级计划完成时间',width:120},
			{field:'adviceName', sort: true, title: '下达者',width:120},
			{field:'adviceTime', sort: true, title: '下达时间',width:180},
			{field:'workflowStatus', sort: true, title: '工作流状态',width:120},
//			{field:'roleName', sort: true,title: '角色'},
//			{field:'createTime', sort: true, templet:function(d){ return layui.util.toDateString(d.createTime); }, title: '创建时间'},
//			{field:'userStatus', sort: true, templet: '#statusTpl',width: 80, title: '状态'},
			{align:'center', toolbar: '#barTpl', minWidth: 150, title: '操作'}
    	]]
	});
	
	//添加按钮点击事件
	$("#addBtn").click(function(){
		showEditModel(null);
	});
	//添加按钮点击事件
	$("#closeAllBtn").click(function(){
		closeChooseBug();
	});
	
	//添加按钮点击事件
	$("#cancelCloseBug").click(function(){
		layer.closeAll('loading');
	});
	
	//工具条点击事件
	layui.table.on('tool(table)', function(obj){
		var data = obj.data;
		var layEvent = obj.event;
 
		if(layEvent === 'view'){ //查看
			doView(data);
		} else if(layEvent === 'close'){ //提交
			closeTask(data);
			
		} else if(layEvent === 'forward'){ //转发
			doForward(obj);
		}
	});
	
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		doSearch(table);
	});
});

//查看
function doView(obj){
	layer.load(1);
	$.ajax({
		url: "api/viewTask?token="+getToken(), 
		data : {"taskId":obj.projectTaskId},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			layer.closeAll('loading');
			if(data.code==200){
				var task = data.task;
				layer.open({
					type: 1,
					title: "查看任务",
					area: '600px',
					offset: '120px',
					content: $("#viewTask").html()
				});
				$("#taskName").val(task.taskName);
				$("#projectTaskCode").val(task.projectTaskCode);
				$("#prodtName").val(task.prodtName);
				$("#desc").val(task.desc);
			}else{
				layer.msg(data.msg,{icon: 2});
			}
		}
	});
}

//搜索
function doSearch(table){
	var key = $("#searchKey").val();
	var value = $("#searchValue").val();
	if (value=='') {
		key = '';
	}
	layui.table.reload('table', {where: {searchKey: key,searchValue: value}});
}
//删除选中bug
function closeChooseBug(){
	var checkedTasks = layui.table.checkStatus("table").data;
	if(checkedTasks.length < 1){
		layer.alert("至少选择一条记录");
		return;
	}
	var projectTaskIds ="";
	var projectTaskCodes ="";
	var taskFlagCodes ="";
	for(var i =0;i<checkedTasks.length;i++){
		projectTaskIds +=checkedTasks[i].projectTaskId.split("`")[0] +",";
		projectTaskCodes +=checkedTasks[i].projectTaskCode+",";
		taskFlagCodes +=checkedTasks[i].taskFlagCode+",";
	}
	
	layer.confirm('确定要关闭吗？', function(index){
		layer.close(index);
		layer.load(1);
		$.ajax({
			url: "api/closeManyTasks?token="+getToken(), 
			data : {"projectTaskIds":projectTaskIds,"projectTaskCodes":projectTaskCodes,"taskFlagCodes":taskFlagCodes},
			type: "POST", 
			dataType: "JSON", 
			success: function(data){
				layer.closeAll('loading');
				if(data.code==200){
					layer.msg(data.msg,{icon: 1});
					
					//重新刷新table
					layui.table.reload("table");
					
				}else{
					layer.msg(data.msg,{icon: 2});
				}
			}
		});
	});
	
}

function viewImage(obj){
	layer.open({
		type: 1,
		title: "查看图片",
		area: '1500px',
		offset: '100px',
		content: $("#viewImageModel").html()
	});
	
	$("#imageUrl_view").attr("src",$(obj).attr("src"));
}

function closeTask(obj){
	var projectTaskId = obj.projectTaskId.split("`")[0];
	var taskFlagCode = obj.taskFlagCode;
	$.ajax({
		url: "api/closeTask?token="+getToken(), 
		data : {"projectTaskId":projectTaskId,"projectTaskCode":obj.projectTaskCode,"taskFlagCode":taskFlagCode},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			layer.closeAll();
			//重新刷新table
			layui.table.reload("table");
		}
	});
}