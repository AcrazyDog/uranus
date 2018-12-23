var testEditor;
var primaryId;
var knowledgeEditParam = "knowledgeEditParam";

$(function() {

	var param = getRequest(knowledgeEditParam)
	if(param != null && param != undefined){
		primaryId = param.id;
	}
	// 搜索按钮点击事件
	$("#searchBtn").click(function() {
		doSearch();
	});
	if (primaryId != null && primaryId != '') {
		initPage();
		// 清空参数
		clearParam("knowledgeEditParam");
	}else{
		initEdit();
	}

	$("#btnCancel").click(function() {
		cancel();
	});

});

// 取消
function cancel() {
	layer.confirm('确定取消，将跳转到知识列表页面', function(index) {
		layer.close(index);
		showPage("knowledge/knowledgelist.html");
	});
}

function initEdit(){
	testEditor = editormd("test-editormd", {
		width : "95%",
		height : "95%",
		path : "assets/libs/editor.md-master/lib/",
		theme : "dark",
		previewTheme : "dark",
		editorTheme : "pastel-on-dark",
//		markdown : md,
		codeFold : true,
		// syncScrolling : false,
		saveHTMLToTextarea : true, // 保存 HTML 到 Textarea
		searchReplace : true,
		// watch : false, // 关闭实时预览
		htmlDecode : "style,script,iframe|on*", // 开启 HTML 标签解析，为了安全性，默认不开启
		// toolbar : false, //关闭工具栏
		// previewCodeHighlight : false, // 关闭预览 HTML 的代码块高亮，默认开启
		emoji : true,
		taskList : true,
		tocm : true, // Using [TOCM]
		tex : true, // 开启科学公式TeX语言支持，默认关闭
		flowChart : true, // 开启流程图支持，默认关闭
		sequenceDiagram : true, // 开启时序/序列图支持，默认关闭,
		// dialogLockScreen : false, // 设置弹出层对话框不锁屏，全局通用，默认为true
		// dialogShowMask : false, // 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
		// dialogDraggable : false, // 设置弹出层对话框不可拖动，全局通用，默认为true
		// dialogMaskOpacity : 0.4, // 设置透明遮罩层的透明度，全局通用，默认值为0.1
		// dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为#fff
		imageUpload : true,
		imageFormats : [ "jpg", "jpeg", "gif", "png", "bmp", "webp" ],
		imageUploadURL : "./php/upload.php",
		onload : function() {
			console.log('onload', this);
		}
	});
}

// 保存
function doSave() {
	layer.load(1);
	debugger;
	var id = $("#id").val();
	var title = $("#title").val();
	var context = testEditor.getMarkdown();
	;
	var _method = "POST"
	if (id != null && id != '') {
		_method = "PUT";
	}
	$.ajax({
		url : "api/knowledge",
		data : {
			"title" : title,
			"context" : context,
			"id" : id,
			"_method" : _method
		},
		type : "POST",
		dataType : "JSON",
		success : function(data) {
			if (data.id != null && data.id != '') {
				$("#id").val(data.id)
			}
			layer.closeAll();
			if (data.code == 200) {
				layer.msg(data.msg, {
					icon : 1
				});
			} else {
				layer.msg(data.msg, {
					icon : 2
				});
			}
		}
	});
}

function initPage() {
	$.ajax({
		url : "api/knowledge/getKnowledgeInfo?token=" + getToken(),
		data : {
			"id" : primaryId
		},
		type : "POST",
		dataType : "JSON",
		success : function(data) {
			layer.closeAll('loading');
			if (data.code == 200) {
				var knowledge = data.knowledge;
				$("#id").val(knowledge.id);
				$("#title").val(knowledge.title);
				$("#content").val(knowledge.context);
				
				initEdit();
			} else {
				layer.msg(data.msg, {
					icon : 2
				});
			}
		}
	});
}