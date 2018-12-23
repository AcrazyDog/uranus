var count ;
var isPage;
$(function() {
//	turnPage();
	// 搜索按钮点击事件
	$("#searchBtn").click(function() {
		doSearch(1);
	});
});

function doView(id) {
	$.ajax({
		url : "api/knowledge/getKnowledgeInfo?token=" + getToken(),
		data : {
			"id" : id
		},
		
		type : "POST",
		dataType : "JSON",
		success : function(data) {
			layer.closeAll('loading');
			if (data.code == 200) {
				var knowledge = data.knowledge;
				layer.open({
					type : 1,
					title : "查看知识",
					area : '1300px',
					offset : 'auto',
					content : $("#viewKnowledgeModel").html()
				});
				var converter = new showdown.Converter();
				var html = converter.makeHtml(knowledge.context);
				document.getElementById("context").innerHTML = html;
			} else {
				layer.msg(data.msg, {
					icon : 2
				});
			}
		}
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
		url : "api/knowledge/searchKnowledge?token=" + getToken(),
		data :param,
		type : "POST",
		dataType : "JSON",
		success : function(data) {
			count = data.count
			var knowledageList = data.data;
			$("#result").html("");
			//创建结果集
			for(var i =0;i<knowledageList.length;i++){
				var knowledge = knowledageList[i];
				
				var div='<div class="layui-card" style="margin-top:30px">'
					+'<div class="layui-card-header" style="height:28px;"><a style="cursor:pointer;" onclick="showKnowledge(this)" rel="'+knowledge.id+'">'+knowledge.title+'</a></div>'
					+'<div class="layui-card-body">'
					+knowledge.context
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


function showKnowledge(obj){
	var id = $(obj).attr('rel');
	doView(id);
}
