	loadCss();//一旦引入这个js,就引入这个相对应的css
var loadShow='<div class="outerLoad"></div>';
function loadOPen(){//loading开始打开
	var n = arguments.length;
	var time;
	if(n == 0){
		time = 300;
	}else if(n == 1 && !isNaN(n)){
		time = arguments[0];
	}
	setTimeout($('body').append(loadShow),time);
	$('.loadShow').append('<span class="innerLoad"></span>');
}
function loadClose(){//loading关闭
	$('.outerLoad').fadeOut().remove();
}
function loadCss(){//获取css的相对路径，并把css加到页面上
	var js=document.scripts;
	var jsPath;
	for(var i=js.length;i>0;i--){
 		if(js[i-1].src.indexOf("loading.js")>-1){
   			jsPath=js[i-1].src.substring(0,js[i-1].src.lastIndexOf("/")+1);
 		}
	}
	var alertCss='<link href="'+jsPath+'loading.css" rel="stylesheet" type="text/css" />';
	$('head').find('link[ href="'+jsPath+'alertInfo.css"]').remove();
	$('head').append(alertCss);
}