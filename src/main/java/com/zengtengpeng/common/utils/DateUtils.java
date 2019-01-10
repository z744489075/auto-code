package com.zengtengpeng.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化
 * Created by zengtengpeng on 2016/1/9.
 */
public class DateUtils {

    static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat formatMonth=new SimpleDateFormat("yyyy-MM");
    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        if(date==null){
            return "";
        }
        return dateFormat.format(date);
    }
    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String formatDateTime(Date date){
        if(date==null){
            return "";
        }
        return dateFormat1.format(date);
    }
    /**
     * 格式化月份
     * @param date
     * @return
     */
    public static String formatMonth(Date date){
        return formatMonth.format(date);
    }
    
    public static String formatDateByPattern(Date date,String pattern){
    	SimpleDateFormat dfs=new SimpleDateFormat(pattern);
        return dfs.format(date);
    }
}
