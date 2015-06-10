var g={
	
	/* 使用jquery.validate验证表单数据格式
	 * @依赖于jquery.validate.js
	 * @param #form
	 * @param #errContainer
	 * @return boolean
	 */
	validateForm:function (form,errContainer) {

	   return $(form).validate({
			errorContainer: errContainer,
			errorLabelContainer: $("label", errContainer),
			//wrapper: 'li',
			
			// 自定义无效处理
			invalidHandler: function(form, validator) {
	      		var errors = validator.numberOfInvalids();
	      		var newErrorList = new Array();
	      		newErrorList.push(validator.errorList[0]);
	      		validator.errorList=newErrorList;
			}

		}).form();
	},
	
	/* 同步post方式发送ajax请求，传送json数据
	 * @param url
	 * @param paramArr ['k=v','k=v']
	 * @param callback
	 */	
	syncPost:function (url, paramArr) {
		
		var $form = $("<form action='" + url + "' method='post' ></form>");
        
        for(i=0;i<paramArr.length;i++){
        	
        	var entry = paramArr[i];
        	var kvArr = entry.split('=');
        	var name = kvArr[0];
        	var val = kvArr[1];
        	var $input = $("<input type='hidden' name='" + name + "' value='" + val + "' />");
        	$form.append($input);
        }
        
        $form.appendTo("body");
        $form.css('display','none');
        
        $form.submit();
	},
	
	/* post方式发送ajax请求，传送json数据
	 * @param url
	 * @param param {}
	 * @param callback
	 */	
	jsonPost:function (url, param, callback) {
		
		// 添加遮罩
		loadOPen();
		
		$.ajax({
			type : "post",
			url : url,
			dataType : "json",
			data : param,
			success : function(result){
				
				// 去掉遮罩
				loadClose();
				callback(result);
			},
			error : function(xhr) {
				// 去掉遮罩
				loadClose();
			}
		});
	},
	
	/* post方式发送ajax请求，提交表单数据
	 * @依赖于jquery.form.min.js
	 * @param #form
	 * @param url
	 * @param callback
	 */
	ajaxSubmit:function (form, url, callback){
	
		// 定义提交参数
		var ajaxOption = {
		    type: 'post',
		    url:url,
		    success:function(result){
				
				// 去掉遮罩
		    	loadClose();
				callback(result);
			},
			error : function() {
				// 去掉遮罩
				loadClose();
			}
	    };
		
		// 添加遮罩
		loadOPen();
		
	    // 提交表单
	    $(form).ajaxSubmit(ajaxOption);
	},

	/* check from输入项是否含有?<>非法字符
	 * @param formId
	 * @param unChkInputIdArray(类型必须是text或textarea)
	 * @result boolean true 没有非法字符
	 */
	checkForm:function (){
		var id=arguments[0];//form的id
		var Nocheck;//不被check的id
		var pattern = new RegExp("[?]+|(<)+|(>)+");//check的内容
		var typeText=$(id+" input[type=text]");//表单中text类型
		var typeTextarea=$(id+" textarea");//表单中textarea类型
		var n=typeText.length;//表单中text类型的个数
		var k=typeTextarea.length;//表单中textarea类型的个数
		var originalBoolean=false;//默认不匹配
		
		//有两个参数，第二个参数是数组
		if(arguments.length==2&&Object.prototype.toString.call(arguments[1]) === '[object Array]'){
			Nocheck=arguments[1];  //获取不被check的id数组
			var NocheckLength=Nocheck.length;//不被check的id个数
			var NocheckText=[];//不被check的表单中text类型
			var NocheckTextarea=[];//不被check的表单中textarea类型
			for(var f=0;f<NocheckLength;f++){//遍历传入的第二个参数数组，判断类型
				if($(Nocheck[f]).attr("type")=="text"){//id的类型为text
					NocheckText.push(Nocheck[f]);//不被check的text类型的id分配
				}
				if($(Nocheck[f]).is('textarea')){//id的类型为text
					NocheckTextarea.push(Nocheck[f]);//不被check的textarea类型的id分配
				}
				
			}
			
			for(var i=0;i<n;i++){
				for(var l=0;l<NocheckText.length;l++){
					var doubleCheck=$(id+" input[type=text]:not("+NocheckText[l]+")");//选择除不被check以外的text
					if( pattern.test($(doubleCheck[i]).val())){//匹配
						originalBoolean=true;
						break;
					}
				}
			}
			for(var j=0;j<k;j++){
				for(var m=0;m<NocheckTextarea.length;m++){
					var doubleCheckT=$(id+" textarea:not("+NocheckTextarea[m]+")");//选择除不被check以外的textarea
						if( pattern.test($(doubleCheckT[j]).val())){//匹配
							originalBoolean=true;
							break;
						}
				}
			}
			return originalBoolean;
		}
		
		//有两个参数，第二个参数是字符串
		else if(arguments.length==2&&(typeof arguments[1]=='string')&&arguments[1].constructor==String){
			Nocheck=arguments[1];  //获取不被check的id
			if($(Nocheck).attr("type")=="text"){//id的类型为text
				for(var i=0;i<n;i++){
					var doubleCheck=$(id+" input[type=text]:not("+Nocheck+")");//选择除不被check以外的text
					if( pattern.test($(doubleCheck[i]).val())){//匹配
						originalBoolean=true;
						break;
					}
				}
			}
			else if($(Nocheck).is('textarea')){//id的类型为textarea
				for(var j=0;j<k;j++){
					var doubleCheckT=$(id+" textarea:not("+Nocheck+")");//选择除不被check以外的textarea
					if( pattern.test($(doubleCheckT[j]).val())){//匹配
						originalBoolean=true;
						break;
					}
				}
			}
			return originalBoolean;
		}
		
		//参数大于2
		else if(arguments.length>2){
			Nocheck=[];
			for(var s=1;s<arguments.length;s++){//把不被check的id放入数组中
				Nocheck.push(arguments[s]);
			}
			var NocheckLength=Nocheck.length;
			var NocheckText=[];//不被check的text类型的数组
			var NocheckTextarea=[];//不被check的textarea类型的数组
			for(var f=0;f<NocheckLength;f++){//遍历传入的第二个参数数组，判断类型
				if($(Nocheck[f]).attr("type")=="text"){//id的类型为text
					NocheckText.push(Nocheck[f]);//不被check的text类型的id分配
				}
				if($(Nocheck[f]).is('textarea')){//id的类型为text
					NocheckTextarea.push(Nocheck[f]);//不被check的textarea类型的id分配
				}
				
			}
			
			for(var i=0;i<n;i++){
				for(var l=0;l<NocheckText.length;l++){
					var doubleCheck=$(id+" input[type=text]:not("+NocheckText[l]+")");//选择除不被check以外的text
					if( pattern.test($(doubleCheck[i]).val())){//匹配
						originalBoolean=true;
						break;
					}
				}
			}
			for(var j=0;j<k;j++){
				for(var m=0;m<NocheckTextarea.length;m++){
					var doubleCheckT=$(id+" textarea:not("+NocheckTextarea[m]+")");//选择除不被check以外的textarea
						if( pattern.test($(doubleCheckT[j]).val())){//匹配
							originalBoolean=true;
							break;
						}
				}
			}
			return originalBoolean;
		}
		
		//只有一个参数form的id
		else{
		
			for(var i=0;i<n;i++){
				if( pattern.test($(typeText[i]).val())){//匹配
					originalBoolean=true;
					break;
				}
			}
			for(var j=0;j<k;j++){
				if( pattern.test($(typeTextarea[j]).val())){//匹配
					originalBoolean=true;
					break;
				}
			}
			return originalBoolean;
		}
	},
	checkInput:function(obj){//检测当前是否匹配
		var pattern = new RegExp("[?]+|(<)+|(>)+");//check的内容
		if(pattern.test($(obj).val())){
			$(obj).addClass('errorText');
			return $(obj);//返回这个对象
		}else{
			$(obj).removeClass('errorText');
			return $(obj);
		}
	},
	bindBlur:function (blurSelector,fuc){//绑定blur事件
		$(blurSelector).each(function(){
			$(this).blur(function(){
				fuc(g.checkInput(this));
			});
		});
	},
	alertText:function (addr){
		var addStr='<label>输入项含有非法字符：?<></label>';
		if(addr.hasClass('errorText') && $(addr).next().length==0){
			$(addr).after(addStr);
		}else if(!addr.hasClass('errorText') && $(addr).next().length==1){
			$(addr).next().remove();
		}
	},
	/* 初始化绑定from表单onblur check输入项是否含有?<>非法字符
	 * @param formId
	 * @param alertMsgFun 消息弹出方式
	 * @param unChkInputIdArray 不需要check的表单元素数组或单个表单【可以不传参】(类型必须是text或textarea)
	 */
	initCheckForm:function (){
		var id=arguments[0];//form的id
		var func=g.alertText;//弹出的方法
		var Nocheck;//不被check的id
		
		//有一个参数
		if(arguments.length==1){
			g.bindBlur(id+" input[type=text]",func);
			g.bindBlur(id+" textarea",func);
		}
		
		//有两个参数，第两个参数是数组
		if(arguments.length==2&&Object.prototype.toString.call(arguments[1]) === '[object Array]'){
			Nocheck=arguments[2];  //获取不被check的id数组
			var NocheckLength=Nocheck.length;//不被check的id个数
			var NocheckText=[];//不被check的表单中text类型
			var NocheckTextarea=[];//不被check的表单中textarea类型
			for(var f=0;f<NocheckLength;f++){//遍历传入的第三个参数数组，判断类型
				if($(Nocheck[f]).attr("type")=="text"){//id的类型为text
					NocheckText.push(Nocheck[f]);//不被check的text类型的id分配
				}
				if($(Nocheck[f]).is('textarea')){//id的类型为text
					NocheckTextarea.push(Nocheck[f]);//不被check的textarea类型的id分配
				}
			}
			for(var l=0;l<NocheckText.length;l++){
				g.bindBlur(id+" input[type=text]:not("+NocheckText[l]+")",func);
			}
			for(var m=0;m<NocheckTextarea.length;m++){
				g.bindBlur(id+" textarea:not("+NocheckTextarea[m]+")",func);
			}
		}
		//有两个参数，第两个参数是字符串
		else if(arguments.length==2&&(typeof arguments[1]=='string')&&arguments[1].constructor==String){
			Nocheck=arguments[1];  //获取不被check的id
			if($(Nocheck).attr("type")=="text"){//id的类型为text
				g.bindBlur(id+" input[type=text]:not("+Nocheck+")",func);
			}
			else if($(Nocheck).is('textarea')){//id的类型为textarea
				g.bindBlur(id+" textarea:not("+Nocheck+")",func);
			}else{
				g.bindBlur(id+" input[type=text]",func);
				g.bindBlur(id+" textarea",func);
			}
		}
		//参数大于2
		else if(arguments.length>2){
			Nocheck=[];
			for(var s=1;s<arguments.length;s++){//把不被check的id放入数组中
				Nocheck.push(arguments[s]);
			}
			var NocheckLength=Nocheck.length;
			var NocheckText=[];//不被check的text类型的数组
			var NocheckTextarea=[];//不被check的textarea类型的数组
			for(var f=0;f<NocheckLength;f++){//遍历传入的第二个参数数组，判断类型
				if($(Nocheck[f]).attr("type")=="text"){//id的类型为text
					NocheckText.push(Nocheck[f]);//不被check的text类型的id分配
				}
				if($(Nocheck[f]).is('textarea')){//id的类型为text
					NocheckTextarea.push(Nocheck[f]);//不被check的textarea类型的id分配
				}
				
			}
			for(var l=0;l<NocheckText.length;l++){
				g.bindBlur(id+" input[type=text]:not("+NocheckText[l]+")",func);
			}
			for(var m=0;m<NocheckTextarea.length;m++){
				g.bindBlur(id+" textarea:not("+NocheckTextarea[m]+")",func);
			}
		}
	}
}