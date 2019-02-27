package com.zengtengpeng.autoCode.utils;

import java.util.ArrayList;

public class MyStringUtils {
    /**
     * 首字母小写
     *
     * @param name
     * @return
     */
    public static String firstLowerCase(String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return name;

    }

    public static Boolean isEmpty(Object str) {
        if(str instanceof String){
            if(str.toString().length() < 1){
                return true;
            }
        }else {
            if(str==null){
                return true;
            }
        }

        return false;
    }

    /**
     * 拼接字符串
     * @param stringBuffer
     * @param text
     * @param param
     */
    public static void append(StringBuffer stringBuffer,String text,String... param){
        stringBuffer.append(String.format(text+"\n",param));
    }
    /**
     * 拼接字符串
     * @param stringBuffer
     * @param text
     * @param param
     * @param tab 制表的数量
     */
    public static void append(StringBuffer stringBuffer,String text,int tab,String... param){
        for (int i = 0; i < tab; i++) {
            stringBuffer.append("\t");
        }
        append(stringBuffer,text,param);
    }

    /**
     * 首字母大写
     *
     * @param name
     * @return
     */
    public static String firstUpperCase(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    /**
     * 将下划线转化为大写
     *
     * @param name
     * @param firstCase 首字母是否大写 true:大写 false;小写
     * @return
     */
    public static String upperCase_(String name, boolean firstCase) {
        if(isEmpty(name)){
            return "";
        }
        String[] s = name.split("_");
        StringBuffer stringBuffer = new StringBuffer();
        for (String s1 : s) {
            stringBuffer.append(s1.substring(0, 1).toUpperCase() + s1.substring(1));
        }
        if(!firstCase){
            return firstLowerCase(stringBuffer.toString());
        }
        return stringBuffer.toString();
    }


    /**
     * 已大写字母为分界点切割字符串
     * @param str
     * @param isLowerCase 首字母是否小写 true 是
     * @return
     */
    public static ArrayList<String> splitByUpperCase(String str,Boolean isLowerCase) {
        ArrayList<String> rs = new ArrayList<>();
        int index = 0;
        int len = str.length();
        for (int i = 1; i < len; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                if(isLowerCase){
                    rs.add( firstLowerCase(str.substring(index, i)));
                }else {
                    rs.add( str.substring(index, i));
                }
                index = i;
            }
        }

        if(isLowerCase){
            rs.add( firstLowerCase(str.substring(index, len)));
        }else {
            rs.add( str.substring(index, len));
        }
        return rs;
    }

}
