package com.hk.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 时间日期工具类
 * 
 * @author James
 * 
 */
public class DateTimeUtils {
	/**
	 * 日期时间格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String dateTimeString = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期格式 yyyy-MM-dd
	 */
	public static String dateString = "yyyy-MM-dd";
	
	/**
	 * 日期时间格式For 文件名 yyyy_MM_dd_HH_mm_ss
	 */
	public static String dateTimeString4FileName = "yyyy_MM_dd_HH_mm_ss";
	
	public final static String YYYY = "yyyy";
	public final static String YYYY_MM = "yyyy-MM";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	public final static String YYYY_MM_DD_HH_mm = "yyyy-MM-dd HH:mm";
	public final static String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public final static String YYYY_MM_DD_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
	
	/**
	 * 日
	 */
	public final static int INTERVAL_DAY = 1;
	/**
	 * 周
	 */
	public final static int INTERVAL_WEEK = 2;
	/**
	 * 月
	 */
	public final static int INTERVAL_MONTH = 3;
	/**
	 * 年
	 */
	public final static int INTERVAL_YEAR = 4;
	/**
	 * 小时
	 */
	public final static int INTERVAL_HOUR = 5;
	/**
	 * 分钟
	 */
	public final static int INTERVAL_MINUTE = 6;
	/**
	 * 秒
	 */
	public final static int INTERVAL_SECOND = 7;
	
	private static final Logger logger = Logger.getLogger(DateTimeUtils.class);
	
	/**
	 * 解决日期字符串,日期格式为： yyyy-MM-dd HH:mm:ss
	 * @param dateStr
	 * @return
	 */
	public static Date parseStr(String dateStr){
		return parseStr(dateStr, dateTimeString);
	}
	
	/**
	 * 解决日期字符串
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date parseStr(String dateStr , String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Date resultDate = null;
		try {
			resultDate = df.parse(dateStr);
		} catch (ParseException e) {
			logger.error("日期解析错误----,dateStr:"+dateStr,e);
			Date lastDate = DateTimeUtils.parseStr("1990-01-01 00:00:00");
			resultDate = lastDate;
		}
		return resultDate;
	}
	
	/**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if (date == null || pattern == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		String result = df.format(date);
		if(result.equalsIgnoreCase("0001-01-01 00:00:00")){
			result = "";
		}
		return result;
	}
	
	 /**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date){
		   return format(date, dateTimeString);
	  }
	
	/**
	 * 两date比较
	 * 
	 * @param beforeDate
	 * @param afterDate
	 * @return
	 */
	public static int compareDate(Date beforeDate, Date afterDate) {
		Calendar beforeCalendar = Calendar.getInstance();
		Calendar afterCalendar = Calendar.getInstance();
		beforeCalendar.setTime(beforeDate);
		afterCalendar.setTime(afterDate);
		return beforeCalendar.compareTo(afterCalendar);
	}
	
	/**
	 * 判断目标日期是否在时间段类
	 * @param beforeDate
	 * @param afterDate
	 * @param targetDate
	 * @return
	 */
	public static boolean isBetweenDate(Date beforeDate, Date afterDate,Date targetDate){
		if(targetDate == null){
			throw new RuntimeException("targetDate should not be null!");
		}
		if(beforeDate == null && afterDate == null){
			return false;
		}
		if(afterDate == null){
			return (compareDate(beforeDate,targetDate) <= 0);
		}
		if(beforeDate == null){
			return (compareDate(targetDate,afterDate)<= 0);
		}
		return (compareDate(beforeDate,targetDate) <= 0)&&(compareDate(targetDate,afterDate)<= 0);
	}
	/**
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date dateOperateByMonth(Date date,int month){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	public static Date dateOperateByDay(Date date,int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
	
	/**
	 * 邮箱激活，时间操作，对验证时间+24H
	 * @param date 需要操作的时间，如果为null,就去当前时间
	 * @param hour 小时，对操作时间增加或减少的量
	 * @author chj
	 */
	public static Date dateOperateByHour(Date date,int hour){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date == null ? new Date() : date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		return calendar.getTime();
	}
	
	/**
	 * 验证手机验证码过期，时间操作，对验证时间+有效时间
	 * @param date 需要操作的时间，如果为null,就去当前时间
	 * @param minute 分钟，对操作时间增加或减少的量
	 * @author chj
	 */
	public static Date dateOperateByMinute(Date date,int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date == null ? new Date() : date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}
	
	public static Date dateOperateBySecond(Date date,int second){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}
	
	public static Date getDateWithoutTime(Date date){
		Date result = null;
		String dateStr = format(date,dateString);
		result = parseStr(dateStr, dateString);
		return result;
	}
	
	/**
	 * 增加时间
	 * 
	 * @param interval
	 *            [INTERVAL_DAY,INTERVAL_WEEK,INTERVAL_MONTH,INTERVAL_YEAR,
	 *            INTERVAL_HOUR,INTERVAL_MINUTE]
	 * @param date
	 * @param n
	 *            可以为负数
	 * @return
	 */
	public static Date dateAdd(int interval, Date date, int n) {
		long time = (date.getTime() / 1000); // 单位秒
		switch (interval) {
		case INTERVAL_DAY:
			time = time + n * 86400;// 60 * 60 * 24;
			break;
		case INTERVAL_WEEK:
			time = time + n * 604800;// 60 * 60 * 24 * 7;
			break;
		case INTERVAL_MONTH:
			time = time + n * 2678400;// 60 * 60 * 24 * 31;
			break;
		case INTERVAL_YEAR:
			time = time + n * 31536000;// 60 * 60 * 24 * 365;
			break;
		case INTERVAL_HOUR:
			time = time + n * 3600;// 60 * 60 ;
			break;
		case INTERVAL_MINUTE:
			time = time + n * 60;
			break;
		case INTERVAL_SECOND:
			time = time + n;
			break;
		default:
		}

		Date result = new Date();
		result.setTime(time * 1000);
		return result;
	}

	/**
	 * 计算两个时间间隔
	 * 
	 * @param interval
	 *            [INTERVAL_DAY,INTERVAL_WEEK,INTERVAL_MONTH,INTERVAL_YEAR,
	 *            INTERVAL_HOUR,INTERVAL_MINUTE]
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int dateDiff(int interval, Date begin, Date end) {
		long beginTime = (begin.getTime() / 1000); // 单位：秒
		long endTime = (end.getTime() / 1000); // 单位: 秒
		long tmp = 0;
		if (endTime == beginTime) {
			return 0;
		}

		// 确定endTime 大于 beginTime 结束时间秒数 大于 开始时间秒数
		if (endTime < beginTime) {
			tmp = beginTime;
			beginTime = endTime;
			endTime = tmp;
		}

		long intervalTime = endTime - beginTime;
		long result = 0;
		switch (interval) {
		case INTERVAL_DAY:
			result = intervalTime / 86400;// 60 * 60 * 24;
			break;
		case INTERVAL_WEEK:
			result = intervalTime / 604800;// 60 * 60 * 24 * 7;
			break;
		case INTERVAL_MONTH:
			result = intervalTime / 2678400;// 60 * 60 * 24 * 31;
			break;
		case INTERVAL_YEAR:
			result = intervalTime / 31536000;// 60 * 60 * 24 * 365;
			break;
		case INTERVAL_HOUR:
			result = intervalTime / 3600;// 60 * 60 ;
			break;
		case INTERVAL_MINUTE:
			result = intervalTime / 60;
			break;
		case INTERVAL_SECOND:
			result = intervalTime / 1;
			break;
		default:
		}

		// 做过交换
		if (tmp > 0) {
			result = 0 - result;
		}
		return (int) result;
	}
	
	//输入日期，返回星期 如 星期一
	public static String getChDateStr(Date date){
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
       return weekDays[w];
	}
	
	//mysql time to time  2014-04-15 13:17:22.0 to  2014-04-15 13:17:22
	public static String getMySqlTimeStrToJSONTimeStr(String dateTime){
		int len=dateTime.length();
		if(len==21){
			return dateTime.substring(0,19);
		}else{
			return dateTime;
		}
	}
	
	//获取Solr的日期格式
	/*
		* 2014-06-18 10:51:05  转化为 2014-06-18T02:51:05Z
	*/
	public static String getSolrDateStr(Date date){
		//由于中国在8区，需要减去8小时再转换。
		date=dateOperateByHour(date,-8);
		//转化为 yyyy-MM-ddTHH:mm:ssZ格式
		String pattern="yyyy-MM-dd HH:mm:ss";
		String dateStr= format(date,pattern);
		dateStr=dateStr.replace(" ","T");
		return dateStr+"Z";
	}
	
	/*
	 * mysql 的日期字符串转化为日期
	 * @param:
	 *		String	mysql得到的日期字符串
	 * @return：
	 * 		Date	
	 */
	public static Date getDateFromMySqlTime(String dateTime){
		int len=dateTime.length();
		String dateStr="";
		if(len==21){
			dateStr=dateTime.substring(0,19);
		}else{
			dateStr=dateTime;
		}
		return parseStr(dateStr ,dateTimeString);
	}
	
	/*
	 * 日期显示，对于特殊日期  2001-01-01 00:00:00  2099-12-31 23:59:59 采取返回 空处理
	 * 
	 * @param Date 日期
	 * @param String 日期格式字符串
	 * @return string 日期字符串
	 * 		
	 */
	public static String getSeiDateStr(Date date,String dateFormatStr){
		String defaultStartDateStr="2001-01-01 00:00:0";
		String defaultEndDateStr="2099-12-31 23:59:59";
		Date defaultStartDate=parseStr(defaultStartDateStr,dateTimeString);
		Date defaultEndDate=parseStr(defaultEndDateStr,dateTimeString);
		
		if(date.equals(defaultStartDate) || date.equals(defaultEndDate)){
			return "";
		}
		
		return format(date,dateFormatStr);
	}
	
	/**
	 * 根据年月格式的字符 返回日期
	 * 201401 type= start  返回 20140101 00:00:00 表示的日期
	 * 201402 type= end  	返回 20140228 23:59:59 表示的日期
	 * @param YMStr
	 * @param type
	 * @return
	 */
	
	public static Date getDateByYMStr(String YMStr,String type){
		
		if(!type.equals("start") && !type.equals("end")){
			return new Date();
		}
		String dateStr=YMStr;
		if(type.equals("start")){
			dateStr+="01 00:00:00";
			return parseStr(dateStr,"yyyyMMdd HH:mm:ss");
		}
		
		if(type.equals("end")){
			//根据月份获取该月最后一天
			String monthStr=YMStr.substring(4,6);
			if(monthStr.equals("01") 
					||monthStr.equals("03")
					||monthStr.equals("05")
					||monthStr.equals("07")
					||monthStr.equals("08")
					||monthStr.equals("10")
					||monthStr.equals("12")){
				dateStr+="31 23:59:59";
			}else{
				if(monthStr.equals("04")
						||monthStr.equals("06")
						||monthStr.equals("09")
						||monthStr.equals("11")){
					dateStr+="30 23:59:59";
				}else{
					//不考虑闰年的问题
					dateStr+="28 23:59:59";
				}
				
			}
			return parseStr(dateStr,"yyyyMMdd HH:mm:ss");
		}
		
		return new Date();
		
	}
	
}
