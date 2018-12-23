var primaryId ;

var knowledgeEditParam = "knowledgeEditParam";

$(function() {
	debugger;
	var param = getRequest(knowledgeEditParam)
	primaryId = param.id;
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		doSearch();
	});
	if(primaryId != null && primaryId != ''){
		initPage();
		//清空参数
		clearParam("knowledgeEditParam");
	}
	
	
	$("#btnCancel").click(function(){
		cancel();
	});
	
});

//取消
function cancel(){
	layer.confirm('确定取消，将跳转到知识列表页面', function(index){
		layer.close(index);
		showPage("knowledge/knowledgelist.html");
	});
}

//保存
function doSave(){
	layer.load(1);
	var id = $("#id").val();
	var title = $("#title").val();
//	var context = encodeURI($("#content").val());
	var context = $("#content").val();
	var _method = "POST"
	if(id != null && id !=''){
		_method = "PUT";
	}
	$.ajax({
		url: "api/knowledge", 
		data : {"title":title,"context":context,"id":id,"_method":_method},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			if(data.id != null && data.id !=''){
				$("#id").val(data.id)
			}
			layer.closeAll();
			if(data.code==200){
				layer.msg(data.msg,{icon: 1});
				//跳转到列表页面
//				showPage("knowledge/knowledgelist.html");
			}else{
				layer.msg(data.msg,{icon: 2});
			}
		}
	});
}

function compile(){
    var text = document.getElementById("content").value;
    var converter = new showdown.Converter();
    var html = converter.makeHtml(text);
    document.getElementById("result").innerHTML = html;
}

function initPage(){
	$.ajax({
		url: "api/knowledge/getKnowledgeInfo?token="+getToken(), 
		data : {"id":primaryId},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			layer.closeAll('loading');
			if(data.code==200){
				var knowledge = data.knowledge;
				$("#id").val(knowledge.id);
				$("#title").val(knowledge.title);
				$("#content").val(knowledge.context);
				var converter = new showdown.Converter();
			    var html = converter.makeHtml( knowledge.context);
			    document.getElementById("result").innerHTML =html;
			}else{
				layer.msg(data.msg,{icon: 2});
			}
		}
	});
}