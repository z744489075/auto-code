package com.zengtengpeng.mybatisUtils;

public class MybatisGlobalUtils {
	/**
	 * 首字母小写
	 * @param name
	 * @return
	 */
	public static String firstLowerCase(String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
       return  name;
      
    }
	
	/**
	 * 首字母大写
	 * @param name
	 * @return
	 */
	public static String firstUpperCase(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
       return  name;
    }
	/**
	 * 将下划线转化为大写
	 * @param name
	 * @return
	 */
	public static String firstUpperCase_(String name) {
		String[] s = name.split("_");
		StringBuffer stringBuffer=new StringBuffer();
		for (String s1 : s) {
			stringBuffer.append( s1.substring(0, 1).toUpperCase() + s1.substring(1));
		}
       return  stringBuffer.toString();
    }
}
