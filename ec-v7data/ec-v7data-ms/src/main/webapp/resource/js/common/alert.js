(function(){

    var _alert = window.alert;
	
    MyAlert = function(alertMsg) {

        // add your own effect 
    	var msg = alertMsg;
    	var type = 11;
    	
    	if(alertMsg == null){
    		return;
    	// 没有:分割
    	}else if(typeof(alertMsg) == 'number'){
    		msg = "【调试】"+ alertMsg;
    	}else if(alertMsg.indexOf(":") == -1){
    		msg = "【调试】"+ alertMsg;
    	}else{
    		var msgCode = alertMsg.substring(0,alertMsg.indexOf(":"));
    		var msg = alertMsg.substring(alertMsg.indexOf(":") + 1,alertMsg.length);

    		var alertType = msgCode.substring(1, 2);
			
			if(alertType == "S"){
				type = 9;
			}if(alertType == "E"){
				type = 8;
			}
    	}
    	
    	layer.msg(msg, 3, {rate: '100px',
	        type: type,
	        shade: false})
    };
	
    MyAlert.noConflict = function() {
          window.alert = _alert;
    };

// expose API 
window.alert = window.MyAlert = MyAlert; 

})();

