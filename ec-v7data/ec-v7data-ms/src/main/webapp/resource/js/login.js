function callback(result){
    	
    if(result.ok){
    	window.location=springUrl + "/home.html";
    }else{
    	alert(result.message);
    }
    
}

$(function(){ 
	
	g.initCheckForm('#loginForm');
	
    // 保存按钮点击事件
    $('#btnLogin').on('click',function(){
    	
        // 1,特殊字符过滤  false： 无特殊字符  true：有特殊字符
    	if(g.checkForm('#loginForm')){
            return;
        }
        
        var url = springUrl + '/doLogin.html';
        // 2,表单验证
        if(g.validateForm('#loginForm','#errorMsg')){
        	
        	// 3,提交表单
            g.ajaxSubmit('#loginForm',url,callback);
        }
    });
});
