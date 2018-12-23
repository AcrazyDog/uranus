var layTable ;
var role =localStorage.getItem("role");
debugger;
$(function() {
	
	var url = "api/getBugList";
	var name = $("#name").val();
	if(name!=null && name !=undefined && name !=''){
		url = "api/queryBugListByParam";
	}
	
	//渲染表格
	var layTable =layui.table.render({
		elem : '#table',
		url : 'api/getBugList',
 		where: {
	  		token : getToken(),
	  		name : name
		},
		cellMinWidth: 80,
		limit:90,
		page: true,
		cols: [[
			{type:'checkbox'},
			{field:'txtSubSys', sort: true, title: '子系统',width:120},
			{field:'group',width:80, sort: true, title: '项目组'},
			{field:'tester', sort: true, title: '测试员',width:80},
			{field:'testTime', sort: true, title: '测试时间',width:180},
			{field:'bugDesc', sort: true, title: '任务名称'},
			{field:'bugNo', sort: true, title: '任务编号',width:120},
			{field:'discoveryTime', sort: true, title: '迭代阶段',width:120},
			{field:'priority',align:'center', sort: true, title: '优先级',width:80, templet:function(d){ 
				if(d.priority == "高") {
					return "<font style='color:red'>"+d.priority+"<font>";
				} else{
					return d.priority;
				}
			}},
			{field:'nextHandleUserName', sort: true, title: '下一步处理人',width:150},
			{align:'center', toolbar: '#barTpl', minWidth: 150, title: '操作'}
    	]]
	});
	
	//添加按钮点击事件
	$("#addBtn").click(function(){
		layer.msg("没有权限！");
		return;
	});
		
	//添加批量关闭BUG按钮点击事件
	$("#closeAllBtn").click(function(){
		closeChooseBug();
	});
	//添加批量转发BUG事件
	$("#forwardAllBtn").click(function(){
		doForward(null,1);
	});
	
	//工具条点击事件
	layui.table.on('tool(table)', function(obj){
		var data = obj.data;
		var layEvent = obj.event;
 
		if(layEvent === 'view'){ //查看
			doView(data);
		} else if(layEvent === 'close'){ //删除
			closeBugModel(data);
			
		} else if(layEvent === 'forward'){ //转发
			doForward(obj.data,2);
		} else if(layEvent === 'pass'){
			doPass(obj.data);
		}
	});
	
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		doSearch(table);
	});
});

//显示关闭bug框
function closeBugModel(data){
	layer.open({
		type: 1,
		title: "关闭BUG",
		area: '550px',
		offset: '200px',
		content: $("#closeBugModel").html()
	});
	$("#bugNo").val(data.bugNo);
	
	//添加按钮点击事件
	$("#cancelCloseBug").click(function(){
		layer.closeAll();
	});
	
	//默认修改原因：
	var closeReason = "出现原因：\n"+"修改内容：\n"+"影响范围：\n"+"测试要点：\n ";
	
	$("#fOpinion").val(closeReason);
	
	$("#saveCloseBug").click(function(){
		layer.load(1);
		var bugNo = $("#bugNo").val();
		var fOpinion = $("#fOpinion").val();
		$.ajax({
			url: "api/closeBug?token="+getToken(), 
			data : {"bugNo":bugNo,"fOpinion":fOpinion},
			type: "POST", 
			dataType: "JSON", 
			success: function(data){
				layer.closeAll();
				//重新刷新table
				layui.table.reload("table");
			}
		});
	});
}

//查看
function doView(obj){
	layer.load(1);
	$.ajax({
		url: "api/getBugDetail?token="+getToken(), 
		data : {"bugNo":obj.bugNo,"bugId":obj.bugId},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			layer.closeAll('loading');
			if(data.code==200){
				var bug = data.bug;
				layer.open({
					type: 1,
					title: "查看bug",
					area: '1000px',
					offset: '90px',
					content: $("#bugDetail").html()
				});
				$("#nextHandleUserName").val(bug.nextHandleUserName);
				$("#keyWord").val(bug.keyWord);
				$("#operateStep").val(bug.operateStep);
				$("#detail").val(bug.detail);
				$("#environment").val(bug.environment);
				$("#imageUrl_a").attr("href",bug.imageUrl);
				$("#imageUrl_img").attr("src",bug.imageUrl);
				$("#imageUrl_img2").attr("src",bug.imageUrl2);
				$("#imageUrl_img3").attr("src",bug.imageUrl3);
				$("#imageUrl_img4").attr("src",bug.imageUrl4);
				
				showHandleDetail(bug.handleList);
				
				
			}else{
				layer.msg(data.msg,{icon: 2});
			}
		}
	});
}

function showHandleDetail(data){
	layui.table.render({
		elem : '#handledetail',
		data : data,
 		where: {
	  		token : getToken()
		},
		height:250,
		cellMinWidth: 80,
		limit:90,
		page: false,
		cols: [[
			{field:'seq', sort: false, title: '序号',width:50},
			{field:'nodeName',width:80, sort: false, title: '节点名称'},
			{field:'operateName', sort: false, title: '操作',width:60},
			{field:'status', sort: false, title: '对象状态',width:100},
			{field:'handlerName', sort: false, title: '处理人'},
			{field:'receiverName', sort: false, title: '接受人',width:90},
			{field:'handleTime', sort: false, title: '处理时间',width:150},
			{field:'handleRemark', sort: true, title: '处理意见',width:200}
    	]]
	});
}


//搜索
function doSearch(table){
	var name = $("#name").val();
	layui.table.reload('table', {url : 'api/queryBugListByParam',where: {"name":name}});
}

//转发
function doForward(data,type){
	
	layer.open({
		type: 1,
		title: "转发BUG",
		area: '550px',
		offset: '200px',
		content: $("#forwardBugModel").html()
	});
	if(data != null){
		$("#bugNo_forward").val(data.bugNo);
	}
	$("#forwardType").val(type);
	
	//转发BUG表单提交事件
	$("#sureForwardBug").click(function(){
		submitForwardBug();
	});
}

function submitForwardBug(){
	var bugNo = $("#bugNo_forward").val();
	var forwardUserName = $("#forwardUserName").val();
	var fOpinion = $("#fOpinion").val();
	if(forwardUserName == null || forwardUserName == "" || forwardUserName ==undefined){
		layer.alert("请输入转发人");
		return;
	}
	layer.load(1);
	
	var type = $("#forwardType").val();
	
	if(type == 1){
		forwardChooseBug(forwardUserName);
	}else{
		forwardOneBug(bugNo,forwardUserName,fOpinion);
	}
}

//验证通过
function doPass(data){
	
	layer.open({
		type: 1,
		title: "验证通过",
		area: '550px',
		offset: '200px',
		content: $("#passBugModel").html()
	});
	if(data != null){
		$("#bugNo_pass").val(data.bugNo);
	}
	
	var bugNo = $("#bugNo_pass").val();
	var fOpinion = $("#fOpinion_pass").val();
	
	//转发BUG表单提交事件
	$("#surePassBug").click(function(){
		submitPassBug(bugNo,fOpinion);
	});
}

function submitPassBug(bugNo,fOpinion) {
	$.ajax({
		url: "api/passBug?token="+getToken(), 
		data : {"bugNo":bugNo,"fOpinion":fOpinion},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			layer.closeAll();
			if(data.code==200){
				//重新刷新table
				layui.table.reload("table");
			}else{
				layer.msg(data.msg,{icon: 2});
			}
		},
		error:function(data){
			layer.closeAll();
			debugger;
			alert(data.message);
		}
	});
}

/**
 * 转发BUG
 * @param bugNo
 * @param forwardUserName
 */
function forwardOneBug(bugNo,forwardUserName,fOpinion) {
	$.ajax({
		url: "api/forwardBug?token="+getToken(), 
		data : {"bugNo":bugNo,"forwardUserName":forwardUserName,"fOpinion":fOpinion},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			layer.closeAll();
			if(data.code==200){
				//重新刷新table
				layui.table.reload("table");
			}else{
				layer.msg(data.msg,{icon: 2});
			}
		},
		error:function(data){
			layer.closeAll();
			debugger;
			alert(data.message);
		}
	});
}

//转发选中bug
function forwardChooseBug(forwardUserName){
	
	var checkedBugs = layui.table.checkStatus("table").data;
	if(checkedBugs.length < 1){
		layer.closeAll();
		layer.alert("至少选择一条记录");
		return;
	}
	var bugNos ="";
	for(var i =0;i<checkedBugs.length;i++){
		bugNos +=checkedBugs[i].bugNo +",";
	}
	
	layer.confirm('确定要转发吗？', function(index){
		layer.close(index);
		layer.load(1);
		$.ajax({
			url: "api/forwardManyBugs?token="+getToken(), 
			data : {"bugNos":bugNos,"forwardUserName":forwardUserName},
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

function closeChooseBug(){
	var checkedBugs = layui.table.checkStatus("table").data;
	if(checkedBugs.length < 1){
		layer.alert("至少选择一条记录");
		return;
	}
	var bugNos ="";
	for(var i =0;i<checkedBugs.length;i++){
		bugNos +=checkedBugs[i].bugNo +",";
	}
	
	layer.confirm('确定要关闭吗？', function(index){
		layer.close(index);
		layer.load(1);
		$.ajax({
			url: "api/closeManyBugs?token="+getToken(), 
			data : {"bugNos":bugNos},
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
	  var img = '<img src="'+$(obj).attr("src")+'">';
	  layer.open({
	    type: 2,//Page层类型
	    area: ['1500px', '900px'],
	    title: '图片预览',
	    shade: 0.6 ,//遮罩透明度
	    maxmin: true ,//允许全屏最小化
	    anim: 1 ,//0-6的动画形式，-1不开启
	    content: $(obj).attr("src")
	  }); 
}



function adapt(){ 
	var tableWidth = $("#imgDiv").width(); //表格宽度 
	var img = new Image(); 
	img.src =$('#imageUrl_view').attr("src") ; 
	var imgWidth = img.width; //图片实际宽度 
	var imgHeight = img.height; //图片实际宽度 
	
	if(imgWidth<imgHeight){ 
		$('#imageUrl_view').attr("style","width: "+imgWidth+";height:"+imgHeight+"\""); 
	}
} 



