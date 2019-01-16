package com.zengtengpeng.generator.utils;

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

    public static Boolean isEmpty(String str) {
        if(str==null || str.length()<1){
            return true;
        }
        return false;
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
     * @return
     */
    public static String firstUpperCase_(String name) {
        String[] s = name.split("_");
        StringBuffer stringBuffer = new StringBuffer();
        for (String s1 : s) {
            stringBuffer.append(s1.substring(0, 1).toUpperCase() + s1.substring(1));
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
