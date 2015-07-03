package com.hk.commons.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountUtils {

	public static final String DEFAULT_MANTISSA_PROCESS = "DEFAULT_MANTISSA_PROCESS";
	/**
	 * 金额格式转换
	 * 
	 * @param o
	 * @return
	 */
	public static String format(Object o){
		if(o == null){
			return null;
		}
		if("".equals(o)){
			return "";
		}
		//金额小数尾数, 统一保留2位小数
		int d = Integer.valueOf("2");
		String temp = String.valueOf((int)Math.pow(10,d)).replace("1", "");
		DecimalFormat format = null;
		if(temp.length() == 0){
			format = new DecimalFormat("#,##0.#");
		}else{
			format = new DecimalFormat("#,##0." + temp);
		}
		format.setRoundingMode(RoundingMode.HALF_UP);
		//double amount = Double.valueOf(String.valueOf(o));
		return format.format(new BigDecimal(String.valueOf(o)));
	}

	/**
	 * 数字保留两位小数
	 * @param o
	 * @return
	 */
	public static String numFormat(Object o){
		String str = "";
		str = format(o);
		if(str != null){
			str = str.replaceAll(",", "");
		}
		return str;
	}
	
	/**
	 * 金额格式转换
	 * 
	 * @param o
	 * @return
	 */
	public static Double parseToDouble(String amout){
		if(amout == null) return null;
		String s = amout.replaceAll(",", "");
		return Double.valueOf(s);
	}
	
	
	//将很长的小数格式掉
	public static double foramtDouble(Object o){
		if (o ==null || o.equals("")){
			return 0;
		}
		String amount = format(o.toString().replace(",", ""));
		return parseToDouble(amount).doubleValue();
	}

	//将很长的小数格式掉
	public static BigDecimal foramtBigDecimal(Object o){
		return BigDecimal.valueOf(foramtDouble(o));
	}

	/**
	 * 金额格式转换
	 * 
	 * @param o
	 * @return
	 */
	public static BigDecimal parseToBigDecimal(String amout){
		if(amout == null) return null;
		return BigDecimal.valueOf(parseToDouble(amout));
	}
	

	/**
	 * 位数处理
	 * 
	 * @param number
	 * @param mantissaProcess
	 * 		1: 舍厘见分进角
	 * 		2: 四舍五入到分
	 * 		3: 四舍五入到角
	 * 		4: 四舍五入到圆
	 * @return
	 */
	/*
	public static BigDecimal mantissaProcessNum(BigDecimal number, String mantissaProcess){
		if(number == null)
			return null;
		BigDecimal newNumber = number;
		if(CommonConstants.MANTISSA_PROCESS_1.equals(mantissaProcess)){
			// 舍厘见分进角
			newNumber = number.setScale(2, BigDecimal.ROUND_DOWN);		// 舍去分以后的位数
			newNumber = newNumber.multiply(BigDecimal.TEN);
			newNumber = newNumber.divide(BigDecimal.TEN, 1, BigDecimal.ROUND_CEILING);
			logger.debug(Math.ceil(newNumber.doubleValue()*10)/10);
		}else if (CommonConstants.MANTISSA_PROCESS_2.equals(mantissaProcess)){
			// 四舍五入到分
			newNumber = number.setScale(2, BigDecimal.ROUND_HALF_UP);
		}else if (CommonConstants.MANTISSA_PROCESS_3.equals(mantissaProcess)){
			// 四舍五入到角
			newNumber = number.setScale(1, BigDecimal.ROUND_HALF_UP);
		}else if (CommonConstants.MANTISSA_PROCESS_4.equals(mantissaProcess)){
			// 四舍五入到圆
			newNumber = number.setScale(0, BigDecimal.ROUND_HALF_UP);
		}else if (DEFAULT_MANTISSA_PROCESS.equals(mantissaProcess)){
			// 系统默认位数位处理
			int d = Integer.valueOf(CommonConstants.AMOUNT_DECIMAL);
			newNumber = number.setScale(d, BigDecimal.ROUND_HALF_UP);
		}
		
		return newNumber;
	}
	*/
	
	/**
	 * 验证是否为有效金额
	 * 
	 * @param amount
	 * @return
	 */
	public static boolean valid(String amount){
		Pattern pattern = Pattern.compile("^(-)?(\\d*)(\\.(\\d*))?$");
		Matcher matcher = pattern.matcher(amount); 
		return matcher.matches();
	}
	
	/**
	 * BigDecimal值累加
	 * 
	 * @param b
	 * @param item
	 * @return
	 */
	public static BigDecimal sumBigDecimal(BigDecimal b, BigDecimal... item){
		if(b == null) return null;
		BigDecimal r = b;
		if(item != null){
			for(BigDecimal i : item){
				r = r.add(i);
			}
		}
		return r;
	}
	
	/**
	 * BigDecimal值累减
	 * 
	 * @param b
	 * @param item
	 * @return
	 */
	public static BigDecimal subtractBigDecimal(BigDecimal b, BigDecimal... item){
		if(b == null) return null;
		BigDecimal r = b;
		if(item != null){
			for(BigDecimal i : item){
				r = r.subtract(i);
			}
		}
		return r;
	}
	
	/**
	 * 金额格式转换 保留整数，如果为空，返回""
	 * 
	 * @param o
	 * @return
	 */
	public static String formatSei(Object o){
		if(o == null){
			return "";
		}
		if("".equals(o)){
			return "";
		}
		//金额小数尾数, 统一保留2位小数
		int d = Integer.valueOf("0");
		String temp = String.valueOf((int)Math.pow(10,d)).replace("1", "");
		DecimalFormat format = null;
		if(temp.length() == 0){
			format = new DecimalFormat("#,##0.#");
		}else{
			format = new DecimalFormat("#,##0." + temp);
		}
		format.setRoundingMode(RoundingMode.HALF_UP);
		//double amount = Double.valueOf(String.valueOf(o));
		return format.format(new BigDecimal(String.valueOf(o)));
	}

	
}
