package com.hk.commons.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanUtils {

    private BeanUtils() {
    }

    public static void encapsulate(Object object, List<String> fields, List<Object> values) {
        String[] params = new String[0];
        Object[] valObj = new Object[0];
        params = fields.toArray(params);
        valObj = values.toArray(valObj);

        encapsulate(object, params, valObj);
    }

    public static void encapsulate(Object object, Map<String, Object> paramMap) {
        String[] fields = new String[0];
        Object[] param = new Object[0];

        if (paramMap != null && paramMap.size() > 0) {

            fields = map2KeyArray(paramMap);
            param = map2ValueArray(paramMap, fields);
        }
        encapsulate(object, fields, param);
    }

    /**
     * 对象快�?封装的方�?
     * 
     * @param object
     *            �?��进行属�?封装的对�?
     * @param fields
     *            �?��进行封装的属�?
     * @param values
     *            �?��进行设置的�?
     */

    public static void encapsulate(Object object, String[] fields, Object[] values) {

        int fieldsLen = 0;
        int paramsLen = 0;
        if (fields != null && values != null) {

            fieldsLen = fields.length;
            paramsLen = values.length;

            if (fieldsLen > paramsLen) {
                try {
                    throw new FieldsParamsNotMatchException("values not enough");
                } catch (FieldsParamsNotMatchException e) {
                    e.printStackTrace();
                }
            } else if (fieldsLen < paramsLen) {
                try {
                    throw new FieldsParamsNotMatchException("values too much");
                } catch (FieldsParamsNotMatchException e) {
                    e.printStackTrace();
                }
            } else {

                // 通过反射构建 对象
                Class<?> cl = object.getClass();
                for (int i = 0; i < fields.length; i++) {
                    StringBuffer buffer = new StringBuffer();
                    try {
                        Field field = cl.getDeclaredField(fields[i]);
                        // 得到参数类型
                        Class<?> paramType = field.getType();
                        buffer.append("set");
                        buffer.append(fields[i].substring(0, 1).toUpperCase());
                        buffer.append(fields[i].substring(1));
                        Method method = cl.getDeclaredMethod(buffer.toString(), paramType);
                        method.invoke(object, values[i]);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public static Object[] getValues(Object object,String ... fields){
        // 通过反射构建 对象
        Class<?> cl = object.getClass();
        Object[] values = null;
        if(fields != null && fields.length > 0){
            int len = fields.length ;
            values = new Object[len];
            for (int i = 0; i < len; i++) {
                StringBuffer buffer = new StringBuffer();
                try {
                    buffer.append("get");
                    buffer.append(fields[i].substring(0, 1).toUpperCase());
                    buffer.append(fields[i].substring(1));
                    Method method = cl.getDeclaredMethod(buffer.toString());
                    values[i] = method.invoke(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Method[] methods = cl.getDeclaredMethods();
            int len = methods.length;
            values = new Object[len/2];
            int index = 0;
            for (Method method : methods) {
                if(method.getName().startsWith("get")){
                    try {
                        values[index] = method.invoke(object);
                        index++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return values;
    }
    
    public static String getClassName(Object object){
        
        // 通过反射构建 对象
        Class<?> cl = object.getClass();
        
        return cl.getCanonicalName();
    }
    
    /**
     * 取得对象字段集
     * @param object
     * @return
     */
    public static String[] getFields(Object object){
        // 通过反射构建 对象
        Class<?> cl = object.getClass();
        Field[] clField = cl.getDeclaredFields();
        String[] fields = new String[clField.length];
        
        for (int i = 0; i < clField.length; i++) {
            
            fields[i] = clField[i].getName();
            
        }
        return fields;
    }
    
    private static String[] map2KeyArray(Map<String, Object> params) {

        // 单个值实例化，为下面赋值使用
        String[] fields = {""};
        Set<String> keySet = params.keySet();
        fields = keySet.toArray(fields);

        return fields;
    }

    @SuppressWarnings("unused")
    private static Object[] map2ValueArray(Map<String, Object> paramMap) {
        String[] fields = map2KeyArray(paramMap);

        Object[] param = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            param[i] = paramMap.get(fields[i]);
        }
        return param;
    }

    private static Object[] map2ValueArray(Map<String, Object> paramMap, String[] fields) {
        Object[] param = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            param[i] = paramMap.get(fields[i]);
        }
        return param;
    }

    private static class FieldsParamsNotMatchException extends Exception {
        private static final long serialVersionUID = 505666029393609554L;

        public FieldsParamsNotMatchException(String message) {
            super(message);
        }
    }
}