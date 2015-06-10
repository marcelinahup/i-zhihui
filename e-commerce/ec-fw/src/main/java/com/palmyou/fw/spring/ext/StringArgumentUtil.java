package com.palmyou.fw.spring.ext;

public class StringArgumentUtil {
	
	private StringArgumentUtil(){}
	
	/**
	 * 替换html标签特殊字符
	 * @param str
	 * @return str
	 */
	public static String replaceHtmlCh(String str){
		
//		str = str.replaceAll("&", "&amp;");
//		str = str.replaceAll("<", "&lt;");
//		str = str.replaceAll(">", "&gt;");
//		str = str.replaceAll("\"", "&quot;");
//        str = str.replace("\\","&#39;");   
//        str = str.replace("\"","&quot;");
		return str;
	}
	
	/**
	 * 使字符串第一个字符小写
	 * @param target
	 * @return
	 */
	public static String firstCharToLow(String target){
		String firstChar = null;
		if(target != null && !"".equalsIgnoreCase(target)){
			firstChar = target.substring(0, 1);
			target = firstChar.toLowerCase() + target.substring(1);
		}
		return target;
	}
	
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
