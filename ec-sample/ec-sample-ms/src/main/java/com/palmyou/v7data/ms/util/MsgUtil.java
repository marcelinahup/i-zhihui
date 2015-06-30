package com.palmyou.v7data.ms.util;

import com.hk.ec.fw.spring.ext.PropertiesUtils;

public class MsgUtil {
	
	public static String getMsg(String msgCode) {
		
		return msgCode + ":" + PropertiesUtils.getString(msgCode);
	}

}
