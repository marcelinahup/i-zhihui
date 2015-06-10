// JavaScript Document Download by http://www.codefans.net
$(document).ready(
	function () {		
		//预先输出内容，页面加载完成好进行布局操作
		var Modules=new Array('module_l','module_m','module_r');//栏目
		var ModuleItems=new Array(['avatar','comment','doing'],['blog','profile','spaceinfo','friends'],['gallery','visitors','thread']);	//栏目包含的模块
		$.each( Modules, function(s, sn){
			$.each(ModuleItems[s], function(m, mn){
				$("#"+mn).appendTo($("#"+sn));//将模块内容布局到栏目				
			})
		});		
		$("#contentlist").empty();//摧毁元素基地,以免冲突
		$(".groupWrapper").sortable({
				connectWith: '.groupWrapper'
			});
		$(".groupWrapper").disableSelection();	
		$(".groupWrapper2").sortable({
				connectWith: '.groupWrapper'
			});
		$(".groupWrapper2").disableSelection();	
		$(".groupWrapper1").sortable({
				connectWith: '.groupWrapper'
			});
		$(".groupWrapper1").disableSelection();		
	}
);

