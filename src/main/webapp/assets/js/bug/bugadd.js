$(function() {

	// 获得Bugcode
	 getAndSetBugCode();
	// 注册搜索框
	registerSearchInput();
	
	//监听提交
	form.on('submit(submitBtn)', function(param){
		$.ajax({
			url : "api/add/bug/addBug",
			type : "post",
			data : param,
			dataType : "JSON",
			success : function(data) {
				if(data.code == 200){
					layer.msg("新增成功");
					debugger;
					localStorage.setItem("bugInfo",param);
				}
			}
		});
	});

});

function getAndSetBugCode() {
	$.ajax({
		url : "api/add/bug/getBugCode",
		type : "get",
		dataType : "JSON",
		success : function(data) {
			debugger;
			$("#txtBugCode").val(data.msg);
		}
	});
}

function registerSearchInput() {

	registerProductTaskInput();
	registerSubSystemInput();

}

function registerProductTaskInput() {
	$.ajax({
		url : "api/add/bug/getAllProductTask",
		type : "get",
		dataType : "JSON",
		success : function(data) {

			var sourceData = [];
			for (var i = 0; i < data.length; i++) {
				sourceData.push(data[i].name);
				
				//暂时默认值 == 下一代云ERP_V1.0
				if(data[i].name == '下一代云ERP_V1.0'){
					$("#productName").val(data[i].name);
					$("#projectTaskId").val(data[i].id);
					$("#txtProdVer").val(data[i].version);
					$("#txtProdName").val(data[i].product);
				}
				
			}
			var input = document.getElementById("productName");
			new Awesomplete(input, {
				list : sourceData
			});

			$(input).on('awesomplete-selectcomplete', function() {
				// 给id赋值
				for (var i = 0; i < data.length; i++) {
					if (this.value == data[i].name) {
						debugger;
						$("#projectTaskId").val(data[i].id);
						$("#txtProdVer").val(data[i].version);
						$("#txtProdName").val(data[i].product);
						break;
					}
				}
			});
		}
	});
}
function registerSubSystemInput() {
	$.ajax({
		url : "api/add/bug/getAllSubSystem",
		type : "get",
		dataType : "JSON",
		success : function(data) {

			var sourceData = [];
			for (var i = 0; i < data.length; i++) {
				sourceData.push(data[i].group + "-" + data[i].name);
			}
			var input = document.getElementById("subSystemName");
			new Awesomplete(input, {
				list : sourceData,
			});

			$(input).on('awesomplete-selectcomplete', function() {
				// 给id赋值
				for (var i = 0; i < data.length; i++) {
					if (this.value == (data[i].group + "-" + data[i].name)) {
						$("#subSystemId").val(data[i].id);
						$("#groupId").val(data[i].groupId);
						$("#group").val(data[i].group);
						break;
					}
				}
				// 注册迭代阶段
				registerStageInput($("#groupId").val());
			});

		}
	});
}

function registerStageInput(id) {

	$.ajax({
		url : "api/add/bug/getStageByGroupId",
		data : {
			groupId : id
		},
		type : "get",
		dataType : "JSON",
		success : function(data) {
			var input = $("#ItStage");
			var sourceData = [];
			for (var i = 0; i < data.length; i++) {
				var option = "<option value ='" + data[i].id + "'>"
						+ data[i].name + "</option>";
				$(input).append(option)
			}

			layui.use('form', function() {
				var form = layui.form;// 高版本建议把括号去掉，有的低版本，需要加()
				form.render();
				form.on('select(ItStage)', function(data) {
					// 注册
					registerRelateProjectTaskName();
				});
			});
		}
	});
}

function registerRelateProjectTaskName() {

	var subSystem = {
		groupId : $("#groupId").val(),
		projectTaskId : $("#projectTaskId").val(),
		id : $("#subSystemId").val(),
		stageId : $("#ItStage").val()
	};

	$.ajax({
		url : "api/add/bug/getRelateProjectTask",
		data : subSystem,
		type : "POST",
		dataType : "json",
		success : function(data) {
			var sourceData = [];
			for (var i = 0; i < data.length; i++) {
				sourceData.push(data[i].name);
			}
			var input = document.getElementById("relateProjectTaskName");
			new Awesomplete(input, {
				list : sourceData,
			});
			$(input).on('awesomplete-selectcomplete', function() {
				// 给id赋值
				for (var i = 0; i < data.length; i++) {
					if (this.value ==  data[i].name) {
						$("#relateProjectTaskId").val(data[i].id);
						break;
					}
				}
			});
		}
	});
}

document.addEventListener('paste', function(event) {
	var items = (event.clipboardData || window.clipboardData).items;
	var file = null;
	if (items && items.length) {
		// 搜索剪切板items
		for (var i = 0; i < items.length; i++) {
			if (items[i].type.indexOf('image') !== -1) {
				file = items[i].getAsFile();
				break;
			}
		}
	} else {
		log.innerHTML = '<span style="color:red;">当前浏览器不支持</span>';
		return;
	}
	if (!file) {
		log.innerHTML = '<span style="color:red;">粘贴内容非图片</span>';
		return;
	}
	// 此时file就是我们的剪切板中的图片对象
	// 如果需要预览，可以执行下面代码
	var reader = new FileReader()
	reader.onload = function(event) {
		var image = '<img class="layui-col-md3" height="130px" src="'
				+ event.target.result + '" class="upload-image">';
		$("#preview").append(image);
	}
	reader.readAsDataURL(file);
	// 如果不需要预览，上面这段可以忽略

	var formData = new FormData();
	formData.append('file', file);
	$.ajax({
		url : 'api/file/uploadFileAndUploadDmp',
		type : 'POST',
		cache : false,
		data : formData,
		processData : false,
		contentType : false
	}).done(function(res) {
		$("#imageUrls").val($("#imageUrls").val() + ";" + res.msg);
	}).fail(function(res) {
		layer.msg(res.msg);
	});

});
