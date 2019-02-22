package com.zengtengpeng.jdbc.utils;

import com.zengtengpeng.generator.utils.MyStringUtils;
import com.zengtengpeng.jdbc.bean.Bean;
import com.zengtengpeng.jdbc.bean.BeanColumn;
import com.zengtengpeng.autoCode.config.DatasourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbc工具类
 */
public class JDBCUtils {


   static Logger logger = LoggerFactory.getLogger(JDBCUtils.class);
    /**
     * 获取jdbc连接
     * @param datasourceConfig
     * @return
     */
    public static Connection getConnection(DatasourceConfig datasourceConfig){
        try {
            Class.forName(datasourceConfig.getDriverClassName());
            return DriverManager.getConnection(datasourceConfig.getUrl(),datasourceConfig.getUsername(),datasourceConfig.getPassword());
        }catch (Exception e) {
           throw new RuntimeException("获取jdbc连接异常",e);
        }
    }

    /**
     * 获取table相关数据
     * @return
     */
    public static Bean getTable(Connection connection, Bean bean){
        try {

            DatabaseMetaData metaData = connection.getMetaData();
            String tableName = bean.getDataName();
            ResultSet tables = metaData.getTables(null, "%", tableName, new String[]{"TABLE"});
            if(tables.next()){

                //获取表名
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SHOW CREATE TABLE " + tableName);
                if (rs != null && rs.next()) {
                    String createDDL = rs.getString(2);
                    String comment = parse(createDDL);
                    logger.info("表注释->{}",comment);
                    bean.setTableRemarks(comment);
                }

                //获取主键

                ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
                Map<String,String> pk=new HashMap<>();
                if(primaryKeys.next()){
                    String column_name = primaryKeys.getString("COLUMN_NAME");
                    pk.put(column_name,column_name);
                }

                //获取列名
                List<BeanColumn> beans=new ArrayList<>();
                List<BeanColumn> keys=new ArrayList<>();
               /*ResultSet resultSet = statement.executeQuery("show full columns from " + tableName);
                while (rs.next()) {
//			    System.out.println("字段名称：" + rs.getString("Field") + "\t"+ "字段注释：" + rs.getString("Comment") );
                    BeanColumn beanColumn=new BeanColumn();
                    beanColumn.setDefaultValue(resultSet.getString("Default"));
                    beanColumn.setJdbcName(resultSet.getString("Field"));
                    beanColumn.setJdbcType(resultSet.getString("Type").split("\\(")[0]);

                    System.out.println(resultSet.getString("Field") + "\t:\t" + resultSet.getString("Comment"));
                }*/
                ResultSet colRet = metaData.getColumns(null,"%", tableName,"%");
                while(colRet.next()) {
                    BeanColumn beanColumn=new BeanColumn();
                    beanColumn.setJdbcName(colRet.getString("COLUMN_NAME"));
                    beanColumn.setJdbcType(colRet.getString("DATA_TYPE"));
                    if(!MyStringUtils.isEmpty(colRet.getString("IS_NULLABLE")) && colRet.getString("IS_NULLABLE").equalsIgnoreCase("yes")){
                        beanColumn.setNullable(true);
                    }
                    beanColumn.setLength(colRet.getString("COLUMN_SIZE"));
                    if(!MyStringUtils.isEmpty(colRet.getString("IS_AUTOINCREMENT")) && colRet.getString("IS_AUTOINCREMENT").equalsIgnoreCase("yes")){
                        beanColumn.setIdentity(true);
                    }
                    beanColumn.setRemarks(colRet.getString("REMARKS"));
                    beanColumn.setDefaultValue(colRet.getString("COLUMN_DEF"));

                    if(pk.get(beanColumn.getJdbcName())!=null){
                        beanColumn.setKey(true);
                        keys.add(beanColumn);
                    }
                    beans.add(beanColumn);
                }
                bean.setPrimaryKey(keys);
                bean.setAllColumns(beans);
                return  bean;
            }else {
                logger.info("数据库表不存在->{}", tableName);
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 返回表注释信息
     *
     * @param all
     * @return
     */

    private static String parse(String all) {
        String comment = null;
        int index = all.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = all.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        return comment;
    }

}
