package com.hk.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 简单的正则表达式处理类
 * author:yulu
 * date: 2014/04/09
 */
public class RegexUtils {
	/*
	 * 从所给字符串 中 找出符合正则的数组 字符串
	 */
	public static List<String> getPatternStrListFromString(String str,String pattern){
		Pattern mpattern = Pattern.compile(pattern);
		
		Matcher mmatcher = mpattern.matcher(str);
		
		List<String> arrayList = new ArrayList<String>();
		
		while(mmatcher.find()){
			//System.out.println("str:"+mmatcher.group());
			arrayList.add(mmatcher.group());
		}
		
		return arrayList;
	
	}
	
	/*
	 * 查看字符串中 是否有符合 正则表达式规则的 子串
	 */
	public static boolean isFindLikeStr(String regex,String testStr){
		Pattern mpattern = Pattern.compile(regex);
		Matcher mmatcher = mpattern.matcher(testStr);
		
		while(mmatcher.find()){
			
				return true;
			
		}
		return false;
		
	}
	
	//返回符合正则表达式的串的内容，如果没有，返回null
	public static String getFindStr(String regex,String testStr){
		Pattern mpattern = Pattern.compile(regex);
		Matcher mmatcher = mpattern.matcher(testStr);
		
		while(mmatcher.find()){
				return mmatcher.group();
		}
		return null;
		
	}
	
	
	/*
	 * 判断 一个字符是否符合该正则表达式
	 */
	public static boolean isFind(String regex,String testStr){
		Pattern mpattern = Pattern.compile(regex);
		Matcher mmatcher = mpattern.matcher(testStr);
		
		while(mmatcher.find()){
			if(mmatcher.group().equals(testStr)){
				return true;
			}
		}
		return false;
		
	}
	
}
