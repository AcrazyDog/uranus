$(function() {
	
	//搜索按钮点击事件
	$("#checkSqlBtn").click(function(){
		doCheck();
	});
});


//搜索
function doCheck(){
	var sql = $("#sql").val();
	
	$.ajax({
		url: "api/check/sql/checkSql", 
		data : {"sql":sql},
		type: "POST", 
		dataType: "JSON", 
		success: function(data){
			layer.closeAll();
			if(data.code==200){
				layer.msg("SQL木有问题");
			}else{
				layer.open({
					  skin: 'demo-class',
					  type: 1,
					  content: data.msg,
					  area: ['750px', '300px']
					});  
//				layer.msg(data.msg);
			}
		}
	});
}

