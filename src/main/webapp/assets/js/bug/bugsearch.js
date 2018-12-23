var count ;
var isPage;
$(function() {
//	turnPage();
	// 搜索按钮点击事件
	$("#searchBtn").click(function() {
		doSearch(1);
	});
});

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

// 搜索
function doSearch(pageNo) {
	var queryStr = $("#queryStr").val();

	var size = 10;
	var from = (pageNo-1) * size;
	var param = {
		queryStr : queryStr,
		from : from,
		size : size
	};
	
	$.ajax({
		url : "api/searchBug?token=" + getToken(),
		data :param,
		type : "POST",
		dataType : "JSON",
		success : function(data) {
			count = data.count
			var bugList = data.data;
			$("#result").html("");
			//创建结果集
			for(var i =0;i<bugList.length;i++){
				var bug = bugList[i];
				
				var div='<div class="layui-card" style="margin-top:30px">'
					+'<div class="layui-card-header" style="height:28px;"><a style="cursor:pointer;" onclick="showBug(this)" rel="'+bug.bugId+'">'+bug.bugNo+'</a></div>'
					+'<div class="layui-card-body">'
					+bug.bugDesc
					+' </div>'
					+'</div>';
					
					$("#result").append($(div));
			}
			
			turnPage();
		}
	})
}


function turnPage(){
	var laypage = layui.laypage, layer = layui.layer;
	// 总页数大于页码总数
	laypage.render({
		elem : 'pager',
		count : count, // 数据总数，从服务端得到
		skip: true, //是否开启跳页
        skin: '#6665fe',
//        curr: curr || 1, //当前页
        jump: function(obj, first){ //触发分页后的回调
//			debugger;
			// obj包含了当前分页的所有参数，比如：
			console.log(obj.curr); // 得到当前页，以便向服务端请求对应页的数据。
			console.log(obj.limit); // 得到每页显示的条数

			if(!first){
				doSearch(obj.curr);
			}
			
		}
	});
}

function showBug(obj){
	debugger;
	var bugId = $(obj).attr('rel');
	var bugNo = $(obj).text();
	
	var param={"bugId":bugId,"bugNo":bugNo};
	
	doView(param);
}
