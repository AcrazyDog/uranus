//定义一个全局参数，为了能从在页面中传递参数
var rootParam ={};

//var sideNavExpand = true;  //导航栏是否展开
$(function() {
	if(getCurrentUser()==null){
		location.replace("login.html");
	}
	//切换导航栏按钮点击事件
	$("#switchNav").click(function(){
		var sideNavExpand = !$('body').hasClass('nav-mini');
		switchNav(!sideNavExpand);
	});
	//手机遮罩层点击事件
	$('.site-mobile-shade').click(function(){
		switchNav(true);
	});
});

//获取当前token
function getToken() {
	return localStorage.getItem("token");
}

//获取当前登录的user
function getCurrentUser(){
	return JSON.parse(localStorage.getItem("user"));
}

//设置选中导航栏
function activeNav(path_name){
	$(".layui-side ul.layui-nav li.layui-nav-item .layui-nav-child dd").removeClass("layui-this");
	$(".layui-side ul.layui-nav li.layui-nav-item").removeClass("layui-nav-itemed");
	var $a = $(".layui-side ul.layui-nav>li.layui-nav-item>.layui-nav-child>dd>a[href='#!"+path_name+"']");
	$a.parent("dd").addClass("layui-this");
	$a.parent("dd").parent("dl.layui-nav-child").parent("li.layui-nav-item").addClass("layui-nav-itemed");
	layui.element.render('nav', 'index-nav');
}

//折叠显示导航栏
function switchNav(expand){
	var sideNavExpand = !$('body').hasClass('nav-mini');
	if(expand==sideNavExpand){
		return;
	}
	if (!expand) {
        //$('.layui-side .layui-nav .layui-nav-item.layui-nav-itemed').removeClass('layui-nav-itemed');
        $('body').addClass('nav-mini');
    }else{
        $('body').removeClass('nav-mini');
    }
	$('.nav-mini .layui-side .layui-nav .layui-nav-item').hover(function(){
		var tipText = $(this).find('span').text();
		if($('body').hasClass('nav-mini')&&document.body.clientWidth>750){
			layer.tips(tipText, this);
		}
	},function(){
		layer.closeAll('tips');
	});
}

//导航栏展开
function openNavItem(){
	if($('body').hasClass('nav-mini')&&document.body.clientWidth>750){
		switchNav(true);
	}
}

function showPage(path){
	parent.$("#main-content").load("views/" + path,function(){
		layui.element.render('breadcrumb');
		layui.form.render('select');
	});
}

function getRequest(paramName) {   
  return rootParam[paramName];
}   

function clearParam(paramName){
	rootParam[paramName] ={};
}

//格式化代码函数,已经用原生方式写好了不需要改动,直接引用就好
var formatJson = function (json, options) {
    var reg = null,
            formatted = '',
            pad = 0,
            PADDING = '    ';
    options = options || {};
    options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
    options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;
    if (typeof json !== 'string') {
        json = JSON.stringify(json);
    } else {
        json = JSON.parse(json);
        json = JSON.stringify(json);
    }
    reg = /([\{\}])/g;
    json = json.replace(reg, '\r\n$1\r\n');
    reg = /([\[\]])/g;
    json = json.replace(reg, '\r\n$1\r\n');
    reg = /(\,)/g;
    json = json.replace(reg, '$1\r\n');
    reg = /(\r\n\r\n)/g;
    json = json.replace(reg, '\r\n');
    reg = /\r\n\,/g;
    json = json.replace(reg, ',');
    if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
        reg = /\:\r\n\{/g;
        json = json.replace(reg, ':{');
        reg = /\:\r\n\[/g;
        json = json.replace(reg, ':[');
    }
    if (options.spaceAfterColon) {
        reg = /\:/g;
        json = json.replace(reg, ':');
    }
    (json.split('\r\n')).forEach(function (node, index) {
                var i = 0,
                        indent = 0,
                        padding = '';

                if (node.match(/\{$/) || node.match(/\[$/)) {
                    indent = 1;
                } else if (node.match(/\}/) || node.match(/\]/)) {
                    if (pad !== 0) {
                        pad -= 1;
                    }
                } else {
                    indent = 0;
                }

                for (i = 0; i < pad; i++) {
                    padding += PADDING;
                }

                formatted += padding + node + '\r\n';
                pad += indent;
            }
    );
    return formatted;
};