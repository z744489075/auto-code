package com.zengtengpeng.common.utils;

import com.zengtengpeng.autoCode.utils.MyStringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * excel 解析工具类 2015年7月7日
 *
 * @author zengtp
 */
public class ExcelUtils {

	/*public static void main(String[] args) {
        List<String> saxNewDiamondExcel = ExcelUtils.saxNewDiamondExcel("d:/1.xlsx");
		System.out.println(saxNewDiamondExcel);
	}*/


    /**
     * 工具类
     *
     * @param title   标题
     * @param headers 头文件必须是map集合
     * @param dataset 内容 可以是bean也可以是map，如果是bean,bean字段必须和headers里面的键相匹配。
     *                如果是map则key必须和headers里面的键相匹配
     * @param out     输出流
     */
    public static <T> void exportExcel(String title, Map<String, String> headers,

                                       List<T> dataset, OutputStream out) {
        try {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            if(MyStringUtils.isEmpty(title)){
                title="data";
            }
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(title);

            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth(15);


            HSSFRow row = sheet.createRow(0);
            // 产生表格标题行
            int j = 0;
            for (Map.Entry<String, String> header : headers.entrySet()) {
                HSSFCell cell = row.createCell(j++);
                HSSFRichTextString text = new HSSFRichTextString(header.getValue());
                cell.setCellValue(text);
            }
            Font font = workbook.createFont();
            font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());    //绿字
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            // 遍历集合数据，产生数据行
            Iterator<T> it = dataset.iterator();
            int index = 0;
            while (it.hasNext()) {
                index++;
                row = sheet.createRow(index);
                T t = (T) it.next();
                if (t instanceof Map) {
                    // 如果泛型为map
                    Map map = (Map) t;
                    int i = 0;
                    Object color = map.get("_color");

                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        //针对整行值设置颜色
                        HSSFCell cell = row.createCell(i++);
                        if (color!=null&&color.equals("red")){
                            cell.setCellStyle(cellStyle);
                        }

                        Object value = map.get(header.getKey());
                        //设置颜色 字段名_color
                        Object o = map.get(header.getKey() + "_color");
                        if(o!=null&&!(Boolean) o){
                            cell.setCellStyle(cellStyle);
                        }

                        judgeType(cell, value);
                    }
                } else {
                    // 利用反射设置值
                    int i = 0;
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        HSSFCell cell = row.createCell(i++);
                        String[] keys = header.getKey().split("\\.");
                        Object value = getDataValue(keys, t, 0);
                        judgeType(cell, value);
                    }
                }
            }
            // 导出excel
            workbook.write(out);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Object getDataValue(String[] keys, Object t, int index) {
        try {
            Method method = t.getClass().getMethod("get" + toUpperCaseFirstOne(keys[index]));
            Object value = method.invoke(t);
            if (value == null) {
                return null;
            } else if (keys.length - 1 == index) {

                if (value instanceof Date) {
                    return DateUtils.formatDateTime((Date) value);
                } else {
                    return value;
                }
            } else {
                return getDataValue(keys, value, ++index);
            }
        } catch (Exception e) {
            throw new RuntimeException("导出异常", e);
        }
    }

    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    static Pattern p = Pattern.compile("^\\d+?$");
    /**
     * 判断值的类型已进行相应的转换
     *
     * @param cell
     * @param value
     */
    private static void judgeType(HSSFCell cell, Object value) {
        if (value == null) {
            return;
        }
        // 如果为string直接设置值
        if (value instanceof String) {
            // 如果匹配为数字也进行转换
            Matcher matcher = p.matcher(value.toString());
            if (matcher.matches()) {
                cell.setCellValue(Double.parseDouble(value.toString()));
            } else {
                cell.setCellValue(value.toString());
            }

        } else if (value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double) {
            cell.setCellValue(Double.parseDouble(value.toString()));
        } else {
            cell.setCellValue(value.toString());
        }
    }



    /**
     * 导出以及设置下载头
     * @param title
     * @param header
     * @param list
     * @param response
     * @param request
     */
    public static void exportExcel(String title, Map<String, String> header, List list, HttpServletResponse response, HttpServletRequest request)  {
        try {
            String fileName = title + DateUtils.formatDateByPattern(new Date(),"yyyyMMddHHmmss") + ".xls";
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName);

            exportExcel(title,header,list,response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
