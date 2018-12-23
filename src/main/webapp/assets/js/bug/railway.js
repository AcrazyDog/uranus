$(function() {
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '/api/log/',
 		where: {
	  		token : getToken()
		},
		page: true,
		cols: [[
			{type:'numbers'},
			{field:'id', sort: true, title: 'ID'},
			{field:'bugNo', sort: true, title: 'BUG编号'},
			{field:'ip', sort: true, title: '操作IP'},
			{field:'bugCloser', sort: true, title: '操作人'},
			{field:'reason', sort: true, title: '原因'},
			{field:'logType', sort: true, title: '操作类型'},
			{field:'createTime', sort: true, templet:function(d){ return layui.util.toDateString(d.createTime); }, title: '操作时间'}
    	]]
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

//搜索
function doSearch(){
	var searchDate = $("#searchDate").val().split(" - ");
	var searchAccount = $("#searchAccount").val();
	layui.table.reload('table', {where: {startDate: searchDate[0], endDate: searchDate[1], account: searchAccount}});
}
