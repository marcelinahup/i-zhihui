package com.hk.ec.common.office;

import java.util.Vector;

public class HtmlElementsGetter {
	/*
	 * author: yulu
	 * date: 2011/7/1
	 * return String[]
	 * Example: 
	 * input:
	 * <a href="1.html">aa</a><div id="div_1">bb</div>
	 * return : String[0]:aa
	 * String[1]:bb
	 */
	public static Vector<String> getElementsByHtmlStr(String htmlstr){
		Vector<String> v=new Vector<String>();
		htmlstr=htmlstr.trim();
		String str="";
		boolean copy=true;
		int size=htmlstr.length();
		for(int i=0;i<size;i++){
			if(htmlstr.charAt(i)=='<'){
				copy=false;
				if(str.length()>0){
					if(str.trim().length()>0){
						 v.add(str.trim());   // don't add "";
					}	  
				}
				continue;
			}
			if(htmlstr.charAt(i)=='>'){
				copy=true;
				str="";
				continue;
			}
			if(copy){
				str+=htmlstr.charAt(i);
			}
		}
		if(copy){
			if(str.trim().length()>0){
				v.add(str.trim());
			}	
		}
		// print elements
		/*for(int i=0;i<v.size();i++){
			System.out.println(i+" : "+v.get(i));
		}*/
		return v;
	}
	
	// may only contain [br /], it is no need to add 
	public static Vector<String> getElementsByHtmlStr2(String htmlstr){
	//	System.out.println("do in th this!");
		Vector<String> v=new Vector<String>();
		htmlstr=htmlstr.trim();
		String str="";
		boolean copy=true;
		int size=htmlstr.length();
		for(int i=0;i<size;i++){
			if(htmlstr.charAt(i)=='<'){
				copy=false;
				if(str.length()>6){
					if(str.trim().length()>6){
						 v.add(str.trim());   // don't add "";
					}	  
				}
				continue;
			}
			if(htmlstr.charAt(i)=='>'){
				copy=true;
				str="";
				continue;
			}
			if(copy){
				str+=htmlstr.charAt(i);
			}
		}
		if(copy){
			if(str.trim().length()>6){
				v.add(str.trim());
			}	
		}
		// print elements
		/*for(int i=0;i<v.size();i++){
			System.out.println(i+" : "+v.get(i));
		}*/
		return v;
	}
}
