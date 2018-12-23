$(function() {
	layui.use('upload', function(){
	  var upload = layui.upload;
	   
	  //执行实例
	  var uploadInst = upload.render({
	    elem: '#test1' //绑定元素
	    ,url: '/api/file/uploadFile/' //上传接口
	    ,accept:'file'	
	    ,done: function(res){
	    	debugger;
	    	if(res.code == 200){
	    		layer.msg("上传成功");
	    	}else{
	    		layer.msg(res.msg);
	    	}
	      //上传完毕回调
	    }
	    ,error: function(e){
	    	layer.msg(e);
	    }
	  });
	});
});

