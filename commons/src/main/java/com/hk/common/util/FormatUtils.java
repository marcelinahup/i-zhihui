package com.hk.common.util;

import static java.util.Calendar.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 格式化工具包
 * @author IXR
 */
public class FormatUtils {
	
	private static final Log logger = LogFactory.getLog(FormatUtils.class);
    
    /**
     * 将指定格式的日期时间字符串转换为日期
     * @param date 日期字符串
     * @param pattern date对应的格式
     * @return date为null或""，返回null，否则返回转换后的Date
     * @author Jesse Lu
     */
    public static Date toDate(String date, String pattern) {
        if (DataUtils.isNullOrEmpty(date)) return null;
        Date theDate;
        try {
            SimpleDateFormat theFormat = new SimpleDateFormat(pattern);
            theDate = theFormat.parse(date);
        } catch (Exception ex) {
            theDate = null;
        }
        return theDate;
    }
    
    /**
     * 将日期字符串转换为日期
     * @param date 日期字符串<br>
     *            格式须符合 <b>yyyy-MM-dd</b> 或 <b>yyyy/MM/dd</b>
     * @return 一个 Date，时间为00:00:00.0，如果无法分析date，则为 null
     * @author Jesse Lu
     */
    public static Date toDate(String date) {
        String separate = date.substring(4, 5);
        return toDate(date, "yyyy" + separate + "MM" + separate + "dd");
    }
    
    /**
     * 将日期时间字符串转换为日期
     * @param dateTime 日期时间字符串<br>
     *            格式须符合 <b>yyyy-MM-dd hh:mm:ss</b> 或 <b>yyyy/MM/dd hh:mm:ss</b>
     * @return 一个 Date，如果无法分析dateTime，则为 null
     */
    public static Date toDateTime(String dateTime) {
        String separate = dateTime.substring(4, 5);
        return toDate(dateTime, "yyyy" + separate + "MM" + separate + "dd HH:mm:ss");
    }
    
    /**
     * 将日期时间字符串转换为日期
     * @param dateTime 日期时间字符串<br>
     *            格式须符合 <b>yyyy-MM-dd hh:mm:ss</b> 或 <b>yyyy/MM/dd hh:mm:ss</b>
     * @param defaultDate 默认日期
     * @return 一个 Date，如果无法分析dateTime，则为 defaultDate
     * @author Jesse Lu
     */
    public static Date toDateTime(String dateTime, Date defaultDate) {
        String separate = dateTime.substring(4, 5);
        Date theDate = toDate(dateTime, "yyyy" + separate + "MM" + separate + "dd HH:mm:ss");
        return theDate == null ? defaultDate : theDate;
    }
    
    /**
     * 自动分析字符串并尝试转换为日期对象<br>
     * 可转换示例
     * 
     * <pre>
     * 2012-3-21, 2012-03-21
     * 2012/3/21, 2012/03/21
     * 2012.3.21, 2012.03.21
     * 2012-3-21 18:11:43, 2012-03-21 18:11:43
     * 2012-3-21 18:11:43.567, 2012-03-21 18:11:43.567
     * 2012/3/21 18:11:43, 2012/03/21 18:11:43
     * 2012/3/21 18:11:43.567, 2012/03/21 18:11:43.567
     * 2012.3.21 18:11:43, 2012.03.21 18:11:43
     * 2012.3.21 18:11:43.567, 2012.03.21 18:11:43.567
     * Wed Mar 21 18:11:43 CST 2012
     * 21/3/2012, 21/03/2012
     * 20120321
     * 1332324703000
     * </pre>
     * @param dateTime 日期字符串
     * @return 一个 Date，如果无法分析dateTime，则为 null
     * @author Jesse Lu
     */
    public static Date autoDate(String dateTime) {
        if (DataUtils.isNullOrEmpty(dateTime)) return null;
        String[][] patterns = {{"\\d{4}(-\\d{1,2}){2}", "yyyy-M-d", "zh"}, // 2012-03-21, 2012-3-21
                               {"\\d{4}(-\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}", "yyyy-M-d H:m:s", "zh"}, // 2012-03-21 18:11:43
                               {"\\d{4}(-\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}", "yyyy-M-d H:m:s.S", "zh"}, // 2012-03-21 18:11:43.567
                               {"([A-Z][a-z]{2} ){2}\\d{1,2} \\d{1,2}(:\\d{1,2}){2} [A-Z]{3} \\d{4}", "EEE MMM d H:m:s z yyyy", "en"}, // Wed Mar 21 18:11:43 CST 2012
                               {"\\d{4}(/\\d{1,2}){2}", "yyyy/M/d", "zh"}, // 2012/03/21, 2012/3/21
                               {"\\d{4}(/\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}", "yyyy/M/d H:m:s", "zh"}, // 2012/03/21 18:11:43
                               {"\\d{4}(/\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}", "yyyy/M/d H:m:s.S", "zh"}, // 2012/03/21 18:11:43.567
                               {"\\d{4}(\\.\\d{1,2}){2}", "yyyy.M.d", "zh"}, // 2012.03.21, 2012.3.21
                               {"\\d{4}(\\.\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}", "yyyy.M.d H:m:s", "zh"}, // 2012.03.21 18:11:43
                               {"\\d{4}(\\.\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}", "yyyy.M.d H:m:s.S", "zh"}, // 2012.03.21 18:11:43.567
                               {"(\\d{1,2}/){2}\\d{4}", "d/M/yyyy", "zh"}, // 21/03/2012, 21/3/2012
                               {"\\d{8}", "yyyyMMdd", "zh"}, // 20120321
        };
        for (int i = 0; i < patterns.length; i++) {
            if (dateTime.matches(patterns[i][0])) {
                SimpleDateFormat format = new SimpleDateFormat(patterns[i][1], new Locale(patterns[i][2]));
                try {
                    return format.parse(dateTime);
                } catch (ParseException e) {
                    logger.warn("日期格式[" + dateTime + "]自动转换错误");
                    return null;
                }
            }
        }
        if (DataUtils.isInteger(dateTime)) return new Date(Long.valueOf(dateTime)); // 1332324703000
        return null;
    }
    
    /**
     * 日期格式化方法
     * @param date 需要格式化的日期
     * @param pattern 格式化格式
     * @return 如果date为null，则返回空字符串，否则返回pattern格式指定的字符串日期格式
     * @throws IllegalArgumentException pattern为空时抛出
     * @author Jesse Lu
     */
    public static String formatDate(Date date, String pattern) {
        if (pattern == null) throw new IllegalArgumentException("pattern不允许为空");
        if (date == null) return "";
        SimpleDateFormat theFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        return theFormat.format(date);
    }
    
    /**
     * 将日期转换为 <b>yyyy-MM-dd HH:mm:ss</b> 格式的日期时间字符串
     * @param dateTime 需要转换的日期对象
     * @return 如果date为null，则返回""，否则返回格式为 <b>yyyy-MM-dd HH:mm:ss</b> 的日期字符串
     * @author Jesse Lu
     */
    public static String dateTimeString(Date dateTime) {
        return formatDate(dateTime, "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 将日期转换为 <b>yyyy-MM-dd</b> 格式的日期字符串
     * @param date 需要转换的日期对象
     * @return 如果date为null，则返回""，否则返回格式为 <b>yyyy-MM-dd</b> 的日期字符串
     * @author Jesse Lu
     */
    public static String dateString(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }
    
    /**
     * 将日期转换为距离当前多久的时间描述<br>
     * 如：刚刚，昨天，2小时之前等
     * @param date
     * @return 距离当前多久
     * @author Jesse Lu
     */
    public static String prettyDate(Date date) {
        if (date == null) return null;
        Calendar now = Calendar.getInstance(Locale.CHINA);
        Calendar foretime = Calendar.getInstance(Locale.CHINA);
        foretime.setTime(date);
        long diff = now.getTimeInMillis() - foretime.getTimeInMillis();
        if (diff < 0) {
            return "未来";
        } else if (diff < 60000) { // 一分钟之内
            return "刚刚";
        } else if (diff < 3600000) { // 一小时之内
            return diff / 60000 + "分钟前";
        } else if (diff < 86400000) { // 一天之内
            if (now.get(DAY_OF_YEAR) == foretime.get(DAY_OF_YEAR) // 同一天
                || (foretime.get(HOUR_OF_DAY) > 12 && now.get(HOUR_OF_DAY) < 6)) { // 不同天且在昨天12点后，今天6点前
                return diff / 3600000 + "小时前";
            } else {
                return "昨天";
            }
        } else if (diff < 604800000) { // 一周之内
            if (now.get(DAY_OF_WEEK) > foretime.get(DAY_OF_WEEK) // 同一周（含跨年）
                || (foretime.get(DAY_OF_WEEK) > 5 && now.get(DAY_OF_WEEK) < 3)) { // 不同周且在上周后两天，本周前两天内
                int dayOfWeek = now.get(DAY_OF_WEEK) - foretime.get(DAY_OF_WEEK);
                if (dayOfWeek < 0) dayOfWeek += 7; // 不同周时，差额负数补正
                if (dayOfWeek == 1) {
                    return "昨天";
                } else {
                    return (dayOfWeek - 1) + "天前";
                }
            } else {
                return "上周";
            }
        } else if (diff < 2592000000l) { // 一个月之内
            if (now.get(MONTH) == foretime.get(MONTH) // 同一月
                || now.get(WEEK_OF_MONTH) < 3) { // 不同月且在本月前2周内
                int weekOfYear = now.get(WEEK_OF_YEAR) - foretime.get(WEEK_OF_YEAR);
                if (weekOfYear < 0) { // 是否跨年
                    weekOfYear += foretime.getMaximum(WEEK_OF_YEAR); // 跨年时，差额负数补正
                    if (now.get(DAY_OF_YEAR) - now.get(DAY_OF_WEEK) - now.get(WEEK_OF_YEAR) * 7 - 7 < 0) { // 去年最后一周是否跨年，合并跨年造成的多余数
                        weekOfYear -= 1;
                    }
                }
                if (weekOfYear == 1) {
                    return "上周";
                } else {
                    return (weekOfYear - 1) + "周前";
                }
            } else {
                return "上月";
            }
        } else if (diff < 31536000000l) { // 一年之内
            if (now.get(YEAR) == foretime.get(YEAR) // 同一年
                || (foretime.get(MONTH) > 4 && now.get(MONTH) < 3)) { // 不同年且在去年6月后，今年前3个月内
                int monthOfYear = now.get(MONTH) - foretime.get(MONTH);
                if (monthOfYear < 0) { // 是否跨年
                    monthOfYear += foretime.getMaximum(MONTH); // 跨年时，差额负数补正
                }
                if (monthOfYear == 1) {
                    return "上月";
                } else {
                    return (monthOfYear - 1) + "个月前";
                }
            } else {
                return "去年";
            }
        } else { // 一年以前
            int yearOfAge = now.get(YEAR) - foretime.get(YEAR);
            if (yearOfAge == 1) {
                return "去年";
            } else {
                return (yearOfAge - 1) + "年前";
            }
        }
    }
    
    /**
     * 金额大写转换方法
     * @param currency 罗马数字金额
     * @return 中文大写金额
     * @author Jesse Lu
     */
    public static String convertRMB(String currency) {
        if ("".equals(currency)) return "";
        double MAXIMUM_NUMBER = 999999999999.99;
        // 中文转换输出变量
        String CN_ZERO = "零";
        String CN_ONE = "壹";
        String CN_TWO = "贰";
        String CN_THREE = "叁";
        String CN_FOUR = "肆";
        String CN_FIVE = "伍";
        String CN_SIX = "陆";
        String CN_SEVEN = "柒";
        String CN_EIGHT = "捌";
        String CN_NINE = "玖";
        String CN_TEN = "拾";
        String CN_HUNDRED = "佰";
        String CN_THOUSAND = "仟";
        String CN_TEN_THOUSAND = "万";
        String CN_HUNDRED_MILLION = "亿";
        // String CN_SYMBOL = "人民币";
        String CN_DOLLAR = "元";
        String CN_TEN_CENT = "角";
        String CN_CENT = "分";
        String CN_INTEGER = "整";
        String integral; // 整数部分变量
        String decimal; // 小数部分变量
        String outputCharacters; // 输出结果
        String[] parts;
        String[] digits, radices, bigRadices, decimals;
        int zeroCount;
        int i, p;
        String d;
        int quotient, modulus;
        // 金额格式验证
        if (currency.matches("[^,.\\d]")) {
            logger.error("金额中含有非法字符！");
            return "";
        }
        if (!currency.matches("^((\\d{1,3}(,\\d{3})*(.((\\d{3},)*\\d{1,3}))?)|(\\d+(.\\d+)?))$")) {
            logger.error("金额格式错误！");
            return "";
        }
        // 金额格式化
        currency = currency.replaceAll(",", ""); // 移除逗号
        currency = currency.replaceAll("^0+", ""); // 移除前端的"0"字符
        currency = "".equals(currency) ? "0" : currency; // 当currency全部为0时，上一步操作为把值清空
        // Assert the number is not greater than the maximum number.
        if (Double.valueOf(currency) > MAXIMUM_NUMBER) {
            logger.error("金额超出可转换的数额大小！");
            return "";
        }
        // 整数小数分离
        parts = currency.split("\\.");
        if (parts.length > 1) {
            integral = parts[0];
            decimal = parts[1];
            // 小数截断2位
            decimal = decimal.substring(0, decimal.length() > 2 ? 2 : decimal.length());
        } else {
            integral = parts[0];
            decimal = "";
        }
        // 中文转换输出数组定义
        digits = new String[]{CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE};
        radices = new String[]{"", CN_TEN, CN_HUNDRED, CN_THOUSAND};
        bigRadices = new String[]{"", CN_TEN_THOUSAND, CN_HUNDRED_MILLION};
        decimals = new String[]{CN_TEN_CENT, CN_CENT};
        // 开始组合输出字符串
        outputCharacters = "";
        // 组合整数转换
        if (Long.valueOf(integral) > 0) {
            zeroCount = 0;
            for (i = 0; i < integral.length(); i++) {
                p = integral.length() - i - 1;
                d = integral.substring(i, i + 1);
                quotient = p / 4;
                modulus = p % 4;
                if ("0".equals(d)) {
                    zeroCount++;
                } else {
                    if (zeroCount > 0) {
                        outputCharacters += digits[0];
                    }
                    zeroCount = 0;
                    outputCharacters += digits[Integer.valueOf(d)] + radices[modulus];
                }
                if (modulus == 0 && zeroCount < 4) {
                    outputCharacters += bigRadices[quotient];
                }
            }
            outputCharacters += CN_DOLLAR;
        }
        // 组合小数转换
        if (!decimal.matches("^[0]*$")) {
            for (i = 0; i < decimal.length(); i++) {
                d = decimal.substring(i, i + 1);
                if (d != "0") {
                    outputCharacters += digits[Integer.valueOf(d)] + decimals[i];
                }
            }
        }
        // Confirm and return the final output string:
        if ("".equals(outputCharacters)) {
            outputCharacters = CN_ZERO + CN_DOLLAR;
        }
        if (decimal.matches("^[0]*$")) {
            outputCharacters += CN_INTEGER;
        }
        return outputCharacters;
    }
    
    /**
     * 根据秒数转换为时间字符串
     * @param second 秒数
     * @return 字符串时间格式 HHH:mm:ss
     * @author Jesse Lu
     */
    public static String timeString(long second) {
        StringBuilder time = new StringBuilder("");
        int h = (int) second / 60 / 60;
        int m = (int) second / 60 % 60;
        int s = (int) second % 60;
        DecimalFormat d = new DecimalFormat("00");
        if (h > 10) {
            time.append(h + ":");
        } else {
            time.append("0" + h + ":");
        }
        time.append(d.format(m) + ":");
        time.append(d.format(s));
        return time.toString();
    }
    
    /**
     * 将字符串转换成unicode编码，同native2ascii转换效果
     * @param value 原始字符串
     * @return 返回Unicode编码格式的字符串，如：中国 -> \u4e2d\u56fd
     * @author IXR
     */
    public static String toUnicode(String value) {
        StringBuffer buffer = new StringBuffer();
        char[] chars = value.toCharArray();
        for (char ch : chars) {
            if (ch > 128) {
                buffer.append("\\u");
                buffer.append(Integer.toHexString(ch));
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
    
}
