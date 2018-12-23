layui.use(['form'], function() {
	var form = layui.form;
	
	//checkLogin();
	//提交
	form.on('submit(LAY-user-login-submit)', function(obj) {
		obj.field.verkey = codeKey;
		layer.load(1);
		$.post("api/login", obj.field, function(data) {
			if (data.code == 200) {
				localStorage.setItem("token", data.token);
				localStorage.setItem("user", JSON.stringify(data.user));
				localStorage.setItem("role",$("#role").val());
				location.replace("index.html");
			} else {
				layer.closeAll('loading');
				layer.msg(data.msg,{icon: 2});
			}
		}, "json");
	});
	
	//getCode();  //获取验证码
	$("#LAY-user-get-vercode").click(function(){
		getCode();
	});
});

//获取验证码
var codeKey = null;
function getCode(){
	if(codeKey==null){
		codeKey = guid();
	}
	$("#LAY-user-get-vercode").attr("src","image/captcha?codeKey="+codeKey+"&n="+Math.random());
}

//检查是否登录
function checkLogin(){
	var tempUser = JSON.parse(localStorage.getItem("user"));
	if (tempUser != null) {
		location.replace("index.html");
	}
}

//生成uuid
function guid() {
    function S4() {
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}

//回车事件
document.onkeydown = function (event) {
    var e = event || window.event;
    if (e && e.keyCode == 13) { //回车键的键值为13
        $("#logonBtn").click(); //调用登录按钮的登录事件
    }
}; 