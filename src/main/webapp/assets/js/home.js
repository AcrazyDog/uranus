var layTable ;

$(function() {
	
	var array = new Array();
	var obj1 = {"elem":"#oneWeakCloserTable",url:"api/getOneWeakCloser"};
	var obj2 = {"elem":"#oneMonthCloserTable",url:"api/getOneMonthCloser"};
	var obj3 = {"elem":"#todayCloserTable",url:"api/getTodayCloser"};
	array.push(obj1);
	array.push(obj2);
	array.push(obj3);
	
	for(var i=0;i<array.length;i++){
		
		var obj = array[i];
		
		layui.table.render({
			elem : obj.elem,
			url : obj.url,
	 		where: {
		  		token : getToken()
			},
			cellMinWidth: 80,
			limit:50,
			page: false,
			cols: [[
		        {field:'name',align:'center',  title: '姓名',width:'50%', templet:function(d){ 
		        	if(d.LAY_INDEX == 1) {
		        		return '<span class="first" style="color:#FF5722">'+d.name+'</span>';
		        	} else if(d.LAY_INDEX == 2) {
		        		return '<span class="second" style="color:#FFB800">'+d.name+'</span>';
		        	}if(d.LAY_INDEX == 3) {
		        		return '<span class="third" style="color:#5FB878">'+d.name+'</span>';
		        	}else {
		        		return d.name
		        	}
		        }},
				{field:'number',width:'50%', align:'center', title: '收割数量'}
	    	]]
		});
	}
	
});