package com.hk.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 身份证核实类
 * @author Jesse Lu
 */
public class IDCardVerify {
    
    /**
     * 验证身份证是否有效
     * @param card
     * @return 如果是有效身份证，则返回true，否则返回false
     * @author Liu Shiyu
     */
    public static boolean convertCardID(String card) {
        if (null == card) return false;
        card = card.trim().toUpperCase();
        int[] xx = {2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7};
        char[] yy = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int mm = 0;
        if (card.length() == 15) {
            if (getBirthday(card))
                return true;
            else
                return false;
        } else if (card.length() == 18) {
            int[] gg = new int[18];
            for (int i = 1; i < 18; i++) {
                int j = 17 - i;
                gg[i - 1] = Integer.parseInt(card.substring(j, j + 1));
            }
            for (int i = 0; i < 17; i++) {
                mm += xx[i] * gg[i];
            }
            mm = mm % 11;
            char c = card.charAt(17);
            if (c == yy[mm] && getNewBirthday(card))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }
    
    /**
     * 15位身份证转18位身份证号码
     * @param id 15位身份证号码
     * @return 如果参数不符合要求则返回null
     * @author Jesse Lu
     */
    public static String convertId15to18(String id) {
        if (null == id || id.equals("") || id.length() != 15) return null;
        id = id.trim().toUpperCase();
        int[] W = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
        String[] A = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int i, j, s = 0;
        String newid = id.substring(0, 6) + "19" + id.substring(6, id.length());
        for (i = 0; i < newid.length(); i++) {
            j = Integer.parseInt(newid.substring(i, i + 1)) * W[i];
            s += j;
        }
        s = s % 11;
        newid += A[s];
        return newid;
    }
    
    /**
     * 判断新身份证的出身日期是否有效
     * @param card
     * @return 如果有效则返回true，否则返回false
     * @author Liu Shiyu
     */
    public static boolean getNewBirthday(String card) {
        int y = Integer.parseInt(card.substring(6, 10));
        int m = Integer.parseInt(card.substring(10, 12));
        int d = Integer.parseInt(card.substring(12, 14));
        if (y < 1900 || m < 1 || m > 12 || d < 1 || d > 31 || ((m == 4 || m == 6 || m == 9 || m == 11) && d > 30) || (m == 2 && ((y % 4 > 0 && d > 28) || d > 29))) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 判断旧身份证的出身日期是否有效
     * @param card
     * @return 如果有效则返回true，否则返回false
     * @author Liu Shiyu
     */
    public static boolean getBirthday(String card) {
        int y = Integer.parseInt(card.substring(6, 8));
        int m = Integer.parseInt(card.substring(8, 10));
        int d = Integer.parseInt(card.substring(10, 12));
        if (m < 1 || m > 12 || d < 1 || d > 31 || ((m == 4 || m == 6 || m == 9 || m == 11) && d > 30) || (m == 2 && (((y + 1900) % 4 > 0 && d > 28) || d > 29))) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 从身份证中获得相关用户信息
     * @param id 身份证
     * @return <b>String[]</b> 长度为3，对应长度，内容分别为性别，生日和籍贯。
     *         若身份证号码错误则返回一个包含三个零字节字符串的数组
     * @author Jesse Lu
     */
    public static String[] getInfoFromId(String id) {
        if (!convertCardID(id)) return new String[]{"", "", ""};
        if (id.length() == 15) id = convertId15to18(id);
        String[] info = new String[3];
        info[0] = Integer.valueOf(((Character) id.charAt(16)).toString()) % 2 == 0 ? "女" : "男";
        info[1] = id.substring(6, 10) + "-" + id.substring(10, 12) + "-" + id.substring(12, 14);
        if (id.substring(0, 4).equals("4403")) {
            info[2] = "深圳";
        } else if (id.substring(0, 4).equals("5102")) {
            info[2] = "重庆";
        } else {
            Map<String, String> nativeList = new HashMap<String, String>();
            nativeList.put("11", "北京");
            nativeList.put("12", "天津");
            nativeList.put("13", "河北");
            nativeList.put("14", "山西");
            nativeList.put("15", "内蒙古");
            nativeList.put("21", "辽宁");
            nativeList.put("22", "吉林");
            nativeList.put("23", "黑龙江");
            nativeList.put("31", "上海");
            nativeList.put("32", "江苏");
            nativeList.put("33", "浙江");
            nativeList.put("34", "安徽");
            nativeList.put("35", "福建");
            nativeList.put("36", "江西");
            nativeList.put("37", "山东");
            nativeList.put("41", "河南");
            nativeList.put("42", "湖北");
            nativeList.put("43", "湖南");
            nativeList.put("44", "广东");
            nativeList.put("45", "广西");
            nativeList.put("46", "海南");
            nativeList.put("50", "重庆");
            nativeList.put("51", "四川");
            nativeList.put("52", "贵州");
            nativeList.put("53", "云南");
            nativeList.put("54", "西藏");
            nativeList.put("61", "陕西");
            nativeList.put("62", "甘肃");
            nativeList.put("63", "青海");
            nativeList.put("64", "宁夏");
            nativeList.put("65", "新疆");
            nativeList.put("71", "台湾");
            nativeList.put("81", "香港");
            nativeList.put("82", "澳门");
            info[2] = nativeList.get(id.substring(0, 2));
        }
        return info;
    }
}
