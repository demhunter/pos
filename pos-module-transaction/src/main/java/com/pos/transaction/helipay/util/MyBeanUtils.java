package com.pos.transaction.helipay.util;

import org.apache.commons.beanutils.BeanUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by heli50 on 2017/4/14.
 */
public class MyBeanUtils extends BeanUtils{
    public static Map convertBean(Object bean, Map retMap)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }
        for (Field f : fields) {
            String key = f.toString().substring(f.toString().lastIndexOf(".") + 1);
            Object value = f.get(bean);
            if(value == null)
                value = "";
            retMap.put(key, value.toString());
        }
        return retMap;
    }

    public static String getSigned(Map<String, String> map, String[] excludes,String split,String signKey){
        StringBuffer sb = new StringBuffer();
        Set<String> excludeSet = new HashSet<String>();
        excludeSet.add("sign");
        if(excludes != null){
            for(String exclude : excludes){
                excludeSet.add(exclude);
            }
        }
        for(String key : map.keySet()){
            if(!excludeSet.contains(key)){
                String value = map.get(key);
                value = (value == null ? "" : value);
                sb.append(split);
                sb.append(value);
            }
        }
        sb.append(split);
        sb.append(signKey);
        return sb.toString();
    }

    public static String getSigned(Object bean, String[] excludes,String split,String signKey) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map<String,String> map  = convertBean(bean, new LinkedHashMap());
        String signedStr = getSigned(map, excludes,split,signKey);
        return signedStr;
    }

}
